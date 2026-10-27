const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database(':memory:');

db.serialize(() => {
    // 1. Productos (Lociones AAA, Ropa, Pijamas)
    db.run(`CREATE TABLE IF NOT EXISTS productos (
        id_producto INTEGER PRIMARY KEY AUTOINCREMENT,
        nombre TEXT NOT NULL,
        categoria TEXT NOT NULL, -- 'Locion_AAA', 'Ropa', 'Pijama'
        precio REAL NOT NULL,
        stock INTEGER NOT NULL
    )`);

    // 2. Transacciones / Ventas Generales
    db.run(`CREATE TABLE IF NOT EXISTS ventas (
        id_venta INTEGER PRIMARY KEY AUTOINCREMENT,
        cliente TEXT NOT NULL,
        subtotal REAL NOT NULL,
        descuento REAL DEFAULT 0,
        total_pagado REAL NOT NULL,
        fecha TEXT NOT NULL
    )`);

    // 3. Detalle de Venta (Normalización 1 a Muchos)
    db.run(`CREATE TABLE IF NOT EXISTS detalle_venta (
        id_detalle INTEGER PRIMARY KEY AUTOINCREMENT,
        id_venta INTEGER,
        id_producto INTEGER,
        cantidad INTEGER NOT NULL,
        precio_unitario REAL NOT NULL,
        FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
        FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
    )`);

    // Datos iniciales de prueba (S&S)
    db.run(`INSERT INTO productos (nombre, categoria, precio, stock) VALUES ('Loción AAA Chanel Bleu 100ml', 'Locion_AAA', 150000, 10)`);
    db.run(`INSERT INTO productos (nombre, categoria, precio, stock) VALUES ('Pijama Térmica Seda Invierno', 'Pijama', 85000, 15)`);
    db.run(`INSERT INTO productos (nombre, categoria, precio, stock) VALUES ('Camibuso Oversize Algodón', 'Ropa', 60000, 20)`);
});

module.exports = db;