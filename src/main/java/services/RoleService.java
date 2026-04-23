package services;

import models.Role;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleService {

    private Connection conn() throws SQLException {
        return DatabaseConnection.getInstance();
    }

    public List<Role> getAll() throws SQLException {
        List<Role> liste = new ArrayList<>();
        String sql = "SELECT * FROM roles ORDER BY id";
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(new Role(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description")
                ));
            }
        }
        return liste;
    }

    public Role getById(int id) throws SQLException {
        String sql = "SELECT * FROM roles WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Role(rs.getInt("id"), rs.getString("nom"), rs.getString("description"));
            }
        }
        return null;
    }
}
