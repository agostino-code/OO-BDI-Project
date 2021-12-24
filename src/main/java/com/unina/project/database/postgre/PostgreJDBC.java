package com.unina.project.database.postgre;

import com.unina.project.database.JDBC;
import java.sql.*;

public class PostgreJDBC implements JDBC {


    private final String url = "jdbc:postgresql://localhost:5432/Project";
    private final String user = "postgres";
    private final String password = "2404";

    @Override
    public Connection Connessione() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
