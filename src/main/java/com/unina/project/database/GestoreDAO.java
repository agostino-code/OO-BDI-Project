package com.unina.project.database;

import com.unina.project.Gestore;

import java.sql.SQLException;

public interface GestoreDAO {
    void insertGestore(Gestore gestore,String codSede) throws SQLException;
    boolean checkNomeExist(String nome) throws SQLException;

}
