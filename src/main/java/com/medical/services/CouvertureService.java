package com.medical.services;

import com.medical.interfaces.IService;
import com.medical.model.Couverture;
import com.medical.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouvertureService implements IService<Couverture> {
    private static final String TABLE_COUVERTURE = "couverture";

    @Override
    public boolean ajouter(Couverture c) {
        String sql = "INSERT INTO " + TABLE_COUVERTURE + " (id_assurance,type_service,pourcentage_couverture,montant_max,condition_speciale) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps = DatabaseConnection.getInstance().prepareStatement(sql);
            ps.setInt(1, c.getIdAssurance()); ps.setString(2, c.getTypeService()); ps.setDouble(3, c.getPourcentageCouverture()); ps.setDouble(4, c.getMontantMax()); ps.setString(5, c.getConditionSpeciale());
            ps.executeUpdate(); return true;
        } catch (SQLException e) { System.out.println("❌ Erreur ajout couverture : " + e.getMessage()); return false; }
    }
    @Override public List<Couverture> getTous() {
        List<Couverture> l=new ArrayList<>();
        try { ResultSet rs=DatabaseConnection.getInstance().createStatement().executeQuery("SELECT * FROM " + TABLE_COUVERTURE); while(rs.next()) l.add(map(rs)); } catch(SQLException e){System.out.println(e.getMessage());}
        return l;
    }
    @Override public Couverture getParId(int id) {
        try { PreparedStatement ps=DatabaseConnection.getInstance().prepareStatement("SELECT * FROM " + TABLE_COUVERTURE + " WHERE id_couverture=?"); ps.setInt(1,id); ResultSet rs=ps.executeQuery(); if(rs.next()) return map(rs); } catch(SQLException e){System.out.println(e.getMessage());}
        return null;
    }
    @Override public boolean modifier(Couverture c) {
        String sql="UPDATE " + TABLE_COUVERTURE + " SET id_assurance=?,type_service=?,pourcentage_couverture=?,montant_max=?,condition_speciale=? WHERE id_couverture=?";
        try { PreparedStatement ps=DatabaseConnection.getInstance().prepareStatement(sql); ps.setInt(1,c.getIdAssurance()); ps.setString(2,c.getTypeService()); ps.setDouble(3,c.getPourcentageCouverture()); ps.setDouble(4,c.getMontantMax()); ps.setString(5,c.getConditionSpeciale()); ps.setInt(6,c.getIdCouverture()); ps.executeUpdate(); return true; } catch(SQLException e){System.out.println(e.getMessage()); return false;}
    }
    @Override public boolean supprimer(int id) {
        try { PreparedStatement ps=DatabaseConnection.getInstance().prepareStatement("DELETE FROM " + TABLE_COUVERTURE + " WHERE id_couverture=?"); ps.setInt(1,id); ps.executeUpdate(); return true; } catch(SQLException e){System.out.println(e.getMessage()); return false;}
    }
    public List<Couverture> getByAssuranceId(int idAssurance) {
        List<Couverture> l=new ArrayList<>();
        try { PreparedStatement ps=DatabaseConnection.getInstance().prepareStatement("SELECT * FROM " + TABLE_COUVERTURE + " WHERE id_assurance=?"); ps.setInt(1,idAssurance); ResultSet rs=ps.executeQuery(); while(rs.next()) l.add(map(rs)); } catch(SQLException e){System.out.println(e.getMessage());}
        return l;
    }
    public double simulerRemboursement(int idAssurance,String typeService,double montantDepense) {
        String sql="SELECT pourcentage_couverture,montant_max FROM " + TABLE_COUVERTURE + " WHERE id_assurance=? AND type_service=?";
        try { PreparedStatement ps=DatabaseConnection.getInstance().prepareStatement(sql); ps.setInt(1,idAssurance); ps.setString(2,typeService); ResultSet rs=ps.executeQuery(); if(rs.next()){ double m=montantDepense*rs.getDouble("pourcentage_couverture")/100.0; return Math.min(m, rs.getDouble("montant_max")); }} catch(SQLException e){System.out.println(e.getMessage());}
        return 0;
    }
    private Couverture map(ResultSet rs)throws SQLException{ Couverture c=new Couverture(); c.setIdCouverture(rs.getInt("id_couverture")); c.setIdAssurance(rs.getInt("id_assurance")); c.setTypeService(rs.getString("type_service")); c.setPourcentageCouverture(rs.getDouble("pourcentage_couverture")); c.setMontantMax(rs.getDouble("montant_max")); c.setConditionSpeciale(rs.getString("condition_speciale")); return c; }
}
