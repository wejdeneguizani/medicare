package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import models.Utilisateur;

import java.io.IOException;

public class DetailsUtilisateurController {

    @FXML
    private Label matriculeLabel;
    @FXML
    private Label nomCompletLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label sexeLabel;
    @FXML
    private Label dateNaissanceLabel;
    @FXML
    private Label telephoneLabel;
    @FXML
    private Label adresseLabel;
    @FXML
    private Label statutLabel;
    @FXML
    private Label deuxFacteursLabel;
    @FXML
    private Label derniereConnexionLabel;

    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        afficherUtilisateur();
    }

    @FXML
    private void modifier(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FormUtilisateur.fxml"));
            Parent root = loader.load();
            FormUtilisateurController controller = loader.getController();
            controller.setUtilisateur(utilisateur);
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void retourListe(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GestionUtilisateurs.fxml"));
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    private void afficherUtilisateur() {
        if (utilisateur == null) {
            return;
        }
        matriculeLabel.setText(value(utilisateur.getMatricule()));
        nomCompletLabel.setText(value(utilisateur.getNomComplet()));
        emailLabel.setText(value(utilisateur.getEmail()));
        roleLabel.setText(value(utilisateur.getRole()));
        sexeLabel.setText(value(utilisateur.getSexe()));
        dateNaissanceLabel.setText(utilisateur.getDateNaissance() == null ? "-" : utilisateur.getDateNaissance().toString());
        telephoneLabel.setText(value(utilisateur.getTelephone()));
        adresseLabel.setText(value(utilisateur.getAdresse()));
        statutLabel.setText(value(utilisateur.getStatut()));
        deuxFacteursLabel.setText(utilisateur.isDeuxFacteurs() ? "Activee" : "Desactivee");
        derniereConnexionLabel.setText(utilisateur.getDerniereConnexion() == null ? "-" : utilisateur.getDerniereConnexion().toString().replace("T", " "));
    }

    private String value(String text) {
        return text == null || text.isBlank() ? "-" : text;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("Erreur");
        alert.showAndWait();
    }
}
