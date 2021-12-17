package project.fxml.controller;

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

    public void setProfileScene(Scene scene) {
        profileScene = scene;
    }

    public void openProfileScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(profileScene);
    }
}

