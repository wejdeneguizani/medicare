package com.medical;

import com.medical.utils.DatabaseConnection;
import com.medical.ui.MenuMedicament;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        // Test de connexion d'abord
        try {
            Connection conn = DatabaseConnection.getInstance();
            System.out.println("🎉 Base de données connectée !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur connexion : " + e.getMessage());
            return; // Arrête le programme si pas de connexion
        }

        // Lance le menu
        MenuMedicament.afficherMenu();
    }
}