package com.medical.controllers;

import com.medical.model.Medicament;
import com.medical.services.MedicamentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class MedicamentController {

    @FXML private TextField tfNom;
    @FXML private TextField tfDci;
    @FXML private TextField tfDosage;
    @FXML private TextField tfIdCategorie;  // ajouter
    @FXML private TextField tfIdForme;
    @FXML private TextField tfIdFabricant;
    @FXML private TableView<Medicament> tableView;
    @FXML private TableColumn<Medicament, Integer> colId;
    @FXML private TableColumn<Medicament, String> colNom;
    @FXML private TableColumn<Medicament, String> colDci;
    @FXML private TableColumn<Medicament, String> colDosage;
    @FXML private Label lbMessage;

    private MedicamentService service = new MedicamentService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idMedicament"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomCommercial"));
        colDci.setCellValueFactory(new PropertyValueFactory<>("nomDci"));
        colDosage.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        chargerTable();
    }

    private void chargerTable() {
        ObservableList<Medicament> liste =
                FXCollections.observableArrayList(service.getTous());
        tableView.setItems(liste);
    }

    @FXML
    public void ajouterMedicament(ActionEvent e) {
        try {
            Medicament m = new Medicament();
            m.setNomCommercial(tfNom.getText());
            m.setNomDci(tfDci.getText());
            m.setDosage(tfDosage.getText());
            m.setIdCategorie(Integer.parseInt(tfIdCategorie.getText()));
            m.setIdForme(Integer.parseInt(tfIdForme.getText()));
            m.setIdFabricant(Integer.parseInt(tfIdFabricant.getText()));
            m.setEstActif(true);
            boolean ok = service.ajouter(m);
            lbMessage.setText(ok ? "✅ Ajouté !" : "❌ Erreur !");
            if (ok) {
                tfNom.clear();
                tfDci.clear();
                tfDosage.clear();
                tfIdCategorie.clear();
                tfIdForme.clear();
                tfIdFabricant.clear();
            }
            chargerTable();
        } catch (NumberFormatException ex) {
            lbMessage.setText("⚠️ Les IDs doivent être des nombres !");
        }
    }

    @FXML
    public void supprimerMedicament(ActionEvent e) {
        Medicament selectionne = tableView.getSelectionModel().getSelectedItem();
        if (selectionne != null) {
            service.supprimer(selectionne.getIdMedicament());
            chargerTable();
        } else {
            lbMessage.setText("⚠️ Sélectionnez un médicament !");
        }
    }
    @FXML
    public void allerMedicaments(ActionEvent e) {
        // déjà sur la vue Médicaments
    }

    @FXML
    public void allerCategories(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/CategorieView.fxml"));
            Parent root = loader.load();
            tableView.getScene().setRoot(root);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}