package com.unina.project.controller.profile;

import com.unina.project.*;
import com.unina.project.database.CorsoDAO;
import com.unina.project.database.OperatoreDAO;
import com.unina.project.database.StudenteDAO;
import com.unina.project.database.postgre.PostgreCorsoDAO;
import com.unina.project.database.postgre.PostgreOperatoreDAO;
import com.unina.project.database.postgre.PostgreStudenteDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class IscrizioneCorsoController implements Initializable {



    @FXML
    private TreeTableView<CorsoRicerca> ricercacorsiTableView;
    public TreeTableColumn<CorsoRicerca,String> codiceCorsoTableColumn;
    public TreeTableColumn<CorsoRicerca,String> titoloTableColumn;
    public TreeTableColumn<CorsoRicerca,String> descrizioneTableColumn;
    public TreeTableColumn<CorsoRicerca,String> codiceGestoreTableColumn;
    public TreeTableColumn<CorsoRicerca,String> nomeGestoreTableColumn;
    public TreeTableColumn<CorsoRicerca,String> cittaGestoreTableColumn;
    public TreeTableColumn<CorsoRicerca,String> provinciaGestoreTableColumn;
    public TreeTableColumn<CorsoRicerca, String> areeTableColumn;
    public TreeTableColumn<CorsoRicerca,String> tipoTableColumn;
    public TreeTableColumn<CorsoRicerca,String> telefonoGestoreTableColumn;

    private Utente utente = new Utente();
    private CorsoRicerca rowData;
    private final CorsoDAO corsoDAO=new PostgreCorsoDAO();
    private final Studente studente=new Studente();
    private final StudenteDAO studenteDAO=new PostgreStudenteDAO();
    private final OperatoreDAO operatoreDAO=new PostgreOperatoreDAO();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Iscriviti");
        MenuItem menuItem2 = new MenuItem("Visualizza Dettagli");
        contextMenu.getItems().addAll(menuItem1,menuItem2);
        ricercacorsiTableView.setRowFactory( tv -> {
            TreeTableRow<CorsoRicerca> row = new TreeTableRow<>();
            row.setContextMenu(contextMenu);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowData = row.getItem();
                }
            });
            return row ;
        });
        menuItem1.setOnAction((event) -> {
            try {
                iscrizioneCorso(rowData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuItem2.setOnAction((event) -> {
            try {
                dettagliCorso(rowData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void iscrizioneCorso(CorsoRicerca corsoRicerca) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText("Vuoi iscriverti al corso "+corsoRicerca.titolo+"?");
        if(corsoRicerca.Privato){
            alert.setContentText("Il corso è Privato sarà inoltrata una richiesta all'Operatore.");
        }
        else{
            alert.setContentText("Il corso è Pubblico l'iscrizione è immediata.");
        }
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent())
        if (result.get() == ButtonType.OK){
            if(studenteDAO.checkStudenteExist(utente.getCodiceFiscale())){
                studente.setCodStudente(studenteDAO.setStudente(utente.getCodiceFiscale()));
            }
            else{
                studente.setCodStudente(studenteDAO.getCodStudente(utente.getCodiceFiscale()));
            }
            studenteDAO.iscriviStudente(studente.getCodStudente(), corsoRicerca.codCorso);
            setCorsoRicercaTableView();
            if(corsoRicerca.Privato){
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Attenzione!");
            alert2.setHeaderText(utente.getNome()+" "+utente.getCognome()+" ha inoltrato correttamente la richiesta per il corso "+corsoRicerca.titolo);
            alert2.showAndWait();
            }
            else{
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Attenzione!");
            alert2.setHeaderText(utente.getNome()+" "+utente.getCognome()+" ora è iscritto del corso "+corsoRicerca.titolo);
            alert2.showAndWait();
            }

        }
    }
    private void dettagliCorso(CorsoRicerca corsoRicerca) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dettagli");
        alert.setHeaderText("Il numero di lezioni è "+corsoRicerca.numeroLezioni+",\n" +
                "con un tasso di presenze minime del "+corsoRicerca.tassoPresenzeMinime+"%.\n" +
                "Il numero di iscritti sono "+corsoDAO.numeroIscrittiCorso(corsoRicerca.codCorso)+"/"+corsoRicerca.iscrizioniMassime+".");
        alert.show();
    }

    private void setCorsoRicercaTableView() {
        try {
            setDefaultRicercaTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        codiceGestoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().codGestore));
        titoloTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().titolo));
        telefonoGestoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().telefono));
        descrizioneTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().descrizione));
        codiceCorsoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().codCorso));
        nomeGestoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().nome));
        cittaGestoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().citta));
        provinciaGestoreTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().provincia));
        areeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().tag));
        tipoTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue().getPrivato()));
    }

    public void setDefaultRicercaTableView() throws SQLException {
        TreeItem<CorsoRicerca> fakeroot=new TreeItem<>();
        ricercacorsiTableView.setRoot(fakeroot);
        //ricercacorsiTableView.getRoot().getChildren().clear();
        String codOperatore=operatoreDAO.getCodOperatore(utente.getCodiceFiscale());
        String codStudente=studenteDAO.getCodStudente(utente.getCodiceFiscale());
        String SQL= "SELECT * FROM \"parametriricerca\" WHERE \"codCorso\" NOT IN" +
                "(SELECT \"codCorso\" FROM \"Coordina\" WHERE \"codOperatore\"='"+codOperatore+"') \n" +
                " AND \"codCorso\" NOT IN(SELECT \"codCorso\" FROM \"Iscritti\" WHERE \"codStudente\"='"+codStudente+"');";
        List<CorsoRicerca> corsoRicercaList=corsoDAO.ricercaCorsi(SQL);
        for(CorsoRicerca i: corsoRicercaList) {
            if (i.iscrizioniMassime > corsoDAO.numeroIscrittiCorso(i.codCorso)) {
                TreeItem<CorsoRicerca> treeItem = new TreeItem<>(i);
                for (AreaTematica areaTematica : i.areetematiche) {
                    CorsoRicerca corsoRicerca = new CorsoRicerca();
                    corsoRicerca.setTag(areaTematica.getTag());
                    TreeItem<CorsoRicerca> tagItem = new TreeItem<>(corsoRicerca);
                    treeItem.getChildren().add(tagItem);
                }
                fakeroot.getChildren().add(treeItem);
            }
        }
        ricercacorsiTableView.setShowRoot(false);
    }

    public void setRicercaTableView(String SQL, List<String> tags) throws SQLException {
        ricercacorsiTableView.getRoot().getChildren().clear();
        TreeItem<CorsoRicerca> fakeroot=new TreeItem<>();
        ricercacorsiTableView.setRoot(fakeroot);
        for(CorsoRicerca i: corsoDAO.ricercaCorsi(SQL)) {
            if (i.iscrizioniMassime > corsoDAO.numeroIscrittiCorso(i.codCorso)) {
                TreeItem<CorsoRicerca> treeItem = new TreeItem<>(i);
                List<String> tagscorso = new ArrayList<>();
                for (AreaTematica areaTematica : i.areetematiche) {
                    CorsoRicerca corsoRicerca = new CorsoRicerca();
                    corsoRicerca.setTag(areaTematica.getTag());
                    tagscorso.add(areaTematica.getTag());
                    TreeItem<CorsoRicerca> tagItem = new TreeItem<>(corsoRicerca);
                    treeItem.getChildren().add(tagItem);
                }
                boolean trovato=false;
                for(String tag:tags){
                    if(tagscorso.contains(tag)){
                        trovato=true;
                    }
                }
                if (trovato || tags.isEmpty()) {
                    fakeroot.getChildren().add(treeItem);
                }
            }
        }
        ricercacorsiTableView.setShowRoot(false);
    }

    @FXML
    void onCercaCorsoButtonClick(ActionEvent actionEvent) throws IOException {
        Stage cercaStage=new Stage();
        FXMLLoader cercaPageLoader = new FXMLLoader(Main.class.getResource("profile/ricercacorso.fxml"));
        Parent cercaPane = cercaPageLoader.load();
        Scene cercaScene = new Scene(cercaPane, 400, 550);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(cercaScene);
        cercaStage.setResizable(false);
        cercaStage.setTitle("Inserisci Indirizzo");
        cercaStage.setScene(cercaScene);
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.getScene().getRoot().setDisable(true);
        cercaStage.showAndWait();
        RicercaCorsoController ricercaCorsoController=cercaPageLoader.getController();
        List<String> tags=ricercaCorsoController.getTagsRicerca();
        String SQL=ricercaCorsoController.getParametriRicercaSQL();
            try {
                String codOperatore=operatoreDAO.getCodOperatore(utente.getCodiceFiscale());
                String codStudente=studenteDAO.getCodStudente(utente.getCodiceFiscale());
                SQL=SQL.concat("\"codCorso\" NOT IN" +
                        "(SELECT \"codCorso\" FROM \"Coordina\" WHERE \"codOperatore\"='"+codOperatore+"') \n" +
                        " AND \"codCorso\" NOT IN(SELECT \"codCorso\" FROM \"Iscritti\" WHERE \"codStudente\"='"+codStudente+"');");
                setRicercaTableView(SQL, tags);
                System.out.println(SQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        primaryStage.getScene().getRoot().setDisable(false);
    }

    public void setDatiUtente(Utente utente){
        this.utente=utente;
        setCorsoRicercaTableView();
    }
}
