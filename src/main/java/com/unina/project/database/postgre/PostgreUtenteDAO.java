package com.unina.project.database.postgre;

import com.unina.project.Autenticazione;
import com.unina.project.Utente;
import com.unina.project.database.UtenteDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgreUtenteDAO implements UtenteDAO {
    private PostgreJDBC postgreJDBC=new PostgreJDBC();
    @Override
    public void insertUtente(Utente utente) throws SQLException {
        String SQL = ("INSERT INTO \"Utente\" (email,nome,cognome,\"dataNascita\",\"comunediNascita\",sesso,\"codiceFiscale\") VALUES (?,?,?,?,?,?,?);");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, utente.email);
        pstmt.setString(2, utente.nome);
        pstmt.setString(3, utente.cognome);
        pstmt.setDate(4, Date.valueOf(utente.dataNascita));
        pstmt.setString(5, utente.comuneDiNascita);
        pstmt.setString(6, utente.sesso);
        pstmt.setString(7, utente.codiceFiscale);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }
}

