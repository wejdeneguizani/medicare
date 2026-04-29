package tn.esprit.medicare.tests;

import tn.esprit.medicare.entities.Habitude;
import tn.esprit.medicare.entities.MesureSante;
import tn.esprit.medicare.entities.User;
import tn.esprit.medicare.services.HabitudeService;
import tn.esprit.medicare.services.MesureSanteService;
import tn.esprit.medicare.utils.HealthUtils;

import java.sql.SQLException;
import java.util.List;

public class MainTestCRUD {
    public static void main(String[] args) {
        HabitudeService habitudeService = new HabitudeService();
        MesureSanteService mesureService = new MesureSanteService();

        try {
            System.out.println("--- Test Recherche Habitudes ---");
            List<Habitude> searchResults = habitudeService.search("Eau");
            System.out.println("Habitudes contenant 'Eau' : " + searchResults.size());
            searchResults.forEach(h -> System.out.println(" - " + h.getTitre()));

            System.out.println("\n--- Test Tri Habitudes par Titre (DESC) ---");
            List<Habitude> sortedHabitudes = habitudeService.sort("titre", false);
            sortedHabitudes.forEach(h -> System.out.println(" - " + h.getTitre()));

            System.out.println("\n--- Test Recherche Mesures par Titre d'Habitude ---");
            // On cherche les mesures liées à une habitude qui a "Eau" dans son titre
            List<MesureSante> searchMesures = mesureService.search("Eau");
            System.out.println("Mesures trouvées : " + searchMesures.size());
            searchMesures.forEach(m -> System.out.println(" - ID: " + m.getId() + " | Date: " + m.getDateMesure()));

            System.out.println("\n--- Test Tri Mesures par Calories (ASC) ---");
            List<MesureSante> sortedMesures = mesureService.sort("calories", true);
            System.out.println("\n--- Test Calculs Santé (BMI / IMC) ---");
            double poids = 75.5;
            double taille = 178.0;
            double imc = HealthUtils.calculateBMI(poids, taille);
            System.out.printf("Poids: %.1f kg | Taille: %.1f cm\n", poids, taille);
            System.out.printf("IMC calculé : %.2f (%s)\n", imc, HealthUtils.getBMICategory(imc));

            double eauRec = HealthUtils.calculateRecommendedWater(poids);
            System.out.printf("Consommation d'eau recommandée : %.2f Litres/jour\n", eauRec);

            double bmr = HealthUtils.calculateBMR(poids, taille, 25, User.Sexe.M);
            System.out.printf("Métabolisme de base (BMR) : %.0f kcal/jour\n", bmr);

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors du test : " + e.getMessage());
        }
    }
}
