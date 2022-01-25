package com.unina.project.controller.profile;

import com.unina.project.*;
import com.unina.project.database.CorsoDAO;
import com.unina.project.database.LezioneDAO;
import com.unina.project.database.OperatoreDAO;
import com.unina.project.database.postgre.PostgreCorsoDAO;
import com.unina.project.database.postgre.PostgreLezioneDAO;
import com.unina.project.database.postgre.PostgreOperatoreDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.LocalTime;
import java.util.ResourceBundle;

public class GestioneLezioniController implements Initializable {

    @FXML
    public TableView<Studente> studentiTableView;
    public TableColumn<Studente,String> nomeTableColumn;
    public TableColumn<Studente,String> cognomeTableColumn;
    public TableColumn<Studente,String> emailTableColumn;
    public TableColumn<Studente,String> codStudenteTableColumn;
    public TableColumn<Studente,String> sessoTableColumn;
    public TableColumn<Studente, LocalDate> datadiNascitaTableColumn;
    public TableColumn<Studente,String> presenteTableColumn;

    @FXML
    public TreeTableView<Lezione> lezioniTableView;
    public TreeTableColumn<Lezione,String> codCorsoTableColumn;
    public TreeTableColumn<Lezione,String> titoloTableColumn;
    public TreeTableColumn<Lezione,String> descrizioneTableColumn;
    public TreeTableColumn<Lezione,LocalDate> dataTableColumn;
    public TreeTableColumn<Lezione,LocalTime> oraTableColumn;
    public TreeTableColumn<Lezione,LocalTime> durataTableColumn;
    public TreeTableColumn<Lezione,String> codLezioneTableColumn;

    private Lezione rowDataLezione=new Lezione();
    private Studente rowDataStudente=new Studente();
    private final LezioneDAO lezioneDAO=new PostgreLezioneDAO();
    private final Operatore operatore=new Operatore();
    private final OperatoreDAO operatoreDAO=new PostgreOperatoreDAO();
    private final CorsoDAO corsoDAO= new PostgreCorsoDAO();
    private final ObservableList<Studente> listStudenti = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLezioniTableView();
        setStudentiTableView();
        ContextMenu contextMenuPresente = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Presente");
        contextMenuPresente.getItems().addAll(menuItem1);
        ContextMenu contextMenuNuovaLezione = new ContextMenu();
        MenuItem menuItem2 = new MenuItem("Nuova Lezione");
        contextMenuNuovaLezione.getItems().addAll(menuItem2);
        ContextMenu contextMenuNonPresente = new ContextMenu();
        MenuItem menuItem3 = new MenuItem("Non Presente");
        contextMenuNonPresente.getItems().addAll(menuItem3);
        lezioniTableView.setRowFactory( tv -> {
            TreeTableRow<Lezione> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowDataLezione = row.getItem();
                    try {
                        if(rowDataLezione.codCorso!=null){
                            row.setContextMenu(contextMenuNuovaLezione);
                        }
                        else{
                            updateStudentiTableView(rowDataLezione.codLezione);
                        }


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
        studentiTableView.setRowFactory( tv -> {
            TableRow<Studente> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowDataStudente = row.getItem();
                    if(rowDataStudente.getPresente().equals("Si")){
                        row.setContextMenu(contextMenuNonPresente);
                    }
                    else{
                        row.setContextMenu(contextMenuPresente);
                    }

                }
            });
            return row ;
        });
        menuItem1.setOnAction((event) -> {
            try {
                lezioneDAO.confermapresenza(rowDataStudente.codStudente,rowDataLezione.codLezione);
                updateStudentiTableView(rowDataLezione.codLezione);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuItem3.setOnAction((event) -> {
            try {
                lezioneDAO.nonpresente(rowDataStudente.codStudente,rowDataLezione.codLezione);
                updateStudentiTableView(rowDataLezione.codLezione);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuItem2.setOnAction((event) -> {
            try {
                nuovalezione(rowDataLezione.codCorso);
                updateLezioniTableView();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }

        });
    }

    public void setLezioniTableView(){
        codCorsoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().codCorso));
        titoloTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().titolo));
        descrizioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().descrizione));
        dataTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getDataInizio()));
        oraTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getOraInizio()));
        durataTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().durata));
        codLezioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().codLezione));
    }

    public void setStudentiTableView(){
        nomeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().nome));
        cognomeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().cognome));
        emailTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().email));
        codStudenteTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().codStudente));
        datadiNascitaTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().dataNascita));
        sessoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().sesso));
        presenteTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPresente()));
    }

    public void nuovalezione(String codCorso) throws IOException {
        Stage lezioneStage=new Stage();
        FXMLLoader lezionePageLoader = new FXMLLoader(Main.class.getResource("profile/lezione.fxml"));
        Parent lezionePane = lezionePageLoader.load();
        Scene lezioneScene = new Scene(lezionePane, 400, 400);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(lezioneScene);
        lezioneStage.setResizable(false);
        lezioneStage.setTitle("Inserisci Lezione");
        lezioneStage.setScene(lezioneScene);
        LezioneController lezionecontroller = lezionePageLoader.getController();
        lezionecontroller.setcodCorso(codCorso);
        Stage primaryStage = (Stage) studentiTableView.getParent().getScene().getWindow();
        primaryStage.getScene().getRoot().setDisable(true);
        lezioneStage.showAndWait();
        primaryStage.getScene().getRoot().setDisable(false);
        try {
            updateLezioniTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateLezioniTableView() throws SQLException {
        TreeItem<Lezione> fakeroot=new TreeItem<>();
        lezioniTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        operatore.setCorsi(corsoDAO.getCorsiOperatoreAccettati(operatore.codOperatore));
        for(Corso i: operatore.corsi){
            Lezione onlycodcorso=new Lezione();
            onlycodcorso.setCodCorso(i.codCorso);
            TreeItem<Lezione> treeItem=new TreeItem<>(onlycodcorso);
            for(Lezione lezione:lezioneDAO.getLezioni(i.codCorso)) {
                TreeItem<Lezione> tagItem = new TreeItem<>(lezione);
                treeItem.getChildren().add(tagItem);
            }
            fakeroot.getChildren().add(treeItem);
        }
        lezioniTableView.setShowRoot(false);
    }

    private void styleRowColor() {
        Callback<TableColumn<Studente, String>, TableCell<Studente, String>> cellFactory
                =
                new Callback<>() {
                    @Override
                    public TableCell<Studente, String> call(final TableColumn<Studente, String> param) {
                        return new TableCell<>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    setText(item);
                                    TableRow<Studente> row = getTableRow();
                                        if (item.equals("No")) {
                                            row.setStyle("-fx-background-color: #ffc1cc");
                                        }
                                        else{
                                            row.setStyle("-fx-background-color: #96ff00");
                                        }
                                }
                            }
                        };
                    }
                };
        presenteTableColumn.setCellFactory(cellFactory);

    }

    public void updateStudentiTableView(String codLezione) throws SQLException {
        studentiTableView.getItems().clear();
        listStudenti.addAll(lezioneDAO.getStudentiPrenotati(codLezione));
        studentiTableView.setItems(listStudenti);
        styleRowColor();
    }

    public void setDatiUtente(Utente utente){
        try {
            operatore.codOperatore=operatoreDAO.getCodOperatore(utente.codiceFiscale);
            updateLezioniTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
