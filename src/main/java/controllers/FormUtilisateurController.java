package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Utilisateur;
import services.UtilisateurService;
import utils.PasswordUtils;

import java.io.IOException;
import java.sql.SQLException;

public class FormUtilisateurController {

    @FXML
    private Label titleLabel;
    @FXML
    private TextField matriculeField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField motDePasseField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField adresseField;
    @FXML
    private ComboBox<String> roleCombo;
    @FXML
    private ComboBox<String> sexeCombo;
    @FXML
    private ComboBox<String> statutCombo;
    @FXML
    private DatePicker dateNaissancePicker;
    @FXML
    private CheckBox deuxFacteursCheck;

    private final UtilisateurService service = new UtilisateurService();
    private Utilisateur utilisateur;

    @FXML
    private void initialize() {
        roleCombo.getItems().setAll("Administrateur", "Medecin", "Patient");
        sexeCombo.getItems().setAll("M", "F", "Autre");
        statutCombo.getItems().setAll("Actif", "Inactif", "En attente", "Bloque");
        statutCombo.setValue("En attente");
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        if (utilisateur == null) {
            titleLabel.setText("Nouvel utilisateur");
            matriculeField.setText("Auto");
            return;
        }

        titleLabel.setText("Modifier l'utilisateur");
        matriculeField.setText(utilisateur.getMatricule());
        nomField.setText(utilisateur.getNom());
        prenomField.setText(utilisateur.getPrenom());
        emailField.setText(utilisateur.getEmail());
        telephoneField.setText(utilisateur.getTelephone());
        adresseField.setText(utilisateur.getAdresse());
        roleCombo.setValue(utilisateur.getRole());
        sexeCombo.setValue(utilisateur.getSexe());
        statutCombo.setValue(utilisateur.getStatut());
        dateNaissancePicker.setValue(utilisateur.getDateNaissance());
        deuxFacteursCheck.setSelected(utilisateur.isDeuxFacteurs());
        motDePasseField.setPromptText("Laisser vide pour ne pas changer");
    }

    @FXML
    private void sauvegarder(ActionEvent event) {
        if (!valider()) {
            return;
        }

        try {
            if (utilisateur == null) {
                Utilisateur nouveau = new Utilisateur();
                remplirUtilisateur(nouveau);
                nouveau.setMotDePasse(motDePasseField.getText());
                nouveau.setMatricule(PasswordUtils.genererMatricule(roleCombo.getValue()));
                service.ajouter(nouveau);
                showInfo("Utilisateur cree avec succes.\nMatricule : " + nouveau.getMatricule());
            } else {
                remplirUtilisateur(utilisateur);
                service.modifier(utilisateur);
                if (!motDePasseField.getText().isBlank()) {
                    service.changerMotDePasse(utilisateur.getId(), motDePasseField.getText());
                }
                showInfo("Utilisateur modifie avec succes.");
            }
            retourListe(event);
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        retourListe(event);
    }

    private void remplirUtilisateur(Utilisateur cible) {
        cible.setNom(nomField.getText().trim());
        cible.setPrenom(prenomField.getText().trim());
        cible.setEmail(emailField.getText().trim());
        cible.setTelephone(telephoneField.getText().trim());
        cible.setAdresse(adresseField.getText().trim());
        cible.setRole(roleCombo.getValue());
        cible.setSexe(sexeCombo.getValue());
        cible.setStatut(statutCombo.getValue());
        cible.setDateNaissance(dateNaissancePicker.getValue());
        cible.setDeuxFacteurs(deuxFacteursCheck.isSelected());
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
        if (roleCombo.getValue() == null) {
            message.append("- Le role est obligatoire.\n");
        }
        if (utilisateur == null && motDePasseField.getText().isBlank()) {
            message.append("- Le mot de passe est obligatoire.\n");
        }

        if (!message.isEmpty()) {
            showError(message.toString());
            return false;
        }
        return true;
    }

    private void retourListe(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GestionUtilisateurs.fxml"));
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
