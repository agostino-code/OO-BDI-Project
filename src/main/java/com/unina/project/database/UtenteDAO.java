package com.unina.project.database;

import com.unina.project.Utente;
import java.sql.SQLException;

public interface UtenteDAO {
    void insertUtente(Utente utente) throws SQLException;
    Utente getUtente(String email) throws SQLException;
}
