package interfaces;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainView {

    private Stage stage;

    public MainView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // ── Sidebar ───────────────────────────────────────────
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(210);
        sidebar.setPadding(new Insets(20, 12, 20, 12));
        sidebar.setStyle("-fx-background-color: #0C447C;");

        Label logo = new Label("Medicare+");
        logo.setFont(Font.font("System Bold", 20));
        logo.setTextFill(Color.web("#E6F1FB"));
        logo.setPadding(new Insets(0, 0, 20, 6));

        Label sectionLabel = new Label("GESTION");
        sectionLabel.setFont(Font.font(11));
        sectionLabel.setTextFill(Color.web("#85B7EB"));
        sectionLabel.setPadding(new Insets(10, 0, 4, 6));

        Button btnUtilisateurs = sidebarBtn("👤  Utilisateurs", true);
        Button btnRoles        = sidebarBtn("🔐  Rôles & Permissions", false);
        Button btnActivite     = sidebarBtn("📋  Journal d'activité", false);

        btnUtilisateurs.setOnAction(e -> new UtilisateurListeView(stage).show());
        btnRoles.setOnAction(e -> new RolesView(stage).show());

        sidebar.getChildren().addAll(logo, sectionLabel, btnUtilisateurs, btnRoles, btnActivite);

        // ── Contenu principal ─────────────────────────────────
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.TOP_LEFT);

        Label titre = new Label("Tableau de bord — Gestion des utilisateurs");
        titre.setFont(Font.font("System Bold", 18));
        titre.setTextFill(Color.web("#0C447C"));

        // Stats rapides
        HBox stats = new HBox(12);
        stats.getChildren().addAll(
                statCard("Total utilisateurs", "248",  "#185FA5"),
                statCard("Actifs",             "183",  "#3B6D11"),
                statCard("En attente",         "14",   "#854F0B"),
                statCard("Bloqués",            "7",    "#A32D2D")
        );

        Button btnVoirListe = new Button("Voir la liste complète →");
        btnVoirListe.setStyle("-fx-background-color:#185FA5;-fx-text-fill:#E6F1FB;-fx-font-size:13px;-fx-padding:9 18;-fx-background-radius:8;-fx-cursor:hand;");
        btnVoirListe.setOnAction(e -> new UtilisateurListeView(stage).show());

        content.getChildren().addAll(titre, stats, btnVoirListe);

        // ── Layout principal ──────────────────────────────────
        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(content);
        root.setStyle("-fx-background-color:#F1EFE8;");

        Scene scene = new Scene(root, 1100, 680);
        stage.setTitle("Medicare+ — Gestion des utilisateurs");
        stage.setScene(scene);
        stage.show();
    }

    private Button sidebarBtn(String text, boolean active) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(9, 12, 9, 12));
        btn.setFont(Font.font(13));
        if (active) {
            btn.setStyle("-fx-background-color:#185FA5;-fx-text-fill:#E6F1FB;-fx-background-radius:8;-fx-cursor:hand;");
        } else {
            btn.setStyle("-fx-background-color:transparent;-fx-text-fill:#85B7EB;-fx-background-radius:8;-fx-cursor:hand;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color:#0C447C;-fx-text-fill:#E6F1FB;-fx-background-radius:8;-fx-cursor:hand;"));
            btn.setOnMouseExited(e  -> btn.setStyle("-fx-background-color:transparent;-fx-text-fill:#85B7EB;-fx-background-radius:8;-fx-cursor:hand;"));
        }
        return btn;
    }

    private VBox statCard(String label, String value, String color) {
        VBox card = new VBox(4);
        card.setPrefWidth(170);
        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color:white;-fx-background-radius:10;");

        Label lbl = new Label(label);
        lbl.setFont(Font.font(12));
        lbl.setTextFill(Color.web("#888780"));

        Label val = new Label(value);
        val.setFont(Font.font("System Bold", 24));
        val.setTextFill(Color.web(color));

        card.getChildren().addAll(lbl, val);
        return card;
    }
}