package com.unina.project.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface AutenticazioneDAO {
    void insertAutenticazione(String email, String password);
    boolean checkEmailExist(String email) throws SQLException;
}
