package com.unina.project.controller.profile;

import com.unina.project.Corso;
import com.unina.project.Lezione;
import com.unina.project.Studente;
import com.unina.project.Utente;
import com.unina.project.database.CorsoDAO;
import com.unina.project.database.LezioneDAO;
import com.unina.project.database.StudenteDAO;
import com.unina.project.database.postgre.PostgreCorsoDAO;
import com.unina.project.database.postgre.PostgreLezioneDAO;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class IscrizioneLezioniController implements Initializable {

    @FXML
    public TreeTableView<Lezione> lezioniTableView;
    public TreeTableColumn<Lezione,String> codCorsoTableColumn;
    public TreeTableColumn<Lezione,String> titoloTableColumn;
    public TreeTableColumn<Lezione,String> descrizioneTableColumn;
    public TreeTableColumn<Lezione,LocalDate> dataTableColumn;
    public TreeTableColumn<Lezione, LocalTime> oraTableColumn;
    public TreeTableColumn<Lezione,LocalTime> durataTableColumn;
    public TreeTableColumn<Lezione,String> codLezioneTableColumn;

    private Utente utente=new Utente();
    private Lezione rowDataLezione=new Lezione();
    private Studente studente=new Studente();
    private LezioneDAO lezioneDAO=new PostgreLezioneDAO();
    private StudenteDAO studenteDAO=new PostgreStudenteDAO();
    private CorsoDAO corsoDAO= new PostgreCorsoDAO();
    private ObservableList<Studente> listStudenti = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLezioniTableView();
        ContextMenu contextMenu= new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Prenota");
        contextMenu.getItems().addAll(menuItem1);
        ContextMenu contextMenuDelete= new ContextMenu();
        MenuItem menuItem2 = new MenuItem("Elimina Prenotazione");
        contextMenuDelete.getItems().addAll(menuItem2);
        lezioniTableView.setRowFactory( tv -> {
            TreeTableRow<Lezione> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowDataLezione = row.getItem();
                    try {
                        if(rowDataLezione.codLezione!=null){
                            if (studenteDAO.checkStudenteIscritto(rowDataLezione.codLezione, studente.codStudente)) {
                                row.setContextMenu(contextMenu);
                            }
                            else{
                                row.setContextMenu(contextMenuDelete);
                            }
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
        menuItem1.setOnAction((event) -> {
            try {
                lezioneDAO.prenotaLezione(rowDataLezione.codLezione, studente.codStudente);
                updateLezioniTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        menuItem2.setOnAction((event) -> {
            try {
                lezioneDAO.deletePrenotazioneLezione(rowDataLezione.codLezione, studente.codStudente);
                updateLezioniTableView();
            } catch (SQLException e) {
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

    public void updateLezioniTableView() throws SQLException {
        TreeItem<Lezione> fakeroot=new TreeItem<>();
        lezioniTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        studente.setCorsi(corsoDAO.getCorsiStudente(studente.codStudente));
        for(Corso i: studente.corsi){
            Lezione onlycodcorso=new Lezione();
            onlycodcorso.setCodCorso(i.codCorso);
            TreeItem<Lezione> treeItem=new TreeItem<>(onlycodcorso);
            for(Lezione lezione:lezioneDAO.getLezioni(i.codCorso)) {
                if(lezione.dataoraInizio.isAfter(LocalDateTime.now())){
                    TreeItem<Lezione> tagItem = new TreeItem<>(lezione);
                    treeItem.getChildren().add(tagItem);
                }
            }
            fakeroot.getChildren().add(treeItem);
        }
        lezioniTableView.setShowRoot(false);
        styleRowColor();
    }

    private void styleRowColor() {
        Callback<TreeTableColumn<Lezione, String>, TreeTableCell<Lezione, String>> cellFactory
                =
                new Callback<>() {
                    @Override
                    public TreeTableCell<Lezione, String> call(final TreeTableColumn<Lezione, String> param) {
                        final TreeTableCell<Lezione, String> cell = new TreeTableCell<>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    setText(item);
                                    TreeTableRow<Lezione> row = getTableRow();
                                    try {
                                        if(item!=null)
                                        if (studenteDAO.checkStudenteIscritto(item,studente.codStudente)) {
                                            row.setStyle("-fx-background-color: #ffc1cc");
                                        }
                                        else{
                                            row.setStyle("-fx-background-color: #96ff00");
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        return cell;
                    }
                };
        codLezioneTableColumn.setCellFactory(cellFactory);

    }

    public void setDatiUtente(Utente utente){
        this.utente=utente;
        try {
            studente.codStudente=studenteDAO.getCodStudente(utente.codiceFiscale);
            updateLezioniTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
