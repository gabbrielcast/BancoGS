package com.mycompany.bancogs;

import ClasesBanco.CuentaBancaria;
import ClasesBanco.MotivoIngreso;
import ClasesBanco.Persona;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class PrimaryController implements Initializable {

    private Set<Persona> personas = new HashSet<>();
    private Alert alerta;
    private ObservableList<Persona> listaPersonas;
    private ObservableList<MotivoIngreso> listaMotivoIngreso;
    private ObservableList<Double> opcionesDinero;
    CuentaBancaria cuenta1;
    private static int contador = 0;
    private static double progreso = 0;

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
    private ChoiceBox<String> ChoiceMotivoRetirada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargaDatosIniciales();
        autorizadosInciales();
        mostrarDatosCuenta();
    }

    private void mostrarDatosCuenta() {
        ncuentaField.setText(cuenta1.getNumCuenta() + " ");
        titularField.setText(cuenta1.getTitular().toString());
        saldoField.setText(cuenta1.getSaldoFormateado());

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
        cuenta1 = new CuentaBancaria(123456789, p7);

        //autorizaciones
        cuenta1.autorizar(p7);
        cuenta1.autorizar(p5);
        cuenta1.autorizar(p9);

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
        cuenta1.ingresar(100);

        //retiros
        cuenta1.sacar(50);

        //domiciliacion recibos en cuentas
        cuenta1.domiciliar("1000000A", "EMIVASA", 45.00, "Agua", "MENSUAL");

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
        cantidadIngreso.setValueFactory(ing);
        cantidadRetirada.setValueFactory(ret);

    }

    @FXML
    private void autorizarPersonas() {
        String nombre = nPersonaAutorizar.getText();
        String dni = dniPerAutorizar.getText();
        Persona p;

        if (contador < 6) {
            p = new Persona(dni, nombre);

            //OBTENER LA PERSONA SELECCIONADA
            if (!cuenta1.existeAutorizado(p)) {

                p.setNombre(p.getNombre().toUpperCase());
                cuenta1.autorizar(p);
                personas.add(p);

                actualizarAutorizados();

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
            if (cuenta1.existeAutorizado(personaSeleccionada) && showAlerta(AlertType.CONFIRMATION, "DESAUTORIZAAR",
                    "Estas seguro de eliminar a " + personaSeleccionada.getNombre() + "?")) {

                cuenta1.quitarAutorizado(personaSeleccionada);
                personas.remove(personaSeleccionada);
                actualizarAutorizados();

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
        int resultado = cuenta1.ingresar(cantidad);
        

        if (resultado == 0) {

        }
        if (resultado > 0) {
            showAlerta(AlertType.WARNING, "HACIENDA", "AVISO: NOTIFICAR A HACIENDA por ingreso: " + cantidad);
        }
        if (resultado < 0) {
            showAlerta(AlertType.INFORMATION, "SALDO NEGATIVO", "DEBES INTRODUCIR UN SALDO POSITIVO");

        }
        mostrarDatosCuenta();

    }

    @FXML
    private void retirarCantidad(ActionEvent event) {
    }

}
