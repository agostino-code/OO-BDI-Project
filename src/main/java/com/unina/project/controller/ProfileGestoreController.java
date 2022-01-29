package com.unina.project.controller;

import com.unina.project.*;
import com.unina.project.controller.profile.StatisticheController;
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
import javafx.util.Callback;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileGestoreController implements Initializable {

    @FXML
    public Button nuovoCorsoButton;

    @FXML
    public TreeTableView<Corso> corsiTableView;
    public TreeTableColumn<Corso,String> codCorsoTableColumn;
    public TreeTableColumn<Corso,String> titoloTableColumn;
    public TreeTableColumn<Corso,String> descrizioneTableColumn;
    public TreeTableColumn<Corso,Integer> iscrizionimassimeTableColumn;
    public TreeTableColumn<Corso,Integer> numerolezioniTableColumn;
    public TreeTableColumn<Corso,String> tassopresenzeminimeTableColumn;
    public TreeTableColumn<Corso,String> privatoTableColumn;
    public TreeTableColumn<Corso,String> areeTableColumn;
    @FXML
    private MenuBar gestoreMenuBar;
    public Menu nomeGestoreMenu;

    @FXML
    public TableView<Studente> studentiTableView;
    public TableColumn<Studente,String> nomeTableColumn;
    public TableColumn<Studente,String> cognomeTableColumn;
    public TableColumn<Studente,String> emailTableColumn;
    public TableColumn<Studente,String> codStudenteTableColumn;
    public TableColumn<Studente,String> sessoTableColumn;
    public TableColumn<Studente, LocalDate> datadiNascitaTableColumn;
    public TableColumn<Studente,String> idoneoTableColumn;

    @FXML
    private TableView<Operatore> operatoriTableView;
    public TableColumn<Operatore,String> emailoperatoreTableColumn;
    public TableColumn<Operatore,String> nomeoperatoreTableColumn;
    public TableColumn<Operatore,String> cognomeoperatoreTableColumn;
    public TableColumn<Operatore,String> codoperatoreTableColumn;
    public TableColumn<Operatore,String> richiestaTableColumn;

    private final ObservableList<Studente> listStudenti = FXCollections.observableArrayList();
    private final ObservableList<Operatore> listOperatori = FXCollections.observableArrayList();
    private Corso rowData;
    private Operatore rowDataOperatore;
    private final StudenteDAO studenteDAO=new PostgreStudenteDAO();
    private final Autenticazione autenticazione = new Autenticazione();
    private final AutenticazioneDAO autenticazioneDAO = new PostgreAutenticazioneDAO();
    public Gestore gestore= new Gestore();
    private final GestoreDAO gestoreDAO= new PostgreGestoreDAO();
    public final AreaTematicaDAO areaTematicaDAO=new PostgreAreaTematicaDAO();
    public final Corso corso=new Corso();
    public final CorsoDAO corsoDAO=new PostgreCorsoDAO();
    private final OperatoreDAO operatoreDAO=new PostgreOperatoreDAO();
    private final Operatore operatore=new Operatore();
    private final UtenteDAO utenteDAO=new PostgreUtenteDAO();
    private Stage loginStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCorsiTableView();
        setOperatoriTableView();
        setStudentiTableView();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Aggiungi Operatore");
        MenuItem menuItem2 = new MenuItem("Visualizza Statistiche");
        MenuItem menuItem3 = new MenuItem("Modifica Corso");
        MenuItem menuItem4 = new MenuItem("Elimina Corso");
        contextMenu.getItems().addAll(menuItem1,menuItem2,menuItem3,menuItem4);
        ContextMenu contextMenuElimina = new ContextMenu();
        MenuItem menuItem5 = new MenuItem("Rimuovi Operatore");
        contextMenuElimina.getItems().addAll(menuItem5);
        corsiTableView.setRowFactory( tv -> {
            TreeTableRow<Corso> row = new TreeTableRow<>();
            row.setContextMenu(contextMenu);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowData = row.getItem();
                    try {
                        updateOperatoriTableView(rowData.codCorso);
                        updateStudentiTableView(rowData.codCorso);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
        menuItem1.setOnAction((event) -> aggiungiOperatore(rowData));
        menuItem2.setOnAction((event) -> {
            try {
                visualizzaStatistiche(rowData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menuItem3.setOnAction((event) -> {
            try {
                modificaCorso(rowData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menuItem4.setOnAction((event) -> eliminaCorso(rowData));
        operatoriTableView.setRowFactory( tv -> {
            TableRow<Operatore> row = new TableRow<>();
            row.setContextMenu(contextMenuElimina);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowDataOperatore = row.getItem();
                }
            });
            return row ;
        });
        menuItem5.setOnAction((event) -> eliminaOperatore(rowDataOperatore.codOperatore,rowData.codCorso));
    }

    public void setCorsiTableView(){
        try {
            updateCorsiTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        codCorsoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().codCorso));
        titoloTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().titolo));
        descrizioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().descrizione));
        iscrizionimassimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().iscrizioniMassime));
        numerolezioniTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().numeroLezioni));
        tassopresenzeminimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getTassoPresenzeMinime()));
        privatoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getPrivato()));
        areeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().tag));
    }

    public void setOperatoriTableView(){
        emailoperatoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().email));
        nomeoperatoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().nome));
        cognomeoperatoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().cognome));
        codoperatoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().codOperatore));
        richiestaTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRichiesta()));
    }

    public void setStudentiTableView(){
        nomeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().nome));
        cognomeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().cognome));
        emailTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().email));
        codStudenteTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().codStudente));
        datadiNascitaTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().dataNascita));
        sessoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().sesso));
        idoneoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIdoneo()));
    }

    public void updateStudentiTableView(String codCorso) throws SQLException {
        studentiTableView.getItems().clear();
        listStudenti.addAll(studenteDAO.getStudentiAccettati(codCorso));
        studentiTableView.setItems(listStudenti);
    }

    private void eliminaOperatore(String codOperatore,String codCorso) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Conferma");
        dialog.setHeaderText("Per rimuovere l'operatore");
        dialog.setContentText("Inserisci 'si' :");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals("si")) {
                try {
                    operatoreDAO.annullaGestioneCorso(codOperatore,codCorso);
                    updateOperatoriTableView(codCorso);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione!");
                alert.setHeaderText("Devi inserire 'si' per rimuovere l'operatore!");
                alert.setContentText("Riprova!");
                alert.showAndWait();
            }
        }
    }


    private void visualizzaStatistiche(Corso corso) throws IOException {
        Stage statisticheStage=new Stage();
        FXMLLoader statistichePageLoader = new FXMLLoader(Main.class.getResource("profile/statistiche.fxml"));
        Parent statistichePane = statistichePageLoader.load();
        Scene statisticheScene = new Scene(statistichePane);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(statisticheScene);
        statisticheStage.setTitle("Inserisci Lezione");
        statisticheStage.setScene(statisticheScene);
        StatisticheController statistichecontroller = statistichePageLoader.getController();
        statistichecontroller.setStatistiche(corso);
        Stage primaryStage = (Stage) corsiTableView.getParent().getScene().getWindow();
        primaryStage.getScene().getRoot().setDisable(true);
        statisticheStage.showAndWait();
        primaryStage.getScene().getRoot().setDisable(false);
    }

    private void styleRowColor() {
        Callback<TableColumn<Operatore, String>, TableCell<Operatore, String>> cellFactory
                =
                new Callback<>() {
                    @Override
                    public TableCell<Operatore, String> call(final TableColumn<Operatore, String> param) {
                        return new TableCell<>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    setText(item);
                                    TableRow<Operatore> row = getTableRow();
                                        if (item.equals("In attesa")) {
                                            row.setStyle("-fx-background-color: #ffc1cc");
                                        }
                                }
                            }
                        };
                    }
                };
        richiestaTableColumn.setCellFactory(cellFactory);
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
                Utente utente = utenteDAO.getUtente(result.get());
                if (autenticazioneDAO.checkEmailUtenteExist(result.get())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Errore email");
                    alert.setHeaderText("L'email che hai inserito non risulta registrata!");
                    alert.showAndWait();
                }
                else{
                    if(!studenteDAO.checkStudenteIscritto(corso.codCorso, studenteDAO.getCodStudente(utente.codiceFiscale))){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Attenzione!");
                        alert.setHeaderText(utente.nome+" "+ utente.cognome+" è già iscritto al corso "+corso.titolo+" come Studente");
                        alert.setContentText("Un Utente può essere Operatore o Studente di un Corso non entrambi!");
                        alert.showAndWait();
                    }
                    else{
                        if(!operatoreDAO.checkOperatoreCorso(operatoreDAO.getCodOperatore(utente.codiceFiscale), corso.codCorso)){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Attenzione!");
                            alert.setHeaderText(utente.nome+" "+ utente.cognome+" è già operatore o ha una richiesta in sospeso per il corso "+corso.titolo);
                            alert.setContentText("Lo stato della richiesta è visibile dal colore della riga. Se la riga è bianca lo stato è accettata!");
                            alert.showAndWait();
                        }
                        else{
                            if(operatoreDAO.checkOperatoreExist(utente.codiceFiscale)){
                                operatore.setCodOperatore(operatoreDAO.setOperatore(utente.codiceFiscale));
                            }
                            else{
                                operatore.setCodOperatore(operatoreDAO.getCodOperatore(utente.codiceFiscale));
                            }
                            operatoreDAO.associaOperatore(operatore.codOperatore, corso.codCorso);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Attenzione!");
                            alert.setHeaderText("E' stata inviata una richiesta ad "+ utente.nome+" "+ utente.cognome+" per diventare operatore del corso "+corso.titolo);
                            alert.setContentText("Lo stato della richiesta è visibile dal colore della riga. Se la riga è bianca lo stato è accettata!");
                            alert.showAndWait();
                            updateOperatoriTableView(corso.codCorso);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void modificaCorso(Corso corso) throws IOException {
        Stage corsoStage=new Stage();
        FXMLLoader corsoPageLoader = new FXMLLoader(Main.class.getResource("corso.fxml"));
        corsoPageLoader.setControllerFactory((Class<?> controllerType) ->{
            if(controllerType == CorsoController.class){
                return new CorsoModificaController();
            }
            return new CorsoModificaController();
        });
        Parent corsoPane = corsoPageLoader.load();
        Scene corsoScene = new Scene(corsoPane, 400, 600);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(corsoScene);
        CorsoModificaController corsoModificaController=corsoPageLoader.getController();
        corsoModificaController.setCorso(corso);
        corsoStage.setResizable(false);
        corsoStage.setTitle("Modifica Corso");
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
        TreeItem<Corso> fakeroot=new TreeItem<>();
        corsiTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        gestore.setCorsi(corsoDAO.getCorsi(gestore.codGestore));
        for(Corso i: gestore.corsi){
                TreeItem<Corso> treeItem=new TreeItem<>(i);
                for(AreaTematica areaTematica:i.areetematiche){
                    Corso corso=new Corso();
                    corso.setTag(areaTematica.tag);
                    TreeItem<Corso> tagItem=new TreeItem<>(corso);
                    treeItem.getChildren().add(tagItem);
            }
            fakeroot.getChildren().add(treeItem);
        }
        corsiTableView.setShowRoot(false);
    }

    public void updateOperatoriTableView(String codCorso) throws SQLException {
        operatoriTableView.getItems().clear();
        corso.setOperatori(operatoreDAO.getOperatori(codCorso));
        listOperatori.addAll(corso.operatori);
        operatoriTableView.setItems(listOperatori);
        styleRowColor();
    }

    public void setProfileGestore(Stage primaryStage,String email, String password) {
        loginStage=primaryStage;
        autenticazione.setEmail(email);
        autenticazione.setPassword(password);
        try {
            gestore=gestoreDAO.getGestore(email);
            nomeGestoreMenu.setText(gestore.nome);
            nomeGestoreMenu.setStyle("-fx-font-weight: bold");
            updateCorsiTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void oncloseButtonMenuClick() {
        Stage secondaryStage = (Stage) (gestoreMenuBar.getScene().getWindow());
        secondaryStage.close();
        loginStage.show();
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
        String codicediverifica = sendVerificationEmail.SendEmail(autenticazione.getEmail());
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
