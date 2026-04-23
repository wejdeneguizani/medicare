package services;

import interfaces.IService;
import models.ProgressionObjectif;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProgression implements IService<ProgressionObjectif> {

    private Connection cnx = MyDataBase.getInstance().getCnx();

    // =============================================
    // AJOUTER une progression
    // Le trigger MySQL calcule le % automatiquement
    // =============================================
    @Override
    public void add(ProgressionObjectif p) {
        String req = "INSERT INTO progression_objectif "
                + "(id_objectif, id_patient, valeur_actuelle, note_patient) "
                + "VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt   (1, p.getId_objectif());
            ps.setInt   (2, p.getId_patient());
            ps.setDouble(3, p.getValeur_actuelle());
            ps.setString(4, p.getNote_patient());

            ps.executeUpdate();
            System.out.println("✅ Progression enregistrée !");

        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout progression : " + e.getMessage());
        }
    }

    // =============================================
    // AFFICHER toutes les progressions
    // =============================================
    @Override
    public List<ProgressionObjectif> getAll() {
        List<ProgressionObjectif> liste = new ArrayList<>();
        String req = "SELECT * FROM progression_objectif ORDER BY date_mesure DESC";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                ProgressionObjectif p = new ProgressionObjectif();
                p.setId_progression (rs.getInt   ("id_progression"));
                p.setId_objectif    (rs.getInt   ("id_objectif"));
                p.setId_patient     (rs.getInt   ("id_patient"));
                p.setValeur_actuelle(rs.getDouble("valeur_actuelle"));
                p.setPourcentage    (rs.getDouble("pourcentage"));
                p.setDate_mesure    (rs.getDate  ("date_mesure"));
                p.setNote_patient   (rs.getString("note_patient"));
                liste.add(p);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur affichage : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // AFFICHER les progressions d'un seul objectif
    // =============================================
    public List<ProgressionObjectif> getByObjectif(int id_objectif) {
        List<ProgressionObjectif> liste = new ArrayList<>();
        String req = "SELECT * FROM progression_objectif "
                + "WHERE id_objectif=? ORDER BY date_mesure DESC";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id_objectif);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProgressionObjectif p = new ProgressionObjectif();
                p.setId_progression (rs.getInt   ("id_progression"));
                p.setId_objectif    (rs.getInt   ("id_objectif"));
                p.setId_patient     (rs.getInt   ("id_patient"));
                p.setValeur_actuelle(rs.getDouble("valeur_actuelle"));
                p.setPourcentage    (rs.getDouble("pourcentage"));
                p.setDate_mesure    (rs.getDate  ("date_mesure"));
                p.setNote_patient   (rs.getString("note_patient"));
                liste.add(p);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur recherche : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // MODIFIER une progression
    // =============================================
    @Override
    public void update(ProgressionObjectif p) {
        String req = "UPDATE progression_objectif SET "
                + "valeur_actuelle=?, note_patient=? "
                + "WHERE id_progression=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setDouble(1, p.getValeur_actuelle());
            ps.setString(2, p.getNote_patient());
            ps.setInt   (3, p.getId_progression());

            ps.executeUpdate();
            System.out.println("✅ Progression modifiée !");

        } catch (SQLException e) {
            System.out.println("❌ Erreur modification : " + e.getMessage());
        }
    }

    // =============================================
    // SUPPRIMER une progression
    // =============================================
    @Override
    public void delete(ProgressionObjectif p) {
        String req = "DELETE FROM progression_objectif WHERE id_progression=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, p.getId_progression());
            ps.executeUpdate();
            System.out.println("✅ Progression supprimée !");

        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression : " + e.getMessage());
        }
    }
}