package com.unina.project.database;

import com.unina.project.Sede;

import java.sql.SQLException;

public interface SedeDAO {
    void insertSede(Sede sede,String codGestore) throws SQLException;
}
