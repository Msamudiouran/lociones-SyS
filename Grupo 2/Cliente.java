package com.lociones.grupo2.clientes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes_g2")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    private String telefono;

    // Estado para cumplir con desactivar en vez de borrar físicamente y Ley 1581
    private boolean activo = true;

    // Autorización de tratamiento de datos (Ley 1581 de 2012)
    @Column(nullable = false)
    private boolean aceptaTratamientoDatos;

    private LocalDateTime fechaRegistro = LocalDateTime.now();
}