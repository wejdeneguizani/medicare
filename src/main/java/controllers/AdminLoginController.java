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
import services.GoogleOAuthService;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AdminLoginController {

    private static final String ADMIN_EMAIL = "hayder@gmail.com";
    private static final String ADMIN_PASSWORD = "123456";

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField motDePasseField;

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
            GoogleUserProfile profile = task.getValue();
            if (profile == null || profile.email() == null) {
                showError("Connexion Google incomplete.");
                return;
            }
            if (!ADMIN_EMAIL.equalsIgnoreCase(profile.email())) {
                showError("Ce compte Google n'est pas autorise comme administrateur.");
                return;
            }
            changerVueNoeud(source, "/Main.fxml");
        });
        task.setOnFailed(e -> {
            Throwable t = task.getException();
            showError(t != null ? t.getMessage() : "Echec de la connexion Google.");
        });
        new Thread(task, "google-oauth-admin").start();
    }

    @FXML
    private void seConnecterAdmin(ActionEvent event) {
        String email = emailField.getText().trim();
        String motDePasse = motDePasseField.getText().trim();

        if (email.isBlank() || motDePasse.isBlank()) {
            showError("Veuillez saisir l'email et le mot de passe admin.");
            return;
        }

        if (!ADMIN_EMAIL.equalsIgnoreCase(email) || !ADMIN_PASSWORD.equals(motDePasse)) {
            showError("Identifiants admin invalides.");
            return;
        }

        changerVue(event, "/Main.fxml");
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        changerVue(event, "/Home.fxml");
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

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("Erreur");
        alert.showAndWait();
    }
}
