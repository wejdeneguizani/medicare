package com.medical.dao;

import com.medical.model.Medicament;
import com.medical.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentDAO {

    public boolean ajouter(Medicament m) {
        String sql = "INSERT INTO medicament (nom_commercial, nom_dci, code_barre, id_categorie, " +
                     "id_forme, id_fabricant, dosage, unite_dosage, description, " +
                     "contre_indications, effets_secondaires, est_actif) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setString(1, m.getNomCommercial());
            ps.setString(2, m.getNomDci());
            ps.setString(3, m.getCodeBarre());
            ps.setInt(4, m.getIdCategorie());
            ps.setInt(5, m.getIdForme());
            ps.setInt(6, m.getIdFabricant());
            ps.setString(7, m.getDosage());
            ps.setString(8, m.getUniteDosage());
            ps.setString(9, m.getDescription());
            ps.setString(10, m.getContreIndications());
            ps.setString(11, m.getEffetsSecondaires());
            ps.setBoolean(12, m.isEstActif());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout médicament : " + e.getMessage());
            return false;
        }
    }

    public List<Medicament> getTous() {
        List<Medicament> liste = new ArrayList<>();
        String sql = "SELECT * FROM medicament";
        try {
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Medicament m = new Medicament();
                m.setIdMedicament(rs.getInt("id_medicament"));
                m.setNomCommercial(rs.getString("nom_commercial"));
                m.setNomDci(rs.getString("nom_dci"));
                m.setCodeBarre(rs.getString("code_barre"));
                m.setIdCategorie(rs.getInt("id_categorie"));
                m.setIdForme(rs.getInt("id_forme"));
                m.setIdFabricant(rs.getInt("id_fabricant"));
                m.setDosage(rs.getString("dosage"));
                m.setUniteDosage(rs.getString("unite_dosage"));
                m.setDescription(rs.getString("description"));
                m.setContreIndications(rs.getString("contre_indications"));
                m.setEffetsSecondaires(rs.getString("effets_secondaires"));
                m.setEstActif(rs.getBoolean("est_actif"));
                liste.add(m);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur affichage médicaments : " + e.getMessage());
        }
        return liste;
    }

    public Medicament getParId(int id) {
        String sql = "SELECT * FROM medicament WHERE id_medicament = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Medicament m = new Medicament();
                m.setIdMedicament(rs.getInt("id_medicament"));
                m.setNomCommercial(rs.getString("nom_commercial"));
                m.setNomDci(rs.getString("nom_dci"));
                m.setCodeBarre(rs.getString("code_barre"));
                m.setIdCategorie(rs.getInt("id_categorie"));
                m.setIdForme(rs.getInt("id_forme"));
                m.setIdFabricant(rs.getInt("id_fabricant"));
                m.setDosage(rs.getString("dosage"));
                m.setUniteDosage(rs.getString("unite_dosage"));
                m.setDescription(rs.getString("description"));
                m.setContreIndications(rs.getString("contre_indications"));
                m.setEffetsSecondaires(rs.getString("effets_secondaires"));
                m.setEstActif(rs.getBoolean("est_actif"));
                return m;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur recherche médicament : " + e.getMessage());
        }
        return null;
    }

    public boolean modifier(Medicament m) {
        String sql = "UPDATE medicament SET nom_commercial=?, nom_dci=?, code_barre=?, " +
                     "id_categorie=?, id_forme=?, id_fabricant=?, dosage=?, unite_dosage=?, " +
                     "description=?, contre_indications=?, effets_secondaires=?, est_actif=? " +
                     "WHERE id_medicament=?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setString(1, m.getNomCommercial());
            ps.setString(2, m.getNomDci());
            ps.setString(3, m.getCodeBarre());
            ps.setInt(4, m.getIdCategorie());
            ps.setInt(5, m.getIdForme());
            ps.setInt(6, m.getIdFabricant());
            ps.setString(7, m.getDosage());
            ps.setString(8, m.getUniteDosage());
            ps.setString(9, m.getDescription());
            ps.setString(10, m.getContreIndications());
            ps.setString(11, m.getEffetsSecondaires());
            ps.setBoolean(12, m.isEstActif());
            ps.setInt(13, m.getIdMedicament());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur modification médicament : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM medicament WHERE id_medicament = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression médicament : " + e.getMessage());
            return false;
        }
    }
}
