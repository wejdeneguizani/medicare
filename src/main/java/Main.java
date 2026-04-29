import models.*;
import services.*;

public class Main {
    public static void main(String[] args) {

        // --- Test Patient ---
        ServicePatient sp = new ServicePatient();
        Patient p = new Patient("Ben Ali", "Sami", "sami@gmail.com", "55123456",
                "2000-05-15", "A+", "Tunis");
        sp.add(p);
        System.out.println(sp.getAll());

        // --- Test Médecin ---
        ServiceMedecin sm = new ServiceMedecin();
        Medecin m = new Medecin("Trabelsi", "Imen", "imen@gmail.com",
                "22987654", "Cardiologie", "ORD-001");
        sm.add(m);
        System.out.println(sm.getAll());

        // --- Test Disponibilité ---
        ServiceDisponibilite sd = new ServiceDisponibilite();
        Disponibilite d = new Disponibilite(1, "Lundi", "08:00", "17:00", true);
        sd.add(d);
        System.out.println(sd.getAll());

        // --- Test Rendez-vous ---
        ServiceRendezVous sr = new ServiceRendezVous();
        RendezVous rdv = new RendezVous(1, 1, "2025-06-10 10:30:00",
                30, "en_attente", "Consultation", "RAS");
        sr.add(rdv);
        System.out.println(sr.getAll());

        // ---- Tests Recherche / Filtre / Tri ----

// Recherche patient par nom
        System.out.println("=== Recherche patient 'Ben' ===");
        System.out.println(sp.rechercherParNom("Ben"));

// Tri patients par nom
        System.out.println("=== Patients triés par nom ===");
        System.out.println(sp.trierParNom());

// Recherche médecin par spécialité
        System.out.println("=== Médecins Cardiologie ===");
        System.out.println(sm.rechercherParSpecialite("Cardio"));

// Tri médecins par nom
        System.out.println("=== Médecins triés par nom ===");
        System.out.println(sm.trierParNom());

// Filtre RDV par statut
        System.out.println("=== RDV en attente ===");
        System.out.println(sr.filtrerParStatut("en_attente"));

// Tri RDV par date
        System.out.println("=== RDV triés par date ===");
        System.out.println(sr.trierParDate());

// Filtre RDV par date
        System.out.println("=== RDV du 2025-06-10 ===");
        System.out.println(sr.filtrerParDate("2025-06-10"));


    }

}
