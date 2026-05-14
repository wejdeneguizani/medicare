package services;

import interfaces.IService;
import models.Progression;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProgression implements IService<Progression> {

    private final Connection cnx = MyDataBase.getInstance().getCnx();

    @Override
    public void add(Progression p) {
        String req = "INSERT INTO progression_objectif " +
                "(id_objectif, date_mesure, valeur_actuelle, valeur_cible, humeur, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, p.getId_objectif());
            ps.setDate(2, Date.valueOf(p.getDate_mesure()));
            ps.setFloat(3, p.getValeur_actuelle());
            ps.setFloat(4, p.getValeur_cible());
            ps.setString(5, p.getHumeur());
            ps.setString(6, p.getNotes());
            ps.executeUpdate();
            System.out.println("Progression ajoutée ✓");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Progression> getAll() {
        List<Progression> list = new ArrayList<>();
        String req = "SELECT * FROM progression_objectif";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs  = stm.executeQuery(req);
            while (rs.next()) {
                Progression p = new Progression();
                p.setId_progression(rs.getInt("id_progression"));
                p.setId_objectif(rs.getInt("id_objectif"));
                p.setDate_mesure(rs.getDate("date_mesure").toLocalDate());
                p.setValeur_actuelle(rs.getFloat("valeur_actuelle"));
                p.setValeur_cible(rs.getFloat("valeur_cible"));
                p.setHumeur(rs.getString("humeur"));
                p.setNotes(rs.getString("notes"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public void update(Progression p) {
        String req = "UPDATE progression_objectif SET valeur_actuelle=?, humeur=?, notes=? " +
                "WHERE id_progression=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setFloat(1, p.getValeur_actuelle());
            ps.setString(2, p.getHumeur());
            ps.setString(3, p.getNotes());
            ps.setInt(4, p.getId_progression());
            ps.executeUpdate();
            System.out.println("Progression mise à jour ✓");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Progression p) {
        String req = "DELETE FROM progression_objectif WHERE id_progression=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, p.getId_progression());
            ps.executeUpdate();
            System.out.println("Progression supprimée ✓");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}