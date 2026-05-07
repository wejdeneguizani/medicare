package com.medical.controllers;

import com.medical.model.RappelMedicament;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AlerteController {

    @FXML private TextField tfMedicament;
    @FXML private TextField tfHeure;
    @FXML private ComboBox<String> cbFrequence;
    @FXML private TextField tfNote;
    @FXML private TableView<RappelMedicament> tableView;
    @FXML private TableColumn<RappelMedicament, Integer> colId;
    @FXML private TableColumn<RappelMedicament, String>  colMedicament;
    @FXML private TableColumn<RappelMedicament, String>  colHeure;
    @FXML private TableColumn<RappelMedicament, String>  colFrequence;
    @FXML private TableColumn<RappelMedicament, String>  colNote;
    @FXML private TableColumn<RappelMedicament, String>  colStatut;
    @FXML private Label lbMessage;
    @FXML private Label lbNotif;
    @FXML private HBox  bannerNotif;

    private ObservableList<RappelMedicament> rappels = FXCollections.observableArrayList();
    private int idCounter = 1;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMedicament.setCellValueFactory(new PropertyValueFactory<>("medicament"));
        colHeure.setCellValueFactory(new PropertyValueFactory<>("heure"));
        colFrequence.setCellValueFactory(new PropertyValueFactory<>("frequence"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        tableView.setItems(rappels);

        cbFrequence.setItems(FXCollections.observableArrayList(
                "Une fois par jour",
                "Deux fois par jour",
                "Trois fois par jour",
                "Toutes les 8 heures",
                "Toutes les 12 heures",
                "Une fois par semaine"
        ));

        // Timer qui vérifie chaque minute si une alerte doit se déclencher
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(30), e -> verifierAlertes()));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @FXML
    public void ajouterAlerte(ActionEvent e) {
        String med  = tfMedicament.getText().trim();
        String heure = tfHeure.getText().trim();
        String freq  = cbFrequence.getValue();

        if (med.isEmpty() || heure.isEmpty() || freq == null) {
            lbMessage.setText("⚠️ Remplissez tous les champs obligatoires !");
            lbMessage.setStyle("-fx-text-fill: #e65100; -fx-font-weight: bold;");
            return;
        }

        if (!heure.matches("\\d{2}:\\d{2}")) {
            lbMessage.setText("⚠️ Format heure invalide ! Utilisez HH:mm (ex: 08:30)");
            lbMessage.setStyle("-fx-text-fill: #e65100; -fx-font-weight: bold;");
            return;
        }

        RappelMedicament r = new RappelMedicament(med, heure, freq, tfNote.getText().trim());
        r.setId(idCounter++);
        rappels.add(r);

        lbMessage.setText("✅ Rappel ajouté pour " + med + " à " + heure);
        lbMessage.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        tfMedicament.clear(); tfHeure.clear(); tfNote.clear(); cbFrequence.setValue(null);
    }

    @FXML
    public void supprimerAlerte(ActionEvent e) {
        RappelMedicament sel = tableView.getSelectionModel().getSelectedItem();
        if (sel != null) {
            rappels.remove(sel);
            lbMessage.setText("✅ Rappel supprimé !");
            lbMessage.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        } else {
            lbMessage.setText("⚠️ Sélectionnez un rappel !");
            lbMessage.setStyle("-fx-text-fill: #e65100; -fx-font-weight: bold;");
        }
    }

    @FXML
    public void marquerPris(ActionEvent e) {
        RappelMedicament sel = tableView.getSelectionModel().getSelectedItem();
        if (sel != null) {
            sel.setStatut("✅ Pris");
            tableView.refresh();
            lbMessage.setText("✅ " + sel.getMedicament() + " marqué comme pris !");
            lbMessage.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        } else {
            lbMessage.setText("⚠️ Sélectionnez un rappel !");
            lbMessage.setStyle("-fx-text-fill: #e65100; -fx-font-weight: bold;");
        }
    }

    private void verifierAlertes() {
        String maintenant = LocalTime.now().format(FMT);
        for (RappelMedicament r : rappels) {
            if (r.getHeure().equals(maintenant) && r.getStatut().equals("En attente")) {
                afficherNotification("⏰ Il est " + maintenant + " — Prenez votre médicament : " + r.getMedicament()
                        + (r.getNote().isEmpty() ? "" : " (" + r.getNote() + ")"));
                r.setStatut("🔔 Rappelé");
                tableView.refresh();
            }
        }
    }

    private void afficherNotification(String message) {
        lbNotif.setText(message);
        bannerNotif.setVisible(true);
        bannerNotif.setManaged(true);
        // Cache la bannière après 10 secondes
        Timeline hide = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            bannerNotif.setVisible(false);
            bannerNotif.setManaged(false);
        }));
        hide.play();
    }

    @FXML public void allerMedicaments(ActionEvent e) { naviguer("/MainView.fxml"); }
    @FXML public void allerCategories(ActionEvent e)  { naviguer("/CategorieView.fxml"); }
    @FXML public void allerFabricants(ActionEvent e)  { naviguer("/FabricantView.fxml"); }
    @FXML public void allerFormes(ActionEvent e)      { naviguer("/FormeView.fxml"); }
    @FXML public void allerStock(ActionEvent e)       { naviguer("/StockView.fxml"); }
    @FXML public void allerAlertes(ActionEvent e)     { /* déjà ici */ }
    @FXML public void allerChatbot(ActionEvent e)     { naviguer("/ChatbotView.fxml"); }

    private void naviguer(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            tableView.getScene().setRoot(root);
        } catch (IOException ex) { System.out.println(ex.getMessage()); }
    }
}