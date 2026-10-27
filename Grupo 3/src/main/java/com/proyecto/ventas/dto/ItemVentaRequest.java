package com.proyecto.ventas.dto;
import java.math.BigDecimal;

public record ItemVentaRequest(
    String tipo_item, 
    String id_referencia, 
    Integer cantidad_item, 
    BigDecimal precio_base,
    BigDecimal descuento_aplicado,
    String mes_cuota 
) {}