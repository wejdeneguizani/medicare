package com.medical.controllers;

import com.medical.services.EmailService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

        import java.io.IOException;

public class ParametresController {

    @FXML private TextField     tfEmailExp;
    @FXML private PasswordField pfMotDePasse;
    @FXML private TextField     tfMotDePasseVisible;
    @FXML private CheckBox      cbAfficher;
    @FXML private Label         lbMessage;

    @FXML
    public void initialize() {
        // Charger les paramètres sauvegardés
        tfEmailExp.setText(EmailService.getGmailUser());
        pfMotDePasse.setText(EmailService.getGmailPass());
        tfMotDePasseVisible.setText(EmailService.getGmailPass());
    }

    @FXML
    public void toggleAfficher(ActionEvent e) {
        boolean visible = cbAfficher.isSelected();
        pfMotDePasse.setVisible(!visible);
        pfMotDePasse.setManaged(!visible);
        tfMotDePasseVisible.setVisible(visible);
        tfMotDePasseVisible.setManaged(visible);
        if (visible) tfMotDePasseVisible.setText(pfMotDePasse.getText());
        else pfMotDePasse.setText(tfMotDePasseVisible.getText());
    }

    @FXML
    public void sauvegarder(ActionEvent e) {
        String email = tfEmailExp.getText().trim();
        String pass  = cbAfficher.isSelected()
                ? tfMotDePasseVisible.getText().trim()
                : pfMotDePasse.getText().trim();

        if (email.isEmpty() || !email.contains("@")) {
            msg("⚠️ Email invalide !", "#e65100"); return;
        }
        if (pass.isEmpty()) {
            msg("⚠️ Mot de passe vide !", "#e65100"); return;
        }

        EmailService.setCredentials(email, pass);
        msg("✅ Paramètres sauvegardés !", "#2e7d32");
    }

    @FXML
    public void testerEmail(ActionEvent e) {
        String email = tfEmailExp.getText().trim();
        String pass  = cbAfficher.isSelected()
                ? tfMotDePasseVisible.getText().trim()
                : pfMotDePasse.getText().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            msg("⚠️ Remplissez email et mot de passe d'abord !", "#e65100"); return;
        }

        EmailService.setCredentials(email, pass);
        msg("📧 Test en cours...", "#1565c0");

        new Thread(() -> {
            boolean ok = EmailService.testerConnexion(email);
            javafx.application.Platform.runLater(() ->
                    msg(ok ? "✅ Connexion Gmail réussie ! Les emails fonctionnent."
                                    : "❌ Échec — vérifiez email et mot de passe d'application.",
                            ok ? "#2e7d32" : "#c62828")
            );
        }).start();
    }

    private void msg(String t, String c) {
        lbMessage.setText(t);
        lbMessage.setStyle("-fx-text-fill:" + c + ";-fx-font-weight:bold;");
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
    @FXML public void allerParametres(ActionEvent e)  { /* déjà ici */ }

    private void naviguer(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            lbMessage.getScene().setRoot(root);
        } catch (IOException ex) { System.out.println(ex.getMessage()); }
    }
}
