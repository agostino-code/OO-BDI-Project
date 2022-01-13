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
        String SQL = ("SELECT email, nome, cognome, sesso, \"dataNascita\", \"comunediNascita\", \"codOperatore\", \"codiceFiscale\" FROM \"operatoricorsi\" WHERE \"codCorso\" = ?;");
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
}
