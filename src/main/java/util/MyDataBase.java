package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {

    private static MyDataBase instance;

    // ✅ Remplace "esprit" par "medicare_plus"
    private final String URL      = "jdbc:mysql://127.0.0.1:3306/medicare_plus";
    private final String USERNAME = "root";
    private final String PASSWORD = "";          // mot de passe WAMP (vide par défaut)
    private Connection cnx;

    private MyDataBase() {
        try {
            this.cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Connecté à medicare_plus !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur connexion : " + e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if (instance == null)
            instance = new MyDataBase();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
