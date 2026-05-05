package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import models.Utilisateur;
import services.UtilisateurService;

import java.io.IOException;
import java.sql.SQLException;

public class GestionUtilisateursController {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> roleFilter;
    @FXML
    private ComboBox<String> statutFilter;
    @FXML
    private TableView<Utilisateur> tableUtilisateurs;
    @FXML
    private TableColumn<Utilisateur, String> colMatricule;
    @FXML
    private TableColumn<Utilisateur, String> colNomComplet;
    @FXML
    private TableColumn<Utilisateur, String> colEmail;
    @FXML
    private TableColumn<Utilisateur, String> colRole;
    @FXML
    private TableColumn<Utilisateur, String> colStatut;
    @FXML
    private TableColumn<Utilisateur, String> colConnexion;
    @FXML
    private TableColumn<Utilisateur, Void> colActions;
    @FXML
    private Label countLabel;

    private final UtilisateurService service = new UtilisateurService();
    private final ObservableList<Utilisateur> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        roleFilter.getItems().setAll("Tous les roles", "Administrateur", "Medecin", "Patient");
        roleFilter.setValue("Tous les roles");
        statutFilter.getItems().setAll("Tous les statuts", "Actif", "Inactif", "En attente", "Bloque");
        statutFilter.setValue("Tous les statuts");

        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colNomComplet.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNomComplet()));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colConnexion.setCellValueFactory(cell -> new SimpleStringProperty(formatConnexion(cell.getValue())));
        colActions.setCellFactory(column -> new ActionCell());

        tableUtilisateurs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableUtilisateurs.setItems(data);
        charger();
    }

    @FXML
    private void rechercher() {
        String role = "Tous les roles".equals(roleFilter.getValue()) ? "" : roleFilter.getValue();
        String statut = "Tous les statuts".equals(statutFilter.getValue()) ? "" : statutFilter.getValue();
        try {
            data.setAll(service.rechercher(searchField.getText(), role, statut));
            updateCount();
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void ajouter(ActionEvent event) {
        ouvrirFormulaire(event, null);
    }

    @FXML
    private void retourDashboard(ActionEvent event) {
        changerVue(event, "/Main.fxml");
    }

    private void charger() {
        try {
            data.setAll(service.getAll());
            updateCount();
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void ouvrirDetails(ActionEvent event, Utilisateur utilisateur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsUtilisateur.fxml"));
            Parent root = loader.load();
            DetailsUtilisateurController controller = loader.getController();
            controller.setUtilisateur(utilisateur);
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    private void ouvrirFormulaire(ActionEvent event, Utilisateur utilisateur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FormUtilisateur.fxml"));
            Parent root = loader.load();
            FormUtilisateurController controller = loader.getController();
            controller.setUtilisateur(utilisateur);
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    private void changerStatut(Utilisateur utilisateur) {
        String nouveauStatut = "Bloque".equals(utilisateur.getStatut()) ? "Actif" : "Bloque";
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Changer le statut de " + utilisateur.getNomComplet() + " vers " + nouveauStatut + " ?",
                ButtonType.YES,
                ButtonType.NO
        );
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(button -> {
            if (button == ButtonType.YES) {
                try {
                    service.changerStatut(utilisateur.getId(), nouveauStatut);
                    charger();
                } catch (SQLException e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void changerVue(ActionEvent event, String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    private String formatConnexion(Utilisateur utilisateur) {
        if (utilisateur.getDerniereConnexion() == null) {
            return "-";
        }
        return utilisateur.getDerniereConnexion().toString().replace("T", " ").substring(0, 16);
    }

    private void updateCount() {
        countLabel.setText(data.size() + " utilisateur(s) affiche(s)");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("Erreur");
        alert.showAndWait();
    }

    private class ActionCell extends TableCell<Utilisateur, Void> {
        private final Button voirButton = new Button("Voir");
        private final Button modifierButton = new Button("Modifier");
        private final Button statutButton = new Button();
        private final HBox box = new HBox(6, voirButton, modifierButton, statutButton);

        private ActionCell() {
            voirButton.getStyleClass().add("small-action-button");
            modifierButton.getStyleClass().add("small-action-button");
            statutButton.getStyleClass().add("danger-action-button");

            voirButton.setOnAction(event -> ouvrirDetails(event, getTableView().getItems().get(getIndex())));
            modifierButton.setOnAction(event -> ouvrirFormulaire(event, getTableView().getItems().get(getIndex())));
            statutButton.setOnAction(event -> changerStatut(getTableView().getItems().get(getIndex())));
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                return;
            }
            Utilisateur utilisateur = getTableView().getItems().get(getIndex());
            statutButton.setText("Bloque".equals(utilisateur.getStatut()) ? "Debloquer" : "Bloquer");
            setGraphic(box);
        }
    }
}
