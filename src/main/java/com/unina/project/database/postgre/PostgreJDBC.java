package com.unina.project.database.postgre;

import com.unina.project.database.JDBC;
import javafx.scene.control.Alert;

import java.sql.*;

public class PostgreJDBC implements JDBC {


    @Override
    public Connection Connessione(){
        String password = "2404";
        String user = "postgres";
        String url = "jdbc:postgresql://localhost:5432/Project";
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("La connessione con il Database non è riuscita.\n" +
                    "Contatta un amministratore oppure consulta la Documentazione!");
            alert.showAndWait();
            return null;
        }
    }
}
