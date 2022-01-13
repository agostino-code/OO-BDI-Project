package com.unina.project.controller.profile;

import com.unina.project.AreaTematica;
import com.unina.project.Main;
import com.unina.project.database.CorsoDAO;
import com.unina.project.database.postgre.PostgreCorsoDAO;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    @FXML
    private Button cercaButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCorsoRicercaTableView();
    }
    private CorsoDAO corsoDAO=new PostgreCorsoDAO();

    private void setCorsoRicercaTableView() {
        try {
            setDefaultRicercaTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        codiceGestoreTableColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CorsoRicerca, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().codGestore)
        );
        titoloTableColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CorsoRicerca, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().titolo)
        );
        descrizioneTableColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CorsoRicerca, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().descrizione)
        );
        codiceCorsoTableColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CorsoRicerca, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().codCorso)
        );
        nomeGestoreTableColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CorsoRicerca, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().nome)
        );
        cittaGestoreTableColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CorsoRicerca, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().citta)
        );
        provinciaGestoreTableColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CorsoRicerca, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().provincia)
        );
        areeTableColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CorsoRicerca, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().tag)
        );
        tipoTableColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<CorsoRicerca, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getPrivato())
        );

    }

    public void setDefaultRicercaTableView() throws SQLException {
        TreeItem<CorsoRicerca> fakeroot=new TreeItem<>();
        ricercacorsiTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        for(CorsoRicerca i: corsoDAO.ricercaCorsi("SELECT * FROM \"parametriricerca\";")){
            TreeItem<CorsoRicerca> treeItem=new TreeItem<>(i);
            for(AreaTematica areaTematica:i.areetematiche){
                CorsoRicerca corsoRicerca=new CorsoRicerca();
                corsoRicerca.setTag(areaTematica.tag);
                TreeItem<CorsoRicerca> tagItem=new TreeItem<>(corsoRicerca);
                treeItem.getChildren().add(tagItem);
            }
            fakeroot.getChildren().add(treeItem);
        }
        ricercacorsiTableView.setShowRoot(false);
    }

    public void setRicercaTableView(String SQL, List<String> tags) throws SQLException {
        TreeItem<CorsoRicerca> fakeroot=new TreeItem<>();
        ricercacorsiTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        for(CorsoRicerca i: corsoDAO.ricercaCorsi(SQL)){
            TreeItem<CorsoRicerca> treeItem=new TreeItem<>(i);
            List<String> tagscorso=new ArrayList<>();
            for(AreaTematica areaTematica:i.areetematiche){
                    CorsoRicerca corsoRicerca=new CorsoRicerca();
                    corsoRicerca.setTag(areaTematica.tag);
                    tagscorso.add(areaTematica.tag);
                    TreeItem<CorsoRicerca> tagItem=new TreeItem<>(corsoRicerca);
                    treeItem.getChildren().add(tagItem);
            }
            if(tagscorso.containsAll(tags)||tags.isEmpty()){
                fakeroot.getChildren().add(treeItem);
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
            setRicercaTableView(SQL,tags);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        primaryStage.getScene().getRoot().setDisable(false);
    }
}
