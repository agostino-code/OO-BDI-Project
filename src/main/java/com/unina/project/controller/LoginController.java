package com.unina.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginController {

    private Scene profileScene;

    @FXML
    Button loginButton;

    private Scene registrazioneScene;

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
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(profileScene);
    }
}

