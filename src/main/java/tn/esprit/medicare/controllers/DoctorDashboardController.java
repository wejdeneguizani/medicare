package tn.esprit.medicare.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import tn.esprit.medicare.entities.Habitude;
import tn.esprit.medicare.entities.MesureSante;
import tn.esprit.medicare.services.HabitudeService;
import tn.esprit.medicare.services.MesureSanteService;

import java.sql.SQLException;
import java.util.List;

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

    private HabitudeService habitudeService = new HabitudeService();
    private MesureSanteService mesureService = new MesureSanteService();

    @Override
    protected void initializeData() {
        loadAllData();
    }

    private void loadAllData() {
        try {
            List<Habitude> habitudes = habitudeService.getAll();
            habitudesContainer.getChildren().clear();
            for (Habitude h : habitudes) {
                habitudesContainer.getChildren().add(createHabitCard(h));
            }

            List<MesureSante> mesures = mesureService.getAll();
            mesuresContainer.getChildren().clear();
            for (MesureSante m : mesures) {
                mesuresContainer.getChildren().add(createMesureCard(m));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        card.setPrefSize(260, 180);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(219,112,147,0.1), 10, 0, 0, 4); -fx-border-color: #FFF0F5; -fx-border-radius: 15;");

        Label userLabel = new Label("PATIENT #" + h.getUserId());
        userLabel.setStyle("-fx-text-fill: #DB7093; -fx-font-size: 10px; -fx-font-weight: bold;");

        Label titleLabel = new Label(h.getTitre());
        titleLabel.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #4A4A4A;");
        titleLabel.setWrapText(true);

        Label goalLabel = new Label("Goal: " + h.getObjectifValeur() + " " + h.getUnite());
        goalLabel.setStyle("-fx-text-fill: #FF69B4; -fx-font-size: 13px; -fx-font-weight: bold;");

        card.getChildren().addAll(userLabel, titleLabel, goalLabel);
        return card;
    }

    private VBox createMesureCard(MesureSante m) {
        VBox card = new VBox(10);
        card.setPrefSize(260, 250);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(three-pass-box, rgba(219,112,147,0.1), 10, 0, 0, 4); -fx-border-color: #FFF0F5; -fx-border-radius: 15;");

        Label userLabel = new Label("PATIENT #" + m.getUserId());
        userLabel.setStyle("-fx-text-fill: #DB7093; -fx-font-size: 10px; -fx-font-weight: bold;");

        Label dateLabel = new Label(m.getDateMesure().toString());
        dateLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 11px;");

        VBox stats = new VBox(5);
        stats.getChildren().addAll(
            new Label("👣 Steps: " + m.getPas()),
            new Label("💧 Water: " + m.getEauLitres() + "L"),
            new Label("❤️ BP: " + (m.getTensionSystolique() != null ? m.getTensionSystolique() + "/" + m.getTensionDiastolique() : "N/A")),
            new Label("🔥 Calories: " + (m.getCalories() != null ? m.getCalories() : "0")),
            new Label("⚖️ Weight: " + (m.getPoidsKg() != null ? m.getPoidsKg() : "N/A") + " Kg"),
            new Label("😴 Sleep: " + (m.getSommeilHeures() != null ? m.getSommeilHeures() : "N/A") + " hrs")
        );
        stats.setStyle("-fx-font-size: 13px; -fx-text-fill: #4A4A4A;");

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
