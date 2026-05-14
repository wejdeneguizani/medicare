package models;

import java.time.LocalDate;

public class PlanCoaching {
    private int       id_plan_coaching;
    private int       id_medecin;
    private int       id_patient;
    private String    titre;
    private LocalDate date_debut;
    private LocalDate date_fin;
    private String    statut;
    private String    intensite;
    private String    objectif_global;

    public PlanCoaching() {}

    public PlanCoaching(int id_medecin, int id_patient, String titre,
                        LocalDate date_debut, LocalDate date_fin,
                        String statut, String intensite, String objectif_global) {
        this.id_medecin      = id_medecin;
        this.id_patient      = id_patient;
        this.titre           = titre;
        this.date_debut      = date_debut;
        this.date_fin        = date_fin;
        this.statut          = statut;
        this.intensite       = intensite;
        this.objectif_global = objectif_global;
    }

    // Getters & Setters
    public int getId_plan_coaching()               { return id_plan_coaching; }
    public void setId_plan_coaching(int v)         { this.id_plan_coaching = v; }
    public int getId_medecin()                     { return id_medecin; }
    public void setId_medecin(int v)               { this.id_medecin = v; }
    public int getId_patient()                     { return id_patient; }
    public void setId_patient(int v)               { this.id_patient = v; }
    public String getTitre()                       { return titre; }
    public void setTitre(String v)                 { this.titre = v; }
    public LocalDate getDate_debut()               { return date_debut; }
    public void setDate_debut(LocalDate v)         { this.date_debut = v; }
    public LocalDate getDate_fin()                 { return date_fin; }
    public void setDate_fin(LocalDate v)           { this.date_fin = v; }
    public String getStatut()                      { return statut; }
    public void setStatut(String v)                { this.statut = v; }
    public String getIntensite()                   { return intensite; }
    public void setIntensite(String v)             { this.intensite = v; }
    public String getObjectif_global()             { return objectif_global; }
    public void setObjectif_global(String v)       { this.objectif_global = v; }

    @Override
    public String toString() {
        return "PlanCoaching{" +
                "id=" + id_plan_coaching +
                ", patient=" + id_patient +
                ", titre='" + titre + '\'' +
                ", statut='" + statut + '\'' +
                ", intensite='" + intensite + '\'' +
                "}\n";
    }
}