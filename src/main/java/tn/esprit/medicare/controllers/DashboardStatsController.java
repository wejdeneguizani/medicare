package tn.esprit.medicare.controllers;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import tn.esprit.medicare.services.DashboardStatsService;

public class DashboardStatsController {

    private final Label totalPatientsValue;
    private final Label totalHabitsValue;
    private final Label totalMesuresValue;
    private final Label avgStepsValue;
    private final PieChart habitsByTypeChart;
    private final BarChart<String, Number> stepsByPatientChart;
    private final LineChart<String, Number> waterTrendChart;

    public DashboardStatsController(
            Label totalPatientsValue,
            Label totalHabitsValue,
            Label totalMesuresValue,
            Label avgStepsValue,
            PieChart habitsByTypeChart,
            BarChart<String, Number> stepsByPatientChart,
            LineChart<String, Number> waterTrendChart
    ) {
        this.totalPatientsValue = totalPatientsValue;
        this.totalHabitsValue = totalHabitsValue;
        this.totalMesuresValue = totalMesuresValue;
        this.avgStepsValue = avgStepsValue;
        this.habitsByTypeChart = habitsByTypeChart;
        this.stepsByPatientChart = stepsByPatientChart;
        this.waterTrendChart = waterTrendChart;
    }

    public void render(DashboardStatsService.StatsSnapshot snapshot) {
        totalPatientsValue.setText(String.valueOf(snapshot.totalPatients));
        totalHabitsValue.setText(String.valueOf(snapshot.totalHabits));
        totalMesuresValue.setText(String.valueOf(snapshot.totalMesures));
        avgStepsValue.setText(String.format("%.0f", snapshot.avgSteps));

        habitsByTypeChart.setData(FXCollections.observableArrayList(
                snapshot.habitsByType.entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                        .toList()
        ));

        XYChart.Series<String, Number> stepsSeries = new XYChart.Series<>();
        stepsSeries.setName("Steps");
        snapshot.stepsByPatient.forEach((userId, totalSteps) ->
                stepsSeries.getData().add(new XYChart.Data<>("P#" + userId, totalSteps))
        );
        stepsByPatientChart.getData().clear();
        stepsByPatientChart.getData().add(stepsSeries);

        XYChart.Series<String, Number> waterSeries = new XYChart.Series<>();
        waterSeries.setName("Avg Water");
        snapshot.waterTrend.forEach((date, liters) ->
                waterSeries.getData().add(new XYChart.Data<>(date, liters))
        );
        waterTrendChart.getData().clear();
        waterTrendChart.getData().add(waterSeries);
    }
}
