package services;

import interfaces.IService;
import models.Objectif;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceObjectif implements IService<Objectif> {

    private final Connection cnx = MyDataBase.getInstance().getCnx();

    @Override
    public void add(Objectif o) {
        String req = "INSERT INTO objectif (id_patient, id_medecin, titre, categorie, " +
                "valeur_cible, unite_mesure, date_debut, date_echeance, statut, priorite) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, o.getId_patient());
            ps.setInt(2, o.getId_medecin());
            ps.setString(3, o.getTitre());
            ps.setString(4, o.getCategorie());
            ps.setFloat(5, o.getValeur_cible());
            ps.setString(6, o.getUnite_mesure());
            ps.setDate(7, Date.valueOf(o.getDate_debut()));
            ps.setDate(8, Date.valueOf(o.getDate_echeance()));
            ps.setString(9, o.getStatut());
            ps.setInt(10, o.getPriorite());
            ps.executeUpdate();
            System.out.println("Objectif ajouté ✓");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Objectif> getAll() {
        List<Objectif> list = new ArrayList<>();
        String req = "SELECT * FROM objectif";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs  = stm.executeQuery(req);
            while (rs.next()) {
                Objectif o = new Objectif();
                o.setId_objectif(rs.getInt("id_objectif"));
                o.setId_patient(rs.getInt("id_patient"));
                o.setId_medecin(rs.getInt("id_medecin"));
                o.setTitre(rs.getString("titre"));
                o.setCategorie(rs.getString("categorie"));
                o.setValeur_cible(rs.getFloat("valeur_cible"));
                o.setUnite_mesure(rs.getString("unite_mesure"));
                o.setDate_debut(rs.getDate("date_debut").toLocalDate());
                o.setDate_echeance(rs.getDate("date_echeance").toLocalDate());
                o.setStatut(rs.getString("statut"));
                o.setPriorite(rs.getInt("priorite"));
                list.add(o);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Objectif o) {
        String req = "UPDATE objectif SET titre=?, categorie=?, valeur_cible=?, " +
                "unite_mesure=?, date_echeance=?, statut=?, priorite=? " +
                "WHERE id_objectif=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, o.getTitre());
            ps.setString(2, o.getCategorie());
            ps.setFloat(3, o.getValeur_cible());
            ps.setString(4, o.getUnite_mesure());
            ps.setDate(5, Date.valueOf(o.getDate_echeance()));
            ps.setString(6, o.getStatut());
            ps.setInt(7, o.getPriorite());
            ps.setInt(8, o.getId_objectif());
            ps.executeUpdate();
            System.out.println("Objectif mis à jour ✓");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Objectif o) {
        String req = "DELETE FROM objectif WHERE id_objectif=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, o.getId_objectif());
            ps.executeUpdate();
            System.out.println("Objectif supprimé ✓");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Objectif> rechercher(String motCle) {
        List<Objectif> list = new ArrayList<>();
        String req = "SELECT * FROM objectif WHERE titre LIKE ? OR categorie LIKE ? OR statut LIKE ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            String pattern = "%" + motCle + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Objectif o = new Objectif();
                o.setId_objectif(rs.getInt("id_objectif"));
                o.setId_patient(rs.getInt("id_patient"));
                o.setId_medecin(rs.getInt("id_medecin"));
                o.setTitre(rs.getString("titre"));
                o.setCategorie(rs.getString("categorie"));
                o.setValeur_cible(rs.getFloat("valeur_cible"));
                o.setUnite_mesure(rs.getString("unite_mesure"));
                o.setDate_debut(rs.getDate("date_debut").toLocalDate());
                o.setDate_echeance(rs.getDate("date_echeance").toLocalDate());
                o.setStatut(rs.getString("statut"));
                o.setPriorite(rs.getInt("priorite"));
                list.add(o);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}