package com.mycompany.bancogs;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class SecondaryController implements Initializable {

    @FXML
    private Label fechaInicio;
    private LocalDate hoy;
    @FXML
    private Button buttonAcceder;
    @FXML
    private Pane paginaInicio;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hoy= LocalDate.now();
        DateTimeFormatter fechaEstandar = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fechaInicio.setText(hoy.format(fechaEstandar));
        
        paginaInicio.getStylesheets().add("styles.css");
    }
}