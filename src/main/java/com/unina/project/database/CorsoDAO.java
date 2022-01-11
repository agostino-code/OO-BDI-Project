package com.unina.project.database;

import com.unina.project.Corso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface CorsoDAO {
    String insertCorso(Corso corso, String codGestore) throws SQLException;
     boolean checkTitoloExist(String titolo) throws SQLException;
    List<Corso> getCorsi(String codGestore) throws SQLException;

    void deleteCorso(Corso corso) throws SQLException;

    List<Corso> ricercaCorsi(String codGestore, String SQL) throws SQLException;
}
