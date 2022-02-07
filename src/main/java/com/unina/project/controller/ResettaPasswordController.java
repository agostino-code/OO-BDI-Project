package com.unina.project.controller;
import com.unina.project.Autenticazione;
import com.unina.project.Utente;
import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.UtenteDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import com.unina.project.database.postgre.PostgreUtenteDAO;
import com.unina.project.graphics.LimitedTextField;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ResettaPasswordController implements Initializable {


    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confermapasswordField;
    private final Autenticazione autenticazione=new Autenticazione();
    private final UtenteDAO utenteDAO = new PostgreUtenteDAO();
    private final AutenticazioneDAO autenticazioneDAO = new PostgreAutenticazioneDAO();

    public void setEmail(String email){ this.autenticazione.setEmail(email);}

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
    void onConfermaButtonClick(ActionEvent event) throws SQLException {
        if(!passwordField.getText().isEmpty()){
            if(!confermapasswordField.getText().isEmpty()){
                if(!passwordField.getText().equals(confermapasswordField.getText())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Attenzione!");
                    alert.setHeaderText("Le password non corrispondono!");
                    alert.setContentText("Riprova!");
                    alert.showAndWait();
                    confermapasswordField.clear();
                }
                else
                {
                    autenticazioneDAO.resetPassword(autenticazione.getEmail(), passwordField.getText());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Attenzione!");
                    alert.setHeaderText("La password è stata correttamente modificata!");
                    alert.showAndWait();
                    Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    primaryStage.close();
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordField.focusedProperty().addListener(passwordListner);
        LimitedTextField limitpassword=new LimitedTextField(passwordField);
        limitpassword.setMaxLength(30);
        limitpassword.setStandardField();
        LimitedTextField limitrepeatpassword=new LimitedTextField(confermapasswordField);
        limitrepeatpassword.setMaxLength(30);
        limitrepeatpassword.setStandardField();
    }
}
