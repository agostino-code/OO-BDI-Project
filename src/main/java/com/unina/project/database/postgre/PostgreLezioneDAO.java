package com.unina.project.database.postgre;

import com.unina.project.Lezione;
import com.unina.project.Studente;
import com.unina.project.database.LezioneDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreLezioneDAO implements LezioneDAO {
    private final PostgreJDBC postgreJDBC=new PostgreJDBC();

    @Override
    public List<Lezione> getLezioni(String codCorso) throws SQLException {
        String SQL = ("SELECT * FROM \"Lezione\" WHERE \"codCorso\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codCorso);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        List<Lezione> lezioni= new ArrayList<>();
        while(rs.next()) {
            Lezione lezione= new Lezione();
            lezione.setTitolo(rs.getString("titolo"));
            lezione.setDescrizione(rs.getString("descrizione"));
            lezione.setDataoraInizio(rs.getTimestamp("dataoraInizio").toLocalDateTime());
            lezione.setDurata(rs.getTime("durata").toLocalTime());
            lezione.setCodLezione(rs.getString("codLezione"));
            lezioni.add(lezione);
        }
        pstmt.close();
        conn.close();
        return lezioni;
    }

    @Override
    public String getCorsoLezione(String codLezione) throws SQLException {
        String SQL = ("SELECT * FROM \"Lezione\" WHERE \"codLezione\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codLezione);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        rs.next();
        String codCorso=rs.getString("codCorso");
        pstmt.close();
        conn.close();
        return codCorso;
    }

    @Override
    public List<Studente> getStudentiPrenotati(String codLezione) throws SQLException {
        String codCorso=getCorsoLezione(codLezione);
        String SQL = ("SELECT email, nome, cognome,sesso,\"dataNascita\",\"codStudente\",\"Idoneo\",\"Presente\" FROM \"studenticorsi\" NATURAL JOIN \"Prenotazioni\" WHERE \"codLezione\" = ? AND \"codCorso\" ='"+codCorso+"'");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codLezione);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        List<Studente> studenti= new ArrayList<>();
        while(rs.next()) {
            Studente studente=new Studente();
            studente.setEmail(rs.getString("email"));
            studente.setNome(rs.getString("nome"));
            studente.setCognome(rs.getString("cognome"));
            studente.setDataNascita(rs.getDate("dataNascita").toLocalDate());
            studente.setSesso(rs.getString("sesso"));
            studente.setCodStudente(rs.getString("codStudente"));
            studente.setIdoneo(rs.getBoolean("Idoneo"));
            studente.setPresente(rs.getBoolean("Presente"));
            studenti.add(studente);
        }
        pstmt.close();
        conn.close();
        return studenti;
    }

    @Override
    public void insertLezione(Lezione lezione) throws SQLException {
        String SQL = ("INSERT INTO \"Lezione\" (titolo,descrizione, \"dataoraInizio\", durata, \"codCorso\") VALUES (?,?,?,?,?);");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, lezione.getTitolo());
        pstmt.setString(2, lezione.getDescrizione());
        pstmt.setTimestamp(3, Timestamp.valueOf(lezione.getDataoraInizio()));
        pstmt.setTime(4, Time.valueOf(lezione.getDurata()));
        pstmt.setString(5,lezione.getCodCorso());
        pstmt.execute();
        pstmt.close();
        conn.close();
    }

    @Override
    public void deleteLezione(String codLezione) throws SQLException {
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"Lezione\" where \"codLezione\"=?");
        stmt.setString(1, codLezione);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    @Override
    public void prenotaLezione(String codLezione,String codStudente) throws SQLException {
        String SQL = ("INSERT INTO \"Prenotazioni\" (\"codLezione\", \"codStudente\") VALUES (?,?);");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codLezione);
        pstmt.setString(2, codStudente);
        pstmt.execute();
        pstmt.close();
        conn.close();
    }

    @Override
    public void deletePrenotazioneLezione(String codLezione, String codStudente) throws SQLException {
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"Prenotazioni\" where \"codLezione\"=? and \"codStudente\"=?");
        stmt.setString(1,codLezione);
        stmt.setString(2,codStudente);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    @Override
    public void confermapresenza(String codStudente,String codLezione) throws SQLException {
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("update \"Prenotazioni\" set \"Presente\"='true' where \"codStudente\"=? and \"codLezione\"=? " );
        stmt.setString(1,codStudente);
        stmt.setString(2,codLezione);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    @Override
    public void nonpresente(String codStudente,String codLezione) throws SQLException {
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("update \"Prenotazioni\" set \"Presente\"='false' where \"codStudente\"=? and \"codLezione\"=? " );
        stmt.setString(1,codStudente);
        stmt.setString(2,codLezione);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    @Override
    public List<Lezione> getLezioniPrenotate(String codStudente) throws SQLException {
        String SQL = ("SELECT * FROM \"Lezione\" NATURAL JOIN \"Prenotazioni\" WHERE \"codStudente\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codStudente);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        List<Lezione> lezioni= new ArrayList<>();
        while(rs.next()) {
            Lezione lezione= new Lezione();
            lezione.setCodCorso(rs.getString("codCorso"));
            lezione.setTitolo(rs.getString("titolo"));
            lezione.setDescrizione(rs.getString("descrizione"));
            lezione.setDataoraInizio(rs.getTimestamp("dataoraInizio").toLocalDateTime());
            lezione.setDurata(rs.getTime("durata").toLocalTime());
            lezione.setCodLezione(rs.getString("codLezione"));
            lezioni.add(lezione);
        }
        pstmt.close();
        conn.close();
        return lezioni;
    }

    @Override
    public boolean checkStudenteIscrittoLezione(String codLezione, String codStudente) throws SQLException {
        String SQL = ("SELECT \"codStudente\" FROM \"Prenotazioni\" WHERE \"codLezione\" = ? AND \"codStudente\"=?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codLezione);
        pstmt.setString(2,codStudente);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }

    @Override
    public Integer countNumeroLezioni(String CodCorso) throws SQLException{
        int numerolezioni;
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("Select count(*) from \"Lezione\" where \"codCorso\"=?");
        stmt.setString(1,CodCorso);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        numerolezioni =rs.getInt(1);
        return numerolezioni;
    }
}
