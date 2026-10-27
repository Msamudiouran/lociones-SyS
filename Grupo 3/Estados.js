const ESTADOS = Object.freeze({
  PENDIENTE: 'PENDIENTE',
  PAGADO: 'PAGADO',
  ENTREGADO: 'ENTREGADO',
  CANCELADO: 'CANCELADO',
});

const descripcion_estado = {
  PENDIENTE: 'pendiente de pago',
  PAGADO: 'pagada',
  ENTREGADO: 'entregada al cliente',
  CANCELADO: 'cancelada',
};

const transiciones_permitidas = {
  PENDIENTE: [ESTADOS.PAGADO, ESTADOS.CANCELADO],
  PAGADO: [ESTADOS.ENTREGADO],
  ENTREGADO: [],
  CANCELADO: [],
};

class Venta {
  constructor(id) {
    this.id = id;
    this.estado = ESTADOS.PENDIENTE;
  }

  puede_pasar_a(nuevo_estado) {
    return transiciones_permitidas[this.estado].includes(nuevo_estado);
  }

  mensaje_error(nuevo_estado) {
    if (this.estado === ESTADOS.CANCELADO) {
      return 'Esta venta ya está cancelada. No se puede cambiar.';
    }
    if (this.estado === ESTADOS.ENTREGADO) {
      return 'Esta venta ya fue entregada. No se puede cambiar.';
    }
    if (nuevo_estado === ESTADOS.CANCELADO) {
      return 'Solo se puede cancelar una venta que aún no está pagada.';
    }
    if (nuevo_estado === ESTADOS.ENTREGADO) {
      return 'Primero debe estar pagada para poder entregarla.';
    }
    return 'Ese cambio no está permitido.';
  }

  cambiar_estado(nuevo_estado) {
    if (!this.puede_pasar_a(nuevo_estado)) {
      throw new Error(this.mensaje_error(nuevo_estado));
    }
    this.estado = nuevo_estado;
  }
}

function probar_cambio(venta, nuevo_estado) {
  console.log(`Intento pasar la venta ${venta.id} a ${nuevo_estado}.`);
  try {
    venta.cambiar_estado(nuevo_estado);
    console.log(`  Listo. La venta quedó ${descripcion_estado[venta.estado]}.`);
  } catch (error) {
    console.log(`  No se pudo: ${error.message}`);
  }
}

console.log('--- Venta 1: camino normal ---');
const venta_uno = new Venta(1);
console.log(`Estado inicial: ${descripcion_estado[venta_uno.estado]}.`);
probar_cambio(venta_uno, ESTADOS.ENTREGADO);
probar_cambio(venta_uno, ESTADOS.PAGADO);
probar_cambio(venta_uno, ESTADOS.CANCELADO);
probar_cambio(venta_uno, ESTADOS.ENTREGADO);
probar_cambio(venta_uno, ESTADOS.PAGADO);

console.log('');
console.log('--- Venta 2: cancelación ---');
const venta_dos = new Venta(2);
console.log(`Estado inicial: ${descripcion_estado[venta_dos.estado]}.`);
probar_cambio(venta_dos, ESTADOS.CANCELADO);
probar_cambio(venta_dos, ESTADOS.PAGADO);