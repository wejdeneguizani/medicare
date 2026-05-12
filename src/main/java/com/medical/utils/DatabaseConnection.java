package com.medical.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/medical_app";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // Retourne toujours une nouvelle connexion fraîche
    public static Connection getInstance() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}