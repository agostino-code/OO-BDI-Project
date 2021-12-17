package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import project.fxml.controller.LoginController;
import project.fxml.controller.ProfileController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // getting loader and a pane for the first scene.
        // loader will then give a possibility to get related controller
        FXMLLoader loginPaneLoader = new FXMLLoader(getClass().getResource("fxml/login.fxml"));
        Parent loginPane = loginPaneLoader.load();
        Scene loginScene = new Scene(loginPane, 300, 400);

        // getting loader and a pane for the second scene
        FXMLLoader profilePageLoader = new FXMLLoader(getClass().getResource("fxml/profile.fxml"));
            Parent profilePane = profilePageLoader.load();
            Scene profileScene = new Scene(profilePane, 600, 400);

            // injecting second scene into the controller of the first scene
            LoginController loginPaneController = (LoginController) loginPaneLoader.getController();
            loginPaneController.setProfileScene(profileScene);

            // injecting first scene into the controller of the second scene
            ProfileController profilePaneController = (ProfileController) profilePageLoader.getController();
            profilePaneController.setLoginScene(loginScene);

        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(loginScene);
        jMetro.setScene(profileScene);
            primaryStage.setTitle("FormazioneFacile");
            primaryStage.setScene(loginScene);
            primaryStage.show();
        }

    public static void main(String[] args) {
        launch();
    }

}
