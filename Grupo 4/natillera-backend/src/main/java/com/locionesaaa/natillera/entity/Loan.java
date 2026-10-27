package com.locionesaaa.natillera.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="natillera_loans")
public class Loan {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private Membership membership;
    @Column(nullable=false, precision=19, scale=2)
    private BigDecimal principal;
    @Column(nullable=false, precision=5, scale=2)
    private BigDecimal annualInterestPercent;
    @Column(nullable=false)
    private LocalDate requestedAt;
    private LocalDate resolvedAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private LoanStatus status = LoanStatus.PENDING;

    public Long getId() { return id; }
    public void setId(Long id) { this.id=id; }
    public Membership getMembership() { return membership; }
    public void setMembership(Membership membership) { this.membership=membership; }
    public BigDecimal getPrincipal() { return principal; }
    public void setPrincipal(BigDecimal principal) { this.principal=principal; }
    public BigDecimal getAnnualInterestPercent() { return annualInterestPercent; }
    public void setAnnualInterestPercent(BigDecimal annualInterestPercent) { this.annualInterestPercent=annualInterestPercent; }
    public LocalDate getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDate requestedAt) { this.requestedAt=requestedAt; }
    public LocalDate getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDate resolvedAt) { this.resolvedAt=resolvedAt; }
    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status=status; }
}
