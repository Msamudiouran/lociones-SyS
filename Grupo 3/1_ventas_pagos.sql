CREATE TABLE ventas (
    id_venta VARCHAR(36) PRIMARY KEY,
    cliente_id VARCHAR(36) NOT NULL,
    estado_venta VARCHAR(20) NOT NULL, -- PENDIENTE, PAGADO, ENTREGADO, CANCELADO
    fecha_creacion DATETIME2 NOT NULL,
    subtotal_venta DECIMAL(12,2) NOT NULL,
    total_descuentos DECIMAL(12,2) NOT NULL,
    total_final DECIMAL(12,2) NOT NULL
);

CREATE TABLE detalles_venta (
    id_detalle BIGINT IDENTITY(1,1) PRIMARY KEY,
    venta_id VARCHAR(36) NOT NULL,
    tipo_item VARCHAR(20) NOT NULL, -- LOCION, ROPA, NATILLERA
    referencia_item VARCHAR(50) NOT NULL,
    cantidad_comprada INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    porcentaje_descuento DECIMAL(5,2) NOT NULL,
    subtotal_linea DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES ventas(id_venta)
);

CREATE TABLE pagos (
    id_pago VARCHAR(36) PRIMARY KEY,
    venta_id VARCHAR(36) NOT NULL,
    metodo_pago VARCHAR(20) NOT NULL, -- EFECTIVO, TRANSFERENCIA
    monto_abonado DECIMAL(12,2) NOT NULL,
    fecha_pago DATETIME2 NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES ventas(id_venta)
);