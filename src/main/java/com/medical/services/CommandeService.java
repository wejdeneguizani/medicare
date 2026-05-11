package com.medical.services;

import com.medical.model.Commande;
import com.medical.model.Stock;
import com.medical.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandeService {

    // ─── RECHERCHE PAR NOM (TextField) ────────────────────────────────────────
    public List<Stock> rechercherParNom(String terme) {
        List<Stock> liste = new ArrayList<>();
        if (terme == null || terme.trim().isEmpty()) return liste;
        String sql = "SELECT s.*, m.nom_commercial " +
                "FROM stock s JOIN medicament m ON s.id_medicament = m.id_medicament " +
                "WHERE m.nom_commercial LIKE ? ORDER BY m.nom_commercial";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + terme.trim() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(mapStock(rs));
        } catch (SQLException e) {
            System.out.println("❌ rechercherParNom : " + e.getMessage());
        }
        return liste;
    }

    // ─── LISTE COMPLÈTE POUR COMBOBOX ─────────────────────────────────────────
    public List<Stock> getTousStocksAvecNom() {
        List<Stock> liste = new ArrayList<>();
        String sql = "SELECT s.*, m.nom_commercial " +
                "FROM stock s JOIN medicament m ON s.id_medicament = m.id_medicament " +
                "ORDER BY m.nom_commercial";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(mapStock(rs));
        } catch (SQLException e) {
            System.out.println("❌ getTousStocksAvecNom : " + e.getMessage());
        }
        return liste;
    }

    // ─── ENREGISTRER UNE COMMANDE ─────────────────────────────────────────────
    public boolean ajouter(Commande c) {
        if (c.getNomClient() == null || c.getNomClient().trim().isEmpty()) {
            System.out.println("⚠️ Nom obligatoire !");        return false;
        }
        if (c.getQuantite() <= 0) {
            System.out.println("⚠️ Quantité invalide !");      return false;
        }
        if (c.getMontantTotal() <= 0) {
            System.out.println("⚠️ Montant invalide !");       return false;
        }
        // Email obligatoire seulement pour les clients
        if ("CLIENT".equals(c.getTypeCommande())) {
            if (c.getEmailClient() == null || !c.getEmailClient().contains("@")) {
                System.out.println("⚠️ Email client invalide !");
                return false;
            }
        }
        String sql = "INSERT INTO commande (type_commande, nom_client, email_client, " +
                "telephone_client, id_stock, quantite, prix_unitaire, montant_total, " +
                "date_commande, mode_paiement, statut) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getTypeCommande());
            ps.setString(2, c.getNomClient());
            ps.setString(3, c.getEmailClient());
            ps.setString(4, c.getTelephoneClient());
            ps.setInt(5, c.getIdStock());
            ps.setInt(6, c.getQuantite());
            ps.setDouble(7, c.getPrixUnitaire());
            ps.setDouble(8, c.getMontantTotal());
            ps.setDate(9, new java.sql.Date(new Date().getTime()));
            ps.setString(10, c.getModePaiement());
            ps.setString(11, c.getStatut());
            if (ps.executeUpdate() > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) c.setIdCommande(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ ajouter commande : " + e.getMessage());
        }
        return false;
    }

    // ─── MISE À JOUR DU STOCK ─────────────────────────────────────────────────
    /** Vente client → diminuer le stock */
    public boolean diminuerStock(int idStock, int qte) {
        String sql = "UPDATE stock SET quantite = quantite - ? WHERE id_stock = ? AND quantite >= ?";
        return executerMajStock(sql, qte, idStock, qte);
    }

    /** Réapprovisionnement → augmenter le stock */
    public boolean augmenterStock(int idStock, int qte) {
        String sql = "UPDATE stock SET quantite = quantite + ? WHERE id_stock = ?";
        return executerMajStock(sql, qte, idStock, null);
    }

    private boolean executerMajStock(String sql, int qte, int idStock, Integer check) {
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qte);
            ps.setInt(2, idStock);
            if (check != null) ps.setInt(3, check);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ majStock : " + e.getMessage());
            return false;
        }
    }

    // ─── HISTORIQUE ───────────────────────────────────────────────────────────
    public List<Commande> getTous() {
        List<Commande> liste = new ArrayList<>();
        String sql = "SELECT c.*, m.nom_commercial " +
                "FROM commande c " +
                "JOIN stock s ON c.id_stock = s.id_stock " +
                "JOIN medicament m ON s.id_medicament = m.id_medicament " +
                "ORDER BY c.date_commande DESC";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Commande c = new Commande();
                c.setIdCommande(rs.getInt("id_commande"));
                c.setTypeCommande(rs.getString("type_commande"));
                c.setNomClient(rs.getString("nom_client"));
                c.setEmailClient(rs.getString("email_client"));
                c.setTelephoneClient(rs.getString("telephone_client"));
                c.setIdStock(rs.getInt("id_stock"));
                c.setQuantite(rs.getInt("quantite"));
                c.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                c.setMontantTotal(rs.getDouble("montant_total"));
                c.setDateCommande(rs.getDate("date_commande"));
                c.setModePaiement(rs.getString("mode_paiement"));
                c.setStatut(rs.getString("statut"));
                c.setNomMedicament(rs.getString("nom_commercial"));
                liste.add(c);
            }
        } catch (SQLException e) {
            System.out.println("❌ getTous commandes : " + e.getMessage());
        }
        return liste;
    }

    // ─── HELPER ───────────────────────────────────────────────────────────────
    private Stock mapStock(ResultSet rs) throws SQLException {
        Stock s = new Stock();
        s.setIdStock(rs.getInt("id_stock"));
        s.setIdMedicament(rs.getInt("id_medicament"));
        s.setNumeroLot(rs.getString("numero_lot"));
        s.setQuantite(rs.getInt("quantite"));
        s.setPrixUnitaire(rs.getDouble("prix_unitaire"));
        s.setDateExpiration(rs.getDate("date_expiration"));
        s.setSeuilAlerte(rs.getInt("seuil_alerte"));
        s.setFournisseur(rs.getString("nom_commercial")); // réutilisé pour nom médicament
        return s;
    }
}