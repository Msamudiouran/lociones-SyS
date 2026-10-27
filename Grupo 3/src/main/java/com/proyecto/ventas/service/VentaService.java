package com.proyecto.ventas.service;

import com.proyecto.ventas.dto.CrearVentaRequest;
import com.proyecto.ventas.dto.ItemVentaRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
public class VentaService {

    public String procesar_nueva_venta(CrearVentaRequest peticion_venta) {
        BigDecimal subtotal_acumulado = BigDecimal.ZERO;
        BigDecimal descuentos_acumulados = BigDecimal.ZERO;
        String id_nueva_venta = UUID.randomUUID().toString();

        for (ItemVentaRequest item : peticion_venta.lista_items()) {
            BigDecimal precio_total_linea = item.precio_base().multiply(new BigDecimal(item.cantidad_item()));
            BigDecimal valor_descontado = precio_total_linea
                .multiply(item.descuento_aplicado())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            
            subtotal_acumulado = subtotal_acumulado.add(precio_total_linea);
            descuentos_acumulados = descuentos_acumulados.add(valor_descontado);

            if (item.tipo_item().equals("NATILLERA")) {
                registrar_aporte_natillera(peticion_venta.cliente_id(), item.mes_cuota(), precio_total_linea);
            } else {
                descontar_inventario_comercio(item.id_referencia(), item.cantidad_item());
            }
        }

        BigDecimal total_a_pagar = subtotal_acumulado.subtract(descuentos_acumulados);
        
        return "Venta " + id_nueva_venta + " registrada. Total a pagar: $" + total_a_pagar;
    }

    private void registrar_aporte_natillera(String cliente_id, String mes_cuota, BigDecimal valor) {
        // Implementación futura conectada al Grupo 4
    }

    private void descontar_inventario_comercio(String id_producto, Integer cantidad) {
        // Implementación futura conectada al Grupo 2
    }
}