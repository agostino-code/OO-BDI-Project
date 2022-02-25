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

    Integer numeroIscrittiCorso(String CodCorso) throws SQLException;

    Integer numeroLezioniCorso(String CodCorso) throws SQLException;

    List<Corso> getCorsiOperatore(String codOperatore) throws SQLException;

    List<Corso> getCorsiOperatoreAccettati(String codOperatore) throws SQLException;

    List<Corso> getCorsiStudente(String codStudente) throws SQLException;

    void disiscrivitiCorso(String codStudente, String codCorso) throws SQLException;

    Integer getNumeroLezioni(String codCorso) throws SQLException;

    Integer numeroPrenotati(String CodCorso) throws SQLException;
}
