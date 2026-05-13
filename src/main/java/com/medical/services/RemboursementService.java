package com.medical.services;

import com.medical.interfaces.IService;
import com.medical.model.Remboursement;
import com.medical.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RemboursementService implements IService<Remboursement> {

    private static final String TABLE_REMBOURSEMENT = "demande_remboursement";

    @Override
    public boolean ajouter(Remboursement r) {
        String sql = "INSERT INTO " + TABLE_REMBOURSEMENT + " (id_assurance,type_depense,montant_depense,montant_estime,montant_valide,date_demande,date_validation,statut,commentaire) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            fill(ps, r);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur ajout remboursement : " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Remboursement> getTous() {
        List<Remboursement> list = new ArrayList<>();
        try {
            ResultSet rs = DatabaseConnection.getInstance().createStatement().executeQuery("SELECT * FROM " + TABLE_REMBOURSEMENT);
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public Remboursement getParId(int id) {
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement("SELECT * FROM " + TABLE_REMBOURSEMENT + " WHERE id_remboursement=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean modifier(Remboursement r) {
        String sql = "UPDATE " + TABLE_REMBOURSEMENT + " SET id_assurance=?,type_depense=?,montant_depense=?,montant_estime=?,montant_valide=?,date_demande=?,date_validation=?,statut=?,commentaire=? WHERE id_remboursement=?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            fill(ps, r);
            ps.setInt(10, r.getIdRemboursement());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean supprimer(int id) {
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement("DELETE FROM " + TABLE_REMBOURSEMENT + " WHERE id_remboursement=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Remboursement> getEnAttente() {
        List<Remboursement> list = new ArrayList<>();
        try {
            ResultSet rs = DatabaseConnection.getInstance().createStatement().executeQuery("SELECT * FROM " + TABLE_REMBOURSEMENT + " WHERE statut='En attente'");
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public double getTotalRembourseValide() {
        try {
            ResultSet rs = DatabaseConnection.getInstance().createStatement().executeQuery("SELECT SUM(montant_valide) AS total FROM " + TABLE_REMBOURSEMENT + " WHERE statut='Valide'");
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    private void fill(PreparedStatement ps, Remboursement r) throws SQLException {
        ps.setInt(1, r.getIdAssurance());
        ps.setString(2, r.getTypeDepense());
        ps.setDouble(3, r.getMontantDepense());
        ps.setDouble(4, r.getMontantEstime());
        ps.setDouble(5, r.getMontantValide());
        ps.setDate(6, r.getDateDemande());
        ps.setDate(7, r.getDateValidation());
        ps.setString(8, r.getStatut());
        ps.setString(9, r.getCommentaire());
    }

    private Remboursement map(ResultSet rs) throws SQLException {
        Remboursement r = new Remboursement();
        r.setIdRemboursement(rs.getInt("id_remboursement"));
        r.setIdAssurance(rs.getInt("id_assurance"));
        r.setTypeDepense(rs.getString("type_depense"));
        r.setMontantDepense(rs.getDouble("montant_depense"));
        r.setMontantEstime(rs.getDouble("montant_estime"));
        r.setMontantValide(rs.getDouble("montant_valide"));
        r.setDateDemande(rs.getDate("date_demande"));
        r.setDateValidation(rs.getDate("date_validation"));
        r.setStatut(rs.getString("statut"));
        r.setCommentaire(rs.getString("commentaire"));
        return r;
    }
}
