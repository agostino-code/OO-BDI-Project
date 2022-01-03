package com.unina.project.database.postgre;

import com.unina.project.Autenticazione;
import com.unina.project.Sede;
import com.unina.project.database.SedeDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSedeDAO implements SedeDAO {
    private final PostgreJDBC postgreJDBC=new PostgreJDBC();

    @Override
    public String insertSede(Sede sede) throws SQLException {
        String SQL = ("INSERT INTO \"Sede\" (città, via, civico,provincia) VALUES (?,?,?,?) returning \"codSede\";");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, sede.citta);
        pstmt.setString(2, sede.via);
        pstmt.setString(3, sede.civico);
        pstmt.setString(4, sede.provincia);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        rs.next();
        String codSede= rs.getString(1);
        pstmt.close();
        conn.close();
        return codSede;
    }
}
