package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    private PasswordUtils() {}

    /** Hashe un mot de passe en clair avec BCrypt. */
    public static String hash(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt(12));
    }

    /** Vérifie un mot de passe en clair contre un hash stocké. */
    public static boolean verifier(String plainText, String hash) {
        return BCrypt.checkpw(plainText, hash);
    }

    /** Génère un matricule unique selon le rôle. */
    public static String genererMatricule(String roleNom) {
        int annee = java.time.Year.now().getValue();
        int rand  = (int)(Math.random() * 9000) + 1000;
        return switch (roleNom) {
            case "Médecin"   -> "MAT-" + annee + "-" + rand;
            case "Infirmier" -> "INF-" + annee + "-" + rand;
            case "Patient"   -> "PAT-" + annee + "-" + rand;
            default          -> "ADM-" + annee + "-" + rand;
        };
    }
}