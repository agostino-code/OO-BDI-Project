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
import java.util.Optional;
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
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem4 = new MenuItem("Modifica Lezione");
        MenuItem menuItem5 = new MenuItem("Cancella Lezione");
        contextMenu.getItems().addAll(menuItem4,menuItem5);
        lezioniTableView.setRowFactory( tv -> {
            TreeTableRow<Lezione> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    rowDataLezione = row.getItem();
                    try {
                        if(rowDataLezione.getCodCorso()!=null){
                            row.setContextMenu(contextMenuNuovaLezione);
                        }
                        else{
                            row.setContextMenu(contextMenu);
                            updateStudentiTableView(rowDataLezione.getCodLezione());
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
                lezioneDAO.confermapresenza(rowDataStudente.getCodStudente(),rowDataLezione.getCodLezione());
                updateStudentiTableView(rowDataLezione.getCodLezione());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuItem2.setOnAction((event) -> {
            try {
                nuovalezione(rowDataLezione.getCodCorso());
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        menuItem3.setOnAction((event) -> {
            try {
                lezioneDAO.nonpresente(rowDataStudente.getCodStudente(),rowDataLezione.getCodLezione());
                updateStudentiTableView(rowDataLezione.getCodLezione());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuItem4.setOnAction((event) -> {
            try {
                modificaLezione(rowDataLezione);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        menuItem5.setOnAction((event) -> {
            eliminaLezione(rowDataLezione);

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

    public void setStudentiTableView(){
        nomeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNome()));
        cognomeTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCognome()));
        emailTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmail()));
        codStudenteTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCodStudente()));
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

    public void modificaLezione(Lezione lezione) throws IOException {
        Stage lezioneStage=new Stage();
        FXMLLoader lezionePageLoader = new FXMLLoader(Main.class.getResource("profile/lezione.fxml"));
        lezionePageLoader.setControllerFactory((Class<?> controllerType) ->{
            if(controllerType == LezioneController.class){
                return new LezioneModificaController();
            }
            return new LezioneModificaController();
        });
        Parent lezionePane = lezionePageLoader.load();
        Scene lezioneScene = new Scene(lezionePane, 400, 400);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(lezioneScene);
        LezioneModificaController lezioneModificaController=lezionePageLoader.getController();
        lezioneModificaController.setLezione(lezione);
        lezioneStage.setResizable(false);
        lezioneStage.setTitle("Modifica Lezione");
        lezioneStage.setScene(lezioneScene);
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

    public void eliminaLezione(Lezione lezione){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Conferma");
        dialog.setHeaderText("Per eliminare la lezione");
        dialog.setContentText("Inserisci 'si' :");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals("si")) {
                try {
                    lezioneDAO.deleteLezione(lezione.getCodLezione());
                    updateLezioniTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione!");
                alert.setHeaderText("Devi inserire 'si' per eliminare la lezione!");
                alert.setContentText("Riprova!");
                alert.showAndWait();
            }
        }

    }
    public void updateLezioniTableView() throws SQLException {
        TreeItem<Lezione> fakeroot=new TreeItem<>();
        lezioniTableView.setRoot(fakeroot);
        fakeroot.getChildren().clear();
        operatore.setCorsi(corsoDAO.getCorsiOperatoreAccettati(operatore.getCodOperatore()));
        for(Corso i: operatore.getCorsi()){
            Lezione onlycodcorso=new Lezione();
            onlycodcorso.setCodCorso(i.getCodCorso());
            TreeItem<Lezione> treeItem=new TreeItem<>(onlycodcorso);
            for(Lezione lezione:lezioneDAO.getLezioni(i.getCodCorso())) {
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
            operatore.setCodOperatore(operatoreDAO.getCodOperatore(utente.getCodiceFiscale()));
            updateLezioniTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
