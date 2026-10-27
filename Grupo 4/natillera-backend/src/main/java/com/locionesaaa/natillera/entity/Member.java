package com.locionesaaa.natillera.entity;

import jakarta.persistence.*;

@Entity
@Table(name="natillera_members", uniqueConstraints=@UniqueConstraint(columnNames="document"))
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String fullName;
    @Column(nullable=false)
    private String document;
    private String phone;
    @Column(nullable=false)
    private boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
