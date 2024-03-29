package com.unina.project.controller;

import com.unina.project.graphics.LimitedTextField;
import com.unina.project.graphics.TagBar;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CorsoController extends ProfileGestoreController {


    @FXML
    public TextArea descrizionecorsoTextArea;

    @FXML
    public TextField iscrizioniMassimeTextField;

    @FXML
    public TextField lezioniTextField;

    @FXML
    public TextField lezioniMinimeTextField;

    @FXML
    public Button nuovoCorsoButton;

    @FXML
    public TextField tassoMinimoTextField;

    @FXML
    public TextField titoloTextField;
    @FXML
    public ChoiceBox<String> tipocorsoChoiseBox;
    @FXML
    public HBox tagshbox;

    public final TagBar tagBar = new TagBar();
    public List<String> areeTematiche=new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LimitedTextField limittitolo =new LimitedTextField(titoloTextField);
        limittitolo.setMaxLength(50);
        LimitedTextField limitiscrizioni =new LimitedTextField(iscrizioniMassimeTextField);
        limitiscrizioni.setMaxLength(3);
        limitiscrizioni.setIntegerField();
        LimitedTextField limitlezioni =new LimitedTextField(lezioniTextField);
        limitlezioni.setMaxLength(3);
        limitlezioni.setIntegerField();
        LimitedTextField limitlezioniminime =new LimitedTextField(lezioniMinimeTextField);
        limitlezioniminime.setMaxLength(3);
        limitlezioniminime.setIntegerField();
        tagBar.setMinWidth(230);
        tagshbox.getChildren().add(tagBar);
        tipocorsoChoiseBox.getItems().addAll("Privato","Pubblico");
        descrizionecorsoTextArea.focusedProperty().addListener(descrizioneLister);
        lezioniMinimeTextField.focusedProperty().addListener(lezioniMinimeListner);
        tassoMinimoTextField.setDisable(true);
    }

    private final ChangeListener<Boolean> descrizioneLister = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if (descrizionecorsoTextArea.getLength()>200){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Errore descrizione");
                alert.setHeaderText("La descrizione non può superare 200 caratteri!");
                alert.setContentText("Quando il testo diventa rosso hai superato i 200 caratteri!");
                alert.showAndWait();
            }
        }
    };

    private final ChangeListener<Boolean> lezioniMinimeListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if (!lezioniMinimeTextField.getText().isBlank()&&!lezioniTextField.getText().isBlank()) {
                if (Integer.parseInt(lezioniTextField.getText()) < Integer.parseInt(lezioniMinimeTextField.getText()) || lezioniTextField.getText().isBlank()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Errore lezioni minime");
                    alert.setHeaderText("Le lezioni minime da seguire non possono essere maggiori del numero di lezioni!");
                    alert.setContentText("Inserisci un numero di lezioni minime minore o uguale di lezioni!");
                    lezioniMinimeTextField.clear();
                    alert.showAndWait();
                }
                else{
                    corso.setTassoPresenzeMinime(Integer.parseInt(lezioniMinimeTextField.getText())*100/Integer.parseInt(lezioniTextField.getText()));
                    tassoMinimoTextField.setText(corso.tassoPresenzeMinime +"%");
                }
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
        gestore.codGestore=codGestore;
    }

    @FXML
    public void onnuovoCorsoButtonClick() {
        if (titoloTextField.getText().isBlank() || iscrizioniMassimeTextField.getText().isBlank()
                || lezioniTextField.getText().isBlank() || lezioniTextField.getText().isBlank()
                ||tassoMinimoTextField.getText().isBlank()||tipocorsoChoiseBox.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attenzione!");
            alert.setHeaderText("Inserisci prima tutti i dati!");
            alert.setContentText("La descrizione è opzionale.");
            alert.showAndWait();
        }
        else{
            areeTematiche=tagBar.getTags();
            corso.setTitolo(titoloTextField.getText());
            corso.setIscrizioniMassime(Integer.parseInt(iscrizioniMassimeTextField.getText()));
            corso.setNumeroLezioni(Integer.parseInt(lezioniTextField.getText()));
            corso.setTassoPresenzeMinime(Integer.parseInt(lezioniMinimeTextField.getText())*100/corso.getNumeroLezioni());
            corso.setDescrizione(descrizionecorsoTextArea.getText());
            corso.setPrivato(tipocorsoChoiseBox.getSelectionModel().getSelectedItem().equals("Privato"));
            try {
                areaTematicaDAO.insertAreaTematica(areeTematiche);
                corso.setCodCorso(corsoDAO.insertCorso(corso,gestore.codGestore));
                areaTematicaDAO.associaAreaTematica(areeTematiche, corso.getCodCorso());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) nuovoCorsoButton.getScene().getWindow();
            stage.close();
        }
    }

}


