package com.medical.controllers;

import com.medical.model.Commande;
import com.medical.model.Stock;
import com.medical.services.CommandeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class CommandeController {

    // ─── TYPE DE COMMANDE ─────────────────────────────────────────────────────
    @FXML private ToggleButton tgClient;
    @FXML private ToggleButton tgReappro;
    @FXML private Label        lbTypeInfo;

    // ─── RECHERCHE ────────────────────────────────────────────────────────────
    @FXML private TextField        tfRecherche;
    @FXML private ComboBox<String> cbListeMedicaments;  // liste déroulante

    // ─── TABLE RÉSULTATS ──────────────────────────────────────────────────────
    @FXML private TableView<Stock>            tableStock;
    @FXML private TableColumn<Stock,Integer>  colStockId;
    @FXML private TableColumn<Stock,String>   colStockNom;
    @FXML private TableColumn<Stock,Integer>  colStockQte;
    @FXML private TableColumn<Stock,Double>   colStockPrix;
    @FXML private TableColumn<Stock,String>   colStockLot;

    // ─── PANNEAU FORMULAIRE (caché jusqu'à sélection) ─────────────────────────
    @FXML private VBox  panneauFormulaire;
    @FXML private Label lbMedicamentChoisi;
    @FXML private Label lbStockDispo;
    @FXML private Label lbPrixInfo;

    // Champs communs
    @FXML private TextField    tfNom;          // nom client OU fournisseur
    @FXML private TextField    tfQuantite;
    @FXML private ComboBox<String> cbMode;
    @FXML private Label        lbMontant;

    // Champs client uniquement
    @FXML private HBox         rowEmail;
    @FXML private HBox         rowTel;
    @FXML private TextField    tfEmail;
    @FXML private TextField    tfTelephone;

    // Labels adaptatifs
    @FXML private Label lbNomLabel;
    @FXML private Label lbModeLabel;

    @FXML private Label lbMessage;

    // ─── CHAMPS CARTE BANCAIRE ────────────────────────────────────────────────
    @FXML private VBox         panneauCarte;
    @FXML private TextField    tfNumeroCarte;
    @FXML private TextField    tfTitulaire;
    @FXML private TextField    tfExpiration;
    @FXML private PasswordField tfCvv;

    private final CommandeService service = new CommandeService();
    private List<Stock> tousLesStocks;
    private Stock       stockSelectionne;
    private boolean     modeClient = true;   // true = CLIENT, false = REAPPRO

    // ─── INIT ─────────────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        // Colonnes table
        colStockId.setCellValueFactory(new PropertyValueFactory<>("idStock"));
        colStockNom.setCellValueFactory(new PropertyValueFactory<>("fournisseur")); // réutilisé pour nom médicament
        colStockQte.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colStockPrix.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colStockLot.setCellValueFactory(new PropertyValueFactory<>("numeroLot"));

        // Mode paiement
        cbMode.setItems(FXCollections.observableArrayList(
                "Espèces", "Carte bancaire", "Chèque", "Virement", "Bon de commande"));
        cbMode.getSelectionModel().selectFirst();

        // Charger tous les stocks pour la ComboBox
        tousLesStocks = service.getTousStocksAvecNom();
        ObservableList<String> noms = FXCollections.observableArrayList();
        tousLesStocks.forEach(s -> noms.add(s.getFournisseur() + " (Stock: " + s.getQuantite() + ")"));
        cbListeMedicaments.setItems(noms);

        // Calcul montant auto
        tfQuantite.textProperty().addListener((o, v1, v2) -> calculerMontant());

        // Sélection depuis la table
        tableStock.getSelectionModel().selectedItemProperty().addListener(
                (o, v, stock) -> { if (stock != null) selectionnerStock(stock); });

        // Sélection depuis la ComboBox
        cbListeMedicaments.setOnAction(e -> {
            int i = cbListeMedicaments.getSelectionModel().getSelectedIndex();
            if (i >= 0 && i < tousLesStocks.size()) {
                selectionnerStock(tousLesStocks.get(i));
                // Aussi mettre à jour la table pour montrer la sélection
                tableStock.setItems(FXCollections.observableArrayList(tousLesStocks.get(i)));
                tableStock.getSelectionModel().selectFirst();
            }
        });

        // Panneau formulaire caché au départ
        panneauFormulaire.setVisible(false);
        panneauFormulaire.setManaged(false);

        // Panneau carte caché au départ
        panneauCarte.setVisible(false);
        panneauCarte.setManaged(false);

        // Mode client par défaut
        basculerVersClient();
    }

    // ─── TOGGLE CLIENT / RÉAPPRO ──────────────────────────────────────────────
    @FXML
    public void basculerVersClient() {
        modeClient = true;
        lbTypeInfo.setText("🛒 Vente au client — le stock sera diminué");
        lbTypeInfo.setStyle("-fx-text-fill:#1565c0;-fx-font-weight:bold;");
        lbNomLabel.setText("Nom du client :");
        lbModeLabel.setText("Mode de paiement :");
        rowEmail.setVisible(true); rowEmail.setManaged(true);
        rowTel.setVisible(true);   rowTel.setManaged(true);
        tfNom.setPromptText("Nom complet du client");
        cbMode.getItems().setAll("Espèces", "Carte bancaire", "Chèque", "Virement");
        cbMode.getSelectionModel().selectFirst();
        if (tgClient != null) tgClient.setSelected(true);
        if (tgReappro != null) tgReappro.setSelected(false);
    }

    @FXML
    public void basculerVersReappro() {
        modeClient = false;
        lbTypeInfo.setText("📦 Réapprovisionnement — le stock sera augmenté");
        lbTypeInfo.setStyle("-fx-text-fill:#e65100;-fx-font-weight:bold;");
        lbNomLabel.setText("Nom du fournisseur :");
        lbModeLabel.setText("Mode de règlement :");
        rowEmail.setVisible(false); rowEmail.setManaged(false);
        rowTel.setVisible(false);   rowTel.setManaged(false);
        tfNom.setPromptText("Nom du fournisseur");
        cbMode.getItems().setAll("Virement", "Chèque", "Bon de commande");
        cbMode.getSelectionModel().selectFirst();
        if (tgClient != null) tgClient.setSelected(false);
        if (tgReappro != null) tgReappro.setSelected(true);
    }

    // ─── RECHERCHE PAR TEXTE ──────────────────────────────────────────────────
    @FXML
    public void rechercherParTexte(ActionEvent e) {
        String terme = tfRecherche.getText().trim();
        if (terme.isEmpty()) {
            afficherTousStocks();
            return;
        }
        List<Stock> resultats = service.rechercherParNom(terme);
        tableStock.setItems(FXCollections.observableArrayList(resultats));
        if (resultats.isEmpty())
            msg("❌ Aucun résultat pour \"" + terme + "\"", "#c62828");
        else
            msg("✅ " + resultats.size() + " résultat(s) — cliquez sur un médicament", "#2e7d32");
    }

    @FXML
    public void afficherTousStocks() {
        tableStock.setItems(FXCollections.observableArrayList(tousLesStocks));
        msg("📋 " + tousLesStocks.size() + " médicament(s) en stock", "#1565c0");
    }

    // ─── SÉLECTION D'UN STOCK ─────────────────────────────────────────────────
    private void selectionnerStock(Stock s) {
        stockSelectionne = s;
        lbMedicamentChoisi.setText("💊 " + s.getFournisseur());
        lbStockDispo.setText("📦 Stock disponible : " + s.getQuantite() + " unités");
        lbPrixInfo.setText("💰 Prix unitaire : " + String.format("%.2f DT", s.getPrixUnitaire()));

        String couleurStock = s.getQuantite() <= s.getSeuilAlerte() ? "#c62828" : "#2e7d32";
        lbStockDispo.setStyle("-fx-text-fill:" + couleurStock + ";-fx-font-weight:bold;");

        panneauFormulaire.setVisible(true);
        panneauFormulaire.setManaged(true);
        tfQuantite.clear();
        lbMontant.setText("—");
        msg("Remplissez le formulaire puis cliquez sur Confirmer.", "#1565c0");
    }

    // ─── CALCUL MONTANT ───────────────────────────────────────────────────────
    private void calculerMontant() {
        if (stockSelectionne == null) return;
        try {
            int qte = Integer.parseInt(tfQuantite.getText());
            if (qte <= 0) { lbMontant.setText("—"); return; }
            double total = qte * stockSelectionne.getPrixUnitaire();
            lbMontant.setText(String.format("%.2f DT", total));
            lbMontant.setStyle("-fx-text-fill:#e91e8c;-fx-font-weight:bold;-fx-font-size:17px;");
        } catch (NumberFormatException ignored) {
            lbMontant.setText("—");
        }
    }

    // ─── AFFICHER/MASQUER CHAMPS CARTE ────────────────────────────────────────
    @FXML
    public void onModeChanged(ActionEvent e) {
        boolean isCarte = "Carte bancaire".equals(cbMode.getValue());
        panneauCarte.setVisible(isCarte);
        panneauCarte.setManaged(isCarte);
    }

    // ─── CONFIRMER ────────────────────────────────────────────────────────────
    @FXML
    public void confirmerCommande(ActionEvent e) {
        if (stockSelectionne == null) { msg("⚠️ Sélectionnez un médicament !", "#e65100"); return; }
        if (tfNom.getText().trim().isEmpty()) { msg("⚠️ Nom obligatoire !", "#e65100"); return; }

        int qte;
        try {
            qte = Integer.parseInt(tfQuantite.getText());
            if (qte <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            msg("⚠️ Quantité invalide !", "#e65100"); return;
        }

        // Vérification stock uniquement pour vente client
        if (modeClient && qte > stockSelectionne.getQuantite()) {
            msg("❌ Stock insuffisant ! Disponible : " + stockSelectionne.getQuantite(), "#c62828");
            return;
        }

        if (modeClient && (tfEmail.getText().trim().isEmpty() || !tfEmail.getText().contains("@"))) {
            msg("⚠️ Email client invalide !", "#e65100"); return;
        }

        // Validation carte bancaire
        if ("Carte bancaire".equals(cbMode.getValue())) {
            String num = tfNumeroCarte.getText().replaceAll("\\s", "");
            if (num.length() != 16) { msg("⚠️ Numéro de carte invalide (16 chiffres requis) !", "#e65100"); return; }
            if (tfTitulaire.getText().trim().isEmpty()) { msg("⚠️ Nom du titulaire requis !", "#e65100"); return; }
            if (!tfExpiration.getText().matches("\\d{2}/\\d{2}")) { msg("⚠️ Date d'expiration invalide (MM/AA) !", "#e65100"); return; }
            if (tfCvv.getText().length() != 3) { msg("⚠️ CVV invalide (3 chiffres requis) !", "#e65100"); return; }
        }

        // Construire la commande
        Commande c = new Commande();
        c.setTypeCommande(modeClient ? "CLIENT" : "REAPPROVISIONNEMENT");
        c.setNomClient(tfNom.getText().trim());
        c.setEmailClient(modeClient ? tfEmail.getText().trim() : "");
        c.setTelephoneClient(modeClient ? tfTelephone.getText().trim() : "");
        c.setIdStock(stockSelectionne.getIdStock());
        c.setQuantite(qte);
        c.setPrixUnitaire(stockSelectionne.getPrixUnitaire());
        c.setMontantTotal(qte * stockSelectionne.getPrixUnitaire());
        c.setModePaiement(cbMode.getValue());
        c.setStatut("Confirmée");
        c.setNomMedicament(stockSelectionne.getFournisseur());
        c.setNumeroLot(stockSelectionne.getNumeroLot());

        boolean enregistre = service.ajouter(c);
        System.out.println("➡️ BDD enregistré=" + enregistre + " ID=" + c.getIdCommande());

        if (enregistre) {
            if (modeClient) service.diminuerStock(stockSelectionne.getIdStock(), qte);
            else            service.augmenterStock(stockSelectionne.getIdStock(), qte);
        }

        // Toujours naviguer vers le reçu
        naviguerVersPaiement(c);
    }

    // ─── NAVIGATION VERS PAIEMENT ─────────────────────────────────────────────
    private void naviguerVersPaiement(Commande commande) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaiementView.fxml"));
            Parent root = loader.load();
            PaiementController ctrl = loader.getController();
            ctrl.recevoirCommande(commande);
            tableStock.getScene().setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
            msg("❌ Erreur navigation : " + ex.getMessage(), "#c62828");
        }
    }

    // ─── HELPERS ──────────────────────────────────────────────────────────────
    private void msg(String t, String c) {
        lbMessage.setText(t);
        lbMessage.setStyle("-fx-text-fill:" + c + ";-fx-font-weight:bold;");
    }

    // ─── PRÉSÉLECTION DEPUIS LE SHOP ──────────────────────────────────────────
    public void preselectionnerStock(Stock stock) {
        // Afficher tous les stocks dans la table
        tableStock.setItems(javafx.collections.FXCollections.observableArrayList(tousLesStocks));
        // Sélectionner le bon
        selectionnerStock(stock);
        tableStock.getSelectionModel().select(stock);
        // Scroll vers le formulaire
        panneauFormulaire.setVisible(true);
        panneauFormulaire.setManaged(true);
    }

    // ─── NAVIGATION ───────────────────────────────────────────────────────────
    @FXML public void allerMedicaments(ActionEvent e) { naviguer("/MainView.fxml"); }
    @FXML public void allerCategories(ActionEvent e)  { naviguer("/CategorieView.fxml"); }
    @FXML public void allerFabricants(ActionEvent e)  { naviguer("/FabricantView.fxml"); }
    @FXML public void allerFormes(ActionEvent e)      { naviguer("/FormeView.fxml"); }
    @FXML public void allerStock(ActionEvent e)       { naviguer("/StockView.fxml"); }
    @FXML public void allerAlertes(ActionEvent e)     { naviguer("/AlerteView.fxml"); }
    @FXML public void allerChatbot(ActionEvent e)     { naviguer("/ChatbotView.fxml"); }
    @FXML public void allerCommande(ActionEvent e)    { /* déjà ici */ }
    @FXML public void allerPaiement(ActionEvent e)    { naviguer("/PaiementView.fxml"); }
    @FXML public void allerShop(ActionEvent e)        { naviguer("/ShopView.fxml"); }

    private void naviguer(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            tableStock.getScene().setRoot(root);
        } catch (IOException ex) { System.out.println(ex.getMessage()); }
    }
}