package com.unina.project.controller;

import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Scene profileScene;

    @FXML
    Button loginButton;
    @FXML
    TextField emailTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    ProgressBar loginProgressBar;

    public AutenticazioneDAO autenticazioneDAO=new PostgreAutenticazioneDAO();
    private Scene registrazioneScene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailTextField.focusedProperty().addListener(checkEmailListner);
    }

    public void setRegistrazioneScene(Scene scene) {
        registrazioneScene = scene;
    }

    public void openRegistrazioneScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(registrazioneScene);
    }

    public void setProfileScene(Scene scene) {
        profileScene = scene;
    }

    public void openProfileScene(ActionEvent actionEvent) {
        try {
            if(autenticazioneDAO.checkLogin(emailTextField.getText(), passwordTextField.getText())){
                loginProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Nessuna corrispondenza tra email e password!");
                alert.setContentText("Riprova!");
                alert.showAndWait();
                loginProgressBar.setProgress(0);
            }
            else{
                loginProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                primaryStage.setScene(profileScene);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ChangeListener<Boolean> checkEmailListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            try {
                if(autenticazioneDAO.checkEmailExist(emailTextField.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Errore email");
                    alert.setHeaderText("L'email che hai inserito non risulta registrata!");
                    alert.setContentText("Registrati!");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    public void openProfileGestoriScene(ActionEvent actionEvent) {
    }

    public void openRegistrazioneGestoriScene(ActionEvent actionEvent) {
    }
}

