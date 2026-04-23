package services;

import interfaces.IService;
import models.PlanCoaching;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePlanCoaching implements IService<PlanCoaching> {

    private Connection cnx = MyDataBase.getInstance().getCnx();

    // =============================================
    // AJOUTER un plan de coaching
    // =============================================
    @Override
    public void add(PlanCoaching p) {
        String req = "INSERT INTO plan_coaching "
                + "(id_patient, id_medecin, titre_plan, description, "
                + "type_coaching, frequence, date_fin, actif) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt    (1, p.getId_patient());
            ps.setInt    (2, p.getId_medecin());
            ps.setString (3, p.getTitre_plan());
            ps.setString (4, p.getDescription());
            ps.setString (5, p.getType_coaching());
            ps.setString (6, p.getFrequence());
            ps.setDate   (7, new java.sql.Date(p.getDate_fin().getTime()));
            ps.setBoolean(8, p.isActif());

            ps.executeUpdate();
            System.out.println("✅ Plan coaching ajouté : " + p.getTitre_plan());

        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout plan : " + e.getMessage());
        }
    }

    // =============================================
    // AFFICHER tous les plans
    // =============================================
    @Override
    public List<PlanCoaching> getAll() {
        List<PlanCoaching> liste = new ArrayList<>();
        String req = "SELECT * FROM plan_coaching";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                PlanCoaching p = new PlanCoaching();
                p.setId_plan      (rs.getInt    ("id_plan"));
                p.setId_patient   (rs.getInt    ("id_patient"));
                p.setId_medecin   (rs.getInt    ("id_medecin"));
                p.setTitre_plan   (rs.getString ("titre_plan"));
                p.setDescription  (rs.getString ("description"));
                p.setType_coaching(rs.getString ("type_coaching"));
                p.setFrequence    (rs.getString ("frequence"));
                p.setDate_creation(rs.getDate   ("date_creation"));
                p.setDate_fin     (rs.getDate   ("date_fin"));
                p.setActif        (rs.getBoolean("actif"));
                liste.add(p);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur affichage plans : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // AFFICHER les plans d'un seul patient
    // =============================================
    public List<PlanCoaching> getByPatient(int id_patient) {
        List<PlanCoaching> liste = new ArrayList<>();
        String req = "SELECT * FROM plan_coaching WHERE id_patient=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id_patient);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PlanCoaching p = new PlanCoaching();
                p.setId_plan      (rs.getInt    ("id_plan"));
                p.setId_patient   (rs.getInt    ("id_patient"));
                p.setId_medecin   (rs.getInt    ("id_medecin"));
                p.setTitre_plan   (rs.getString ("titre_plan"));
                p.setDescription  (rs.getString ("description"));
                p.setType_coaching(rs.getString ("type_coaching"));
                p.setFrequence    (rs.getString ("frequence"));
                p.setDate_creation(rs.getDate   ("date_creation"));
                p.setDate_fin     (rs.getDate   ("date_fin"));
                p.setActif        (rs.getBoolean("actif"));
                liste.add(p);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur recherche patient : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // MODIFIER un plan
    // =============================================
    @Override
    public void update(PlanCoaching p) {
        String req = "UPDATE plan_coaching SET "
                + "titre_plan=?, type_coaching=?, "
                + "frequence=?, date_fin=?, actif=? "
                + "WHERE id_plan=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString (1, p.getTitre_plan());
            ps.setString (2, p.getType_coaching());
            ps.setString (3, p.getFrequence());
            ps.setDate   (4, new java.sql.Date(p.getDate_fin().getTime()));
            ps.setBoolean(5, p.isActif());
            ps.setInt    (6, p.getId_plan());

            ps.executeUpdate();
            System.out.println("✅ Plan modifié : " + p.getTitre_plan());

        } catch (SQLException e) {
            System.out.println("❌ Erreur modification plan : " + e.getMessage());
        }
    }

    // =============================================
    // SUPPRIMER un plan
    // =============================================
    @Override
    public void delete(PlanCoaching p) {
        String req = "DELETE FROM plan_coaching WHERE id_plan=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, p.getId_plan());
            ps.executeUpdate();
            System.out.println("✅ Plan supprimé : id=" + p.getId_plan());

        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression plan : " + e.getMessage());
        }
    }
}