package com.unina.project.controller.profile;

import com.unina.project.Corso;
import com.unina.project.Main;
import com.unina.project.Statistiche;
import com.unina.project.database.StatisticheDAO;
import com.unina.project.database.postgre.PostgreStatisticheDAO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Objects;

public class StatisticheController {
    @FXML
    public final CategoryAxis xAxis = new CategoryAxis();
    public final NumberAxis yAxis = new NumberAxis();
    @FXML
    public LineChart<String, Number> statisticheLineChart= new LineChart<>(xAxis, yAxis);
    public BarChart<String,Number> statisticheBarChart=new BarChart<>(xAxis, yAxis);
    public PieChart statistichePieChart;

    private Corso corso;
    private Statistiche statistiche=new Statistiche();
    private final StatisticheDAO statisticheDAO=new PostgreStatisticheDAO();

    public void setStatistiche(Corso corso){
        this.corso=corso;
        try {
            this.statistiche=statisticheDAO.getStatistiche(corso.getCodCorso());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setStatisticheRiempimento();
        setStatisticheLezioniSingole();
        setStatisticheLezioni();
    }

    private void setStatisticheLezioni() {
        statisticheBarChart.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm());
        statisticheBarChart.setTitle("Statistiche Lezioni");
        statisticheBarChart.setAnimated(true);
        yAxis.setMinorTickVisible(false);
        xAxis.setTickLabelRotation(90);
        PresenzeLezioni presenzeMinime=new PresenzeLezioni();
        PresenzeLezioni presenzeMassime=new PresenzeLezioni();
        try {
            for (PresenzeLezioni i : statisticheDAO.getPresenzeLezioni(corso.getCodCorso())) {
                if(i.getNumero_presenze().equals(statistiche.getPresenzeMinime())){
                    presenzeMinime.setCodLezione(i.getCodLezione());
                }
                if(i.getNumero_presenze().equals(statistiche.getPresenzeMassime())){
                    presenzeMassime.setCodLezione(i.getCodLezione());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("Presenze Minime");
        XYChart.Series<String,Number> series1 = new XYChart.Series<>();
        series1.setName("Presenze Massime");
        XYChart.Series<String,Number> series2 = new XYChart.Series<>();
        series2.setName("Presenze Medie");
        series.getData().add(new XYChart.Data<>(presenzeMinime.getCodLezione(), statistiche.getPresenzeMinime()));
        series1.getData().add(new XYChart.Data<>(presenzeMassime.getCodLezione(), statistiche.getPresenzeMassime()));
        series2.getData().add(new XYChart.Data<>("", statistiche.getPresenzeMedie()));
        statisticheBarChart.getData().add(series);
        statisticheBarChart.getData().add(series1);
        statisticheBarChart.getData().add(series2);
    }

    private void setStatisticheLezioniSingole() {
        statisticheLineChart.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm());
        statisticheLineChart.setTitle("Monitoraggio presenze per Lezione");
        statisticheLineChart.setCreateSymbols(true);
        statisticheLineChart.setAnimated(true);
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("Lezioni");
        try {
            for (PresenzeLezioni i : statisticheDAO.getPresenzeLezioni(corso.getCodCorso())) {
                series.getData().add(new XYChart.Data<>(i.getCodLezione(), i.getNumero_presenze()));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        statisticheLineChart.getData().add(series);
    }

        private void setStatisticheRiempimento() {
        statistichePieChart.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm());
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Posti rimanenti", 100-statistiche.getPercentualeRiempimento()),
                        new PieChart.Data("Posti occupati", statistiche.getPercentualeRiempimento()));

        statistichePieChart.setAnimated(true);
        statistichePieChart.setTitle("Percentuale di Riempimento");
            pieChartData.forEach(data ->
                    data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName(), " ", data.pieValueProperty(), "%"
                            )
                    )
            );
            statistichePieChart.getData().addAll(pieChartData);
    }
}
