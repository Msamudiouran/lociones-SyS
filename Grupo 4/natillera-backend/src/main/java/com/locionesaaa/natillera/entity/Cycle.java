package com.locionesaaa.natillera.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "natillera_cycles")
public class Cycle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private LocalDate openingDate;
    private LocalDate closingDate;
    @Column(nullable=false, precision=19, scale=2)
    private BigDecimal fixedContribution;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private CycleStatus status = CycleStatus.OPEN;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }
    public LocalDate getClosingDate() { return closingDate; }
    public void setClosingDate(LocalDate closingDate) { this.closingDate = closingDate; }
    public BigDecimal getFixedContribution() { return fixedContribution; }
    public void setFixedContribution(BigDecimal fixedContribution) { this.fixedContribution = fixedContribution; }
    public CycleStatus getStatus() { return status; }
    public void setStatus(CycleStatus status) { this.status = status; }
}
