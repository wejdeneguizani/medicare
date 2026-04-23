package com.medical.model;

public class Categorie {
    private int idCategorie;
    private String libelle;
    private String codeAtc;
    private String description;

    public Categorie() {}

    public Categorie(int idCategorie, String libelle, String codeAtc, String description) {
        this.idCategorie = idCategorie;
        this.libelle = libelle;
        this.codeAtc = codeAtc;
        this.description = description;
    }

    public int getIdCategorie() { return idCategorie; }
    public void setIdCategorie(int idCategorie) { this.idCategorie = idCategorie; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getCodeAtc() { return codeAtc; }
    public void setCodeAtc(String codeAtc) { this.codeAtc = codeAtc; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "╔══════════════════════════════════════╗\n" +
                "  ID          : " + idCategorie + "\n" +
                "  Libelle     : " + libelle + "\n" +
                "  Code ATC    : " + codeAtc + "\n" +
                "╚══════════════════════════════════════╝";
    }
}