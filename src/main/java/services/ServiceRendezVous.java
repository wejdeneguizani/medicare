package services;

import interfaces.IService;
import models.RendezVous;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRendezVous implements IService<RendezVous> {

    // ─────────────────────────────────────────
    // ADD  –  INSERT
    // ─────────────────────────────────────────
    @Override
    public void add(RendezVous rdv) {
        String req = "INSERT INTO rendez_vous " +
                "(id_utilisateur, id_medecin, date_heure, motif, statut, type_rdv, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt   (1, rdv.getId_utilisateur());
            ps.setInt   (2, rdv.getId_medecin());
            ps.setString(3, rdv.getDate_heure());
            ps.setString(4, rdv.getMotif());
            ps.setString(5, rdv.getStatut());
            ps.setString(6, rdv.getType_rdv());
            ps.setString(7, rdv.getNotes());
            ps.executeUpdate();
            System.out.println("✅ Rendez-vous ajouté !");
        } catch (SQLException e) {
            System.out.println("❌ add() : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // GET ALL  –  SELECT *
    // ─────────────────────────────────────────
    @Override
    public List<RendezVous> getAll() {
        List<RendezVous> liste = new ArrayList<>();
        String req = "SELECT * FROM rendez_vous";
        try {
            Statement stm = MyDataBase.getInstance().getCnx().createStatement();
            ResultSet rs  = stm.executeQuery(req);
            while (rs.next()) {
                RendezVous rdv = new RendezVous();
                rdv.setId_rdv        (rs.getInt   ("id_rdv"));
                rdv.setId_utilisateur(rs.getInt   ("id_utilisateur"));
                rdv.setId_medecin    (rs.getInt   ("id_medecin"));
                rdv.setDate_heure    (rs.getString("date_heure"));
                rdv.setMotif         (rs.getString("motif"));
                rdv.setStatut        (rs.getString("statut"));
                rdv.setType_rdv      (rs.getString("type_rdv"));
                rdv.setNotes         (rs.getString("notes"));
                liste.add(rdv);
            }
        } catch (SQLException e) {
            System.out.println("❌ getAll() : " + e.getMessage());
        }
        return liste;
    }

    // ─────────────────────────────────────────
    // DELETE  –  DELETE WHERE id
    // ─────────────────────────────────────────
    @Override
    public void delete(RendezVous rdv) {
        String req = "DELETE FROM rendez_vous WHERE id_rdv = ?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, rdv.getId_rdv());
            ps.executeUpdate();
            System.out.println("🗑️ Rendez-vous supprimé (id=" + rdv.getId_rdv() + ")");
        } catch (SQLException e) {
            System.out.println("❌ delete() : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // UPDATE  –  UPDATE SET … WHERE id
    // ─────────────────────────────────────────
    @Override
    public void update(RendezVous rdv) {
        String req = "UPDATE rendez_vous SET " +
                "id_utilisateur=?, id_medecin=?, date_heure=?, " +
                "motif=?, statut=?, type_rdv=?, notes=? " +
                "WHERE id_rdv=?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt   (1, rdv.getId_utilisateur());
            ps.setInt   (2, rdv.getId_medecin());
            ps.setString(3, rdv.getDate_heure());
            ps.setString(4, rdv.getMotif());
            ps.setString(5, rdv.getStatut());
            ps.setString(6, rdv.getType_rdv());
            ps.setString(7, rdv.getNotes());
            ps.setInt   (8, rdv.getId_rdv());
            ps.executeUpdate();
            System.out.println("✏️ Rendez-vous mis à jour (id=" + rdv.getId_rdv() + ")");
        } catch (SQLException e) {
            System.out.println("❌ update() : " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // GET BY ID  –  méthode bonus
    // ─────────────────────────────────────────
    public RendezVous getById(int id) {
        RendezVous rdv = null;
        String req = "SELECT * FROM rendez_vous WHERE id_rdv = ?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rdv = new RendezVous();
                rdv.setId_rdv        (rs.getInt   ("id_rdv"));
                rdv.setId_utilisateur(rs.getInt   ("id_utilisateur"));
                rdv.setId_medecin    (rs.getInt   ("id_medecin"));
                rdv.setDate_heure    (rs.getString("date_heure"));
                rdv.setMotif         (rs.getString("motif"));
                rdv.setStatut        (rs.getString("statut"));
                rdv.setType_rdv      (rs.getString("type_rdv"));
                rdv.setNotes         (rs.getString("notes"));
            }
        } catch (SQLException e) {
            System.out.println("❌ getById() : " + e.getMessage());
        }
        return rdv;
    }
}