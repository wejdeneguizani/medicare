package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Objectif;
import services.ServiceObjectif;

import java.time.LocalDate;
import java.util.List;

public class GestionObjectifController {

    @FXML private TextField tfTitre;
    @FXML private TextField tfCategorie;
    @FXML private TextField tfValeurCible;
    @FXML private TextField tfUniteMesure;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateEcheance;
    @FXML private ComboBox<String> cbStatut;
    @FXML private TextField tfPriorite;
    @FXML private TextField tfIdPatient;
    @FXML private TextField tfIdMedecin;

    @FXML private TableView<Objectif> tableView;
    @FXML private TableColumn<Objectif, Integer> colId;
    @FXML private TableColumn<Objectif, String>  colTitre;
    @FXML private TableColumn<Objectif, String>  colCategorie;
    @FXML private TableColumn<Objectif, String>  colStatut;
    @FXML private TableColumn<Objectif, Integer> colPriorite;

    @FXML private Label lbMessage;

    private final ServiceObjectif service = new ServiceObjectif();

    @FXML public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_objectif"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colPriorite.setCellValueFactory(new PropertyValueFactory<>("priorite"));

        cbStatut.setItems(FXCollections.observableArrayList(
                "En cours", "Atteint", "Abandonné", "En attente"
        ));
        cbStatut.setValue("En cours");
        refreshTable();

        tableView.setOnMouseClicked(e -> {
            Objectif s = tableView.getSelectionModel().getSelectedItem();
            if (s != null) {
                tfTitre.setText(s.getTitre());
                tfCategorie.setText(s.getCategorie());
                tfValeurCible.setText(String.valueOf(s.getValeur_cible()));
                tfUniteMesure.setText(s.getUnite_mesure());
                dpDateDebut.setValue(s.getDate_debut());
                dpDateEcheance.setValue(s.getDate_echeance());
                cbStatut.setValue(s.getStatut());
                tfPriorite.setText(String.valueOf(s.getPriorite()));
                tfIdPatient.setText(String.valueOf(s.getId_patient()));
                tfIdMedecin.setText(String.valueOf(s.getId_medecin()));
            }
        });
    }

    @FXML public void ajouterObjectif(ActionEvent e) {
        try {
            service.add(buildFromForm());
            lbMessage.setText("✓ Objectif ajouté !");
            viderFormulaire(null); refreshTable();
        } catch (Exception ex) { lbMessage.setText("Erreur : " + ex.getMessage()); ex.printStackTrace(); }
    }

    @FXML public void modifierObjectif(ActionEvent e) {
        Objectif s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) { lbMessage.setText("Sélectionnez un objectif."); return; }
        try {
            Objectif o = buildFromForm(); o.setId_objectif(s.getId_objectif());
            service.update(o);
            lbMessage.setText("✓ Objectif modifié !");
            viderFormulaire(null); refreshTable();
        } catch (Exception ex) { lbMessage.setText("Erreur : " + ex.getMessage()); ex.printStackTrace(); }
    }

    @FXML public void supprimerObjectif(ActionEvent e) {
        Objectif s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) { lbMessage.setText("Sélectionnez un objectif."); return; }
        service.delete(s);
        lbMessage.setText("✓ Objectif supprimé.");
        viderFormulaire(null); refreshTable();
    }

    @FXML public void voirDetails(ActionEvent e) {
        Objectif s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) { lbMessage.setText("Sélectionnez un objectif."); return; }
        DetailsObjectifController.objectif = s;
        try {
            Node vue = FXMLLoader.load(getClass().getResource("/DetailsObjectif.fxml"));
            AnchorPane contentArea = (AnchorPane) tfTitre.getScene().lookup("#contentArea");
            AnchorPane.setTopAnchor(vue, 0.0);
            AnchorPane.setBottomAnchor(vue, 0.0);
            AnchorPane.setLeftAnchor(vue, 0.0);
            AnchorPane.setRightAnchor(vue, 0.0);
            contentArea.getChildren().setAll(vue);
        } catch (Exception ex) { lbMessage.setText("Erreur : " + ex.getMessage()); ex.printStackTrace(); }
    }

    @FXML public void viderFormulaire(ActionEvent e) {
        tfTitre.clear(); tfCategorie.clear(); tfValeurCible.clear();
        tfUniteMesure.clear(); tfPriorite.clear();
        tfIdPatient.clear(); tfIdMedecin.clear();
        dpDateDebut.setValue(null); dpDateEcheance.setValue(null);
        cbStatut.setValue("En cours"); lbMessage.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        List<Objectif> list = service.getAll();
        tableView.setItems(FXCollections.observableArrayList(list));
    }

    private Objectif buildFromForm() {
        Objectif o = new Objectif();
        o.setTitre(tfTitre.getText());
        o.setCategorie(tfCategorie.getText());
        o.setValeur_cible(Float.parseFloat(tfValeurCible.getText()));
        o.setUnite_mesure(tfUniteMesure.getText());
        o.setDate_debut(dpDateDebut.getValue() != null ? dpDateDebut.getValue() : LocalDate.now());
        o.setDate_echeance(dpDateEcheance.getValue() != null ? dpDateEcheance.getValue() : LocalDate.now().plusMonths(3));
        o.setStatut(cbStatut.getValue());
        o.setPriorite(Integer.parseInt(tfPriorite.getText()));
        o.setId_patient(Integer.parseInt(tfIdPatient.getText()));
        o.setId_medecin(Integer.parseInt(tfIdMedecin.getText()));
        return o;
    }
}