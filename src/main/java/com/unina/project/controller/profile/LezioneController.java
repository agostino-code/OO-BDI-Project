package com.unina.project.controller.profile;

import com.unina.project.Lezione;
import com.unina.project.database.CorsoDAO;
import com.unina.project.database.LezioneDAO;
import com.unina.project.database.postgre.PostgreCorsoDAO;
import com.unina.project.database.postgre.PostgreLezioneDAO;
import com.unina.project.graphics.LimitedTextField;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class LezioneController implements Initializable {

    @FXML
    public TextField oraTextField;

    @FXML
    public TextField codCorsoTextField;

    @FXML
    public DatePicker dataPicker=new DatePicker(LocalDate.now().plusDays(1));

    @FXML
    public TextArea descrizioneTextArea;

    @FXML
    public TextField durataTextField;

    @FXML
    public Button nuovaLezioneButton;

    @FXML
    public TextField titoloTextField;

    public Lezione lezione=new Lezione();
    public final LezioneDAO lezioneDAO=new PostgreLezioneDAO();
    private final CorsoDAO corsoDAO=new PostgreCorsoDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LimitedTextField limittitolo =new LimitedTextField(titoloTextField);
        limittitolo.setMaxLength(50);
        LimitedTextField limitdurata =new LimitedTextField(durataTextField);
        limitdurata.setMaxLength(5);
        limitdurata.setRestrict("[0-9:]");
        LimitedTextField limitora =new LimitedTextField(oraTextField);
        limitora.setMaxLength(5);
        limitora.setRestrict("[0-9:]");
        descrizioneTextArea.focusedProperty().addListener(descrizioneLister);
        oraTextField.focusedProperty().addListener(oraListner);
        durataTextField.focusedProperty().addListener(durataListner);
        dataPicker.setEditable(false);
        restrictDatePicker(dataPicker,LocalDate.now().plusDays(1),LocalDate.now().plusYears(1));
    }

    private final ChangeListener<Boolean> descrizioneLister = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if (descrizioneTextArea.getLength()>200){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Errore descrizione");
                alert.setHeaderText("La descrizione non può superare 200 caratteri!");
                alert.setContentText("Quando il testo diventa rosso hai superato i 200 caratteri!");
                alert.showAndWait();
            }
        }
    };

    @FXML
    public void checkDescrizioneLenght() {
        if (descrizioneTextArea.getLength()>200){
            descrizioneTextArea.setStyle("-fx-text-fill: red;");
        }
        else{
            descrizioneTextArea.setStyle("-fx-text-fill: black");
        }
    }

    public void setcodCorso(String codCorso){
        lezione.setCodCorso(codCorso);
        codCorsoTextField.setText(codCorso);
    }

    private void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(minDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        } else if (item.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }

    public final ChangeListener<Boolean> oraListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if(!oraTextField.getText().isBlank())
            try {
                LocalTime.parse(oraTextField.getText());
            } catch (DateTimeParseException | NullPointerException e) {
                oraTextField.clear();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("L'ora inserita è di un formato non valido!\n" +
                        "Il formato valido è ( hh:mm ).");
                alert.setContentText("Riprova!");
                alert.showAndWait();
            }
        }
    };

    public final ChangeListener<Boolean> durataListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if(!durataTextField.getText().isBlank())
            try {
                LocalTime.parse(durataTextField.getText());
            } catch (DateTimeParseException | NullPointerException e) {
                durataTextField.clear();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("La durata inserita è di un formato non valido!\n" +
                        "Il formato valido è ( hh:mm ) es. 01:30 per una durata di un ora e mezza.");
                alert.setContentText("Riprova!");
                alert.showAndWait();
            }
        }
    };

    @FXML
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
                if(lezioneDAO.countNumeroLezioni(lezione.getCodCorso()) < corsoDAO.getNumeroLezioni(lezione.getCodCorso())){
                    lezioneDAO.insertLezione(lezione);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setHeaderText("Hai raggiunto il numero massimo di Lezioni!");
                    alert.setContentText("Contatta il tuo Gestore.");
                    alert.showAndWait();
                }
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

}