package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Utilisateur;
import services.UtilisateurService;
import utils.SessionContext;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField motDePasseField;

    private final UtilisateurService service = new UtilisateurService();

    @FXML
    private void seConnecter(ActionEvent event) {
        if (!valider()) {
            return;
        }

        try {
            Utilisateur utilisateur = service.connecter(
                    emailField.getText().trim(),
                    motDePasseField.getText().trim()
            );

            if (utilisateur == null) {
                afficherErreurConnexion();
                return;
            }

            SessionContext.setUtilisateurConnecte(utilisateur);

            if ("Admin".equalsIgnoreCase(utilisateur.getRole())
                    || "Administrateur".equalsIgnoreCase(utilisateur.getRole())) {
                changerVue(event, "/Main.fxml");
            } else if ("Patient".equalsIgnoreCase(utilisateur.getRole())) {
                changerVue(event, "/PatientDashboard.fxml");
            } else if ("Medecin".equalsIgnoreCase(utilisateur.getRole())) {
                changerVue(event, "/MedecinDashboard.fxml");
            } else {
                showInfo("Bienvenue " + utilisateur.getNomComplet() + ".");
            }
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void creerCompte(ActionEvent event) {
        changerVue(event, "/ChoixCompte.fxml");
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        changerVue(event, "/Home.fxml");
    }

    private boolean valider() {
        StringBuilder message = new StringBuilder();
        if (emailField.getText().isBlank()) {
            message.append("- L'email est obligatoire.\n");
        }
        if (motDePasseField.getText().isBlank()) {
            message.append("- Le mot de passe est obligatoire.\n");
        }

        if (!message.isEmpty()) {
            showError(message.toString());
            return false;
        }
        return true;
    }

    private void afficherErreurConnexion() throws SQLException {
        Utilisateur utilisateur = service.getByEmail(emailField.getText().trim());
        if (utilisateur == null) {
            showError("Aucun compte ne correspond a cet email.");
        } else if ("Bloque".equalsIgnoreCase(utilisateur.getStatut())
                || "Bloqué".equalsIgnoreCase(utilisateur.getStatut())) {
            showError("Ce compte est bloque. Contactez l'administrateur.");
        } else {
            showError("Mot de passe incorrect. Utilisez le mot de passe que vous avez saisi a l'inscription, pas le hash affiche dans la base de donnees.");
        }
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
