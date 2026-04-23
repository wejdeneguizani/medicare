package com.medical.dao;

import com.medical.utils.DatabaseConnection;
import com.medical.model.Stock;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    public boolean ajouter(Stock s) {
        String sql = "INSERT INTO stock_medicament (id_medicament, numero_lot, quantite, " +
                "seuil_alerte, prix_unitaire, date_expiration, date_reception, " +
                "localisation, fournisseur) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, s.getIdMedicament());
            ps.setString(2, s.getNumeroLot());
            ps.setInt(3, s.getQuantite());
            ps.setInt(4, s.getSeuilAlerte());
            ps.setDouble(5, s.getPrixUnitaire());
            ps.setDate(6, new java.sql.Date(s.getDateExpiration().getTime()));
            ps.setDate(7, new java.sql.Date(s.getDateReception().getTime()));
            ps.setString(8, s.getLocalisation());
            ps.setString(9, s.getFournisseur());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout stock : " + e.getMessage());
            return false;
        }
    }

    public List<Stock> getTous() {
        List<Stock> liste = new ArrayList<>();
        String sql = "SELECT * FROM stock_medicament";
        try {
            Connection conn = DatabaseConnection.getInstance();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Stock s = new Stock();
                s.setIdStock(rs.getInt("id_stock"));
                s.setIdMedicament(rs.getInt("id_medicament"));
                s.setNumeroLot(rs.getString("numero_lot"));
                s.setQuantite(rs.getInt("quantite"));
                s.setSeuilAlerte(rs.getInt("seuil_alerte"));
                s.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                s.setDateExpiration(rs.getDate("date_expiration"));
                s.setDateReception(rs.getDate("date_reception"));
                s.setLocalisation(rs.getString("localisation"));
                s.setFournisseur(rs.getString("fournisseur"));
                liste.add(s);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur affichage stock : " + e.getMessage());
        }
        return liste;
    }

    public List<Stock> getStockFaible() {
        List<Stock> liste = new ArrayList<>();
        String sql = "SELECT * FROM stock_medicament WHERE quantite <= seuil_alerte";
        try {
            Connection conn = DatabaseConnection.getInstance();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Stock s = new Stock();
                s.setIdStock(rs.getInt("id_stock"));
                s.setIdMedicament(rs.getInt("id_medicament"));
                s.setNumeroLot(rs.getString("numero_lot"));
                s.setQuantite(rs.getInt("quantite"));
                s.setSeuilAlerte(rs.getInt("seuil_alerte"));
                s.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                s.setDateExpiration(rs.getDate("date_expiration"));
                liste.add(s);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur stock faible : " + e.getMessage());
        }
        return liste;
    }

    public boolean modifier(Stock s) {
        String sql = "UPDATE stock_medicament SET quantite=?, seuil_alerte=?, " +
                "prix_unitaire=?, localisation=?, fournisseur=? WHERE id_stock=?";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, s.getQuantite());
            ps.setInt(2, s.getSeuilAlerte());
            ps.setDouble(3, s.getPrixUnitaire());
            ps.setString(4, s.getLocalisation());
            ps.setString(5, s.getFournisseur());
            ps.setInt(6, s.getIdStock());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur modification stock : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM stock_medicament WHERE id_stock = ?";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression stock : " + e.getMessage());
            return false;
        }
    }
}