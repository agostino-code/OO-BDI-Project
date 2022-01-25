package com.unina.project.controller.profile;

import com.unina.project.*;
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

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
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
    public TableView<Lezione> lezioniTableView;
    public TableColumn<Lezione,String> codCorsoLezioneTableColumn;
    public TableColumn<Lezione,String> titoloLezioneTableColumn;
    public TableColumn<Lezione,String> descrizioneLezioneTableColumn;
    public TableColumn<Lezione, LocalDate> dataTableColumn;
    public TableColumn<Lezione, LocalTime> oraTableColumn;
    public TableColumn<Lezione,LocalTime> durataTableColumn;
    public TableColumn<Lezione,String> codLezioneTableColumn;

    private final Studente studente=new Studente();
    private final StudenteDAO studenteDAO=new PostgreStudenteDAO();
    private final CorsoDAO corsoDAO =new PostgreCorsoDAO();
    private final LezioneDAO lezioneDAO=new PostgreLezioneDAO();
    private Corso rowData=new Corso();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCorsiTableView();
        setLezioniTableView();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Esci dal corso");
        contextMenu.getItems().addAll(menuItem1);
        corsiTableView.setRowFactory( tv -> {
            TreeTableRow<Corso> row = new TreeTableRow<>();
            row.setContextMenu(contextMenu);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowData = row.getItem();
                    row.setContextMenu(contextMenu);
                }
            });
            return row ;
        });
        menuItem1.setOnAction((event) -> disiscriviCorso(rowData.codCorso,studente.codStudente));
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

    public void setLezioniTableView(){
        codCorsoLezioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().codCorso));
        titoloLezioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().titolo));
        descrizioneLezioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().descrizione));
        dataTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataInizio()));
        oraTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOraInizio()));
        durataTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().durata));
        codLezioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().codLezione));
    }
    private void updateCorsiTableView() {
        TreeItem<Corso> fakeroot=new TreeItem<>();
        corsiTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        try {
            for(Corso i:corsoDAO.getCorsiStudente(studente.codStudente)){
                TreeItem<Corso> treeItem=new TreeItem<>(i);
                for(AreaTematica areaTematica:i.areetematiche){
                    Corso corso=new Corso();
                    corso.setTag(areaTematica.tag);
                    TreeItem<Corso> tagItem=new TreeItem<>(corso);
                    treeItem.getChildren().add(tagItem);
                }
                fakeroot.getChildren().add(treeItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        corsiTableView.setShowRoot(false);
    }

    public void updateLezioniTableView() throws SQLException {
        lezioniTableView.getItems().clear();
        for(Lezione i:lezioneDAO.getLezioniPrenotate(studente.codStudente)){
            if(i.dataoraInizio.isAfter(LocalDateTime.now())){
                lezioniTableView.getItems().add(i);
            }
        }

    }

    public void disiscriviCorso(String codCorso,String codStudente){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Conferma");
        dialog.setHeaderText("Per uscire dal corso");
        dialog.setContentText("Inserisci 'si' :");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals("si")) {
                try {
                    corsoDAO.disiscrivitiCorso(codStudente,codCorso);
                    updateCorsiTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione!");
                alert.setHeaderText("Devi inserire 'si' per uscire dal corso!");
                alert.setContentText("Riprova!");
                alert.showAndWait();
            }
        }
    }

    public void setDatiUtente(Utente utente){
        try {
            studente.codStudente=studenteDAO.getCodStudente(utente.codiceFiscale);
            updateCorsiTableView();
            updateLezioniTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
