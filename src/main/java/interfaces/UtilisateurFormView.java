package interfaces;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Role;
import models.Utilisateur;
import services.RoleService;
import services.UtilisateurService;
import utils.PasswordUtils;

import java.sql.SQLException;
import java.util.List;

public class UtilisateurFormView {

    private final Stage stage;
    private final Utilisateur utilisateur;
    private final UtilisateurListeView parent;

    private final UtilisateurService userService = new UtilisateurService();
    private final RoleService        roleService = new RoleService();

    private TextField      tfNom, tfPrenom, tfEmail, tfTelephone, tfAdresse;
    private PasswordField  pfMotDePasse;
    private ComboBox<Role>   cbRole;
    private ComboBox<String> cbSexe, cbStatut;
    private DatePicker     dpNaissance;
    private CheckBox       chkDeuxFacteurs;

    public UtilisateurFormView(Stage stage, Utilisateur u, UtilisateurListeView parent) {
        this.stage       = stage;
        this.utilisateur = u;
        this.parent      = parent;
    }

    public void show() {
        buildUI(false);
    }

    public void showReadOnly() {
        buildUI(true);
    }

    private void buildUI(boolean readOnly) {
        boolean isEdit = utilisateur != null;

        Label titre = new Label(readOnly ? "Fiche utilisateur"
                : isEdit  ? "Modifier l'utilisateur"
                  : "Nouvel utilisateur");
        titre.setFont(Font.font("System Bold", 18));
        titre.setTextFill(Color.web("#0C447C"));

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(12);
        grid.setPadding(new Insets(16));
        grid.setStyle("-fx-background-color:white;-fx-background-radius:10;");
        ColumnConstraints c1 = new ColumnConstraints(120);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setHgrow(Priority.ALWAYS);
        ColumnConstraints c3 = new ColumnConstraints(120);
        ColumnConstraints c4 = new ColumnConstraints(); c4.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(c1, c2, c3, c4);

        tfNom       = field("Nom *",       isEdit ? utilisateur.getNom()       : "", readOnly, grid, 0, 0);
        tfPrenom    = field("Prenom *",    isEdit ? utilisateur.getPrenom()    : "", readOnly, grid, 0, 2);
        tfEmail     = field("Email *",     isEdit ? utilisateur.getEmail()     : "", readOnly, grid, 1, 0);
        tfTelephone = field("Telephone",   isEdit ? utilisateur.getTelephone() : "", readOnly, grid, 1, 2);
        tfAdresse   = field("Adresse",     isEdit ? utilisateur.getAdresse()   : "", readOnly, grid, 2, 0);

        grid.add(label("Mot de passe"), 0, 3);
        pfMotDePasse = new PasswordField();
        pfMotDePasse.setPromptText(isEdit ? "Laisser vide pour ne pas changer" : "Mot de passe *");
        pfMotDePasse.setDisable(readOnly);
        GridPane.setColumnSpan(pfMotDePasse, 3);
        grid.add(pfMotDePasse, 1, 3);

        grid.add(label("Role *"), 0, 4);
        cbRole = new ComboBox<>();
        try {
            List<Role> roles = roleService.getAll();
            cbRole.getItems().addAll(roles);
            if (isEdit) {
                roles.stream()
                        .filter(r -> r.getId() == utilisateur.getRoleId())
                        .findFirst()
                        .ifPresent(cbRole::setValue);
            }
        } catch (SQLException e) {
            error("Impossible de charger les roles : " + e.getMessage());
        }
        cbRole.setDisable(readOnly);
        cbRole.setMaxWidth(Double.MAX_VALUE);
        grid.add(cbRole, 1, 4);

        grid.add(label("Sexe"), 2, 4);
        cbSexe = new ComboBox<>();
        cbSexe.getItems().addAll("M", "F", "Autre");
        if (isEdit && utilisateur.getSexe() != null) cbSexe.setValue(utilisateur.getSexe());
        cbSexe.setDisable(readOnly);
        cbSexe.setMaxWidth(Double.MAX_VALUE);
        grid.add(cbSexe, 3, 4);

        grid.add(label("Date naissance"), 0, 5);
        dpNaissance = new DatePicker();
        if (isEdit && utilisateur.getDateNaissance() != null) dpNaissance.setValue(utilisateur.getDateNaissance());
        dpNaissance.setDisable(readOnly);
        dpNaissance.setMaxWidth(Double.MAX_VALUE);
        grid.add(dpNaissance, 1, 5);

        grid.add(label("Statut *"), 2, 5);
        cbStatut = new ComboBox<>();
        cbStatut.getItems().addAll("Actif", "Inactif", "En attente", "Bloque");
        cbStatut.setValue(isEdit ? utilisateur.getStatut() : "En attente");
        cbStatut.setDisable(readOnly);
        cbStatut.setMaxWidth(Double.MAX_VALUE);
        grid.add(cbStatut, 3, 5);

        grid.add(label("Double auth."), 0, 6);
        chkDeuxFacteurs = new CheckBox("Activee");
        if (isEdit) chkDeuxFacteurs.setSelected(utilisateur.isDeuxFacteurs());
        chkDeuxFacteurs.setDisable(readOnly);
        grid.add(chkDeuxFacteurs, 1, 6);

        if (isEdit) {
            grid.add(label("Matricule"), 2, 6);
            Label lblMat = new Label(utilisateur.getMatricule());
            lblMat.setFont(Font.font("Monospaced", 13));
            lblMat.setTextFill(Color.web("#5F5E5A"));
            grid.add(lblMat, 3, 6);
        }

        HBox btnBar = new HBox(10);
        btnBar.setAlignment(Pos.CENTER_RIGHT);

        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setStyle("-fx-background-color:transparent;-fx-text-fill:#5F5E5A;-fx-border-color:#D3D1C7;-fx-border-radius:8;-fx-background-radius:8;-fx-padding:8 18;-fx-cursor:hand;");
        btnAnnuler.setOnAction(e -> parent.show());

        if (!readOnly) {
            Button btnSauvegarder = new Button(isEdit ? "Enregistrer les modifications" : "Creer l'utilisateur");
            btnSauvegarder.setStyle("-fx-background-color:#185FA5;-fx-text-fill:#E6F1FB;-fx-font-size:13px;-fx-padding:8 18;-fx-background-radius:8;-fx-cursor:hand;");
            btnSauvegarder.setOnAction(e -> sauvegarder(isEdit));
            btnBar.getChildren().addAll(btnAnnuler, btnSauvegarder);
        } else {
            Button btnModifier = new Button("Modifier");
            btnModifier.setStyle("-fx-background-color:#185FA5;-fx-text-fill:#E6F1FB;-fx-font-size:13px;-fx-padding:8 18;-fx-background-radius:8;-fx-cursor:hand;");
            btnModifier.setOnAction(e -> new UtilisateurFormView(stage, utilisateur, parent).show());
            btnBar.getChildren().addAll(btnAnnuler, btnModifier);
        }

        VBox root = new VBox(16, titre, grid, btnBar);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color:#F1EFE8;");
        VBox.setVgrow(grid, Priority.ALWAYS);

        Scene scene = new Scene(root, 760, 520);
        stage.setScene(scene);
        stage.setTitle(readOnly ? "Fiche - " + (utilisateur != null ? utilisateur.getNomComplet() : "")
                : (isEdit ? "Modifier" : "Nouvel utilisateur"));
        stage.show();
    }

