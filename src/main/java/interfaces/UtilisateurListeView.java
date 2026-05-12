package interfaces;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Utilisateur;
import services.UtilisateurService;

import java.sql.SQLException;
import java.util.List;

public class UtilisateurListeView {

    private final Stage stage;
    private final UtilisateurService service = new UtilisateurService();
    private TableView<Utilisateur> table;
    private ObservableList<Utilisateur> data;

    public UtilisateurListeView(Stage stage) {
        this.stage = stage;
    }

    @SuppressWarnings("unchecked")
    public void show() {
        // ── Toolbar ───────────────────────────────────────────
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher nom, email, matricule…");
        searchField.setPrefWidth(260);

        ComboBox<String> roleFilter = new ComboBox<>();
        roleFilter.getItems().addAll("Tous les roles", "Administrateur", "Medecin", "Patient");
        roleFilter.setValue("Tous les roles");

        ComboBox<String> statutFilter = new ComboBox<>();
        statutFilter.getItems().addAll("Tous les statuts", "Actif", "Inactif", "En attente", "Bloqué");
        statutFilter.setValue("Tous les statuts");

        Button btnRechercher = actionBtn("Rechercher", "#185FA5", "#E6F1FB");
        Button btnAjouter    = actionBtn("+ Ajouter", "#3B6D11", "#EAF3DE");
        Button btnRetour     = actionBtn("← Retour", "#5F5E5A", "#F1EFE8");

        btnRetour.setOnAction(e -> new MainView(stage).show());
        btnAjouter.setOnAction(e -> new UtilisateurFormView(stage, null, this).show());
        btnRechercher.setOnAction(e -> rechercher(searchField.getText(),
                roleFilter.getValue().equals("Tous les roles") ? "" : roleFilter.getValue(),
                statutFilter.getValue().equals("Tous les statuts") ? "" : statutFilter.getValue()));
        searchField.setOnAction(e -> btnRechercher.fire());

        HBox toolbar = new HBox(8, searchField, roleFilter, statutFilter, btnRechercher, btnAjouter);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchField, Priority.SOMETIMES);

