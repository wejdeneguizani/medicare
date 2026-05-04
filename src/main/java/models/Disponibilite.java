package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Disponibilite {

    private int       id;
    private int       medecinId;       // FK → utilisateurs.id
    private LocalDate dateDisponible;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String    statut;          // Disponible / Réservé / Annulé

    // ── Constructeurs ─────────────────────────────────────────────
    public Disponibilite() {}

    public Disponibilite(int medecinId, LocalDate dateDisponible,
                         LocalTime heureDebut, LocalTime heureFin,
                         String statut) {
        this.medecinId      = medecinId;
        this.dateDisponible = dateDisponible;
        this.heureDebut     = heureDebut;
        this.heureFin       = heureFin;
        this.statut         = statut;
    }

    // ── Getters / Setters ─────────────────────────────────────────
    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public int getMedecinId()                  { return medecinId; }
    public void setMedecinId(int medecinId)    { this.medecinId = medecinId; }

    public LocalDate getDateDisponible()       { return dateDisponible; }
    public void setDateDisponible(LocalDate d) { this.dateDisponible = d; }

    public LocalTime getHeureDebut()           { return heureDebut; }
    public void setHeureDebut(LocalTime t)     { this.heureDebut = t; }

    public LocalTime getHeureFin()             { return heureFin; }
    public void setHeureFin(LocalTime t)       { this.heureFin = t; }

    public String getStatut()                  { return statut; }
    public void setStatut(String statut)       { this.statut = statut; }

    // ── toString ──────────────────────────────────────────────────
    @Override
    public String toString() {
        return "Disponibilite{" +
                "id=" + id +
                ", medecinId=" + medecinId +
                ", date=" + dateDisponible +
                ", de=" + heureDebut +
                " à=" + heureFin +
                ", statut='" + statut + '\'' +
                "}\n";
    }
}