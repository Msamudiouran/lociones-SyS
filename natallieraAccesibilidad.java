package Grupo4;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class natallieraAccesibilidad {

    private String nombre_cliente;
    private double saldo_actual;
    private LocalDate fecha_ultimo_pedido;

    public natallieraAccesibilidad(String nombre_cliente, double saldo_actual, LocalDate fecha_ultimo_pedido) {
        this.nombre_cliente = nombre_cliente;
        this.saldo_actual = saldo_actual;
        this.fecha_ultimo_pedido = fecha_ultimo_pedido;
    }

    public String convertir_monto_hablado(double monto_valor) {
        int monto_entero = (int) monto_valor;
        return monto_entero + " pesos";
    }

    public String convertir_fecha_hablada(LocalDate fecha_objeto) {
        if (fecha_objeto == null) {
            return "fecha no disponible";
        }
        int dia_numero = fecha_objeto.getDayOfMonth();
        String mes_nombre = obtener_nombre_mes(fecha_objeto.getMonthValue());
        
        return dia_numero + " de " + mes_nombre;
    }

    private String obtener_nombre_mes(int numero_mes) {
        String[] meses_nombres = {
            "enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
        };
        return meses_nombres[numero_mes - 1];
    }

    public String generar_resumen_en_texto_plano() {
        String saldo_hablado = convertir_monto_hablado(this.saldo_actual);
        String fecha_hablada = convertir_fecha_hablada(this.fecha_ultimo_pedido);

        return "Resumen para " + this.nombre_cliente + ". " +
               "Su saldo actual es de " + saldo_hablado + ". " +
               "Su último pedido se registró el " + fecha_hablada + ".";
    }

    public static void main(String[] args) {
        natallieraAccesibilidad cliente_prueba = new natallieraAccesibilidad(
            "Carlos", 
            15000.0, 
            LocalDate.of(2026, 11, 20)
        );

        System.out.println(cliente_prueba.generar_resumen_en_texto_plano());
    }
}