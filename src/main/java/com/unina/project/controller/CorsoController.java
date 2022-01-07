package com.unina.project.controller;

import com.unina.project.Corso;
import com.unina.project.database.CorsoDAO;
import com.unina.project.database.postgre.PostgreCorsoDAO;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CorsoController extends ProfileGestoreController {

    @FXML
    private TextArea descrizionecorsoTextArea;

    @FXML
    private TextField iscrizioniMassimeTextField;

    @FXML
    private TextField lezioniTextField;

    @FXML
    private TextField lezioniMinimeTextField;

    @FXML
    private Button nuovoCorsoButton;

    @FXML
    private TextField tassoMinimoTextField;

    @FXML
    private TextField titoloTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        descrizionecorsoTextArea.focusedProperty().addListener(descrizioneLister);
        lezioniTextField.focusedProperty().addListener(onlynumberListner);
        iscrizioniMassimeTextField.focusedProperty().addListener(onlynumberiscrittiListner);
        lezioniMinimeTextField.focusedProperty().addListener(lezioniMinimeListner);
        titoloTextField.focusedProperty().addListener(titoloListner);
        tassoMinimoTextField.setDisable(true);
    }

    private final ChangeListener<Boolean> titoloListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            try {
                if (!corsoDAO.checkTitoloExist(titoloTextField.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Errore titolo");
                    alert.setHeaderText("Esiste già un corso con questo itolo!");
                    alert.setContentText("Cambia il valore di titolo");
                    titoloTextField.clear();
                    alert.showAndWait();
                } else {
                    corso.setTitolo(titoloTextField.getText());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    private final ChangeListener<Boolean> descrizioneLister = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if (descrizionecorsoTextArea.getLength()>200){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Errore descrizione");
                alert.setHeaderText("La descrizione non può superare 200 caratteri!");
                alert.setContentText("Quando il testo diventa rosso hai superato i 200 caratteri!");
                alert.showAndWait();
            } else {
                if(!descrizionecorsoTextArea.getText().isBlank()){
                    corso.setDescrizione(descrizionecorsoTextArea.getText());
                }
            }
        }
    };

    private final ChangeListener<Boolean> lezioniMinimeListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if (!lezioniMinimeTextField.getText().isBlank()) {
                Integer lezioniminime = Integer.parseInt(lezioniMinimeTextField.getText());
                if (corso.numeroLezioni < lezioniminime || lezioniTextField.getText().isBlank()
                        ||!lezioniMinimeTextField.getText().matches("[0-9]+")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Errore lezioni minime");
                    alert.setHeaderText("Le lezioni minime da seguire non possono essere maggiori del numero di lezioni!\n" +
                            "Il numero di lezioni minime è un parametro numerico!");
                    alert.setContentText("Inserisci un numero di lezioni minime minore o uguale di lezioni!");
                    lezioniMinimeTextField.clear();
                    alert.showAndWait();
                }
                else {
                    corso.setTassoPresenzeMinime(lezioniminime*100/corso.numeroLezioni);
                    tassoMinimoTextField.setText(corso.tassoPresenzeMinime +"%");
                }
            }
        }
    };

    private final ChangeListener<Boolean> onlynumberListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if (!lezioniTextField.getText().isBlank())
                if (!lezioniTextField.getText().matches("[0-9]+")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Errore lezioni");
                    alert.setHeaderText("Il numero di lezioni è un parametro numerico!");
                    alert.setContentText("Cambia il valore di lezioni");
                    lezioniTextField.clear();
                    alert.showAndWait();
                } else {
                    Integer lezioni = Integer.parseInt(lezioniTextField.getText());
                    corso.setNumeroLezioni(lezioni);
                }
        }
    };

    private final ChangeListener<Boolean> onlynumberiscrittiListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if (!iscrizioniMassimeTextField.getText().isBlank())
                if (!iscrizioniMassimeTextField.getText().matches("[0-9]+")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Errore iscrizioni massime");
                    alert.setHeaderText("Il numero di iscrizioni massime è un parametro numerico!");
                    alert.setContentText("Cambia il valore di iscrizioni massime");
                    iscrizioniMassimeTextField.clear();
                    alert.showAndWait();
                } else {
                    Integer iscrizioniMassime = Integer.parseInt(iscrizioniMassimeTextField.getText());
                    corso.setIscrizioniMassime(iscrizioniMassime);
                }
        }
    };


    public void checkDescrizioneLenght() {
        if (descrizionecorsoTextArea.getLength()>200){
            descrizionecorsoTextArea.setStyle("-fx-text-fill: red;");
        }
        else{
            descrizionecorsoTextArea.setStyle("-fx-text-fill: black");
        }
    }

    public void setcodGestore(String codGestore){
        this.codGestore=codGestore;
    }

    @FXML
    public void onnuovoCorsoButtonClick(ActionEvent event) {
        if (titoloTextField.getText().isBlank() || iscrizioniMassimeTextField.getText().isBlank()
                || lezioniTextField.getText().isBlank() || lezioniTextField.getText().isBlank()
                ||tassoMinimoTextField.getText().isBlank()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attenzione!");
            alert.setHeaderText("Inserisci prima tutti i dati!");
            alert.setContentText("La descrizione è opzionale.");
            alert.showAndWait();
        }
        else{
            try {
                corsoDAO.insertCorso(corso,codGestore);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) nuovoCorsoButton.getScene().getWindow();
            stage.close();
        }
    }

}


