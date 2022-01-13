package com.unina.project.database;

import com.unina.project.AreaTematica;
import com.unina.project.Corso;
import com.unina.project.controller.profile.CorsoRicerca;

import java.sql.SQLException;
import java.util.List;

public interface CorsoDAO {
    String insertCorso(Corso corso, String codGestore) throws SQLException;

    List<Corso> getCorsi(String codGestore) throws SQLException;

    void deleteCorso(Corso corso) throws SQLException;

    List<CorsoRicerca> ricercaCorsi(String SQL) throws SQLException;

    void updateCorso(Corso corso) throws SQLException;

    List<AreaTematica> getAreeTematiche(String codCorso) throws SQLException;
}
