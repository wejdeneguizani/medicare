package models;

import java.time.LocalDateTime;

public class RendezVous {

    private int    id_rdv;
    private int    id_utilisateur;
    private int    id_medecin;
    private String date_heure;     // format "YYYY-MM-DD HH:MM:SS"
    private String motif;
    private String statut;         // planifie / confirme / annule / termine
    private String type_rdv;       // presentiel / teleconsultation
    private String notes;

    // ── Constructeur vide ──
    public RendezVous() {}

    // ── Constructeur sans id (pour INSERT) ──
    public RendezVous(int id_utilisateur, int id_medecin,
                      String date_heure, String motif,
                      String statut, String type_rdv, String notes) {
        this.id_utilisateur = id_utilisateur;
        this.id_medecin     = id_medecin;
        this.date_heure     = date_heure;
        this.motif          = motif;
        this.statut         = statut;
        this.type_rdv       = type_rdv;
        this.notes          = notes;
    }

    // ── Constructeur complet (pour SELECT) ──
    public RendezVous(int id_rdv, int id_utilisateur, int id_medecin,
                      String date_heure, String motif,
                      String statut, String type_rdv, String notes) {
        this.id_rdv         = id_rdv;
        this.id_utilisateur = id_utilisateur;
        this.id_medecin     = id_medecin;
        this.date_heure     = date_heure;
        this.motif          = motif;
        this.statut         = statut;
        this.type_rdv       = type_rdv;
        this.notes          = notes;
    }

    // ── Getters & Setters ──
    public int    getId_rdv()          { return id_rdv; }
    public void   setId_rdv(int v)     { this.id_rdv = v; }

    public int    getId_utilisateur()       { return id_utilisateur; }
    public void   setId_utilisateur(int v)  { this.id_utilisateur = v; }

    public int    getId_medecin()           { return id_medecin; }
    public void   setId_medecin(int v)      { this.id_medecin = v; }

    public String getDate_heure()           { return date_heure; }
    public void   setDate_heure(String v)   { this.date_heure = v; }

    public String getMotif()                { return motif; }
    public void   setMotif(String v)        { this.motif = v; }

    public String getStatut()               { return statut; }
    public void   setStatut(String v)       { this.statut = v; }

    public String getType_rdv()             { return type_rdv; }
    public void   setType_rdv(String v)     { this.type_rdv = v; }

    public String getNotes()                { return notes; }
    public void   setNotes(String v)        { this.notes = v; }

    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id_rdv +
                ", patient=" + id_utilisateur +
                ", medecin=" + id_medecin +
                ", date='" + date_heure + '\'' +
                ", motif='" + motif + '\'' +
                ", statut='" + statut + '\'' +
                ", type='" + type_rdv + '\'' +
                "}\n";
    }
}