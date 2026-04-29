package services;

import interfaces.IService;
import models.Medecin;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceMedecin implements IService<Medecin> {

    @Override
    public void add(Medecin m) {
        String req = "INSERT INTO medecin(nom, prenom, email, telephone, specialite, num_ordre) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setString(1, m.getNom());
            ps.setString(2, m.getPrenom());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getTelephone());
            ps.setString(5, m.getSpecialite());
            ps.setString(6, m.getNum_ordre());
            ps.executeUpdate();
            System.out.println("Médecin ajouté !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Medecin> getAll() {
        List<Medecin> list = new ArrayList<>();
        String req = "SELECT * FROM medecin";
        try {
            Statement stm = MyDataBase.getInstance().getCnx().createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                Medecin m = new Medecin();
                m.setId_medecin(rs.getInt("id_medecin"));
                m.setNom(rs.getString("nom"));
                m.setPrenom(rs.getString("prenom"));
                m.setEmail(rs.getString("email"));
                m.setTelephone(rs.getString("telephone"));
                m.setSpecialite(rs.getString("specialite"));
                m.setNum_ordre(rs.getString("num_ordre"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Medecin m) {
        String req = "UPDATE medecin SET nom=?, prenom=?, email=?, telephone=?, specialite=?, num_ordre=? WHERE id_medecin=?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setString(1, m.getNom());
            ps.setString(2, m.getPrenom());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getTelephone());
            ps.setString(5, m.getSpecialite());
            ps.setString(6, m.getNum_ordre());
            ps.setInt(7, m.getId_medecin());
            ps.executeUpdate();
            System.out.println("Médecin modifié !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Medecin m) {
        String req = "DELETE FROM medecin WHERE id_medecin=?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, m.getId_medecin());
            ps.executeUpdate();
            System.out.println("Médecin supprimé !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public List<Medecin> rechercherParSpecialite(String specialite) {
        List<Medecin> list = new ArrayList<>();
        String req = "SELECT * FROM medecin WHERE specialite LIKE ?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setString(1, "%" + specialite + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Medecin m = new Medecin();
                m.setId_medecin(rs.getInt("id_medecin"));
                m.setNom(rs.getString("nom"));
                m.setPrenom(rs.getString("prenom"));
                m.setEmail(rs.getString("email"));
                m.setTelephone(rs.getString("telephone"));
                m.setSpecialite(rs.getString("specialite"));
                m.setNum_ordre(rs.getString("num_ordre"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }



    public List<Medecin> trierParNom() {
        List<Medecin> list = new ArrayList<>();
        String req = "SELECT * FROM medecin ORDER BY nom ASC";
        try {
            Statement stm = MyDataBase.getInstance().getCnx().createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                Medecin m = new Medecin();
                m.setId_medecin(rs.getInt("id_medecin"));
                m.setNom(rs.getString("nom"));
                m.setPrenom(rs.getString("prenom"));
                m.setEmail(rs.getString("email"));
                m.setTelephone(rs.getString("telephone"));
                m.setSpecialite(rs.getString("specialite"));
                m.setNum_ordre(rs.getString("num_ordre"));
                list.add(m);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }


}
