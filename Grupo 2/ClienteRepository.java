package com.lociones.grupo2.clientes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Para listar solo los clientes activos
    List<Cliente> findByActivoTrue();
}