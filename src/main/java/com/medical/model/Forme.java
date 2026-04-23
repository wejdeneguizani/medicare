package com.medical.model;

public class Forme {
    private int idForme;
    private String libelle;
    private String voieAdministration;

    public Forme() {}

    public Forme(int idForme, String libelle, String voieAdministration) {
        this.idForme = idForme;
        this.libelle = libelle;
        this.voieAdministration = voieAdministration;
    }

    public int getIdForme() { return idForme; }
    public void setIdForme(int idForme) { this.idForme = idForme; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getVoieAdministration() { return voieAdministration; }
    public void setVoieAdministration(String voieAdministration) { this.voieAdministration = voieAdministration; }

    @Override
    public String toString() {
        return "╔══════════════════════════════════════╗\n" +
                "  ID    : " + idForme + "\n" +
                "  Forme : " + libelle + "\n" +
                "  Voie  : " + voieAdministration + "\n" +
                "╚══════════════════════════════════════╝";
    }
}