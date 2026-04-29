package services;

import interfaces.IService;
import models.Patient;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePatient implements IService<Patient> {

    @Override
    public void add(Patient p) {
        String req = "INSERT INTO patient(nom, prenom, email, telephone, date_naissance, groupe_sanguin, adresse) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getTelephone());
            ps.setString(5, p.getDate_naissance());
            ps.setString(6, p.getGroupe_sanguin());
            ps.setString(7, p.getAdresse());
            ps.executeUpdate();
            System.out.println("Patient ajouté !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Patient> getAll() {
        List<Patient> list = new ArrayList<>();
        String req = "SELECT * FROM patient";
        try {
            Statement stm = MyDataBase.getInstance().getCnx().createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                Patient p = new Patient();
                p.setId_patient(rs.getInt("id_patient"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setEmail(rs.getString("email"));
                p.setTelephone(rs.getString("telephone"));
                p.setDate_naissance(rs.getString("date_naissance"));
                p.setGroupe_sanguin(rs.getString("groupe_sanguin"));
                p.setAdresse(rs.getString("adresse"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Patient p) {
        String req = "UPDATE patient SET nom=?, prenom=?, email=?, telephone=?, date_naissance=?, groupe_sanguin=?, adresse=? WHERE id_patient=?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getTelephone());
            ps.setString(5, p.getDate_naissance());
            ps.setString(6, p.getGroupe_sanguin());
            ps.setString(7, p.getAdresse());
            ps.setInt(8, p.getId_patient());
            ps.executeUpdate();
            System.out.println("Patient modifié !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Patient p) {
        String req = "DELETE FROM patient WHERE id_patient=?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setInt(1, p.getId_patient());
            ps.executeUpdate();
            System.out.println("Patient supprimé !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Patient> rechercherParNom(String nom) {
        List<Patient> list = new ArrayList<>();
        String req = "SELECT * FROM patient WHERE nom LIKE ?";
        try {
            PreparedStatement ps = MyDataBase.getInstance().getCnx().prepareStatement(req);
            ps.setString(1, "%" + nom + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Patient p = new Patient();
                p.setId_patient(rs.getInt("id_patient"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setEmail(rs.getString("email"));
                p.setTelephone(rs.getString("telephone"));
                p.setDate_naissance(rs.getString("date_naissance"));
                p.setGroupe_sanguin(rs.getString("groupe_sanguin"));
                p.setAdresse(rs.getString("adresse"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Patient> trierParNom() {
        List<Patient> list = new ArrayList<>();
        String req = "SELECT * FROM patient ORDER BY nom ASC";
        try {
            Statement stm = MyDataBase.getInstance().getCnx().createStatement();
            ResultSet rs = stm.executeQuery(req);
            while (rs.next()) {
                Patient p = new Patient();
                p.setId_patient(rs.getInt("id_patient"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setEmail(rs.getString("email"));
                p.setTelephone(rs.getString("telephone"));
                p.setDate_naissance(rs.getString("date_naissance"));
                p.setGroupe_sanguin(rs.getString("groupe_sanguin"));
                p.setAdresse(rs.getString("adresse"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }


}





