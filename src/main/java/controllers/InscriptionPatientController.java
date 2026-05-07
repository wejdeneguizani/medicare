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
import utils.PasswordUtils;

import java.io.IOException;
import java.sql.SQLException;

public class InscriptionPatientController {

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
    private TextField villeField;
    @FXML
    private DatePicker dateNaissancePicker;
    @FXML
    private ComboBox<String> sexeCombo;
    @FXML
    private PasswordField motDePasseField;
    @FXML
    private PasswordField confirmationField;

    private final UtilisateurService service = new UtilisateurService();

    @FXML
    private void initialize() {
        sexeCombo.getItems().setAll("M", "F", "Autre");
    }

    @FXML
    private void creerCompte(ActionEvent event) {
        if (!valider()) {
            return;
        }

        Utilisateur patient = new Utilisateur();
        patient.setNom(nomField.getText().trim());
        patient.setPrenom(prenomField.getText().trim());
        patient.setEmail(emailField.getText().trim());
        patient.setTelephone(telephoneField.getText().trim());
        patient.setAdresse(adresseComplete());
        patient.setDateNaissance(dateNaissancePicker.getValue());
        patient.setSexe(sexeCombo.getValue());
        patient.setRole("Patient");
        patient.setStatut("Actif");
        patient.setMotDePasse(motDePasseField.getText().trim());
        patient.setMatricule(PasswordUtils.genererMatricule("Patient"));

        try {
            service.ajouter(patient);
            showInfo("Compte patient cree avec succes.\nMatricule : " + patient.getMatricule());
            changerVue(event, "/Home.fxml");
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void retourChoix(ActionEvent event) {
        changerVue(event, "/ChoixCompte.fxml");
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
        if (villeField.getText().isBlank()) {
            message.append("- La ville est obligatoire.\n");
        }
        if (sexeCombo.getValue() == null) {
            message.append("- Le sexe est obligatoire.\n");
        }
        if (motDePasseField.getText().isBlank()) {
            message.append("- Le mot de passe est obligatoire.\n");
        }
        if (!motDePasseField.getText().trim().equals(confirmationField.getText().trim())) {
            message.append("- Les mots de passe ne correspondent pas.\n");
        }

        if (!message.isEmpty()) {
            showError(message.toString());
            return false;
        }
        return true;
    }

    private String adresseComplete() {
        return adresseField.getText().trim() + ", " + villeField.getText().trim();
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
