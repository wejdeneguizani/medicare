package services;

import interfaces.IService;
import models.Objectif;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceObjectif implements IService<Objectif> {

    private Connection cnx = MyDataBase.getInstance().getCnx();

    // =============================================
    // AJOUTER un objectif
    // =============================================
    @Override
    public void add(Objectif o) {
        String req = "INSERT INTO objectif "
                + "(id_patient, id_medecin, titre, type_objectif, "
                + "valeur_initiale, valeur_cible, unite, "
                + "date_debut, date_echeance, statut, priorite, description) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt   (1,  o.getId_patient());
            ps.setInt   (2,  o.getId_medecin());
            ps.setString(3,  o.getTitre());
            ps.setString(4,  o.getType_objectif());
            ps.setDouble(5,  o.getValeur_initiale());
            ps.setDouble(6,  o.getValeur_cible());
            ps.setString(7,  o.getUnite());
            ps.setDate  (8,  new java.sql.Date(o.getDate_debut().getTime()));
            ps.setDate  (9,  new java.sql.Date(o.getDate_echeance().getTime()));
            ps.setString(10, "en_cours");
            ps.setInt   (11, o.getPriorite());
            ps.setString(12, o.getDescription());

            ps.executeUpdate();
            System.out.println("✅ Objectif ajouté : " + o.getTitre());

        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout : " + e.getMessage());
        }
    }

    // =============================================
    // AFFICHER tous les objectifs
    // =============================================
    @Override
    public List<Objectif> getAll() {
        List<Objectif> liste = new ArrayList<>();
        String req = "SELECT * FROM objectif";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Objectif o = new Objectif();
                o.setId_objectif    (rs.getInt   ("id_objectif"));
                o.setId_patient     (rs.getInt   ("id_patient"));
                o.setId_medecin     (rs.getInt   ("id_medecin"));
                o.setTitre          (rs.getString("titre"));
                o.setType_objectif  (rs.getString("type_objectif"));
                o.setValeur_initiale(rs.getDouble("valeur_initiale"));
                o.setValeur_cible   (rs.getDouble("valeur_cible"));
                o.setUnite          (rs.getString("unite"));
                o.setDate_debut     (rs.getDate  ("date_debut"));
                o.setDate_echeance  (rs.getDate  ("date_echeance"));
                o.setStatut         (rs.getString("statut"));
                o.setPriorite       (rs.getInt   ("priorite"));
                o.setDescription    (rs.getString("description"));
                liste.add(o);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur affichage : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // MODIFIER un objectif
    // =============================================
    @Override
    public void update(Objectif o) {
        String req = "UPDATE objectif SET "
                + "titre=?, type_objectif=?, valeur_cible=?, "
                + "unite=?, statut=?, priorite=?, description=? "
                + "WHERE id_objectif=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, o.getTitre());
            ps.setString(2, o.getType_objectif());
            ps.setDouble(3, o.getValeur_cible());
            ps.setString(4, o.getUnite());
            ps.setString(5, o.getStatut());
            ps.setInt   (6, o.getPriorite());
            ps.setString(7, o.getDescription());
            ps.setInt   (8, o.getId_objectif());

            ps.executeUpdate();
            System.out.println("✅ Objectif modifié : " + o.getTitre());

        } catch (SQLException e) {
            System.out.println("❌ Erreur modification : " + e.getMessage());
        }
    }

    // =============================================
    // SUPPRIMER un objectif
    // =============================================
    @Override
    public void delete(Objectif o) {
        String req = "DELETE FROM objectif WHERE id_objectif=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, o.getId_objectif());
            ps.executeUpdate();
            System.out.println("✅ Objectif supprimé : id=" + o.getId_objectif());

        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression : " + e.getMessage());
        }
    }
}