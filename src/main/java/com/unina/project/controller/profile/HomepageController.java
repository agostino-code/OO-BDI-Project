package com.unina.project.controller.profile;

import com.unina.project.*;
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

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
    public static class CorsoIdoneo extends Corso{
        public String getIdoneo() {
            if (Idoneo == null) {
                return null;
            }
            if(Idoneo){
                return "Si";
            }
            else{
                return "No";
            }
        }

        public void setIdoneo(Boolean idoneo) {
            Idoneo = idoneo;
        }

        Boolean Idoneo;
    }

    @FXML
    public TreeTableView<CorsoIdoneo> corsiTableView;
    public TreeTableColumn<CorsoIdoneo,String> codCorsoTableColumn;
    public TreeTableColumn<CorsoIdoneo,String> titoloTableColumn;
    public TreeTableColumn<CorsoIdoneo,String> descrizioneTableColumn;
    public TreeTableColumn<CorsoIdoneo,String> idoneoTableColumn;
    public TreeTableColumn<CorsoIdoneo,Integer> numerolezioniTableColumn;
    public TreeTableColumn<CorsoIdoneo,String> tassopresenzeminimeTableColumn;
    public TreeTableColumn<CorsoIdoneo,String> privatoTableColumn;
    public TreeTableColumn<CorsoIdoneo,String> areeTableColumn;

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
            TreeTableRow<CorsoIdoneo> row = new TreeTableRow<>();
            row.setContextMenu(contextMenu);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowData = row.getItem();
                    row.setContextMenu(contextMenu);
                }
            });
            return row ;
        });
        menuItem1.setOnAction((event) -> disiscriviCorso(rowData.getCodCorso(),studente.getCodStudente()));
    }

    public void setCorsiTableView(){
        codCorsoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getCodCorso()));
        titoloTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getTitolo()));
        descrizioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getDescrizione()));
        idoneoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getIdoneo()));
        numerolezioniTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getNumeroLezioni()));
        tassopresenzeminimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getTassoPresenzeMinime()));
        privatoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getPrivato()));
        areeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().tag));
    }

    public void setLezioniTableView(){
        codCorsoLezioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCodCorso()));
        titoloLezioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTitolo()));
        descrizioneLezioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescrizione()));
        dataTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataInizio()));
        oraTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOraInizio()));
        durataTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDurata()));
        codLezioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCodLezione()));
    }
    private void updateCorsiTableView() {
        TreeItem<CorsoIdoneo> fakeroot=new TreeItem<>();
        corsiTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        try {
            for(Corso i:corsoDAO.getCorsiStudente(studente.getCodStudente())){
                CorsoIdoneo corsoIdoneo = new CorsoIdoneo();
                corsoIdoneo.setTitolo(i.getTitolo());
                corsoIdoneo.setDescrizione(i.getDescrizione());
                corsoIdoneo.setCodCorso(i.getCodCorso());
                corsoIdoneo.setNumeroLezioni(i.getNumeroLezioni());
                corsoIdoneo.setTassoPresenzeMinime(i.tassoPresenzeMinime);
                corsoIdoneo.setPrivato(i.Privato);
                corsoIdoneo.setIdoneo(studenteDAO.getStudenteIdoneo(i.getCodCorso()));
                TreeItem<CorsoIdoneo> treeItem=new TreeItem<>(corsoIdoneo);
                for(AreaTematica areaTematica:i.getAreetematiche()){
                    CorsoIdoneo corso= new CorsoIdoneo();
                    corso.setTag(areaTematica.getTag());
                    TreeItem<CorsoIdoneo> tagItem=new TreeItem<>(corso);
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
        for(Lezione i:lezioneDAO.getLezioniPrenotate(studente.getCodStudente())){
            if(i.getDataoraInizio().isAfter(LocalDateTime.now())){
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
            studente.setCodStudente(studenteDAO.getCodStudente(utente.getCodiceFiscale()));
            updateCorsiTableView();
            updateLezioniTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
