package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.Utilisateur;
import utils.DatabaseConnection;
import utils.SessionContext;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientHistoryController {

    @FXML
    private Label titleLabel;
    @FXML
    private VBox historiqueContainer;

    @FXML
    private void initialize() {
        Utilisateur utilisateur = SessionContext.getUtilisateurConnecte();
        if (utilisateur != null) {
            titleLabel.setText("Historique recent de " + utilisateur.getPrenom());
            chargerHistorique(utilisateur.getId());
            return;
        }
        titleLabel.setText("Historique recent");
        ajouterLigne("Aucun utilisateur connecte.");
    }

    @FXML
    private void retourDashboard(ActionEvent event) {
        changerVue(event, "/PatientDashboard.fxml");
    }

    private void chargerHistorique(int utilisateurId) {
        String sql = """
                SELECT action, details, created_at
                FROM journal_activite
                WHERE utilisateur_id = ?
                ORDER BY created_at DESC
                LIMIT 8
                """;
        historiqueContainer.getChildren().clear();
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean hasRows = false;
                while (rs.next()) {
                    hasRows = true;
                    String action = rs.getString("action");
                    String details = rs.getString("details");
                    String createdAt = rs.getString("created_at");
                    ajouterLigne(createdAt + "  |  " + action + " - " + details);
                }
                if (!hasRows) {
                    ajouterLigne("Aucune activite recente pour le moment.");
                    ajouterLigne("Exemple: vos prochains rendez-vous et prises de medicaments apparaitront ici.");
                }
            }
        } catch (SQLException e) {
            ajouterLigne("Impossible de charger l'historique depuis la base.");
            ajouterLigne("Detail: " + e.getMessage());
        }
    }

    private void ajouterLigne(String texte) {
        Label label = new Label(texte);
        label.setWrapText(true);
        label.getStyleClass().add("history-item");
        historiqueContainer.getChildren().add(label);
    }

    private void changerVue(ActionEvent event, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
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
