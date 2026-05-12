package com.medical.controllers;

import com.medical.model.Forme;
import com.medical.services.FormeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class FormeController {

    @FXML private TextField tfLibelle;
    @FXML private TextField tfVoieAdministration;
    @FXML private TableView<Forme> tableView;
    @FXML private TableColumn<Forme, Integer> colId;
    @FXML private TableColumn<Forme, String> colLibelle;
    @FXML private TableColumn<Forme, String> colVoie;
    @FXML private Label lbMessage;

    private FormeService service = new FormeService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idForme"));
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colVoie.setCellValueFactory(new PropertyValueFactory<>("voieAdministration"));
        chargerTable();
    }

    private void chargerTable() {
        ObservableList<Forme> liste =
                FXCollections.observableArrayList(service.getTous());
        tableView.setItems(liste);
    }

    @FXML
    public void ajouterForme(ActionEvent e) {
        Forme f = new Forme();
        f.setLibelle(tfLibelle.getText());
        f.setVoieAdministration(tfVoieAdministration.getText());
        boolean ok = service.ajouter(f);
        if (ok) {
            lbMessage.setText(" Forme pharmaceutique ajoutée avec succès !");
            lbMessage.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            tfLibelle.clear();
            tfVoieAdministration.clear();
        } else {
            lbMessage.setText(" Erreur lors de l'ajout !");
            lbMessage.setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold;");
        }
        chargerTable();
    }

    @FXML
    public void supprimerForme(ActionEvent e) {
        Forme selectionne = tableView.getSelectionModel().getSelectedItem();
        if (selectionne != null) {
            boolean ok = service.supprimer(selectionne.getIdForme());
            if (ok) {
                lbMessage.setText(" Forme supprimée !");
                lbMessage.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            } else {
                lbMessage.setText(" Erreur lors de la suppression !");
                lbMessage.setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold;");
            }
            chargerTable();
        } else {
            lbMessage.setText("️ Sélectionnez une forme !");
            lbMessage.setStyle("-fx-text-fill: #e65100; -fx-font-weight: bold;");
        }
    }

    @FXML public void allerMedicaments(ActionEvent e) { naviguer("/MainView.fxml"); }
    @FXML public void allerCategories(ActionEvent e)  { naviguer("/CategorieView.fxml"); }
    @FXML public void allerFabricants(ActionEvent e)  { naviguer("/FabricantView.fxml"); }
    @FXML public void allerFormes(ActionEvent e)      { /* déjà sur cette vue */ }
    @FXML public void allerStock(ActionEvent e)       { naviguer("/StockView.fxml"); }
    @FXML public void allerAlertes(ActionEvent e)     { naviguer("/AlerteView.fxml"); }
    @FXML public void allerChatbot(ActionEvent e)     { naviguer("/ChatbotView.fxml"); }
    @FXML public void allerCommande(ActionEvent e)    { naviguer("/CommandeView.fxml"); }
    @FXML public void allerPaiement(ActionEvent e)    { naviguer("/PaiementView.fxml"); }
    @FXML public void allerShop(ActionEvent e)        { naviguer("/ShopView.fxml"); }
    @FXML public void allerParametres(ActionEvent e)  { naviguer("/ParametresView.fxml"); }

    private void naviguer(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            tableView.getScene().setRoot(root);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}