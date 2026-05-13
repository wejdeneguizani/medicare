package com.medical.model;

public class RappelMedicament {

    private int    id;
    private String medicament;
    private String heure;
    private String frequence;
    private String note;
    private String statut;

    public RappelMedicament() {
        this.statut = "En attente";
    }

    public RappelMedicament(String medicament, String heure, String frequence, String note) {
        this.medicament = medicament;
        this.heure      = heure;
        this.frequence  = frequence;
        this.note       = note;
        this.statut     = "En attente";
    }

    public int    getId()                    { return id; }
    public void   setId(int id)             { this.id = id; }
    public String getMedicament()           { return medicament; }
    public void   setMedicament(String m)   { this.medicament = m; }
    public String getHeure()                { return heure; }
    public void   setHeure(String h)        { this.heure = h; }
    public String getFrequence()            { return frequence; }
    public void   setFrequence(String f)    { this.frequence = f; }
    public String getNote()                 { return note; }
    public void   setNote(String n)         { this.note = n; }
    public String getStatut()               { return statut; }
    public void   setStatut(String s)       { this.statut = s; }
}