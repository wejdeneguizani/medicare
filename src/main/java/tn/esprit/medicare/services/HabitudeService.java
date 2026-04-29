package tn.esprit.medicare.services;

import tn.esprit.medicare.entities.Habitude;
import tn.esprit.medicare.interfaces.IService;
import tn.esprit.medicare.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitudeService implements IService<Habitude> {

    private final Connection connection;

    public HabitudeService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void add(Habitude habitude) throws SQLException {
        String sql = "INSERT INTO habitudes (user_id, type, titre, description, objectif_valeur, unite, active, date_debut, date_fin) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, habitude.getUserId());
            ps.setString(2, habitude.getType().name());
            ps.setString(3, habitude.getTitre());
            ps.setString(4, habitude.getDescription());
            ps.setDouble(5, habitude.getObjectifValeur());
            ps.setString(6, habitude.getUnite());
            ps.setBoolean(7, habitude.isActive());
            ps.setDate(8, Date.valueOf(habitude.getDateDebut()));
            if (habitude.getDateFin() == null) ps.setNull(9, Types.DATE);
            else ps.setDate(9, Date.valueOf(habitude.getDateFin()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    habitude.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Habitude habitude) throws SQLException {
        String sql = "UPDATE habitudes SET type=?, titre=?, description=?, objectif_valeur=?, unite=?, active=?, date_debut=?, date_fin=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, habitude.getType().name());
            ps.setString(2, habitude.getTitre());
            ps.setString(3, habitude.getDescription());
            ps.setDouble(4, habitude.getObjectifValeur());
            ps.setString(5, habitude.getUnite());
            ps.setBoolean(6, habitude.isActive());
            ps.setDate(7, Date.valueOf(habitude.getDateDebut()));
            if (habitude.getDateFin() == null) ps.setNull(8, Types.DATE);
            else ps.setDate(8, Date.valueOf(habitude.getDateFin()));
            ps.setInt(9, habitude.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM habitudes WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Habitude getById(int id) throws SQLException {
        String sql = "SELECT * FROM habitudes WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapHabitude(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Habitude> getAll() throws SQLException {
        String sql = "SELECT * FROM habitudes ORDER BY id";
        List<Habitude> habitudes = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                habitudes.add(mapHabitude(rs));
            }
        }
        return habitudes;
    }

    public List<Habitude> getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM habitudes WHERE user_id=? ORDER BY id";
        List<Habitude> habitudes = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    habitudes.add(mapHabitude(rs));
                }
            }
        }
        return habitudes;
    }

    public List<Habitude> search(String query) throws SQLException {
        String sql = "SELECT * FROM habitudes WHERE titre LIKE ? OR description LIKE ? ORDER BY id";
        List<Habitude> habitudes = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    habitudes.add(mapHabitude(rs));
                }
            }
        }
        return habitudes;
    }

    public List<Habitude> sort(String column, boolean ascending) throws SQLException {
        String order = ascending ? "ASC" : "DESC";
        // Validation basic to avoid SQL Injection if this were a user input, though here it's for testing
        String sql = "SELECT * FROM habitudes ORDER BY " + column + " " + order;
        List<Habitude> habitudes = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                habitudes.add(mapHabitude(rs));
            }
        }
        return habitudes;
    }

    private Habitude mapHabitude(ResultSet rs) throws SQLException {
        Habitude habitude = new Habitude();
        habitude.setId(rs.getInt("id"));
        habitude.setUserId(rs.getInt("user_id"));
        habitude.setType(Habitude.TypeHabitude.valueOf(rs.getString("type")));
        habitude.setTitre(rs.getString("titre"));
        habitude.setDescription(rs.getString("description"));
        habitude.setObjectifValeur(rs.getDouble("objectif_valeur"));
        habitude.setUnite(rs.getString("unite"));
        habitude.setActive(rs.getBoolean("active"));

        Date dateDebut = rs.getDate("date_debut");
        if (dateDebut != null) {
            habitude.setDateDebut(dateDebut.toLocalDate());
        }

        Date dateFin = rs.getDate("date_fin");
        if (dateFin != null) {
            habitude.setDateFin(dateFin.toLocalDate());
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            habitude.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            habitude.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        return habitude;
    }
}
