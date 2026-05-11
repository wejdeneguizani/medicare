package com.medical.controllers;

import com.medical.model.Categorie;
import com.medical.services.CategorieService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class CategorieController {

    @FXML private TextField tfLibelle;
    @FXML private TextField tfCodeAtc;
    @FXML private TextField tfDescription;
    @FXML private TableView<Categorie> tableView;
    @FXML private TableColumn<Categorie, Integer> colId;
    @FXML private TableColumn<Categorie, String> colLibelle;
    @FXML private TableColumn<Categorie, String> colCodeAtc;
    @FXML private TableColumn<Categorie, String> colDescription;
    @FXML private Label lbMessage;

    private CategorieService service = new CategorieService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(
                new PropertyValueFactory<>("idCategorie"));
        colLibelle.setCellValueFactory(
                new PropertyValueFactory<>("libelle"));
        colCodeAtc.setCellValueFactory(
                new PropertyValueFactory<>("codeAtc"));
        colDescription.setCellValueFactory(
                new PropertyValueFactory<>("description"));
        chargerTable();
    }

    private void chargerTable() {
        ObservableList<Categorie> liste =
                FXCollections.observableArrayList(service.getTous());
        tableView.setItems(liste);
    }

    @FXML
    public void ajouterCategorie(ActionEvent e) {
        Categorie c = new Categorie();
        c.setLibelle(tfLibelle.getText());
        c.setCodeAtc(tfCodeAtc.getText());
        c.setDescription(tfDescription.getText());
        boolean ok = service.ajouter(c);
        if (ok) {
            lbMessage.setText(" Catégorie ajoutée avec succès !");
            lbMessage.setStyle(
                    "-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            tfLibelle.clear();
            tfCodeAtc.clear();
            tfDescription.clear();
        } else {
            lbMessage.setText(" Erreur lors de l'ajout !");
            lbMessage.setStyle(
                    "-fx-text-fill: #c62828; -fx-font-weight: bold;");
        }
        chargerTable();
    }

    @FXML
    public void supprimerCategorie(ActionEvent e) {
        Categorie selectionne =
                tableView.getSelectionModel().getSelectedItem();
        if (selectionne != null) {
            boolean ok = service.supprimer(selectionne.getIdCategorie());
            if (ok) {
                lbMessage.setText(" Catégorie supprimée !");
                lbMessage.setStyle(
                        "-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            } else {
                lbMessage.setText(" Erreur lors de la suppression !");
                lbMessage.setStyle(
                        "-fx-text-fill: #c62828; -fx-font-weight: bold;");
            }
            chargerTable();
        } else {
            lbMessage.setText(" Sélectionnez une catégorie !");
            lbMessage.setStyle(
                    "-fx-text-fill: #e65100; -fx-font-weight: bold;");
        }
    }
    @FXML public void allerMedicaments(ActionEvent e) { naviguer("/MainView.fxml"); }
    @FXML public void allerCategories(ActionEvent e)  { /* déjà sur cette vue */ }
    @FXML public void allerFabricants(ActionEvent e)  { naviguer("/FabricantView.fxml"); }
    @FXML public void allerFormes(ActionEvent e)      { naviguer("/FormeView.fxml"); }
    @FXML public void allerStock(ActionEvent e)       { naviguer("/StockView.fxml"); }
    @FXML public void allerAlertes(ActionEvent e)     { naviguer("/AlerteView.fxml"); }
    @FXML public void allerChatbot(ActionEvent e)     { naviguer("/ChatbotView.fxml"); }
    @FXML public void allerCommande(ActionEvent e)    { naviguer("/CommandeView.fxml"); }
    @FXML public void allerPaiement(ActionEvent e)    { naviguer("/PaiementView.fxml"); }

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