package com.medical.controllers;

import com.medical.model.Fabricant;
import com.medical.services.FabricantService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class FabricantController {

    @FXML private TextField tfNom;
    @FXML private TextField tfPays;
    @FXML private TextField tfContact;
    @FXML private TextField tfSiteWeb;
    @FXML private TableView<Fabricant> tableView;
    @FXML private TableColumn<Fabricant, Integer> colId;
    @FXML private TableColumn<Fabricant, String> colNom;
    @FXML private TableColumn<Fabricant, String> colPays;
    @FXML private TableColumn<Fabricant, String> colContact;
    @FXML private TableColumn<Fabricant, String> colSiteWeb;
    @FXML private Label lbMessage;

    private FabricantService service = new FabricantService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFabricant"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPays.setCellValueFactory(new PropertyValueFactory<>("pays"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colSiteWeb.setCellValueFactory(new PropertyValueFactory<>("siteWeb"));
        chargerTable();
    }

    private void chargerTable() {
        ObservableList<Fabricant> liste =
                FXCollections.observableArrayList(service.getTous());
        tableView.setItems(liste);
    }

    @FXML
    public void ajouterFabricant(ActionEvent e) {
        Fabricant f = new Fabricant();
        f.setNom(tfNom.getText());
        f.setPays(tfPays.getText());
        f.setContact(tfContact.getText());
        f.setSiteWeb(tfSiteWeb.getText());
        boolean ok = service.ajouter(f);
        if (ok) {
            lbMessage.setText(" Fabricant ajouté avec succès !");
            lbMessage.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            tfNom.clear(); tfPays.clear(); tfContact.clear(); tfSiteWeb.clear();
        } else {
            lbMessage.setText(" Erreur lors de l'ajout !");
            lbMessage.setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold;");
        }
        chargerTable();
    }

    @FXML
    public void supprimerFabricant(ActionEvent e) {
        Fabricant selectionne = tableView.getSelectionModel().getSelectedItem();
        if (selectionne != null) {
            boolean ok = service.supprimer(selectionne.getIdFabricant());
            if (ok) {
                lbMessage.setText(" Fabricant supprimé !");
                lbMessage.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            } else {
                lbMessage.setText(" Erreur lors de la suppression !");
                lbMessage.setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold;");
            }
            chargerTable();
        } else {
            lbMessage.setText("️ Sélectionnez un fabricant !");
            lbMessage.setStyle("-fx-text-fill: #e65100; -fx-font-weight: bold;");
        }
    }

    @FXML public void allerMedicaments(ActionEvent e) { naviguer("/MainView.fxml"); }
    @FXML public void allerCategories(ActionEvent e)  { naviguer("/CategorieView.fxml"); }
    @FXML public void allerFabricants(ActionEvent e)  { /* déjà sur cette vue */ }
    @FXML public void allerFormes(ActionEvent e)      { naviguer("/FormeView.fxml"); }
    @FXML public void allerStock(ActionEvent e)       { naviguer("/StockView.fxml"); }
    @FXML public void allerAlertes(ActionEvent e)     { naviguer("/AlerteView.fxml"); }
    @FXML public void allerChatbot(ActionEvent e)     { naviguer("/ChatbotView.fxml"); }
    @FXML public void allerCommande(ActionEvent e)    { naviguer("/CommandeView.fxml"); }
    @FXML public void allerPaiement(ActionEvent e)    { naviguer("/PaiementView.fxml"); }
    @FXML public void allerShop(ActionEvent e)        { naviguer("/ShopView.fxml"); }

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