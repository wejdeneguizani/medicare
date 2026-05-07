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

public class PatientDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private void initialize() {
        Utilisateur utilisateur = SessionContext.getUtilisateurConnecte();
        if (utilisateur != null) {
            welcomeLabel.setText("Bonjour, " + utilisateur.getPrenom());
        }
    }

    @FXML
    private void ouvrirMedicaments() {
        showInfo("Espace medicaments : ici le patient pourra voir les heures de prise et les rappels.");
    }

    @FXML
    private void ouvrirProgression() {
        showInfo("Objectifs et coaching : ici le patient pourra suivre ses objectifs et recevoir des conseils personnalises.");
    }

    @FXML
    private void ouvrirRendezVous() {
        showInfo("Rendez-vous : ici le patient pourra choisir un medecin et demander un rendez-vous.");
    }

    @FXML
    private void ouvrirAssurances() {
        showInfo("Assurances : ici le patient pourra consulter ses informations d'assurance et ses prises en charge.");
    }

    @FXML
    private void ouvrirRoutines() {
        showInfo("Routines quotidiennes : ici le patient pourra organiser ses habitudes de sante chaque jour.");
    }

    @FXML
    private void ouvrirHistorique(ActionEvent event) {
        changerVue(event, "/PatientHistory.fxml");
    }

    @FXML
    private void ouvrirProfil(ActionEvent event) {
        changerVue(event, "/PatientProfile.fxml");
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
