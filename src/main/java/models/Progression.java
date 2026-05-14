package models;

import java.time.LocalDate;

public class Progression {
    private int       id_progression;
    private int       id_objectif;
    private LocalDate date_mesure;
    private float     valeur_actuelle;
    private float     valeur_cible;
    private String    humeur;
    private String    notes;

    public Progression() {}

    public Progression(int id_objectif, LocalDate date_mesure,
                       float valeur_actuelle, float valeur_cible,
                       String humeur, String notes) {
        this.id_objectif     = id_objectif;
        this.date_mesure     = date_mesure;
        this.valeur_actuelle = valeur_actuelle;
        this.valeur_cible    = valeur_cible;
        this.humeur          = humeur;
        this.notes           = notes;
    }

    // Getters & Setters
    public int getId_progression()              { return id_progression; }
    public void setId_progression(int v)        { this.id_progression = v; }
    public int getId_objectif()                 { return id_objectif; }
    public void setId_objectif(int v)           { this.id_objectif = v; }
    public LocalDate getDate_mesure()           { return date_mesure; }
    public void setDate_mesure(LocalDate v)     { this.date_mesure = v; }
    public float getValeur_actuelle()           { return valeur_actuelle; }
    public void setValeur_actuelle(float v)     { this.valeur_actuelle = v; }
    public float getValeur_cible()              { return valeur_cible; }
    public void setValeur_cible(float v)        { this.valeur_cible = v; }
    public String getHumeur()                   { return humeur; }
    public void setHumeur(String v)             { this.humeur = v; }
    public String getNotes()                    { return notes; }
    public void setNotes(String v)              { this.notes = v; }

    @Override
    public String toString() {
        return "Progression{" +
                "id=" + id_progression +
                ", objectif=" + id_objectif +
                ", date=" + date_mesure +
                ", valeur=" + valeur_actuelle + "/" + valeur_cible +
                ", humeur='" + humeur + '\'' +
                "}\n";
    }
}