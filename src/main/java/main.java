import models.RendezVous;
import services.ServiceRendezVous;

public class main {
    public static void main(String[] args) {

        ServiceRendezVous srv = new ServiceRendezVous();

        // ── TEST 1 : ADD ──────────────────────────────────
        RendezVous rdv1 = new RendezVous(
                1,
                1,
                "2026-05-10 09:00:00",
                "Consultation cardiaque",
                "planifie",
                "presentiel",
                "Premier rendez-vous"
        );
        srv.add(rdv1);

        // ── TEST 2 : GET ALL ──────────────────────────────
        System.out.println("── Liste des rendez-vous ──");
        System.out.println(srv.getAll());

        // ── TEST 3 : GET BY ID ────────────────────────────
        RendezVous trouve = srv.getById(1);
        if (trouve != null)
            System.out.println("Trouvé : " + trouve);

        // ── TEST 4 : UPDATE ───────────────────────────────
        if (trouve != null) {
            trouve.setStatut("confirme");
            trouve.setNotes("Confirmé par le médecin");
            srv.update(trouve);
        }

        // ── TEST 5 : DELETE ───────────────────────────────
        // srv.delete(trouve);

        // ── Affichage final ───────────────────────────────
        System.out.println("── Après mise à jour ──");
        System.out.println(srv.getAll());
    }
}