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
        //String password = "551351d5bee4c181ab94eecc48dd92d30ab45020fc7f95e0f0e43810245df4fb";
        //String user = "aevxiyldoeykvf";
        //String url = "jdbc:postgresql://ec2-34-242-89-204.eu-west-1.compute.amazonaws.com:5432/d7jks8d3jrtbbo";
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
