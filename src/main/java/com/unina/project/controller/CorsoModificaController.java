package com.unina.project.controller;

import com.unina.project.AreaTematica;
import com.unina.project.Corso;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CorsoModificaController extends  CorsoController{
    @Override
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
            corso.setIscrizioniMassime(Integer.parseInt(iscrizioniMassimeTextField.getText()));
            corso.setPrivato(tipocorsoChoiseBox.getSelectionModel().getSelectedItem().equals("Privato"));
            try {
                areaTematicaDAO.insertAreaTematica(areeTematiche);
                corso.setCodCorso(corsoDAO.insertCorso(corso,gestore.codGestore));
                areaTematicaDAO.associaAreaTematica(areeTematiche, corso.codCorso);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) nuovoCorsoButton.getScene().getWindow();
            stage.close();
        }
    }

    public void setCorso(Corso corso) {
        titoloTextField.setText(corso.titolo);
        descrizionecorsoTextArea.setText(corso.titolo);
        if(corso.Privato ==false){
            tipocorsoChoiseBox.getSelectionModel().selectLast();
        }
        else{
            tipocorsoChoiseBox.getSelectionModel().selectFirst();
        }
        iscrizioniMassimeTextField.setText(String.valueOf(corso.iscrizioniMassime));
        lezioniTextField.setText(String.valueOf(corso.numeroLezioni));
        lezioniMinimeTextField.setText(String.valueOf((corso.numeroLezioni/100)*corso.tassoPresenzeMinime));
        tassoMinimoTextField.setText(corso.getTassoPresenzeMinime());
        for(AreaTematica i: corso.areetematiche){
            tagBar.getTags().add(i.tag);
        }

    }
}
