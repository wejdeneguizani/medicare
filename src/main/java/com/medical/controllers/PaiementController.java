package com.medical.controllers;

import com.medical.model.Commande;
import com.medical.services.EmailService;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaiementController {

    // ─── Labels récapitulatif ─────────────────────────────────────────────────
    @FXML private Label lbTitreRecu;
    @FXML private Label lbNumero;
    @FXML private Label lbType;
    @FXML private Label lbNom;
    @FXML private Label lbEmail;
    @FXML private Label lbMedicament;
    @FXML private Label lbQuantite;
    @FXML private Label lbPrix;
    @FXML private Label lbMode;
    @FXML private Label lbMontantTotal;

    // ─── Boutons ─────────────────────────────────────────────────────────────
    @FXML private Button btnEmail;
    @FXML private Button btnImprimer;
    @FXML private Label  lbMessage;

    private Commande          commandeEnCours;
    private String            cheminPdf;
    private final EmailService emailService = new EmailService();
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        btnEmail.setDisable(true);
        btnImprimer.setDisable(true);
    }

    // ─── RÉCEPTION DE LA COMMANDE DEPUIS CommandeController ──────────────────
    public void recevoirCommande(Commande c) {
        this.commandeEnCours = c;
        boolean estClient = "CLIENT".equals(c.getTypeCommande());

        lbTitreRecu.setText(estClient ? "REÇU DE PAIEMENT" : "BON DE RÉAPPROVISIONNEMENT");
        lbNumero.setText("N° " + String.format("%05d", c.getIdCommande()) + "   —   " + SDF.format(new Date()));
        lbType.setText(estClient ? "🛒 Vente client" : "📦 Réapprovisionnement");
        lbType.setStyle(estClient
                ? "-fx-text-fill:#1565c0;-fx-font-weight:bold;"
                : "-fx-text-fill:#e65100;-fx-font-weight:bold;");
        lbNom.setText((estClient ? "Client : " : "Fournisseur : ") + c.getNomClient());
        lbEmail.setText(estClient ? "Email : " + c.getEmailClient() : "");
        lbMedicament.setText("Médicament : " + nvl(c.getNomMedicament()));
        lbQuantite.setText("Quantité : " + c.getQuantite() + " unité(s)");
        lbPrix.setText("Prix unitaire : " + String.format("%.2f DT", c.getPrixUnitaire()));
        lbMode.setText("Mode : " + nvl(c.getModePaiement()));
        lbMontantTotal.setText(String.format("%.2f DT", c.getMontantTotal()));
    }

    // ─── GÉNÉRER PDF ──────────────────────────────────────────────────────────
    @FXML
    public void genererPdf(ActionEvent e) {
        if (commandeEnCours == null) { msg("⚠️ Aucune commande.", "#e65100"); return; }

        FileChooser fc = new FileChooser();
        fc.setTitle("Enregistrer le reçu");
        fc.setInitialFileName("recu_" + commandeEnCours.getIdCommande() + ".pdf");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File f = fc.showSaveDialog(lbMessage.getScene().getWindow());
        if (f == null) return;

        try {
            construirePdf(commandeEnCours, f.getAbsolutePath());
            cheminPdf = f.getAbsolutePath();
            btnEmail.setDisable("CLIENT".equals(commandeEnCours.getTypeCommande()) ? false : true);
            btnImprimer.setDisable(false);
            msg("✅ PDF généré : " + f.getName(), "#2e7d32");
        } catch (Exception ex) {
            msg("❌ Erreur PDF : " + ex.getMessage(), "#c62828");
            ex.printStackTrace();
        }
    }

    // ─── IMPRIMER ─────────────────────────────────────────────────────────────
    @FXML
    public void imprimerRecu(ActionEvent e) {
        if (cheminPdf == null) { msg("⚠️ Générez d'abord le PDF.", "#e65100"); return; }
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().print(new File(cheminPdf));
                msg("🖨 Impression lancée !", "#2e7d32");
            } else {
                msg("⚠️ Impression non supportée sur cet OS.", "#e65100");
            }
        } catch (Exception ex) {
            msg("❌ Erreur impression : " + ex.getMessage(), "#c62828");
        }
    }

    // ─── ENVOYER EMAIL ────────────────────────────────────────────────────────
    @FXML
    public void envoyerEmail(ActionEvent e) {
        if (commandeEnCours == null) { msg("⚠️ Aucune commande.", "#e65100"); return; }
        msg("📧 Envoi en cours...", "#1565c0");
        new Thread(() -> {
            boolean ok = emailService.envoyerRecu(commandeEnCours, cheminPdf);
            javafx.application.Platform.runLater(() ->
                    msg(ok ? "✅ Email envoyé à : " + commandeEnCours.getEmailClient()
                                    : "❌ Échec envoi. Vérifiez GMAIL_USER/GMAIL_PASS dans EmailService.java",
                            ok ? "#2e7d32" : "#c62828"));
        }).start();
    }

    // ─── CONSTRUCTION PDF ─────────────────────────────────────────────────────
    private void construirePdf(Commande c, String chemin) throws Exception {
        boolean estClient = "CLIENT".equals(c.getTypeCommande());
        Document doc = new Document(new PdfDocument(new PdfWriter(chemin)));

        // En-tête
        doc.add(new Paragraph("Medicare+").setFontSize(30).setBold()
                .setFontColor(ColorConstants.MAGENTA).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph("Votre santé, notre priorité").setFontSize(11).setItalic()
                .setFontColor(ColorConstants.GRAY).setTextAlignment(TextAlignment.CENTER));
        doc.add(separateur());

        // Titre
        String titrePdf = estClient ? "REÇU DE PAIEMENT" : "BON DE RÉAPPROVISIONNEMENT";
        doc.add(new Paragraph(titrePdf).setFontSize(20).setBold()
                .setTextAlignment(TextAlignment.CENTER).setMarginTop(8));
        doc.add(new Paragraph("N° " + String.format("%05d", c.getIdCommande()) + "   —   " + SDF.format(new Date()))
                .setFontSize(10).setFontColor(ColorConstants.GRAY).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph(" "));

        // Tableau principal
        Table t = new Table(UnitValue.createPercentArray(new float[]{38, 62})).useAllAvailableWidth();
        t.addHeaderCell(cellTete("Information")); t.addHeaderCell(cellTete("Valeur"));

        if (estClient) {
            ligne(t, "Client",          nvl(c.getNomClient()));
            ligne(t, "Email",           nvl(c.getEmailClient()));
            if (c.getTelephoneClient() != null && !c.getTelephoneClient().isEmpty())
                ligne(t, "Téléphone",   c.getTelephoneClient());
        } else {
            ligne(t, "Fournisseur",     nvl(c.getNomClient()));
        }
        ligne(t, "Médicament",          nvl(c.getNomMedicament()));
        if (c.getNumeroLot() != null && !c.getNumeroLot().isEmpty())
            ligne(t, "N° de lot",       c.getNumeroLot());
        ligne(t, estClient ? "Qté vendue" : "Qté commandée",
                c.getQuantite() + " unité(s)");
        ligne(t, "Prix unitaire",       String.format("%.2f DT", c.getPrixUnitaire()));
        ligne(t, estClient ? "Mode de paiement" : "Mode de règlement",
                nvl(c.getModePaiement()));
        ligne(t, "Statut",              "✅ Confirmé");
        doc.add(t);
        doc.add(new Paragraph(" "));

        // Montant total
        Table tm = new Table(UnitValue.createPercentArray(new float[]{60, 40})).useAllAvailableWidth();
        tm.addCell(new Cell().add(new Paragraph("MONTANT TOTAL").setBold().setFontSize(14)
                .setFontColor(ColorConstants.WHITE)).setBackgroundColor(ColorConstants.DARK_GRAY).setPadding(10));
        tm.addCell(new Cell().add(new Paragraph(String.format("%.2f DT", c.getMontantTotal()))
                        .setBold().setFontSize(18).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ColorConstants.MAGENTA).setTextAlignment(TextAlignment.RIGHT).setPadding(10));
        doc.add(tm);
        doc.add(new Paragraph(" "));
        doc.add(separateur());
        doc.add(new Paragraph("Merci de votre confiance — Medicare+").setItalic()
                .setTextAlignment(TextAlignment.CENTER).setFontColor(ColorConstants.GRAY));
        doc.add(new Paragraph("Document généré le " + SDF.format(new Date())).setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER).setFontColor(ColorConstants.LIGHT_GRAY));
        doc.close();
    }

    private Paragraph separateur() {
        return new Paragraph("────────────────────────────────────────────────")
                .setTextAlignment(TextAlignment.CENTER).setFontColor(ColorConstants.LIGHT_GRAY);
    }
    private Cell cellTete(String txt) {
        return new Cell().add(new Paragraph(txt).setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ColorConstants.MAGENTA).setTextAlignment(TextAlignment.CENTER).setPadding(7);
    }
    private void ligne(Table t, String label, String val) {
        t.addCell(new Cell().add(new Paragraph(label).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY).setPadding(6));
        t.addCell(new Cell().add(new Paragraph(val)).setPadding(6));
    }
    private String nvl(String s) { return s != null && !s.isEmpty() ? s : "—"; }
    private void msg(String txt, String color) {
        lbMessage.setText(txt);
        lbMessage.setStyle("-fx-text-fill:" + color + ";-fx-font-weight:bold;-fx-font-size:13px;");
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
    @FXML public void allerPaiement(ActionEvent e)    { /* déjà ici */ }

    private void naviguer(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            lbMessage.getScene().setRoot(root);
        } catch (IOException ex) { System.out.println(ex.getMessage()); }
    }
}