package controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.GoogleUserProfile;
import models.Utilisateur;
import services.GoogleOAuthService;
import services.UtilisateurService;
import utils.SessionContext;

import java.io.IOException;
import java.security.GeneralSecurityException;
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

            apresConnexionReussie(utilisateur, event);
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void seConnecterAvecGoogle(ActionEvent event) {
        Node source = (Node) event.getSource();
        Task<GoogleUserProfile> task = new Task<>() {
            @Override
            protected GoogleUserProfile call() throws IOException, GeneralSecurityException {
                return new GoogleOAuthService().authorizeAndFetchProfile();
            }
        };
        task.setOnSucceeded(e -> {
            try {
                GoogleUserProfile profile = task.getValue();
                if (profile == null || profile.email() == null) {
                    showError("Connexion Google incomplete.");
                    return;
                }
                Utilisateur utilisateur = service.connecterParOAuth(profile.email());
                if (utilisateur == null) {
                    afficherEchecOAuthGoogle(profile.email());
                    return;
                }
                apresConnexionReussie(utilisateur, source);
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
        });
        task.setOnFailed(e -> {
            Throwable t = task.getException();
            showError(t != null ? t.getMessage() : "Echec de la connexion Google.");
        });
        new Thread(task, "google-oauth-login").start();
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

    private void afficherEchecOAuthGoogle(String emailGoogle) throws SQLException {
        Utilisateur utilisateur = service.getByEmailIgnoreCase(emailGoogle);
        if (utilisateur == null) {
            showError("Aucun compte ne correspond a ce compte Google. Creez un compte ou connectez-vous avec email et mot de passe.");
        } else if ("Bloque".equalsIgnoreCase(utilisateur.getStatut())
                || "Bloqué".equalsIgnoreCase(utilisateur.getStatut())) {
            showError("Ce compte est bloque. Contactez l'administrateur.");
        } else {
            showError("Connexion Google impossible pour le moment.");
        }
    }

    private void apresConnexionReussie(Utilisateur utilisateur, ActionEvent event) {
        apresConnexionReussie(utilisateur, (Node) event.getSource());
    }

    private void apresConnexionReussie(Utilisateur utilisateur, Node source) {
        SessionContext.setUtilisateurConnecte(utilisateur);
        if ("Admin".equalsIgnoreCase(utilisateur.getRole())
                || "Administrateur".equalsIgnoreCase(utilisateur.getRole())) {
            changerVueNoeud(source, "/Main.fxml");
        } else if ("Patient".equalsIgnoreCase(utilisateur.getRole())) {
            changerVueNoeud(source, "/PatientDashboard.fxml");
        } else if ("Medecin".equalsIgnoreCase(utilisateur.getRole())) {
            changerVueNoeud(source, "/MedecinDashboard.fxml");
        } else {
            showInfo("Bienvenue " + utilisateur.getNomComplet() + ".");
        }
    }

    private void changerVue(ActionEvent event, String fxml) {
        changerVueNoeud((Node) event.getSource(), fxml);
    }

    private void changerVueNoeud(Node source, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            source.getScene().setRoot(root);
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
