package com.unina.project.controller.profile;

import com.unina.project.*;
import com.unina.project.database.CorsoDAO;
import com.unina.project.database.OperatoreDAO;
import com.unina.project.database.StudenteDAO;
import com.unina.project.database.postgre.PostgreCorsoDAO;
import com.unina.project.database.postgre.PostgreOperatoreDAO;
import com.unina.project.database.postgre.PostgreStudenteDAO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RichiesteController implements Initializable {
    
    @FXML
    public TableView<Studente> richiestecorsiTableView;
    public TableColumn<Studente,String> nomeTableColumn;
    public TableColumn<Studente,String> cognomeTableColumn;
    public TableColumn<Studente,String> emailTableColumn;
    public TableColumn<Studente,String> codStudenteTableColumn;
    public TableColumn<Studente,String> sessoTableColumn;
    public TableColumn<Studente, LocalDate> datadiNascitaTableColumn;

    @FXML
    public TreeTableView<Corso> corsiTableView;
    public TreeTableColumn<Corso,String> codCorsoTableColumn;
    public TreeTableColumn<Corso,String> titoloTableColumn;
    public TreeTableColumn<Corso,String> descrizioneTableColumn;
    public TreeTableColumn<Corso,Integer> iscrizionimassimeTableColumn;
    public TreeTableColumn<Corso,Integer> numerolezioniTableColumn;
    public TreeTableColumn<Corso,String> tassopresenzeminimeTableColumn;
    public TreeTableColumn<Corso,String> areeTableColumn;

    private Corso rowDataCorso=new Corso();
    private Studente rowDataStudente=new Studente();
    private final StudenteDAO studenteDAO=new PostgreStudenteDAO();
    private final Operatore operatore=new Operatore();
    private final OperatoreDAO operatoreDAO=new PostgreOperatoreDAO();
    private final CorsoDAO corsoDAO= new PostgreCorsoDAO();
    private final ObservableList<Studente> listStudentiDaAccettare = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCorsiTableView();
        setRichiesteTableView();
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Accetta Richiesta");
        MenuItem menuItem2 = new MenuItem("Elimina Richiesta");
        contextMenu.getItems().addAll(menuItem1,menuItem2);
        corsiTableView.setRowFactory( tv -> {
            TreeTableRow<Corso> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowDataCorso = row.getItem();
                    try {
                        updateRichiesteTableView(rowDataCorso.codCorso);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
        richiestecorsiTableView.setRowFactory( tv -> {
            TableRow<Studente> row = new TableRow<>();
            row.setContextMenu(contextMenu);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowDataStudente = row.getItem();
                }
            });
            return row ;
        });
        menuItem1.setOnAction((event) -> {
            try {
                accettaRichiesta(rowDataStudente,rowDataCorso);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuItem2.setOnAction((event) -> {
            try {
                eliminaRichiesta(rowDataStudente,rowDataCorso);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void accettaRichiesta(Studente studente,Corso corso) throws SQLException {
        studenteDAO.richiestaAccettata(studente.codStudente, corso.codCorso);
        updateRichiesteTableView(corso.codCorso);
    }
    public void eliminaRichiesta(Studente studente,Corso corso) throws SQLException {
        studenteDAO.richiestaRifiutata(studente.codStudente, corso.codCorso);
        updateRichiesteTableView(corso.codCorso);
    }

    public void setCorsiTableView(){
        codCorsoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().codCorso));
        titoloTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().titolo));
        descrizioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().descrizione));
        iscrizionimassimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().iscrizioniMassime));
        numerolezioniTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().numeroLezioni));
        tassopresenzeminimeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getTassoPresenzeMinime()));
        areeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().tag));
    }

    public void setRichiesteTableView(){
        nomeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().nome));
        cognomeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().cognome));
        emailTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().email));
        codStudenteTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().codStudente));
        datadiNascitaTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().dataNascita));
        sessoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().sesso));
    }

    public void updateCorsiTableView() throws SQLException {
        TreeItem<Corso> fakeroot=new TreeItem<>();
        corsiTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        operatore.setCorsi(corsoDAO.getCorsiOperatoreAccettati(operatore.codOperatore));
        for(Corso i: operatore.corsi){
            if(i.Privato){
                TreeItem<Corso> treeItem=new TreeItem<>(i);
                for(AreaTematica areaTematica:i.areetematiche){
                    Corso corso=new Corso();
                    corso.setTag(areaTematica.tag);
                    TreeItem<Corso> tagItem=new TreeItem<>(corso);
                    treeItem.getChildren().add(tagItem);
                }
                fakeroot.getChildren().add(treeItem);
            }
        }
        corsiTableView.setShowRoot(false);
    }

    public void updateRichiesteTableView(String codCorso) throws SQLException {
        richiestecorsiTableView.getItems().clear();
        listStudentiDaAccettare.addAll(studenteDAO.getStudentiDaAccettare(codCorso));
        richiestecorsiTableView.setItems(listStudentiDaAccettare);
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
