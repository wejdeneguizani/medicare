package models;

import java.time.LocalDate;

public class Objectif {
    private int    id_objectif;
    private int    id_patient;
    private int    id_medecin;
    private String titre;
    private String categorie;
    private float  valeur_cible;
    private String unite_mesure;
    private LocalDate date_debut;
    private LocalDate date_echeance;
    private String statut;
    private int    priorite;

    public Objectif() {}

    public Objectif(int id_patient, int id_medecin, String titre, String categorie,
                    float valeur_cible, String unite_mesure,
                    LocalDate date_debut, LocalDate date_echeance,
                    String statut, int priorite) {
        this.id_patient   = id_patient;
        this.id_medecin   = id_medecin;
        this.titre        = titre;
        this.categorie    = categorie;
        this.valeur_cible = valeur_cible;
        this.unite_mesure = unite_mesure;
        this.date_debut   = date_debut;
        this.date_echeance = date_echeance;
        this.statut       = statut;
        this.priorite     = priorite;
    }

    // Getters & Setters
    public int getId_objectif()              { return id_objectif; }
    public void setId_objectif(int v)        { this.id_objectif = v; }
    public int getId_patient()               { return id_patient; }
    public void setId_patient(int v)         { this.id_patient = v; }
    public int getId_medecin()               { return id_medecin; }
    public void setId_medecin(int v)         { this.id_medecin = v; }
    public String getTitre()                 { return titre; }
    public void setTitre(String v)           { this.titre = v; }
    public String getCategorie()             { return categorie; }
    public void setCategorie(String v)       { this.categorie = v; }
    public float getValeur_cible()           { return valeur_cible; }
    public void setValeur_cible(float v)     { this.valeur_cible = v; }
    public String getUnite_mesure()          { return unite_mesure; }
    public void setUnite_mesure(String v)    { this.unite_mesure = v; }
    public LocalDate getDate_debut()         { return date_debut; }
    public void setDate_debut(LocalDate v)   { this.date_debut = v; }
    public LocalDate getDate_echeance()      { return date_echeance; }
    public void setDate_echeance(LocalDate v){ this.date_echeance = v; }
    public String getStatut()                { return statut; }
    public void setStatut(String v)          { this.statut = v; }
    public int getPriorite()                 { return priorite; }
    public void setPriorite(int v)           { this.priorite = v; }

    @Override
    public String toString() {
        return "Objectif{" +
                "id=" + id_objectif +
                ", patient=" + id_patient +
                ", titre='" + titre + '\'' +
                ", categorie='" + categorie + '\'' +
                ", statut='" + statut + '\'' +
                ", priorite=" + priorite +
                "}\n";
    }
}