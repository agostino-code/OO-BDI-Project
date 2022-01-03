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
    public Button loginButton;
    @FXML
    public TextField emailTextField;
    @FXML
    public TextField passwordTextField;
    @FXML
    public ProgressBar loginProgressBar;
    @FXML
    public TextField gestoriemailTextField;
    @FXML
    public PasswordField gestoriPasswordField;

    private final AutenticazioneDAO autenticazioneDAO=new PostgreAutenticazioneDAO();
    private final JMetro jMetro = new JMetro(Style.LIGHT);



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailTextField.focusedProperty().addListener(checkEmailListner);
        gestoriemailTextField.focusedProperty().addListener(checkEmailGestoriListner);
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
            if(autenticazioneDAO.loginUtente(emailTextField.getText(), passwordTextField.getText())){
                checkLoginFail();
            }
            else{
                loginProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                FXMLLoader profilePageLoader = new FXMLLoader(Main.class.getResource("profile.fxml"));
                loadProfile(actionEvent, profilePageLoader);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private final ChangeListener<Boolean> checkEmailListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
                if (!emailTextField.getText().isBlank()) {
                    try {
                    if (autenticazioneDAO.checkEmailUtenteExist(emailTextField.getText())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Errore email");
                        alert.setHeaderText("L'email che hai inserito non risulta registrata!");
                        alert.setContentText("Registrati!");
                        alert.showAndWait();
                    }
                } catch(SQLException e){
                    e.printStackTrace();
                }
                }
        }
    };
    private final ChangeListener<Boolean> checkEmailGestoriListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if (!gestoriemailTextField.getText().isBlank()) {
                try {
                    if (autenticazioneDAO.checkEmailGestoriExist(gestoriemailTextField.getText())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Errore email");
                        alert.setHeaderText("L'email che hai inserito non risulta registrata!");
                        alert.setContentText("Registrati!");
                        alert.showAndWait();
                    }
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    };

    public void openRegistrazioneGestoriScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader registrazionegestorePageLoader = new FXMLLoader(Main.class.getResource("registrazioneGestore.fxml"));
        Parent registrazionegestorePane = registrazionegestorePageLoader.load();
        Scene registrazionegestoreScene = new Scene(registrazionegestorePane, 400, 600);
        jMetro.setScene(registrazionegestoreScene);
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Registrazione Gestore");
        primaryStage.setScene(registrazionegestoreScene);
    }

    public void openProfileGestoriScene(ActionEvent actionEvent) {
        try {
            if(autenticazioneDAO.loginGestore(gestoriemailTextField.getText(), gestoriPasswordField.getText())){
                checkLoginFail();
            }
            else{
                loginProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                FXMLLoader profilePageLoader = new FXMLLoader(Main.class.getResource("profileGestore.fxml"));
                loadProfile(actionEvent, profilePageLoader);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProfile(ActionEvent actionEvent, FXMLLoader profilePageLoader) throws IOException {
        Parent profilePane = profilePageLoader.load();
        Scene profileScene = new Scene(profilePane);
        jMetro.setScene(profileScene);
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(profileScene);
        primaryStage.setResizable(true);
    }


    private void checkLoginFail() {
        loginProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText("Nessuna corrispondenza tra email e password!");
        alert.setContentText("Riprova!");
        alert.showAndWait();
        loginProgressBar.setProgress(0);
    }
}

