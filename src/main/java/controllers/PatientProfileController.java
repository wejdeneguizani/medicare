package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Utilisateur;
import services.UtilisateurService;
import utils.SessionContext;

import java.io.IOException;
import java.sql.SQLException;

public class PatientProfileController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField adresseField;
    @FXML
    private DatePicker dateNaissancePicker;
    @FXML
    private ComboBox<String> sexeCombo;
    @FXML
    private PasswordField nouveauMotDePasseField;
    @FXML
    private PasswordField confirmationField;

    private final UtilisateurService service = new UtilisateurService();
    private Utilisateur utilisateur;

    @FXML
    private void initialize() {
        sexeCombo.getItems().setAll("M", "F", "Autre");
        utilisateur = SessionContext.getUtilisateurConnecte();

        if (utilisateur != null) {
            remplirFormulaire();
        }
    }

    @FXML
    private void enregistrer(ActionEvent event) {
        if (utilisateur == null) {
            showError("Aucun utilisateur connecte.");
            return;
        }
        if (!valider()) {
            return;
        }

        utilisateur.setNom(nomField.getText().trim());
        utilisateur.setPrenom(prenomField.getText().trim());
        utilisateur.setEmail(emailField.getText().trim());
        utilisateur.setTelephone(telephoneField.getText().trim());
        utilisateur.setAdresse(adresseField.getText().trim());
        utilisateur.setDateNaissance(dateNaissancePicker.getValue());
        utilisateur.setSexe(sexeCombo.getValue());

        try {
            service.modifier(utilisateur);
            if (!nouveauMotDePasseField.getText().isBlank()) {
                service.changerMotDePasse(utilisateur.getId(), nouveauMotDePasseField.getText().trim());
            }
            SessionContext.setUtilisateurConnecte(utilisateur);
            showInfo("Profil mis a jour avec succes.");
            changerVue(event, "/PatientDashboard.fxml");
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void retour(ActionEvent event) {
        changerVue(event, "/PatientDashboard.fxml");
    }

    private void remplirFormulaire() {
        nomField.setText(valeur(utilisateur.getNom()));
        prenomField.setText(valeur(utilisateur.getPrenom()));
        emailField.setText(valeur(utilisateur.getEmail()));
        telephoneField.setText(valeur(utilisateur.getTelephone()));
        adresseField.setText(valeur(utilisateur.getAdresse()));
        dateNaissancePicker.setValue(utilisateur.getDateNaissance());
        sexeCombo.setValue(utilisateur.getSexe());
    }

    private boolean valider() {
        StringBuilder message = new StringBuilder();
        if (nomField.getText().isBlank()) {
            message.append("- Le nom est obligatoire.\n");
        }
        if (prenomField.getText().isBlank()) {
            message.append("- Le prenom est obligatoire.\n");
        }
        if (emailField.getText().isBlank()) {
            message.append("- L'email est obligatoire.\n");
        }
        if (telephoneField.getText().isBlank()) {
            message.append("- Le telephone est obligatoire.\n");
        }
        if (adresseField.getText().isBlank()) {
            message.append("- L'adresse est obligatoire.\n");
        }
        if (sexeCombo.getValue() == null) {
            message.append("- Le sexe est obligatoire.\n");
        }
        if (!nouveauMotDePasseField.getText().isBlank()
                && !nouveauMotDePasseField.getText().trim().equals(confirmationField.getText().trim())) {
            message.append("- Les mots de passe ne correspondent pas.\n");
        }

        if (!message.isEmpty()) {
            showError(message.toString());
            return false;
        }
        return true;
    }

    private String valeur(String valeur) {
        return valeur == null ? "" : valeur;
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
