package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {

    @FXML
    private Label totalLabel;
    @FXML
    private Label activeLabel;
    @FXML
    private Label waitingLabel;
    @FXML
    private Label blockedLabel;

    private final UtilisateurService service = new UtilisateurService();

    @FXML
    private void initialize() {
        refreshStats();
    }

    @FXML
    private void ouvrirUtilisateurs(ActionEvent event) {
        changerVue(event, "/GestionUtilisateurs.fxml");
    }

    private void refreshStats() {
        try {
            totalLabel.setText(String.valueOf(service.countTotal()));
            activeLabel.setText(String.valueOf(service.countByStatut("Actif")));
            waitingLabel.setText(String.valueOf(service.countByStatut("En attente")));
            blockedLabel.setText(String.valueOf(service.countByStatut("Bloque")));
        } catch (SQLException e) {
            totalLabel.setText("-");
            activeLabel.setText("-");
            waitingLabel.setText("-");
            blockedLabel.setText("-");
        }
    }

    private void changerVue(ActionEvent event, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger " + fxml, e);
        }
    }
}
