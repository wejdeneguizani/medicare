package services;

import interfaces.IService;
import models.Disponibilite;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDisponibilite implements IService<Disponibilite> {

    @Override
    public void add(Disponibilite d) {
        String req = "INSERT INTO disponibilite_medecin(id_medecin, jour_semaine, heure_debut, heure_fin, actif) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, d.getId_medecin());
            ps.setString(2, d.getJour_semaine());
            ps.setString(3, d.getHeure_debut());
            ps.setString(4, d.getHeure_fin());
            ps.setBoolean(5, d.isActif());
            ps.executeUpdate();
            System.out.println("Disponibilité ajoutée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Disponibilite> getAll() {
        List<Disponibilite> list = new ArrayList<>();
        String req = "SELECT * FROM disponibilite_medecin";
        try {
            Statement stm = MyDataBase.getInstance().getCnx().createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                Disponibilite d = new Disponibilite();
                d.setId_dispo(rs.getInt("id_dispo"));
                d.setId_medecin(rs.getInt("id_medecin"));
                d.setJour_semaine(rs.getString("jour_semaine"));
                d.setHeure_debut(rs.getString("heure_debut"));
                d.setHeure_fin(rs.getString("heure_fin"));
                d.setActif(rs.getBoolean("actif"));
                list.add(d);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Disponibilite d) {
        String req = "UPDATE disponibilite_medecin SET jour_semaine=?, heure_debut=?, heure_fin=?, actif=? WHERE id_dispo=?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setString(1, d.getJour_semaine());
            ps.setString(2, d.getHeure_debut());
            ps.setString(3, d.getHeure_fin());
            ps.setBoolean(4, d.isActif());
            ps.setInt(5, d.getId_dispo());
            ps.executeUpdate();
            System.out.println("Disponibilité modifiée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Disponibilite d) {
        String req = "DELETE FROM disponibilite_medecin WHERE id_dispo=?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, d.getId_dispo());
            ps.executeUpdate();
            System.out.println("Disponibilité supprimée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Méthode bonus : récupérer les dispos d'un médecin spécifique
    public List<Disponibilite> getByMedecin(int id_medecin) {
        List<Disponibilite> list = new ArrayList<>();
        String req = "SELECT * FROM disponibilite_medecin WHERE id_medecin=? AND actif=1";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, id_medecin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Disponibilite d = new Disponibilite();
                d.setId_dispo(rs.getInt("id_dispo"));
                d.setId_medecin(rs.getInt("id_medecin"));
                d.setJour_semaine(rs.getString("jour_semaine"));
                d.setHeure_debut(rs.getString("heure_debut"));
                d.setHeure_fin(rs.getString("heure_fin"));
                d.setActif(rs.getBoolean("actif"));
                list.add(d);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
