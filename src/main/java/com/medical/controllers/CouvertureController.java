package com.medical.controllers;

import com.medical.model.Couverture;
import com.medical.services.CouvertureService;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.io.IOException;

public class CouvertureController {

    @FXML private TextField tfId;
    @FXML private TextField tfIdAssurance;
    @FXML private ComboBox<String> cbTypeService;
    @FXML private TextField tfPourcentage;
    @FXML private TextField tfMontantMax;
    @FXML private TextField tfCondition;
    @FXML private TextField tfMontantDepense;
    @FXML private Label lblMessage;
    @FXML private Label lblSimulation;
    @FXML private TableView<Couverture> tableView;
    @FXML private TableColumn<Couverture, Integer> colId;
    @FXML private TableColumn<Couverture, Integer> colIdAssurance;
    @FXML private TableColumn<Couverture, String> colTypeService;
    @FXML private TableColumn<Couverture, Double> colPourcentage;
    @FXML private TableColumn<Couverture, Double> colMontantMax;
    @FXML private TableColumn<Couverture, String> colCondition;

    private final CouvertureService service = new CouvertureService();

    @FXML
    public void initialize() {
        cbTypeService.setItems(FXCollections.observableArrayList("Consultation", "Medicament", "Analyse", "Radiologie", "Hospitalisation"));
        colId.setCellValueFactory(new PropertyValueFactory<>("idCouverture"));
        colIdAssurance.setCellValueFactory(new PropertyValueFactory<>("idAssurance"));
        colTypeService.setCellValueFactory(new PropertyValueFactory<>("typeService"));
        colPourcentage.setCellValueFactory(new PropertyValueFactory<>("pourcentageCouverture"));
        colMontantMax.setCellValueFactory(new PropertyValueFactory<>("montantMax"));
        colCondition.setCellValueFactory(new PropertyValueFactory<>("conditionSpeciale"));
        chargerTable();
        tableView.setOnMouseClicked(e -> remplirFormulaire());
    }

    @FXML public void ajouter(ActionEvent e) {
        Couverture c = lireFormulaire(false);
        if (c == null) return;
        afficher(service.ajouter(c), "Couverture ajoutee.", "Erreur ajout couverture.");
        chargerTable();
        vider(null);
    }

    @FXML public void modifier(ActionEvent e) {
        Couverture c = lireFormulaire(true);
        if (c == null) return;
        afficher(service.modifier(c), "Couverture modifiee.", "Erreur modification couverture.");
        chargerTable();
    }

    @FXML public void supprimer(ActionEvent e) {
        Couverture c = tableView.getSelectionModel().getSelectedItem();
        if (c == null) {
            message("Selectionnez une couverture.", "#e65100");
            return;
        }
        afficher(service.supprimer(c.getIdCouverture()), "Couverture supprimee.", "Erreur suppression couverture.");
        chargerTable();
        vider(null);
    }

    @FXML public void afficherTout(ActionEvent e) {
        chargerTable();
    }

    @FXML public void filtrerParAssurance(ActionEvent e) {
        try {
            int idAssurance = Integer.parseInt(tfIdAssurance.getText());
            tableView.setItems(FXCollections.observableArrayList(service.getByAssuranceId(idAssurance)));
            message("Couvertures de l'assurance " + idAssurance + ".", "#1565c0");
        } catch (NumberFormatException ex) {
            message("ID Assurance invalide.", "#e65100");
        }
    }

    @FXML public void simuler(ActionEvent e) {
        try {
            if (tfIdAssurance.getText().isBlank()) {
                message("Entrez l'ID Assurance.", "#e65100");
                return;
            }
            if (tfMontantDepense.getText().isBlank()) {
                message("Entrez le montant depense pour la simulation.", "#e65100");
                return;
            }
            int idAssurance = Integer.parseInt(tfIdAssurance.getText());
            String type = cbTypeService.getValue();
            double montant = Double.parseDouble(tfMontantDepense.getText());
            if (type == null) {
                message("Choisissez un type de service.", "#e65100");
                return;
            }
            double resultat = service.simulerRemboursement(idAssurance, type, montant);
            lblSimulation.setText("Montant remboursable estime : " + resultat);
            lblSimulation.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        } catch (NumberFormatException ex) {
            message("ID assurance ou montant depense invalide.", "#e65100");
        }
    }

    @FXML public void vider(ActionEvent e) {
        tfId.clear();
        tfIdAssurance.clear();
        cbTypeService.setValue(null);
        tfPourcentage.clear();
        tfMontantMax.clear();
        tfCondition.clear();
        tfMontantDepense.clear();
        lblSimulation.setText("");
    }

    @FXML public void retourAssurances(ActionEvent e) {
        naviguer("/MainView.fxml");
    }

    @FXML public void ouvrirRemboursements(ActionEvent e) {
        naviguer("/RemboursementView.fxml");
    }

    @FXML public void ouvrirOcr(ActionEvent e) {
        naviguer("/OcrView.fxml");
    }

    private void chargerTable() {
        tableView.setItems(FXCollections.observableArrayList(service.getTous()));
    }

    private Couverture lireFormulaire(boolean avecId) {
        try {
            if (avecId && tfId.getText().isBlank()) {
                message("Selectionnez une couverture.", "#e65100");
                return null;
            }
            Couverture c = new Couverture();
            if (avecId) c.setIdCouverture(Integer.parseInt(tfId.getText()));
            c.setIdAssurance(Integer.parseInt(tfIdAssurance.getText()));
            c.setTypeService(cbTypeService.getValue());
            c.setPourcentageCouverture(Double.parseDouble(tfPourcentage.getText()));
            c.setMontantMax(Double.parseDouble(tfMontantMax.getText()));
            c.setConditionSpeciale(tfCondition.getText());
            if (c.getTypeService() == null || c.getPourcentageCouverture() < 0 || c.getPourcentageCouverture() > 100) {
                message("Verifiez service et pourcentage.", "#e65100");
                return null;
            }
            return c;
        } catch (Exception ex) {
            message("Verifiez tous les champs.", "#e65100");
            return null;
        }
    }

    private void remplirFormulaire() {
        Couverture c = tableView.getSelectionModel().getSelectedItem();
        if (c == null) return;
        tfId.setText(String.valueOf(c.getIdCouverture()));
        tfIdAssurance.setText(String.valueOf(c.getIdAssurance()));
        cbTypeService.setValue(c.getTypeService());
        tfPourcentage.setText(String.valueOf(c.getPourcentageCouverture()));
        tfMontantMax.setText(String.valueOf(c.getMontantMax()));
        tfCondition.setText(c.getConditionSpeciale());
    }

    private void afficher(boolean ok, String succes, String erreur) {
        message(ok ? succes : erreur, ok ? "#2e7d32" : "#c62828");
    }

    private void message(String texte, String couleur) {
        lblMessage.setText(texte);
        lblMessage.setStyle("-fx-text-fill: " + couleur + "; -fx-background-color: white; -fx-border-color: " + couleur + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold;");
        FadeTransition fade = new FadeTransition(Duration.millis(180), lblMessage);
        fade.setFromValue(0.25);
        fade.setToValue(1);
        fade.play();
    }

    private void naviguer(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            root.setOpacity(0);
            tableView.getScene().setRoot(root);
            FadeTransition fade = new FadeTransition(Duration.millis(220), root);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        } catch (IOException ex) {
            message("Navigation impossible.", "#c62828");
        }
    }
}
