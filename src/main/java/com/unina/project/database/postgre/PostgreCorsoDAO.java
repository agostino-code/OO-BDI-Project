package com.unina.project.database.postgre;

import com.unina.project.AreaTematica;
import com.unina.project.Corso;
import com.unina.project.controller.profile.CorsoRicerca;
import com.unina.project.database.CorsoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreCorsoDAO implements CorsoDAO {
    private final PostgreJDBC postgreJDBC=new PostgreJDBC();

    @Override
    public String insertCorso(Corso corso, String codGestore) throws SQLException {
        String SQL = ("INSERT INTO \"Corso\" (titolo, descrizione, \"iscrizioniMassime\", \"tassoPresenzeMinime\",\"numeroLezioni\",\"codGestore\",\"Privato\") VALUES (?,?,?,?,?,?,?) returning \"codCorso\";");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, corso.titolo);
        pstmt.setString(2, corso.descrizione);
        pstmt.setInt(3, corso.iscrizioniMassime);
        pstmt.setInt(4, corso.tassoPresenzeMinime);
        pstmt.setInt(5, corso.numeroLezioni);
        pstmt.setString(6,codGestore);
        pstmt.setBoolean(7,corso.Privato);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        rs.next();
        String codCorso= rs.getString(1);
        pstmt.close();
        conn.close();
        return codCorso;
    }
    public boolean checkTitoloExist(String titolo) throws SQLException {
        String SQL = ("SELECT titolo FROM \"Corso\" WHERE titolo = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, titolo);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }

    @Override
    public List<Corso> getCorsi(String codGestore) throws SQLException {
        String SQL = ("SELECT titolo, descrizione, \"iscrizioniMassime\", \"tassoPresenzeMinime\", \"numeroLezioni\",\"codCorso\",\"Privato\" FROM \"Corso\" WHERE \"codGestore\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codGestore);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        List<Corso> corsi= new ArrayList<>();
        while(rs.next()) {
            Corso corso= new Corso();
            corso.setTitolo(rs.getString("titolo"));
            corso.setDescrizione(rs.getString("descrizione"));
            corso.setIscrizioniMassime(rs.getInt("iscrizioniMassime"));
            corso.setTassoPresenzeMinime(rs.getInt("tassoPresenzeMinime"));
            corso.setNumeroLezioni(rs.getInt("numeroLezioni"));
            corso.setCodCorso(rs.getString("codCorso"));
            corso.setPrivato(rs.getBoolean("Privato"));
            corso.setAreetematiche(getAreeTematiche(corso.codCorso));
            corsi.add(corso);
        }
        pstmt.close();
        conn.close();
        return corsi;
    }

    @Override
    public void deleteCorso(Corso corso) throws SQLException {
        String SQL = ("Delete FROM \"Corso\" WHERE titolo = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, corso.titolo);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }

    @Override
    public List<CorsoRicerca> ricercaCorsi(String SQL) throws SQLException {
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement(SQL);
        stmt.execute();
        ResultSet rs =stmt.getResultSet();
        List<CorsoRicerca> corsi= new ArrayList<>();
        while(rs.next()) {
            CorsoRicerca corso= new CorsoRicerca();
            corso.setCodGestore(rs.getString("codGestore"));
            corso.setDescrizione(rs.getString("descrizione"));
            corso.setTitolo(rs.getString("titolo"));
            corso.setNome(rs.getString("nome"));
            corso.setCitta(rs.getString("città"));
            corso.setCodCorso(rs.getString("codCorso"));
            corso.setProvincia(rs.getString("provincia"));
            corso.setPrivato(rs.getBoolean("Privato"));
            corso.setAreetematiche(getAreeTematiche(corso.codCorso));
            corsi.add(corso);
        }
        stmt.close();
        conn.close();
        return corsi;
    }

    @Override
    public void updateCorso(Corso corso) throws SQLException {
        String SQL = ("UPDATE \"Corso\" SET titolo = '"+corso.titolo+
                "', descrizione = '"+corso.descrizione+
                "', \"iscrizioniMassime\" = "+corso.iscrizioniMassime+
                ", \"tassoPresenzeMinime\" = "+corso.tassoPresenzeMinime+
                ",\"numeroLezioni\" = "+corso.numeroLezioni+
                ",\"Privato\" = "+corso.Privato+
                " WHERE \"codCorso\" = '"+corso.codCorso+"';");
        Connection conn = postgreJDBC.Connessione();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(SQL);
        stmt.close();
        conn.close();
    }

    @Override
    public List<AreaTematica> getAreeTematiche(String codCorso) throws SQLException {
        String SQL = ("SELECT tag FROM \"Appartiene\" WHERE \"codCorso\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codCorso);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        List<AreaTematica> areeTematiche= new ArrayList<>();
        while(rs.next()) {
            AreaTematica areaTematica= new AreaTematica();
            areaTematica.setTag(rs.getString("tag"));
            areeTematiche.add(areaTematica);
        }
        pstmt.close();
        conn.close();
        return areeTematiche;
    }
}
