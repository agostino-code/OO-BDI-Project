package com.unina.project.database.postgre;

import com.unina.project.Corso;
import com.unina.project.database.CorsoDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreCorsoDAO implements CorsoDAO {
    private final PostgreJDBC postgreJDBC=new PostgreJDBC();

    @Override
    public String insertCorso(Corso corso, String codGestore) throws SQLException {
        String SQL = ("INSERT INTO \"Corso\" (titolo, descrizione, \"iscrizioniMassime\", \"tassoPresenzMinime\",\"codGestore\") VALUES (?,?,?,?,?) returning \"codCorso\";");
        Connection conn = postgreJDBC.Connessione();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, corso.titolo);
        pstmt.setString(2, corso.descrizione);
        pstmt.setInt(3, corso.iscrizioniMassime);
        pstmt.setFloat(4, corso.tassoPresenzeMinime);
        pstmt.setString(5,codGestore);
        pstmt.execute();
        ResultSet rs =pstmt.getResultSet();
        rs.next();
        String codCorso= rs.getString(1);
        pstmt.close();
        conn.close();
        return codCorso;
    }
}
