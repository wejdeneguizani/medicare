package com.medical.services;

import com.medical.interfaces.IService;
import com.medical.model.Fabricant;
import com.medical.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FabricantService implements IService<Fabricant> {

    // ─── AJOUTER ──────────────────────────────────────────────────────────────
    @Override
    public boolean ajouter(Fabricant f) {
        if (f.getNom() == null || f.getNom().trim().isEmpty()) {
            System.out.println("️ Le nom du fabricant est obligatoire !");
            return false;
        }
        if (f.getNom().trim().length() < 2) {
            System.out.println("️ Le nom doit avoir au moins 2 caractères !");
            return false;
        }
        if (f.getPays() == null || f.getPays().trim().isEmpty()) {
            System.out.println(" Le pays est obligatoire !");
            return false;
        }
        for (Fabricant existing : getTous()) {
            if (existing.getNom().equalsIgnoreCase(f.getNom())) {
                System.out.println("️ Ce fabricant existe déjà !");
                return false;
            }
        }
        if (f.getContact() != null && f.getContact().contains("@") && !f.getContact().contains(".")) {
            System.out.println("️ L'email du contact est invalide !");
            return false;
        }
        String sql = "INSERT INTO fabricant (nom, pays, contact) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getNom());
            ps.setString(2, f.getPays());
            ps.setString(3, f.getContact());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(" Erreur ajouter fabricant : " + e.getMessage());
            return false;
        }
    }

    // ─── GET TOUS ─────────────────────────────────────────────────────────────
    @Override
    public List<Fabricant> getTous() {
        List<Fabricant> liste = new ArrayList<>();
        String sql = "SELECT * FROM fabricant";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Fabricant f = new Fabricant();
                f.setIdFabricant(rs.getInt("id_fabricant"));
                f.setNom(rs.getString("nom"));
                f.setPays(rs.getString("pays"));
                f.setContact(rs.getString("contact"));
                liste.add(f);
            }
        } catch (SQLException e) {
            System.out.println(" Erreur getTous fabricant : " + e.getMessage());
        }
        return liste;
    }

    // ─── GET PAR ID ───────────────────────────────────────────────────────────
    @Override
    public Fabricant getParId(int id) {
        if (id <= 0) {
            System.out.println("️ L'ID doit être positif !");
            return null;
        }
        String sql = "SELECT * FROM fabricant WHERE id_fabricant = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Fabricant f = new Fabricant();
                f.setIdFabricant(rs.getInt("id_fabricant"));
                f.setNom(rs.getString("nom"));
                f.setPays(rs.getString("pays"));
                f.setContact(rs.getString("contact"));
                return f;
            }
        } catch (SQLException e) {
            System.out.println(" Erreur getParId fabricant : " + e.getMessage());
        }
        return null;
    }

    // ─── MODIFIER ─────────────────────────────────────────────────────────────
    @Override
    public boolean modifier(Fabricant f) {
        if (f.getIdFabricant() <= 0) {
            System.out.println("️ ID invalide !");
            return false;
        }
        if (f.getNom() == null || f.getNom().trim().isEmpty()) {
            System.out.println("️ Le nom est obligatoire !");
            return false;
        }
        if (f.getPays() == null || f.getPays().trim().isEmpty()) {
            System.out.println("️ Le pays est obligatoire !");
            return false;
        }
        if (getParId(f.getIdFabricant()) == null) {
            System.out.println("️ Ce fabricant n'existe pas !");
            return false;
        }
        String sql = "UPDATE fabricant SET nom=?, pays=?, contact=? WHERE id_fabricant=?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getNom());
            ps.setString(2, f.getPays());
            ps.setString(3, f.getContact());
            ps.setInt(4, f.getIdFabricant());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(" Erreur modifier fabricant : " + e.getMessage());
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
            System.out.println("️ Ce fabricant n'existe pas !");
            return false;
        }
        String sql = "DELETE FROM fabricant WHERE id_fabricant = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(" Erreur supprimer fabricant : " + e.getMessage());
            return false;
        }
    }
}