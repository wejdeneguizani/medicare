package com.medical.services;

import com.medical.interfaces.IService;
import com.medical.model.Stock;
import com.medical.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockService implements IService<Stock> {

    // ─── AJOUTER ──────────────────────────────────────────────────────────────
    @Override
    public boolean ajouter(Stock s) {
        if (s.getIdMedicament() <= 0) {
            System.out.println("⚠️ L'ID du médicament est invalide !");
            return false;
        }
        if (s.getNumeroLot() == null || s.getNumeroLot().trim().isEmpty()) {
            System.out.println("⚠️ Le numéro de lot est obligatoire !");
            return false;
        }
        if (s.getQuantite() < 0) {
            System.out.println("⚠️ La quantité ne peut pas être négative !");
            return false;
        }
        if (s.getPrixUnitaire() <= 0) {
            System.out.println("⚠️ Le prix unitaire doit être positif !");
            return false;
        }
        if (s.getDateExpiration() == null) {
            System.out.println("⚠️ La date d'expiration est obligatoire !");
            return false;
        }
        if (s.getDateExpiration().before(new Date())) {
            System.out.println("⚠️ La date d'expiration est déjà passée !");
            return false;
        }
        if (s.getSeuilAlerte() < 0) {
            System.out.println("⚠️ Le seuil d'alerte ne peut pas être négatif !");
            return false;
        }
        String sql = "INSERT INTO stock (id_medicament, numero_lot, quantite, prix_unitaire, " +
                "date_expiration, seuil_alerte) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getIdMedicament());
            ps.setString(2, s.getNumeroLot());
            ps.setInt(3, s.getQuantite());
            ps.setDouble(4, s.getPrixUnitaire());
            ps.setDate(5, new java.sql.Date(s.getDateExpiration().getTime()));
            ps.setInt(6, s.getSeuilAlerte());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajouter stock : " + e.getMessage());
            return false;
        }
    }

    // ─── GET TOUS ─────────────────────────────────────────────────────────────
    @Override
    public List<Stock> getTous() {
        List<Stock> liste = new ArrayList<>();
        String sql = "SELECT * FROM stock";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur getTous stock : " + e.getMessage());
        }
        return liste;
    }

    // ─── GET PAR ID ───────────────────────────────────────────────────────────
    @Override
    public Stock getParId(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return null;
        }
        String sql = "SELECT * FROM stock WHERE id_stock = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("❌ Erreur getParId stock : " + e.getMessage());
        }
        return null;
    }

    // ─── STOCK FAIBLE ─────────────────────────────────────────────────────────
    public List<Stock> getStockFaible() {
        List<Stock> liste = new ArrayList<>();
        String sql = "SELECT * FROM stock WHERE quantite <= seuil_alerte";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) liste.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("❌ Erreur getStockFaible : " + e.getMessage());
        }
        if (liste.isEmpty()) {
            System.out.println("✅ Aucun stock en alerte !");
        } else {
            System.out.println("⚠️ " + liste.size() + " stock(s) en alerte !");
        }
        return liste;
    }

    // ─── MODIFIER ─────────────────────────────────────────────────────────────
    @Override
    public boolean modifier(Stock s) {
        if (s.getIdStock() <= 0) {
            System.out.println("⚠️ ID invalide !");
            return false;
        }
        if (s.getQuantite() < 0) {
            System.out.println("⚠️ La quantité ne peut pas être négative !");
            return false;
        }
        if (s.getPrixUnitaire() <= 0) {
            System.out.println("⚠️ Le prix unitaire doit être positif !");
            return false;
        }
        if (s.getSeuilAlerte() < 0) {
            System.out.println("⚠️ Le seuil d'alerte ne peut pas être négatif !");
            return false;
        }
        String sql = "UPDATE stock SET id_medicament=?, numero_lot=?, quantite=?, prix_unitaire=?, " +
                "date_expiration=?, seuil_alerte=? WHERE id_stock=?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getIdMedicament());
            ps.setString(2, s.getNumeroLot());
            ps.setInt(3, s.getQuantite());
            ps.setDouble(4, s.getPrixUnitaire());
            ps.setDate(5, s.getDateExpiration() != null
                    ? new java.sql.Date(s.getDateExpiration().getTime()) : null);
            ps.setInt(6, s.getSeuilAlerte());
            ps.setInt(7, s.getIdStock());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Erreur modifier stock : " + e.getMessage());
            return false;
        }
    }

    // ─── SUPPRIMER ────────────────────────────────────────────────────────────
    @Override
    public boolean supprimer(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return false;
        }
        if (getParId(id) == null) {
            System.out.println("⚠️ Ce stock n'existe pas !");
            return false;
        }
        String sql = "DELETE FROM stock WHERE id_stock = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Erreur supprimer stock : " + e.getMessage());
            return false;
        }
    }

    // ─── HELPER ───────────────────────────────────────────────────────────────
    private Stock mapRow(ResultSet rs) throws SQLException {
        Stock s = new Stock();
        s.setIdStock(rs.getInt("id_stock"));
        s.setIdMedicament(rs.getInt("id_medicament"));
        s.setNumeroLot(rs.getString("numero_lot"));
        s.setQuantite(rs.getInt("quantite"));
        s.setPrixUnitaire(rs.getDouble("prix_unitaire"));
        s.setDateExpiration(rs.getDate("date_expiration"));
        s.setSeuilAlerte(rs.getInt("seuil_alerte"));
        return s;
    }
}