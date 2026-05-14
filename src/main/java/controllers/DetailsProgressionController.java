package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Progression;
import services.ServiceProgression;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DetailsProgressionController {

    @FXML private TextField        tfIdObjectif;
    @FXML private DatePicker       dpDateMesure;
    @FXML private TextField        tfValeurActuelle;
    @FXML private TextField        tfValeurCible;
    @FXML private ComboBox<String> cbHumeur;
    @FXML private TextField        tfNotes;

    @FXML private TableView<Progression>            tableView;
    @FXML private TableColumn<Progression, Integer> colId;
    @FXML private TableColumn<Progression, Integer> colIdObjectif;
    @FXML private TableColumn<Progression, String>  colDateMesure;
    @FXML private TableColumn<Progression, Float>   colValeurActuelle;
    @FXML private TableColumn<Progression, Float>   colValeurCible;
    @FXML private TableColumn<Progression, String>  colHumeur;

    @FXML private Label lbMessage;

    private final ServiceProgression service = new ServiceProgression();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_progression"));
        colIdObjectif.setCellValueFactory(new PropertyValueFactory<>("id_objectif"));
        colDateMesure.setCellValueFactory(new PropertyValueFactory<>("date_mesure"));
        colValeurActuelle.setCellValueFactory(new PropertyValueFactory<>("valeur_actuelle"));
        colValeurCible.setCellValueFactory(new PropertyValueFactory<>("valeur_cible"));
        colHumeur.setCellValueFactory(new PropertyValueFactory<>("humeur"));

        cbHumeur.setItems(FXCollections.observableArrayList(
                "😊 Très bien", "🙂 Bien", "😐 Neutre", "😔 Mal", "😞 Très mal"
        ));
        cbHumeur.setValue("🙂 Bien");

        refreshTable();

        tableView.setOnMouseClicked(e -> {
            Progression s = tableView.getSelectionModel().getSelectedItem();
            if (s != null) {
                tfIdObjectif.setText(String.valueOf(s.getId_objectif()));
                dpDateMesure.setValue(s.getDate_mesure());
                tfValeurActuelle.setText(String.valueOf(s.getValeur_actuelle()));
                tfValeurCible.setText(String.valueOf(s.getValeur_cible()));
                cbHumeur.setValue(s.getHumeur());
                tfNotes.setText(s.getNotes());
            }
        });
    }

    @FXML public void ajouterProgression(ActionEvent e) {
        try {
            service.add(buildFromForm());
            lbMessage.setText("✓ Progression ajoutée !");
            viderFormulaire(null);
            refreshTable();
        } catch (Exception ex) {
            lbMessage.setText("Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML public void modifierProgression(ActionEvent e) {
        Progression s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) { lbMessage.setText("Sélectionnez une progression."); return; }
        try {
            Progression p = buildFromForm();
            p.setId_progression(s.getId_progression());
            service.update(p);
            lbMessage.setText("✓ Progression modifiée !");
            viderFormulaire(null);
            refreshTable();
        } catch (Exception ex) {
            lbMessage.setText("Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML public void supprimerProgression(ActionEvent e) {
        Progression s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) { lbMessage.setText("Sélectionnez une progression."); return; }
        service.delete(s);
        lbMessage.setText("✓ Progression supprimée.");
        viderFormulaire(null);
        refreshTable();
    }

    @FXML public void voirDetails(ActionEvent e) {
        Progression s = tableView.getSelectionModel().getSelectedItem();
        if (s == null) { lbMessage.setText("Sélectionnez une progression."); return; }
        DetailsProgressionController.progression = s;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/DetailsProgression.fxml"));
            tfIdObjectif.getScene().setRoot(root);
        } catch (IOException ex) {
            lbMessage.setText("Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML public void viderFormulaire(ActionEvent e) {
        tfIdObjectif.clear();
        tfValeurActuelle.clear();
        tfValeurCible.clear();
        tfNotes.clear();
        dpDateMesure.setValue(null);
        cbHumeur.setValue("🙂 Bien");
        lbMessage.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    private void refreshTable() {
        List<Progression> list = service.getAll();
        tableView.setItems(FXCollections.observableArrayList(list));
    }

    private Progression buildFromForm() {
        Progression p = new Progression();
        p.setId_objectif(Integer.parseInt(tfIdObjectif.getText()));
        p.setDate_mesure(dpDateMesure.getValue() != null ? dpDateMesure.getValue() : LocalDate.now());
        p.setValeur_actuelle(Float.parseFloat(tfValeurActuelle.getText()));
        p.setValeur_cible(Float.parseFloat(tfValeurCible.getText()));
        p.setHumeur(cbHumeur.getValue());
        p.setNotes(tfNotes.getText());
        return p;
    }
}