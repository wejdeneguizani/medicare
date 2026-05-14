package services;

import interfaces.IService;
import models.PlanCoaching;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePlanCoaching implements IService<PlanCoaching> {

    private final Connection cnx = MyDataBase.getInstance().getCnx();

    @Override
    public void add(PlanCoaching pc) {
        String req = "INSERT INTO plan_coaching (id_medecin, id_patient, titre, " +
                "date_debut, date_fin, statut, intensite, objectif_global) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, pc.getId_medecin());
            ps.setInt(2, pc.getId_patient());
            ps.setString(3, pc.getTitre());
            ps.setDate(4, Date.valueOf(pc.getDate_debut()));
            ps.setDate(5, pc.getDate_fin() != null ? Date.valueOf(pc.getDate_fin()) : null);
            ps.setString(6, pc.getStatut());
            ps.setString(7, pc.getIntensite());
            ps.setString(8, pc.getObjectif_global());
            ps.executeUpdate();
            System.out.println("Plan coaching ajouté ✓");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<PlanCoaching> getAll() {
        List<PlanCoaching> list = new ArrayList<>();
        String req = "SELECT * FROM plan_coaching";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs  = stm.executeQuery(req);
            while (rs.next()) {
                PlanCoaching pc = new PlanCoaching();
                pc.setId_plan_coaching(rs.getInt("id_plan_coaching"));
                pc.setId_medecin(rs.getInt("id_medecin"));
                pc.setId_patient(rs.getInt("id_patient"));
                pc.setTitre(rs.getString("titre"));
                pc.setDate_debut(rs.getDate("date_debut").toLocalDate());
                Date dateFin = rs.getDate("date_fin");
                if (dateFin != null) pc.setDate_fin(dateFin.toLocalDate());
                pc.setStatut(rs.getString("statut"));
                pc.setIntensite(rs.getString("intensite"));
                pc.setObjectif_global(rs.getString("objectif_global"));
                list.add(pc);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public void update(PlanCoaching pc) {
        String req = "UPDATE plan_coaching SET titre=?, date_fin=?, statut=?, " +
                "intensite=?, objectif_global=? WHERE id_plan_coaching=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, pc.getTitre());
            ps.setDate(2, pc.getDate_fin() != null ? Date.valueOf(pc.getDate_fin()) : null);
            ps.setString(3, pc.getStatut());
            ps.setString(4, pc.getIntensite());
            ps.setString(5, pc.getObjectif_global());
            ps.setInt(6, pc.getId_plan_coaching());
            ps.executeUpdate();
            System.out.println("Plan coaching mis à jour ✓");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(PlanCoaching pc) {
        String req = "DELETE FROM plan_coaching WHERE id_plan_coaching=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, pc.getId_plan_coaching());
            ps.executeUpdate();
            System.out.println("Plan coaching supprimé ✓");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}