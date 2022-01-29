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
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

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
                        if(operatoreDAO.checkOperatoreDaAccettare(operatore.codOperatore, rowDataCorso.codCorso)){
                            row.setContextMenu(contextMenuAccetta);
                        }
                        else{
                            row.setContextMenu(contextMenu);
                            updateStudentiTableView(rowDataCorso.codCorso);
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
                accettaRichiesta(operatore.codOperatore,rowDataCorso);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuItem2.setOnAction((event) -> {
            try {
                eliminaRichiesta(operatore.codOperatore,rowDataCorso);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuItem3.setOnAction((event) -> {

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
        alert.setHeaderText("Vuoi davvero rimuovere lo studente "+rowDataStudente.nome+" "+rowDataStudente.cognome+" dal Corso "+ rowDataCorso.titolo+"?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent())
        if (result.get() == ButtonType.OK){
            studenteDAO.richiestaRifiutata(rowDataStudente.codStudente, rowDataCorso.codCorso);
        }

    }
    public void accettaRichiesta(String codOperatore,Corso corso) throws SQLException {
        operatoreDAO.accettaGestioneCorso(codOperatore, corso.codCorso);
        updateCorsiTableView();
        updateStudentiTableView(corso.codCorso);
    }
    public void eliminaRichiesta(String codOperatore,Corso corso) throws SQLException {
        operatoreDAO.annullaGestioneCorso(codOperatore, corso.codCorso);
        updateCorsiTableView();
    }

    public void setCorsiTableView(){
        codCorsoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().codCorso));
        titoloTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().titolo));
        descrizioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().descrizione));
        iscrizionimassimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().iscrizioniMassime));
        numerolezioniTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().numeroLezioni));
        tassopresenzeminimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getTassoPresenzeMinime()));
        privatoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getPrivato()));
        areeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().tag));
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

    public void updateCorsiTableView() throws SQLException {
        TreeItem<Corso> fakeroot=new TreeItem<>();
        corsiTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        operatore.setCorsi(corsoDAO.getCorsiOperatore(operatore.codOperatore));
        for(Corso i: operatore.corsi){
                TreeItem<Corso> treeItem=new TreeItem<>(i);
                for(AreaTematica areaTematica:i.areetematiche) {
                    Corso corso = new Corso();
                    corso.setTag(areaTematica.tag);
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
                                        if (operatoreDAO.checkOperatoreDaAccettare(operatore.codOperatore, item)) {
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
            operatore.codOperatore=operatoreDAO.getCodOperatore(utente.codiceFiscale);
            updateCorsiTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
