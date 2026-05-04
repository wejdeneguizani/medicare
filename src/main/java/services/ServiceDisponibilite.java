package services;

import interfaces.IService;
import models.Disponibilite;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDisponibilite implements IService<Disponibilite> {

    private final Connection cnx = MyDataBase.getInstance().getCnx();

    // ── ADD ───────────────────────────────────────────────────────
    @Override
    public void add(Disponibilite d) {
        String req = "INSERT INTO disponibilite (medecin_id, date_disponible, " +
                "heure_debut, heure_fin, statut) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, d.getMedecinId());
            ps.setDate(2, Date.valueOf(d.getDateDisponible()));
            ps.setTime(3, Time.valueOf(d.getHeureDebut()));
            ps.setTime(4, Time.valueOf(d.getHeureFin()));
            ps.setString(5, d.getStatut());
            ps.executeUpdate();
            System.out.println("✔ Disponibilité ajoutée pour médecin id=" + d.getMedecinId());
        } catch (SQLException e) {
            System.out.println("Erreur add Disponibilite : " + e.getMessage());
        }
    }

    // ── GET ALL ───────────────────────────────────────────────────
    @Override
    public List<Disponibilite> getAll() {
        List<Disponibilite> liste = new ArrayList<>();
        String req = "SELECT * FROM disponibilite";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs  = stm.executeQuery(req);
            while (rs.next()) {
                Disponibilite d = new Disponibilite();
                d.setId(rs.getInt("id"));
                d.setMedecinId(rs.getInt("medecin_id"));
                d.setDateDisponible(rs.getDate("date_disponible").toLocalDate());
                d.setHeureDebut(rs.getTime("heure_debut").toLocalTime());
                d.setHeureFin(rs.getTime("heure_fin").toLocalTime());
                d.setStatut(rs.getString("statut"));
                liste.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getAll Disponibilite : " + e.getMessage());
        }
        return liste;
    }

    // ── DELETE ────────────────────────────────────────────────────
    @Override
    public void delete(Disponibilite d) {
        String req = "DELETE FROM disponibilite WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, d.getId());
            ps.executeUpdate();
            System.out.println("✔ Disponibilité supprimée id=" + d.getId());
        } catch (SQLException e) {
            System.out.println("Erreur delete Disponibilite : " + e.getMessage());
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────
    @Override
    public void update(Disponibilite d) {
        String req = "UPDATE disponibilite SET medecin_id=?, date_disponible=?, " +
                "heure_debut=?, heure_fin=?, statut=? WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, d.getMedecinId());
            ps.setDate(2, Date.valueOf(d.getDateDisponible()));
            ps.setTime(3, Time.valueOf(d.getHeureDebut()));
            ps.setTime(4, Time.valueOf(d.getHeureFin()));
            ps.setString(5, d.getStatut());
            ps.setInt(6, d.getId());
            ps.executeUpdate();
            System.out.println("✔ Disponibilité mise à jour id=" + d.getId());
        } catch (SQLException e) {
            System.out.println("Erreur update Disponibilite : " + e.getMessage());
        }
    }

    // ── GET BY MEDECIN ────────────────────────────────────────────
    public List<Disponibilite> getByMedecin(int medecinId) {
        List<Disponibilite> liste = new ArrayList<>();
        String req = "SELECT * FROM disponibilite WHERE medecin_id = ? AND statut = 'Disponible'";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, medecinId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Disponibilite d = new Disponibilite();
                d.setId(rs.getInt("id"));
                d.setMedecinId(rs.getInt("medecin_id"));
                d.setDateDisponible(rs.getDate("date_disponible").toLocalDate());
                d.setHeureDebut(rs.getTime("heure_debut").toLocalTime());
                d.setHeureFin(rs.getTime("heure_fin").toLocalTime());
                d.setStatut(rs.getString("statut"));
                liste.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getByMedecin : " + e.getMessage());
        }
        return liste;
    }
}