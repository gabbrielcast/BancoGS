package ClasesBanco;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CuentaBancaria {

    //COLORES POR CONSOLA
    private final static String GREEN = "\033[32m";
    private final static String RED = "\033[31m";
    private final static String RESET = "\u001B[0m";
    private final static String PURPLE = "\033[35m";

    //VARIABLES DE CONTROL
    private final static int CANTIDAD_MAX = 3000;
    private final static String MENSUAL = "mensual";
    private final static String TRIMESTRAL = "trimestral";
    private final static String ANUAL = "anual";

    //ATRIBUTOS
    private final long numCuenta;
    private Persona titular;
    private double saldo;
   
    private Set<Persona> autorizados = new HashSet<>(); //colección para recoger los autorizados
    private List<Recibo> listaRecibos = new ArrayList<>(); //coleccion para recoger los recibos

    //CONSTRUCTOR
    public CuentaBancaria(long numeroCuenta, Persona titular) {

        this.numCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = 0;

    }

    //GETTERS
    public long getNumCuenta() {
        return numCuenta;
    }

    public Persona getTitular() {
        return titular;
    }

    public boolean autorizar(Persona autorizado) {
        return autorizados.add(autorizado);
    }

    public double getSaldo() {
        return saldo;
    }
    
    public Boolean existeAutorizado(Persona p){
        
        for(Persona per:autorizados){
            if (per.igual(p)) {
                return true;
            }
        }
        return false;
    }
    
    public Boolean quitarAutorizado(Persona p){
        if (autorizados.contains(p)) {
            autorizados.remove(p);
            return true;
        }
        
        return false;
    }

    //SETTERS
    //NO HAY METODOS SET PORQUE NO SE DEBE PERMITIR MODIFICAR DATOS DESPUES DE CREAR EL OBJETO
    
    
    //MÉTODOS
    
    //Se mostrara el toString()de cada objeto Persona que hay en autorizados
    public String verAutorizados() {
        return "Personas autorizadas: " + autorizados;
    }

    //Método getSaldoFormateado: muestra el saldo con un formato determinado del tipo ###.###,##€
    public String getSaldoFormateado() {
        return String.format("%1$,.2f", saldo);
    }

    //Método ingresar
    public int ingresar(double cantidad) {
        
        final int CANTIDAD_CORRECTA = 0;
        final int CANTIDAD_MAX_SUPERADA = 1;
        final int CANTIDAD_INCORRECTA = -1;
        int resultadoIngreso = CANTIDAD_INCORRECTA;

        if (cantidad > 0) {
            if (cantidad <= CANTIDAD_MAX) {
                saldo += cantidad;
                resultadoIngreso = CANTIDAD_CORRECTA;
            } else if (cantidad >= CANTIDAD_MAX) {
                saldo += cantidad;
                resultadoIngreso = CANTIDAD_MAX_SUPERADA;
            }
        }

        return resultadoIngreso;
    }

    //Método retirar dinero
    public double sacar(double cantidad) {
        if (cantidad > 0 && ((saldo - cantidad) >= -50)) {
            saldo -= cantidad;
        }
        return saldo;
    }

    //método informacion general de Cuenta
    public String informacionCuenta() {

        String informacionCuenta = getNumCuenta() + " - " + getTitular();

        if (saldo >= 0) {

            if (verAutorizados().isEmpty()) {
                informacionCuenta += "\nSaldo actual: " + GREEN + getSaldoFormateado() + "€" + RESET;

            } else {

                informacionCuenta += "\n" + verAutorizados() + "\n" + "Saldo actual: "
                        + GREEN + getSaldoFormateado() + "€" + RESET;

            }
        }

        if (saldo < 0) {

            if (verAutorizados().isEmpty()) {

                informacionCuenta += "\nSaldo actual: " + RED + getSaldoFormateado() + "€" + RESET;

            } else {

                informacionCuenta += "\n" + verAutorizados() + "\n" + "Saldo actual: "
                        + RED + getSaldoFormateado() + "€" + RESET;

            }
        }

        return informacionCuenta;
    }

    //Método domiciliar recibos
    public String domiciliar(String cif, String nombreEmpresa, double importe, String concepto, String periodicidad) {

        String reciboDomiciliado = PURPLE + "CIF: " + RESET + cif + PURPLE + "\nNombre de Empresa: " + RESET + nombreEmpresa
                + PURPLE + "\nImporte: " + RESET + String.format("%1$,.2f", importe) + "€" + PURPLE + "\nConcepto: "
                + RESET + concepto + PURPLE + "\nPeriodicidad: " + RESET + periodicidad;

        boolean reciboOK = true;
        String infoRecibo = RED + "No se ha podido domiciliar el recibo" + RESET;

        if (cif.isEmpty()) {
            infoRecibo += "\nEl" + RED + " CIF " + RESET + "no se ha introducido correctamente\nVuelve a intentarlo";
            reciboOK = false;
        }

        if (nombreEmpresa.isEmpty()) {
            infoRecibo += "\nEl" + RED + " NOMBRE DE LA EMPRESA " + RESET + "no se ha introducido correctamente\nVuelve a intentarlo";
            reciboOK = false;
        }

        if (importe <= 0) {
            infoRecibo += "\nEl" + RED + " IMPORTE " + RESET + "no se ha introducido correctamente\nVuelve a intentarlo";
            reciboOK = false;
        }

        if (concepto.isEmpty()) {
            infoRecibo += "\nEl" + RED + " CONCEPTO " + RESET + "no se ha introducido correctamente\nVuelve a intentarlo";
            reciboOK = false;
        }

        if (!(periodicidad.equalsIgnoreCase(MENSUAL) || periodicidad.equalsIgnoreCase(TRIMESTRAL) || periodicidad.equalsIgnoreCase(ANUAL))) {
            infoRecibo += "\nLa" + RED + " PERIODICIDAD " + RESET + "no se ha introducido correctamente\nVuelve a intentarlo";
            reciboOK = false;
        }

        if (reciboOK) {
            Recibo reciboDomiciliadoOK = new Recibo(cif, nombreEmpresa, importe, concepto, periodicidad);
            listaRecibos.add(reciboDomiciliadoOK);
            infoRecibo = GREEN + "\nEl recibo se ha creado correctamente" + RESET + "\n" + reciboDomiciliado;
        }

        return infoRecibo;

    }

    //Método conjunto de recibos domiciliados
    public Set<Recibo> listaRecibosDomicialidos(String periodicidad) {

        Set<Recibo> listaRecibosEncontrados = new HashSet<>();
        Set<Recibo> listaRecibosMensuales = new HashSet<>();
        Set<Recibo> listaRecibosTrimestrales = new HashSet<>();
        Set<Recibo> listaRecibosAnuales = new HashSet<>();

        listaRecibos.forEach(recibos -> {
            if (recibos.getPeriodicidad().equalsIgnoreCase(MENSUAL)) {
                listaRecibosMensuales.add(recibos);

            } else if (recibos.getPeriodicidad().equalsIgnoreCase(TRIMESTRAL)) {
                listaRecibosTrimestrales.add(recibos);

            } else if (recibos.getPeriodicidad().equalsIgnoreCase(ANUAL)) {
                listaRecibosAnuales.add(recibos);
            }
        });

        if (periodicidad.equalsIgnoreCase(MENSUAL)) {
            listaRecibosEncontrados.addAll(listaRecibosMensuales);
        } else if (periodicidad.equalsIgnoreCase(TRIMESTRAL)) {
            listaRecibosEncontrados.addAll(listaRecibosTrimestrales);
        } else if (periodicidad.equalsIgnoreCase(ANUAL)) {
            listaRecibosEncontrados.addAll(listaRecibosAnuales);
        }

        return listaRecibosEncontrados;
    }
    
    
    public Set<Recibo> getAllRecibos(){
        Set<Recibo> allRecibos= new HashSet<>();
        
        for(Recibo bill: listaRecibos){
            allRecibos.add(bill);
        }
        return allRecibos;
    }
}
