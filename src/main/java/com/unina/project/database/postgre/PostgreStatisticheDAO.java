package com.unina.project.database.postgre;

import com.unina.project.Statistiche;
import com.unina.project.controller.profile.PresenzeLezioni;
import com.unina.project.database.StatisticheDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgreStatisticheDAO implements StatisticheDAO {
    private final PostgreJDBC postgreJDBC=new PostgreJDBC();
    @Override
    public Statistiche getStatistiche(String codCorso) throws SQLException {
        String SQL = ("SELECT * FROM \"Statistiche\" WHERE \"codCorso\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codCorso);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        rs.next();
        Statistiche statistiche=new Statistiche();
        statistiche.setPercentualeRiempimento(rs.getInt("percentualeRempimento"));
        statistiche.setPresenzeMassime(rs.getInt("presenzeMassime"));
        statistiche.setPresenzeMinime(rs.getInt("presenzeMinime"));
        statistiche.setPresenzeMedie(rs.getFloat("presenzeMedie"));
        pstmt.close();
        conn.close();
        return statistiche;
    }

    @Override
    public List<PresenzeLezioni> getPresenzeLezioni(String codCorso) throws SQLException {
        String SQL = ("SELECT * FROM presenzelezioni WHERE \"codCorso\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codCorso);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        List<PresenzeLezioni> stats= new ArrayList<>();
        while(rs.next()) {
            PresenzeLezioni presenzeLezioni = new PresenzeLezioni();
            presenzeLezioni.setCodLezione(rs.getString("codLezione"));
            presenzeLezioni.setNumero_presenze(rs.getInt("numero_presenti"));
            stats.add(presenzeLezioni);
        }
        pstmt.close();
        conn.close();
        return stats;
    }
}
