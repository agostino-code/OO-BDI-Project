package com.unina.project.controller;

import com.unina.project.Main;
import com.unina.project.Sede;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.net.URL;
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

    public Sede sede=new Sede();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailTextField.focusedProperty().addListener(checkEmailListner);
        passwordField.focusedProperty().addListener(passwordListner);
        repeatpasswordField.focusedProperty().addListener(repeatpasswordListner);

    }

    public void oninviasottoscrizioneButtonClick(ActionEvent actionEvent) {
    }

    public void getinserisciScene(MouseEvent mouseEvent) throws IOException {
        Stage indirizzoStage=new Stage();
        FXMLLoader indirizzoPageLoader = new FXMLLoader(Main.class.getResource("indirizzo.fxml"));
        Parent indirizzoPane = indirizzoPageLoader.load();
        Scene indirizzoScene = new Scene(indirizzoPane, 400, 300);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(indirizzoScene);
        indirizzoStage.setResizable(false);
        indirizzoStage.setTitle("Inserisci Indirizzo");
        indirizzoStage.setScene(indirizzoScene);
        indirizzoStage.showAndWait();
        IndirizzoController indirizzocontroller= indirizzoPageLoader.getController();
        this.sede=indirizzocontroller.getSede();
        indirizzoTextField.setText(this.sede.getIndirizzo());
    }
}
