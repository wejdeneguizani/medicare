package com.medical.controllers;

import com.medical.model.Assurance;
import com.medical.services.AssuranceService;
import com.medical.services.RemboursementService;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class AssuranceController {

    @FXML private TextField tfId;
    @FXML private TextField tfNumero;
    @FXML private ComboBox<String> cbType;
    @FXML private TextField tfAssureur;
    @FXML private DatePicker dpDebut;
    @FXML private DatePicker dpFin;
    @FXML private TextField tfPlafond;
    @FXML private TextField tfTaux;
    @FXML private ComboBox<String> cbStatut;
    @FXML private TextField tfUser;

    @FXML private TableView<Assurance> tableView;
    @FXML private TableColumn<Assurance, Integer> colId;
    @FXML private TableColumn<Assurance, String> colNumero;
    @FXML private TableColumn<Assurance, String> colType;
    @FXML private TableColumn<Assurance, String> colAssureur;
    @FXML private TableColumn<Assurance, String> colDebut;
    @FXML private TableColumn<Assurance, String> colFin;
    @FXML private TableColumn<Assurance, Double> colPlafond;
    @FXML private TableColumn<Assurance, Double> colTaux;
    @FXML private TableColumn<Assurance, String> colStatut;
    @FXML private Label lbMessage;
    @FXML private Label lblActives;
    @FXML private Label lblExpirees;
    @FXML private Label lblDemandesAttente;
    @FXML private Label lblTotalRembourse;

    private AssuranceService service = new AssuranceService();
    private RemboursementService remboursementService = new RemboursementService();

    @FXML
    public void initialize() {
        cbType.setItems(FXCollections.observableArrayList("Publique", "Privee", "Complementaire"));
        cbStatut.setItems(FXCollections.observableArrayList("Active", "Expiree", "Suspendue"));

        colId.setCellValueFactory(new PropertyValueFactory<>("idAssurance"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroContrat"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeAssurance"));
        colAssureur.setCellValueFactory(new PropertyValueFactory<>("nomAssureur"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colPlafond.setCellValueFactory(new PropertyValueFactory<>("plafondAnnuel"));
        colTaux.setCellValueFactory(new PropertyValueFactory<>("tauxBaseRemboursement"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        chargerTable();

        tableView.setOnMouseClicked(e -> remplirFormulaire());
        tableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Assurance assurance, boolean empty) {
                super.updateItem(assurance, empty);
                if (empty || assurance == null || assurance.getStatut() == null) {
                    setStyle("");
                } else if ("Expiree".equalsIgnoreCase(assurance.getStatut())) {
                    setStyle("-fx-background-color: #ffebee;");
                } else if ("Active".equalsIgnoreCase(assurance.getStatut())) {
                    setStyle("-fx-background-color: #e8f5e9;");
                } else {
                    setStyle("-fx-background-color: #fff8e1;");
                }
            }
        });
    }

    private void chargerTable() {
        List<Assurance> assurances = service.getTous();
        ObservableList<Assurance> liste = FXCollections.observableArrayList(assurances);
        tableView.setItems(liste);
        majStatistiques(assurances);
    }

    private void majStatistiques(List<Assurance> assurances) {
        long actives = assurances.stream().filter(a -> "Active".equalsIgnoreCase(a.getStatut())).count();
        long expirees = assurances.stream().filter(a -> "Expiree".equalsIgnoreCase(a.getStatut())).count();
        lblActives.setText(String.valueOf(actives));
        lblExpirees.setText(String.valueOf(expirees));
        lblDemandesAttente.setText(String.valueOf(remboursementService.getEnAttente().size()));
        lblTotalRembourse.setText(remboursementService.getTotalRembourseValide() + " TND");
    }

    @FXML
    public void ajouterAssurance(ActionEvent e) {
        Assurance a = lireFormulaire(false);
        if (a == null) return;
        boolean ok = service.ajouter(a);
        afficherResultat(ok, "Assurance ajoutée avec succès !", "Erreur lors de l'ajout !");
        if (ok) viderChamps(null);
        chargerTable();
    }

    @FXML
    public void modifierAssurance(ActionEvent e) {
        Assurance a = lireFormulaire(true);
        if (a == null) return;
        boolean ok = service.modifier(a);
        afficherResultat(ok, "Assurance modifiée avec succès !", "Erreur lors de la modification !");
        if (ok) viderChamps(null);
        chargerTable();
    }

    @FXML
    public void supprimerAssurance(ActionEvent e) {
        Assurance selectionne = tableView.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            message("⚠️ Sélectionnez une assurance !", "#e65100");
            return;
        }
        boolean ok = service.supprimer(selectionne.getIdAssurance());
        afficherResultat(ok, "Assurance supprimée !", "Erreur suppression !");
        chargerTable();
        viderChamps(null);
    }

    @FXML
    public void afficherExpirees(ActionEvent e) {
        tableView.setItems(FXCollections.observableArrayList(service.getAssurancesExpirees()));
        message("Contrats expirés affichés.", "#1565c0");
    }

    @FXML
    public void afficherTous(ActionEvent e) {
        chargerTable();
        message("Toutes les assurances affichées.", "#1565c0");
    }

    @FXML
    public void majStatuts(ActionEvent e) {
        boolean ok = service.mettreAJourStatutsExpires();
        afficherResultat(ok, "Statuts expirés mis à jour !", "Erreur mise à jour !");
        chargerTable();
    }

    @FXML
    public void viderChamps(ActionEvent e) {
        tfId.clear(); tfNumero.clear(); cbType.setValue(null); tfAssureur.clear();
        dpDebut.setValue(null); dpFin.setValue(null); tfPlafond.clear(); tfTaux.clear();
        cbStatut.setValue(null); tfUser.clear();
    }

    private Assurance lireFormulaire(boolean withId) {
        try {
            String erreur = champInvalide(withId);
            if (erreur != null) {
                message("Verifiez le champ : " + erreur, "#e65100");
                return null;
            }

            Assurance a = new Assurance();
            if (withId) a.setIdAssurance(Integer.parseInt(tfId.getText()));
            a.setNumeroContrat(tfNumero.getText());
            a.setTypeAssurance(cbType.getValue());
            a.setNomAssureur(tfAssureur.getText());
            a.setDateDebut(Date.valueOf(dpDebut.getValue()));
            a.setDateFin(Date.valueOf(dpFin.getValue()));
            a.setPlafondAnnuel(Double.parseDouble(tfPlafond.getText()));
            a.setTauxBaseRemboursement(Double.parseDouble(tfTaux.getText()));
            a.setStatut(cbStatut.getValue());
            a.setIdUser(Integer.parseInt(tfUser.getText()));
            return a;
        } catch (Exception ex) {
            message("Verifiez les valeurs numeriques et les dates.", "#e65100");
            return null;
        }
    }

    private String champInvalide(boolean withId) {
        if (withId && tfId.getText().isBlank()) return "ID";
        if (tfNumero.getText().isBlank()) return "N contrat";
        if (cbType.getValue() == null) return "Type";
        if (tfAssureur.getText().isBlank()) return "Assureur";
        if (dpDebut.getValue() == null) return "Date debut";
        if (dpFin.getValue() == null) return "Date fin";
        if (tfPlafond.getText().isBlank()) return "Plafond";
        if (tfTaux.getText().isBlank()) return "Taux";
        if (cbStatut.getValue() == null) return "Statut";
        if (tfUser.getText().isBlank()) return "User";
        return null;
    }

    private void remplirFormulaire() {
        Assurance a = tableView.getSelectionModel().getSelectedItem();
        if (a != null) {
            tfId.setText(String.valueOf(a.getIdAssurance()));
            tfNumero.setText(a.getNumeroContrat());
            cbType.setValue(a.getTypeAssurance());
            tfAssureur.setText(a.getNomAssureur());
            if (a.getDateDebut() != null) dpDebut.setValue(a.getDateDebut().toLocalDate());
            if (a.getDateFin() != null) dpFin.setValue(a.getDateFin().toLocalDate());
            tfPlafond.setText(String.valueOf(a.getPlafondAnnuel()));
            tfTaux.setText(String.valueOf(a.getTauxBaseRemboursement()));
            cbStatut.setValue(a.getStatut());
            tfUser.setText(String.valueOf(a.getIdUser()));
        }
    }

    private void afficherResultat(boolean ok, String succes, String erreur) {
        if (ok) message("✅ " + succes, "#2e7d32"); else message("❌ " + erreur, "#c62828");
    }

    private void message(String texte, String couleur) {
        lbMessage.setText(texte);
        lbMessage.setStyle("-fx-text-fill: " + couleur + "; -fx-background-color: white; -fx-border-color: " + couleur + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold;");
        FadeTransition fade = new FadeTransition(Duration.millis(180), lbMessage);
        fade.setFromValue(0.25);
        fade.setToValue(1);
        fade.play();
    }

    @FXML public void allerMedicaments(ActionEvent e) { naviguer("/MainView.fxml"); }
    @FXML public void allerCategories(ActionEvent e)  { naviguer("/CategorieView.fxml"); }
    @FXML public void allerFabricants(ActionEvent e)  { naviguer("/FabricantView.fxml"); }
    @FXML public void allerFormes(ActionEvent e)      { naviguer("/FormeView.fxml"); }
    @FXML public void allerStock(ActionEvent e)       { naviguer("/StockView.fxml"); }
    @FXML public void allerAlertes(ActionEvent e)     { naviguer("/AlerteView.fxml"); }
    @FXML public void allerChatbot(ActionEvent e)     { naviguer("/ChatbotView.fxml"); }
    @FXML public void allerAssurances(ActionEvent e)  { /* déjà ici */ }
    @FXML public void ouvrirOcr(ActionEvent e)        { naviguer("/OcrView.fxml"); }
    @FXML public void ouvrirCouvertures(ActionEvent e) { naviguer("/CouvertureView.fxml"); }
    @FXML public void ouvrirRemboursements(ActionEvent e) { naviguer("/RemboursementView.fxml"); }

    private void naviguer(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            root.setOpacity(0);
            tableView.getScene().setRoot(root);
            FadeTransition fade = new FadeTransition(Duration.millis(220), root);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
