package com.unina.project.database;

import com.unina.project.Corso;
import com.unina.project.Gestore;

import java.sql.SQLException;
import java.util.List;

public interface GestoreDAO {
    String insertGestore(Gestore gestore) throws SQLException;
    boolean checkNomeExist(String nome) throws SQLException;
    Gestore getGestore(String codGestore) throws  SQLException;

}
