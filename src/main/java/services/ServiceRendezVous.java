package services;

import interfaces.IService;
import models.RendezVous;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRendezVous implements IService<RendezVous> {

    private final Connection cnx = MyDataBase.getInstance().getCnx();

    // ── ADD ───────────────────────────────────────────────────────
    @Override
    public void add(RendezVous rv) {
        String req = "INSERT INTO rendez_vous (patient_id, medecin_id, disponibilite_id, " +
                "date_rdv, heure_rdv, motif, notes, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, rv.getPatientId());
            ps.setInt(2, rv.getMedecinId());
            if (rv.getDisponibiliteId() > 0)
                ps.setInt(3, rv.getDisponibiliteId());
            else
                ps.setNull(3, Types.INTEGER);
            ps.setDate(4, Date.valueOf(rv.getDateRdv()));
            ps.setTime(5, Time.valueOf(rv.getHeureRdv()));
            ps.setString(6, rv.getMotif());
            ps.setString(7, rv.getNotes());
            ps.setString(8, rv.getStatut());
            ps.executeUpdate();
            System.out.println("✔ Rendez-vous ajouté le " + rv.getDateRdv());
        } catch (SQLException e) {
            System.out.println("Erreur add RendezVous : " + e.getMessage());
        }
    }

    // ── GET ALL ───────────────────────────────────────────────────
    @Override
    public List<RendezVous> getAll() {
        List<RendezVous> liste = new ArrayList<>();
        String req = "SELECT * FROM rendez_vous";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs  = stm.executeQuery(req);
            while (rs.next()) {
                RendezVous rv = new RendezVous();
                rv.setId(rs.getInt("id"));
                rv.setPatientId(rs.getInt("patient_id"));
                rv.setMedecinId(rs.getInt("medecin_id"));
                rv.setDisponibiliteId(rs.getInt("disponibilite_id"));
                rv.setDateRdv(rs.getDate("date_rdv").toLocalDate());
                rv.setHeureRdv(rs.getTime("heure_rdv").toLocalTime());
                rv.setMotif(rs.getString("motif"));
                rv.setNotes(rs.getString("notes"));
                rv.setStatut(rs.getString("statut"));
                liste.add(rv);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getAll RendezVous : " + e.getMessage());
        }
        return liste;
    }

    // ── DELETE ────────────────────────────────────────────────────
    @Override
    public void delete(RendezVous rv) {
        String req = "DELETE FROM rendez_vous WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, rv.getId());
            ps.executeUpdate();
            System.out.println("✔ Rendez-vous supprimé id=" + rv.getId());
        } catch (SQLException e) {
            System.out.println("Erreur delete RendezVous : " + e.getMessage());
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────
    @Override
    public void update(RendezVous rv) {
        String req = "UPDATE rendez_vous SET patient_id=?, medecin_id=?, disponibilite_id=?, " +
                "date_rdv=?, heure_rdv=?, motif=?, notes=?, statut=? WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, rv.getPatientId());
            ps.setInt(2, rv.getMedecinId());
            if (rv.getDisponibiliteId() > 0)
                ps.setInt(3, rv.getDisponibiliteId());
            else
                ps.setNull(3, Types.INTEGER);
            ps.setDate(4, Date.valueOf(rv.getDateRdv()));
            ps.setTime(5, Time.valueOf(rv.getHeureRdv()));
            ps.setString(6, rv.getMotif());
            ps.setString(7, rv.getNotes());
            ps.setString(8, rv.getStatut());
            ps.setInt(9, rv.getId());
            ps.executeUpdate();
            System.out.println("✔ Rendez-vous mis à jour id=" + rv.getId());
        } catch (SQLException e) {
            System.out.println("Erreur update RendezVous : " + e.getMessage());
        }
    }

    // ── GET BY PATIENT ────────────────────────────────────────────
    public List<RendezVous> getByPatient(int patientId) {
        List<RendezVous> liste = new ArrayList<>();
        String req = "SELECT * FROM rendez_vous WHERE patient_id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RendezVous rv = new RendezVous();
                rv.setId(rs.getInt("id"));
                rv.setPatientId(rs.getInt("patient_id"));
                rv.setMedecinId(rs.getInt("medecin_id"));
                rv.setDateRdv(rs.getDate("date_rdv").toLocalDate());
                rv.setHeureRdv(rs.getTime("heure_rdv").toLocalTime());
                rv.setMotif(rs.getString("motif"));
                rv.setStatut(rs.getString("statut"));
                liste.add(rv);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getByPatient : " + e.getMessage());
        }
        return liste;
    }

    // ── GET BY MEDECIN ────────────────────────────────────────────
    public List<RendezVous> getByMedecin(int medecinId) {
        List<RendezVous> liste = new ArrayList<>();
        String req = "SELECT * FROM rendez_vous WHERE medecin_id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, medecinId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RendezVous rv = new RendezVous();
                rv.setId(rs.getInt("id"));
                rv.setPatientId(rs.getInt("patient_id"));
                rv.setMedecinId(rs.getInt("medecin_id"));
                rv.setDateRdv(rs.getDate("date_rdv").toLocalDate());
                rv.setHeureRdv(rs.getTime("heure_rdv").toLocalTime());
                rv.setMotif(rs.getString("motif"));
                rv.setStatut(rs.getString("statut"));
                liste.add(rv);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getByMedecin : " + e.getMessage());
        }
        return liste;
    }

    // ── CHANGER STATUT ────────────────────────────────────────────
    public void changerStatut(int rdvId, String nouveauStatut) {
        String req = "UPDATE rendez_vous SET statut=? WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, nouveauStatut);
            ps.setInt(2, rdvId);
            ps.executeUpdate();
            System.out.println("✔ Statut RDV " + rdvId + " → " + nouveauStatut);
        } catch (SQLException e) {
            System.out.println("Erreur changerStatut : " + e.getMessage());
        }
    }
}