package com.mycompany.bancogs;

import ClasesBanco.CuentaBancaria;
import ClasesBanco.Persona;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;

public class PrimaryController implements Initializable {

    private Set<Persona> personas = new HashSet<>();
    private Alert alerta;
    private ObservableList<Persona> listaPersonas;
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

    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargaDatosIniciales();
        autorizadosInciales();
        mostrarDatosCuenta();
    }
    
    
    private void mostrarDatosCuenta(){
        ncuentaField.setText(cuenta1.getNumCuenta() + " ");
        titularField.setText(cuenta1.getTitular().toString());
        saldoField.setText(cuenta1.getSaldoFormateado());
        
        
    }
    
    private void autorizadosInciales() {

        listaPersonas = FXCollections.observableArrayList(personas);
        viewAutorizados.setItems(listaPersonas);

    }

    @FXML
    private void autorizarPersonas() {

        if (contador < 6) {
            //OBTENER LA PERSONA SELECCIONADA
            Persona personaSeleccionada = getPersonaSeleccionda();

            if (!cuenta1.existeAutorizado(personaSeleccionada)) {

                personaSeleccionada.setNombre(personaSeleccionada.getNombre().toUpperCase());
                cuenta1.autorizar(personaSeleccionada);
                personas.add(personaSeleccionada);

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
            if (cuenta1.existeAutorizado(personaSeleccionada) && showAlerta(AlertType.CONFIRMATION, personaSeleccionada)) {

                personaSeleccionada.setNombre(personaSeleccionada.getNombre().toLowerCase());
                cuenta1.quitarAutorizado(personaSeleccionada);
                personas.add(personaSeleccionada);
                actualizarAutorizados();

                progreso -= 0.17;

                progressAutorizados.setProgress(progreso);
                contador--;
            }
        }

    }

    private boolean showAlerta(AlertType tipo, Persona p) {

        if (tipo == AlertType.CONFIRMATION) {
            alerta = new Alert(AlertType.CONFIRMATION);
            alerta.setTitle("CONFIRMACION");
            alerta.setHeaderText("ELIIMINACION DE AUTORIZADOS");
            alerta.setContentText("Estas seguro de que quieres eliminar a: " + p.getNombre() + "?");

            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK) {
                return true;
            } else {
                return false;
            }
        }
        return false;

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
        Persona p1 = new Persona("19103511A", "pau fandos");
        Persona p2 = new Persona("95025322B", "jose luis coloma");
        Persona p3 = new Persona("33333333C", "raquel lópez");

        Persona p4 = new Persona("76423444D", "esteban adarme");
        Persona p5 = new Persona("55555555R", "daniel cano");
        Persona p6 = new Persona("66302666P", "alvaro garcia");

        Persona p7 = new Persona("44444444M", "gabriel castillo");
        Persona p8 = new Persona("56355555X", "sajeel");
        Persona p9 = new Persona("96666666Z", "wendy aguilar");

        personas.add(p1);
        personas.add(p2);
        personas.add(p3);
        personas.add(p4);
        personas.add(p5);
        personas.add(p6);
        personas.add(p7);
        personas.add(p8);
        personas.add(p9);

        //cuentas bancarias
        cuenta1 = new CuentaBancaria(123456789, p7);

        //autorizaciones
        cuenta1.autorizar(p7);
        progreso += 0.17;
        progressAutorizados.setProgress(progreso);
        contador++;

        p7.setNombre(p7.getNombre().toUpperCase());

        //ingresos
        cuenta1.ingresar(100);

        //retiros
        cuenta1.sacar(50);

        //domiciliacion recibos en cuentas
        cuenta1.domiciliar("1000000A", "EMIVASA", 45.00, "Agua", "MENSUAL");

    }

}
