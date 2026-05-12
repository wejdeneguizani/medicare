package models;

/**
 * Minimal profile returned by Google after OAuth (userinfo).
 */
public record GoogleUserProfile(String email, String name) {

    public GoogleUserProfile {
        if (email != null) {
            email = email.trim().toLowerCase();
        }
    }
}
