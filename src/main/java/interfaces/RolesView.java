package interfaces;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Role;
import services.RoleService;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.List;

public class RolesView {

    private final Stage stage;
    private final RoleService roleService = new RoleService();

    public RolesView(Stage stage) { this.stage = stage; }

    @SuppressWarnings("unchecked")
    public void show() {
        Label titre = new Label("Rôles & Permissions");
        titre.setFont(Font.font("System Bold", 18));
        titre.setTextFill(Color.web("#0C447C"));

        Button btnRetour = new Button("← Retour");
        btnRetour.setStyle("-fx-background-color:transparent;-fx-text-fill:#5F5E5A;-fx-border-color:#D3D1C7;-fx-border-radius:8;-fx-background-radius:8;-fx-padding:7 14;-fx-cursor:hand;");
        btnRetour.setOnAction(e -> new MainView(stage).show());

        HBox header = new HBox(12, btnRetour, titre);
        header.setAlignment(Pos.CENTER_LEFT);

        // ── TableView rôles ───────────────────────────────────
        TableView<Role> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Role, String> colNom  = new TableColumn<>("Rôle");
        colNom.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNom()));
        colNom.setPrefWidth(160);
        colNom.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String r, boolean empty) {
                super.updateItem(r, empty);
                if (empty || r == null) { setGraphic(null); return; }
                Label badge = new Label(r);
                badge.setFont(Font.font(12));
                badge.setPadding(new Insets(3,10,3,10));
                badge.setStyle(badgeStyle(r));
                setGraphic(badge);
            }
        });

        TableColumn<Role, Number> colCount = new TableColumn<>("Utilisateurs");
        colCount.setCellValueFactory(c -> new SimpleIntegerProperty(countUsersForRole(c.getValue().getId())));
        colCount.setPrefWidth(110);
        colCount.setStyle("-fx-alignment:CENTER;");

        TableColumn<Role, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));

        TableColumn<Role, String> colPerms = new TableColumn<>("Modules autorisés");
        colPerms.setCellValueFactory(c -> new SimpleStringProperty(permissionsFor(c.getValue().getId())));
        colPerms.setPrefWidth(350);

        table.getColumns().addAll(colNom, colCount, colDesc, colPerms);

        try {
            List<Role> roles = roleService.getAll();
            table.setItems(FXCollections.observableArrayList(roles));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }

        VBox root = new VBox(16, header, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color:#F1EFE8;");

        stage.setScene(new Scene(root, 1000, 500));
        stage.setTitle("Medicare+ — Rôles & Permissions");
    }

    private int countUsersForRole(int roleId) {
        try (PreparedStatement ps = DatabaseConnection.getInstance()
                .prepareStatement("SELECT COUNT(*) FROM utilisateurs WHERE role_id=?")) {
            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { return 0; }
    }

    private String permissionsFor(int roleId) {
        StringBuilder sb = new StringBuilder();
        try (PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement("""
            SELECT DISTINCT p.module FROM permissions p
            JOIN roles_permissions rp ON rp.permission_id = p.id
            WHERE rp.role_id = ?
            ORDER BY p.module
            """)) {
            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(rs.getString("module").replace("_", " "));
            }
        } catch (SQLException e) { return "—"; }
        return sb.isEmpty() ? "—" : sb.toString();
    }

    private String badgeStyle(String role) {
        return switch (role) {
            case "Administrateur" -> "-fx-background-color:#E6F1FB;-fx-text-fill:#0C447C;-fx-background-radius:20;";
            case "Médecin"        -> "-fx-background-color:#EAF3DE;-fx-text-fill:#27500A;-fx-background-radius:20;";
            case "Infirmier"      -> "-fx-background-color:#FAEEDA;-fx-text-fill:#633806;-fx-background-radius:20;";
            default               -> "-fx-background-color:#EEEDFE;-fx-text-fill:#3C3489;-fx-background-radius:20;";
        };
    }
}