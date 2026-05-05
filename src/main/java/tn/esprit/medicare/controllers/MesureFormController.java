package tn.esprit.medicare.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tn.esprit.medicare.entities.Habitude;
import tn.esprit.medicare.entities.MesureSante;
import tn.esprit.medicare.services.HabitudeService;
import tn.esprit.medicare.services.MesureSanteService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MesureFormController {

    @FXML private ComboBox<Habitude> habitudeCombo;
    @FXML private TextField pasField;
    @FXML private TextField eauField;
    @FXML private TextField sysField;
    @FXML private TextField diaField;
    @FXML private TextField calField;
    @FXML private TextField weightField;
    @FXML private TextField sleepField;
    @FXML private Label habitudeErrorLabel;
    @FXML private Label pasErrorLabel;
    @FXML private Label eauErrorLabel;
    @FXML private Label sysErrorLabel;
    @FXML private Label diaErrorLabel;
    @FXML private Label calErrorLabel;
    @FXML private Label weightErrorLabel;
    @FXML private Label sleepErrorLabel;

    private MesureSante mesure;
    private int userId;
    private MesureSanteService mesureService = new MesureSanteService();
    private HabitudeService habitudeService = new HabitudeService();
    private boolean saved = false;

    public void setMesure(MesureSante mesure, int userId) {
        this.mesure = mesure;
        this.userId = userId;
        
        loadHabits();
        setupHabitCombo();

        if (mesure != null) {
            pasField.setText(String.valueOf(mesure.getPas()));
            eauField.setText(String.valueOf(mesure.getEauLitres()));
            sysField.setText(mesure.getTensionSystolique() != null ? String.valueOf(mesure.getTensionSystolique()) : "");
            diaField.setText(mesure.getTensionDiastolique() != null ? String.valueOf(mesure.getTensionDiastolique()) : "");
            calField.setText(mesure.getCalories() != null ? String.valueOf(mesure.getCalories()) : "");
            weightField.setText(mesure.getPoidsKg() != null ? String.valueOf(mesure.getPoidsKg()) : "");
            sleepField.setText(mesure.getSommeilHeures() != null ? String.valueOf(mesure.getSommeilHeures()) : "");
            
            for (Habitude h : habitudeCombo.getItems()) {
                if (h.getId() == mesure.getHabitudeId()) {
                    habitudeCombo.setValue(h);
                    break;
                }
            }
        }
    }

    private void loadHabits() {
        try {
            List<Habitude> habits = habitudeService.getByUserId(userId);
            habitudeCombo.getItems().setAll(habits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupHabitCombo() {
        habitudeCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Habitude item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getTitre());
            }
        });
        habitudeCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Habitude habit) {
                return habit == null ? "" : habit.getTitre();
            }
            @Override
            public Habitude fromString(String string) {
                return null;
            }
        });
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) return;

        if (mesure == null) {
            mesure = new MesureSante();
            mesure.setUserId(userId);
            mesure.setDateMesure(LocalDateTime.now());
        }

        mesure.setHabitudeId(habitudeCombo.getValue().getId());
        mesure.setPas(Integer.parseInt(pasField.getText()));
        mesure.setEauLitres(Double.parseDouble(eauField.getText()));
        
        mesure.setTensionSystolique(sysField.getText().isEmpty() ? null : Integer.parseInt(sysField.getText()));
        mesure.setTensionDiastolique(diaField.getText().isEmpty() ? null : Integer.parseInt(diaField.getText()));
        mesure.setCalories(calField.getText().isEmpty() ? null : Double.parseDouble(calField.getText()));
        mesure.setPoidsKg(weightField.getText().isEmpty() ? null : Double.parseDouble(weightField.getText()));
        mesure.setSommeilHeures(sleepField.getText().isEmpty() ? null : Double.parseDouble(sleepField.getText()));

        try {
            if (mesure.getId() == 0) {
                mesureService.add(mesure);
            } else {
                mesureService.update(mesure);
            }
            saved = true;
            closeStage();
        } catch (SQLException e) {
            e.printStackTrace();
            clearFieldErrors();
            setFieldError(habitudeErrorLabel, "Database error occurred: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        clearFieldErrors();
        boolean valid = true;

        if (habitudeCombo.getValue() == null) {
            setFieldError(habitudeErrorLabel, "Please select a related habit.");
            valid = false;
        }
        if (pasField.getText().trim().isEmpty()) {
            setFieldError(pasErrorLabel, "Steps are required.");
            valid = false;
        }
        if (eauField.getText().trim().isEmpty()) {
            setFieldError(eauErrorLabel, "Water intake is required.");
            valid = false;
        }

        if (!valid) {
            return false;
        }

        try {
            int steps = Integer.parseInt(pasField.getText().trim());
            if (steps < 0) {
                setFieldError(pasErrorLabel, "Steps must be 0 or greater.");
                valid = false;
            }
        } catch (NumberFormatException e) {
            setFieldError(pasErrorLabel, "Steps must be a valid integer.");
            valid = false;
        }

        try {
            double water = Double.parseDouble(eauField.getText().trim());
            if (water < 0) {
                setFieldError(eauErrorLabel, "Water must be 0 or greater.");
                valid = false;
            }
        } catch (NumberFormatException e) {
            setFieldError(eauErrorLabel, "Water must be a valid number.");
            valid = false;
        }

        valid &= validateOptionalInteger(sysField, sysErrorLabel, "Systolic");
        valid &= validateOptionalInteger(diaField, diaErrorLabel, "Diastolic");
        valid &= validateOptionalDouble(calField, calErrorLabel, "Calories");
        valid &= validateOptionalDouble(weightField, weightErrorLabel, "Weight");
        valid &= validateOptionalDouble(sleepField, sleepErrorLabel, "Sleep");

        return valid;
    }

    private boolean validateOptionalInteger(TextField field, Label errorLabel, String fieldName) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            return true;
        }
        try {
            int parsed = Integer.parseInt(value);
            if (parsed < 0) {
                setFieldError(errorLabel, fieldName + " must be 0 or greater.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            setFieldError(errorLabel, fieldName + " must be a valid integer.");
            return false;
        }
    }

    private boolean validateOptionalDouble(TextField field, Label errorLabel, String fieldName) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            return true;
        }
        try {
            double parsed = Double.parseDouble(value);
            if (parsed < 0) {
                setFieldError(errorLabel, fieldName + " must be 0 or greater.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            setFieldError(errorLabel, fieldName + " must be a valid number.");
            return false;
        }
    }

    private void setFieldError(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void clearFieldErrors() {
        hideFieldError(habitudeErrorLabel);
        hideFieldError(pasErrorLabel);
        hideFieldError(eauErrorLabel);
        hideFieldError(sysErrorLabel);
        hideFieldError(diaErrorLabel);
        hideFieldError(calErrorLabel);
        hideFieldError(weightErrorLabel);
        hideFieldError(sleepErrorLabel);
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
        ((Stage) pasField.getScene().getWindow()).close();
    }
}
