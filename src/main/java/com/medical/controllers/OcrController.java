package com.medical.controllers;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OcrController {

    @FXML private ComboBox<String> cbLangue;
    @FXML private ImageView imagePreview;
    @FXML private TextArea txtResultat;
    @FXML private Label lblFichier;
    @FXML private Label lblStatus;
    @FXML private ProgressIndicator progress;

    private File fichierSelectionne;

    @FXML
    public void initialize() {
        cbLangue.setItems(FXCollections.observableArrayList("fra", "eng"));
        cbLangue.setValue("fra");
        progress.setVisible(false);
    }

    @FXML
    public void choisirFichier(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choisir une image ou un PDF");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images et PDF", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif", "*.tif", "*.tiff", "*.pdf"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif", "*.tif", "*.tiff"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );

        File fichier = chooser.showOpenDialog(txtResultat.getScene().getWindow());
        if (fichier == null) {
            return;
        }

        fichierSelectionne = fichier;
        lblFichier.setText(fichier.getAbsolutePath());
        txtResultat.clear();
        message("Fichier charge. Cliquez sur Lire OCR.", "#1565c0");

        if (estImage(fichier)) {
            imagePreview.setImage(new Image(fichier.toURI().toString()));
        } else {
            imagePreview.setImage(null);
        }
    }

    @FXML
    public void lancerOcr(ActionEvent event) {
        if (fichierSelectionne == null) {
            message("Choisissez un fichier avant de lancer OCR.", "#e65100");
            return;
        }

        progress.setVisible(true);
        message("Lecture OCR en cours...", "#1565c0");

        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                ITesseract tesseract = new Tesseract();
                tesseract.setLanguage(cbLangue.getValue());
                configurerTessdata(tesseract);
                return tesseract.doOCR(fichierSelectionne);
            }
        };

        task.setOnSucceeded(e -> {
            progress.setVisible(false);
            txtResultat.setText(task.getValue());
            message("OCR termine.", "#2e7d32");
        });

        task.setOnFailed(e -> {
            progress.setVisible(false);
            Throwable erreur = task.getException();
            String detail = erreur == null ? "Erreur inconnue" : erreur.getMessage();
            if (detail != null && detail.contains("Specified language data does not exist")) {
                message("Langue OCR introuvable : installez " + cbLangue.getValue() + ".traineddata dans tessdata.", "#c62828");
            } else {
                message("Erreur OCR : " + detail, "#c62828");
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void vider(ActionEvent event) {
        fichierSelectionne = null;
        imagePreview.setImage(null);
        txtResultat.clear();
        lblFichier.setText("Aucun fichier choisi");
        message("", "#1565c0");
    }

    @FXML
    public void retourAssurances(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainView.fxml"));
            root.setOpacity(0);
            txtResultat.getScene().setRoot(root);
            FadeTransition fade = new FadeTransition(Duration.millis(220), root);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        } catch (IOException ex) {
            message("Impossible de revenir a l'ecran assurances.", "#c62828");
        }
    }

    @FXML
    public void creerDemande(ActionEvent event) {
        String texte = txtResultat.getText();
        if (texte == null || texte.isBlank()) {
            message("Lancez OCR avant de creer une demande.", "#e65100");
            return;
        }

        String type = extraireTypeDepense(texte);
        String montant = extraireMontant(texte);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RemboursementView.fxml"));
            Parent root = loader.load();
            RemboursementController controller = loader.getController();
            controller.preRemplirDepuisOcr(type, montant, texte);
            root.setOpacity(0);
            txtResultat.getScene().setRoot(root);
            FadeTransition fade = new FadeTransition(Duration.millis(220), root);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        } catch (IOException ex) {
            message("Impossible d'ouvrir les remboursements.", "#c62828");
        }
    }

    private void configurerTessdata(ITesseract tesseract) {
        String env = System.getenv("TESSDATA_PREFIX");
        if (env != null && !env.isBlank()) {
            tesseract.setDatapath(env);
            return;
        }

        File installationWindows = new File("C:\\Program Files\\Tesseract-OCR\\tessdata");
        if (installationWindows.exists()) {
            tesseract.setDatapath(installationWindows.getAbsolutePath());
            return;
        }

        File dossierLocal = new File("tessdata");
        if (dossierLocal.exists()) {
            tesseract.setDatapath(dossierLocal.getAbsolutePath());
        }
    }

    private boolean estImage(File fichier) {
        String nom = fichier.getName().toLowerCase();
        return nom.endsWith(".png")
                || nom.endsWith(".jpg")
                || nom.endsWith(".jpeg")
                || nom.endsWith(".bmp")
                || nom.endsWith(".gif")
                || nom.endsWith(".tif")
                || nom.endsWith(".tiff");
    }

    private String extraireTypeDepense(String texte) {
        String min = texte.toLowerCase();
        if (min.contains("consultation")) return "Consultation";
        if (min.contains("medicament") || min.contains("médicament")) return "Medicament";
        if (min.contains("analyse")) return "Analyse";
        if (min.contains("radiologie") || min.contains("radio")) return "Radiologie";
        if (min.contains("hospitalisation")) return "Hospitalisation";
        return "Consultation";
    }

    private String extraireMontant(String texte) {
        Pattern pattern = Pattern.compile("(?i)(montant total|montant depense|montant dépensé)\\s*:?\\s*([0-9]+(?:[,.][0-9]+)?)");
        Matcher matcher = pattern.matcher(texte);
        if (matcher.find()) {
            return matcher.group(2).replace(',', '.');
        }
        return "";
    }

    private void message(String texte, String couleur) {
        lblStatus.setText(texte);
        lblStatus.setStyle("-fx-text-fill: " + couleur + "; -fx-background-color: white; -fx-border-color: " + couleur + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; -fx-padding: 8 12;");
        FadeTransition fade = new FadeTransition(Duration.millis(180), lblStatus);
        fade.setFromValue(0.25);
        fade.setToValue(1);
        fade.play();
    }
}
