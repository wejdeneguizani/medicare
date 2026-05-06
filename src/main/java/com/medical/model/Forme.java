package com.medical.model;

public class Forme {

    private int    idForme;
    private String libelle;
    private String voieAdministration;

    public Forme() {}

    public Forme(String libelle, String voieAdministration) {
        this.libelle            = libelle;
        this.voieAdministration = voieAdministration;
    }

    public int    getIdForme()                        { return idForme; }
    public void   setIdForme(int id)                  { this.idForme = id; }
    public String getLibelle()                        { return libelle; }
    public void   setLibelle(String libelle)          { this.libelle = libelle; }
    public String getVoieAdministration()             { return voieAdministration; }
    public void   setVoieAdministration(String voie)  { this.voieAdministration = voie; }

    @Override
    public String toString() {
        return "Forme{id=" + idForme + ", libelle='" + libelle + "', voie='" + voieAdministration + "'}";
    }
}