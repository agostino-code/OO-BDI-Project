package com.unina.project.controller;

import com.unina.project.AreaTematica;
import com.unina.project.Corso;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CorsoModificaController extends CorsoController{
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
            corso.setTitolo(titoloTextField.getText());
            corso.setIscrizioniMassime(Integer.parseInt(iscrizioniMassimeTextField.getText()));
            corso.setNumeroLezioni(Integer.parseInt(lezioniTextField.getText()));
            corso.setTassoPresenzeMinime(Integer.parseInt(lezioniMinimeTextField.getText())*100/corso.getNumeroLezioni());
            corso.setDescrizione(descrizionecorsoTextArea.getText());
            corso.setPrivato(tipocorsoChoiseBox.getSelectionModel().getSelectedItem().equals("Privato"));
            try {
                areaTematicaDAO.insertAreaTematica(areeTematiche);
                corsoDAO.updateCorso(corso);
                areaTematicaDAO.deleteAreaTematica(corso);
                areaTematicaDAO.associaAreaTematica(areeTematiche, corso.getCodCorso());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) nuovoCorsoButton.getScene().getWindow();
            stage.close();
        }
    }

    public void setCorso(Corso corso) {
        titoloTextField.setText(corso.getTitolo());
        descrizionecorsoTextArea.setText(corso.getDescrizione());
        this.corso.setCodCorso(corso.getCodCorso());
        if(!corso.Privato){
            tipocorsoChoiseBox.getSelectionModel().selectLast();
        }
        else{
            tipocorsoChoiseBox.getSelectionModel().selectFirst();
        }
        iscrizioniMassimeTextField.setText(String.valueOf(corso.getIscrizioniMassime()));
        lezioniTextField.setText(String.valueOf(corso.getNumeroLezioni()));
        lezioniMinimeTextField.setText(String.valueOf((corso.getNumeroLezioni()*corso.tassoPresenzeMinime)/100));
        tassoMinimoTextField.setText(corso.getTassoPresenzeMinime());
        for(AreaTematica i: corso.getAreetematiche()){
            tagBar.getTags().add(i.getTag());
        }
        nuovoCorsoButton.setText("Modifica Corso");
    }
}
