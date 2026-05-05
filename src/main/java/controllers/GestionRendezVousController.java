package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.RendezVous;
import services.ServiceRendezVous;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class GestionRendezVousController {

    // Champs du formulaire
    @FXML private TextField tfPatientId, tfMedecinId, tfDate, tfHeure, tfMotif;
    @FXML private ComboBox<String> cbStatut;
    @FXML private Label lbMessage;

    // Table
    @FXML private TableView<RendezVous> tableRdv;
    @FXML private TableColumn<RendezVous, Integer> colId, colPatient, colMedecin;
    @FXML private TableColumn<RendezVous, LocalDate> colDate;
    @FXML private TableColumn<RendezVous, LocalTime> colHeure;
    @FXML private TableColumn<RendezVous, String>  colMotif, colStatut;

    private final ServiceRendezVous service = new ServiceRendezVous();

    @FXML
    public void initialize() {
        // Remplir le ComboBox statuts
        cbStatut.setItems(FXCollections.observableArrayList(
                "En attente", "Confirmé", "Annulé", "Terminé"));
        cbStatut.getSelectionModel().selectFirst();

        // Lier colonnes aux propriétés du modèle
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colMedecin.setCellValueFactory(new PropertyValueFactory<>("medecinId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateRdv"));
        colHeure.setCellValueFactory(new PropertyValueFactory<>("heureRdv"));
        colMotif.setCellValueFactory(new PropertyValueFactory<>("motif"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Quand on clique une ligne → remplir le formulaire
        tableRdv.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    if (selected != null) remplirFormulaire(selected);
                });

        chargerTous(null);
    }

    @FXML
    public void chargerTous(ActionEvent e) {
        ObservableList<RendezVous> liste =
                FXCollections.observableArrayList(service.getAll());
        tableRdv.setItems(liste);
        lbMessage.setText("✅ " + liste.size() + " rendez-vous chargés.");
    }

    @FXML
    public void ajouterRdv(ActionEvent e) {
        try {
            RendezVous rv = lireFormulaire();
            service.add(rv);
            lbMessage.setText("✅ Rendez-vous ajouté !");
            viderFormulaire();
            chargerTous(null);
        } catch (Exception ex) {
            lbMessage.setText("❌ Erreur : " + ex.getMessage());
        }
    }

    @FXML
    public void modifierRdv(ActionEvent e) {
        RendezVous selected = tableRdv.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lbMessage.setText("❌ Sélectionne un rendez-vous à modifier !");
            return;
        }
        try {
            RendezVous rv = lireFormulaire();
            rv.setId(selected.getId());
            service.update(rv);
            lbMessage.setText("✅ Rendez-vous modifié !");
            chargerTous(null);
        } catch (Exception ex) {
            lbMessage.setText("❌ Erreur : " + ex.getMessage());
        }
    }

    @FXML
    public void supprimerRdv(ActionEvent e) {
        RendezVous selected = tableRdv.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lbMessage.setText("❌ Sélectionne un rendez-vous à supprimer !");
            return;
        }
        service.delete(selected);
        lbMessage.setText("✅ Rendez-vous supprimé !");
        chargerTous(null);
    }

    @FXML
    public void ouvrirDisponibilites(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/GestionDisponibilite.fxml"));
            tableRdv.getScene().setRoot(root);
        } catch (IOException ex) {
            lbMessage.setText("❌ Impossible d'ouvrir : " + ex.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────
    private RendezVous lireFormulaire() {
        RendezVous rv = new RendezVous();
        rv.setPatientId(Integer.parseInt(tfPatientId.getText().trim()));
        rv.setMedecinId(Integer.parseInt(tfMedecinId.getText().trim()));
        rv.setDateRdv(LocalDate.parse(tfDate.getText().trim()));
        rv.setHeureRdv(LocalTime.parse(tfHeure.getText().trim()));
        rv.setMotif(tfMotif.getText().trim());
        rv.setStatut(cbStatut.getValue());
        return rv;
    }

    private void remplirFormulaire(RendezVous rv) {
        tfPatientId.setText(String.valueOf(rv.getPatientId()));
        tfMedecinId.setText(String.valueOf(rv.getMedecinId()));
        tfDate.setText(rv.getDateRdv().toString());
        tfHeure.setText(rv.getHeureRdv().toString());
        tfMotif.setText(rv.getMotif() != null ? rv.getMotif() : "");
        cbStatut.setValue(rv.getStatut());
    }

    private void viderFormulaire() {
        tfPatientId.clear(); tfMedecinId.clear();
        tfDate.clear(); tfHeure.clear(); tfMotif.clear();
        cbStatut.getSelectionModel().selectFirst();
    }
    @FXML
    public void ouvrirOrdonnance(ActionEvent e) {
        RendezVous selected = tableRdv.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lbMessage.setText("❌ Sélectionne d'abord un rendez-vous !");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/GestionOrdonnance.fxml"));
            Parent root = loader.load();

            // Passer le RDV sélectionné au contrôleur ordonnance
            GestionOrdonnanceController ctrl = loader.getController();
            ctrl.setRendezVous(selected);

            tableRdv.getScene().setRoot(root);
        } catch (IOException ex) {
            lbMessage.setText("❌ " + ex.getMessage());
        }
    }
}