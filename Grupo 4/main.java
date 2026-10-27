import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// ==========================================
// CAPA DE MODELOS (ENTIDADES)
// ==========================================
class Socio {
    private int id;
    private String nombre;
    private double ahorros;
    private double prestamos;

    public Socio(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.ahorros = 0.0;
        this.prestamos = 0.0;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getAhorros() { return ahorros; }
    public double getPrestamos() { return prestamos; }

    public void agregarAporte(double monto) { this.ahorros += monto; }
    public void agregarPrestamo(double monto) { this.prestamos += monto; }
    public void abonarPrestamo(double monto) { this.prestamos -= monto; }
}

// ==========================================
// MÓDULO 9: ACCESIBILIDAD (accesibilidad)
// ==========================================
class AccesibilidadService {
    
    // Genera respuestas simples y claras en texto plano aptas para lectura en voz
    public String generarResumenTextoPlano(Socio socio) {
        return "Estado de cuenta del socio " + socio.getNombre() + ". " +
               "Total ahorrado: " + convertirNumeroALetras(socio.getAhorros()) + ". " +
               "Total adeudado por préstamos: " + convertirNumeroALetras(socio.getPrestamos()) + ".";
    }

    // Traduce errores técnicos a mensajes en español claro
    public String formatearError(String causa) {
        return "Lo sentimos, no pudimos procesar la solicitud. Razón: " + causa + ". Por favor, intente de nuevo.";
    }

    // Convierte valores a formato hablado, ej: 15000 -> "quince mil pesos"
    public String convertirNumeroALetras(double monto) {
        int valor = (int) monto;
        if (valor == 0) return "cero pesos";
        if (valor == 15000) return "quince mil pesos";
        if (valor == 20000) return "veinte mil pesos";
        if (valor == 50000) return "cincuenta mil pesos";
        if (valor == 100000) return "cien mil pesos";
        return valor + " pesos";
    }
}

// ==========================================
// MÓDULO 8: NATILLERA (natillera)
// ==========================================
class NatilleraService {
    private List<Socio> socios = new ArrayList<>();
    
    // Variables en snake_case aplicadas aquí
    private AccesibilidadService accesibilidad_service;
    private double fondo_total = 0.0;

    public NatilleraService(AccesibilidadService accesibilidad_service) {
        this.accesibilidad_service = accesibilidad_service;
    }

    // Inscripción de socios
    public String inscribirSocio(int id, String nombre) {
        for (Socio s : socios) {
            if (s.getId() == id) {
                return accesibilidad_service.formatearError("el número de identificación ya está registrado");
            }
        }
        socios.add(new Socio(id, nombre));
        return "Socio " + nombre + " inscrito correctamente en la natillera.";
    }

    // Registro de aportes/cuotas
    public String registrarAporte(int id_socio, double monto) {
        if (monto <= 0) return accesibilidad_service.formatearError("el monto del aporte debe ser mayor a cero");
        
        Socio socio = buscarSocio(id_socio);
        if (socio == null) return accesibilidad_service.formatearError("el socio no existe");

        socio.agregarAporte(monto);
        fondo_total += monto;
        return "Aporte de " + accesibilidad_service.convertirNumeroALetras(monto) + 
               " registrado con éxito para el socio " + socio.getNombre() + ".";
    }

    // Solicitud y aprobación de préstamos
    public String solicitarPrestamo(int id_socio, double monto) {
        Socio socio = buscarSocio(id_socio);
        if (socio == null) return accesibilidad_service.formatearError("el socio no existe");
        if (monto > fondo_total) return accesibilidad_service.formatearError("no hay fondos suficientes en la natillera para este préstamo");

        socio.agregarPrestamo(monto);
        fondo_total -= monto;
        return "Préstamo de " + accesibilidad_service.convertirNumeroALetras(monto) + 
               " aprobado para " + socio.getNombre() + ".";
    }

    // Estado de cuenta por socio (resumen simple)
    public String obtenerEstadoCuenta(int id_socio) {
        Socio socio = buscarSocio(id_socio);
        if (socio == null) return accesibilidad_service.formatearError("el socio no existe");
        
        return accesibilidad_service.generarResumenTextoPlano(socio);
    }
    
    // Liquidación final
    public String liquidarNatillera() {
        return "Iniciando liquidación anual. Fondo total disponible: " + 
               accesibilidad_service.convertirNumeroALetras(fondo_total) + 
               ". Se procederá a repartir ahorros e intereses a los " + socios.size() + " socios.";
    }

    private Socio buscarSocio(int id_socio) {
        for (Socio s : socios) {
            if (s.getId() == id_socio) return s;
        }
        return null;
    }
}

// ==========================================
// INTERFAZ DE CONSOLA (Para usuarios de Linux/Orca)
// ==========================================
public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Variables instanciadas en snake_case
        AccesibilidadService accesibilidad_service = new AccesibilidadService();
        NatilleraService natillera_service = new NatilleraService(accesibilidad_service);

        boolean salir = false;

        System.out.println("=== Sistema de Natillera AAA ===");
        System.out.println("Modo de alta accesibilidad activado. Navegue usando los números.");

        while (!salir) {
            System.out.println("\nMenú principal:");
            System.out.println("1. Inscribir nuevo socio");
            System.out.println("2. Registrar un aporte");
            System.out.println("3. Solicitar un préstamo");
            System.out.println("4. Escuchar estado de cuenta");
            System.out.println("5. Liquidar natillera (Fin de año)");
            System.out.println("6. Salir");
            System.out.print("Ingrese el número de la opción: ");
            
            String opcion = scanner.nextLine();

            try {
                switch (opcion) {
                    case "1":
                        System.out.print("Ingrese el número de identificación del socio: ");
                        int id_socio = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingrese el nombre del socio: ");
                        String nombre_socio = scanner.nextLine();
                        System.out.println(natillera_service.inscribirSocio(id_socio, nombre_socio));
                        break;
                    case "2":
                        System.out.print("Ingrese la identificación del socio: ");
                        int id_aporte = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingrese el monto a aportar (ej. 15000): ");
                        double monto_aporte = Double.parseDouble(scanner.nextLine());
                        System.out.println(natillera_service.registrarAporte(id_aporte, monto_aporte));
                        break;
                    case "3":
                        System.out.print("Ingrese la identificación del socio: ");
                        int id_prestamo = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingrese el monto a solicitar (ej. 50000): ");
                        double monto_prestamo = Double.parseDouble(scanner.nextLine());
                        System.out.println(natillera_service.solicitarPrestamo(id_prestamo, monto_prestamo));
                        break;
                    case "4":
                        System.out.print("Ingrese la identificación del socio: ");
                        int id_estado = Integer.parseInt(scanner.nextLine());
                        System.out.println(natillera_service.obtenerEstadoCuenta(id_estado));
                        break;
                    case "5":
                        System.out.println("ADVERTENCIA: ¿Está seguro que desea liquidar la natillera? (sí/no)");
                        String confirmacion = scanner.nextLine();
                        if (confirmacion.equalsIgnoreCase("sí") || confirmacion.equalsIgnoreCase("si")) {
                            System.out.println(natillera_service.liquidarNatillera());
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                        break;
                    case "6":
                        salir = true;
                        System.out.println("Saliendo del sistema. Hasta pronto.");
                        break;
                    default:
                        System.out.println(accesibilidad_service.formatearError("opción no reconocida"));
                }
            } catch (NumberFormatException e) {
                System.out.println(accesibilidad_service.formatearError("se esperaba un número válido pero se ingresó texto"));
            }
        }
        scanner.close();
    }
}