        HBox topBar = new HBox(btnRetour, new Label("  "), toolbar);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 12, 0));

        // ── TableView ─────────────────────────────────────────
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color:white;-fx-background-radius:10;");

        TableColumn<Utilisateur, String> colMatricule = new TableColumn<>("Matricule");
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colMatricule.setPrefWidth(130);

        TableColumn<Utilisateur, String> colNom = new TableColumn<>("Nom complet");
        colNom.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNomComplet()));
        colNom.setPrefWidth(180);

        TableColumn<Utilisateur, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(200);

        TableColumn<Utilisateur, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colRole.setPrefWidth(110);
        colRole.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String role, boolean empty) {
                super.updateItem(role, empty);
                if (empty || role == null) { setGraphic(null); setText(null); return; }
                Label badge = new Label(role);
                badge.setFont(Font.font(11));
                badge.setPadding(new Insets(3, 8, 3, 8));
                badge.setStyle(badgeStyle(role));
                setGraphic(badge);
            }
        });

        TableColumn<Utilisateur, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colStatut.setPrefWidth(100);
        colStatut.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String statut, boolean empty) {
                super.updateItem(statut, empty);
                if (empty || statut == null) { setGraphic(null); setText(null); return; }
                Label lbl = new Label("● " + statut);
                lbl.setFont(Font.font(12));
                lbl.setTextFill(Color.web(statutColor(statut)));
                setGraphic(lbl);
            }
        });

        TableColumn<Utilisateur, String> colConnexion = new TableColumn<>("Dernière connexion");
        colConnexion.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDerniereConnexion() != null
                        ? c.getValue().getDerniereConnexion().toString().replace("T", " ").substring(0, 16)
                        : "—"
        ));
        colConnexion.setPrefWidth(150);

        TableColumn<Utilisateur, Void> colActions = new TableColumn<>("Actions");
        colActions.setPrefWidth(160);
        colActions.setCellFactory(col -> new TableCell<>() {
            final Button btnVoir    = smallBtn("Voir", "#185FA5");
            final Button btnEdit    = smallBtn("Modifier", "#3B6D11");
            final Button btnBloquer = smallBtn("Bloquer", "#A32D2D");
            {
                btnVoir.setOnAction(e -> {
                    Utilisateur u = getTableView().getItems().get(getIndex());
                    new UtilisateurFormView(stage, u, UtilisateurListeView.this).showReadOnly();
                });
                btnEdit.setOnAction(e -> {
                    Utilisateur u = getTableView().getItems().get(getIndex());
                    new UtilisateurFormView(stage, u, UtilisateurListeView.this).show();
                });
                btnBloquer.setOnAction(e -> {
                    Utilisateur u = getTableView().getItems().get(getIndex());
                    String newStatut = u.getStatut().equals("Bloqué") ? "Actif" : "Bloqué";
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Changer le statut de " + u.getNomComplet() + " → " + newStatut + " ?",
                            ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            try { service.changerStatut(u.getId(), newStatut); charger(); }
                            catch (SQLException ex) { showError(ex.getMessage()); }
                        }
                    });
                });
            }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                if (empty) { setGraphic(null); return; }
                Utilisateur u = getTableView().getItems().get(getIndex());
                btnBloquer.setText(u.getStatut().equals("Bloqué") ? "Débloquer" : "Bloquer");
                HBox box = new HBox(4, btnVoir, btnEdit, btnBloquer);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
            }
        });

        table.getColumns().addAll(colMatricule, colNom, colEmail, colRole, colStatut, colConnexion, colActions);
        data = FXCollections.observableArrayList();
        table.setItems(data);
        charger();

        // ── Label bas de page ─────────────────────────────────
        Label lblCount = new Label();
        data.addListener((javafx.collections.ListChangeListener<Utilisateur>) c ->
                lblCount.setText(data.size() + " utilisateur(s) affiché(s)"));

        // ── Layout ────────────────────────────────────────────
        VBox root = new VBox(topBar, table, lblCount);
        VBox.setVgrow(table, Priority.ALWAYS);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color:#F1EFE8;");

        Scene scene = new Scene(root, 1100, 680);
        stage.setScene(scene);
        stage.setTitle("Medicare+ — Liste des utilisateurs");
    }

    // ── méthodes utilitaires ───────────────────────────────────
    public void charger() {
        try {
            List<Utilisateur> liste = service.getAll();
            data.setAll(liste);
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void rechercher(String terme, String role, String statut) {
        try {
            data.setAll(service.rechercher(terme, role, statut));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, "Erreur : " + msg).showAndWait();
    }

    private Button actionBtn(String text, String bg, String fg) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color:" + bg + ";-fx-text-fill:" + fg + ";-fx-font-size:13px;-fx-padding:7 14;-fx-background-radius:8;-fx-cursor:hand;");
        return btn;
    }

    private Button smallBtn(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color:transparent;-fx-text-fill:" + color + ";-fx-font-size:11px;-fx-padding:3 7;-fx-border-color:" + color + ";-fx-border-radius:5;-fx-background-radius:5;-fx-cursor:hand;");
        return btn;
    }

    private String badgeStyle(String role) {
        return switch (role) {
            case "Administrateur" -> "-fx-background-color:#E6F1FB;-fx-text-fill:#0C447C;-fx-background-radius:20;";
            case "Medecin"        -> "-fx-background-color:#EAF3DE;-fx-text-fill:#27500A;-fx-background-radius:20;";
            default               -> "-fx-background-color:#EEEDFE;-fx-text-fill:#3C3489;-fx-background-radius:20;";
        };
    }

    private String statutColor(String statut) {
        return switch (statut) {
            case "Actif"       -> "#639922";
            case "Inactif"     -> "#888780";
            case "En attente"  -> "#EF9F27";
            case "Bloqué"      -> "#E24B4A";
            default            -> "#888780";
        };
    }
}
