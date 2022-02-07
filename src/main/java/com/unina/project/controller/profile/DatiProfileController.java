package com.unina.project.controller.profile;

import com.unina.project.Autenticazione;
import com.unina.project.Main;
import com.unina.project.Utente;
import com.unina.project.controller.ModificaDatiAutenticazioneController;
import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import com.unina.project.verificationcode.SendVerificationEmail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

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
    private final AutenticazioneDAO autenticazioneDAO=new PostgreAutenticazioneDAO();

    public void setDatiAutenticazione(Autenticazione autenticazione){
        this.autenticazione=autenticazione;
        emailTextField.setText(autenticazione.getEmail());
        passwordField.setText(autenticazione.getPassword());
        passwordTextField.setText(autenticazione.getPassword());
    }

    public void setDatiUtente(Utente utente){
        NomeTextField.setText(utente.getNome());
        CognomeTextField.setText(utente.getCognome());
        CodiceFiscaleTextField.setText(utente.getCodiceFiscale());
        ComuneNascitaTextField.setText(utente.getComuneDiNascita());
        DataNascitaTextField.setText(String.valueOf(utente.getDataNascita()));
    }
    @FXML
    void onViewMousePressed() {
        passwordField.setVisible(false);
        passwordTextField.setVisible(true);
    }

    @FXML
    void onViewMouseRelased() {
        passwordTextField.setVisible(false);
        passwordField.setVisible(true);

    }

    public void onModificaDatiButtonClick(ActionEvent actionEvent) throws IOException {
        SendVerificationEmail sendVerificationEmail = new SendVerificationEmail();
        String codicediverifica = sendVerificationEmail.SendEmail(emailTextField.getText());
        TextInputDialog textInput = new TextInputDialog();
        textInput.setTitle("Invio codice di Verifica");
        textInput.setHeaderText("Abbiamo inviato un codice di verifica all'email che ci hai fornito");
        textInput.setContentText("Inserisci qui il codice :");
        Optional<String> result = textInput.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals(codicediverifica)) {
                Stage cercaStage = new Stage();
                FXMLLoader cercaPageLoader = new FXMLLoader(Main.class.getResource("modificaDatiAutenticazione.fxml"));
                ModificaDatiAutenticazioneController modificaDatiAutenticazioneController = cercaPageLoader.getController();
                modificaDatiAutenticazioneController.setAutenticazione(autenticazione);
                Parent cercaPane = cercaPageLoader.load();
                Scene cercaScene = new Scene(cercaPane);
                JMetro jMetro = new JMetro(Style.LIGHT);
                jMetro.setScene(cercaScene);
                cercaStage.setResizable(false);
                cercaStage.setScene(cercaScene);
                Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                primaryStage.getScene().getRoot().setDisable(true);
                cercaStage.showAndWait();
                primaryStage.getScene().getRoot().setDisable(false);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione!");
                alert.setHeaderText("Il codice inserito non è corretto!");
                alert.setContentText("Riprova!");
                alert.showAndWait();
            }
        }
    }

    public void onEliminaAccountButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Vuoi davvero eliminare il tuo account?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                eliminaUtente();
            }
        }
    }

    private void eliminaUtente() {
        SendVerificationEmail sendVerificationEmail = new SendVerificationEmail();
        String codicediverifica = sendVerificationEmail.SendEmail(autenticazione.getEmail());
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Invio codice di Verifica");
        dialog.setHeaderText("Per eliminare il tuo account inserisci il codice inviato alla tua Mail");
        dialog.setContentText("Inserisci qui il codice :");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals(codicediverifica)) {
                try {
                    autenticazioneDAO.deleteAutenticazione(autenticazione);
                    Stage primaryStage = (Stage) emailTextField.getParent().getScene().getWindow();
                    primaryStage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione!");
                alert.setHeaderText("Il codice inserito non è corretto!");
                alert.setContentText("Riprova!");
                alert.showAndWait();
            }
        }
    }
}
