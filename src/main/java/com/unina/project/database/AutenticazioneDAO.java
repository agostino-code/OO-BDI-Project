package com.unina.project.database;

import java.sql.Connection;

public interface AutenticazioneDAO {
    void insertAutenticazione(String email, String password);
    boolean controllaemailneldatabase(String email);
}
