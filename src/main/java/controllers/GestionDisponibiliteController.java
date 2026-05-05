package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Disponibilite;
import services.ServiceDisponibilite;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class GestionDisponibiliteController {

    @FXML private TextField tfMedecinId, tfDate, tfHeureDebut, tfHeureFin;
    @FXML private ComboBox<String> cbStatut;
    @FXML private Label lbMessage;

    @FXML private TableView<Disponibilite> tableDispo;
    @FXML private TableColumn<Disponibilite, Integer>   colId, colMedecin;
    @FXML private TableColumn<Disponibilite, LocalDate> colDate;
    @FXML private TableColumn<Disponibilite, LocalTime> colDebut, colFin;
    @FXML private TableColumn<Disponibilite, String>    colStatut;

    private final ServiceDisponibilite service = new ServiceDisponibilite();

    @FXML
    public void initialize() {
        cbStatut.setItems(FXCollections.observableArrayList(
                "Disponible", "Réservé", "Annulé"));
        cbStatut.getSelectionModel().selectFirst();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMedecin.setCellValueFactory(new PropertyValueFactory<>("medecinId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateDisponible"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        tableDispo.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, sel) -> { if (sel != null) remplirFormulaire(sel); });

        chargerTous(null);
    }

    @FXML public void chargerTous(ActionEvent e) {
        ObservableList<Disponibilite> liste =
                FXCollections.observableArrayList(service.getAll());
        tableDispo.setItems(liste);
        lbMessage.setText("✅ " + liste.size() + " disponibilités chargées.");
    }

    @FXML public void ajouterDispo(ActionEvent e) {
        try {
            Disponibilite d = lireFormulaire();
            service.add(d);
            lbMessage.setText("✅ Disponibilité ajoutée !");
            chargerTous(null);
        } catch (Exception ex) {
            lbMessage.setText("❌ Erreur : " + ex.getMessage());
        }
    }

    @FXML public void modifierDispo(ActionEvent e) {
        Disponibilite sel = tableDispo.getSelectionModel().getSelectedItem();
        if (sel == null) { lbMessage.setText("❌ Sélectionne une ligne !"); return; }
        try {
            Disponibilite d = lireFormulaire();
            d.setId(sel.getId());
            service.update(d);
            lbMessage.setText("✅ Disponibilité modifiée !");
            chargerTous(null);
        } catch (Exception ex) {
            lbMessage.setText("❌ Erreur : " + ex.getMessage());
        }
    }

    @FXML public void supprimerDispo(ActionEvent e) {
        Disponibilite sel = tableDispo.getSelectionModel().getSelectedItem();
        if (sel == null) { lbMessage.setText("❌ Sélectionne une ligne !"); return; }
        service.delete(sel);
        lbMessage.setText("✅ Disponibilité supprimée !");
        chargerTous(null);
    }

    @FXML public void retourRdv(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/GestionRendezVous.fxml"));
            tableDispo.getScene().setRoot(root);
        } catch (IOException ex) {
            lbMessage.setText("❌ " + ex.getMessage());
        }
    }

    private Disponibilite lireFormulaire() {
        Disponibilite d = new Disponibilite();
        d.setMedecinId(Integer.parseInt(tfMedecinId.getText().trim()));
        d.setDateDisponible(LocalDate.parse(tfDate.getText().trim()));
        d.setHeureDebut(LocalTime.parse(tfHeureDebut.getText().trim()));
        d.setHeureFin(LocalTime.parse(tfHeureFin.getText().trim()));
        d.setStatut(cbStatut.getValue());
        return d;
    }

    private void remplirFormulaire(Disponibilite d) {
        tfMedecinId.setText(String.valueOf(d.getMedecinId()));
        tfDate.setText(d.getDateDisponible().toString());
        tfHeureDebut.setText(d.getHeureDebut().toString());
        tfHeureFin.setText(d.getHeureFin().toString());
        cbStatut.setValue(d.getStatut());
    }
}