package services;

import models.Utilisateur;
import utils.DatabaseConnection;
import utils.PasswordUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService {

    private Connection conn() throws SQLException {
        return DatabaseConnection.getInstance();
    }

    // ── mapper ResultSet → Utilisateur ────────────────────────
    private Utilisateur mapper(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getInt("id"));
        u.setMatricule(rs.getString("matricule"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setRoleId(rs.getInt("role_id"));
        u.setRoleNom(rs.getString("role_nom"));
        u.setSexe(rs.getString("sexe"));
        Date dn = rs.getDate("date_naissance");
        if (dn != null) u.setDateNaissance(dn.toLocalDate());
        u.setTelephone(rs.getString("telephone"));
        u.setAdresse(rs.getString("adresse"));
        u.setStatut(rs.getString("statut"));
        u.setDeuxFacteurs(rs.getBoolean("deux_facteurs"));
        u.setTentativesEchec(rs.getInt("tentatives_echec"));
        Timestamp dc = rs.getTimestamp("derniere_connexion");
        if (dc != null) u.setDerniereConnexion(dc.toLocalDateTime());
        Timestamp ca = rs.getTimestamp("created_at");
        if (ca != null) u.setCreatedAt(ca.toLocalDateTime());
        return u;
    }

    // ── SELECT ALL ─────────────────────────────────────────────
    public List<Utilisateur> getAll() throws SQLException {
        String sql = """
            SELECT u.*, r.nom AS role_nom
            FROM utilisateurs u
            JOIN roles r ON r.id = u.role_id
            ORDER BY u.created_at DESC
            """;
        List<Utilisateur> liste = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapper(rs));
        }
        return liste;
    }

    // ── SELECT par ID ──────────────────────────────────────────
    public Utilisateur getById(int id) throws SQLException {
        String sql = """
            SELECT u.*, r.nom AS role_nom
            FROM utilisateurs u
            JOIN roles r ON r.id = u.role_id
            WHERE u.id = ?
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper(rs);
            }
        }
        return null;
    }

    // ── RECHERCHE (nom / email / matricule) ────────────────────
    public List<Utilisateur> rechercher(String terme, String roleNom, String statut) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT u.*, r.nom AS role_nom
            FROM utilisateurs u
            JOIN roles r ON r.id = u.role_id
            WHERE 1=1
            """);
        List<Object> params = new ArrayList<>();

        if (terme != null && !terme.isBlank()) {
            sql.append(" AND (u.nom LIKE ? OR u.prenom LIKE ? OR u.email LIKE ? OR u.matricule LIKE ?)");
            String like = "%" + terme + "%";
            params.add(like); params.add(like); params.add(like); params.add(like);
        }
        if (roleNom != null && !roleNom.isBlank()) {
            sql.append(" AND r.nom = ?");
            params.add(roleNom);
        }
        if (statut != null && !statut.isBlank()) {
            sql.append(" AND u.statut = ?");
            params.add(statut);
        }
        sql.append(" ORDER BY u.created_at DESC");

        List<Utilisateur> liste = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) liste.add(mapper(rs));
            }
        }
        return liste;
    }

    // ── INSERT ─────────────────────────────────────────────────
    public boolean ajouter(Utilisateur u) throws SQLException {
        String sql = """
            INSERT INTO utilisateurs
              (matricule, nom, prenom, email, mot_de_passe, role_id,
               sexe, date_naissance, telephone, adresse, statut, deux_facteurs)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getMatricule());
            ps.setString(2, u.getNom());
            ps.setString(3, u.getPrenom());
            ps.setString(4, u.getEmail());
            ps.setString(5, PasswordUtils.hash(u.getMotDePasse()));
            ps.setInt(6, u.getRoleId());
            ps.setString(7, u.getSexe());
            ps.setObject(8, u.getDateNaissance());
            ps.setString(9, u.getTelephone());
            ps.setString(10, u.getAdresse());
            ps.setString(11, u.getStatut() != null ? u.getStatut() : "En attente");
            ps.setBoolean(12, u.isDeuxFacteurs());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) u.setId(keys.getInt(1));
                journaliser(u.getId(), "Création compte", "Nouveau compte créé : " + u.getNomComplet());
            }
            return rows > 0;
        }
    }

    // ── UPDATE ─────────────────────────────────────────────────
    public boolean modifier(Utilisateur u) throws SQLException {
        String sql = """
            UPDATE utilisateurs SET
              nom=?, prenom=?, email=?, role_id=?,
              sexe=?, date_naissance=?, telephone=?, adresse=?,
              statut=?, deux_facteurs=?
            WHERE id=?
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setInt(4, u.getRoleId());
            ps.setString(5, u.getSexe());
            ps.setObject(6, u.getDateNaissance());
            ps.setString(7, u.getTelephone());
            ps.setString(8, u.getAdresse());
            ps.setString(9, u.getStatut());
            ps.setBoolean(10, u.isDeuxFacteurs());
            ps.setInt(11, u.getId());
            int rows = ps.executeUpdate();
            if (rows > 0) journaliser(u.getId(), "Modification profil", "Profil mis à jour");
            return rows > 0;
        }
    }

    // ── CHANGER MOT DE PASSE ───────────────────────────────────
    public boolean changerMotDePasse(int id, String nouveauMdp) throws SQLException {
        String sql = "UPDATE utilisateurs SET mot_de_passe=? WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, PasswordUtils.hash(nouveauMdp));
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            if (rows > 0) journaliser(id, "Changement mot de passe", "Mot de passe mis à jour");
            return rows > 0;
        }
    }

    // ── BLOQUER / DÉBLOQUER ────────────────────────────────────
    public boolean changerStatut(int id, String nouveauStatut) throws SQLException {
        String sql = "UPDATE utilisateurs SET statut=?, tentatives_echec=0 WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, nouveauStatut);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            if (rows > 0) journaliser(id, "Changement statut", "Statut → " + nouveauStatut);
            return rows > 0;
        }
    }

    // ── DELETE ─────────────────────────────────────────────────
    public boolean supprimer(int id) throws SQLException {
        journaliser(id, "Suppression compte", "Compte supprimé de la base");
        String sql = "DELETE FROM utilisateurs WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── CONNEXION (auth) ───────────────────────────────────────
    public Utilisateur connecter(String email, String motDePasse) throws SQLException {
        String sql = """
            SELECT u.*, r.nom AS role_nom
            FROM utilisateurs u
            JOIN roles r ON r.id = u.role_id
            WHERE u.email = ? AND u.statut = 'Actif'
            """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utilisateur u = mapper(rs);
                    if (PasswordUtils.verifier(motDePasse, u.getMotDePasse())) {
                        // Mise à jour dernière connexion
                        String upd = "UPDATE utilisateurs SET derniere_connexion=NOW(), tentatives_echec=0 WHERE id=?";
                        try (PreparedStatement ps2 = conn().prepareStatement(upd)) {
                            ps2.setInt(1, u.getId()); ps2.executeUpdate();
                        }
                        journaliser(u.getId(), "Connexion", "Connexion réussie");
                        return u;
                    } else {
                        // Incrémenter tentatives
                        String fail = "UPDATE utilisateurs SET tentatives_echec = tentatives_echec + 1 WHERE email=?";
                        try (PreparedStatement ps3 = conn().prepareStatement(fail)) {
                            ps3.setString(1, email); ps3.executeUpdate();
                        }
                    }
                }
            }
        }
        return null;
    }

    // ── STATS ──────────────────────────────────────────────────
    public int countByStatut(String statut) throws SQLException {
        String sql = "SELECT COUNT(*) FROM utilisateurs WHERE statut=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int countTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM utilisateurs";
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ── JOURNAL ────────────────────────────────────────────────
    private void journaliser(int userId, String action, String details) {
        String sql = "INSERT INTO journal_activite (utilisateur_id, action, details) VALUES (?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, action);
            ps.setString(3, details);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("⚠️ Journal : " + e.getMessage());
        }
    }
}