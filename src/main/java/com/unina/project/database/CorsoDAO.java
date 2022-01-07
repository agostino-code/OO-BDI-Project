package com.unina.project.database;

import com.unina.project.Corso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface CorsoDAO {
    String insertCorso(Corso corso, String codGestore) throws SQLException;
     boolean checkTitoloExist(String titolo) throws SQLException;
}
