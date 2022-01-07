package com.unina.project.controller;

import com.unina.project.Autenticazione;
import com.unina.project.Corso;
import com.unina.project.Gestore;
import com.unina.project.Main;
import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.CorsoDAO;
import com.unina.project.database.GestoreDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import com.unina.project.database.postgre.PostgreCorsoDAO;
import com.unina.project.database.postgre.PostgreGestoreDAO;
import com.unina.project.verificationcode.SendVerificationEmail;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileGestoreController implements Initializable {

    @FXML
    public Button nuovoCorsoButton;

    @FXML
    public TableView<Corso> corsiTableView;

    @FXML
    public TableColumn<Corso,String> titoloTableColumn;

    @FXML
    public TableColumn<Corso,String> descrizioneTableColumn;

    @FXML
    public TableColumn<Corso,Integer> iscrizionimassimeTableColumn;

    @FXML
    public TableColumn<Corso,Integer> numerolezioniTableColumn;

    @FXML
    public TableColumn<Corso,String> tassopresenzeminimeTableColumn;

    @FXML
    private MenuBar gestoreMenuBar;

    @FXML
    private TableView<?> iscrittiTableView;

    @FXML
    private TableView<?> operatoriTableView;

    private ObservableList<Corso> listCorsi = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titoloTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().titolo));
        descrizioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().descrizione));
        iscrizionimassimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().iscrizioniMassime));
        numerolezioniTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().numeroLezioni));
        tassopresenzeminimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTassoPresenzeMinime()));
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Choice 1");
        MenuItem menuItem2 = new MenuItem("Choice 2");
        MenuItem menuItem3 = new MenuItem("Choice 3");

        menuItem3.setOnAction((event) -> {
            System.out.println("Choice 3 clicked!");
        });
        contextMenu.getItems().addAll(menuItem1,menuItem2,menuItem3);
        corsiTableView.setRowFactory( tv -> {
            TableRow<Corso> row = new TableRow<>();
            row.setContextMenu(contextMenu);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    Corso rowData = row.getItem();




                    System.out.println(rowData);
                }
            });
            return row ;
        });

    }
    public void updateCorsiTableView() throws SQLException {
        gestore.setCorsi(gestoreDAO.getCorsi(codGestore));
        listCorsi.addAll(gestore.corsi);
        corsiTableView.setItems(listCorsi);

    }

    public void setProfileGestore(String email, String password) {
        autenticazione.setEmail(email);
        autenticazione.setPassword(password);
        try {
            codGestore=gestoreDAO.returncodGestore(email);
            gestore=gestoreDAO.getGestore(codGestore);
            updateCorsiTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Autenticazione autenticazione = new Autenticazione();
    private AutenticazioneDAO autenticazioneDAO = new PostgreAutenticazioneDAO();
    private GestoreDAO gestoreDAO= new PostgreGestoreDAO();
    public CorsoDAO corsoDAO=new PostgreCorsoDAO();
    private Gestore gestore= new Gestore();
    public Corso corso=new Corso();
    public String codGestore;

    @FXML
    public void oncloseButtonMenuClick() {
        Stage primaryStage = (Stage) (gestoreMenuBar.getScene().getWindow());
        primaryStage.setTitle("Formazione Facile");
        primaryStage.setScene(Main.getLoginScene());
    }

    @FXML
    private void oneliminaGestoreButtonMenuClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Vuoi davvero eliminare il tuo account?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
        if (result.get() == ButtonType.OK) {
            eliminaGestore();
        }
        }
    }


    private void eliminaGestore() {
        SendVerificationEmail sendVerificationEmail = new SendVerificationEmail();
        String codicediverifica = sendVerificationEmail.SendEmail(autenticazione.email);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Invio codice di Verifica");
        dialog.setHeaderText("Per eliminare il tuo account inserisci il codice inviato alla tua Mail");
        dialog.setContentText("Inserisci qui il codice :");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals(codicediverifica)) {
                try {
                    autenticazioneDAO.deleteAutenticazione(autenticazione);
                    oncloseButtonMenuClick();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione!");
                alert.setHeaderText("Il codice inserito non è corretto!");
                alert.setContentText("Riprova!");
                alert.showAndWait();
            }
        }
    }

    public void onNuovoCorsoButton(ActionEvent actionEvent) throws IOException {
            Stage corsoStage=new Stage();
            FXMLLoader corsoPageLoader = new FXMLLoader(Main.class.getResource("corso.fxml"));
            Parent corsoPane = corsoPageLoader.load();
            Scene corsoScene = new Scene(corsoPane, 400, 500);
            JMetro jMetro = new JMetro(Style.LIGHT);
            jMetro.setScene(corsoScene);
            CorsoController corsoController=corsoPageLoader.getController();
            corsoController.setcodGestore(codGestore);
            corsoStage.setResizable(false);
            corsoStage.setTitle("Inserisci Nuovo Corso");
            corsoStage.setScene(corsoScene);
            nuovoCorsoButton.getParent().setDisable(true);
            corsoStage.showAndWait();
        try {
            updateCorsiTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        nuovoCorsoButton.getParent().setDisable(false);


        }
}
