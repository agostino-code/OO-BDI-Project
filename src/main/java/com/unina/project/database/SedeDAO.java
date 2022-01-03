package com.unina.project.database;

import com.unina.project.Autenticazione;
import com.unina.project.Sede;

import java.sql.SQLException;

public interface SedeDAO {
    String insertSede(Sede sede) throws SQLException;
}
