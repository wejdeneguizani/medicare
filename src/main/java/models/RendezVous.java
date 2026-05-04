package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class RendezVous {

    private int       id;
    private int       patientId;         // FK → utilisateurs.id
    private int       medecinId;         // FK → utilisateurs.id
    private int       disponibiliteId;   // FK → disponibilite.id (0 = pas de dispo liée)
    private LocalDate dateRdv;
    private LocalTime heureRdv;
    private String    motif;
    private String    notes;
    private String    statut;            // En attente / Confirmé / Annulé / Terminé

    // ── Constructeurs ─────────────────────────────────────────────
    public RendezVous() {}

    public RendezVous(int patientId, int medecinId, int disponibiliteId,
                      LocalDate dateRdv, LocalTime heureRdv,
                      String motif, String notes, String statut) {
        this.patientId      = patientId;
        this.medecinId      = medecinId;
        this.disponibiliteId = disponibiliteId;
        this.dateRdv        = dateRdv;
        this.heureRdv       = heureRdv;
        this.motif          = motif;
        this.notes          = notes;
        this.statut         = statut;
    }

    // ── Getters / Setters ─────────────────────────────────────────
    public int getId()                           { return id; }
    public void setId(int id)                    { this.id = id; }

    public int getPatientId()                    { return patientId; }
    public void setPatientId(int patientId)      { this.patientId = patientId; }

    public int getMedecinId()                    { return medecinId; }
    public void setMedecinId(int medecinId)      { this.medecinId = medecinId; }

    public int getDisponibiliteId()              { return disponibiliteId; }
    public void setDisponibiliteId(int id)       { this.disponibiliteId = id; }

    public LocalDate getDateRdv()                { return dateRdv; }
    public void setDateRdv(LocalDate d)          { this.dateRdv = d; }

    public LocalTime getHeureRdv()               { return heureRdv; }
    public void setHeureRdv(LocalTime t)         { this.heureRdv = t; }

    public String getMotif()                     { return motif; }
    public void setMotif(String motif)           { this.motif = motif; }

    public String getNotes()                     { return notes; }
    public void setNotes(String notes)           { this.notes = notes; }

    public String getStatut()                    { return statut; }
    public void setStatut(String statut)         { this.statut = statut; }

    // ── toString ──────────────────────────────────────────────────
    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", medecinId=" + medecinId +
                ", date=" + dateRdv +
                ", heure=" + heureRdv +
                ", motif='" + motif + '\'' +
                ", statut='" + statut + '\'' +
                "}\n";
    }
}