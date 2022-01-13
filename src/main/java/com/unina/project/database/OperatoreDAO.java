package com.unina.project.database;

import com.unina.project.Operatore;

import java.sql.SQLException;
import java.util.List;

public interface OperatoreDAO {
    String setOperatore(String codiceFiscale) throws SQLException;

    void associaOperatore(String codOperatore, String codCorso) throws SQLException;

    List<Operatore> getOperatori(String codGestore) throws SQLException;

    boolean checkOperatoreExist(String codiceFiscale) throws SQLException;
}
