package com.locionesaaa.natillera.controller;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<Map<String,String>> handle(ResponseStatusException ex){
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("mensaje", ex.getReason()==null ? "No fue posible completar la operación." : ex.getReason()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String,String>> invalid(MethodArgumentNotValidException ex){
        String message=ex.getBindingResult().getFieldErrors().stream().findFirst().map(e -> "El campo " + e.getField() + " no es válido.").orElse("Los datos no son válidos.");
        return ResponseEntity.badRequest().body(Map.of("mensaje",message));
    }
}
