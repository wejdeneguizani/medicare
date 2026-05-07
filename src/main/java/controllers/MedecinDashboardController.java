package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import models.Utilisateur;
import utils.SessionContext;

import java.io.IOException;

public class MedecinDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private void initialize() {
        Utilisateur utilisateur = SessionContext.getUtilisateurConnecte();
        if (utilisateur != null) {
            welcomeLabel.setText("Dr. " + utilisateur.getNomComplet());
        }
    }

    @FXML
    private void ouvrirPatients() {
        showInfo("Liste patients : ici le medecin pourra consulter ses patients.");
    }

    @FXML
    private void ouvrirRendezVous() {
        showInfo("Rendez-vous : ici le medecin pourra valider et organiser ses consultations.");
    }

    @FXML
    private void ouvrirSuivi() {
        showInfo("Suivi medical : ici le medecin pourra suivre les traitements et l'evolution des patients.");
    }

    @FXML
    private void ouvrirProfil(ActionEvent event) {
        changerVue(event, "/MedecinProfile.fxml");
    }

    @FXML
    private void ouvrirHistorique(ActionEvent event) {
        changerVue(event, "/MedecinHistory.fxml");
    }

    @FXML
    private void deconnexion(ActionEvent event) {
        SessionContext.clear();
        changerVue(event, "/Home.fxml");
    }

    private void changerVue(ActionEvent event, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("Erreur");
        alert.showAndWait();
    }
}
