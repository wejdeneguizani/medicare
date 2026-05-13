package com.medical.controllers;

import com.medical.model.Remboursement;
import com.medical.services.CouvertureService;
import com.medical.services.RemboursementService;
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
import java.sql.Date;
import java.time.LocalDate;

public class RemboursementController {

    @FXML private TextField tfId;
    @FXML private TextField tfIdAssurance;
    @FXML private ComboBox<String> cbTypeDepense;
    @FXML private TextField tfMontantDepense;
    @FXML private TextField tfMontantEstime;
    @FXML private TextField tfMontantValide;
    @FXML private DatePicker dpDateDemande;
    @FXML private DatePicker dpDateValidation;
    @FXML private ComboBox<String> cbStatut;
    @FXML private TextField tfCommentaire;
    @FXML private Label lblMessage;
    @FXML private Label lblTotalValide;
    @FXML private TableView<Remboursement> tableView;
    @FXML private TableColumn<Remboursement, Integer> colId;
    @FXML private TableColumn<Remboursement, Integer> colIdAssurance;
    @FXML private TableColumn<Remboursement, String> colTypeDepense;
    @FXML private TableColumn<Remboursement, Double> colMontantDepense;
    @FXML private TableColumn<Remboursement, Double> colMontantEstime;
    @FXML private TableColumn<Remboursement, Double> colMontantValide;
    @FXML private TableColumn<Remboursement, Date> colDateDemande;
    @FXML private TableColumn<Remboursement, Date> colDateValidation;
    @FXML private TableColumn<Remboursement, String> colStatut;
    @FXML private TableColumn<Remboursement, String> colCommentaire;

    private final RemboursementService service = new RemboursementService();
    private final CouvertureService couvertureService = new CouvertureService();

