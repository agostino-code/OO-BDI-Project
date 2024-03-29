package com.unina.project.controller;

import com.unina.project.Gestore;
import com.unina.project.Main;
import com.unina.project.Sede;
import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.GestoreDAO;
import com.unina.project.database.SedeDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import com.unina.project.database.postgre.PostgreGestoreDAO;
import com.unina.project.database.postgre.PostgreSedeDAO;
import com.unina.project.graphics.LimitedTextField;
import com.unina.project.verificationcode.SendVerificationEmail;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegistrazioneGestoreController extends RegistrazioneController{

    @FXML
    public Button registratigestoreButton;
    @FXML
    public TextArea descrizionegestoreTextArea;
    @FXML
    public TextField telefonoTextField;
    @FXML
    public TextField indirizzoTextField;
    @FXML
    public TextField nomeTextField;

    public Sede sede=new Sede();
    private final Gestore gestore=new Gestore();
    private final AutenticazioneDAO autenticazioneDAO=new PostgreAutenticazioneDAO();
    private final GestoreDAO gestoreDAO =new PostgreGestoreDAO();
    private final SedeDAO sedeDAO=new PostgreSedeDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LimitedTextField limitemail=new LimitedTextField(emailTextField);
        limitemail.setMaxLength(60);
        limitemail.setEmailField();
        LimitedTextField limitnome=new LimitedTextField(nomeTextField);
        limitnome.setMaxLength(30);
        limitnome.setCharsOnlyFieldwSpace();
        LimitedTextField limitelefono=new LimitedTextField(telefonoTextField);
        limitelefono.setPhoneNumberField();
        LimitedTextField limitpassword=new LimitedTextField(passwordField);
        limitpassword.setMaxLength(30);
        limitpassword.setStandardField();
        LimitedTextField limitrepeatpassword=new LimitedTextField(repeatpasswordField);
        limitrepeatpassword.setMaxLength(30);
        limitrepeatpassword.setStandardField();
        emailTextField.focusedProperty().addListener(checkEmailListner);
        passwordField.focusedProperty().addListener(passwordListner);
        repeatpasswordField.focusedProperty().addListener(repeatpasswordListner);
        descrizionegestoreTextArea.focusedProperty().addListener(descrizioneLister);
        nomeTextField.focusedProperty().addListener(nomeListner);

    }

    public void getinserisciScene() throws IOException {
        Stage indirizzoStage=new Stage();
        FXMLLoader indirizzoPageLoader = new FXMLLoader(Main.class.getResource("indirizzo.fxml"));
        Parent indirizzoPane = indirizzoPageLoader.load();
        Scene indirizzoScene = new Scene(indirizzoPane, 400, 300);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(indirizzoScene);
        indirizzoStage.setResizable(false);
        indirizzoStage.setTitle("Inserisci Indirizzo");
        indirizzoStage.setScene(indirizzoScene);
        registratigestoreButton.getParent().setDisable(true);
        indirizzoStage.showAndWait();
        registratigestoreButton.getParent().setDisable(false);
        IndirizzoController indirizzocontroller = indirizzoPageLoader.getController();
            sede=indirizzocontroller.getSede();
            if(sede.getVia()!=null){
                indirizzoTextField.setText(sede.getIndirizzo());
            }

        }

    private final ChangeListener<Boolean> nomeListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            try {
                if (!gestoreDAO.checkNomeExist(nomeTextField.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Errore nome");
                    alert.setHeaderText("Esiste già un gestore con questo nome!");
                    alert.setContentText("Cambia il valore di nome");
                    nomeTextField.clear();
                    alert.showAndWait();
                } else {
                    gestore.setNome(nomeTextField.getText());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    private final ChangeListener<Boolean> descrizioneLister = (observable, oldValue, newValue) -> {
        if (!newValue) {
                if (descrizionegestoreTextArea.getLength()>200){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Errore descrizione");
                    alert.setHeaderText("La descrizione non può superare 200 caratteri!");
                    alert.setContentText("Quando il testo diventa rosso hai superato i 200 caratteri!");
                    alert.showAndWait();
                } else {
                    if(!descrizionegestoreTextArea.getText().isBlank()){
                        gestore.setDescrizione(descrizionegestoreTextArea.getText());
                    }
                }
        }
    };

    public void checkDescrizioneLenght() {
        if (descrizionegestoreTextArea.getLength()>200){
            descrizionegestoreTextArea.setStyle("-fx-text-fill: red;");
        }
        else
            descrizionegestoreTextArea.setStyle("-fx-text-fill: black");
    }

    public void oninviasottoscrizioneButtonClick(ActionEvent actionEvent) {
        if(!emailTextField.getText().isBlank()&&!nomeTextField.getText().isBlank()&&
                !indirizzoTextField.getText().isBlank()&&
                !telefonoTextField.getText().isBlank()&&
                !passwordField.getText().isBlank()&&
                !repeatpasswordField.getText().isBlank()) {
            SendVerificationEmail sendVerificationEmail=new SendVerificationEmail();
            String codicediverifica=sendVerificationEmail.SendEmail(autenticazione.getEmail());
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Invio codice di Verifica");
            dialog.setHeaderText("Abbiamo inviato un codice di verifica all'email che ci hai fornito");
            dialog.setContentText("Inserisci qui il codice :");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                if(result.get().equals(codicediverifica)){
                    gestore.setEmail(autenticazione.getEmail());
                    try {
                        autenticazioneDAO.insertAutenticazione(autenticazione);
                        String codGestore=gestoreDAO.insertGestore(gestore);
                        sedeDAO.insertSede(sede,codGestore);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    accountCreatedSuccessful(actionEvent);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attenzione!");
                    alert.setHeaderText("Il codice inserito non è corretto!");
                    alert.setContentText("Riprova!");
                    alert.showAndWait();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Errore");
            alert.setHeaderText("Inserisci prima tutti i dati!");
            alert.setContentText("La descrizione è opzionale.");
            alert.showAndWait();
        }
    }
}
