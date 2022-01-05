package com.unina.project.database;

import com.unina.project.Corso;

import java.sql.SQLException;

public interface CorsoDAO {
    String insertCorso(Corso corso, String codGestore) throws SQLException;
}
