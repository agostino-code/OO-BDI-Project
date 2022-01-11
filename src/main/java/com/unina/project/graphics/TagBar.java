package com.unina.project.graphics;

import com.unina.project.AreaTematica;
import com.unina.project.Main;
import com.unina.project.database.AreaTematicaDAO;
import com.unina.project.database.postgre.PostgreAreaTematicaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TagBar extends VBox {
    private AreaTematicaDAO areaTematicaDAO=new PostgreAreaTematicaDAO();
    private final ObservableList<String> tags;
    private final TextField inputTextField;
    public ObservableList<String> getTags() {
        return tags;
    }

    public TagBar() {
        getStyleClass().setAll("tag-bar");
        getStylesheets().add(Objects.requireNonNull(Main.class.getResource("style.css").toExternalForm()));
        tags = FXCollections.observableArrayList();
        inputTextField = new TextField();
        inputTextField.setOnAction(evt -> {
            String text = inputTextField.getText();
            if (!text.isEmpty() && !tags.contains(text)) {
                tags.add(text);
                inputTextField.clear();
            }
        });
        LimitedTextField limitag=new LimitedTextField(inputTextField);
        limitag.setMaxLength(30);
        limitag.setStandardField();
        List<AreaTematica>loadtags= new ArrayList<>();
        try {
            loadtags = areaTematicaDAO.getAreeTematiche();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(AreaTematica i:loadtags){
            TextFields.bindAutoCompletion(inputTextField,i.tag);
        }
        inputTextField.prefHeightProperty().bind(this.heightProperty());
        HBox.setHgrow(inputTextField, Priority.ALWAYS);
        inputTextField.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        tags.addListener((ListChangeListener.Change<? extends String> change) -> {
            while (change.next()) {
                if (change.wasPermutated()) {
                    ArrayList<Node> newSublist = new ArrayList<>(change.getTo() - change.getFrom());
                    for (int i = change.getFrom(), end = change.getTo(); i < end; i++) {
                        newSublist.add(null);
                    }
                    for (int i = change.getFrom(), end = change.getTo(); i < end; i++) {
                        newSublist.set(change.getPermutation(i), getChildren().get(i));
                    }
                    getChildren().subList(change.getFrom(), change.getTo()).clear();
                    getChildren().addAll(change.getFrom(), newSublist);
                } else {
                    if (change.wasRemoved()) {
                        getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                    }
                    if (change.wasAdded()) {
                        getChildren().addAll(change.getFrom(), change.getAddedSubList().stream().map(Tag::new).collect(Collectors.toList()));
                    }
                }
            }
        });
        getChildren().add(inputTextField);
    }

    private class Tag extends BorderPane {

        public Tag(String tag) {
            getStyleClass().setAll("tag");
            Button removeButton = new Button();
            removeButton.setOnAction((evt) -> tags.remove(tag));
            Text text = new Text(tag);
            setCenter(text);
            setRight(removeButton);

        }
    }

}
