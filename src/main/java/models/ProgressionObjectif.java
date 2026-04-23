package models;

import java.util.Date;

public class ProgressionObjectif {

    private int    id_progression;
    private int    id_objectif;
    private int    id_patient;
    private double valeur_actuelle;
    private double pourcentage;
    private Date   date_mesure;
    private String note_patient;

    // Constructeur vide
    public ProgressionObjectif() {}

    // Constructeur pour ajouter une progression
    public ProgressionObjectif(int id_objectif, int id_patient,
                               double valeur_actuelle, String note_patient) {
        this.id_objectif     = id_objectif;
        this.id_patient      = id_patient;
        this.valeur_actuelle = valeur_actuelle;
        this.note_patient    = note_patient;
    }

    // Getters et Setters
    public int getId_progression()              { return id_progression; }
    public void setId_progression(int v)        { this.id_progression = v; }

    public int getId_objectif()                 { return id_objectif; }
    public void setId_objectif(int v)           { this.id_objectif = v; }

    public int getId_patient()                  { return id_patient; }
    public void setId_patient(int v)            { this.id_patient = v; }

    public double getValeur_actuelle()          { return valeur_actuelle; }
    public void setValeur_actuelle(double v)    { this.valeur_actuelle = v; }

    public double getPourcentage()              { return pourcentage; }
    public void setPourcentage(double v)        { this.pourcentage = v; }

    public Date getDate_mesure()                { return date_mesure; }
    public void setDate_mesure(Date v)          { this.date_mesure = v; }

    public String getNote_patient()             { return note_patient; }
    public void setNote_patient(String v)       { this.note_patient = v; }

    @Override
    public String toString() {
        return "Progression{" +
                "objectif=" + id_objectif +
                ", valeur=" + valeur_actuelle +
                ", pourcentage=" + pourcentage + "%" +
                ", date=" + date_mesure +
                ", note='" + note_patient + "'" +
                "}\n";
    }
}