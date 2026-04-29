package tn.esprit.medicare.services;

import tn.esprit.medicare.entities.MesureSante;
import tn.esprit.medicare.interfaces.IService;
import tn.esprit.medicare.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesureSanteService implements IService<MesureSante> {

    private final Connection connection;

    public MesureSanteService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void add(MesureSante mesure) throws SQLException {
        String sql = "INSERT INTO mesures_sante (user_id, habitude_id, pas, eau_litres, tension_systolique, tension_diastolique, calories, poids_kg, sommeil_heures, date_mesure) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, mesure.getUserId());
            ps.setInt(2, mesure.getHabitudeId());
            ps.setInt(3, mesure.getPas());
            ps.setDouble(4, mesure.getEauLitres());
            if (mesure.getTensionSystolique() == null) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, mesure.getTensionSystolique());
            if (mesure.getTensionDiastolique() == null) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, mesure.getTensionDiastolique());
            if (mesure.getCalories() == null) ps.setNull(7, Types.DOUBLE);
            else ps.setDouble(7, mesure.getCalories());
            if (mesure.getPoidsKg() == null) ps.setNull(8, Types.DOUBLE);
            else ps.setDouble(8, mesure.getPoidsKg());
            if (mesure.getSommeilHeures() == null) ps.setNull(9, Types.DOUBLE);
            else ps.setDouble(9, mesure.getSommeilHeures());
            ps.setTimestamp(10, Timestamp.valueOf(mesure.getDateMesure()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    mesure.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(MesureSante mesure) throws SQLException {
        String sql = "UPDATE mesures_sante SET habitude_id=?, pas=?, eau_litres=?, tension_systolique=?, tension_diastolique=?, calories=?, poids_kg=?, sommeil_heures=?, date_mesure=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, mesure.getHabitudeId());
            ps.setInt(2, mesure.getPas());
            ps.setDouble(3, mesure.getEauLitres());
            if (mesure.getTensionSystolique() == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, mesure.getTensionSystolique());
            if (mesure.getTensionDiastolique() == null) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, mesure.getTensionDiastolique());
            if (mesure.getCalories() == null) ps.setNull(6, Types.DOUBLE);
            else ps.setDouble(6, mesure.getCalories());
            if (mesure.getPoidsKg() == null) ps.setNull(7, Types.DOUBLE);
            else ps.setDouble(7, mesure.getPoidsKg());
            if (mesure.getSommeilHeures() == null) ps.setNull(8, Types.DOUBLE);
            else ps.setDouble(8, mesure.getSommeilHeures());
            ps.setTimestamp(9, Timestamp.valueOf(mesure.getDateMesure()));
            ps.setInt(10, mesure.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM mesures_sante WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public MesureSante getById(int id) throws SQLException {
        String sql = "SELECT * FROM mesures_sante WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapMesure(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<MesureSante> getAll() throws SQLException {
        String sql = "SELECT * FROM mesures_sante ORDER BY id";
        List<MesureSante> mesures = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                mesures.add(mapMesure(rs));
            }
        }
        return mesures;
    }

    public List<MesureSante> getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM mesures_sante WHERE user_id=? ORDER BY id";
        List<MesureSante> mesures = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mesures.add(mapMesure(rs));
                }
            }
        }
        return mesures;
    }

    public List<MesureSante> search(String query) throws SQLException {
        if (query == null || query.trim().isEmpty()) return getAll();
        String sql = "SELECT m.* FROM mesures_sante m JOIN habitudes h ON m.habitude_id = h.id WHERE h.titre LIKE ?";
        
        Double numQuery = null;
        try {
            numQuery = Double.parseDouble(query);
            sql += " OR m.pas = ? OR m.calories = ? OR m.poids_kg = ?";
        } catch (NumberFormatException ignored) {}

        sql += " ORDER BY m.date_mesure DESC";

        List<MesureSante> mesures = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            if (numQuery != null) {
                ps.setDouble(2, numQuery);
                ps.setDouble(3, numQuery);
                ps.setDouble(4, numQuery);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mesures.add(mapMesure(rs));
                }
            }
        }
        return mesures;
    }

    public List<MesureSante> searchBySteps(int minSteps) throws SQLException {
        String sql = "SELECT * FROM mesures_sante WHERE pas >= ? ORDER BY pas DESC";
        List<MesureSante> mesures = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, minSteps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mesures.add(mapMesure(rs));
                }
            }
        }
        return mesures;
    }

    public List<MesureSante> searchByMaxCalories(double maxCal) throws SQLException {
        String sql = "SELECT * FROM mesures_sante WHERE calories <= ? ORDER BY calories ASC";
        List<MesureSante> mesures = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, maxCal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mesures.add(mapMesure(rs));
                }
            }
        }
        return mesures;
    }

    public List<MesureSante> sort(String column, boolean ascending) throws SQLException {
        String order = ascending ? "ASC" : "DESC";
        String sql = "SELECT * FROM mesures_sante ORDER BY " + column + " " + order;
        List<MesureSante> mesures = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                mesures.add(mapMesure(rs));
            }
        }
        return mesures;
    }

    private MesureSante mapMesure(ResultSet rs) throws SQLException {
        MesureSante mesure = new MesureSante();
        mesure.setId(rs.getInt("id"));
        mesure.setUserId(rs.getInt("user_id"));
        mesure.setHabitudeId(rs.getInt("habitude_id"));
        mesure.setPas(rs.getInt("pas"));
        mesure.setEauLitres(rs.getDouble("eau_litres"));
        int tensionSys = rs.getInt("tension_systolique");
        mesure.setTensionSystolique(rs.wasNull() ? null : tensionSys);
        int tensionDia = rs.getInt("tension_diastolique");
        mesure.setTensionDiastolique(rs.wasNull() ? null : tensionDia);
        double calories = rs.getDouble("calories");
        mesure.setCalories(rs.wasNull() ? null : calories);
        double poids = rs.getDouble("poids_kg");
        mesure.setPoidsKg(rs.wasNull() ? null : poids);
        double sommeil = rs.getDouble("sommeil_heures");
        mesure.setSommeilHeures(rs.wasNull() ? null : sommeil);

        Timestamp dateMesure = rs.getTimestamp("date_mesure");
        if (dateMesure != null) {
            mesure.setDateMesure(dateMesure.toLocalDateTime());
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            mesure.setCreatedAt(createdAt.toLocalDateTime());
        }
        return mesure;
    }
}
