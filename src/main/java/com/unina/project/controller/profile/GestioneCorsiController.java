package com.unina.project.controller.profile;

import com.unina.project.*;
import com.unina.project.database.CorsoDAO;
import com.unina.project.database.OperatoreDAO;
import com.unina.project.database.StudenteDAO;
import com.unina.project.database.postgre.PostgreCorsoDAO;
import com.unina.project.database.postgre.PostgreOperatoreDAO;
import com.unina.project.database.postgre.PostgreStudenteDAO;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class GestioneCorsiController implements Initializable {

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
    public TreeTableView<Corso> corsiTableView;
    public TreeTableColumn<Corso,String> codCorsoTableColumn;
    public TreeTableColumn<Corso,String> titoloTableColumn;
    public TreeTableColumn<Corso,String> descrizioneTableColumn;
    public TreeTableColumn<Corso,Integer> iscrizionimassimeTableColumn;
    public TreeTableColumn<Corso,Integer> numerolezioniTableColumn;
    public TreeTableColumn<Corso,String> tassopresenzeminimeTableColumn;
    public TreeTableColumn<Corso,String> areeTableColumn;
    public TreeTableColumn<Corso,String> privatoTableColumn;

    private Corso rowDataCorso=new Corso();
    private Studente rowDataStudente=new Studente();
    private final StudenteDAO studenteDAO=new PostgreStudenteDAO();
    private final Operatore operatore=new Operatore();
    private final OperatoreDAO operatoreDAO=new PostgreOperatoreDAO();
    private final CorsoDAO corsoDAO= new PostgreCorsoDAO();
    private final ObservableList<Studente> listStudenti = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCorsiTableView();
        setStudentiTableView();
        ContextMenu contextMenuAccetta = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Accetta Richiesta di Gestione");
        MenuItem menuItem2 = new MenuItem("Elimina Richiesta di Gestione");
        contextMenuAccetta.getItems().addAll(menuItem1,menuItem2);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem3 = new MenuItem("Visualizza statistiche Corso");
        contextMenu.getItems().addAll(menuItem3);
        corsiTableView.setRowFactory( tv -> {
            TreeTableRow<Corso> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowDataCorso = row.getItem();
                    try {
                        if(operatoreDAO.checkOperatoreDaAccettare(operatore.getCodOperatore(), rowDataCorso.getCodCorso())){
                            row.setContextMenu(contextMenuAccetta);
                        }
                        else{
                            row.setContextMenu(contextMenu);
                            updateStudentiTableView(rowDataCorso.getCodCorso());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
        ContextMenu contextMenuStudente = new ContextMenu();
        MenuItem menuItem4 = new MenuItem("Rimuovi Studente");
        contextMenuStudente.getItems().addAll(menuItem4);
        studentiTableView.setRowFactory( tv -> {
            TableRow<Studente> row = new TableRow<>();
            row.setContextMenu(contextMenuStudente);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowDataStudente = row.getItem();
                }
            });
            return row ;
        });
        menuItem1.setOnAction((event) -> {
            try {
                accettaRichiesta(operatore.getCodOperatore(),rowDataCorso);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuItem2.setOnAction((event) -> {
            try {
                eliminaRichiesta(operatore.getCodOperatore(),rowDataCorso);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuItem3.setOnAction((event) -> {
            try {
                visualizzaStatistiche(rowDataCorso);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        menuItem4.setOnAction((event) -> {
            try {
                eliminaStudente();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void eliminaStudente() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText("Vuoi davvero rimuovere lo studente "+rowDataStudente.getNome()+" "+rowDataStudente.getCognome()+" dal Corso "+ rowDataCorso.getTitolo()+"?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent())
        if (result.get() == ButtonType.OK){
            studenteDAO.richiestaRifiutata(rowDataStudente.getCodStudente(), rowDataCorso.getCodCorso());
        }

    }
    public void accettaRichiesta(String codOperatore,Corso corso) throws SQLException {
        operatoreDAO.accettaGestioneCorso(codOperatore, corso.getCodCorso());
        updateCorsiTableView();
        updateStudentiTableView(corso.getCodCorso());
    }
    public void eliminaRichiesta(String codOperatore,Corso corso) throws SQLException {
        operatoreDAO.annullaGestioneCorso(codOperatore, corso.getCodCorso());
        updateCorsiTableView();
    }

    private void visualizzaStatistiche(Corso corso) throws IOException {
        Stage statisticheStage=new Stage();
        FXMLLoader statistichePageLoader = new FXMLLoader(Main.class.getResource("profile/statistiche.fxml"));
        Parent statistichePane = statistichePageLoader.load();
        Scene statisticheScene = new Scene(statistichePane);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(statisticheScene);
        statisticheStage.setTitle("Statistiche Corso: "+corso.getCodCorso());
        statisticheStage.setScene(statisticheScene);
        StatisticheController statistichecontroller = statistichePageLoader.getController();
        statistichecontroller.setStatistiche(corso);
        Stage primaryStage = (Stage) corsiTableView.getParent().getScene().getWindow();
        primaryStage.getScene().getRoot().setDisable(true);
        statisticheStage.showAndWait();
        primaryStage.getScene().getRoot().setDisable(false);
    }

    public void setCorsiTableView(){
        codCorsoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getCodCorso()));
        titoloTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getTitolo()));
        descrizioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getDescrizione()));
        iscrizionimassimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getIscrizioniMassime()));
        numerolezioniTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getNumeroLezioni()));
        tassopresenzeminimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getTassoPresenzeMinime()));
        privatoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getPrivato()));
        areeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().tag));
    }

    public void setStudentiTableView(){
        nomeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNome()));
        cognomeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCognome()));
        emailTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmail()));
        codStudenteTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCodStudente()));
        datadiNascitaTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().dataNascita));
        sessoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().sesso));
        idoneoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIdoneo()));
    }

    public void updateCorsiTableView() throws SQLException {
        TreeItem<Corso> fakeroot=new TreeItem<>();
        corsiTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        operatore.setCorsi(corsoDAO.getCorsiOperatore(operatore.getCodOperatore()));
        for(Corso i: operatore.getCorsi()){
                TreeItem<Corso> treeItem=new TreeItem<>(i);
                for(AreaTematica areaTematica:i.getAreetematiche()) {
                    Corso corso = new Corso();
                    corso.setTag(areaTematica.getTag());
                    TreeItem<Corso> tagItem = new TreeItem<>(corso);
                    treeItem.getChildren().add(tagItem);
                }
                fakeroot.getChildren().add(treeItem);
            }
        corsiTableView.setShowRoot(false);
        styleRowColor();
    }

    private void styleRowColor() {
        Callback<TreeTableColumn<Corso, String>, TreeTableCell<Corso, String>> cellFactory
                =
                new Callback<>() {
                    @Override
                    public TreeTableCell<Corso, String> call(final TreeTableColumn<Corso, String> param) {
                        return new TreeTableCell<>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    setText(item);
                                    TreeTableRow<Corso> row = getTableRow();
                                    try {
                                        if (operatoreDAO.checkOperatoreDaAccettare(operatore.getCodOperatore(), item)) {
                                            row.setStyle("-fx-background-color: #ffc1cc");
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                    }
                };
        codCorsoTableColumn.setCellFactory(cellFactory);
    }

    public void updateStudentiTableView(String codCorso) throws SQLException {
        studentiTableView.getItems().clear();
        listStudenti.addAll(studenteDAO.getStudentiAccettati(codCorso));
        studentiTableView.setItems(listStudenti);
    }

    public void setDatiUtente(Utente utente){
        try {
            operatore.setCodOperatore(operatoreDAO.getCodOperatore(utente.getCodiceFiscale()));
            updateCorsiTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
