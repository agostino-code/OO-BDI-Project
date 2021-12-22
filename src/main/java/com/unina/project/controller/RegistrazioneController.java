package com.unina.project.controller;

import com.unina.project.Autenticazione;
import com.unina.project.Utente;
import com.unina.project.codicefiscale.engine.Engine;
import com.unina.project.codicefiscale.engine.Utils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegistrazioneController {
    @FXML
    public Button registratiButton;
    @FXML
    public TextField emailTextField;
    @FXML
    public TextField nomeTextField;
    @FXML
    public TextField cognomeTextField;
    @FXML
    public DatePicker dataDatePicker;
    @FXML
    public javafx.scene.control.TextField comunedinascitaTextField;
    @FXML
    public ChoiceBox<String> sessoChoiseBox = new ChoiceBox<>(FXCollections.observableArrayList("M", "F"));
    @FXML
    public TextField codicefiscaleTextField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatpasswordField;

    public List<String> comuni = new ArrayList<>();

    @FXML
    private void getComuni() {
        for (Map.Entry<String, String> e : Utils.getCitiesCodes().entrySet()) {
            comuni.add(e.getValue());
        }
    }

    @FXML
    private void comuniSuggeriti(KeyEvent keyEvent){
        TextFields.bindAutoCompletion(comunedinascitaTextField,comuni);
    }

    public void newCodiceFiscale(javafx.event.ActionEvent actionEvent) throws IOException {
        Utente utente = new Utente();
        Autenticazione autenticazione = new Autenticazione();
        autenticazione.setEmail(emailTextField.getText());
        utente.setNome(nomeTextField.getText());
        utente.setCognome(cognomeTextField.getText());
        utente.setDataNascita(dataDatePicker.getValue());
        utente.setComuneDiNascita(comunedinascitaTextField.getText());
        utente.setSesso(sessoChoiseBox.getValue());
        Engine engine = new Engine(utente);
        codicefiscaleTextField.setText(engine.getCode());

        }
}


