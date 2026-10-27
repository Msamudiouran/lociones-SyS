package com.locionesaaa.natillera.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="natillera_liquidations")
public class Liquidation {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @OneToOne(optional=false)
    private Cycle cycle;
    @Column(nullable=false)
    private LocalDateTime performedAt;
    @Column(nullable=false, precision=19, scale=2)
    private BigDecimal totalSavings;
    @Column(nullable=false, precision=19, scale=2)
    private BigDecimal totalInterest;

    public Long getId() { return id; }
    public void setId(Long id) { this.id=id; }
    public Cycle getCycle() { return cycle; }
    public void setCycle(Cycle cycle) { this.cycle=cycle; }
    public LocalDateTime getPerformedAt() { return performedAt; }
    public void setPerformedAt(LocalDateTime performedAt) { this.performedAt=performedAt; }
    public BigDecimal getTotalSavings() { return totalSavings; }
    public void setTotalSavings(BigDecimal totalSavings) { this.totalSavings=totalSavings; }
    public BigDecimal getTotalInterest() { return totalInterest; }
    public void setTotalInterest(BigDecimal totalInterest) { this.totalInterest=totalInterest; }
}
