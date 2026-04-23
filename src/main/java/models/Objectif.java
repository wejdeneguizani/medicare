package models;

import java.util.Date;

public class Objectif {

    private int    id_objectif;
    private int    id_patient;
    private int    id_medecin;
    private String titre;
    private String type_objectif;
    private double valeur_initiale;
    private double valeur_cible;
    private String unite;
    private Date   date_debut;
    private Date   date_echeance;
    private String statut;
    private int    priorite;
    private String description;

    // Constructeur vide
    public Objectif() {}

    // Constructeur pour ajouter un objectif
    public Objectif(int id_patient, int id_medecin, String titre,
                    String type_objectif, double valeur_initiale,
                    double valeur_cible, String unite,
                    Date date_debut, Date date_echeance,
                    int priorite, String description) {
        this.id_patient      = id_patient;
        this.id_medecin      = id_medecin;
        this.titre           = titre;
        this.type_objectif   = type_objectif;
        this.valeur_initiale = valeur_initiale;
        this.valeur_cible    = valeur_cible;
        this.unite           = unite;
        this.date_debut      = date_debut;
        this.date_echeance   = date_echeance;
        this.statut          = "en_cours";
        this.priorite        = priorite;
        this.description     = description;
    }

    // Getters et Setters
    public int getId_objectif()              { return id_objectif; }
    public void setId_objectif(int v)        { this.id_objectif = v; }

    public int getId_patient()               { return id_patient; }
    public void setId_patient(int v)         { this.id_patient = v; }

    public int getId_medecin()               { return id_medecin; }
    public void setId_medecin(int v)         { this.id_medecin = v; }

    public String getTitre()                 { return titre; }
    public void setTitre(String v)           { this.titre = v; }

    public String getType_objectif()         { return type_objectif; }
    public void setType_objectif(String v)   { this.type_objectif = v; }

    public double getValeur_initiale()       { return valeur_initiale; }
    public void setValeur_initiale(double v) { this.valeur_initiale = v; }

    public double getValeur_cible()          { return valeur_cible; }
    public void setValeur_cible(double v)    { this.valeur_cible = v; }

    public String getUnite()                 { return unite; }
    public void setUnite(String v)           { this.unite = v; }

    public Date getDate_debut()              { return date_debut; }
    public void setDate_debut(Date v)        { this.date_debut = v; }

    public Date getDate_echeance()           { return date_echeance; }
    public void setDate_echeance(Date v)     { this.date_echeance = v; }

    public String getStatut()                { return statut; }
    public void setStatut(String v)          { this.statut = v; }

    public int getPriorite()                 { return priorite; }
    public void setPriorite(int v)           { this.priorite = v; }

    public String getDescription()           { return description; }
    public void setDescription(String v)     { this.description = v; }

    @Override
    public String toString() {
        return "Objectif{" +
                "id=" + id_objectif +
                ", titre='" + titre + "'" +
                ", type='" + type_objectif + "'" +
                ", cible=" + valeur_cible + " " + unite +
                ", statut='" + statut + "'" +
                "}\n";
    }
}