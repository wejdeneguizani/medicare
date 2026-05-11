package com.medical.controllers;

import com.medical.model.Stock;
import com.medical.services.StockService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class StockController {

    @FXML private TextField tfIdMedicament;
    @FXML private TextField tfNumeroLot;
    @FXML private TextField tfQuantite;
    @FXML private TextField tfSeuilAlerte;
    @FXML private TextField tfPrixUnitaire;
    @FXML private TextField tfDateExpiration;
    @FXML private TextField tfLocalisation;
    @FXML private TextField tfFournisseur;

    @FXML private TableView<Stock> tableView;
    @FXML private TableColumn<Stock, Integer> colId;
    @FXML private TableColumn<Stock, Integer> colIdMed;
    @FXML private TableColumn<Stock, String>  colLot;
    @FXML private TableColumn<Stock, Integer> colQuantite;
    @FXML private TableColumn<Stock, Integer> colSeuil;
    @FXML private TableColumn<Stock, Double>  colPrix;
    @FXML private TableColumn<Stock, Date>    colExpiration;
    @FXML private TableColumn<Stock, String>  colLocalisation;
    @FXML private TableColumn<Stock, String>  colFournisseur;

    @FXML private Label lbMessage;

    private StockService service = new StockService();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idStock"));
        colIdMed.setCellValueFactory(new PropertyValueFactory<>("idMedicament"));
        colLot.setCellValueFactory(new PropertyValueFactory<>("numeroLot"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colSeuil.setCellValueFactory(new PropertyValueFactory<>("seuilAlerte"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colExpiration.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        colLocalisation.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        colFournisseur.setCellValueFactory(new PropertyValueFactory<>("fournisseur"));
        chargerTable();
    }

    private void chargerTable() {
        ObservableList<Stock> liste =
                FXCollections.observableArrayList(service.getTous());
        tableView.setItems(liste);
    }

    @FXML
    public void ajouterStock(ActionEvent e) {
        try {
            Stock s = new Stock();
            s.setIdMedicament(Integer.parseInt(tfIdMedicament.getText()));
            s.setNumeroLot(tfNumeroLot.getText());
            s.setQuantite(Integer.parseInt(tfQuantite.getText()));
            s.setSeuilAlerte(Integer.parseInt(tfSeuilAlerte.getText()));
            s.setPrixUnitaire(Double.parseDouble(tfPrixUnitaire.getText()));
            s.setDateExpiration(sdf.parse(tfDateExpiration.getText()));
            s.setDateReception(new Date());
            s.setLocalisation(tfLocalisation.getText());
            s.setFournisseur(tfFournisseur.getText());

            boolean ok = service.ajouter(s);
            if (ok) {
                lbMessage.setText(" Stock ajouté avec succès !");
                lbMessage.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
                viderFormulaire();
            } else {
                lbMessage.setText(" Erreur lors de l'ajout !");
                lbMessage.setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold;");
            }
            chargerTable();
        } catch (NumberFormatException ex) {
            lbMessage.setText("️ Vérifiez les champs numériques !");
            lbMessage.setStyle("-fx-text-fill: #e65100; -fx-font-weight: bold;");
        } catch (ParseException ex) {
            lbMessage.setText("️ Format de date invalide (yyyy-MM-dd) !");
            lbMessage.setStyle("-fx-text-fill: #e65100; -fx-font-weight: bold;");
        }
    }

    @FXML
    public void supprimerStock(ActionEvent e) {
        Stock selectionne = tableView.getSelectionModel().getSelectedItem();
        if (selectionne != null) {
            boolean ok = service.supprimer(selectionne.getIdStock());
            if (ok) {
                lbMessage.setText(" Stock supprimé !");
                lbMessage.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
            } else {
                lbMessage.setText(" Erreur lors de la suppression !");
                lbMessage.setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold;");
            }
            chargerTable();
        } else {
            lbMessage.setText("️ Sélectionnez un stock !");
            lbMessage.setStyle("-fx-text-fill: #e65100; -fx-font-weight: bold;");
        }
    }

    @FXML
    public void afficherStockFaible(ActionEvent e) {
        ObservableList<Stock> liste =
                FXCollections.observableArrayList(service.getStockFaible());
        tableView.setItems(liste);
        lbMessage.setText(" Affichage des stocks faibles (" + liste.size() + " article(s))");
        lbMessage.setStyle("-fx-text-fill: #e65100; -fx-font-weight: bold;");
    }

    @FXML
    public void afficherTout(ActionEvent e) {
        chargerTable();
        lbMessage.setText(" Tous les stocks affichés.");
        lbMessage.setStyle("-fx-text-fill: #1565c0; -fx-font-weight: bold;");
    }

    private void viderFormulaire() {
        tfIdMedicament.clear(); tfNumeroLot.clear(); tfQuantite.clear();
        tfSeuilAlerte.clear(); tfPrixUnitaire.clear(); tfDateExpiration.clear();
        tfLocalisation.clear(); tfFournisseur.clear();
    }

    @FXML public void allerMedicaments(ActionEvent e) { naviguer("/MainView.fxml"); }
    @FXML public void allerCategories(ActionEvent e)  { naviguer("/CategorieView.fxml"); }
    @FXML public void allerFabricants(ActionEvent e)  { naviguer("/FabricantView.fxml"); }
    @FXML public void allerFormes(ActionEvent e)      { naviguer("/FormeView.fxml"); }
    @FXML public void allerStock(ActionEvent e)       { /* déjà sur cette vue */ }
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