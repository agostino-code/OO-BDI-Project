package com.unina.project.database.postgre;

import com.unina.project.Sede;
import com.unina.project.database.JDBC;
import com.unina.project.database.SedeDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgreSedeDAO implements SedeDAO {
    private final JDBC postgreJDBC=new PostgreJDBC();

    @Override
    public void insertSede(Sede sede,String codGestore) throws SQLException {
        String SQL = ("INSERT INTO \"Sede\" (città, via, civico, provincia, \"codGestore\") VALUES (?,?,?,?,?);");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, sede.getCivico());
        pstmt.setString(2, sede.getVia());
        pstmt.setString(3, sede.getCivico());
        pstmt.setString(4, sede.getProvincia());
        pstmt.setString(5, codGestore);
        pstmt.execute();
        pstmt.close();
        conn.close();
    }
}
