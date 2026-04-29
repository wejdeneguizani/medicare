package com.medical.dao;

import com.medical.model.Forme;
import com.medical.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormeDAO {

    public boolean ajouter(Forme f) {
        String sql = "INSERT INTO forme_pharmaceutique (libelle, voie_administration) VALUES (?, ?)";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setString(1, f.getLibelle());
            ps.setString(2, f.getVoieAdministration());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout forme : " + e.getMessage());
            return false;
        }
    }

    public List<Forme> getTous() {
        List<Forme> liste = new ArrayList<>();
        String sql = "SELECT * FROM forme_pharmaceutique";
        try {
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Forme f = new Forme();
                f.setIdForme(rs.getInt("id_forme"));
                f.setLibelle(rs.getString("libelle"));
                f.setVoieAdministration(rs.getString("voie_administration"));
                liste.add(f);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur affichage formes : " + e.getMessage());
        }
        return liste;
    }

    public Forme getParId(int id) {
        String sql = "SELECT * FROM forme_pharmaceutique WHERE id_forme = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Forme f = new Forme();
                f.setIdForme(rs.getInt("id_forme"));
                f.setLibelle(rs.getString("libelle"));
                f.setVoieAdministration(rs.getString("voie_administration"));
                return f;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur recherche forme : " + e.getMessage());
        }
        return null;
    }

    public boolean modifier(Forme f) {
        String sql = "UPDATE forme_pharmaceutique SET libelle=?, voie_administration=? WHERE id_forme=?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setString(1, f.getLibelle());
            ps.setString(2, f.getVoieAdministration());
            ps.setInt(3, f.getIdForme());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur modification forme : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM forme_pharmaceutique WHERE id_forme = ?";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression forme : " + e.getMessage());
            return false;
        }
    }
}
