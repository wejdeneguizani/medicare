package tn.esprit.medicare.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectionController {

    @FXML
    private VBox doctorCard;

    @FXML
    private VBox patientCard;

    @FXML
    private void handleDoctorSelection() {
        // User ID 1 is Doc
        loadDashboard("/views/DoctorDashboard.fxml", 1);
    }

    @FXML
    private void handlePatientSelection() {
        // User ID 2 is Patient
        loadDashboard("/views/PatientDashboard.fxml", 2);
    }

    private void loadDashboard(String fxmlPath, int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Pass the hardcoded user ID to the controller
            Object controller = loader.getController();
            if (controller instanceof BaseDashboardController) {
                ((BaseDashboardController) controller).setUserId(userId);
            }

            Stage stage = (Stage) doctorCard.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(userId == 1 ? "Doctor Dashboard" : "Patient Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
