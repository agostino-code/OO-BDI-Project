package com.unina.project.database.postgre;

import com.unina.project.AreaTematica;
import com.unina.project.Corso;
import com.unina.project.controller.profile.CorsoRicerca;
import com.unina.project.database.CorsoDAO;
import com.unina.project.database.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreCorsoDAO implements CorsoDAO {
    private final JDBC postgreJDBC=new PostgreJDBC();

    @Override
    public String insertCorso(Corso corso, String codGestore) throws SQLException {
        String SQL = ("INSERT INTO \"Corso\" (titolo, descrizione, \"iscrizioniMassime\", \"tassoPresenzeMinime\",\"numeroLezioni\",\"codGestore\",\"Privato\") VALUES (?,?,?,?,?,?,?) returning \"codCorso\";");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, corso.getTitolo());
        pstmt.setString(2, corso.getDescrizione());
        pstmt.setInt(3, corso.getIscrizioniMassime());
        pstmt.setInt(4, corso.tassoPresenzeMinime);
        pstmt.setInt(5, corso.getNumeroLezioni());
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

    @Override
    public List<Corso> getCorsi(String codGestore) throws SQLException {
        String SQL = ("SELECT titolo, descrizione, \"iscrizioniMassime\", \"tassoPresenzeMinime\", \"numeroLezioni\",\"codCorso\",\"Privato\" FROM \"Corso\" WHERE \"codGestore\" = ?;");
        return getCorsoList(codGestore, SQL);
    }

    public List<Corso> getCorsoList(String codGestore, String SQL) throws SQLException {
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
            corso.setAreetematiche(getAreeTematiche(corso.getCodCorso()));
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
        pstmt.setString(1, corso.getTitolo());
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
            corso.setTelefono(rs.getString("telefono"));
            corso.setProvincia(rs.getString("provincia"));
            corso.setPrivato(rs.getBoolean("Privato"));
            corso.setNumeroLezioni(rs.getInt("numeroLezioni"));
            corso.setTassoPresenzeMinime(rs.getInt("tassoPresenzeMinime"));
            corso.setIscrizioniMassime(rs.getInt("iscrizioniMassime"));
            corso.setAreetematiche(getAreeTematiche(corso.codCorso));
            corsi.add(corso);
        }
        stmt.close();
        conn.close();
        return corsi;
    }

    @Override
    public void updateCorso(Corso corso) throws SQLException {
        String SQL = ("UPDATE \"Corso\" SET titolo = '"+corso.getTitolo()+
                "', descrizione = '"+corso.getDescrizione()+
                "', \"iscrizioniMassime\" = "+corso.getIscrizioniMassime()+
                ", \"tassoPresenzeMinime\" = "+corso.tassoPresenzeMinime+
                ",\"numeroLezioni\" = "+corso.getNumeroLezioni()+
                ",\"Privato\" = "+corso.Privato+
                " WHERE \"codCorso\" = '"+corso.getCodCorso()+"';");
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

    @Override
    public Integer numeroIscrittiCorso(String CodCorso) throws SQLException{
        int NumeroIscritti;
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("Select count(*) from \"Iscritti\" where \"codCorso\"=? and \"Richiesta\"=true");
        stmt.setString(1,CodCorso);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        NumeroIscritti =rs.getInt(1);
        return NumeroIscritti;
    }

    @Override
    public Integer numeroLezioniCorso(String CodCorso) throws SQLException{
        int NumeroIscritti;
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("Select count(*) from \"Lezione\" where \"codCorso\"=?");
        stmt.setString(1,CodCorso);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        NumeroIscritti =rs.getInt(1);
        return NumeroIscritti;
    }

    @Override
    public List<Corso> getCorsiOperatore(String codOperatore) throws SQLException {
        String SQL = ("SELECT titolo, descrizione, \"iscrizioniMassime\", \"tassoPresenzeMinime\", \"numeroLezioni\",\"codCorso\",\"Privato\" FROM \"Corso\" NATURAL JOIN \"Coordina\" WHERE \"codOperatore\" = ?;");
        return getCorsoList(codOperatore, SQL);
    }

    @Override
    public List<Corso> getCorsiOperatoreAccettati(String codOperatore) throws SQLException {
        String SQL = ("SELECT titolo, descrizione, \"iscrizioniMassime\", \"tassoPresenzeMinime\", \"numeroLezioni\",\"codCorso\",\"Privato\" FROM \"Corso\" NATURAL JOIN \"Coordina\" WHERE \"codOperatore\" = ? AND \"Richiesta\"=true;");
        return getCorsoList(codOperatore, SQL);
    }

    @Override
    public List<Corso> getCorsiStudente(String codStudente) throws SQLException {
        String SQL = ("SELECT titolo, descrizione, \"iscrizioniMassime\", \"tassoPresenzeMinime\", \"numeroLezioni\",\"codCorso\",\"Privato\" FROM \"Corso\" NATURAL JOIN \"Iscritti\" WHERE \"codStudente\" = ? AND \"Richiesta\"=true;");
        return getCorsoList(codStudente, SQL);
    }

    @Override
    public void disiscrivitiCorso(String codStudente, String codCorso) throws SQLException {
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"Iscritti\" where \"codCorso\"=? and \"codStudente\"=?");
        stmt.setString(1,codCorso);
        stmt.setString(2,codStudente);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    @Override
    public Integer getNumeroLezioni(String codCorso) throws SQLException {
        String SQL = ("SELECT \"numeroLezioni\" FROM \"Corso\" WHERE \"codCorso\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codCorso);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        rs.next();
        Integer numeroLezioni=rs.getInt("numeroLezioni");
        pstmt.close();
        conn.close();
        return numeroLezioni;
    }

    @Override
    public Integer numeroPrenotati(String CodCorso) throws SQLException{
        int NumeroIscritti;
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("Select count(*) from \"Lezione\" NATURAL JOIN \"Prenotazioni\" WHERE \"codCorso\"=?");
        stmt.setString(1,CodCorso);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        NumeroIscritti =rs.getInt(1);
        return NumeroIscritti;
    }
}
