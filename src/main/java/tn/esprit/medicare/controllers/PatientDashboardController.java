package tn.esprit.medicare.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.esprit.medicare.entities.Habitude;
import tn.esprit.medicare.entities.MesureSante;
import tn.esprit.medicare.services.HabitudeService;
import tn.esprit.medicare.services.MesureSanteService;

import java.sql.SQLException;
import java.util.List;

public class PatientDashboardController extends BaseDashboardController {

    @FXML private FlowPane habitudesContainer;
    @FXML private FlowPane mesuresContainer;
    @FXML private VBox habitsSection;
    @FXML private VBox mesuresSection;
    @FXML private ToggleButton toggleHabits;
    @FXML private ToggleButton toggleMesures;

    private HabitudeService habitudeService = new HabitudeService();
    private MesureSanteService mesureService = new MesureSanteService();

    @Override
    protected void initializeData() {
        loadUserData();
    }

    private void loadUserData() {
        try {
            List<Habitude> habitudes = habitudeService.getByUserId(userId);
            habitudesContainer.getChildren().clear();
            for (Habitude h : habitudes) {
                habitudesContainer.getChildren().add(createHabitCard(h));
            }

            List<MesureSante> mesures = mesureService.getByUserId(userId);
            mesuresContainer.getChildren().clear();
            for (MesureSante m : mesures) {
                mesuresContainer.getChildren().add(createMesureCard(m));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
