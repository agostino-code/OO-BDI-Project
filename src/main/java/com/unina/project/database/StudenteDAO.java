package com.unina.project.database;

import com.unina.project.Studente;

import java.sql.SQLException;
import java.util.List;

public interface StudenteDAO {
    String setStudente(String codiceFiscale) throws SQLException;

    boolean checkStudenteExist(String codiceFiscale) throws SQLException;

    void iscriviStudente(String codStudente, String codCorso) throws SQLException;

    List<Studente> getStudentiDaAccettare(String codCorso) throws SQLException;

    List<Studente> getStudentiAccettati(String codCorso) throws SQLException;

    String getCodStudente(String codiceFiscale) throws SQLException;

    void richiestaAccettata(String CodStudente, String CodCorso) throws SQLException;
    void richiestaRifiutata(String CodStudente, String CodCorso) throws SQLException;

    boolean checkStudenteIscritto(String codCorso,String codStudente) throws SQLException;

    Boolean getStudenteIdoneo(String codCorso) throws SQLException;
}
