package com.unina.project.controller;

import com.unina.project.Autenticazione;
import com.unina.project.Utente;
import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.UtenteDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import com.unina.project.database.postgre.PostgreUtenteDAO;
import com.unina.project.graphics.LimitedTextField;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModificaDatiAutenticazioneController implements Initializable {
    @FXML
    private Button confermaModificaButton;

    @FXML
    private PasswordField confermaPasswordField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;
    private final AutenticazioneDAO autenticazioneDAO=new PostgreAutenticazioneDAO();
    private final UtenteDAO utenteDAO=new PostgreUtenteDAO();
    private Autenticazione autenticazione=new Autenticazione();

    public void setAutenticazione(Autenticazione autenticazione)
    {
        this.autenticazione=autenticazione;
        emailTextField.setText(autenticazione.getEmail());
        passwordField.setText(autenticazione.getPassword());
        confermaPasswordField.setText(autenticazione.getPassword());
    }



    public String nomeUtente(String email) throws SQLException {
        Utente utente =utenteDAO.getUtente(email);
        return utente.getNome();
    }

    public ChangeListener<Boolean> passwordListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if(!passwordField.getText().isBlank()) {
                try {
                    if ((calcolaPasswordStrength(passwordField.getText(),nomeUtente(autenticazione.getEmail()))) < 8) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Attenzione!");
                        alert.setHeaderText("La password che hai inserito non è sicura!");
                        alert.setContentText("""
                            Non deve contenere il nome dell'utente/gestore.
                            Deve essere composta da almeno otto caratteri.
                            Deve contenere caratteri di almeno tre delle quattro categorie seguenti:
                               1) Lettere maiuscole dell'alfabeto latino (dalla A alla Z).
                               2) Lettere minuscole dell'alfabeto latino (dalla a alla z).
                               3) Numeri in base 10 (da 0 a 9).
                               4) Caratteri non alfanumerici, simboli.""");
                        alert.showAndWait();
                        passwordField.clear();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private double calcolaPasswordStrength(String password, String nome) throws SQLException {

        double passwordScore = 1;
        if (password.length() < 1)
            return 0;
        if (password.length() < 8)
            return passwordScore;
        else if (password.length() >= 10)
            passwordScore += 2;
        else
            passwordScore += 1;

        if (password.matches("(?=.*[0-9]).*"))
            passwordScore += 2;

        if (password.matches("(?=.*[a-z]).*"))
            passwordScore += 2;

        if (password.matches("(?=.*[A-Z]).*"))
            passwordScore += 2;

        if (password.matches("(?=.*[~!@#$%^&*()_-]).*"))
            passwordScore += 2;

        if (!nome.isEmpty() && password.contains(nome)) {
            return 1;
        }
        return passwordScore;
    }

    @FXML
    void confermaModifica() throws SQLException {
        if(emailTextField.getText().isBlank()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attenzione!");
            alert.setHeaderText("Il campo email è vuoto!");
            alert.show();
        }else if(!emailTextField.getText().equals(autenticazione.getEmail()) && !autenticazioneDAO.checkEmailExist(emailTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore email");
            alert.setHeaderText("L'email che hai inserito risulta già registrata!");
            alert.setContentText("Cambia il valore di Email!");
            emailTextField.clear();
            alert.showAndWait();
        }else if(passwordField.getText().isBlank()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore password");
            alert.setHeaderText("Inserisci una password!");
            alert.show();
        }else if(confermaPasswordField.getText().isBlank()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore conferma password");
            alert.setHeaderText("Devi confermare la password inserita!");
            alert.showAndWait();
        }else if(!confermaPasswordField.getText().equals(passwordField.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore email");
            alert.setHeaderText("Le password inserite non concidono");
            alert.setContentText("Riprova!");
            confermaPasswordField.clear();
            alert.showAndWait();
        }else{
            autenticazioneDAO.updateAutenticazione(passwordField.getText(),emailTextField.getText(),autenticazione.getEmail());
            Stage primaryStage = (Stage) confermaModificaButton.getScene().getWindow();
            primaryStage.close();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordField.focusedProperty().addListener(passwordListner);
        LimitedTextField limitemail=new LimitedTextField(emailTextField);
        limitemail.setMaxLength(60);
        limitemail.setEmailField();
        LimitedTextField limitpassword=new LimitedTextField(passwordField);
        limitpassword.setMaxLength(30);
        limitpassword.setStandardField();
        LimitedTextField limitrepeatpassword=new LimitedTextField(confermaPasswordField);
        limitrepeatpassword.setMaxLength(30);
        limitrepeatpassword.setStandardField();
    }
}
