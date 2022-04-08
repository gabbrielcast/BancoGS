package com.mycompany.bancogs;

import ClasesBanco.CuentaBancaria;
import ClasesBanco.MotivoIngreso;
import ClasesBanco.Persona;
import ClasesBanco.Recibo;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class PrimaryController implements Initializable {

    private Set<Persona> personas = new HashSet<>();
    private Alert alerta;
    private ObservableList<Persona> listaPersonas;
    private ObservableList<MotivoIngreso> listaMotivoIngreso;
    private ObservableList<Double> opcionesDinero;
    private ObservableList<Recibo> observableRecibos;
    private Set<Recibo> recibos;
    CuentaBancaria cuenta;
    private static int contador = 0;
    private static double progreso = 0;
    private static double donacionProg = 0;
    private static int totalDonacion = 0;
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
    private Spinner<Double> cantidadIngreso;
    @FXML
    private ChoiceBox<String> ChoiceMotivoIngreso;
    @FXML
    private Spinner<Double> cantidadRetirada;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargaDatosIniciales();
        autorizadosInciales();
        mostrarDatosCuenta();
        updateViewRecibos();

    }

    private void mostrarDatosCuenta() {
        ncuentaField.setText(cuenta.getNumCuenta() + " ");
        titularField.setText(cuenta.getTitular().toString());
        saldoField.setText(cuenta.getSaldoFormateado());

    }

    private void autorizadosInciales() {

        listaPersonas = FXCollections.observableArrayList(personas);
        viewAutorizados.setItems(listaPersonas);

    }

    private boolean showAlerta(AlertType tipo, String cabecera, String contenido) {
        alerta = new Alert(tipo);
        alerta.setTitle(tipo.name());
        alerta.setHeaderText(cabecera);
        alerta.setContentText(contenido);

        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }

    }

    private Persona getPersonaSeleccionda() {
        int posicionSeleccionado = viewAutorizados.getSelectionModel().getSelectedIndex();

        //ACCEDEMOS A LA OBSERVABLE ARRAYLIST PARA OBTENER LA PERSONA SELECCIONADA
        Persona personaSeleccionada = listaPersonas.get(posicionSeleccionado);

        return personaSeleccionada;
    }

    private void actualizarAutorizados() {
        listaPersonas = FXCollections.observableArrayList(personas);
        viewAutorizados.setItems(listaPersonas);

    }

    private void cargaDatosIniciales() {

        //Titulares de cuentas y personas autorizadas para pruebas
        Persona p5 = new Persona("55555555R", "daniel cano");

        Persona p7 = new Persona("44444444M", "gabriel castillo");

        Persona p9 = new Persona("96666666Z", "wendy aguilar");

        //cuentas bancarias
        cuenta = new CuentaBancaria(123456789, p7);
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

        personas.add(p9);
        personas.add(p5);
        personas.add(p7);

        actualizarAutorizados();

        //ingresos
        cuenta.ingresar(100);

        //retiros
        cuenta.sacar(50);

        //domiciliacion recibos en cuentas
        cuenta.domiciliar("24589652F", "BBVA", 120.0, "LUZ", "Mensual");

        recibos.addAll(cuenta.getAllRecibos());

        //CHOICEBOX
        listaMotivoIngreso = FXCollections.observableArrayList(MotivoIngreso.NOMINA, MotivoIngreso.DONACION, MotivoIngreso.REGALO, MotivoIngreso.OTROS);
        for (int i = 0; i < 4; i++) {
            ChoiceMotivoIngreso.getItems().add(listaMotivoIngreso.get(i).name());
        }
        ChoiceMotivoIngreso.setValue(MotivoIngreso.NOMINA.name());

        //Spinner
        opcionesDinero = FXCollections.observableArrayList(1.0, 5.0, 10.0, 20.0, 50.0, 100.0, 200.0, 500.0, 1000.0, 5000.0);
        SpinnerValueFactory.ListSpinnerValueFactory<Double> ing = new SpinnerValueFactory.ListSpinnerValueFactory(opcionesDinero);
        SpinnerValueFactory.ListSpinnerValueFactory<Double> ret = new SpinnerValueFactory.ListSpinnerValueFactory(opcionesDinero);
        SpinnerValueFactory.ListSpinnerValueFactory<Double> importeRecibo = new SpinnerValueFactory.ListSpinnerValueFactory(opcionesDinero);
        cantidadIngreso.setValueFactory(ing);
        cantidadRetirada.setValueFactory(ret);
        importe.setValueFactory(importeRecibo);
        
        //selected
        periodicidad.selectToggle(pAnual);

    }

    private void setHistorialOperaciones(double cantidad, int op) {
        hora = LocalTime.now();
        DateTimeFormatter horaEstandar = DateTimeFormatter.ofPattern("H:m");
        String operacion = "";
        if (op == 1) {
            operacion = "INGRESO <> " + cantidad + "€ a las " + hora.format(horaEstandar) + "\n";
        } else {
            operacion = "RETIRO <> " + cantidad + "€ a las " + hora.format(horaEstandar) + "\n";
        }
        historialOperaciones.appendText(operacion);

    }

    private void updateViewRecibos() {
        observableRecibos = FXCollections.observableArrayList(recibos);

        historialRecibos.setItems(observableRecibos);

    }

    private String getPeriodicidad() {
        String periodicidad = "Mensual";

        if (pAnual.isSelected()) {
            periodicidad = "Anual";
        }
        if (pTrimestral.isSelected()) {
            periodicidad = "Trimestral";
        }
        return periodicidad;
    }

    private double calcularDonacion(double cantidad) {
        final int PORCENTAJEDONACION = 5;
        double resultado = cantidad * PORCENTAJEDONACION / 100;
        return resultado;
    }

    @FXML
    private void autorizarPersonas() {
        String nombre = nPersonaAutorizar.getText();
        String dni = dniPerAutorizar.getText();
        Persona p;

        if (contador < 6) {
            p = new Persona(dni, nombre);

            //OBTENER LA PERSONA SELECCIONADA
            if (!cuenta.existeAutorizado(p)) {

                p.setNombre(p.getNombre().toUpperCase());
                cuenta.autorizar(p);
                listaPersonas.add(p);

                

                progreso += 0.17;
                progressAutorizados.setProgress(progreso);
                contador++;

            }
        }

    }

    @FXML
    private void desautorizarPersona() {

        //OBTENER LA PERSONA SELECCIONADA
        Persona personaSeleccionada = getPersonaSeleccionda();
        if (progressAutorizados.getProgress() >= 0) {
            if (cuenta.existeAutorizado(personaSeleccionada) && showAlerta(AlertType.CONFIRMATION, "DESAUTORIZAAR",
                    "Estas seguro de eliminar a " + personaSeleccionada.getNombre() + "?")) {

                cuenta.quitarAutorizado(personaSeleccionada);
                listaPersonas.remove(personaSeleccionada);
                

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

        double cantidad = Double.parseDouble(cantidadIngreso.getValue().toString());
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
            setHistorialOperaciones(cantidad, 1);
        }

    }

    @FXML
    private void retirarCantidad(ActionEvent event) {

        double cantidad = Double.parseDouble(cantidadRetirada.getValue().toString());

        boolean ok = true;

        if (cantidad < 0) {
            showAlerta(AlertType.WARNING, "CANTIDAD NEGATIVA", "AVISO: la cantidad es negativa: " + cantidad);
            ok = false;
        }

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
                    donacionProg += 0.1;
                    progressDonaciones.setProgress(donacionProg);
                    
                }else{
                    showAlerta(AlertType.WARNING, "DONACION", "AVISO: No se puede donar mas de 100€: ");
                }

            }

        }

    }

    @FXML
    private void insertarRescibo() {
        String cifEmpresa, nombreEmpresa, conceptoRecibo, periodoRecibo;
        double cantidad;
        try {
            cifEmpresa = cif.getText();
            nombreEmpresa = nEmpresa.getText();
            conceptoRecibo = concepto.getText();
            cantidad = importe.getValue();

            periodoRecibo = getPeriodicidad();

            if (cifEmpresa.matches("^[0-9]{8}[a-zA-Z]{1}$")) {
                try {
                    String domiciliar = cuenta.domiciliar(cifEmpresa, nombreEmpresa, cantidad, conceptoRecibo, periodoRecibo);
                    if (!domiciliar.contains("creado")) {
                        showAlerta(AlertType.INFORMATION, "FALLO RECIBO", domiciliar);

                    }
                    observableRecibos.setAll(cuenta.getAllRecibos());
//                    updateViewRecibos();
                } catch (Exception e) {
                    showAlerta(AlertType.INFORMATION, "FALLO RECIBO", "Periodicidad Nula");
                }

            } else {
                showAlerta(AlertType.WARNING, "FALLO CIF", "El CIF no encaja con el formato");
            }

        } catch (Exception e) {
            showAlerta(AlertType.INFORMATION, "FALLO RECIBO", "Hay Valores Nulos");
        }
        
    }

}
