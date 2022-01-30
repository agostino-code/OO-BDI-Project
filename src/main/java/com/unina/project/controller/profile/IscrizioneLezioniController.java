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

    private Lezione rowDataLezione=new Lezione();
    private final Studente studente=new Studente();
    private final LezioneDAO lezioneDAO=new PostgreLezioneDAO();
    private final StudenteDAO studenteDAO=new PostgreStudenteDAO();
    private final CorsoDAO corsoDAO= new PostgreCorsoDAO();

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
                        if(rowDataLezione.getCodLezione()!=null){
                            if (lezioneDAO.checkStudenteIscrittoLezione(rowDataLezione.getCodLezione(), studente.getCodStudente())) {
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
                lezioneDAO.prenotaLezione(rowDataLezione.getCodLezione(), studente.getCodStudente());
                updateLezioniTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        menuItem2.setOnAction((event) -> {
            try {
                lezioneDAO.deletePrenotazioneLezione(rowDataLezione.getCodLezione(), studente.getCodStudente());
                updateLezioniTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void setLezioniTableView(){
        codCorsoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getCodCorso()));
        titoloTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getTitolo()));
        descrizioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getDescrizione()));
        dataTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getDataInizio()));
        oraTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getOraInizio()));
        durataTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getDurata()));
        codLezioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getCodLezione()));
    }

    public void updateLezioniTableView() throws SQLException {
        TreeItem<Lezione> fakeroot=new TreeItem<>();
        lezioniTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        studente.setCorsi(corsoDAO.getCorsiStudente(studente.getCodStudente()));
        for(Corso i: studente.getCorsi()){
            Lezione onlycodcorso=new Lezione();
            onlycodcorso.setCodCorso(i.getCodCorso());
            TreeItem<Lezione> treeItem=new TreeItem<>(onlycodcorso);
            for(Lezione lezione:lezioneDAO.getLezioni(i.getCodCorso())) {
                if(lezione.getDataoraInizio().isAfter(LocalDateTime.now())){
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
                        return new TreeTableCell<>() {

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
                                        if (lezioneDAO.checkStudenteIscrittoLezione(item,studente.getCodStudente())) {
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
                    }
                };
        codLezioneTableColumn.setCellFactory(cellFactory);

    }

    public void setDatiUtente(Utente utente){
        try {
            studente.setCodStudente(studenteDAO.getCodStudente(utente.getCodiceFiscale()));
            updateLezioniTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
