package com.unina.project.controller;

import com.unina.project.Autenticazione;
import com.unina.project.Utente;
import com.unina.project.codicefiscale.engine.Engine;
import com.unina.project.codicefiscale.engine.Utils;
import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.UtenteDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import com.unina.project.database.postgre.PostgreUtenteDAO;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class RegistrazioneController implements Initializable {
    @FXML
    private Button registratiButton;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField nomeTextField;
    @FXML
    private TextField cognomeTextField;
    @FXML
    private DatePicker dataDatePicker;
    @FXML
    private javafx.scene.control.TextField comunedinascitaTextField;
    @FXML
    private ChoiceBox<String> sessoChoiceBox;
    @FXML
    private TextField codicefiscaleTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ProgressBar passwordProgressBar;
    @FXML
    private PasswordField repeatpasswordField;

    private boolean registratiButtonDisable=true;
    private List<String> comuni = new ArrayList<>();
    private Utente utente=new Utente();
    private Autenticazione autenticazione = new Autenticazione();
    private AutenticazioneDAO autenticazioneDAO=new PostgreAutenticazioneDAO();
    private UtenteDAO utenteDAO=new PostgreUtenteDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getComuni();
        setDataPicker();
        registratiButton.setDisable(true);
        emailTextField.focusedProperty().addListener(checkEmailListner);
        sessoChoiceBox.getItems().addAll("M","F");
        nomeTextField.focusedProperty().addListener(generaCodiceListner);
        cognomeTextField.focusedProperty().addListener(generaCodiceListner);
        dataDatePicker.focusedProperty().addListener(generaCodiceListner);
        comunedinascitaTextField.focusedProperty().addListener(generaCodiceListner);
        sessoChoiceBox.focusedProperty().addListener(generaCodiceListner);
        passwordField.focusedProperty().addListener(passwordListner);
        repeatpasswordField.focusedProperty().addListener(repeatpasswordListner);

    }

    private Scene loginScene;

    public void setLoginScene(Scene scene) {
        loginScene = scene;
    }

    private void getComuni() {
        for (Map.Entry<String, String> e : Utils.getCitiesCodes().entrySet()) {
            comuni.add(e.getValue());
        }
    }

    @FXML
    private void comuniSuggeriti(KeyEvent keyEvent){
        TextFields.bindAutoCompletion(comunedinascitaTextField,comuni);
    }

    private void newCodiceFiscale() throws IOException {
        utente.setNome(nomeTextField.getText());
        utente.setCognome(cognomeTextField.getText());
        utente.setDataNascita(dataDatePicker.getValue());
        utente.setComuneDiNascita(comunedinascitaTextField.getText());
        utente.setSesso(sessoChoiceBox.getSelectionModel().getSelectedItem());
        Engine engine = new Engine(utente);
        codicefiscaleTextField.setText(engine.getCode());
        utente.setCodiceFiscale(codicefiscaleTextField.getText());
        }

    private ChangeListener<Boolean> generaCodiceListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            try {
                if(!nomeTextField.getText().isEmpty()&&!cognomeTextField.getText().isEmpty()&&
                        !comunedinascitaTextField.getText().isEmpty()&&
                        !sessoChoiceBox.getSelectionModel().isEmpty()&&
                        !dataDatePicker.getValue().toString().isEmpty()) {
                    registratiButtonDisable=false;
                    newCodiceFiscale();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private ChangeListener<Boolean> checkEmailListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            try {
                    if (!autenticazioneDAO.checkEmailExist(emailTextField.getText())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Errore email");
                        alert.setHeaderText("L'email che hai inserito risulta già registrata!");
                        alert.setContentText("Cambia il valore di Email");
                        emailTextField.clear();
                        alert.showAndWait();
                    } else {
                        autenticazione.setEmail(emailTextField.getText());
                        utente.setEmail(emailTextField.getText());
                    }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    private void setDataPicker(){
        LocalDate dataLocale = LocalDate.now();
        LocalDate maxDate =dataLocale.minusYears(16);
        LocalDate minDate =dataLocale.minusYears(80);
        restrictDatePicker(dataDatePicker,minDate,maxDate);
        dataDatePicker.setPromptText("dd/mm/yyyy");
        dataDatePicker.getEditor().focusedProperty().addListener((obj, wasFocused, isFocused)->{
            if (!isFocused) {
                try {
                    dataDatePicker.setValue(dataDatePicker.getConverter().fromString(dataDatePicker.getEditor().getText()));
                } catch (DateTimeParseException e) {
                    dataDatePicker.getEditor().setText(dataDatePicker.getConverter().toString(dataDatePicker.getValue()));
                }
            }
        });
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

    public void passwordCheck(KeyEvent keyEvent) {
        passwordProgressBar.setProgress(calcolaPasswordStrength(passwordField.getText(),nomeTextField.getText())/10);

    }

    private ChangeListener<Boolean> passwordListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if(!passwordField.getText().isEmpty())
            if ((calcolaPasswordStrength(passwordField.getText(),nomeTextField.getText())) < 8) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione!");
                alert.setHeaderText("La password che hai inserito non è sicura!");
                alert.setContentText("""
                        Non deve contenere il nome dell'utente.
                        Deve essere composta da almeno otto caratteri.
                        Deve contenere caratteri di almeno tre delle quattro categorie seguenti:
                           1) Lettere maiuscole dell'alfabeto latino (dalla A alla Z).
                           2) Lettere minuscole dell'alfabeto latino (dalla a alla z).
                           3) Numeri in base 10 (da 0 a 9).
                           4) Caratteri non alfanumerici, simboli.""");
                alert.showAndWait();
                passwordField.clear();
                passwordProgressBar.setProgress(0);
            }
            else{
                registratiButton.setDisable(registratiButtonDisable);
            }
        }
    };

    private ChangeListener<Boolean> repeatpasswordListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if(!passwordField.getText().isEmpty()){
                if(!repeatpasswordField.getText().isEmpty()){
                    if(!passwordField.getText().equals(repeatpasswordField.getText())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Attenzione!");
                        alert.setHeaderText("Le password non corrispondono!");
                        alert.setContentText("Riprova!");
                        alert.showAndWait();
                        repeatpasswordField.clear();
                    }
                    else
                    {
                        registratiButton.setDisable(registratiButtonDisable);
                        autenticazione.setPassword(repeatpasswordField.getText());
                    }
                }
            }
        }
    };

    private double calcolaPasswordStrength(String password,String nome) {

        double passwordScore = 1;
        if (password.length() < 1)
            return 0;
        if (password.length() < 8)
            return passwordScore;
        else if (password.length() >= 10)
            passwordScore += 2;
        else
            passwordScore += 1;

        if (password.matches("(?=.*[0-9]).*"))
            passwordScore += 2;

        if (password.matches("(?=.*[a-z]).*"))
            passwordScore += 2;

        if (password.matches("(?=.*[A-Z]).*"))
            passwordScore += 2;

        if (password.matches("(?=.*[~!@#$%^&*()_-]).*"))
            passwordScore += 2;

        if (!nome.isEmpty() && password.contains(nome)) {
            return 1;
        }
        return passwordScore;
    }
    public void onregistratiButtonClick(ActionEvent actionEvent) {
        try {
            autenticazioneDAO.insertAutenticazione(autenticazione);
            utenteDAO.insertUtente(utente);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attenzione!");
        alert.setHeaderText("Account Creato Correttamente");
        alert.setContentText("Benvenuto!");
        alert.showAndWait();
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(loginScene);

    }

    public void onindietroButtonClick(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(loginScene);
    }
}


