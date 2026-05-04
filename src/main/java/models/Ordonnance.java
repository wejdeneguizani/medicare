package models;

import java.time.LocalDate;

public class Ordonnance {

    private int       id;
    private int       rendezVousId;     // FK → rendez_vous.id
    private int       medecinId;        // FK → utilisateurs.id
    private int       patientId;        // FK → utilisateurs.id
    private String    medicament;
    private String    posologie;
    private String    dureeTraitement;
    private String    instructions;
    private LocalDate dateEmission;
    private LocalDate dateExpiration;

    // ── Constructeurs ─────────────────────────────────────────────
    public Ordonnance() {}

    public Ordonnance(int rendezVousId, int medecinId, int patientId,
                      String medicament, String posologie,
                      String dureeTraitement, String instructions,
                      LocalDate dateEmission, LocalDate dateExpiration) {
        this.rendezVousId    = rendezVousId;
        this.medecinId       = medecinId;
        this.patientId       = patientId;
        this.medicament      = medicament;
        this.posologie       = posologie;
        this.dureeTraitement = dureeTraitement;
        this.instructions    = instructions;
        this.dateEmission    = dateEmission;
        this.dateExpiration  = dateExpiration;
    }

    // ── Getters / Setters ─────────────────────────────────────────
    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public int getRendezVousId()                    { return rendezVousId; }
    public void setRendezVousId(int rendezVousId)   { this.rendezVousId = rendezVousId; }

    public int getMedecinId()                       { return medecinId; }
    public void setMedecinId(int medecinId)         { this.medecinId = medecinId; }

    public int getPatientId()                       { return patientId; }
    public void setPatientId(int patientId)         { this.patientId = patientId; }

    public String getMedicament()                   { return medicament; }
    public void setMedicament(String medicament)    { this.medicament = medicament; }

    public String getPosologie()                    { return posologie; }
    public void setPosologie(String posologie)      { this.posologie = posologie; }

    public String getDureeTraitement()              { return dureeTraitement; }
    public void setDureeTraitement(String d)        { this.dureeTraitement = d; }

    public String getInstructions()                 { return instructions; }
    public void setInstructions(String i)           { this.instructions = i; }

    public LocalDate getDateEmission()              { return dateEmission; }
    public void setDateEmission(LocalDate d)        { this.dateEmission = d; }

    public LocalDate getDateExpiration()            { return dateExpiration; }
    public void setDateExpiration(LocalDate d)      { this.dateExpiration = d; }

    // ── toString ──────────────────────────────────────────────────
    @Override
    public String toString() {
        return "Ordonnance{" +
                "id=" + id +
                ", rendezVousId=" + rendezVousId +
                ", patientId=" + patientId +
                ", medecinId=" + medecinId +
                ", medicament='" + medicament + '\'' +
                ", posologie='" + posologie + '\'' +
                ", dateEmission=" + dateEmission +
                "}\n";
    }
}