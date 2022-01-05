package com.unina.project.database.postgre;

import com.unina.project.Gestore;
import com.unina.project.database.GestoreDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreGestoreDAO implements GestoreDAO {
    private final PostgreJDBC postgreJDBC=new PostgreJDBC();

    public String insertGestore(Gestore gestore) throws SQLException {
        String SQL = ("INSERT INTO \"Gestore\" (nome, descrizione, telefono, email) VALUES (?,?,?,?) returning \"codGestore\";");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, gestore.nome);
        pstmt.setString(2, gestore.descrizione);
        pstmt.setString(3, gestore.telefono);
        pstmt.setString(4, gestore.email);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        rs.next();
        String codGestore= rs.getString(1);
        pstmt.close();
        conn.close();
        return codGestore;
    }

    public boolean checkNomeExist(String nome) throws SQLException {
        String SQL = ("SELECT nome FROM \"Gestore\" WHERE nome = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, nome);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }

    public String returncodGestore(String email) throws SQLException {
        String SQL = ("SELECT \"codGestore\" FROM \"Gestore\" WHERE email = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, email);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        rs.next();
        String codGestore= rs.getString(1);
        pstmt.close();
        conn.close();
        return codGestore;
    }
}
