import models.Objectif;
import models.PlanCoaching;
import models.ProgressionObjectif;
import services.ServiceObjectif;
import services.ServicePlanCoaching;
import services.ServiceProgression;

import java.util.Date;

public class Main {

    public static void main(String[] args) {

        // =============================================
        // TEST 1 : OBJECTIFS
        // =============================================
        ServiceObjectif so = new ServiceObjectif();

        System.out.println("==============================");
        System.out.println("   TEST AJOUT OBJECTIF");
        System.out.println("==============================");

        Objectif obj = new Objectif(
                1,                        // id_patient
                1,                        // id_medecin
                "Perdre 5 kg en 3 mois",  // titre
                "poids",                  // type
                85.0,                     // valeur initiale
                80.0,                     // valeur cible
                "kg",                     // unite
                new Date(),               // date debut = aujourd'hui
                new Date(System.currentTimeMillis()
                        + 90L * 24 * 60 * 60 * 1000), // date echeance = +90 jours
                1,                        // priorite (1=haute)
                "Objectif principal sante" // description
        );
        so.add(obj);

        System.out.println("\n==============================");
        System.out.println("   LISTE DE TOUS LES OBJECTIFS");
        System.out.println("==============================");
        so.getAll().forEach(System.out::println);

        // =============================================
        // TEST 2 : PROGRESSION
        // =============================================
        ServiceProgression sp = new ServiceProgression();

        System.out.println("==============================");
        System.out.println("   TEST AJOUT PROGRESSION");
        System.out.println("==============================");

        ProgressionObjectif prog = new ProgressionObjectif(
                1,               // id_objectif
                1,               // id_patient
                83.5,            // valeur actuelle
                "Bonne semaine, je continue !"  // note
        );
        sp.add(prog);
        // ✅ Le trigger MySQL calcule le % automatiquement !

        System.out.println("\n==============================");
        System.out.println("   PROGRESSIONS DE L'OBJECTIF 1");
        System.out.println("==============================");
        sp.getByObjectif(1).forEach(System.out::println);

        // =============================================
        // TEST 3 : PLAN COACHING
        // =============================================
        ServicePlanCoaching spc = new ServicePlanCoaching();

        System.out.println("==============================");
        System.out.println("   TEST AJOUT PLAN COACHING");
        System.out.println("==============================");

        PlanCoaching plan = new PlanCoaching(
                1,                           // id_patient
                1,                           // id_medecin
                "Programme perte de poids",  // titre
                "Nutrition + sport doux",    // description
                "mixte",                     // type_coaching
                "hebdomadaire",              // frequence
                new Date(System.currentTimeMillis()
                        + 90L * 24 * 60 * 60 * 1000) // date fin = +90 jours
        );
        spc.add(plan);

        System.out.println("\n==============================");
        System.out.println("   LISTE DE TOUS LES PLANS");
        System.out.println("==============================");
        spc.getAll().forEach(System.out::println);

        // =============================================
        // TEST 4 : MODIFIER UN OBJECTIF
        // =============================================
        System.out.println("==============================");
        System.out.println("   TEST MODIFICATION OBJECTIF");
        System.out.println("==============================");

        Objectif objModifie = new Objectif();
        objModifie.setId_objectif(1);         // id de l'objectif à modifier
        objModifie.setTitre("Perdre 7 kg !");
        objModifie.setType_objectif("poids");
        objModifie.setValeur_cible(78.0);
        objModifie.setUnite("kg");
        objModifie.setStatut("en_cours");
        objModifie.setPriorite(1);
        objModifie.setDescription("Objectif revu à la hausse !");
        objModifie.setDate_echeance(
                new Date(System.currentTimeMillis()
                        + 120L * 24 * 60 * 60 * 1000)
        );
        so.update(objModifie);

        System.out.println("\n==============================");
        System.out.println("   OBJECTIFS APRES MODIFICATION");
        System.out.println("==============================");
        so.getAll().forEach(System.out::println);

        // =============================================
        // TEST 5 : SUPPRIMER
        // =============================================
        System.out.println("==============================");
        System.out.println("   TEST SUPPRESSION PROGRESSION");
        System.out.println("==============================");

        ProgressionObjectif progSuppr = new ProgressionObjectif();
        progSuppr.setId_progression(1); // id de la progression à supprimer
        sp.delete(progSuppr);

        System.out.println("\n==============================");
        System.out.println("   PROGRESSIONS APRES SUPPRESSION");
        System.out.println("==============================");
        sp.getAll().forEach(System.out::println);
    }
    System.out.println("\n==============================");
    System.out.println("   RECHERCHE : mot-clé 'perdre'");
    System.out.println("==============================");
    so.rechercher("perdre").forEach(System.out::println);

    System.out.println("\n==============================");
    System.out.println("   FILTRE : statut 'en_cours'");
    System.out.println("==============================");
    so.filtrerParStatut("en_cours").forEach(System.out::println);
}