package com.medical.services;

import com.medical.interfaces.IService;
import com.medical.model.Forme;
import com.medical.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormeService implements IService<Forme> {

    // ─── AJOUTER ──────────────────────────────────────────────────────────────
    @Override
    public boolean ajouter(Forme f) {
        if (f.getLibelle() == null || f.getLibelle().trim().isEmpty()) {
            System.out.println("️ Le libellé est obligatoire !");
            return false;
        }
        if (f.getLibelle().trim().length() < 3) {
            System.out.println("️ Le libellé doit avoir au moins 3 caractères !");
            return false;
        }
        if (f.getVoieAdministration() == null || f.getVoieAdministration().trim().isEmpty()) {
            System.out.println("️ La voie d'administration est obligatoire !");
            return false;
        }
        for (Forme existing : getTous()) {
            if (existing.getLibelle().equalsIgnoreCase(f.getLibelle())) {
                System.out.println("️ Cette forme pharmaceutique existe déjà !");
                return false;
            }
        }
        String sql = "INSERT INTO forme (libelle, voie_administration) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getLibelle());
            ps.setString(2, f.getVoieAdministration());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(" Erreur ajouter forme : " + e.getMessage());
            return false;
        }
    }

    // ─── GET TOUS ─────────────────────────────────────────────────────────────
    @Override
    public List<Forme> getTous() {
        List<Forme> liste = new ArrayList<>();
        String sql = "SELECT * FROM forme";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Forme f = new Forme();
                f.setIdForme(rs.getInt("id_forme"));
                f.setLibelle(rs.getString("libelle"));
                f.setVoieAdministration(rs.getString("voie_administration"));
                liste.add(f);
            }
        } catch (SQLException e) {
            System.out.println(" Erreur getTous forme : " + e.getMessage());
        }
        return liste;
    }

    // ─── GET PAR ID ───────────────────────────────────────────────────────────
    @Override
    public Forme getParId(int id) {
        if (id <= 0) {
            System.out.println("️ L'ID doit être positif !");
            return null;
        }
        String sql = "SELECT * FROM forme WHERE id_forme = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Forme f = new Forme();
                f.setIdForme(rs.getInt("id_forme"));
                f.setLibelle(rs.getString("libelle"));
                f.setVoieAdministration(rs.getString("voie_administration"));
                return f;
            }
        } catch (SQLException e) {
            System.out.println(" Erreur getParId forme : " + e.getMessage());
        }
        return null;
    }

    // ─── MODIFIER ─────────────────────────────────────────────────────────────
    @Override
    public boolean modifier(Forme f) {
        if (f.getIdForme() <= 0) {
            System.out.println("️ ID invalide !");
            return false;
        }
        if (f.getLibelle() == null || f.getLibelle().trim().isEmpty()) {
            System.out.println("️ Le libellé est obligatoire !");
            return false;
        }
        if (f.getVoieAdministration() == null || f.getVoieAdministration().trim().isEmpty()) {
            System.out.println("️ La voie d'administration est obligatoire !");
            return false;
        }
        if (getParId(f.getIdForme()) == null) {
            System.out.println("️ Cette forme n'existe pas !");
            return false;
        }
        String sql = "UPDATE forme SET libelle=?, voie_administration=? WHERE id_forme=?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getLibelle());
            ps.setString(2, f.getVoieAdministration());
            ps.setInt(3, f.getIdForme());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(" Erreur modifier forme : " + e.getMessage());
            return false;
        }
    }

    // ─── SUPPRIMER ────────────────────────────────────────────────────────────
    @Override
    public boolean supprimer(int id) {
        if (id <= 0) {
            System.out.println("️ L'ID doit être positif !");
            return false;
        }
        if (getParId(id) == null) {
            System.out.println("️ Cette forme n'existe pas !");
            return false;
        }
        String sql = "DELETE FROM forme WHERE id_forme = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(" Erreur supprimer forme : " + e.getMessage());
            return false;
        }
    }
}