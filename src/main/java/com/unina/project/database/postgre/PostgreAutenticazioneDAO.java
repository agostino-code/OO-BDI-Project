package com.unina.project.database.postgre;

import com.unina.project.database.AutenticazioneDAO;

import java.sql.*;

public class PostgreAutenticazioneDAO implements AutenticazioneDAO {

    @Override
    public void insertAutenticazione(String email, String password) {

    }

    @Override
    public boolean controllaemailneldatabase(String email) {
        return false;
    }
}
