package tn.esprit.medicare.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

    public static void initialize() {
        Connection connection = DBConnection.getInstance().getConnection();
        if (connection == null) return;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS habitudes (
                        id INT NOT NULL AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        type VARCHAR(30) NOT NULL,
                        titre VARCHAR(100) NOT NULL,
                        description VARCHAR(255),
                        objectif_valeur DECIMAL(8,2) NOT NULL,
                        unite VARCHAR(20) NOT NULL,
                        active TINYINT(1) NOT NULL DEFAULT 1,
                        date_debut DATE NOT NULL,
                        date_fin DATE NULL,
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_habitudes_user FOREIGN KEY (user_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
                    )
                    """);

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS mesures_sante (
                        id INT NOT NULL AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        habitude_id INT NOT NULL,
                        pas INT NOT NULL DEFAULT 0,
                        eau_litres DECIMAL(5,2) NOT NULL DEFAULT 0.00,
                        tension_systolique INT NULL,
                        tension_diastolique INT NULL,
                        calories DECIMAL(8,2) NULL,
                        poids_kg DECIMAL(6,2) NULL,
                        sommeil_heures DECIMAL(4,2) NULL,
                        date_mesure DATETIME NOT NULL,
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_mesures_user FOREIGN KEY (user_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
                        CONSTRAINT fk_mesures_habitude FOREIGN KEY (habitude_id) REFERENCES habitudes(id) ON DELETE CASCADE
                    )
                    """);

            System.out.println("Module mesures sante & habitudes tables initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing mesures sante & habitudes tables: " + e.getMessage());
        }
    }
}


