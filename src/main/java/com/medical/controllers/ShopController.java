package com.medical.controllers;

import com.medical.model.Stock;
import com.medical.services.CommandeService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class ShopController {

    @FXML private FlowPane grilleProduits;
    @FXML private TextField tfSearch;
    @FXML private ComboBox<String> cbFiltreStock;
    @FXML private Label lbMessage;

    private final CommandeService service = new CommandeService();
    private List<Stock> tousLesStocks;

    // Icônes emoji par catégorie de médicament
    private static final String[] EMOJIS = {"💊", "💉", "🧴", "🩺", "🌿", "🧪", "❤️", "🫁", "🧠", "👁️"};
    private static final String[] COULEURS = {
            "#fce4ec", "#e8f5e9", "#e3f2fd", "#fff8e1",
            "#f3e5f5", "#e0f7fa", "#fbe9e7", "#e8eaf6"
    };

    @FXML
    public void initialize() {
        cbFiltreStock.setItems(FXCollections.observableArrayList(
                "Tous les médicaments", "✅ En stock", "⚠️ Stock faible", "❌ Rupture de stock"
        ));
        cbFiltreStock.getSelectionModel().selectFirst();

        tousLesStocks = service.getTousStocksAvecNom();
        afficherProduits(tousLesStocks);
    }

    @FXML
    public void filtrerShop() {
        String recherche = tfSearch.getText().toLowerCase().trim();
        String filtre = cbFiltreStock.getValue();

        List<Stock> filtres = tousLesStocks.stream().filter(s -> {
            String nom = s.getFournisseur() != null ? s.getFournisseur().toLowerCase() : "";
            boolean matchNom = recherche.isEmpty() || nom.contains(recherche);
            boolean matchStock = switch (filtre) {
                case "✅ En stock"       -> s.getQuantite() > s.getSeuilAlerte();
                case "⚠️ Stock faible"  -> s.getQuantite() > 0 && s.getQuantite() <= s.getSeuilAlerte();
                case "❌ Rupture de stock" -> s.getQuantite() == 0;
                default -> true;
            };
            return matchNom && matchStock;
        }).collect(Collectors.toList());

        afficherProduits(filtres);
    }

    private void afficherProduits(List<Stock> stocks) {
        grilleProduits.getChildren().clear();
        if (stocks.isEmpty()) {
            lbMessage.setText("Aucun médicament trouvé.");
            return;
        }
        lbMessage.setText("");
        for (int i = 0; i < stocks.size(); i++) {
            grilleProduits.getChildren().add(creerCarteProduit(stocks.get(i), i));
        }
    }

    private VBox creerCarteProduit(Stock stock, int index) {
        String couleur = COULEURS[index % COULEURS.length];
        String emoji   = EMOJIS[index % EMOJIS.length];
        String nom     = stock.getFournisseur() != null ? stock.getFournisseur() : "Médicament";

        // Couleur badge stock
        int qte = stock.getQuantite();
        String badgeColor, badgeText;
        if (qte == 0) {
            badgeColor = "#f44336"; badgeText = "Rupture";
        } else if (qte <= stock.getSeuilAlerte()) {
            badgeColor = "#ff9800"; badgeText = "Stock faible";
        } else {
            badgeColor = "#4caf50"; badgeText = "En stock";
        }

        // ── Carte principale ──────────────────────────────────────────────────
        VBox carte = new VBox(8);
        carte.setPrefWidth(210);
        carte.setPadding(new Insets(0, 0, 12, 0));
        carte.setStyle(
                "-fx-background-color:white;" +
                        "-fx-border-color:#f0f0f0;" +
                        "-fx-border-width:1;" +
                        "-fx-border-radius:14;" +
                        "-fx-background-radius:14;" +
                        "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.08),10,0,0,3);"
        );

        // Image / icône du produit
        VBox imgBox = new VBox();
        imgBox.setAlignment(Pos.CENTER);
        imgBox.setPrefHeight(130);
        imgBox.setStyle("-fx-background-color:" + couleur + ";-fx-background-radius:14 14 0 0;");

        // Essayer de charger une image, sinon afficher emoji
        boolean imageChargee = false;
        String[] nomsSuffixes = {
                nom.toLowerCase().replace(" ", "_") + ".png",
                nom.toLowerCase().replace(" ", "_") + ".jpg",
                nom.toLowerCase() + ".png"
        };
        for (String imgNom : nomsSuffixes) {
            try {
                InputStream is = getClass().getResourceAsStream("/images/" + imgNom);
                if (is != null) {
                    ImageView iv = new ImageView(new Image(is));
                    iv.setFitWidth(90);
                    iv.setFitHeight(90);
                    iv.setPreserveRatio(true);
                    imgBox.getChildren().add(iv);
                    imageChargee = true;
                    break;
                }
            } catch (Exception ignored) {}
        }

        if (!imageChargee) {
            // Emoji grand
            Label emojiLabel = new Label(emoji);
            emojiLabel.setFont(Font.font("System", 54));
            imgBox.getChildren().add(emojiLabel);
        }

        // Badge stock (en haut à droite de l'image)
        StackPane imgStack = new StackPane(imgBox);
        Label badge = new Label(badgeText);
        badge.setStyle(
                "-fx-background-color:" + badgeColor + ";" +
                        "-fx-text-fill:white;" +
                        "-fx-font-size:10px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:3 8;" +
                        "-fx-background-radius:10;"
        );
        StackPane.setAlignment(badge, Pos.TOP_RIGHT);
        StackPane.setMargin(badge, new Insets(8, 8, 0, 0));
        imgStack.getChildren().add(badge);

        // Infos texte
        VBox infos = new VBox(4);
        infos.setPadding(new Insets(4, 12, 0, 12));

        Label lbNom = new Label(nom);
        lbNom.setFont(FontWeight.BOLD != null ? Font.font("System", FontWeight.BOLD, 13) : Font.font(13));
        lbNom.setWrapText(true);
        lbNom.setStyle("-fx-text-fill:#333;-fx-font-weight:bold;");

        Label lbLot = new Label("📦 Lot : " + (stock.getNumeroLot() != null ? stock.getNumeroLot() : "—"));
        lbLot.setStyle("-fx-font-size:11px;-fx-text-fill:#888;");

        Label lbQte = new Label("🗃 Disponible : " + qte + " unités");
        lbQte.setStyle("-fx-font-size:11px;-fx-text-fill:" + (qte <= stock.getSeuilAlerte() ? "#e65100" : "#555") + ";");

        Label lbPrix = new Label(String.format("%.2f DT", stock.getPrixUnitaire()));
        lbPrix.setFont(Font.font("System", FontWeight.BOLD, 18));
        lbPrix.setStyle("-fx-text-fill:#e91e8c;-fx-font-weight:bold;");

        infos.getChildren().addAll(lbNom, lbLot, lbQte, lbPrix);

        // Bouton commander
        Button btnCommander = new Button(qte == 0 ? "Indisponible" : "🛒 Commander");
        btnCommander.setMaxWidth(Double.MAX_VALUE);
        btnCommander.setDisable(qte == 0);
        btnCommander.setStyle(
                "-fx-background-color:" + (qte == 0 ? "#bdbdbd" : "#e91e8c") + ";" +
                        "-fx-text-fill:white;" +
                        "-fx-font-weight:bold;" +
                        "-fx-font-size:12px;" +
                        "-fx-background-radius:0 0 14 14;" +
                        "-fx-padding:9 0;"
        );
        VBox.setMargin(btnCommander, new Insets(4, 0, 0, 0));

        final Stock stockFinal = stock;
        btnCommander.setOnAction(e -> ouvrirCommandeAvecStock(stockFinal));

        carte.getChildren().addAll(imgStack, infos, btnCommander);
        return carte;
    }

    private void ouvrirCommandeAvecStock(Stock stock) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommandeView.fxml"));
            Parent root = loader.load();
            CommandeController ctrl = loader.getController();
            ctrl.preselectionnerStock(stock);
            grilleProduits.getScene().setRoot(root);
        } catch (IOException ex) {
            lbMessage.setText("❌ Erreur : " + ex.getMessage());
        }
    }

    // ─── NAVIGATION ───────────────────────────────────────────────────────────
    @FXML public void allerMedicaments(ActionEvent e) { naviguer("/MainView.fxml"); }
    @FXML public void allerCategories(ActionEvent e)  { naviguer("/CategorieView.fxml"); }
    @FXML public void allerFabricants(ActionEvent e)  { naviguer("/FabricantView.fxml"); }
    @FXML public void allerFormes(ActionEvent e)      { naviguer("/FormeView.fxml"); }
    @FXML public void allerStock(ActionEvent e)       { naviguer("/StockView.fxml"); }
    @FXML public void allerAlertes(ActionEvent e)     { naviguer("/AlerteView.fxml"); }
    @FXML public void allerChatbot(ActionEvent e)     { naviguer("/ChatbotView.fxml"); }
    @FXML public void allerCommande(ActionEvent e)    { naviguer("/CommandeView.fxml"); }
    @FXML public void allerPaiement(ActionEvent e)    { naviguer("/PaiementView.fxml"); }
    @FXML public void allerShop(ActionEvent e)        { /* déjà ici */ }

    private void naviguer(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            grilleProduits.getScene().setRoot(root);
        } catch (IOException ex) { System.out.println(ex.getMessage()); }
    }
}