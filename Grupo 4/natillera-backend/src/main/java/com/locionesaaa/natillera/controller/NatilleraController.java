package com.locionesaaa.natillera.controller;

import com.locionesaaa.natillera.dto.*;
import com.locionesaaa.natillera.entity.*;
import com.locionesaaa.natillera.service.NatilleraService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/natillera")
public class NatilleraController {
    private final NatilleraService service;
    public NatilleraController(NatilleraService service){ this.service=service; }

    @PostMapping("/cycles") public ResponseEntity<Cycle> createCycle(@Valid @RequestBody CreateCycleRequest r){ return ResponseEntity.status(HttpStatus.CREATED).body(service.createCycle(r)); }
    @GetMapping("/cycles") public List<Cycle> cycles(){ return service.getCycles(); }
    @GetMapping("/cycles/{id}") public Cycle cycle(@PathVariable Long id){ return service.getCycle(id); }
    @PostMapping("/cycles/{cycleId}/close") public Liquidation close(@PathVariable Long cycleId){ return service.liquidate(cycleId); }

    @PostMapping("/members") public ResponseEntity<Member> createMember(@Valid @RequestBody CreateMemberRequest r){ return ResponseEntity.status(HttpStatus.CREATED).body(service.createMember(r)); }
    @GetMapping("/members") public List<Member> members(){ return service.getMembers(); }
    @PostMapping("/cycles/{cycleId}/members") public ResponseEntity<Membership> join(@PathVariable Long cycleId,@Valid @RequestBody JoinMemberRequest r){ return ResponseEntity.status(HttpStatus.CREATED).body(service.joinCycle(cycleId,r)); }

    @PostMapping("/cycles/{cycleId}/memberships/{membershipId}/contributions") public ResponseEntity<Contribution> contribute(@PathVariable Long cycleId,@PathVariable Long membershipId,@Valid @RequestBody ContributionRequest r){ return ResponseEntity.status(HttpStatus.CREATED).body(service.contribute(cycleId,membershipId,r)); }
    @PostMapping("/cycles/{cycleId}/memberships/{membershipId}/loans") public ResponseEntity<Loan> requestLoan(@PathVariable Long cycleId,@PathVariable Long membershipId,@Valid @RequestBody LoanRequest r){ return ResponseEntity.status(HttpStatus.CREATED).body(service.requestLoan(cycleId,membershipId,r)); }
    @PatchMapping("/cycles/{cycleId}/loans/{loanId}") public Loan decide(@PathVariable Long cycleId,@PathVariable Long loanId,@Valid @RequestBody DecisionRequest r){ return service.decideLoan(cycleId,loanId,r); }
    @PostMapping("/cycles/{cycleId}/loans/{loanId}/payments") public ResponseEntity<LoanPayment> pay(@PathVariable Long cycleId,@PathVariable Long loanId,@Valid @RequestBody LoanPaymentRequest r){ return ResponseEntity.status(HttpStatus.CREATED).body(service.repayLoan(cycleId,loanId,r)); }
    @GetMapping("/cycles/{cycleId}/memberships/{membershipId}/account") public AccountSummary account(@PathVariable Long cycleId,@PathVariable Long membershipId){ return service.account(cycleId,membershipId); }
    @GetMapping(value="/cycles/{cycleId}/memberships/{membershipId}/account/text", produces=MediaType.TEXT_PLAIN_VALUE)
    public String textAccount(@PathVariable Long cycleId,@PathVariable Long membershipId){ return service.plainTextAccount(cycleId,membershipId); }
}
