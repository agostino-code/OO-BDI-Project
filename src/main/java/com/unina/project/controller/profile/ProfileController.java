package com.unina.project.controller.profile;

import com.unina.project.Autenticazione;
import com.unina.project.Main;
import com.unina.project.Operatore;
import com.unina.project.Utente;
import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.OperatoreDAO;
import com.unina.project.database.UtenteDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import com.unina.project.database.postgre.PostgreOperatoreDAO;
import com.unina.project.database.postgre.PostgreUtenteDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private Button gestioneCorsiButton;

    @FXML
    private Button gestorelezioneButton;

    @FXML
    private Button richiesteButton;

    @FXML
    private StackPane stackPanelProfilo;

    private Stage loginStage;
    public Autenticazione autenticazione = new Autenticazione();
    private AutenticazioneDAO autenticazioneDAO = new PostgreAutenticazioneDAO();
    public Utente utente=new Utente();
    private UtenteDAO utenteDAO=new PostgreUtenteDAO();
    private Operatore operatore=new Operatore();
    private OperatoreDAO operatoreDAO=new PostgreOperatoreDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setHomepage();
    }

    @FXML
    void onDatiAnagraficiButtonClick(ActionEvent event) {
        Parent root = null;
        try {
            FXMLLoader datiAnagraficiPageLoader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("profile/datiAnagrafici.fxml")));
            root = datiAnagraficiPageLoader.load();
            DatiProfileController datiProfileController=datiAnagraficiPageLoader.getController();
            datiProfileController.setDatiUtente(utente);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stackPanelProfilo.getChildren().clear();
        stackPanelProfilo.getChildren().addAll(root);
    }

    @FXML
    void onDatiAutenticazioneButtonClick(ActionEvent event) {
        Parent root = null;
        try {
            FXMLLoader datiAutenticazionePageLoader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("profile/datiAutenticazione.fxml")));
            root = datiAutenticazionePageLoader.load();
            DatiProfileController datiProfileController=datiAutenticazionePageLoader.getController();
            datiProfileController.setDatiAutenticazione(autenticazione);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stackPanelProfilo.getChildren().clear();
        stackPanelProfilo.getChildren().addAll(root);
    }

    @FXML
    void onIscrizioneCorsiButtonClick(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("profile/iscrizioneCorsi.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stackPanelProfilo.getChildren().clear();
        stackPanelProfilo.getChildren().addAll(root);


    }

    @FXML
    void onLogoutButtonClick(ActionEvent actionEvent) {
        Stage secondaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        secondaryStage.close();
        loginStage.show();
    }

    @FXML
    void oniscrizioniLezioniButtonClick(ActionEvent event) {

    }

    public void setHomepage() {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("profile/homepage.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stackPanelProfilo.getChildren().clear();
        stackPanelProfilo.getChildren().addAll(root);
    }

    public void setProfile(Stage primaryStage, String email, String password) {
        loginStage=primaryStage;
        autenticazione.setEmail(email);
        autenticazione.setPassword(password);
        try {
            utente=utenteDAO.getUtente(email);
            if(!operatoreDAO.checkOperatoreExist(utente.codiceFiscale)){
                gestioneCorsiButton.setVisible(true);
                gestorelezioneButton.setVisible(true);
                richiesteButton.setVisible(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

