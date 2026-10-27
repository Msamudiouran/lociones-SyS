package com.locionesaaa.natillera.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="natillera_memberships", uniqueConstraints=@UniqueConstraint(columnNames={"cycle_id","member_id"}))
public class Membership {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private Cycle cycle;
    @JsonIgnore
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private Member member;
    @Column(nullable=false)
    private LocalDate joinedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cycle getCycle() { return cycle; }
    public void setCycle(Cycle cycle) { this.cycle = cycle; }
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    public LocalDate getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDate joinedAt) { this.joinedAt = joinedAt; }
}
