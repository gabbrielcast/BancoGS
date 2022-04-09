package com.mycompany.bancogs;

import ClasesBanco.CuentaBancaria;
import ClasesBanco.MotivoIngreso;
import ClasesBanco.Persona;
import ClasesBanco.Recibo;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Paint;

public class PrimaryController implements Initializable {

    
    private Alert alerta;
    private ObservableList<Persona> listaPersonas;
    private ObservableList<MotivoIngreso> listaMotivoIngreso;
    private ObservableList<Double> opcionesDinero;
    private ObservableList<Recibo> observableRecibos;
    private Set<Recibo> recibos;
    private CuentaBancaria cuenta;
    
    private static int contador = 0;
    private static double progreso = 0;
    private static double donacionProg = 0;
    private static double totalDonacion = 0;
    private LocalTime hora;

    @FXML
    private ListView<Persona> viewAutorizados;
    @FXML
    private ProgressBar progressAutorizados;
    @FXML
    private Label ncuentaField;
    @FXML
    private Label titularField;
    @FXML
    private Label saldoField;
    @FXML
    private TextField nPersonaAutorizar;
    @FXML
    private TextField dniPerAutorizar;
    @FXML
    private ProgressBar progressDonaciones;    
    @FXML
    private ChoiceBox<String> ChoiceMotivoOperacion;   
    @FXML
    private CheckBox checkBoxDonacion;
    @FXML
    private TextArea historialOperaciones;
    @FXML
    private TextField cif;
    @FXML
    private TextField nEmpresa;
    @FXML
    private Spinner<Double> importe;
    @FXML
    private RadioButton pTrimestral;
    @FXML
    private ToggleGroup periodicidad;
    @FXML
    private RadioButton pMensual;
    @FXML
    private RadioButton pAnual;
    @FXML
    private TextField concepto;
    @FXML
    private ListView<Recibo> historialRecibos;
    @FXML
    private Spinner<Double> spinnerOperacion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargaDatosIniciales();
        mostrarDatosCuenta();
        updateViewRecibos();

    }

    private void mostrarDatosCuenta() {
        
        double saldo=cuenta.getSaldo();
        
        ncuentaField.setText(cuenta.getNumCuenta() + " ");
        titularField.setText(cuenta.getTitular().toString());
        
        
        if (saldo<0) {
            saldoField.setTextFill(Paint.valueOf("#ff2929"));
            
        }else{
            saldoField.setTextFill(Paint.valueOf("#1fde18"));
        }
        
        saldoField.setText(cuenta.getSaldoFormateado() + "€");

    }

    private boolean showAlerta(AlertType tipo, String cabecera, String contenido) {
        alerta = new Alert(tipo);
        alerta.setTitle(tipo.name());
        alerta.setHeaderText(cabecera);
        alerta.setContentText(contenido);

        Optional<ButtonType> result = alerta.showAndWait();

        return result.get() == ButtonType.OK;

    }

    private Persona getPersonaSeleccionada() {
        int posicionSeleccionado = viewAutorizados.getSelectionModel().getSelectedIndex();

        Persona personaSeleccionada = listaPersonas.get(posicionSeleccionado);

        return personaSeleccionada;
    }

    private void cargaDatosIniciales() {

        //Titulares de cuentas y personas autorizadas para pruebas
        Persona p5 = new Persona("55555555R", "daniel cano");
        Persona p7 = new Persona("44444444M", "gabriel castillo");
        Persona p9 = new Persona("96666666Z", "wendy aguilar");
        Persona p8 = new Persona("45103672G", "Gabriel Sajeel");

        //cuentas bancarias
        cuenta = new CuentaBancaria(123456789, p8);
        recibos = new HashSet<>();

        //autorizaciones
        cuenta.autorizar(p7);
        cuenta.autorizar(p5);
        cuenta.autorizar(p9);

        progreso += 0.51;
        progressAutorizados.setProgress(progreso);
        contador += 3;

        p7.setNombre(p7.getNombre().toUpperCase());
        p5.setNombre(p5.getNombre().toUpperCase());
        p9.setNombre(p9.getNombre().toUpperCase());

        listaPersonas = FXCollections.observableArrayList(p9, p5, p7);
        viewAutorizados.setItems(listaPersonas);

        //ingresos
        cuenta.ingresar(100);

        //retiros
        cuenta.sacar(50);

        //domiciliacion recibos en cuentas
        cuenta.domiciliar("24589652F", "BBVA", 120.0, "LUZ", "Mensual");

        recibos.addAll(cuenta.getListaRecibos());

        //CHOICEBOX
        listaMotivoIngreso = FXCollections.observableArrayList(MotivoIngreso.NOMINA, MotivoIngreso.DONACION, MotivoIngreso.REGALO, MotivoIngreso.OTROS);
        for (int i = 0; i < 4; i++) {
            ChoiceMotivoOperacion.getItems().add(listaMotivoIngreso.get(i).name());
        }
        ChoiceMotivoOperacion.setValue(MotivoIngreso.NOMINA.name());

        //Spinner
        opcionesDinero = FXCollections.observableArrayList(1.0, 5.0, 10.0, 20.0, 50.0, 100.0, 200.0, 500.0, 1000.0, 5000.0);
        SpinnerValueFactory.ListSpinnerValueFactory<Double> ing = new SpinnerValueFactory.ListSpinnerValueFactory(opcionesDinero);       
        SpinnerValueFactory.ListSpinnerValueFactory<Double> importeRecibo = new SpinnerValueFactory.ListSpinnerValueFactory(opcionesDinero);
        spinnerOperacion.setValueFactory(ing);
        
        importe.setValueFactory(importeRecibo);

        //selected
        periodicidad.selectToggle(pAnual);

    }

    private void setHistorialOperaciones(double cantidad, int op) {
        hora = LocalTime.now();
        DateTimeFormatter horaEstandar = DateTimeFormatter.ofPattern("H:m");
        String operacion;
        String motivo=ChoiceMotivoOperacion.getValue();
        if (op == 1) {
            operacion = "INGRESO <" + motivo +  "> " + cantidad + "€ a las " + hora.format(horaEstandar) + "h \n";
        } else {           
            operacion = "RETIRO <" + motivo + "> "+ cantidad + "€ a las " + hora.format(horaEstandar) + "h \n";
        }
        historialOperaciones.appendText(operacion);

    }

    private void updateViewRecibos() {
        observableRecibos = FXCollections.observableArrayList(recibos);

        historialRecibos.setItems(observableRecibos);

    }

    private String getPeriodicidad() {
        String valor = "Anual";

        if (pMensual.isSelected()) {
            valor = "Mensual";
        }
        if (pTrimestral.isSelected()) {
            valor = "Trimestral";
        }
        return valor;
    }

    private double calcularDonacion(double cantidad) {
        final int PORCENTAJEDONACION = 5;
        double resultado = (double) cantidad * PORCENTAJEDONACION / 100;
        return resultado;
    }

    @FXML
    private void autorizarPersonas() {
        String nombre = nPersonaAutorizar.getText();
        String dni = dniPerAutorizar.getText();
        Persona p;
        boolean ok = true;

        if (nombre.isBlank()) {
            ok = false;
        }

        if (!dni.matches("^[0-9]{8}[a-zA-Z]{1}$")) {
            ok = false;
        }

        if (contador < 6 && ok) {
            p = new Persona(dni, nombre);

            //OBTENER LA PERSONA SELECCIONADA
            if (!listaPersonas.contains(p)) {

                p.setNombre(p.getNombre().toUpperCase());

                listaPersonas.add(p);

                progreso += 0.17;
                progressAutorizados.setProgress(progreso);
                contador++;

            }
        } else {
            showAlerta(AlertType.ERROR, "ERROR DATOS", "El DNI o el Nombre son incorrectos");

        }

    }

    @FXML
    private void desautorizarPersona() {

        //OBTENER LA PERSONA SELECCIONADA
        Persona desautorizado = getPersonaSeleccionada();
        if (progressAutorizados.getProgress() >= 0) {
            if (listaPersonas.contains(desautorizado) && showAlerta(AlertType.CONFIRMATION, "DESAUTORIZAR",
                    "Estas seguro de eliminar a " + desautorizado.getNombre() + "?")) {

                listaPersonas.remove(desautorizado);

                //progressbar
                progreso -= 0.17;
                progressAutorizados.setProgress(progreso);
                contador--;
            }
        }

    }

    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void ingresarCantidad() {

        double cantidad = Double.parseDouble(spinnerOperacion.getValue().toString());
        int resultado = cuenta.ingresar(cantidad);
        boolean ok = false;

        if (resultado == 0) {
            ok = true;
        }
        if (resultado > 0) {
            showAlerta(AlertType.WARNING, "HACIENDA", "AVISO: NOTIFICAR A HACIENDA por ingreso: " + cantidad);
            ok = true;
        }

        if (ok) {
            mostrarDatosCuenta();
            //1 es para ingresar
            setHistorialOperaciones(cantidad, 1);
        }

    }

    @FXML
    private void retirarCantidad(ActionEvent event) {

        double cantidad = Double.parseDouble(spinnerOperacion.getValue().toString());
        boolean ok = true;

        if (cuenta.getSaldo() - cantidad < -50) {
            showAlerta(AlertType.WARNING, "SALDO", "AVISO: El saldo no puede ser menor a 50: ");
            ok = false;
        }

        if (ok) {
            cuenta.sacar(cantidad);
            mostrarDatosCuenta();
            setHistorialOperaciones(cantidad, 2);

            if (checkBoxDonacion.isSelected()) {

                double donacion = calcularDonacion(cantidad);
                if (totalDonacion + donacion <= 100) {
                    totalDonacion += donacion;
                    donacionProg = (double) totalDonacion / 100;

                    progressDonaciones.setProgress(donacionProg);

                } else {
                    showAlerta(AlertType.WARNING, "DONACION", "AVISO: No se puede donar mas de 100€ en total, has donado " + totalDonacion + "€");
                }

            }
            
            if (cuenta.getSaldo()<0) {
                showAlerta(AlertType.WARNING, "SALDO NEGATIVO", "Tu saldo actual es: " + cuenta.getSaldoFormateado() + "€");
            }

        }

    }

    @FXML
    private void insertarRescibo() {
        String cifEmpresa, nombreEmpresa, conceptoRecibo, periodoRecibo;
        double cantidad;
        boolean ok = true;
        boolean cifOK = true;

        cifEmpresa = cif.getText();
        nombreEmpresa = nEmpresa.getText();
        conceptoRecibo = concepto.getText();
        cantidad = importe.getValue();

        if (nombreEmpresa.isBlank() || conceptoRecibo.isBlank()) {
            ok = false;
        }

        if ((!cifEmpresa.matches("^[0-9]{8}[a-zA-Z]{1}$") || cifEmpresa.isBlank()) && ok) {
            cifOK = false;
            showAlerta(AlertType.WARNING, "FALLO CIF", "El CIF no encaja con el formato");
        }

        if (ok && cifOK) {
            periodoRecibo = getPeriodicidad();

            cuenta.domiciliar(cifEmpresa, nombreEmpresa, cantidad, conceptoRecibo, periodoRecibo);

            observableRecibos.setAll(cuenta.getListaRecibos());

        }

        if (ok == false) {
            showAlerta(AlertType.INFORMATION, "FALLO RECIBO", "Hay Valores Nulos en empresa y concepto");
        }

    }

    @FXML
    private void filtrarRecibos() {

        ChoiceDialog<String> choice = new ChoiceDialog<>("Anual", "Mensual", "Trimestral");
        choice.setTitle("FILTRAR");
        choice.setHeaderText("Filtrar por periodicidad");
        choice.setContentText("Elije una opción");

        Optional<String> result = choice.showAndWait();

        if (result.isPresent()) {
            Set<Recibo> recibosBuscados = cuenta.listaRecibosDomicialidos(result.get());
            if (!recibosBuscados.isEmpty()) {
                observableRecibos.setAll(recibosBuscados);
            } else {
                showAlerta(AlertType.INFORMATION, "Filtro busqueda", "No se ha encontrado ningun recibo");
            }
        }

    }

}
