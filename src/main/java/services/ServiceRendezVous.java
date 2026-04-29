package services;

import interfaces.IService;
import models.RendezVous;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRendezVous implements IService<RendezVous> {

    @Override
    public void add(RendezVous r) {
        String req = "INSERT INTO rendez_vous(id_patient, id_medecin, date_heure, duree_min, statut, motif, notes) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, r.getId_patient());
            ps.setInt(2, r.getId_medecin());
            ps.setString(3, r.getDate_heure());
            ps.setInt(4, r.getDuree_min());
            ps.setString(5, r.getStatut());
            ps.setString(6, r.getMotif());
            ps.setString(7, r.getNotes());
            ps.executeUpdate();
            System.out.println("Rendez-vous ajouté !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<RendezVous> getAll() {
        List<RendezVous> list = new ArrayList<>();
        String req = "SELECT * FROM rendez_vous";
        try {
            Statement stm = MyDataBase.getInstance().getCnx().createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                RendezVous r = new RendezVous();
                r.setId_rdv(rs.getInt("id_rdv"));
                r.setId_patient(rs.getInt("id_patient"));
                r.setId_medecin(rs.getInt("id_medecin"));
                r.setDate_heure(rs.getString("date_heure"));
                r.setDuree_min(rs.getInt("duree_min"));
                r.setStatut(rs.getString("statut"));
                r.setMotif(rs.getString("motif"));
                r.setNotes(rs.getString("notes"));
                list.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public void update(RendezVous r) {
        String req = "UPDATE rendez_vous SET id_patient=?, id_medecin=?, date_heure=?, duree_min=?, statut=?, motif=?, notes=? WHERE id_rdv=?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, r.getId_patient());
            ps.setInt(2, r.getId_medecin());
            ps.setString(3, r.getDate_heure());
            ps.setInt(4, r.getDuree_min());
            ps.setString(5, r.getStatut());
            ps.setString(6, r.getMotif());
            ps.setString(7, r.getNotes());
            ps.setInt(8, r.getId_rdv());
            ps.executeUpdate();
            System.out.println("Rendez-vous modifié !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(RendezVous r) {
        String req = "DELETE FROM rendez_vous WHERE id_rdv=?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, r.getId_rdv());
            ps.executeUpdate();
            System.out.println("Rendez-vous supprimé !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Méthode bonus : récupérer les RDV d'un patient
    public List<RendezVous> getByPatient(int id_patient) {
        List<RendezVous> list = new ArrayList<>();
        String req = "SELECT * FROM rendez_vous WHERE id_patient=?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, id_patient);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RendezVous r = new RendezVous();
                r.setId_rdv(rs.getInt("id_rdv"));
                r.setId_patient(rs.getInt("id_patient"));
                r.setId_medecin(rs.getInt("id_medecin"));
                r.setDate_heure(rs.getString("date_heure"));
                r.setDuree_min(rs.getInt("duree_min"));
                r.setStatut(rs.getString("statut"));
                r.setMotif(rs.getString("motif"));
                r.setNotes(rs.getString("notes"));
                list.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }



    public List<RendezVous> filtrerParStatut(String statut) {
        List<RendezVous> list = new ArrayList<>();
        String req = "SELECT * FROM rendez_vous WHERE statut = ?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RendezVous r = new RendezVous();
                r.setId_rdv(rs.getInt("id_rdv"));
                r.setId_patient(rs.getInt("id_patient"));
                r.setId_medecin(rs.getInt("id_medecin"));
                r.setDate_heure(rs.getString("date_heure"));
                r.setDuree_min(rs.getInt("duree_min"));
                r.setStatut(rs.getString("statut"));
                r.setMotif(rs.getString("motif"));
                r.setNotes(rs.getString("notes"));
                list.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }


    public List<RendezVous> trierParDate() {
        List<RendezVous> list = new ArrayList<>();
        String req = "SELECT * FROM rendez_vous ORDER BY date_heure DESC";
        try {
            Statement stm = MyDataBase.getInstance().getCnx().createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                RendezVous r = new RendezVous();
                r.setId_rdv(rs.getInt("id_rdv"));
                r.setId_patient(rs.getInt("id_patient"));
                r.setId_medecin(rs.getInt("id_medecin"));
                r.setDate_heure(rs.getString("date_heure"));
                r.setDuree_min(rs.getInt("duree_min"));
                r.setStatut(rs.getString("statut"));
                r.setMotif(rs.getString("motif"));
                r.setNotes(rs.getString("notes"));
                list.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }



    public List<RendezVous> filtrerParDate(String date) {
        List<RendezVous> list = new ArrayList<>();
        String req = "SELECT * FROM rendez_vous WHERE DATE(date_heure) = ?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RendezVous r = new RendezVous();
                r.setId_rdv(rs.getInt("id_rdv"));
                r.setId_patient(rs.getInt("id_patient"));
                r.setId_medecin(rs.getInt("id_medecin"));
                r.setDate_heure(rs.getString("date_heure"));
                r.setDuree_min(rs.getInt("duree_min"));
                r.setStatut(rs.getString("statut"));
                r.setMotif(rs.getString("motif"));
                r.setNotes(rs.getString("notes"));
                list.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }


}
