package com.medical.services;

import com.medical.interfaces.IService;
import com.medical.model.Assurance;
import com.medical.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssuranceService implements IService<Assurance> {

    private static final String TABLE_ASSURANCE = "assurance_sante";

    @Override
    public boolean ajouter(Assurance a) {
        if (!valider(a)) return false;
        if (numeroExiste(a.getNumeroContrat(), 0)) {
            System.out.println("⚠️ Ce numéro de contrat existe déjà !");
            return false;
        }
        String sql = "INSERT INTO " + TABLE_ASSURANCE + " (numero_contrat,type_assurance,nom_assureur,date_debut,date_fin,plafond_annuel,taux_remboursement,statut,id_user) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            remplirStatement(ps, a);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout assurance : " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Assurance> getTous() {
        List<Assurance> liste = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_ASSURANCE;
        try {
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) liste.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("❌ Erreur liste assurances : " + e.getMessage());
        }
        return liste;
    }

    @Override
    public Assurance getParId(int id) {
        String sql = "SELECT * FROM " + TABLE_ASSURANCE + " WHERE id_assurance=?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("❌ Erreur recherche assurance : " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean modifier(Assurance a) {
        if (a.getIdAssurance() <= 0 || getParId(a.getIdAssurance()) == null) {
            System.out.println("⚠️ Assurance introuvable !");
            return false;
        }
        if (!valider(a)) return false;
        if (numeroExiste(a.getNumeroContrat(), a.getIdAssurance())) {
            System.out.println("⚠️ Ce numéro de contrat existe déjà !");
            return false;
        }
        String sql = "UPDATE " + TABLE_ASSURANCE + " SET numero_contrat=?,type_assurance=?,nom_assureur=?,date_debut=?,date_fin=?,plafond_annuel=?,taux_remboursement=?,statut=?,id_user=? WHERE id_assurance=?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            remplirStatement(ps, a);
            ps.setInt(10, a.getIdAssurance());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur modification assurance : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean supprimer(int id) {
        if (id <= 0 || getParId(id) == null) {
            System.out.println("⚠️ Assurance introuvable !");
            return false;
        }
        String sql = "DELETE FROM " + TABLE_ASSURANCE + " WHERE id_assurance=?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression assurance : " + e.getMessage());
            return false;
        }
    }

    public List<Assurance> getAssurancesExpirees() {
        List<Assurance> liste = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_ASSURANCE + " WHERE date_fin < CURDATE()";
        try {
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) liste.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("❌ Erreur assurances expirées : " + e.getMessage());
        }
        return liste;
    }

    public boolean mettreAJourStatutsExpires() {
        String sql = "UPDATE " + TABLE_ASSURANCE + " SET statut='Expiree' WHERE date_fin < CURDATE()";
        try {
            Statement st = DatabaseConnection.getInstance().createStatement();
            st.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur mise à jour statut : " + e.getMessage());
            return false;
        }
    }

    private boolean valider(Assurance a) {
        if (a.getNumeroContrat() == null || a.getNumeroContrat().trim().isEmpty()) return false;
        if (a.getNomAssureur() == null || a.getNomAssureur().trim().isEmpty()) return false;
        if (a.getDateDebut() == null || a.getDateFin() == null) return false;
        if (a.getPlafondAnnuel() < 0 || a.getTauxBaseRemboursement() < 0 || a.getTauxBaseRemboursement() > 100) return false;
        return true;
    }

    private boolean numeroExiste(String numero, int idIgnore) {
        String sql = "SELECT id_assurance FROM " + TABLE_ASSURANCE + " WHERE numero_contrat=? AND id_assurance<>?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setString(1, numero);
            ps.setInt(2, idIgnore);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) { return false; }
    }

    private void remplirStatement(PreparedStatement ps, Assurance a) throws SQLException {
        ps.setString(1, a.getNumeroContrat().trim());
        ps.setString(2, a.getTypeAssurance());
        ps.setString(3, a.getNomAssureur().trim());
        ps.setDate(4, a.getDateDebut());
        ps.setDate(5, a.getDateFin());
        ps.setDouble(6, a.getPlafondAnnuel());
        ps.setDouble(7, a.getTauxBaseRemboursement());
        ps.setString(8, a.getStatut());
        ps.setInt(9, a.getIdUser());
    }

    private Assurance mapRow(ResultSet rs) throws SQLException {
        Assurance a = new Assurance();
        a.setIdAssurance(rs.getInt("id_assurance"));
        a.setNumeroContrat(rs.getString("numero_contrat"));
        a.setTypeAssurance(rs.getString("type_assurance"));
        a.setNomAssureur(rs.getString("nom_assureur"));
        a.setDateDebut(rs.getDate("date_debut"));
        a.setDateFin(rs.getDate("date_fin"));
        a.setPlafondAnnuel(rs.getDouble("plafond_annuel"));
        a.setTauxBaseRemboursement(rs.getDouble("taux_remboursement"));
        a.setStatut(rs.getString("statut"));
        a.setIdUser(rs.getInt("id_user"));
        return a;
    }
}
