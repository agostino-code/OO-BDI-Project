package com.unina.project.database.postgre;

import com.unina.project.AreaTematica;
import com.unina.project.Corso;
import com.unina.project.database.AreaTematicaDAO;
import com.unina.project.database.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgreAreaTematicaDAO implements AreaTematicaDAO {
    private final JDBC postgreJDBC=new PostgreJDBC();

    @Override
    public void insertAreaTematica(List<String> areetematiche) throws SQLException {
        for(String areatematica:areetematiche) {
            String SQL = ("INSERT INTO \"AreaTematica\" (tag) VALUES (?);");
            Connection conn = postgreJDBC.Connessione();
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, areatematica);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        }
    }

    @Override
    public void associaAreaTematica(List<String> areetematiche, String codCorso) throws SQLException {
        for(String areatematica:areetematiche) {
            String SQL = ("INSERT INTO \"Appartiene\" (tag,\"codCorso\") VALUES (?,?);");
            Connection conn = postgreJDBC.Connessione();
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, areatematica);
            pstmt.setString(2, codCorso);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        }
    }

    @Override
    public List<AreaTematica> getAreeTematiche() throws SQLException {
        String SQL = ("SELECT * FROM \"AreaTematica\";");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        List<AreaTematica> areaTematicaList= new ArrayList<>();
        while(rs.next()) {
                AreaTematica areaTematica= new AreaTematica();
                areaTematica.setTag(rs.getString("tag"));
                areaTematicaList.add(areaTematica);
            }
        pstmt.close();
        conn.close();
        return areaTematicaList;
    }

    @Override
    public void deleteAreaTematica(Corso corso) throws SQLException {
        String SQL = ("DELETE FROM \"Appartiene\" WHERE \"codCorso\"= ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1,corso.getCodCorso());
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }
}

