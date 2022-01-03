package com.unina.project.database.postgre;

import com.unina.project.database.JDBC;
import java.sql.*;

public class PostgreJDBC implements JDBC {


    @Override
    public Connection Connessione() throws SQLException {
        String password = "2404";
        String user = "postgres";
        String url = "jdbc:postgresql://localhost:5432/Project";
        return DriverManager.getConnection(url, user, password);
    }
}
