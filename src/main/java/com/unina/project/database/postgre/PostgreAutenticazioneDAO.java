package com.unina.project.database.postgre;

import com.unina.project.database.AutenticazioneDAO;

import java.sql.*;

public class PostgreAutenticazioneDAO implements AutenticazioneDAO {

    @Override
    public void insertAutenticazione(String email, String password) {

    }

    @Override
    public boolean checkEmailExist(String email) throws SQLException {
        PostgreJDBC postgreJDBC=new PostgreJDBC();
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
}
