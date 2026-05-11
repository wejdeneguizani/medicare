package com.medical.services;

import com.medical.model.RappelMedicament;
import com.medical.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlerteService {

    public List<RappelMedicament> getTous() {
        List<RappelMedicament> liste = new ArrayList<>();
        String sql = "SELECT * FROM rappel_medicament";
        try (Connection conn = DatabaseConnection.getInstance();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                RappelMedicament r = new RappelMedicament();
                r.setId(rs.getInt("id_rappel"));
                r.setMedicament(rs.getString("medicament"));
                r.setHeure(rs.getString("heure"));
                r.setFrequence(rs.getString("frequence"));
                r.setNote(rs.getString("note"));
                r.setStatut(rs.getString("statut"));
                liste.add(r);
            }
        } catch (SQLException e) {
            System.out.println(" Erreur getTous alertes : " + e.getMessage());
        }
        return liste;
    }

    public boolean ajouter(RappelMedicament r) {
        String sql = "INSERT INTO rappel_medicament (medicament, heure, frequence, note, statut) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getMedicament());
            ps.setString(2, r.getHeure());
            ps.setString(3, r.getFrequence());
            ps.setString(4, r.getNote());
            ps.setString(5, r.getStatut());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) r.setId(keys.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.out.println(" Erreur ajouter alerte : " + e.getMessage());
        }
        return false;
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM rappel_medicament WHERE id_rappel = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(" Erreur supprimer alerte : " + e.getMessage());
        }
        return false;
    }

    public boolean mettreAJourStatut(int id, String statut) {
        String sql = "UPDATE rappel_medicament SET statut = ? WHERE id_rappel = ?";
        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(" Erreur update statut : " + e.getMessage());
        }
        return false;
    }
}