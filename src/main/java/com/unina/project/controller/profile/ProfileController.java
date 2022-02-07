package com.unina.project.controller.profile;

import com.unina.project.Autenticazione;
import com.unina.project.Main;
import com.unina.project.Utente;
import com.unina.project.database.OperatoreDAO;
import com.unina.project.database.UtenteDAO;
import com.unina.project.database.postgre.PostgreOperatoreDAO;
import com.unina.project.database.postgre.PostgreUtenteDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class ProfileController {

    public AnchorPane operatoreAnchorPane;
    @FXML
    private Button gestioneCorsiButton;

    @FXML
    private Button gestorelezioneButton;

    @FXML
    private Button richiesteButton;

    @FXML
    private StackPane stackPanelProfilo;

    private Stage loginStage;
    public final Autenticazione autenticazione = new Autenticazione();
    public Utente utente=new Utente();
    private final UtenteDAO utenteDAO=new PostgreUtenteDAO();
    private final OperatoreDAO operatoreDAO=new PostgreOperatoreDAO();


    @FXML
    void onDatiAnagraficiButtonClick() {
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
    void onDatiAutenticazioneButtonClick() {
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
    void onIscrizioneCorsiButtonClick() {
        Parent root = null;
        try {
            FXMLLoader iscrizioneCorsiPageLoader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("profile/iscrizioneCorsi.fxml")));
            root = iscrizioneCorsiPageLoader.load();
            IscrizioneCorsoController iscrizioneCorsoController=iscrizioneCorsiPageLoader.getController();
            iscrizioneCorsoController.setDatiUtente(utente);
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
    void oniscrizioniLezioniButtonClick() {
        Parent root = null;
        try {
            FXMLLoader iscrizioneLezioniPageLoader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("profile/iscrizioneLezioni.fxml")));
            root = iscrizioneLezioniPageLoader.load();
            IscrizioneLezioniController iscrizioneLezioniController=iscrizioneLezioniPageLoader.getController();
            iscrizioneLezioniController.setDatiUtente(utente);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stackPanelProfilo.getChildren().clear();
        stackPanelProfilo.getChildren().addAll(root);
    }

    public void setHomepage() {
        Parent root = null;
        try {
            FXMLLoader homepagePageLoader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("profile/homepage.fxml")));
            root = homepagePageLoader.load();
            HomepageController homepageController=homepagePageLoader.getController();
            homepageController.setDatiUtente(utente);
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
            if(!operatoreDAO.checkOperatoreExist(utente.getCodiceFiscale())){
                operatoreAnchorPane.setVisible(true);
                gestioneCorsiButton.setVisible(true);
                gestorelezioneButton.setVisible(true);
                richiesteButton.setVisible(true);
            }
            setHomepage();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onGestioneCorsiButtonClick() {
        Parent root = null;
        try {
            FXMLLoader gestioneCorsiPageLoader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("profile/gestioneCorsi.fxml")));
            root = gestioneCorsiPageLoader.load();
            GestioneCorsiController gestioneCorsiController=gestioneCorsiPageLoader.getController();
            gestioneCorsiController.setDatiUtente(utente);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stackPanelProfilo.getChildren().clear();
        stackPanelProfilo.getChildren().addAll(root);
    }

    public void onGestioneLezioniButtonClick() {
        Parent root = null;
        try {
            FXMLLoader gestioneLezioniPageLoader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("profile/gestioneLezioni.fxml")));
            root = gestioneLezioniPageLoader.load();
            GestioneLezioniController gestioneLezioniController=gestioneLezioniPageLoader.getController();
            gestioneLezioniController.setDatiUtente(utente);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stackPanelProfilo.getChildren().clear();
        stackPanelProfilo.getChildren().addAll(root);
    }

    public void onRichiesteButtonClick() {
        Parent root = null;
        try {
            FXMLLoader richiestePageLoader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("profile/richieste.fxml")));
            root = richiestePageLoader.load();
            RichiesteController richiesteController=richiestePageLoader.getController();
            richiesteController.setDatiUtente(utente);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stackPanelProfilo.getChildren().clear();
        stackPanelProfilo.getChildren().addAll(root);
    }
}

