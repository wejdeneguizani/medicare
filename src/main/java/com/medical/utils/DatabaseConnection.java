package com.medical.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private final String URL      = "jdbc:mysql://localhost:3306/medical_app";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    private DatabaseConnection() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        System.out.println("✅ Connexion à la base de données réussie !");
    }

    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance.connection;
    }
}
