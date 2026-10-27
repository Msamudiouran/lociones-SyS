class Ventas {
  constructor() {
    this.ventas = [];
  }

  agregarVenta(cliente, total) {
    const nuevaVenta = {
      id: this.ventas.length + 1,
      cliente: cliente,
      total: total,
      fecha: new Date().toLocaleDateString()
    };
    this.ventas.push(nuevaVenta);
    return `Venta registrada: ${cliente} - $${total}`;
  }

  listarVentas() {
    if (this.ventas.length === 0) {
      return "No hay ventas registradas";
    }
    return this.ventas;
  }

  calcularTotal() {
    let total = 0;
    this.ventas.forEach(v => total += v.total);
    return total;
  }

  
  resumenVoz() {
    const total = this.calcularTotal();
    const texto = `Hay ${this.ventas.length} ventas, con un total de ${total} pesos`;
    console.log(texto);
    
    if (typeof window !== 'undefined' && window.speechSynthesis) {
      const voz = new SpeechSynthesisUtterance(texto);
      window.speechSynthesis.speak(voz);
    }
    return texto;
  }
}

module.exports = Ventas;