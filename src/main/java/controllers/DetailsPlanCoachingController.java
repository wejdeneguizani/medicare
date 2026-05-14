package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import models.PlanCoaching;

public class DetailsPlanCoachingController {

    public static PlanCoaching plan = new PlanCoaching();

    @FXML private Label lbTitre;
    @FXML private Label lbObjectifGlobal;
    @FXML private Label lbDateDebut;
    @FXML private Label lbDateFin;
    @FXML private Label lbStatut;
    @FXML private Label lbIntensite;
    @FXML private Label lbIdPatient;
    @FXML private Label lbIdMedecin;

    @FXML public void initialize() {
        lbTitre.setText(plan.getTitre() != null ? plan.getTitre() : "");
        lbObjectifGlobal.setText(plan.getObjectif_global() != null ? plan.getObjectif_global() : "");
        lbDateDebut.setText(plan.getDate_debut() != null ? plan.getDate_debut().toString() : "");
        lbDateFin.setText(plan.getDate_fin() != null ? plan.getDate_fin().toString() : "—");
        lbStatut.setText(plan.getStatut() != null ? plan.getStatut() : "");
        lbIntensite.setText(plan.getIntensite() != null ? plan.getIntensite() : "");
        lbIdPatient.setText(String.valueOf(plan.getId_patient()));
        lbIdMedecin.setText(String.valueOf(plan.getId_medecin()));
    }

    @FXML public void retourListe(ActionEvent e) {
        try {
            Node vue = FXMLLoader.load(getClass().getResource("/GestionPlanCoaching.fxml"));
            AnchorPane contentArea = (AnchorPane) lbTitre.getScene().lookup("#contentArea");
            AnchorPane.setTopAnchor(vue, 0.0);
            AnchorPane.setBottomAnchor(vue, 0.0);
            AnchorPane.setLeftAnchor(vue, 0.0);
            AnchorPane.setRightAnchor(vue, 0.0);
            contentArea.getChildren().setAll(vue);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}