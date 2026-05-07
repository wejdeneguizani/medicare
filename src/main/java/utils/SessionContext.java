package utils;

import models.Utilisateur;

public final class SessionContext {

    private static Utilisateur utilisateurConnecte;

    private SessionContext() {
    }

    public static void setUtilisateurConnecte(Utilisateur utilisateur) {
        utilisateurConnecte = utilisateur;
    }

    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public static void clear() {
        utilisateurConnecte = null;
    }
}
