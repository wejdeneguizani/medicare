package com.medical.services;

import com.medical.model.Categorie;
import com.medical.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService {

    // ───────────────────────── AJOUTER ─────────────────────────
    public boolean ajouter(Categorie c) {
        if (c.getLibelle() == null || c.getLibelle().trim().isEmpty()) {
            System.out.println("⚠️ Le libellé est obligatoire !");
            return false;
        }
        if (c.getLibelle().trim().length() < 3) {
            System.out.println("⚠️ Le libellé doit avoir au moins 3 caractères !");
            return false;
        }
        // Vérifier unicité du code ATC
        if (c.getCodeAtc() != null && !c.getCodeAtc().trim().isEmpty()) {
            for (Categorie existing : getTous()) {
                if (c.getCodeAtc().equalsIgnoreCase(existing.getCodeAtc())) {
                    System.out.println("⚠️ Ce code ATC existe déjà !");
                    return false;
                }
            }
        }
        String sql = "INSERT INTO categorie_medicament (libelle, code_atc, description) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setString(1, c.getLibelle().trim());
            ps.setString(2, c.getCodeAtc());
            ps.setString(3, c.getDescription());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout catégorie : " + e.getMessage());
            return false;
        }
    }

    // ───────────────────────── LISTER ─────────────────────────
    public List<Categorie> getTous() {
        List<Categorie> liste = new ArrayList<>();
        String sql = "SELECT * FROM categorie_medicament";
        try {
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                liste.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur liste catégories : " + e.getMessage());
        }
        return liste;
    }

    // ───────────────────────── CHERCHER PAR ID ─────────────────────────
    public Categorie getParId(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return null;
        }
        String sql = "SELECT * FROM categorie_medicament WHERE id_categorie = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("❌ Erreur recherche catégorie : " + e.getMessage());
        }
        return null;
    }

    // ───────────────────────── MODIFIER ─────────────────────────
    public boolean modifier(Categorie c) {
        if (c.getIdCategorie() <= 0) {
            System.out.println("⚠️ ID invalide !");
            return false;
        }
        if (c.getLibelle() == null || c.getLibelle().trim().isEmpty()) {
            System.out.println("⚠️ Le libellé est obligatoire !");
            return false;
        }
        if (getParId(c.getIdCategorie()) == null) {
            System.out.println("⚠️ Cette catégorie n'existe pas !");
            return false;
        }
        String sql = "UPDATE categorie_medicament SET libelle=?, code_atc=?, description=? WHERE id_categorie=?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setString(1, c.getLibelle().trim());
            ps.setString(2, c.getCodeAtc());
            ps.setString(3, c.getDescription());
            ps.setInt(4, c.getIdCategorie());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur modification catégorie : " + e.getMessage());
            return false;
        }
    }

    // ───────────────────────── SUPPRIMER ─────────────────────────
    public boolean supprimer(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return false;
        }
        if (getParId(id) == null) {
            System.out.println("⚠️ Cette catégorie n'existe pas !");
            return false;
        }
        String sql = "DELETE FROM categorie_medicament WHERE id_categorie = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression catégorie : " + e.getMessage());
            return false;
        }
    }

    // ───────────────────────── MAPPING ─────────────────────────
    private Categorie mapRow(ResultSet rs) throws SQLException {
        Categorie c = new Categorie();
        c.setIdCategorie(rs.getInt("id_categorie"));
        c.setLibelle(rs.getString("libelle"));
        c.setCodeAtc(rs.getString("code_atc"));
        c.setDescription(rs.getString("description"));
        return c;
    }
}