package com.unina.project.database;

import com.unina.project.Lezione;
import com.unina.project.Studente;

import java.sql.SQLException;
import java.util.List;

public interface LezioneDAO {

    List<Lezione> getLezioni(String codCorso) throws SQLException;

    List<Studente> getStudentiPrenotati(String codLezione) throws SQLException;
    void insertLezione(Lezione lezione) throws SQLException;
    void deleteLezione(String codLezione) throws SQLException;
    void prenotaLezione(String codLezione, String codStudente) throws SQLException;
    void deletePrenotazioneLezione(String codLezione,String codStudente) throws SQLException;
}
