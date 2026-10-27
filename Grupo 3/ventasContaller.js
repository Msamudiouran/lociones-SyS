const express = require('express');
const db = require('./db-ventas');
const router = express.Router();

// Middleware de seguridad simulado (verifica token o rol de comercio/caja)
function validarSeguridadComercio(req, res, next) {
    const tokenSeguridad = req.headers['x-token-seguridad'];
    // Clave simétrica o rol básico de prueba para el debate
    if (!tokenSeguridad || tokenSeguridad !== 'seguridad-sys-2026') {
        return res.status(403).json({ 
            error: "Acceso denegado. Falta credencial de seguridad o token de comercio válido." 
        });
    }
    next();
}

/**
 * HISTORIA DE USUARIO: Cálculo de totales, descuentos y comprobante simple.
 * POST /api/ventas/procesar
 */
router.post('/procesar', validarSeguridadComercio, (req, res) => {
    const { cliente, items, codigo_promo, tipo_transaccion } = req.body; 
    // tipo_transaccion: 'COMERCIO_SS' (lociones, ropa, pijamas) o 'NATILLERA'

    if (!items || items.length === 0) {
        return res.status(400).json({ error: "No hay productos o items en la transacción." });
    }

    let subtotalCalculado = 0;
    let itemsProcesados = [];

    // Simulación de consulta síncrona/asíncrona para cálculo de totales
    let contadorQueries = 0;

    items.forEach(item => {
        db.get(`SELECT * FROM productos WHERE id_producto = ?`, [item.id_producto], (err, producto) => {
            if (err || !producto) {
                return res.status(404).json({ error: `Producto ID ${item.id_producto} no encontrado.` });
            }

            const totalItem = producto.precio * item.cantidad;
            subtotalCalculado += totalItem;

            itemsProcesados.push({
                nombre: producto.nombre,
                categoria: producto.categoria,
                cantidad: item.cantidad,
                precio_unitario: producto.precio,
                total_item: totalItem
            });

            contadorQueries++;

            // Cuando terminamos de procesar todos los items del carrito
            if (contadorQueries === items.length) {
                finalizarTransaccion(cliente, subtotalCalculado, codigo_promo, tipo_transaccion, itemsProcesados, res);
            }
        });
    });
});

function finalizarTransaccion(cliente, subtotal, codigo_promo, tipo_transaccion, itemsProcesados, res) {
    let descuentoAplicado = 0;

    if (codigo_promo === 'SYSVIP') {
        descuentoAplicado = subtotal * 0.10; 
    } else if (tipo_transaccion === 'NATILLERA') {
        descuentoAplicado = 0; 
    }

    const totalFinal = subtotal - descuentoAplicado;
    const fechaActual = new Date().toISOString().replace('T', ' ').substring(0, 19);

    const queryVenta = `INSERT INTO ventas (cliente, subtotal, descuento, total_pagado, fecha) VALUES (?, ?, ?, ?, ?)`;
    
    db.run(queryVenta, [cliente, subtotal, descuentoAplicado, totalFinal, fechaActual], function(err) {
        if (err) return res.status(500).json({ error: err.message });

        const idVentaGenerada = this.lastID;

       
        const comprobanteTexto = `
========================================
       COMPROBANTE DE PAGO S & S        
========================================
No. Transacción : ${idVentaGenerada}
Fecha           : ${fechaActual}
Cliente         : ${cliente}
Tipo            : ${tipo_transaccion || 'COMERCIO_SS'}
----------------------------------------
DETALLE DE ARTÍCULOS / CUOTAS:
${itemsProcesados.map(i => `- ${i.nombre} (x${i.cantidad}) : $${i.total_item}`).join('\n')}
----------------------------------------
Subtotal        : $${subtotal}
Descuento       : -$${descuentoAplicado}
TOTAL A PAGAR   : $${totalFinal}
========================================
¡Gracias por preferir S y S & Natillera!
Sistema Accesible Linux - Versión 1.0
========================================
        `.trim();

        res.json({
            mensaje: "Transacción procesada y comprobante generado con éxito.",
            id_venta: idVentaGenerada,
            totales: {
                subtotal,
                descuento: descuentoAplicado,
                total: totalFinal
            },
            comprobante_texto_plano: comprobanteTexto
        });
    });
}

module.exports = router;