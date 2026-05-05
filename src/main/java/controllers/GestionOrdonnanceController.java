package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Ordonnance;
import models.RendezVous;
import services.ServiceOrdonnance;

import java.io.IOException;
import java.time.LocalDate;

public class GestionOrdonnanceController {

    @FXML private TextField tfRdvId, tfMedecinId, tfPatientId;
    @FXML private TextField tfMedicament, tfPosologie, tfDuree, tfDateEmission;
    @FXML private Label lbMessage;

    @FXML private TableView<Ordonnance> tableOrd;
    @FXML private TableColumn<Ordonnance, Integer>   colId, colRdvId;
    @FXML private TableColumn<Ordonnance, String>    colMedicament, colPosologie, colDuree;
    @FXML private TableColumn<Ordonnance, LocalDate> colDate;

    private final ServiceOrdonnance service = new ServiceOrdonnance();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRdvId.setCellValueFactory(new PropertyValueFactory<>("rendezVousId"));
        colMedicament.setCellValueFactory(new PropertyValueFactory<>("medicament"));
        colPosologie.setCellValueFactory(new PropertyValueFactory<>("posologie"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("dureeTraitement"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateEmission"));

        tableOrd.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> { if (sel != null) remplirFormulaire(sel); });

        chargerTous(null);
    }

    @FXML
    public void chargerTous(ActionEvent e) {
        ObservableList<Ordonnance> liste =
                FXCollections.observableArrayList(service.getAll());
        tableOrd.setItems(liste);
        lbMessage.setText("✅ " + liste.size() + " ordonnances chargées.");
    }

    @FXML
    public void ajouterOrd(ActionEvent e) {
        try {
            Ordonnance o = lireFormulaire();
            service.add(o);
            lbMessage.setText("✅ Ordonnance ajoutée !");
            chargerTous(null);
        } catch (Exception ex) {
            lbMessage.setText("❌ Erreur : " + ex.getMessage());
        }
    }

    @FXML
    public void modifierOrd(ActionEvent e) {
        Ordonnance sel = tableOrd.getSelectionModel().getSelectedItem();
        if (sel == null) { lbMessage.setText("❌ Sélectionne une ligne !"); return; }
        try {
            Ordonnance o = lireFormulaire();
            o.setId(sel.getId());
            service.update(o);
            lbMessage.setText("✅ Ordonnance modifiée !");
            chargerTous(null);
        } catch (Exception ex) {
            lbMessage.setText("❌ Erreur : " + ex.getMessage());
        }
    }

    @FXML
    public void supprimerOrd(ActionEvent e) {
        Ordonnance sel = tableOrd.getSelectionModel().getSelectedItem();
        if (sel == null) { lbMessage.setText("❌ Sélectionne une ligne !"); return; }
        service.delete(sel);
        lbMessage.setText("✅ Ordonnance supprimée !");
        chargerTous(null);
    }

    @FXML
    public void retourRdv(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/GestionRendezVous.fxml"));
            tableOrd.getScene().setRoot(root);
        } catch (IOException ex) {
            lbMessage.setText("❌ " + ex.getMessage());
        }
    }

    // Appelée depuis GestionRendezVousController pour pré-remplir le formulaire
    public void setRendezVous(RendezVous rv) {
        tfRdvId.setText(String.valueOf(rv.getId()));
        tfMedecinId.setText(String.valueOf(rv.getMedecinId()));
        tfPatientId.setText(String.valueOf(rv.getPatientId()));
        tfDateEmission.setText(LocalDate.now().toString());
        lbMessage.setText("✅ RDV du " + rv.getDateRdv() + " — " + rv.getMotif());
    }

    // ── Helpers ───────────────────────────────────────────────────
    private Ordonnance lireFormulaire() {
        Ordonnance o = new Ordonnance();
        o.setRendezVousId(Integer.parseInt(tfRdvId.getText().trim()));
        o.setMedecinId(Integer.parseInt(tfMedecinId.getText().trim()));
        o.setPatientId(Integer.parseInt(tfPatientId.getText().trim()));
        o.setMedicament(tfMedicament.getText().trim());
        o.setPosologie(tfPosologie.getText().trim());
        o.setDureeTraitement(tfDuree.getText().trim());
        String dateStr = tfDateEmission.getText().trim();
        o.setDateEmission(dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr));
        return o;
    }

    private void remplirFormulaire(Ordonnance o) {
        tfRdvId.setText(String.valueOf(o.getRendezVousId()));
        tfMedecinId.setText(String.valueOf(o.getMedecinId()));
        tfPatientId.setText(String.valueOf(o.getPatientId()));
        tfMedicament.setText(o.getMedicament());
        tfPosologie.setText(o.getPosologie());
        tfDuree.setText(o.getDureeTraitement() != null ? o.getDureeTraitement() : "");
        tfDateEmission.setText(o.getDateEmission() != null ? o.getDateEmission().toString() : "");
    }
}