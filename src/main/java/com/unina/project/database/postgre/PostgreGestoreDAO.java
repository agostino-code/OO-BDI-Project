package com.unina.project.database.postgre;

import com.unina.project.Corso;
import com.unina.project.Gestore;
import com.unina.project.database.GestoreDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Corso> getCorsi(String codGestore) throws SQLException {
        String SQL = ("SELECT titolo, descrizione, \"iscrizioniMassime\", \"tassoPresenzeMinime\", \"numeroLezioni\" FROM \"Corso\" WHERE \"codGestore\" = ?;");
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
            corsi.add(corso);
        }
        pstmt.close();
        conn.close();
        return corsi;
    }

    public Gestore getGestore(String codGestore) throws SQLException {
        String SQL = ("SELECT nome, descrizione, telefono, email FROM \"Gestore\" WHERE \"codGestore\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codGestore);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        Gestore gestore= new Gestore();
        while(rs.next()) {
            gestore.setNome(rs.getString("nome"));
            gestore.setDescrizione(rs.getString("descrizione"));
            gestore.setTelefono(rs.getString("telefono"));
            gestore.setEmail(rs.getString("email"));
        }
        pstmt.close();
        conn.close();
        return gestore;
    }
}
