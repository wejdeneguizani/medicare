package com.medical.services;

import com.medical.interfaces.IService;
import com.medical.model.Medicament;
import com.medical.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentService implements IService<Medicament> {

    // ─── AJOUTER ──────────────────────────────────────────────────────────────
    @Override
    public boolean ajouter(Medicament m) {
        if (m.getNomCommercial() == null || m.getNomCommercial().trim().isEmpty()) {
            System.out.println("⚠️ Le nom commercial est obligatoire !");
            return false;
        }
        if (m.getNomDci() == null || m.getNomDci().trim().isEmpty()) {
            System.out.println("⚠️ Le nom DCI est obligatoire !");
            return false;
        }
        if (m.getDosage() == null || m.getDosage().trim().isEmpty()) {
            System.out.println("⚠️ Le dosage est obligatoire !");
            return false;
        }
        for (Medicament existing : getTous()) {
            if (existing.getCodeBarre() != null &&
                    existing.getCodeBarre().equals(m.getCodeBarre())) {
                System.out.println("⚠️ Ce code barre existe déjà !");
                return false;
            }
        }
        if (m.getIdCategorie() <= 0) {
            System.out.println("⚠️ La catégorie est obligatoire !");
            return false;
        }
        if (m.getIdForme() <= 0) {
            System.out.println("⚠️ La forme pharmaceutique est obligatoire !");
            return false;
        }
        String sql = "INSERT INTO medicament (nom_commercial, nom_dci, dosage, code_barre, " +
                "id_categorie, id_forme, id_fabricant, est_actif) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNomCommercial());
            ps.setString(2, m.getNomDci());
            ps.setString(3, m.getDosage());
            ps.setString(4, m.getCodeBarre());
            ps.setInt(5, m.getIdCategorie());
            ps.setInt(6, m.getIdForme());
            ps.setInt(7, m.getIdFabricant());
            ps.setBoolean(8, m.isEstActif());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajouter médicament : " + e.getMessage());
            return false;
        }
    }

    // ─── GET TOUS ─────────────────────────────────────────────────────────────
    @Override
    public List<Medicament> getTous() {
        List<Medicament> liste = new ArrayList<>();
        String sql = "SELECT * FROM medicament";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Medicament m = new Medicament();
                m.setIdMedicament(rs.getInt("id_medicament"));
                m.setNomCommercial(rs.getString("nom_commercial"));
                m.setNomDci(rs.getString("nom_dci"));
                m.setDosage(rs.getString("dosage"));
                m.setCodeBarre(rs.getString("code_barre"));
                m.setIdCategorie(rs.getInt("id_categorie"));
                m.setIdForme(rs.getInt("id_forme"));
                m.setIdFabricant(rs.getInt("id_fabricant"));
                m.setEstActif(rs.getBoolean("est_actif"));
                liste.add(m);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur getTous médicament : " + e.getMessage());
        }
        return liste;
    }

    // ─── GET PAR ID ───────────────────────────────────────────────────────────
    @Override
    public Medicament getParId(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return null;
        }
        String sql = "SELECT * FROM medicament WHERE id_medicament = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Medicament m = new Medicament();
                m.setIdMedicament(rs.getInt("id_medicament"));
                m.setNomCommercial(rs.getString("nom_commercial"));
                m.setNomDci(rs.getString("nom_dci"));
                m.setDosage(rs.getString("dosage"));
                m.setCodeBarre(rs.getString("code_barre"));
                m.setIdCategorie(rs.getInt("id_categorie"));
                m.setIdForme(rs.getInt("id_forme"));
                m.setIdFabricant(rs.getInt("id_fabricant"));
                m.setEstActif(rs.getBoolean("est_actif"));
                return m;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur getParId médicament : " + e.getMessage());
        }
        return null;
    }

    // ─── MODIFIER ─────────────────────────────────────────────────────────────
    @Override
    public boolean modifier(Medicament m) {
        if (m.getIdMedicament() <= 0) {
            System.out.println("⚠️ ID invalide !");
            return false;
        }
        if (m.getNomCommercial() == null || m.getNomCommercial().trim().isEmpty()) {
            System.out.println("⚠️ Le nom commercial est obligatoire !");
            return false;
        }
        if (getParId(m.getIdMedicament()) == null) {
            System.out.println("⚠️ Ce médicament n'existe pas !");
            return false;
        }
        String sql = "UPDATE medicament SET nom_commercial=?, nom_dci=?, dosage=?, code_barre=?, " +
                "id_categorie=?, id_forme=?, id_fabricant=?, est_actif=? WHERE id_medicament=?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNomCommercial());
            ps.setString(2, m.getNomDci());
            ps.setString(3, m.getDosage());
            ps.setString(4, m.getCodeBarre());
            ps.setInt(5, m.getIdCategorie());
            ps.setInt(6, m.getIdForme());
            ps.setInt(7, m.getIdFabricant());
            ps.setBoolean(8, m.isEstActif());
            ps.setInt(9, m.getIdMedicament());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Erreur modifier médicament : " + e.getMessage());
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
        Medicament m = getParId(id);
        if (m == null) {
            System.out.println("⚠️ Ce médicament n'existe pas !");
            return false;
        }
        if (m.isEstActif()) {
            System.out.println("⚠️ Impossible de supprimer un médicament actif !");
            return false;
        }
        String sql = "DELETE FROM medicament WHERE id_medicament = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Erreur supprimer médicament : " + e.getMessage());
            return false;
        }
    }
}