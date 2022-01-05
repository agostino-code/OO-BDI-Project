package com.unina.project.controller;

import com.unina.project.Autenticazione;
import com.unina.project.Corso;
import com.unina.project.Main;
import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.GestoreDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import com.unina.project.database.postgre.PostgreGestoreDAO;
import com.unina.project.verificationcode.SendVerificationEmail;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Optional;

public class ProfileGestoreController {


    @FXML
    private TreeTableView<Corso> corsiTableView;

    @FXML
    private MenuBar gestoreMenuBar;

    @FXML
    private TreeTableView<?> iscrittiTableView;

    @FXML
    private TreeTableView<?> operatoriTableView;

    public void setAutenticazione(String email, String password) {
        autenticazione.setEmail(email);
        autenticazione.setPassword(password);
        try {
            codGestore=gestoreDAO.returncodGestore(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Autenticazione autenticazione = new Autenticazione();
    private AutenticazioneDAO autenticazioneDAO = new PostgreAutenticazioneDAO();
    private GestoreDAO gestoreDAO= new PostgreGestoreDAO();
    private String codGestore;

    @FXML
    public void oncloseButtonMenuClick() {
        Stage primaryStage = (Stage) (gestoreMenuBar.getScene().getWindow());
        primaryStage.setTitle("Formazione Facile");
        primaryStage.setScene(Main.getLoginScene());
    }

    @FXML
    private void oneliminaGestoreButtonMenuClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Vuoi davvero eliminare il tuo account?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            eliminaGestore();
        }
    }


    private void eliminaGestore() {
        SendVerificationEmail sendVerificationEmail = new SendVerificationEmail();
        String codicediverifica = sendVerificationEmail.SendEmail(autenticazione.email);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Invio codice di Verifica");
        dialog.setHeaderText("Per eliminare il tuo account inserisci il codice inviato alla tua Mail");
        dialog.setContentText("Inserisci qui il codice :");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals(codicediverifica)) {
                try {
                    autenticazioneDAO.deleteAutenticazione(autenticazione);
                    oncloseButtonMenuClick();
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
