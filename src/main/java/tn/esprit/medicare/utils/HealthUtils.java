package tn.esprit.medicare.utils;

import tn.esprit.medicare.entities.User;

public class HealthUtils {

    /**
     * Calcule l'Indice de Masse Corporelle (IMC / BMI)
     * Formule : poids (kg) / [taille (m)]²
     */
    public static double calculateBMI(double weightKg, double heightCm) {
        if (heightCm <= 0) return 0;
        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }

    /**
     * Retourne la catégorie IMC
     */
    public static String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Insuffisance pondérale (Maigreur)";
        if (bmi < 25) return "Poids normal";
        if (bmi < 30) return "Surpoids";
        if (bmi < 35) return "Obésité modérée";
        if (bmi < 40) return "Obésité sévère";
        return "Obésité morbide";
    }

    /**
     * Calcule les besoins quotidiens en eau (Estimation simple)
     * Environ 30-35ml par kg de poids corporel
     */
    public static double calculateRecommendedWater(double weightKg) {
        return weightKg * 0.033; // Litres
    }

    /**
     * Calcule le Métabolisme de Base (BMR) - Formule de Mifflin-St Jeor
     */
    public static double calculateBMR(double weightKg, double heightCm, int age, User.Sexe sexe) {
        double bmr;
        if (sexe == User.Sexe.M) {
            bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5;
        } else {
            bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * age) - 161;
        }
        return bmr;
    }
}
