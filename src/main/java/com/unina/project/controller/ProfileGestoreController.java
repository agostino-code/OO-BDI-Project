package com.unina.project.controller;

import com.unina.project.*;
import com.unina.project.database.*;
import com.unina.project.database.postgre.*;
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
    public TableColumn<Corso,String> titoloTableColumn;
    public TableColumn<Corso,String> descrizioneTableColumn;
    public TableColumn<Corso,Integer> iscrizionimassimeTableColumn;
    public TableColumn<Corso,Integer> numerolezioniTableColumn;
    public TableColumn<Corso,String> tassopresenzeminimeTableColumn;

    @FXML
    private MenuBar gestoreMenuBar;

    @FXML
    private TableView<?> iscrittiTableView;

    @FXML
    private TableView<Operatore> operatoriTableView;
    public TableColumn<Operatore,String> emailoperatoreTableColumn;
    public TableColumn<Operatore,String> nomeoperatoreTableColumn;
    public TableColumn<Operatore,String> cognomeoperatoreTableColumn;
    public TableColumn<Operatore,String> codoperatoreTableColumn;

    private ObservableList<Corso> listCorsi = FXCollections.observableArrayList();
    private ObservableList<Operatore> listOperatori = FXCollections.observableArrayList();
    private Corso rowData;
    private Autenticazione autenticazione = new Autenticazione();
    private AutenticazioneDAO autenticazioneDAO = new PostgreAutenticazioneDAO();
    public Gestore gestore= new Gestore();
    private GestoreDAO gestoreDAO= new PostgreGestoreDAO();
    public AreaTematicaDAO areaTematicaDAO=new PostgreAreaTematicaDAO();
    public Corso corso=new Corso();
    public CorsoDAO corsoDAO=new PostgreCorsoDAO();
    private OperatoreDAO operatoreDAO=new PostgreOperatoreDAO();
    private Operatore operatore=new Operatore();
    private Utente utente=new Utente();
    private UtenteDAO utenteDAO=new PostgreUtenteDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCorsiTableView();
        setOperatoriTableView();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Aggiungi Operatore");
        MenuItem menuItem2 = new MenuItem("Visualizza Statistiche");
        MenuItem menuItem3 = new MenuItem("Modifica Corso");
        MenuItem menuItem4 = new MenuItem("Elimina Corso");
        contextMenu.getItems().addAll(menuItem1,menuItem2,menuItem3,menuItem4);
        corsiTableView.setRowFactory( tv -> {
            TableRow<Corso> row = new TableRow<>();
            row.setContextMenu(contextMenu);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowData = row.getItem();
                    try {
                        updateOperatoriTableView(rowData.codCorso);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
        menuItem1.setOnAction((event) -> {
            aggiungiOperatore(rowData);
        });
        menuItem4.setOnAction((event) -> {
            try {
                modificaCorso(rowData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menuItem4.setOnAction((event) -> {
            eliminaCorso(rowData);
        });
    }

    public void setCorsiTableView(){
        titoloTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().titolo));
        descrizioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().descrizione));
        iscrizionimassimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().iscrizioniMassime));
        numerolezioniTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().numeroLezioni));
        tassopresenzeminimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTassoPresenzeMinime()));
    }

    public void setOperatoriTableView(){
        emailoperatoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().email));
        nomeoperatoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().nome));
        cognomeoperatoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().cognome));
        codoperatoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().codOperatore));
    }
    public void eliminaCorso(Corso corso){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Conferma");
        dialog.setHeaderText("Per eliminare il corso");
        dialog.setContentText("Inserisci 'si' :");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals("si")) {
                try {
                    corsoDAO.deleteCorso(corso);
                    updateCorsiTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione!");
                alert.setHeaderText("Devi inserire 'si' per eliminare il corso!");
                alert.setContentText("Riprova!");
                alert.showAndWait();
            }
        }
    }

    public void aggiungiOperatore(Corso corso){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Inserisci Operatore");
        dialog.setHeaderText("Aggiungi l'email dell'operatore che vuoi che coordina il corso");
        dialog.setContentText("Email :");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                if (autenticazioneDAO.checkEmailUtenteExist(result.get())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Errore email");
                    alert.setHeaderText("L'email che hai inserito non risulta registrata!");
                    alert.showAndWait();
                }
                else{
                    utente=utenteDAO.getUtente(result.get());
                    operatore.setCodOperatore(operatoreDAO.setOperatore(utente.codiceFiscale));
                    operatoreDAO.associaOperatore(operatore.codOperatore, corso.codCorso);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Attenzione!");
                    alert.setHeaderText(utente.nome+" "+utente.cognome+" ora è operatore del corso "+corso.titolo);
                    alert.showAndWait();
                    updateOperatoriTableView(corso.codCorso);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void modificaCorso(Corso corso) throws IOException {
        Stage corsoStage=new Stage();
        FXMLLoader corsoPageLoader = new FXMLLoader(Main.class.getResource("corso.fxml"));
        CorsoModificaController corsoModificaController=new CorsoModificaController();
        corsoPageLoader.setController(corsoModificaController);
        Parent corsoPane = corsoPageLoader.load();
        Scene corsoScene = new Scene(corsoPane, 400, 600);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(corsoScene);
        corsoModificaController.setcodGestore(gestore.codGestore);
        corsoStage.setResizable(false);
        corsoStage.setTitle("Muodifica Corso");
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
    public void updateCorsiTableView() throws SQLException {
        corsiTableView.getItems().clear();
        gestore.setCorsi(corsoDAO.getCorsi(gestore.codGestore));
        listCorsi.addAll(gestore.corsi);
        corsiTableView.setItems(listCorsi);
    }

    public void updateOperatoriTableView(String codCorso) throws SQLException {
        operatoriTableView.getItems().clear();
        corso.setOperatori(operatoreDAO.getOperatori(codCorso));
        listOperatori.addAll(corso.operatori);
        operatoriTableView.setItems(listOperatori);

    }

    public void setProfileGestore(String email, String password) {
        autenticazione.setEmail(email);
        autenticazione.setPassword(password);
        try {
            gestore=gestoreDAO.getGestore(email);
            updateCorsiTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            Scene corsoScene = new Scene(corsoPane, 400, 600);
            JMetro jMetro = new JMetro(Style.LIGHT);
            jMetro.setScene(corsoScene);
            CorsoController corsoController=corsoPageLoader.getController();
            corsoController.setcodGestore(gestore.codGestore);
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
