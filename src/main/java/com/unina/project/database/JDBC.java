package com.unina.project.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface JDBC {
    Connection Connessione() throws SQLException;
}
