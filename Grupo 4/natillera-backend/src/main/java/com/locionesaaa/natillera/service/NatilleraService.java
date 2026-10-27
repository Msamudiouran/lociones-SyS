package com.locionesaaa.natillera.service;

import com.locionesaaa.natillera.dto.*;
import com.locionesaaa.natillera.entity.*;
import com.locionesaaa.natillera.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class NatilleraService {
    private final CycleRepository cycles;
    private final MemberRepository members;
    private final MembershipRepository memberships;
    private final ContributionRepository contributions;
    private final LoanRepository loans;
    private final LoanPaymentRepository loanPayments;
    private final LiquidationRepository liquidations;

    public NatilleraService(CycleRepository c, MemberRepository m, MembershipRepository ms,
            ContributionRepository cp, LoanRepository l, LoanPaymentRepository lp, LiquidationRepository ld) {
        cycles=c; members=m; memberships=ms; contributions=cp; loans=l; loanPayments=lp; liquidations=ld;
    }

    @Transactional public Cycle createCycle(CreateCycleRequest r) {
        Cycle c=new Cycle(); c.setName(r.name().trim()); c.setOpeningDate(r.openingDate()); c.setFixedContribution(r.fixedContribution());
        return cycles.save(c);
    }
    public List<Cycle> getCycles(){ return cycles.findAll(); }
    public Cycle getCycle(Long id){ return cycle(id); }

    @Transactional public Member createMember(CreateMemberRequest r) {
        if(members.findByDocument(r.document()).isPresent()) throw conflict("Ya existe un socio con ese documento.");
        Member m=new Member(); m.setFullName(r.fullName().trim()); m.setDocument(r.document().trim()); m.setPhone(r.phone());
        return members.save(m);
    }
    public List<Member> getMembers(){ return members.findAll(); }

    @Transactional public Membership joinCycle(Long cycleId, JoinMemberRequest r) {
        Cycle c=openCycle(cycleId); Member m=members.findById(r.memberId()).orElseThrow(() -> notFound("Socio no encontrado."));
        if(!m.isActive()) throw conflict("El socio está inactivo.");
        if(memberships.findByCycleIdAndMemberId(cycleId,m.getId()).isPresent()) throw conflict("El socio ya pertenece al ciclo.");
        Membership x=new Membership(); x.setCycle(c); x.setMember(m); x.setJoinedAt(LocalDate.now()); return memberships.save(x);
    }

    @Transactional public Contribution contribute(Long cycleId, Long membershipId, ContributionRequest r) {
        openCycle(cycleId); Membership m=membership(membershipId,cycleId);
        Contribution c=new Contribution(); c.setMembership(m); c.setAmount(r.amount()); c.setPaidOn(r.paidOn());
        return contributions.save(c);
    }

    @Transactional public Loan requestLoan(Long cycleId, Long membershipId, LoanRequest r) {
        Cycle c=openCycle(cycleId); Membership m=membership(membershipId,cycleId);
        Loan l=new Loan(); l.setMembership(m); l.setPrincipal(r.principal()); l.setAnnualInterestPercent(r.annualInterestPercent()); l.setRequestedAt(c.getOpeningDate().isAfter(LocalDate.now()) ? c.getOpeningDate() : LocalDate.now());
        return loans.save(l);
    }

    @Transactional public Loan decideLoan(Long cycleId, Long loanId, DecisionRequest r) {
        openCycle(cycleId); Loan l=loan(loanId,cycleId);
        if(l.getStatus()!=LoanStatus.PENDING) throw conflict("La solicitud ya fue decidida.");
        if(r.status()!=LoanStatus.APPROVED && r.status()!=LoanStatus.REJECTED) throw badRequest("La decisión debe ser APROBADO o RECHAZADO.");
        l.setStatus(r.status()); l.setResolvedAt(LocalDate.now()); return loans.save(l);
    }

    @Transactional public LoanPayment repayLoan(Long cycleId, Long loanId, LoanPaymentRequest r) {
        openCycle(cycleId); Loan l=loan(loanId,cycleId);
        if(l.getStatus()!=LoanStatus.APPROVED) throw conflict("Solo se puede pagar un préstamo aprobado.");
        BigDecimal total=totalDue(l);
        BigDecimal paid=loanPayments.sumByLoan(l.getId());
        if(r.amount().compareTo(total.subtract(paid))>0) throw badRequest("El pago supera el saldo pendiente.");
        LoanPayment p=new LoanPayment(); p.setLoan(l); p.setAmount(r.amount()); p.setPaidOn(r.paidOn()); p=loanPayments.save(p);
        if(paid.add(r.amount()).compareTo(total)==0) { l.setStatus(LoanStatus.PAID); loans.save(l); }
        return p;
    }

    @Transactional(readOnly=true)
    public AccountSummary account(Long cycleId, Long membershipId) {
        Membership m=membership(membershipId,cycleId); BigDecimal savings=contributions.sumByMembership(m.getId());
        BigDecimal principal=ZERO, interest=ZERO, paid=ZERO;
        for(Loan l:loans.findByMembershipId(m.getId())) if(l.getStatus()==LoanStatus.APPROVED || l.getStatus()==LoanStatus.PAID) {
            principal=principal.add(l.getPrincipal()); interest=interest.add(loanInterest(l)); paid=paid.add(loanPayments.sumByLoan(l.getId()));
        }
        BigDecimal pending=principal.add(interest).subtract(paid).max(ZERO);
        BigDecimal share=liquidations.findByCycleId(cycleId).map(x -> proportionalShare(cycleId,savings,x)).orElse(ZERO);
        return new AccountSummary(m.getMember().getId(),m.getMember().getFullName(),savings,principal,interest,paid,pending,share);
    }

    @Transactional public Liquidation liquidate(Long cycleId) {
        Cycle c=openCycle(cycleId); if(liquidations.findByCycleId(cycleId).isPresent()) throw conflict("El ciclo ya fue liquidado.");
        for(Loan l:loans.findAll()) if(l.getMembership().getCycle().getId().equals(cycleId) && l.getStatus()!=LoanStatus.PAID && l.getStatus()!=LoanStatus.REJECTED)
            throw conflict("No se puede liquidar: hay solicitudes o préstamos pendientes.");
        Liquidation liquid=new Liquidation(); liquid.setCycle(c); liquid.setPerformedAt(LocalDateTime.now());
        BigDecimal savings=ZERO, interest=ZERO;
        for(Membership m:memberships.findByCycleId(cycleId)) savings=savings.add(contributions.sumByMembership(m.getId()));
        for(Loan l:loans.findAll()) if(l.getMembership().getCycle().getId().equals(cycleId) && l.getStatus()==LoanStatus.PAID) interest=interest.add(loanInterest(l));
        liquid.setTotalSavings(savings); liquid.setTotalInterest(interest); c.setStatus(CycleStatus.CLOSED); c.setClosingDate(LocalDate.now()); cycles.save(c);
        return liquidations.save(liquid);
    }

    public String plainTextAccount(Long cycleId, Long membershipId) {
        AccountSummary a=account(cycleId,membershipId);
        return String.format("Estado de cuenta de %s. Aportes: $%s. Préstamos aprobados: $%s. Intereses: $%s. Abonos: $%s. Saldo de préstamos: $%s. Reparto proyectado: $%s.",
            a.memberName(),money(a.contributions()),money(a.approvedLoans()),money(a.interestToPay()),money(a.loanPayments()),money(a.pendingLoanBalance()),money(a.liquidationShare()));
    }

    private BigDecimal proportionalShare(Long cycleId, BigDecimal savings, Liquidation l) { 
        BigDecimal total=ZERO; for(Membership m:memberships.findByCycleId(cycleId)) total=total.add(contributions.sumByMembership(m.getId()));
        if(total.signum()==0) return ZERO;
        return savings.add(savings.multiply(l.getTotalInterest()).divide(total,2,RoundingMode.HALF_UP));
    }
    private BigDecimal totalDue(Loan l){ return l.getPrincipal().add(loanInterest(l)); }
    private BigDecimal loanInterest(Loan l){ return l.getPrincipal().multiply(l.getAnnualInterestPercent()).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP); }
    private String money(BigDecimal n){ return n.setScale(0,RoundingMode.HALF_UP).toPlainString(); }
    private Cycle cycle(Long id){ return cycles.findById(id).orElseThrow(() -> notFound("Ciclo no encontrado.")); }
    private Cycle openCycle(Long id){ Cycle c=cycle(id); if(c.getStatus()!=CycleStatus.OPEN) throw conflict("El ciclo está cerrado."); return c; }
    private Membership membership(Long id,Long cycleId){ return memberships.findById(id).filter(m -> m.getCycle().getId().equals(cycleId)).orElseThrow(() -> notFound("El socio no pertenece a este ciclo.")); }
    private Loan loan(Long id,Long cycleId){ return loans.findById(id).filter(l -> l.getMembership().getCycle().getId().equals(cycleId)).orElseThrow(() -> notFound("Préstamo no encontrado.")); }
    private ResponseStatusException notFound(String m){ return new ResponseStatusException(HttpStatus.NOT_FOUND,m); }
    private ResponseStatusException conflict(String m){ return new ResponseStatusException(HttpStatus.CONFLICT,m); }
    private ResponseStatusException badRequest(String m){ return new ResponseStatusException(HttpStatus.BAD_REQUEST,m); }
    private static final BigDecimal ZERO=BigDecimal.ZERO;
}
