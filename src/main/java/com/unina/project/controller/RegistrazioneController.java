package com.unina.project.controller;

import com.unina.project.Autenticazione;
import com.unina.project.Utente;
import com.unina.project.codicefiscale.engine.Engine;
import com.unina.project.codicefiscale.engine.Utils;
import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class RegistrazioneController implements Initializable {
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
    private ChoiceBox<String> sessoChoiceBox;
    @FXML
    public TextField codicefiscaleTextField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatpasswordField;

    public List<String> comuni = new ArrayList<>();
    public Utente utente=new Utente();
    public Autenticazione autenticazione = new Autenticazione();
    public AutenticazioneDAO autenticazioneDAO=new PostgreAutenticazioneDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getComuni();
        emailTextField.focusedProperty().addListener(checkEmailListner);
        sessoChoiceBox.getItems().addAll("M","F");
        nomeTextField.focusedProperty().addListener(generaCodiceListner);
        cognomeTextField.focusedProperty().addListener(generaCodiceListner);
        dataDatePicker.focusedProperty().addListener(generaCodiceListner);
        comunedinascitaTextField.focusedProperty().addListener(generaCodiceListner);
        sessoChoiceBox.focusedProperty().addListener(generaCodiceListner);

    }

    public void getComuni() {
        for (Map.Entry<String, String> e : Utils.getCitiesCodes().entrySet()) {
            comuni.add(e.getValue());
        }
    }

    @FXML
    private void comuniSuggeriti(KeyEvent keyEvent){
        TextFields.bindAutoCompletion(comunedinascitaTextField,comuni);
    }

    @FXML
    public void newCodiceFiscale() throws IOException {
        utente.setNome(nomeTextField.getText());
        utente.setCognome(cognomeTextField.getText());
        utente.setDataNascita(dataDatePicker.getValue());
        utente.setComuneDiNascita(comunedinascitaTextField.getText());
        utente.setSesso(sessoChoiceBox.getSelectionModel().getSelectedItem());
        Engine engine = new Engine(utente);
        codicefiscaleTextField.setText(engine.getCode());
        }

    @FXML
    private ChangeListener<Boolean> generaCodiceListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            try {

                newCodiceFiscale();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @FXML
    private ChangeListener<Boolean> checkEmailListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore email");
                    alert.setHeaderText("L'email che hai inserito risulta già registrata!");
                    alert.setContentText("Cambia il valore di Email");
                    alert.showAndWait();
                }
    };
}


