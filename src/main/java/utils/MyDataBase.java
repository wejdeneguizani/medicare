package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {

    private static MyDataBase instance;
    private Connection cnx;

    private MyDataBase() {
        try {
            cnx = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/gestion_medicale",
                    "root",
                    ""
            );
            System.out.println("✅ Connecté à la base de données !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion : " + e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}