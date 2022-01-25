package com.unina.project.database.postgre;

import com.unina.project.Studente;
import com.unina.project.database.StudenteDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgreStudenteDAO implements StudenteDAO {
    private final PostgreJDBC postgreJDBC=new PostgreJDBC();

    @Override
    public String setStudente(String codiceFiscale) throws SQLException {
        String SQL = ("INSERT INTO \"Studente\" (\"codiceFiscale\") VALUES (?) returning \"codStudente\";");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codiceFiscale);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        rs.next();
        String codStudente= rs.getString(1);
        pstmt.close();
        conn.close();
        return codStudente;
    }

    @Override
    public boolean checkStudenteExist(String codiceFiscale) throws SQLException {
        String SQL = ("SELECT \"codStudente\" FROM \"Studente\" WHERE \"codiceFiscale\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codiceFiscale);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }

    @Override
    public void iscriviStudente(String codStudente, String codCorso) throws SQLException {
        String SQL = ("INSERT INTO \"Iscritti\" (\"codStudente\",\"codCorso\") VALUES (?,?);");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codStudente);
        pstmt.setString(2, codCorso);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }

    @Override
    public List<Studente> getStudentiDaAccettare(String codCorso) throws SQLException {
        String SQL = ("SELECT email, nome, cognome,sesso,\"dataNascita\",\"codStudente\",\"Idoneo\",\"Richiesta\" FROM \"studenticorsi\" WHERE \"codCorso\" = ? AND \"Richiesta\"=false;");
        return getStudente(codCorso, SQL);
    }

    public List<Studente> getStudente(String codCorso, String SQL) throws SQLException {
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codCorso);
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
            studenti.add(studente);
        }
        pstmt.close();
        conn.close();
        return studenti;
    }

    @Override
    public List<Studente> getStudentiAccettati(String codCorso) throws SQLException {
        String SQL = ("SELECT email, nome, cognome,sesso,\"dataNascita\",\"codStudente\",\"Idoneo\",\"Richiesta\" FROM \"studenticorsi\" WHERE \"codCorso\" = ? AND \"Richiesta\"=true;");
        return getStudente(codCorso, SQL);
    }
    @Override
    public String getCodStudente(String codiceFiscale) throws SQLException {
        String SQL = ("SELECT \"codStudente\" FROM \"Studente\" WHERE \"codiceFiscale\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codiceFiscale);
        ResultSet rs = pstmt.executeQuery();
        String codStudente="";
        if (rs.next()){
            codStudente= rs.getString(1);
        }
        pstmt.close();
        conn.close();
        return codStudente;
    }

    @Override
    public void richiestaAccettata(String CodStudente, String CodCorso) throws SQLException{
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("Update \"Iscritti\" set \"Richiesta\"='TRUE' where \"codStudente\"=? and \"codCorso\"=?");
        stmt.setString(1,CodStudente);
        stmt.setString(2,CodCorso);
        stmt.executeUpdate();
        stmt.close();
        conn.close();

    }
    @Override
    public void richiestaRifiutata(String CodStudente, String CodCorso) throws SQLException{
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"Iscritti\" where \"codStudente\"=? and \"codCorso\"=?");
        stmt.setString(1,CodStudente);
        stmt.setString(2,CodCorso);
        stmt.executeUpdate();
        stmt.close();
        conn.close();

    }

    @Override
    public boolean checkStudenteIscritto(String codCorso,String codStudente) throws SQLException {
        String SQL = ("SELECT \"codStudente\" FROM \"Iscritti\" WHERE \"codCorso\" = ? AND \"codStudente\"=?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codCorso);
        pstmt.setString(2,codStudente);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }



}
