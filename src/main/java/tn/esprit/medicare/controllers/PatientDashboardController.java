package tn.esprit.medicare.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.medicare.entities.Habitude;
import tn.esprit.medicare.entities.MesureSante;
import tn.esprit.medicare.services.HabitudeService;
import tn.esprit.medicare.services.MesureSanteService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PatientDashboardController extends BaseDashboardController {

    @FXML private FlowPane habitudesContainer;
    @FXML private FlowPane mesuresContainer;
    @FXML private VBox habitsSection;
    @FXML private VBox mesuresSection;
    @FXML private ToggleButton toggleHabits;
    @FXML private ToggleButton toggleMesures;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> typeFilterCombo;
    @FXML private ComboBox<String> measureSortCombo;

    private final HabitudeService habitudeService = new HabitudeService();
    private final MesureSanteService mesureService = new MesureSanteService();
    private List<Habitude> allHabitudes = new ArrayList<>();
    private List<MesureSante> allMesures = new ArrayList<>();

    @FXML
    public void initialize() {
        typeFilterCombo.getItems().setAll("All");
        measureSortCombo.getItems().setAll("Date Desc", "Date Asc", "Steps Desc", "Steps Asc");
        typeFilterCombo.setValue("All");
        measureSortCombo.setValue("Date Desc");
    }

    @Override
    protected void initializeData() {
        loadUserData();
    }

    private void loadUserData() {
        try {
            allHabitudes = habitudeService.getByUserId(userId);
            allMesures = mesureService.getByUserId(userId);
            refreshFilters();
            applyFilters();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshFilters() {
        String selected = typeFilterCombo.getValue();
        List<String> types = allHabitudes.stream()
                .map(h -> h.getType().name())
                .distinct()
                .sorted()
                .toList();
        typeFilterCombo.getItems().setAll("All");
        typeFilterCombo.getItems().addAll(types);
        typeFilterCombo.setValue(selected != null ? selected : "All");
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
        typeFilterCombo.setValue("All");
        measureSortCombo.setValue("Date Desc");
        applyFilters();
    }

    private void applyFilters() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        String selectedType = typeFilterCombo.getValue();
        String sortMode = measureSortCombo.getValue();

        List<Habitude> filteredHabits = allHabitudes.stream()
                .filter(h -> "All".equals(selectedType) || h.getType().name().equalsIgnoreCase(selectedType))
                .filter(h -> query.isBlank() || matchesHabit(h, query))
                .collect(Collectors.toList());

        List<MesureSante> filteredMesures = allMesures.stream()
                .filter(m -> query.isBlank() || matchesMesure(m, query))
                .collect(Collectors.toList());

        sortMesures(filteredMesures, sortMode);
        renderHabits(filteredHabits);
        renderMesures(filteredMesures);
    }

    private boolean matchesHabit(Habitude habitude, String query) {
        return habitude.getTitre().toLowerCase(Locale.ROOT).contains(query)
                || habitude.getType().name().toLowerCase(Locale.ROOT).contains(query)
                || habitude.getUnite().toLowerCase(Locale.ROOT).contains(query);
    }

    private boolean matchesMesure(MesureSante mesure, String query) {
        return String.valueOf(mesure.getPas()).contains(query)
                || String.valueOf(mesure.getEauLitres()).contains(query)
                || mesure.getDateMesure().toLocalDate().toString().contains(query);
    }

    private void sortMesures(List<MesureSante> mesures, String sortMode) {
        if ("Date Asc".equals(sortMode)) {
            mesures.sort(Comparator.comparing(MesureSante::getDateMesure));
        } else if ("Steps Desc".equals(sortMode)) {
            mesures.sort(Comparator.comparing(MesureSante::getPas).reversed());
        } else if ("Steps Asc".equals(sortMode)) {
            mesures.sort(Comparator.comparing(MesureSante::getPas));
        } else {
            mesures.sort(Comparator.comparing(MesureSante::getDateMesure).reversed());
        }
    }

    private void renderHabits(List<Habitude> habitudes) {
        habitudesContainer.getChildren().clear();
        for (Habitude h : habitudes) {
            habitudesContainer.getChildren().add(createHabitCard(h));
        }
    }

    private void renderMesures(List<MesureSante> mesures) {
        mesuresContainer.getChildren().clear();
        for (MesureSante m : mesures) {
            mesuresContainer.getChildren().add(createMesureCard(m));
        }
    }

    @FXML
    private void showHabits() {
        habitsSection.setVisible(true);
        mesuresSection.setVisible(false);
        updateToggleStyles();
    }

    @FXML
    private void showMesures() {
        habitsSection.setVisible(false);
        mesuresSection.setVisible(true);
        updateToggleStyles();
    }

    private void updateToggleStyles() {
        if (toggleHabits.isSelected()) {
            toggleHabits.setStyle("-fx-background-color: #FFC0CB; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 25; -fx-font-weight: bold;");
            toggleMesures.setStyle("-fx-background-color: #F0F0F0; -fx-text-fill: #888; -fx-background-radius: 20; -fx-padding: 10 25; -fx-font-weight: bold;");
        } else {
            toggleMesures.setStyle("-fx-background-color: #FFC0CB; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 25; -fx-font-weight: bold;");
            toggleHabits.setStyle("-fx-background-color: #F0F0F0; -fx-text-fill: #888; -fx-background-radius: 20; -fx-padding: 10 25; -fx-font-weight: bold;");
        }
    }

    private VBox createHabitCard(Habitude h) {
        VBox card = new VBox(15);
        card.setPrefSize(280, 220);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-padding: 25; -fx-effect: dropshadow(three-pass-box, rgba(219,112,147,0.1), 10, 0, 0, 4); -fx-border-color: #FFF0F5; -fx-border-radius: 20;");

        Label typeLabel = new Label(h.getType().toString());
        typeLabel.setStyle("-fx-background-color: #FFF0F5; -fx-text-fill: #DB7093; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 5 12; -fx-background-radius: 15;");

        Label titleLabel = new Label(h.getTitre());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #4A4A4A;");
        titleLabel.setWrapText(true);

        Label goalLabel = new Label("Goal: " + h.getObjectifValeur() + " " + h.getUnite());
        goalLabel.setStyle("-fx-text-fill: #DB7093; -fx-font-size: 14px;");

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        
        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        editBtn.setOnAction(e -> handleEditHabit(h));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #FFC0CB; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        deleteBtn.setOnAction(e -> handleDeleteHabit(h));

        actions.getChildren().addAll(editBtn, deleteBtn);
        card.getChildren().addAll(typeLabel, titleLabel, goalLabel, new VBox() {{ VBox.setVgrow(this, javafx.scene.layout.Priority.ALWAYS); }}, actions);
        
        return card;
    }

    private VBox createMesureCard(MesureSante m) {
        VBox card = new VBox(12);
        card.setPrefSize(280, 240);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-padding: 25; -fx-effect: dropshadow(three-pass-box, rgba(219,112,147,0.1), 10, 0, 0, 4); -fx-border-color: #FFF0F5; -fx-border-radius: 20;");

        Label dateLabel = new Label(m.getDateMesure().toLocalDate().toString());
        dateLabel.setStyle("-fx-text-fill: #BC8F8F; -fx-font-size: 12px;");

        VBox stats = new VBox(8);
        Label pasLabel = new Label("👣 " + m.getPas() + " Steps");
        pasLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4A4A4A;");
        
        Label eauLabel = new Label("💧 " + m.getEauLitres() + " Liters");
        eauLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #FF69B4;");

        String tension = (m.getTensionSystolique() != null) ? (m.getTensionSystolique() + "/" + m.getTensionDiastolique()) : "N/A";
        Label tensionLabel = new Label("❤️ BP: " + tension);
        tensionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #DB7093;");

        stats.getChildren().addAll(pasLabel, eauLabel, tensionLabel);

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        
        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        editBtn.setOnAction(e -> handleEditMesure(m));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #FFC0CB; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        deleteBtn.setOnAction(e -> handleDeleteMesure(m));

        actions.getChildren().addAll(editBtn, deleteBtn);
        card.getChildren().addAll(dateLabel, stats, new VBox() {{ VBox.setVgrow(this, javafx.scene.layout.Priority.ALWAYS); }}, actions);

        return card;
    }

    @FXML
    private void handleAddHabit() {
        openHabitForm(null);
    }

    private void handleEditHabit(Habitude habitude) {
        openHabitForm(habitude);
    }

    private void openHabitForm(Habitude habitude) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/views/HabitudeForm.fxml"));
            javafx.scene.Parent root = loader.load();
            HabitudeFormController controller = loader.getController();
            controller.setHabitude(habitude, userId);
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
            if (controller.isSaved()) loadUserData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleAddMesure() {
        openMesureForm(null);
    }

    private void handleEditMesure(MesureSante mesure) {
        openMesureForm(mesure);
    }

    private void openMesureForm(MesureSante mesure) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/views/MesureForm.fxml"));
            javafx.scene.Parent root = loader.load();
            MesureFormController controller = loader.getController();
            controller.setMesure(mesure, userId);
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
            if (controller.isSaved()) loadUserData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void handleDeleteHabit(Habitude habitude) {
        try {
            habitudeService.delete(habitude.getId());
            loadUserData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void handleDeleteMesure(MesureSante mesure) {
        try {
            mesureService.delete(mesure.getId());
            loadUserData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleLogout() {
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) habitudesContainer.getScene().getWindow();
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/views/SelectionView.fxml"));
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
