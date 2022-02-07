package com.unina.project.database.postgre;

import com.unina.project.Utente;
import com.unina.project.database.JDBC;
import com.unina.project.database.UtenteDAO;

import java.sql.*;

public class PostgreUtenteDAO implements UtenteDAO {
    private final JDBC postgreJDBC=new PostgreJDBC();
    @Override
    public void insertUtente(Utente utente) throws SQLException {
        String SQL = ("INSERT INTO \"Utente\" (email,nome,cognome,\"dataNascita\",\"comunediNascita\",sesso,\"codiceFiscale\") VALUES (?,?,?,?,?,?,?);");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, utente.getEmail());
        pstmt.setString(2, utente.getNome());
        pstmt.setString(3, utente.getCognome());
        pstmt.setDate(4, Date.valueOf(utente.getDataNascita()));
        pstmt.setString(5, utente.getComuneDiNascita());
        pstmt.setString(6, utente.getSesso());
        pstmt.setString(7, utente.getCodiceFiscale());
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }

    @Override
    public Utente getUtente(String email) throws SQLException {
        String SQL = ("SELECT nome, cognome, \"dataNascita\", \"comunediNascita\", sesso, \"codiceFiscale\" FROM \"Utente\" WHERE email = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, email);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        rs.next();
        Utente utente=new Utente();
        utente.setNome(rs.getString("nome"));
        utente.setCognome(rs.getString("cognome"));
        utente.setDataNascita(rs.getDate("dataNascita").toLocalDate());
        utente.setComuneDiNascita(rs.getString("comunediNascita"));
        utente.setSesso(rs.getString("sesso"));
        utente.setCodiceFiscale(rs.getString("codiceFiscale"));
        pstmt.close();
        conn.close();
        return utente;
    }
}

