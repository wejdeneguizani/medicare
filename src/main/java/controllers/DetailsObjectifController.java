package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import models.Objectif;

public class DetailsObjectifController {

    public static Objectif objectif = new Objectif();

    @FXML private Label lbTitre;
    @FXML private Label lbCategorie;
    @FXML private Label lbValeurCible;
    @FXML private Label lbUniteMesure;
    @FXML private Label lbDateDebut;
    @FXML private Label lbDateEcheance;
    @FXML private Label lbStatut;
    @FXML private Label lbPriorite;
    @FXML private Label lbIdPatient;
    @FXML private Label lbIdMedecin;

    @FXML public void initialize() {
        lbTitre.setText(objectif.getTitre() != null ? objectif.getTitre() : "");
        lbCategorie.setText(objectif.getCategorie() != null ? objectif.getCategorie() : "");
        lbValeurCible.setText(String.valueOf(objectif.getValeur_cible()));
        lbUniteMesure.setText(objectif.getUnite_mesure() != null ? objectif.getUnite_mesure() : "");
        lbDateDebut.setText(objectif.getDate_debut() != null ? objectif.getDate_debut().toString() : "");
        lbDateEcheance.setText(objectif.getDate_echeance() != null ? objectif.getDate_echeance().toString() : "");
        lbStatut.setText(objectif.getStatut() != null ? objectif.getStatut() : "");
        lbPriorite.setText(String.valueOf(objectif.getPriorite()));
        lbIdPatient.setText(String.valueOf(objectif.getId_patient()));
        lbIdMedecin.setText(String.valueOf(objectif.getId_medecin()));
    }

    @FXML public void retourListe(ActionEvent e) {
        try {
            Node vue = FXMLLoader.load(getClass().getResource("/GestionObjectif.fxml"));
            AnchorPane contentArea = (AnchorPane) lbTitre.getScene().lookup("#contentArea");
            AnchorPane.setTopAnchor(vue, 0.0);
            AnchorPane.setBottomAnchor(vue, 0.0);
            AnchorPane.setLeftAnchor(vue, 0.0);
            AnchorPane.setRightAnchor(vue, 0.0);
            contentArea.getChildren().setAll(vue);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}