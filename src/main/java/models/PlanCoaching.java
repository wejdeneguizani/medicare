package models;

import java.util.Date;

public class PlanCoaching {

    private int     id_plan;
    private int     id_patient;
    private int     id_medecin;
    private String  titre_plan;
    private String  description;
    private String  type_coaching;
    private String  frequence;
    private Date    date_creation;
    private Date    date_fin;
    private boolean actif;

    // Constructeur vide
    public PlanCoaching() {}

    // Constructeur pour ajouter un plan
    public PlanCoaching(int id_patient, int id_medecin,
                        String titre_plan, String description,
                        String type_coaching, String frequence,
                        Date date_fin) {
        this.id_patient    = id_patient;
        this.id_medecin    = id_medecin;
        this.titre_plan    = titre_plan;
        this.description   = description;
        this.type_coaching = type_coaching;
        this.frequence     = frequence;
        this.date_fin      = date_fin;
        this.actif         = true;
    }

    // Getters et Setters
    public int getId_plan()                  { return id_plan; }
    public void setId_plan(int v)            { this.id_plan = v; }

    public int getId_patient()               { return id_patient; }
    public void setId_patient(int v)         { this.id_patient = v; }

    public int getId_medecin()               { return id_medecin; }
    public void setId_medecin(int v)         { this.id_medecin = v; }

    public String getTitre_plan()            { return titre_plan; }
    public void setTitre_plan(String v)      { this.titre_plan = v; }

    public String getDescription()           { return description; }
    public void setDescription(String v)     { this.description = v; }

    public String getType_coaching()         { return type_coaching; }
    public void setType_coaching(String v)   { this.type_coaching = v; }

    public String getFrequence()             { return frequence; }
    public void setFrequence(String v)       { this.frequence = v; }

    public Date getDate_creation()           { return date_creation; }
    public void setDate_creation(Date v)     { this.date_creation = v; }

    public Date getDate_fin()                { return date_fin; }
    public void setDate_fin(Date v)          { this.date_fin = v; }

    public boolean isActif()                 { return actif; }
    public void setActif(boolean v)          { this.actif = v; }

    @Override
    public String toString() {
        return "PlanCoaching{" +
                "id=" + id_plan +
                ", titre='" + titre_plan + "'" +
                ", type='" + type_coaching + "'" +
                ", frequence='" + frequence + "'" +
                ", actif=" + actif +
                "}\n";
    }
}