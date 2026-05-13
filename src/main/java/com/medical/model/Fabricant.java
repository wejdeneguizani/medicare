package com.medical.model;

public class Fabricant {

    private int    idFabricant;
    private String nom;
    private String pays;
    private String contact;
    private String siteWeb;

    public Fabricant() {}

    public Fabricant(String nom, String pays, String contact, String siteWeb) {
        this.nom     = nom;
        this.pays    = pays;
        this.contact = contact;
        this.siteWeb = siteWeb;
    }

    public int    getIdFabricant()             { return idFabricant; }
    public void   setIdFabricant(int id)       { this.idFabricant = id; }
    public String getNom()                     { return nom; }
    public void   setNom(String nom)           { this.nom = nom; }
    public String getPays()                    { return pays; }
    public void   setPays(String pays)         { this.pays = pays; }
    public String getContact()                 { return contact; }
    public void   setContact(String contact)   { this.contact = contact; }
    public String getSiteWeb()                 { return siteWeb; }
    public void   setSiteWeb(String siteWeb)   { this.siteWeb = siteWeb; }

    @Override
    public String toString() {
        return "Fabricant{id=" + idFabricant + ", nom='" + nom + "', pays='" + pays + "'}";
    }
}