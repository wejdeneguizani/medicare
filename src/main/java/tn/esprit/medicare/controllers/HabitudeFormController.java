package tn.esprit.medicare.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.medicare.entities.Habitude;
import tn.esprit.medicare.services.HabitudeService;

import java.sql.SQLException;
import java.time.LocalDate;

public class HabitudeFormController {

    @FXML private TextField titleField;
    @FXML private ComboBox<Habitude.TypeHabitude> typeCombo;
    @FXML private TextField goalField;
    @FXML private ComboBox<String> unitCombo;
    @FXML private CheckBox activeCheck;
    @FXML private Label titleErrorLabel;
    @FXML private Label typeErrorLabel;
    @FXML private Label goalErrorLabel;
    @FXML private Label unitErrorLabel;

    private Habitude habitude;
    private int userId;
    private HabitudeService habitudeService = new HabitudeService();
    private boolean saved = false;

    @FXML
    public void initialize() {
        typeCombo.getItems().setAll(Habitude.TypeHabitude.values());
        unitCombo.getItems().setAll("min", "km", "Liters", "Steps", "kcal", "hours", "glasses");
    }

    public void setHabitude(Habitude habitude, int userId) {
        this.habitude = habitude;
        this.userId = userId;
        if (habitude != null) {
            titleField.setText(habitude.getTitre());
            typeCombo.setValue(habitude.getType());
            goalField.setText(String.valueOf(habitude.getObjectifValeur()));
            unitCombo.setValue(habitude.getUnite());
            activeCheck.setSelected(habitude.isActive());
        }
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) return;

        if (habitude == null) {
            habitude = new Habitude();
            habitude.setUserId(userId);
            habitude.setDateDebut(LocalDate.now());
        }

        habitude.setTitre(titleField.getText());
        habitude.setType(typeCombo.getValue());
        habitude.setObjectifValeur(Double.parseDouble(goalField.getText()));
        habitude.setUnite(unitCombo.getValue());
        habitude.setActive(activeCheck.isSelected());

        try {
            if (habitude.getId() == 0) {
                habitudeService.add(habitude);
            } else {
                habitudeService.update(habitude);
            }
            saved = true;
            closeStage();
        } catch (SQLException e) {
            e.printStackTrace();
            clearFieldErrors();
            setFieldError(titleErrorLabel, "Database error occurred.");
        }
    }

    private boolean validateInput() {
        clearFieldErrors();
        boolean valid = true;

        if (titleField.getText().trim().isEmpty()) {
            setFieldError(titleErrorLabel, "Title is required.");
            valid = false;
        }
        if (typeCombo.getValue() == null) {
            setFieldError(typeErrorLabel, "Category is required.");
            valid = false;
        }
        if (goalField.getText().trim().isEmpty()) {
            setFieldError(goalErrorLabel, "Daily goal is required.");
            valid = false;
        }
        if (unitCombo.getValue() == null) {
            setFieldError(unitErrorLabel, "Unit is required.");
            valid = false;
        }

        if (!valid) {
            return false;
        }

        try {
            double goal = Double.parseDouble(goalField.getText().trim());
            if (goal <= 0) {
                setFieldError(goalErrorLabel, "Goal must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            setFieldError(goalErrorLabel, "Goal must be a valid number.");
            return false;
        }
        return true;
    }

    private void setFieldError(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void clearFieldErrors() {
        hideFieldError(titleErrorLabel);
        hideFieldError(typeErrorLabel);
        hideFieldError(goalErrorLabel);
        hideFieldError(unitErrorLabel);
    }

    private void hideFieldError(Label errorLabel) {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) titleField.getScene().getWindow()).close();
    }
}
