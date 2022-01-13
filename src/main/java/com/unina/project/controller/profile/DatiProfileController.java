package com.unina.project.controller.profile;

import com.unina.project.Autenticazione;
import com.unina.project.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class DatiProfileController{
    @FXML
    private TextField CodiceFiscaleTextField;

    @FXML
    private TextField CognomeTextField;

    @FXML
    private TextField ComuneNascitaTextField;

    @FXML
    private TextField DataNascitaTextField;

    @FXML
    private TextField NomeTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField;

    private Autenticazione autenticazione=new Autenticazione();
    private Utente utente=new Utente();

    public void setDatiAutenticazione(Autenticazione autenticazione){
        this.autenticazione=autenticazione;
        emailTextField.setText(autenticazione.getEmail());
        passwordField.setText(autenticazione.getPassword());
        passwordTextField.setText(autenticazione.getPassword());
    }

    public void setDatiUtente(Utente utente){
        this.utente=utente;
        NomeTextField.setText(utente.getNome());
        CognomeTextField.setText(utente.getCognome());
        CodiceFiscaleTextField.setText(utente.getCodiceFiscale());
        ComuneNascitaTextField.setText(utente.getComuneDiNascita());
        DataNascitaTextField.setText(String.valueOf(utente.getDataNascita()));
    }
    @FXML
    void onViewMousePressed(MouseEvent event) {
        passwordField.setVisible(false);
        passwordTextField.setVisible(true);
    }

    @FXML
    void onViewMouseRelased() {
        passwordTextField.setVisible(false);
        passwordField.setVisible(true);

    }

    public void onModificaDatiButtonClick(ActionEvent actionEvent) {
    }
}
