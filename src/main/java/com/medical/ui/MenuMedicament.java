package com.medical.ui;

import com.medical.controller.MedicamentController;
import com.medical.model.Medicament;
import java.util.List;
import java.util.Scanner;

public class MenuMedicament {

    private static MedicamentController controller = new MedicamentController();
    private static Scanner scanner = new Scanner(System.in);

    public static void afficherMenu() {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║     GESTION DES MÉDICAMENTS      ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Ajouter un médicament        ║");
            System.out.println("║  2. Afficher tous les médicaments║");
            System.out.println("║  3. Rechercher par ID            ║");
            System.out.println("║  4. Modifier un médicament       ║");
            System.out.println("║  5. Supprimer un médicament      ║");
            System.out.println("║  0. Quitter                      ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("👉 Votre choix : ");

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1 -> ajouter();
                case 2 -> afficherTous();
                case 3 -> rechercherParId();
                case 4 -> modifier();
                case 5 -> supprimer();
                case 0 -> System.out.println("👋 Au revoir !");
                default -> System.out.println("⚠️ Choix invalide !");
            }
        }
    }

    private static void ajouter() {
        System.out.println("\n➕ AJOUTER UN MÉDICAMENT");
        Medicament m = new Medicament();

        System.out.print("Nom commercial : ");
        m.setNomCommercial(scanner.nextLine());

        System.out.print("Nom DCI : ");
        m.setNomDci(scanner.nextLine());

        System.out.print("Code barre : ");
        m.setCodeBarre(scanner.nextLine());

        System.out.print("ID Catégorie : ");
        m.setIdCategorie(scanner.nextInt());

        System.out.print("ID Forme : ");
        m.setIdForme(scanner.nextInt());

        System.out.print("ID Fabricant : ");
        m.setIdFabricant(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Dosage (ex: 500) : ");
        m.setDosage(scanner.nextLine());

        System.out.print("Unité (mg/ml...) : ");
        m.setUniteDosage(scanner.nextLine());

        m.setEstActif(true);

        if (controller.ajouterMedicament(m))
            System.out.println("✅ Médicament ajouté avec succès !");
    }

    private static void afficherTous() {
        System.out.println("\n📋 LISTE DES MÉDICAMENTS");
        List<Medicament> liste = controller.afficherTous();
        if (liste.isEmpty())
            System.out.println("⚠️ Aucun médicament trouvé.");
        else
            liste.forEach(System.out::println);
    }

    private static void rechercherParId() {
        System.out.print("\n🔍 Entrez l'ID du médicament : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Medicament m = controller.chercherParId(id);
        if (m != null) System.out.println(m);
        else System.out.println("⚠️ Médicament introuvable.");
    }

    private static void modifier() {
        System.out.print("\n✏️ ID du médicament à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Medicament m = controller.chercherParId(id);
        if (m == null) {
            System.out.println("⚠️ Médicament introuvable.");
            return;
        }

        System.out.print("Nouveau nom commercial (" + m.getNomCommercial() + ") : ");
        String val = scanner.nextLine();
        if (!val.isEmpty()) m.setNomCommercial(val);

        System.out.print("Nouveau dosage (" + m.getDosage() + ") : ");
        val = scanner.nextLine();
        if (!val.isEmpty()) m.setDosage(val);

        if (controller.modifierMedicament(m))
            System.out.println("✅ Médicament modifié avec succès !");
    }

    private static void supprimer() {
        System.out.print("\n🗑️ ID du médicament à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        if (controller.supprimerMedicament(id))
            System.out.println("✅ Médicament supprimé !");
    }
}