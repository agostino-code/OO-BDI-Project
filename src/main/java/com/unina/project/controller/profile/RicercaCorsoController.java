package com.unina.project.controller.profile;

import com.unina.project.graphics.TagBar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class RicercaCorsoController implements Initializable {

        @FXML
        private Button cercaCorsoButton;

        @FXML
        private TextField codGestoreTextField;

        @FXML
        private TextField codcorsoTextField;

        @FXML
        private TextField cittaTextField;

        @FXML
        private TextField nomeTextField;

        @FXML
        private HBox tagshbox;

        @FXML
        private TextField provinciaTextField;

        @FXML
        private ChoiceBox<String> tipocorsoChoiseBox;

        @FXML
        private TextField titoloTextField;

        public TagBar tagBar = new TagBar();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tagBar.setMinWidth(230);
        tagshbox.getChildren().add(tagBar);
        tipocorsoChoiseBox.getItems().addAll("Privato","Pubblico");
    }

    String SQL = "Select * from \"parametriricerca\" WHERE ";
    @FXML
    void onCercaCorsoButtonClick(ActionEvent event) {
        if(!codcorsoTextField.getText().isEmpty())
        {
            String sqlcodicecorso =" \"codCorso\" = '"+ codcorsoTextField.getText()+"' AND ";
            SQL=SQL.concat(sqlcodicecorso);

        }
        if(!titoloTextField.getText().isEmpty())
        {
            String sqltitolo = " titolo ='"+titoloTextField.getText()+"' AND ";
            SQL=SQL.concat(sqltitolo);

        }
        if(!tipocorsoChoiseBox.getSelectionModel().isEmpty())
        {
            if(tipocorsoChoiseBox.getSelectionModel().getSelectedItem().equals("Privato")){
                String sqltipo = " Privato = true AND ";
                SQL=SQL.concat(sqltipo);
            }
            else{
                String sqltipo = " Privato = false AND ";
                SQL=SQL.concat(sqltipo);
            }

        }
        if(!codGestoreTextField.getText().isEmpty())
        {
            String sqlcodicegestore = " \"codGestore\" ='"+codGestoreTextField.getText()+"' AND ";
            SQL=SQL.concat(sqlcodicegestore);
        }
        if(!nomeTextField.getText().isEmpty())
        {
            String sqlnomegestore = " nome ='"+nomeTextField.getText()+"' AND ";
            SQL=SQL.concat(sqlnomegestore);
        }
        if(!cittaTextField.getText().isEmpty())
        {
            String sqlcittagestore = " città ='"+cittaTextField.getText().toUpperCase()+"' AND ";
            SQL=SQL.concat(sqlcittagestore);
        }
        if(!provinciaTextField.getText().isEmpty())
        {
            String sqlprovinciagestore = " provincia ='"+provinciaTextField.getText().toUpperCase()+"' AND ";
            SQL=SQL.concat(sqlprovinciagestore);
        }
        SQL=SQL.concat(" TRUE ");
        System.out.println(SQL);
        Stage stage = (Stage) cercaCorsoButton.getScene().getWindow();
        stage.close();
    }
    public String getParametriRicercaSQL(){
        return SQL;
    }
    public List<String> getTagsRicerca(){
        return tagBar.getTags();
    }


}
