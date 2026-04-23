package com.medical.dao;

import com.medical.utils.DatabaseConnection;
import com.medical.model.Fabricant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FabricantDAO {

    public boolean ajouter(Fabricant f) {
        String sql = "INSERT INTO fabricant (nom, pays, contact, site_web) VALUES (?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, f.getNom());
            ps.setString(2, f.getPays());
            ps.setString(3, f.getContact());
            ps.setString(4, f.getSiteWeb());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout fabricant : " + e.getMessage());
            return false;
        }
    }

    public List<Fabricant> getTous() {
        List<Fabricant> liste = new ArrayList<>();
        String sql = "SELECT * FROM fabricant";
        try {
            Connection conn = DatabaseConnection.getInstance();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Fabricant f = new Fabricant();
                f.setIdFabricant(rs.getInt("id_fabricant"));
                f.setNom(rs.getString("nom"));
                f.setPays(rs.getString("pays"));
                f.setContact(rs.getString("contact"));
                f.setSiteWeb(rs.getString("site_web"));
                liste.add(f);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur affichage fabricants : " + e.getMessage());
        }
        return liste;
    }

    public Fabricant getParId(int id) {
        String sql = "SELECT * FROM fabricant WHERE id_fabricant = ?";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Fabricant f = new Fabricant();
                f.setIdFabricant(rs.getInt("id_fabricant"));
                f.setNom(rs.getString("nom"));
                f.setPays(rs.getString("pays"));
                f.setContact(rs.getString("contact"));
                f.setSiteWeb(rs.getString("site_web"));
                return f;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur recherche fabricant : " + e.getMessage());
        }
        return null;
    }

    public boolean modifier(Fabricant f) {
        String sql = "UPDATE fabricant SET nom=?, pays=?, contact=?, site_web=? WHERE id_fabricant=?";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, f.getNom());
            ps.setString(2, f.getPays());
            ps.setString(3, f.getContact());
            ps.setString(4, f.getSiteWeb());
            ps.setInt(5, f.getIdFabricant());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur modification fabricant : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM fabricant WHERE id_fabricant = ?";
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression fabricant : " + e.getMessage());
            return false;
        }
    }
}