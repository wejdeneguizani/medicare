package com.medical.utils;  // ← changer config en utils

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // ⚠️ Modifie PASSWORD avec ton mot de passe MySQL
    private static final String URL      = "jdbc:mysql://localhost:3306/medical_app?useSSL=false&serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "";  // ← ton mot de passe ici

    private static Connection instance = null;

    private DatabaseConnection() {}

    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion réussie !");
        }
        return instance;
    }

    public static void closeConnection() {
        if (instance != null) {
            try {
                instance.close();
                System.out.println("🔌 Connexion fermée.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}