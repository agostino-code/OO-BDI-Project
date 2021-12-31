package com.unina.project.controller;

import com.unina.project.Main;
import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    Button loginButton;
    @FXML
    TextField emailTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    ProgressBar loginProgressBar;

    private final AutenticazioneDAO autenticazioneDAO=new PostgreAutenticazioneDAO();
    private final JMetro jMetro = new JMetro(Style.LIGHT);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailTextField.focusedProperty().addListener(checkEmailListner);
    }

    public void openRegistrazioneScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader registrazionePageLoader = new FXMLLoader(Main.class.getResource("registrazione.fxml"));
        Parent registrazionePane = registrazionePageLoader.load();
        Scene registrazioneScene = new Scene(registrazionePane, 400, 600);
        jMetro.setScene(registrazioneScene);
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Registrazione Utente");
        primaryStage.setScene(registrazioneScene);
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
                FXMLLoader profilePageLoader = new FXMLLoader(Main.class.getResource("profile.fxml"));
                Parent profilePane = profilePageLoader.load();
                Scene profileScene = new Scene(profilePane, 600, 400);
                jMetro.setScene(profileScene);
                Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                primaryStage.setScene(profileScene);
                primaryStage.setResizable(true);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private final ChangeListener<Boolean> checkEmailListner = (observable, oldValue, newValue) -> {
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

    public void openRegistrazioneGestoriScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader registrazionegestorePageLoader = new FXMLLoader(Main.class.getResource("registrazioneGestore.fxml"));
        Parent registrazionegestorePane = registrazionegestorePageLoader.load();
        Scene registrazionegestoreScene = new Scene(registrazionegestorePane, 400, 600);
        jMetro.setScene(registrazionegestoreScene);
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Registrazione Gestore");
        primaryStage.setScene(registrazionegestoreScene);
    }
}

