import models.*;
import services.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {

        // --- Test Disponibilité ---
        ServiceDisponibilite sd = new ServiceDisponibilite();
        Disponibilite d = new Disponibilite(1, LocalDate.of(2026, 5, 10),
                LocalTime.of(9, 0), LocalTime.of(9, 30), "Disponible");
        sd.add(d);
        System.out.println("=== Disponibilités ===");
        System.out.println(sd.getAll());

        // --- Test RendezVous ---
        ServiceRendezVous sr = new ServiceRendezVous();
        RendezVous rdv = new RendezVous(2, 1, 1,
                LocalDate.of(2026, 5, 10), LocalTime.of(9, 0),
                "Consultation", "RAS", "En attente");
        sr.add(rdv);
        System.out.println("=== Rendez-vous ===");
        System.out.println(sr.getAll());
        System.out.println("=== RDV En attente ===");

        System.out.println("=== RDV triés par date ===");


        // --- Test Ordonnance ---
        ServiceOrdonnance so = new ServiceOrdonnance();
        Ordonnance o = new Ordonnance(1, 1, 2,
                "Paracétamol 500mg", "1 comprimé toutes les 8h",
                "5 jours", "Prendre après repas",
                LocalDate.now(), LocalDate.now().plusDays(5));
        so.add(o);
        System.out.println("=== Ordonnances ===");
        System.out.println(so.getAll());
    }
}