package tn.esprit.medicare.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import tn.esprit.medicare.entities.Habitude;
import tn.esprit.medicare.entities.MesureSante;
import tn.esprit.medicare.services.DashboardStatsService;
import tn.esprit.medicare.services.HabitudeService;
import tn.esprit.medicare.services.MesureSanteService;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class DoctorDashboardController extends BaseDashboardController {

    @FXML private FlowPane habitudesContainer;
    @FXML private FlowPane mesuresContainer;
    @FXML private VBox habitsSection;
    @FXML private VBox mesuresSection;
    @FXML private VBox overviewSection;
    @FXML private Button btnOverview;
    @FXML private Button btnHabitudes;
    @FXML private Button btnMesures;
    @FXML private Label contentTitle;
    @FXML private Label contentSubtitle;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> patientFilterCombo;
    @FXML private ComboBox<String> typeFilterCombo;
    @FXML private ComboBox<String> statusFilterCombo;

    @FXML private Label totalPatientsValue;
    @FXML private Label totalHabitsValue;
    @FXML private Label totalMesuresValue;
    @FXML private Label avgStepsValue;
    @FXML private PieChart habitsByTypeChart;
    @FXML private BarChart<String, Number> stepsByPatientChart;
    @FXML private LineChart<String, Number> waterTrendChart;

    private final HabitudeService habitudeService = new HabitudeService();
    private final MesureSanteService mesureService = new MesureSanteService();
    private final DashboardStatsService statsService = new DashboardStatsService();

    private DashboardStatsController statsController;
    private List<Habitude> allHabitudes = new ArrayList<>();
    private List<MesureSante> allMesures = new ArrayList<>();

    @FXML
    public void initialize() {
        patientFilterCombo.getItems().add("All Patients");
        typeFilterCombo.getItems().add("All");
        statusFilterCombo.getItems().addAll("All", "Active", "Inactive");
        patientFilterCombo.setValue("All Patients");
        typeFilterCombo.setValue("All");
        statusFilterCombo.setValue("All");

        statsController = new DashboardStatsController(
                totalPatientsValue,
                totalHabitsValue,
                totalMesuresValue,
                avgStepsValue,
                habitsByTypeChart,
                stepsByPatientChart,
                waterTrendChart
        );
    }

    @Override
    protected void initializeData() {
        loadAllData();
    }

    private void loadAllData() {
        try {
            allHabitudes = habitudeService.getAll();
            allMesures = mesureService.getAll();
            refreshFilterOptions();
            applyFilters();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshFilterOptions() {
        Set<String> patientIds = new TreeSet<>();
        for (Habitude habitude : allHabitudes) {
            patientIds.add("Patient " + habitude.getUserId());
        }
        for (MesureSante mesure : allMesures) {
            patientIds.add("Patient " + mesure.getUserId());
        }

        String selectedPatient = patientFilterCombo.getValue();
        patientFilterCombo.getItems().setAll("All Patients");
        patientFilterCombo.getItems().addAll(patientIds);
        patientFilterCombo.setValue(selectedPatient != null ? selectedPatient : "All Patients");

        String selectedType = typeFilterCombo.getValue();
        List<String> types = allHabitudes.stream()
                .map(h -> h.getType().name())
                .distinct()
                .sorted()
                .toList();
        typeFilterCombo.getItems().setAll("All");
        typeFilterCombo.getItems().addAll(types);
        typeFilterCombo.setValue(selectedType != null ? selectedType : "All");
    }

    @FXML
    private void handleSearchChanged() {
        applyFilters();
    }

    @FXML
    private void handleFilterChanged() {
        applyFilters();
    }

    @FXML
    private void handleResetFilters() {
        searchField.clear();
        patientFilterCombo.setValue("All Patients");
        typeFilterCombo.setValue("All");
        statusFilterCombo.setValue("All");
        applyFilters();
    }

    @FXML
    private void handleRefreshStats() {
        applyFilters();
    }

    private void applyFilters() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        int selectedPatientId = parsePatientId(patientFilterCombo.getValue());
        String selectedType = typeFilterCombo.getValue();
        String selectedStatus = statusFilterCombo.getValue();

        List<Habitude> filteredHabits = allHabitudes.stream()
                .filter(h -> selectedPatientId == -1 || h.getUserId() == selectedPatientId)
                .filter(h -> "All".equals(selectedType) || h.getType().name().equalsIgnoreCase(selectedType))
                .filter(h -> "All".equals(selectedStatus)
                        || ("Active".equals(selectedStatus) && h.isActive())
                        || ("Inactive".equals(selectedStatus) && !h.isActive()))
                .filter(h -> query.isBlank() || matchesHabitQuery(h, query))
                .collect(Collectors.toList());

        List<MesureSante> filteredMesures = allMesures.stream()
                .filter(m -> selectedPatientId == -1 || m.getUserId() == selectedPatientId)
                .filter(m -> query.isBlank() || matchesMesureQuery(m, query))
                .sorted(Comparator.comparing(MesureSante::getDateMesure).reversed())
                .collect(Collectors.toList());

        renderHabitCards(filteredHabits);
        renderMesureCards(filteredMesures);
        renderStats(filteredHabits, filteredMesures);
    }

    private boolean matchesHabitQuery(Habitude habitude, String query) {
        return String.valueOf(habitude.getUserId()).contains(query)
                || habitude.getTitre().toLowerCase(Locale.ROOT).contains(query)
                || habitude.getType().name().toLowerCase(Locale.ROOT).contains(query)
                || habitude.getUnite().toLowerCase(Locale.ROOT).contains(query);
    }

    private boolean matchesMesureQuery(MesureSante mesure, String query) {
        return String.valueOf(mesure.getUserId()).contains(query)
                || String.valueOf(mesure.getPas()).contains(query)
                || String.valueOf(mesure.getEauLitres()).contains(query)
                || mesure.getDateMesure().toLocalDate().toString().contains(query);
    }

    private int parsePatientId(String value) {
        if (value == null || value.equals("All Patients")) {
            return -1;
        }
        if (!value.startsWith("Patient ")) {
            return -1;
        }
        try {
            return Integer.parseInt(value.substring("Patient ".length()));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void renderHabitCards(List<Habitude> habitudes) {
        habitudesContainer.getChildren().clear();
        for (Habitude h : habitudes) {
            habitudesContainer.getChildren().add(createHabitCard(h));
        }
    }

    private void renderMesureCards(List<MesureSante> mesures) {
        mesuresContainer.getChildren().clear();
        for (MesureSante m : mesures) {
            mesuresContainer.getChildren().add(createMesureCard(m));
        }
    }

    private void renderStats(List<Habitude> habitudes, List<MesureSante> mesures) {
        DashboardStatsService.StatsSnapshot snapshot = statsService.buildSnapshot(habitudes, mesures);
        statsController.render(snapshot);
    }

    @FXML
    private void handleOverview() {
        overviewSection.setVisible(true);
        habitsSection.setVisible(false);
        mesuresSection.setVisible(false);
        contentTitle.setText("Global Health Monitoring");
        contentSubtitle.setText("Manage and monitor all patient activities effortlessly");
        resetButtonStyles();
        btnOverview.setStyle("-fx-background-color: #FFC0CB; -fx-text-fill: white; -fx-alignment: CENTER_LEFT; -fx-background-radius: 12; -fx-padding: 15; -fx-font-weight: bold;");
    }

    @FXML
    private void showHabits() {
        overviewSection.setVisible(false);
        habitsSection.setVisible(true);
        mesuresSection.setVisible(false);
        contentTitle.setText("Patient Habits");
        contentSubtitle.setText("Detailed look at all patient habits and routines");
        resetButtonStyles();
        btnHabitudes.setStyle("-fx-background-color: #FFC0CB; -fx-text-fill: white; -fx-alignment: CENTER_LEFT; -fx-background-radius: 12; -fx-padding: 15; -fx-font-weight: bold;");
    }

    @FXML
    private void showMesures() {
        overviewSection.setVisible(false);
        habitsSection.setVisible(false);
        mesuresSection.setVisible(true);
        contentTitle.setText("Measurements Logs");
        contentSubtitle.setText("Latest health metrics recorded by all patients");
        resetButtonStyles();
        btnMesures.setStyle("-fx-background-color: #FFC0CB; -fx-text-fill: white; -fx-alignment: CENTER_LEFT; -fx-background-radius: 12; -fx-padding: 15; -fx-font-weight: bold;");
    }

    private void resetButtonStyles() {
        String idleStyle = "-fx-background-color: transparent; -fx-text-fill: #8B4513; -fx-alignment: CENTER_LEFT; -fx-padding: 15; -fx-font-weight: bold;";
        btnOverview.setStyle(idleStyle);
        btnHabitudes.setStyle(idleStyle);
        btnMesures.setStyle(idleStyle);
    }

    private VBox createHabitCard(Habitude h) {
        VBox card = new VBox(12);
        card.setPrefSize(260, 190);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(219,112,147,0.1), 10, 0, 0, 4); -fx-border-color: #FFF0F5; -fx-border-radius: 15;");

        Label userLabel = new Label("PATIENT #" + h.getUserId());
        userLabel.setStyle("-fx-text-fill: #DB7093; -fx-font-size: 10px; -fx-font-weight: bold;");

        Label titleLabel = new Label(h.getTitre());
        titleLabel.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #4A4A4A;");
        titleLabel.setWrapText(true);

        Label typeLabel = new Label("Type: " + h.getType().name());
        typeLabel.setStyle("-fx-text-fill: #BC8F8F; -fx-font-size: 12px;");

        Label goalLabel = new Label("Goal: " + h.getObjectifValeur() + " " + h.getUnite());
        goalLabel.setStyle("-fx-text-fill: #FF69B4; -fx-font-size: 13px; -fx-font-weight: bold;");

        Label statusLabel = new Label(h.isActive() ? "Status: Active" : "Status: Inactive");
        statusLabel.setStyle("-fx-text-fill: #8B4513; -fx-font-size: 12px;");

        card.getChildren().addAll(userLabel, titleLabel, typeLabel, goalLabel, statusLabel);
        return card;
    }

    private VBox createMesureCard(MesureSante m) {
        VBox card = new VBox(10);
        card.setPrefSize(260, 260);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(219,112,147,0.1), 10, 0, 0, 4); -fx-border-color: #FFF0F5; -fx-border-radius: 15;");

        Label userLabel = new Label("PATIENT #" + m.getUserId());
        userLabel.setStyle("-fx-text-fill: #DB7093; -fx-font-size: 10px; -fx-font-weight: bold;");

        Label dateLabel = new Label(m.getDateMesure().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        dateLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 11px;");

        VBox stats = new VBox(5);
        Label stepsLabel = new Label("👣 Steps: " + m.getPas());
        Label waterLabel = new Label("💧 Water: " + m.getEauLitres() + "L");
        Label bpLabel = new Label("❤️ BP: " + (m.getTensionSystolique() != null ? m.getTensionSystolique() + "/" + m.getTensionDiastolique() : "N/A"));
        Label caloriesLabel = new Label("🔥 Calories: " + (m.getCalories() != null ? m.getCalories() : "N/A"));
        Label weightLabel = new Label("⚖️ Weight: " + (m.getPoidsKg() != null ? m.getPoidsKg() : "N/A") + " Kg");
        Label sleepLabel = new Label("😴 Sleep: " + (m.getSommeilHeures() != null ? m.getSommeilHeures() : "N/A") + " hrs");

        String metricStyle = "-fx-font-size: 13px; -fx-text-fill: #4A4A4A;";
        stepsLabel.setStyle(metricStyle);
        waterLabel.setStyle(metricStyle);
        bpLabel.setStyle(metricStyle);
        caloriesLabel.setStyle(metricStyle);
        weightLabel.setStyle(metricStyle);
        sleepLabel.setStyle(metricStyle);

        stats.getChildren().addAll(stepsLabel, waterLabel, bpLabel, caloriesLabel, weightLabel, sleepLabel);

        card.getChildren().addAll(userLabel, dateLabel, stats);
        return card;
    }

    @FXML
    private void handleLogout() {
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) habitudesContainer.getScene().getWindow();
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/views/SelectionView.fxml"));
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
