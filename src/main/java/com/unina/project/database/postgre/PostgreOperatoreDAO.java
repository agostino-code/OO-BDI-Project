package com.unina.project.database.postgre;

import com.unina.project.Operatore;
import com.unina.project.database.OperatoreDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgreOperatoreDAO implements OperatoreDAO {
    private final PostgreJDBC postgreJDBC=new PostgreJDBC();

    @Override
    public String setOperatore(String codiceFiscale) throws SQLException {
        String SQL = ("INSERT INTO \"Operatore\" (\"codiceFiscale\") VALUES (?) returning \"codOperatore\";");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codiceFiscale);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        rs.next();
        String codOperatore= rs.getString(1);
        pstmt.close();
        conn.close();
        return codOperatore;
    }

    @Override
    public void associaOperatore(String codOperatore, String codCorso) throws SQLException {
        String SQL = ("INSERT INTO \"Coordina\" (\"codOperatore\",\"codCorso\") VALUES (?,?);");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codOperatore);
        pstmt.setString(2, codCorso);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }

    @Override
    public List<Operatore> getOperatori(String codCorso) throws SQLException {
        String SQL = ("SELECT email, nome, cognome, sesso, \"dataNascita\", \"comunediNascita\", \"codOperatore\", \"codiceFiscale\",\"Richiesta\" FROM \"operatoricorsi\" WHERE \"codCorso\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codCorso);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        List<Operatore> operatori= new ArrayList<>();
        while(rs.next()) {
            Operatore operatore=new Operatore();
            operatore.setEmail(rs.getString("email"));
            operatore.setNome(rs.getString("nome"));
            operatore.setCognome(rs.getString("cognome"));
            operatore.setSesso(rs.getString("sesso"));
            operatore.setDataNascita(rs.getDate("dataNascita").toLocalDate());
            operatore.setComuneDiNascita(rs.getString("comunediNascita"));
            operatore.setCodOperatore(rs.getString("codOperatore"));
            operatore.setCodiceFiscale(rs.getString("codiceFiscale"));
            operatore.setRichiesta(rs.getBoolean("Richiesta"));
            operatori.add(operatore);
        }
        pstmt.close();
        conn.close();
        return operatori;
    }

    @Override
    public boolean checkOperatoreExist(String codiceFiscale) throws SQLException {
        String SQL = ("SELECT \"codOperatore\" FROM \"Operatore\" WHERE \"codiceFiscale\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codiceFiscale);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }

    @Override
    public String getCodOperatore(String codiceFiscale) throws SQLException {
        String SQL = ("SELECT \"codOperatore\" FROM \"Operatore\" WHERE \"codiceFiscale\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codiceFiscale);
        ResultSet rs = pstmt.executeQuery();
        String codOperatore="";
        if (rs.next()){
            codOperatore= rs.getString(1);
        }
        pstmt.close();
        conn.close();
        return codOperatore;
    }

    @Override
    public boolean checkOperatoreDaAccettare(String codOperatore, String codCorso) throws SQLException {
        String SQL = ("SELECT \"codOperatore\" FROM \"Coordina\" WHERE  \"codCorso\" = ? AND \"codOperatore\" = ? AND \"Richiesta\" = false;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codCorso);
        pstmt.setString(2, codOperatore);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }

    @Override
    public void annullaGestioneCorso(String codOperatore,String codCorso) throws SQLException {
      Connection conn = postgreJDBC.Connessione();
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"Coordina\" where \"codCorso\"=? and \"codOperatore\"=? " );
      stmt.setString(1,codCorso);
      stmt.setString(2,codOperatore);
      stmt.executeUpdate();
      stmt.close();
      conn.close();
    }

    @Override
    public void accettaGestioneCorso(String codOperatore,String codCorso) throws SQLException {
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement stmt = conn.prepareStatement("update \"Coordina\" set \"Richiesta\"='true' where \"codCorso\"=? and \"codOperatore\"=? " );
        stmt.setString(1,codCorso);
        stmt.setString(2,codOperatore);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    @Override
    public boolean checkOperatoreCorso(String codOperatore, String codCorso) throws SQLException {
        String SQL = ("SELECT \"codOperatore\" FROM \"Coordina\" WHERE  \"codCorso\" = ? AND \"codOperatore\" = ?;");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, codCorso);
        pstmt.setString(2, codOperatore);
        ResultSet rs = pstmt.executeQuery();
        return !rs.next();
    }
}
