package com.medical;

import com.medical.model.*;
import com.medical.services.*;
import java.util.Date;

public class Main {

    public static void main(String[] args) {


        System.out.println("   TEST CRUD - APPLICATION MEDICARE");


        // =============================================
        // TEST CATEGORIE
        // =============================================
        System.out.println("===== TEST CATEGORIE =====");
        CategorieService cs = new CategorieService();

        Categorie cat = new Categorie("Antibiotiques Test", "J99", "Test description");
        System.out.println("Ajout : " + cs.ajouter(cat));
        System.out.println("Liste : " + cs.getTous());

        // =============================================
        // TEST FABRICANT
        // =============================================
        System.out.println("\n===== TEST FABRICANT =====");
        FabricantService fs = new FabricantService();

        Fabricant fab = new Fabricant("TestPharma", "Tunisie", "test@pharma.com", "www.testpharma.com");
        System.out.println("Ajout : " + fs.ajouter(fab));
        System.out.println("Liste : " + fs.getTous());

        // =============================================
        // TEST FORME
        // =============================================
        System.out.println("\n===== TEST FORME =====");
        FormeService fos = new FormeService();

        Forme forme = new Forme("Gelule Test", "orale");
        System.out.println("Ajout : " + fos.ajouter(forme));
        System.out.println("Liste : " + fos.getTous());

        // =============================================
        // TEST MEDICAMENT
        // =============================================
        System.out.println("\n===== TEST MEDICAMENT =====");
        MedicamentService ms = new MedicamentService();

        Medicament med = new Medicament(
            "TestMed", "TestDCI", "123456789",
            1, 1, 1,
            "500", "mg", "Médicament test",
            "Aucune", "Aucun", true
        );
        System.out.println("Ajout : " + ms.ajouter(med));
        System.out.println("Liste : " + ms.getTous());

        // =============================================
        // TEST STOCK
        // =============================================
        System.out.println("\n===== TEST STOCK =====");
        StockService ss = new StockService();

        // Date expiration dans 1 an
        Date dateExp = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000);
        Stock stock = new Stock(1, "LOT-TEST-001", 100, 10, 1.5, dateExp, new Date(), "Armoire A", "FournisseurTest");
        System.out.println("Ajout : " + ss.ajouter(stock));
        System.out.println("Liste : " + ss.getTous());
        System.out.println("Stock faible : " + ss.getStockFaible());

        // =============================================
        // TEST RÈGLES MÉTIER
        // =============================================
        System.out.println("\n===== TEST RÈGLES MÉTIER =====");

        // Test nom vide
        Categorie catVide = new Categorie("", "X99", "test");
        System.out.println("Catégorie sans nom (doit échouer) : " + cs.ajouter(catVide));

        // Test ID négatif
        System.out.println("Supprimer ID négatif (doit échouer) : " + cs.supprimer(-1));

        // Test médicament actif
        System.out.println("Supprimer médicament actif (doit échouer) : " + ms.supprimer(1));

        System.out.println("\n✅ Tests terminés !");
    }
}
