package com.medical.model;

public class Categorie {

    private int    idCategorie;
    private String libelle;
    private String codeAtc;
    private String description;

    public Categorie() {}

    public Categorie(String libelle, String codeAtc, String description) {
        this.libelle     = libelle;
        this.codeAtc     = codeAtc;
        this.description = description;
    }

    public int    getIdCategorie()            { return idCategorie; }
    public void   setIdCategorie(int id)      { this.idCategorie = id; }
    public String getLibelle()                { return libelle; }
    public void   setLibelle(String libelle)  { this.libelle = libelle; }
    public String getCodeAtc()                { return codeAtc; }
    public void   setCodeAtc(String codeAtc)  { this.codeAtc = codeAtc; }
    public String getDescription()            { return description; }
    public void   setDescription(String desc) { this.description = desc; }

    @Override
    public String toString() {
        return "Categorie{id=" + idCategorie + ", libelle='" + libelle + "', codeAtc='" + codeAtc + "'}";
    }
}