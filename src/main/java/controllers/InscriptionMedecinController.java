package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Utilisateur;
import services.UtilisateurService;
import utils.PasswordUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

public class InscriptionMedecinController {

    private static final String NUMERO_ORDRE_PATTERN = "MED-[0-9]{6}";
    private static final Set<String> CODES_AUTORISATION = Set.of("123456", "MED2026", "CLINIC2026", "ADMIN-MED");

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField cabinetField;
    @FXML
    private TextField villeField;
    @FXML
    private ComboBox<String> specialiteCombo;
    @FXML
    private TextField numeroOrdreField;
    @FXML
    private TextField codeAutorisationField;
    @FXML
    private PasswordField motDePasseField;
    @FXML
    private PasswordField confirmationField;
    @FXML
    private CheckBox attestationCheck;

    private final UtilisateurService service = new UtilisateurService();

    @FXML
    private void initialize() {
        specialiteCombo.getItems().setAll(
                "Generaliste",
                "Cardiologue",
                "Dermatologue",
                "Pediatre",
                "Gynecologue",
                "Ophtalmologue",
                "Psychiatre",
                "Dentiste",
                "Autre"
        );
    }

    @FXML
    private void creerCompte(ActionEvent event) {
        if (!valider()) {
            return;
        }

        Utilisateur medecin = new Utilisateur();
        medecin.setNom(nomField.getText().trim());
        medecin.setPrenom(prenomField.getText().trim());
        medecin.setEmail(emailField.getText().trim());
        medecin.setTelephone(telephoneField.getText().trim());
        medecin.setAdresse(adresseProfessionnelle());
        medecin.setRole("Medecin");
        medecin.setStatut("Actif");
        medecin.setMotDePasse(motDePasseField.getText().trim());
        medecin.setMatricule(PasswordUtils.genererMatricule("Medecin"));

        try {
            service.ajouter(medecin);
            showInfo("Compte medecin cree avec succes.\nMatricule : " + medecin.getMatricule());
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
        if (cabinetField.getText().isBlank()) {
            message.append("- L'adresse du cabinet est obligatoire.\n");
        }
        if (villeField.getText().isBlank()) {
            message.append("- La ville est obligatoire.\n");
        }
        if (specialiteCombo.getValue() == null) {
            message.append("- La specialite est obligatoire.\n");
        }
        if (!numeroOrdreField.getText().trim().matches(NUMERO_ORDRE_PATTERN)) {
            message.append("- Le numero d'ordre doit respecter le format MED-123456.\n");
        }
        if (!CODES_AUTORISATION.contains(codeAutorisation())) {
            message.append("- Le code d'autorisation medecin est invalide.\n");
        }
        if (!attestationCheck.isSelected()) {
            message.append("- Vous devez attester que les informations professionnelles sont exactes.\n");
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

    private String adresseProfessionnelle() {
        return cabinetField.getText().trim()
                + ", " + villeField.getText().trim()
                + " | Specialite: " + specialiteCombo.getValue()
                + " | Numero ordre: " + numeroOrdreField.getText().trim();
    }

    private String codeAutorisation() {
        return codeAutorisationField.getText().trim().toUpperCase();
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
