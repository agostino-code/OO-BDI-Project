package com.unina.project.controller;

import com.unina.project.Autenticazione;
import com.unina.project.Main;
import com.unina.project.Utente;
import com.unina.project.codicefiscale.engine.Engine;
import com.unina.project.codicefiscale.engine.Utils;
import com.unina.project.database.AutenticazioneDAO;
import com.unina.project.database.UtenteDAO;
import com.unina.project.database.postgre.PostgreAutenticazioneDAO;
import com.unina.project.database.postgre.PostgreUtenteDAO;
import com.unina.project.verificationcode.SendVerificationEmail;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class RegistrazioneController implements Initializable {
    @FXML
    public TextField emailTextField;
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
    public PasswordField passwordField;
    @FXML
    private ProgressBar passwordProgressBar;
    @FXML
    public PasswordField repeatpasswordField;

    private final List<String> comuni = new ArrayList<>();
    private final Utente utente=new Utente();
    public final Autenticazione autenticazione = new Autenticazione();
    private final AutenticazioneDAO autenticazioneDAO=new PostgreAutenticazioneDAO();
    private final UtenteDAO utenteDAO=new PostgreUtenteDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getComuni(comuni);
        setDataPicker();
            dataDatePicker.focusedProperty().addListener(generaCodiceListner);
            nomeTextField.focusedProperty().addListener(generaCodiceListner);
            cognomeTextField.focusedProperty().addListener(generaCodiceListner);
            comunedinascitaTextField.focusedProperty().addListener(generaCodiceListner);
            sessoChoiceBox.getItems().addAll("M","F");
            sessoChoiceBox.focusedProperty().addListener(generaCodiceListner);
        emailTextField.focusedProperty().addListener(checkEmailListner);
        passwordField.focusedProperty().addListener(passwordListner);
        repeatpasswordField.focusedProperty().addListener(repeatpasswordListner);
        TextFields.bindAutoCompletion(comunedinascitaTextField,comuni);
    }

    public void getComuni(List<String> comuni) {
        for (Map.Entry<String, String> e : Utils.getCitiesCodes().entrySet()) {
            comuni.add(e.getValue());
        }
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

    private final ChangeListener<Boolean> generaCodiceListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            try {
                if(!nomeTextField.getText().isBlank()&&!cognomeTextField.getText().isBlank()&&
                        !comunedinascitaTextField.getText().isBlank()&&
                        !sessoChoiceBox.getSelectionModel().isEmpty()&&
                        !dataDatePicker.getValue().toString().isBlank()) {
                    newCodiceFiscale();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public ChangeListener<Boolean> checkEmailListner = (observable, oldValue, newValue) -> {
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

    public void passwordCheck() {
        passwordProgressBar.setProgress(calcolaPasswordStrength(passwordField.getText(),nomeTextField.getText())/10);

    }

    public ChangeListener<Boolean> passwordListner = (observable, oldValue, newValue) -> {
        if (!newValue) {
            if(!passwordField.getText().isBlank())
            if ((calcolaPasswordStrength(passwordField.getText(),nomeTextField.getText())) < 8) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione!");
                alert.setHeaderText("La password che hai inserito non è sicura!");
                alert.setContentText("""
                        Non deve contenere il nome dell'utente/gestore.
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
        }
    };

    public ChangeListener<Boolean> repeatpasswordListner = (observable, oldValue, newValue) -> {
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
            if(!emailTextField.getText().isBlank()&&!nomeTextField.getText().isBlank()&&
                    !cognomeTextField.getText().isBlank()&& !sessoChoiceBox.getSelectionModel().isEmpty()&&
                    !dataDatePicker.getValue().toString().isBlank()&&!codicefiscaleTextField.getText().isEmpty()&&
                    !passwordField.getText().isBlank()&&!repeatpasswordField.getText().isBlank()) {
                SendVerificationEmail sendVerificationEmail=new SendVerificationEmail();
                String codicediverifica=sendVerificationEmail.SendEmail(autenticazione.email);
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Invio codice di Verifica");
                dialog.setHeaderText("Abbiamo inviato un codice di verifica all'email che ci hai fornito");
                dialog.setContentText("Inserisci qui il codice :");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    if(result.get().equals(codicediverifica)){
                        utente.setEmail(autenticazione.email);
                        try {
                            autenticazioneDAO.insertAutenticazione(autenticazione);
                            utenteDAO.insertUtente(utente);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        accountCreatedSuccessful(actionEvent);
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Attenzione!");
                        alert.setHeaderText("Il codice inserito non è corretto!");
                        alert.setContentText("Riprova!");
                        alert.showAndWait();
                    }
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Errore");
                alert.setHeaderText("Inserisci prima tutti i dati!");
                alert.showAndWait();
            }
        }

    public static void accountCreatedSuccessful(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attenzione!");
        alert.setHeaderText("Account Creato Correttamente");
        alert.setContentText("Benvenuto!");
        alert.showAndWait();
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(Main.getLoginScene());
    }

    public void onindietroButtonClick(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setTitle("Formazione Facile");
        primaryStage.setScene(Main.getLoginScene());
    }

}


