package com.unina.project.database;

import com.unina.project.Statistiche;
import com.unina.project.controller.profile.PresenzeLezioni;

import java.sql.SQLException;
import java.util.List;

public interface StatisticheDAO {

    Statistiche getStatistiche(String codCorso) throws SQLException;

    List<PresenzeLezioni> getPresenzeLezioni(String codCorso) throws SQLException;
}
