package tn.esprit.medicare.main;

import tn.esprit.medicare.entities.Habitude;
import tn.esprit.medicare.entities.MesureSante;
import tn.esprit.medicare.services.HabitudeService;
import tn.esprit.medicare.services.MesureSanteService;
import tn.esprit.medicare.utils.DBConnection;
import tn.esprit.medicare.utils.DBInitializer;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class MainCrudTest {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        DBInitializer.initialize();

        try {
            int userId = getExistingUserId();
            if (userId == -1) {
                System.err.println("No user found in table 'utilisateurs'. Insert one user first, then rerun tests.");
                return;
            }

            System.out.println("Connected with user_id = " + userId);
            runMainMenu(userId);
        } catch (SQLException e) {
            System.err.println("CRUD test failed: " + e.getMessage());
        }
    }

    private static int getExistingUserId() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT id FROM utilisateurs ORDER BY id LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    private static void runMainMenu(int userId) {
        boolean running = true;
        while (running) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1) Habitude CRUD");
            System.out.println("2) Mesure Sante CRUD");
            System.out.println("0) Exit");
            int choice = readInt("Choose option: ");

            switch (choice) {
                case 1 -> runHabitudeMenu(userId);
                case 2 -> runMesureMenu(userId);
                case 0 -> {
                    running = false;
                    System.out.println("Bye bro, see you.");
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void runHabitudeMenu(int userId) {
        HabitudeService service = new HabitudeService();
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Habitude CRUD ---");
            System.out.println("1) Add");
            System.out.println("2) Update");
            System.out.println("3) Delete");
            System.out.println("4) Get by id");
            System.out.println("5) List all");
            System.out.println("0) Back");
            int choice = readInt("Choose option: ");

            try {
                switch (choice) {
                    case 1 -> {
                        Habitude h = new Habitude();
                        h.setUserId(userId);
                        h.setType(readHabitudeType());
                        h.setTitre(readLine("Titre: "));
                        h.setDescription(readLine("Description: "));
                        h.setObjectifValeur(readDouble("Objectif valeur: "));
                        h.setUnite(readLine("Unite: "));
                        h.setActive(readInt("Active (1/0): ") == 1);
                        h.setDateDebut(LocalDate.parse(readLine("Date debut (yyyy-mm-dd): ")));

                        String dateFinInput = readLine("Date fin (yyyy-mm-dd or empty): ");
                        if (!dateFinInput.isBlank()) {
                            h.setDateFin(LocalDate.parse(dateFinInput));
                        }

                        service.add(h);
                        System.out.println("Habitude added with id = " + h.getId());
                    }
                    case 2 -> {
                        int id = readInt("Habitude id to update: ");
                        Habitude existing = service.getById(id);
                        if (existing == null) {
                            System.out.println("Habitude not found.");
                            break;
                        }

                        existing.setType(readHabitudeType());
                        existing.setTitre(readLine("New titre: "));
                        existing.setDescription(readLine("New description: "));
                        existing.setObjectifValeur(readDouble("New objectif valeur: "));
                        existing.setUnite(readLine("New unite: "));
                        existing.setActive(readInt("Active (1/0): ") == 1);
                        existing.setDateDebut(LocalDate.parse(readLine("New date debut (yyyy-mm-dd): ")));

                        String dateFinInput = readLine("New date fin (yyyy-mm-dd or empty): ");
                        if (dateFinInput.isBlank()) {
                            existing.setDateFin(null);
                        } else {
                            existing.setDateFin(LocalDate.parse(dateFinInput));
                        }

                        service.update(existing);
                        System.out.println("Habitude updated.");
                    }
                    case 3 -> {
                        int id = readInt("Habitude id to delete: ");
                        service.delete(id);
                        System.out.println("Habitude deleted.");
                    }
                    case 4 -> {
                        int id = readInt("Habitude id: ");
                        Habitude h = service.getById(id);
                        if (h == null) {
                            System.out.println("Habitude not found.");
                        } else {
                            printHabitude(h);
                        }
                    }
                    case 5 -> {
                        List<Habitude> all = service.getAll();
                        if (all.isEmpty()) {
                            System.out.println("No habitudes found.");
                        } else {
                            all.forEach(MainCrudTest::printHabitude);
                        }
                    }
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private static void runMesureMenu(int userId) {
        MesureSanteService service = new MesureSanteService();
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Mesure Sante CRUD ---");
            System.out.println("1) Add");
            System.out.println("2) Update");
            System.out.println("3) Delete");
            System.out.println("4) Get by id");
            System.out.println("5) List all");
            System.out.println("0) Back");
            int choice = readInt("Choose option: ");

            try {
                switch (choice) {
                    case 1 -> {
                        MesureSante m = new MesureSante();
                        m.setUserId(userId);
                        m.setHabitudeId(readInt("Habitude id: "));
                        m.setPas(readInt("Pas: "));
                        m.setEauLitres(readDouble("Eau litres: "));
                        m.setTensionSystolique(readOptionalInt("Tension systolique (empty = null): "));
                        m.setTensionDiastolique(readOptionalInt("Tension diastolique (empty = null): "));
                        m.setCalories(readOptionalDouble("Calories (empty = null): "));
                        m.setPoidsKg(readOptionalDouble("Poids Kg (empty = null): "));
                        m.setSommeilHeures(readOptionalDouble("Sommeil heures (empty = null): "));
                        m.setDateMesure(LocalDateTime.now());
                        System.out.println("Date mesure auto-set to now: " + m.getDateMesure());

                        service.add(m);
                        System.out.println("Mesure added with id = " + m.getId());
                    }
                    case 2 -> {
                        int id = readInt("Mesure id to update: ");
                        MesureSante existing = service.getById(id);
                        if (existing == null) {
                            System.out.println("Mesure not found.");
                            break;
                        }

                        existing.setHabitudeId(readInt("New habitude id: "));
                        existing.setPas(readInt("New pas: "));
                        existing.setEauLitres(readDouble("New eau litres: "));
                        existing.setTensionSystolique(readOptionalInt("New tension systolique (empty = null): "));
                        existing.setTensionDiastolique(readOptionalInt("New tension diastolique (empty = null): "));
                        existing.setCalories(readOptionalDouble("New calories (empty = null): "));
                        existing.setPoidsKg(readOptionalDouble("New poids Kg (empty = null): "));
                        existing.setSommeilHeures(readOptionalDouble("New sommeil heures (empty = null): "));
                        existing.setDateMesure(LocalDateTime.now());
                        System.out.println("Date mesure auto-updated to now: " + existing.getDateMesure());

                        service.update(existing);
                        System.out.println("Mesure updated.");
                    }
                    case 3 -> {
                        int id = readInt("Mesure id to delete: ");
                        service.delete(id);
                        System.out.println("Mesure deleted.");
                    }
                    case 4 -> {
                        int id = readInt("Mesure id: ");
                        MesureSante m = service.getById(id);
                        if (m == null) {
                            System.out.println("Mesure not found.");
                        } else {
                            printMesure(m);
                        }
                    }
                    case 5 -> {
                        List<MesureSante> all = service.getAll();
                        if (all.isEmpty()) {
                            System.out.println("No mesures found.");
                        } else {
                            all.forEach(MainCrudTest::printMesure);
                        }
                    }
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private static Habitude.TypeHabitude readHabitudeType() {
        System.out.println("Types: EAU, PAS, SOMMEIL, TENSION, ACTIVITE_PHYSIQUE, AUTRE");
        while (true) {
            try {
                return Habitude.TypeHabitude.valueOf(readLine("Type: ").trim().toUpperCase());
            } catch (Exception ignored) {
                System.out.println("Invalid type, try again.");
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid number, try again.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readLine(prompt));
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid number, try again.");
            }
        }
    }

    private static Integer readOptionalInt(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (value.isBlank()) {
                return null;
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid number, try again.");
            }
        }
    }

    private static Double readOptionalDouble(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (value.isBlank()) {
                return null;
            }
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid number, try again.");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine();
    }

    private static void printHabitude(Habitude h) {
        System.out.println(
                "Habitude{id=" + h.getId() +
                        ", userId=" + h.getUserId() +
                        ", type=" + h.getType() +
                        ", titre='" + h.getTitre() + '\'' +
                        ", objectif=" + h.getObjectifValeur() +
                        ", unite='" + h.getUnite() + '\'' +
                        ", active=" + h.isActive() +
                        ", dateDebut=" + h.getDateDebut() +
                        ", dateFin=" + h.getDateFin() +
                        '}'
        );
    }

    private static void printMesure(MesureSante m) {
        System.out.println(
                "MesureSante{id=" + m.getId() +
                        ", userId=" + m.getUserId() +
                        ", habitudeId=" + m.getHabitudeId() +
                        ", pas=" + m.getPas() +
                        ", eauLitres=" + m.getEauLitres() +
                        ", tensionSystolique=" + m.getTensionSystolique() +
                        ", tensionDiastolique=" + m.getTensionDiastolique() +
                        ", calories=" + m.getCalories() +
                        ", poidsKg=" + m.getPoidsKg() +
                        ", sommeilHeures=" + m.getSommeilHeures() +
                        ", dateMesure=" + m.getDateMesure() +
                        '}'
        );
    }
}