    private void sauvegarder(boolean isEdit) {
        if (!valider()) return;

        if (isEdit) {
            utilisateur.setNom(tfNom.getText().trim());
            utilisateur.setPrenom(tfPrenom.getText().trim());
            utilisateur.setEmail(tfEmail.getText().trim());
            utilisateur.setTelephone(tfTelephone.getText().trim());
            utilisateur.setAdresse(tfAdresse.getText().trim());
            utilisateur.setRoleId(cbRole.getValue().getId());
            utilisateur.setSexe(cbSexe.getValue());
            utilisateur.setDateNaissance(dpNaissance.getValue());
            utilisateur.setStatut(cbStatut.getValue());
            utilisateur.setDeuxFacteurs(chkDeuxFacteurs.isSelected());
            try {
                userService.modifier(utilisateur);
                if (!pfMotDePasse.getText().isBlank())
                    userService.changerMotDePasse(utilisateur.getId(), pfMotDePasse.getText());
                success("Utilisateur modifie avec succes.");
                parent.charger();
                parent.show();
            } catch (SQLException e) {
                error(e.getMessage());
            }
        } else {
            Utilisateur u = new Utilisateur();
            u.setNom(tfNom.getText().trim());
            u.setPrenom(tfPrenom.getText().trim());
            u.setEmail(tfEmail.getText().trim());
            u.setMotDePasse(pfMotDePasse.getText());
            u.setTelephone(tfTelephone.getText().trim());
            u.setAdresse(tfAdresse.getText().trim());
            u.setRoleId(cbRole.getValue().getId());
            u.setSexe(cbSexe.getValue());
            u.setDateNaissance(dpNaissance.getValue());
            u.setStatut(cbStatut.getValue());
            u.setDeuxFacteurs(chkDeuxFacteurs.isSelected());
            u.setMatricule(PasswordUtils.genererMatricule(cbRole.getValue().getNom()));
            try {
                userService.ajouter(u);
                success("Utilisateur cree avec succes.\nMatricule : " + u.getMatricule());
                parent.charger();
                parent.show();
            } catch (SQLException e) {
                error(e.getMessage());
            }
        }
    }

    private boolean valider() {
        StringBuilder sb = new StringBuilder();
        if (tfNom.getText().isBlank())    sb.append("- Le nom est obligatoire.\n");
        if (tfPrenom.getText().isBlank()) sb.append("- Le prenom est obligatoire.\n");
        if (tfEmail.getText().isBlank())  sb.append("- L'email est obligatoire.\n");
        if (cbRole.getValue() == null)    sb.append("- Le role est obligatoire.\n");
        if (utilisateur == null && pfMotDePasse.getText().isBlank())
            sb.append("- Le mot de passe est obligatoire.\n");
        if (!sb.isEmpty()) { error(sb.toString()); return false; }
        return true;
    }

    private TextField field(String lbl, String val, boolean ro, GridPane g, int row, int col) {
        g.add(label(lbl), col, row);
        TextField tf = new TextField(val);
        tf.setDisable(ro);
        tf.setMaxWidth(Double.MAX_VALUE);
        g.add(tf, col + 1, row);
        return tf;
    }

    private Label label(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(12));
        l.setTextFill(Color.web("#5F5E5A"));
        return l;
    }

    private void success(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void error(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText("Erreur");
        a.showAndWait();
    }
}
