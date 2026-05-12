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
        if (plainText == null || hash == null || hash.isBlank()) {
            return false;
        }

        try {
            return BCrypt.checkpw(plainText, hash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /** Génère un matricule unique selon le rôle. */
    public static String genererMatricule(String role) {
        int annee = java.time.Year.now().getValue();
        int rand  = (int)(Math.random() * 9000) + 1000;
        return switch (role) {
            case "Administrateur" -> "ADM-" + annee + "-" + rand;
            case "Medecin"        -> "MAT-" + annee + "-" + rand;
            case "Patient"        -> "PAT-" + annee + "-" + rand;
            default               -> "USR-" + annee + "-" + rand;
        };
    }
}
