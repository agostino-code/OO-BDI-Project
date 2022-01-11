package com.unina.project.database;

import com.unina.project.AreaTematica;

import java.sql.SQLException;
import java.util.List;

public interface AreaTematicaDAO{
    void insertAreaTematica(List<String> areetematiche) throws SQLException;

    void associaAreaTematica(List<String> areetematiche, String codCorso) throws SQLException;

    List<AreaTematica> getAreeTematiche() throws SQLException;
}
