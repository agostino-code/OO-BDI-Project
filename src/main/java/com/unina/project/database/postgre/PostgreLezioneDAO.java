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
    public List<Studente> getStudentiPrenotati(String codLezione) throws SQLException {
        String SQL = ("SELECT email, nome, cognome,sesso,\"dataNascita\",\"codStudente\",\"Idoneo\",\"Presente\" FROM \"studenticorsi\" NATURAL JOIN \"Prenotazioni\" WHERE \"codLezione\" = ?");
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
        pstmt.setString(1, lezione.titolo);
        pstmt.setString(2, lezione.descrizione);
        pstmt.setTimestamp(3, Timestamp.valueOf(lezione.dataoraInizio));
        pstmt.setTime(4, Time.valueOf(lezione.durata));
        pstmt.setString(5,lezione.codCorso);
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
}
