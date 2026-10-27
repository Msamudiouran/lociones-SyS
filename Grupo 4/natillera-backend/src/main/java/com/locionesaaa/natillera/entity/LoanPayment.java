package com.locionesaaa.natillera.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="natillera_loan_payments")
public class LoanPayment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private Loan loan;
    @Column(nullable=false, precision=19, scale=2)
    private BigDecimal amount;
    @Column(nullable=false)
    private LocalDate paidOn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id=id; }
    public Loan getLoan() { return loan; }
    public void setLoan(Loan loan) { this.loan=loan; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount=amount; }
    public LocalDate getPaidOn() { return paidOn; }
    public void setPaidOn(LocalDate paidOn) { this.paidOn=paidOn; }
}
