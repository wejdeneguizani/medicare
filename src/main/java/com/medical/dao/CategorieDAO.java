package com.medical.dao;

import com.medical.model.Categorie;
import com.medical.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {

    public boolean ajouter(Categorie c) {
        String sql = "INSERT INTO categorie_medicament (libelle, code_atc, description) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setString(1, c.getLibelle());
            ps.setString(2, c.getCodeAtc());
            ps.setString(3, c.getDescription());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout catégorie : " + e.getMessage());
            return false;
        }
    }

    public List<Categorie> getTous() {
        List<Categorie> liste = new ArrayList<>();
        String sql = "SELECT * FROM categorie_medicament";
        try {
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Categorie c = new Categorie();
                c.setIdCategorie(rs.getInt("id_categorie"));
                c.setLibelle(rs.getString("libelle"));
                c.setCodeAtc(rs.getString("code_atc"));
                c.setDescription(rs.getString("description"));
                liste.add(c);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur affichage catégories : " + e.getMessage());
        }
        return liste;
    }

    public Categorie getParId(int id) {
        String sql = "SELECT * FROM categorie_medicament WHERE id_categorie = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Categorie c = new Categorie();
                c.setIdCategorie(rs.getInt("id_categorie"));
                c.setLibelle(rs.getString("libelle"));
                c.setCodeAtc(rs.getString("code_atc"));
                c.setDescription(rs.getString("description"));
                return c;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur recherche catégorie : " + e.getMessage());
        }
        return null;
    }

    public boolean modifier(Categorie c) {
        String sql = "UPDATE categorie_medicament SET libelle=?, code_atc=?, description=? WHERE id_categorie=?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setString(1, c.getLibelle());
            ps.setString(2, c.getCodeAtc());
            ps.setString(3, c.getDescription());
            ps.setInt(4, c.getIdCategorie());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur modification catégorie : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM categorie_medicament WHERE id_categorie = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression catégorie : " + e.getMessage());
            return false;
        }
    }
}