    @FXML
    public void initialize() {
        cbTypeDepense.setItems(FXCollections.observableArrayList("Consultation", "Medicament", "Analyse", "Radiologie", "Hospitalisation"));
        cbStatut.setItems(FXCollections.observableArrayList("En attente", "Valide", "Refuse"));
        cbStatut.setValue("En attente");
        dpDateDemande.setValue(LocalDate.now());

        colId.setCellValueFactory(new PropertyValueFactory<>("idRemboursement"));
        colIdAssurance.setCellValueFactory(new PropertyValueFactory<>("idAssurance"));
        colTypeDepense.setCellValueFactory(new PropertyValueFactory<>("typeDepense"));
        colMontantDepense.setCellValueFactory(new PropertyValueFactory<>("montantDepense"));
        colMontantEstime.setCellValueFactory(new PropertyValueFactory<>("montantEstime"));
        colMontantValide.setCellValueFactory(new PropertyValueFactory<>("montantValide"));
        colDateDemande.setCellValueFactory(new PropertyValueFactory<>("dateDemande"));
        colDateValidation.setCellValueFactory(new PropertyValueFactory<>("dateValidation"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colCommentaire.setCellValueFactory(new PropertyValueFactory<>("commentaire"));

        chargerTable();
        tableView.setOnMouseClicked(e -> remplirFormulaire());
    }

    @FXML public void calculerEstimation(ActionEvent e) {
        try {
            int idAssurance = Integer.parseInt(tfIdAssurance.getText());
            String type = cbTypeDepense.getValue();
            double montant = Double.parseDouble(tfMontantDepense.getText());
            if (type == null) {
                message("Choisissez un type de depense.", "#e65100");
                return;
            }
            double estime = couvertureService.simulerRemboursement(idAssurance, type, montant);
            tfMontantEstime.setText(String.valueOf(estime));
            if (tfMontantValide.getText().isBlank()) tfMontantValide.setText("0");
            message("Estimation calculee selon la couverture.", "#2e7d32");
        } catch (NumberFormatException ex) {
            message("ID assurance ou montant invalide.", "#e65100");
        }
    }

    @FXML public void ajouter(ActionEvent e) {
        Remboursement r = lireFormulaire(false);
        if (r == null) return;
        afficher(service.ajouter(r), "Demande ajoutee.", "Erreur ajout demande.");
        chargerTable();
        vider(null);
    }

    @FXML public void modifier(ActionEvent e) {
        Remboursement r = lireFormulaire(true);
        if (r == null) return;
        afficher(service.modifier(r), "Demande modifiee.", "Erreur modification demande.");
        chargerTable();
    }

    @FXML public void supprimer(ActionEvent e) {
        Remboursement r = tableView.getSelectionModel().getSelectedItem();
        if (r == null) {
            message("Selectionnez une demande.", "#e65100");
            return;
        }
        afficher(service.supprimer(r.getIdRemboursement()), "Demande supprimee.", "Erreur suppression demande.");
        chargerTable();
        vider(null);
    }

    @FXML public void afficherTout(ActionEvent e) {
        chargerTable();
    }

    @FXML public void afficherEnAttente(ActionEvent e) {
        tableView.setItems(FXCollections.observableArrayList(service.getEnAttente()));
        message("Demandes en attente affichees.", "#1565c0");
    }

    @FXML public void vider(ActionEvent e) {
        tfId.clear();
        tfIdAssurance.clear();
        cbTypeDepense.setValue(null);
        tfMontantDepense.clear();
        tfMontantEstime.clear();
        tfMontantValide.clear();
        dpDateDemande.setValue(LocalDate.now());
        dpDateValidation.setValue(null);
        cbStatut.setValue("En attente");
        tfCommentaire.clear();
    }

    @FXML public void retourAssurances(ActionEvent e) {
        naviguer("/MainView.fxml");
    }

    @FXML public void ouvrirCouvertures(ActionEvent e) {
        naviguer("/CouvertureView.fxml");
    }

    @FXML public void ouvrirOcr(ActionEvent e) {
        naviguer("/OcrView.fxml");
    }

    public void preRemplirDepuisOcr(String typeDepense, String montantDepense, String texteOcr) {
        cbTypeDepense.setValue(typeDepense);
        tfMontantDepense.setText(montantDepense);
        tfCommentaire.setText("Cree depuis OCR");
        message("Demande pre-remplie depuis OCR. Completez ID Assurance puis calculez l'estimation.", "#1565c0");
    }

    private void chargerTable() {
        tableView.setItems(FXCollections.observableArrayList(service.getTous()));
        lblTotalValide.setText("Total valide : " + service.getTotalRembourseValide());
    }

    private Remboursement lireFormulaire(boolean avecId) {
        try {
            if (tfMontantEstime.getText().isBlank()) calculerEstimation(null);
            Remboursement r = new Remboursement();
            if (avecId) r.setIdRemboursement(Integer.parseInt(tfId.getText()));
            r.setIdAssurance(Integer.parseInt(tfIdAssurance.getText()));
            r.setTypeDepense(cbTypeDepense.getValue());
            r.setMontantDepense(Double.parseDouble(tfMontantDepense.getText()));
            r.setMontantEstime(Double.parseDouble(tfMontantEstime.getText()));
            r.setMontantValide(tfMontantValide.getText().isBlank() ? 0 : Double.parseDouble(tfMontantValide.getText()));
            r.setDateDemande(Date.valueOf(dpDateDemande.getValue()));
            r.setDateValidation(dpDateValidation.getValue() == null ? null : Date.valueOf(dpDateValidation.getValue()));
            r.setStatut(cbStatut.getValue());
            r.setCommentaire(tfCommentaire.getText());
            if (r.getTypeDepense() == null || r.getStatut() == null) {
                message("Choisissez type et statut.", "#e65100");
                return null;
            }
            return r;
        } catch (Exception ex) {
            message("Verifiez tous les champs.", "#e65100");
            return null;
        }
    }

    private void remplirFormulaire() {
        Remboursement r = tableView.getSelectionModel().getSelectedItem();
        if (r == null) return;
        tfId.setText(String.valueOf(r.getIdRemboursement()));
        tfIdAssurance.setText(String.valueOf(r.getIdAssurance()));
        cbTypeDepense.setValue(r.getTypeDepense());
        tfMontantDepense.setText(String.valueOf(r.getMontantDepense()));
        tfMontantEstime.setText(String.valueOf(r.getMontantEstime()));
        tfMontantValide.setText(String.valueOf(r.getMontantValide()));
        if (r.getDateDemande() != null) dpDateDemande.setValue(r.getDateDemande().toLocalDate());
        if (r.getDateValidation() != null) dpDateValidation.setValue(r.getDateValidation().toLocalDate());
        cbStatut.setValue(r.getStatut());
        tfCommentaire.setText(r.getCommentaire());
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
