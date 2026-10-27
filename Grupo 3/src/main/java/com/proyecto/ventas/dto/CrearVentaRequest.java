package com.proyecto.ventas.dto;
import java.util.List;

public record CrearVentaRequest(
    String cliente_id,
    List<ItemVentaRequest> lista_items
) {}
