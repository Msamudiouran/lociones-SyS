package com.lociones.grupo2.clientes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarClientesActivos() {
        return clienteRepository.findByActivoTrue();
    }

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente guardarCliente(Cliente cliente) {
        // Validar Ley 1581 obligatoria
        if (!isAceptaTratamientoDatosValido(cliente)) {
            throw new IllegalArgumentException("Debe aceptar la política de tratamiento de datos (Ley 1581 de 2012).");
        }
        return clienteRepository.save(cliente);
    }

    public Cliente actualizarCliente(Long id, Cliente detallesCliente) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        cliente.setNombre(detallesCliente.getNombre());
        cliente.setCorreo(detallesCliente.getCorreo());
        cliente.setTelefono(detallesCliente.getTelefono());
        
        return clienteRepository.save(cliente);
    }

    // Desactivar en lugar de eliminar (Requerimiento del módulo)
    public void desactivarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    private boolean isAceptaTratamientoDatosValido(Cliente cliente) {
        return cliente.isAceptaTratamientoDatos();
    }
}