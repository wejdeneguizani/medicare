package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.PlanCoaching;
import services.ServicePlanCoaching;

import java.time.LocalDate;
import java.util.List;

public class GestionPlanCoachingController {

    @FXML private TextField tfTitre;
    @FXML private TextField tfObjectifGlobal;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private ComboBox<String> cbStatut;
    @FXML private ComboBox<String> cbIntensite;
    @FXML private TextField tfIdPatient;
    @FXML private TextField tfIdMedecin;

    @FXML private TableView<PlanCoaching> tableView;
    @FXML private TableColumn<PlanCoaching, Integer> colId;
    @FXML private TableColumn<PlanCoaching, String>  colTitre;
    @FXML private TableColumn<PlanCoaching, String>  colStatut;
    @FXML private TableColumn<PlanCoaching, String>  colIntensite;
    @FXML private TableColumn<PlanCoaching, String>  colObjectif;

    @FXML private Label lbMessage;

    private final ServicePlanCoaching service = new ServicePlanCoaching();

    @FXML public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_plan_coaching"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colIntensite.setCellValueFactory(new PropertyValueFactory<>("intensite"));
        colObjectif.setCellValueFactory(new PropertyValueFactory<>("objectif_global"));

        cbStatut.setItems(FXCollections.observableArrayList("En cours", "Terminé", "Suspendu", "Planifié"));
        cbStatut.setValue("En cours");
        cbIntensite.setItems(FXCollections.observableArrayList("Faible", "Modérée", "Élevée"));
        cbIntensite.setValue("Modérée");
        refreshTable();

        tableView.setOnMouseClicked(e -> {
            PlanCoaching s = tableView.getSelectionModel().getSelectedItem();
            if (s != null) {
                tfTitre.setText(s.getTitre());
                tfObjectifGlobal.setText(s.getObjectif_global());
                dpDateDebut.setValue(s.getDate_debut());
                dpDateFin.setValue(s.getDate_fin());
                cbStatut.setValue(s.getStatut());
                cbIntensite.setValue(s.getIntensite());
                tfIdPatient.setText(String.valueOf(s.getId_patient()));
                tfIdMedecin.setText(String.valueOf(s.getId_medecin()));
            }
        });
    }

    @FXML public void ajouterPlan(ActionEvent e) {
        try {
            service.add(buildFromForm());
            lbMessage.setText("✓ Plan ajouté !");
            viderFormulaire(null); refreshTable();
        } catch (Exception ex) { lbMessage.setText("Erreur : " + ex.getMessage()); ex.printStackTrace(); }
    }

    @FXML public void modifierPlan(ActionEvent e) {
        PlanCoaching s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) { lbMessage.setText("Sélectionnez un plan."); return; }
        try {
            PlanCoaching p = buildFromForm(); p.setId_plan_coaching(s.getId_plan_coaching());
            service.update(p);
            lbMessage.setText("✓ Plan modifié !");
            viderFormulaire(null); refreshTable();
        } catch (Exception ex) { lbMessage.setText("Erreur : " + ex.getMessage()); ex.printStackTrace(); }
    }

    @FXML public void supprimerPlan(ActionEvent e) {
        PlanCoaching s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) { lbMessage.setText("Sélectionnez un plan."); return; }
        service.delete(s);
        lbMessage.setText("✓ Plan supprimé.");
        viderFormulaire(null); refreshTable();
    }

    @FXML public void voirDetails(ActionEvent e) {
        PlanCoaching s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) { lbMessage.setText("Sélectionnez un plan."); return; }
        DetailsPlanCoachingController.plan = s;
        try {
            Node vue = FXMLLoader.load(getClass().getResource("/DetailsPlanCoaching.fxml"));
            AnchorPane contentArea = (AnchorPane) tfTitre.getScene().lookup("#contentArea");
            AnchorPane.setTopAnchor(vue, 0.0);
            AnchorPane.setBottomAnchor(vue, 0.0);
            AnchorPane.setLeftAnchor(vue, 0.0);
            AnchorPane.setRightAnchor(vue, 0.0);
            contentArea.getChildren().setAll(vue);
        } catch (Exception ex) { lbMessage.setText("Erreur : " + ex.getMessage()); ex.printStackTrace(); }
    }

    @FXML public void viderFormulaire(ActionEvent e) {
        tfTitre.clear(); tfObjectifGlobal.clear();
        tfIdPatient.clear(); tfIdMedecin.clear();
        dpDateDebut.setValue(null); dpDateFin.setValue(null);
        cbStatut.setValue("En cours"); cbIntensite.setValue("Modérée");
        lbMessage.setText(""); tableView.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        List<PlanCoaching> list = service.getAll();
        tableView.setItems(FXCollections.observableArrayList(list));
    }

    private PlanCoaching buildFromForm() {
        PlanCoaching p = new PlanCoaching();
        p.setTitre(tfTitre.getText());
        p.setObjectif_global(tfObjectifGlobal.getText());
        p.setDate_debut(dpDateDebut.getValue() != null ? dpDateDebut.getValue() : LocalDate.now());
        p.setDate_fin(dpDateFin.getValue());
        p.setStatut(cbStatut.getValue());
        p.setIntensite(cbIntensite.getValue());
        p.setId_patient(Integer.parseInt(tfIdPatient.getText()));
        p.setId_medecin(Integer.parseInt(tfIdMedecin.getText()));
        return p;
    }
}