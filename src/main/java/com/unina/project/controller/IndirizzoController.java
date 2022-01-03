package com.unina.project.controller;

import com.unina.project.Sede;
import com.unina.project.codicefiscale.engine.Utils;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class IndirizzoController extends RegistrazioneGestoreController {
    @FXML
    public Button inserisciButton;
    @FXML
    private TextField viaTextField;
    @FXML
    private TextField civicoTextField;
    @FXML
    private javafx.scene.control.TextField cittaTextField;
    @FXML
    private javafx.scene.control.TextField provinciaTextField;
    @FXML
    private TextField siglaTextField;

    private final List<String> comuni =new ArrayList<>();
    private final List<String> province =new ArrayList<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getComuni(comuni);
        getProvince(province);
        provinciaTextField.focusedProperty().addListener(provinciaListner);
        TextFields.bindAutoCompletion(provinciaTextField,province);
        TextFields.bindAutoCompletion(cittaTextField,comuni);
        siglaTextField.setDisable(true);
    }

    public void getProvince(List<String> province) {
        for (Map.Entry<String, String> e : Utils.getCitiesProvinces().entrySet()) {
            province.add(e.getValue());
        }
    }

    public String getProvinceSigle(String provincia){
        return Utils.getCitiesProvinces().getKey(provincia);
    }

    public ChangeListener<Boolean> provinciaListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            siglaTextField.setText(getProvinceSigle(provinciaTextField.getText().toUpperCase()));
        }
    };

    @FXML
    public void oninserisciButtonClick(ActionEvent actionEvent) {
        if (viaTextField.getText().isBlank() || civicoTextField.getText().isBlank() ||
        cittaTextField.getText().isBlank() || provinciaTextField.getText().isBlank() ||
        siglaTextField.getText().equals("#EE#")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attenzione!");
            alert.setHeaderText("L'indirizzo che hai inserito non è valido!");
            alert.setContentText("Riprova!");
            alert.showAndWait();
        }
        else{
            sede.setVia(viaTextField.getText());
            sede.setCivico(civicoTextField.getText());
            sede.setCitta(cittaTextField.getText());
            sede.setProvincia(provinciaTextField.getText());

            Stage stage = (Stage) inserisciButton.getScene().getWindow();
            stage.close();
        }
    }
    public Sede getSede(){
        return sede;
    }
}
