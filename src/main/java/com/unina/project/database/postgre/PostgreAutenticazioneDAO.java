package com.unina.project.database.postgre;

import com.unina.project.Autenticazione;
import com.unina.project.database.AutenticazioneDAO;

import java.sql.*;

public class PostgreAutenticazioneDAO implements AutenticazioneDAO {
    private PostgreJDBC postgreJDBC=new PostgreJDBC();

    @Override
    public void insertAutenticazione(Autenticazione autenticazione) throws SQLException {
        String SQL = ("INSERT INTO \"Autenticazione\" (email, password) VALUES (?,?);");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, autenticazione.email);
        pstmt.setString(2, autenticazione.password);
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
            if(rs.next())
            {
                return false;
            }
            else{
                return true;
            }
    }

    @Override
    public boolean checkLogin(String email, String password) throws SQLException {
        String SQL = ("SELECT email FROM \"Autenticazione\" WHERE email = ? AND password = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, email);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next())
        {
            return false;
        }
        else{
            return true;
        }
    }
}
