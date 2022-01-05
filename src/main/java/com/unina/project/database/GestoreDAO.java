package com.unina.project.database;

import com.unina.project.Gestore;

import java.sql.SQLException;

public interface GestoreDAO {
    String insertGestore(Gestore gestore) throws SQLException;
    boolean checkNomeExist(String nome) throws SQLException;
    String returncodGestore(String email) throws SQLException;

}
