package com.unina.project.database.postgre;

import com.unina.project.Autenticazione;
import com.unina.project.database.AutenticazioneDAO;

import java.sql.*;

public class PostgreAutenticazioneDAO implements AutenticazioneDAO {
    private final PostgreJDBC postgreJDBC=new PostgreJDBC();

    @Override
    public void insertAutenticazione(Autenticazione autenticazione) throws SQLException {
        String SQL = ("INSERT INTO \"Autenticazione\" (email, password) VALUES (?,?);");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, autenticazione.getEmail());
        pstmt.setString(2, autenticazione.getPassword());
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }

    @Override
    public boolean checkEmailExist(String email) throws SQLException {
        String SQL = ("SELECT email FROM \"Autenticazione\" WHERE email = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }


    @Override
    public boolean checkEmailUtenteExist(String email) throws SQLException {
        String SQL = ("SELECT email FROM loginutente WHERE email = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }

    @Override
    public boolean checkEmailGestoriExist(String email) throws SQLException {
        String SQL = ("SELECT email FROM logingestore WHERE email = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }

    @Override
    public boolean loginUtente(String email, String password) throws SQLException {
        String SQL = ("SELECT email FROM loginutente WHERE email = ? AND password = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, email);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }

    @Override
    public boolean loginGestore(String email, String password) throws SQLException {
        String SQL = ("SELECT email FROM logingestore WHERE email = ? AND password = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, email);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }

    @Override
    public void deleteAutenticazione(Autenticazione autenticazione) throws SQLException {
        String SQL = ("Delete FROM \"Autenticazione\" WHERE email = ? AND password = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, autenticazione.getEmail());
        pstmt.setString(2, autenticazione.getPassword());
        pstmt.executeQuery();
        pstmt.close();
        conn.close();
    }
}
