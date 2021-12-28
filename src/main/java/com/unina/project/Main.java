package com.unina.project;

import com.unina.project.controller.LoginController;
import com.unina.project.controller.ProfileController;
import com.unina.project.controller.RegistrazioneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loginPaneLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent loginPane = loginPaneLoader.load();
        Scene loginScene = new Scene(loginPane, 300, 400);

        FXMLLoader profilePageLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
        Parent profilePane = profilePageLoader.load();
        Scene profileScene = new Scene(profilePane, 600, 400);

        FXMLLoader registrazionePageLoader = new FXMLLoader(getClass().getResource("registrazione.fxml"));
        Parent registrazionePane = registrazionePageLoader.load();
        Scene registrazioneScene = new Scene(registrazionePane, 400, 600);

            LoginController loginPaneController = loginPaneLoader.getController();
            loginPaneController.setProfileScene(profileScene);
            loginPaneController.setRegistrazioneScene(registrazioneScene);


            ProfileController profilePaneController = profilePageLoader.getController();
            profilePaneController.setLoginScene(loginScene);

            RegistrazioneController registrazionePaneController = registrazionePageLoader.getController();
            registrazionePaneController.setLoginScene(loginScene);

        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(loginScene);
        jMetro.setScene(profileScene);
        jMetro.setScene(registrazioneScene);
            primaryStage.setTitle("FormazioneFacile");
            primaryStage.setScene(loginScene);
            primaryStage.show();
        }

    public static void main(String[] args) {
        launch();
    }

}
