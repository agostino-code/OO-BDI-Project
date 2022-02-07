package com.unina.project.controller.profile;

import com.unina.project.Lezione;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LezioneModificaController extends LezioneController{

    @Override
    public void onnuovaLezioneButtonClick(ActionEvent event) {
        if(!titoloTextField.getText().isBlank()&&dataPicker.getValue()!=null&&
                !durataTextField.getText().isBlank()&&!codCorsoTextField.getText().isBlank()&&!oraTextField.getText().isBlank()){
            lezione.setTitolo(titoloTextField.getText());
            lezione.setDescrizione(descrizioneTextArea.getText());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String dataTime=dataPicker.getValue().toString()+" "+oraTextField.getText();
            System.out.println(dataTime);
            LocalDateTime localDateTime = LocalDateTime.parse(dataTime,formatter);
            lezione.setDataoraInizio(localDateTime);
            lezione.setDurata(LocalTime.parse(durataTextField.getText()));
            try {
                lezioneDAO.updateLezione(lezione);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) nuovaLezioneButton.getScene().getWindow();
            stage.close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Errore");
            alert.setHeaderText("Inserisci prima tutti i dati!");
            alert.setContentText("La descrizione è opzionale.");
            alert.showAndWait();
        }
    }

    public void setLezione(Lezione lezione) {
        this.lezione=lezione;
        titoloTextField.setText(lezione.getTitolo());
        descrizioneTextArea.setText(lezione.getDescrizione());
        dataPicker.setValue(lezione.getDataInizio());
        oraTextField.setText(String.valueOf(lezione.getOraInizio()));
        durataTextField.setText(String.valueOf(lezione.getDurata()));
        try {
            codCorsoTextField.setText(lezioneDAO.getCorsoLezione(lezione.getCodLezione()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        nuovaLezioneButton.setText("Modifica Corso");
    }
}
