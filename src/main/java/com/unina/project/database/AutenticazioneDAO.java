package com.unina.project.database;

import com.unina.project.Autenticazione;

import java.sql.SQLException;

public interface AutenticazioneDAO {
    void insertAutenticazione(Autenticazione autenticazione) throws SQLException;

    boolean checkEmailExist(String email) throws SQLException;

    boolean checkEmailUtenteExist(String email) throws SQLException;
    boolean checkEmailGestoriExist(String email) throws SQLException;
    boolean loginUtente(String email, String password) throws SQLException;
    boolean loginGestore(String email, String password) throws SQLException;
    void resetPassword(String email,String password) throws SQLException;
    void deleteAutenticazione(Autenticazione autenticazione) throws SQLException;
    void updateAutenticazione(String password, String email, String emaildacontrollare) throws SQLException;
}
