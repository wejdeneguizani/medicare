package services;

import interfaces.IService;
import models.Ordonnance;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrdonnance implements IService<Ordonnance> {

    private final Connection cnx = MyDataBase.getInstance().getCnx();

    // ── ADD ───────────────────────────────────────────────────────
    @Override
    public void add(Ordonnance o) {
        String req = "INSERT INTO ordonnance (rendez_vous_id, medecin_id, patient_id, " +
                "medicament, posologie, duree_traitement, instructions, " +
                "date_emission, date_expiration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, o.getRendezVousId());
            ps.setInt(2, o.getMedecinId());
            ps.setInt(3, o.getPatientId());
            ps.setString(4, o.getMedicament());
            ps.setString(5, o.getPosologie());
            ps.setString(6, o.getDureeTraitement());
            ps.setString(7, o.getInstructions());
            ps.setDate(8, o.getDateEmission() != null ? Date.valueOf(o.getDateEmission()) : Date.valueOf(java.time.LocalDate.now()));
            ps.setDate(9, o.getDateExpiration() != null ? Date.valueOf(o.getDateExpiration()) : null);
            ps.executeUpdate();
            System.out.println("✔ Ordonnance ajoutée : " + o.getMedicament());
        } catch (SQLException e) {
            System.out.println("Erreur add Ordonnance : " + e.getMessage());
        }
    }

    // ── GET ALL ───────────────────────────────────────────────────
    @Override
    public List<Ordonnance> getAll() {
        List<Ordonnance> liste = new ArrayList<>();
        String req = "SELECT * FROM ordonnance";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs  = stm.executeQuery(req);
            while (rs.next()) {
                Ordonnance o = new Ordonnance();
                o.setId(rs.getInt("id"));
                o.setRendezVousId(rs.getInt("rendez_vous_id"));
                o.setMedecinId(rs.getInt("medecin_id"));
                o.setPatientId(rs.getInt("patient_id"));
                o.setMedicament(rs.getString("medicament"));
                o.setPosologie(rs.getString("posologie"));
                o.setDureeTraitement(rs.getString("duree_traitement"));
                o.setInstructions(rs.getString("instructions"));
                Date de = rs.getDate("date_emission");
                if (de != null) o.setDateEmission(de.toLocalDate());
                Date dx = rs.getDate("date_expiration");
                if (dx != null) o.setDateExpiration(dx.toLocalDate());
                liste.add(o);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getAll Ordonnance : " + e.getMessage());
        }
        return liste;
    }

    // ── DELETE ────────────────────────────────────────────────────
    @Override
    public void delete(Ordonnance o) {
        String req = "DELETE FROM ordonnance WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, o.getId());
            ps.executeUpdate();
            System.out.println("✔ Ordonnance supprimée id=" + o.getId());
        } catch (SQLException e) {
            System.out.println("Erreur delete Ordonnance : " + e.getMessage());
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────
    @Override
    public void update(Ordonnance o) {
        String req = "UPDATE ordonnance SET medicament=?, posologie=?, " +
                "duree_traitement=?, instructions=?, date_expiration=? WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, o.getMedicament());
            ps.setString(2, o.getPosologie());
            ps.setString(3, o.getDureeTraitement());
            ps.setString(4, o.getInstructions());
            ps.setDate(5, o.getDateExpiration() != null ? Date.valueOf(o.getDateExpiration()) : null);
            ps.setInt(6, o.getId());
            ps.executeUpdate();
            System.out.println("✔ Ordonnance mise à jour id=" + o.getId());
        } catch (SQLException e) {
            System.out.println("Erreur update Ordonnance : " + e.getMessage());
        }
    }

    // ── GET BY PATIENT ────────────────────────────────────────────
    public List<Ordonnance> getByPatient(int patientId) {
        List<Ordonnance> liste = new ArrayList<>();
        String req = "SELECT * FROM ordonnance WHERE patient_id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ordonnance o = new Ordonnance();
                o.setId(rs.getInt("id"));
                o.setRendezVousId(rs.getInt("rendez_vous_id"));
                o.setMedecinId(rs.getInt("medecin_id"));
                o.setPatientId(rs.getInt("patient_id"));
                o.setMedicament(rs.getString("medicament"));
                o.setPosologie(rs.getString("posologie"));
                Date de = rs.getDate("date_emission");
                if (de != null) o.setDateEmission(de.toLocalDate());
                liste.add(o);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getByPatient Ordonnance : " + e.getMessage());
        }
        return liste;
    }

    // ── GET BY RENDEZ-VOUS ────────────────────────────────────────
    public List<Ordonnance> getByRendezVous(int rdvId) {
        List<Ordonnance> liste = new ArrayList<>();
        String req = "SELECT * FROM ordonnance WHERE rendez_vous_id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, rdvId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ordonnance o = new Ordonnance();
                o.setId(rs.getInt("id"));
                o.setRendezVousId(rs.getInt("rendez_vous_id"));
                o.setMedecinId(rs.getInt("medecin_id"));
                o.setPatientId(rs.getInt("patient_id"));
                o.setMedicament(rs.getString("medicament"));
                o.setPosologie(rs.getString("posologie"));
                o.setDureeTraitement(rs.getString("duree_traitement"));
                Date de = rs.getDate("date_emission");
                if (de != null) o.setDateEmission(de.toLocalDate());
                liste.add(o);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getByRendezVous : " + e.getMessage());
        }
        return liste;
    }
}