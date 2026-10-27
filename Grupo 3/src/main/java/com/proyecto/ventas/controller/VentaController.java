package com.proyecto.ventas.controller;

import com.proyecto.ventas.dto.CrearVentaRequest;
import com.proyecto.ventas.service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaService servicio_ventas;

    public VentaController(VentaService servicio_ventas) {
        this.servicio_ventas = servicio_ventas;
    }

    @PostMapping
    public ResponseEntity<String> crear_venta(@RequestBody CrearVentaRequest peticion) {
        String respuesta = servicio_ventas.procesar_nueva_venta(peticion);
        return ResponseEntity.ok(respuesta);
    }
}