package com.medical.model;

import java.util.Date;

public class Stock {

    private int    idStock;
    private int    idMedicament;
    private String numeroLot;
    private int    quantite;
    private int    seuilAlerte;
    private double prixUnitaire;
    private Date   dateExpiration;
    private Date   dateReception;
    private String localisation;
    private String fournisseur;

    public Stock() {}

    public Stock(int idMedicament, String numeroLot, int quantite, int seuilAlerte,
                 double prixUnitaire, Date dateExpiration, Date dateReception,
                 String localisation, String fournisseur) {
        this.idMedicament   = idMedicament;
        this.numeroLot      = numeroLot;
        this.quantite       = quantite;
        this.seuilAlerte    = seuilAlerte;
        this.prixUnitaire   = prixUnitaire;
        this.dateExpiration = dateExpiration;
        this.dateReception  = dateReception;
        this.localisation   = localisation;
        this.fournisseur    = fournisseur;
    }

    public int    getIdStock()                         { return idStock; }
    public void   setIdStock(int idStock)              { this.idStock = idStock; }
    public int    getIdMedicament()                    { return idMedicament; }
    public void   setIdMedicament(int idMedicament)    { this.idMedicament = idMedicament; }
    public String getNumeroLot()                       { return numeroLot; }
    public void   setNumeroLot(String numeroLot)       { this.numeroLot = numeroLot; }
    public int    getQuantite()                        { return quantite; }
    public void   setQuantite(int quantite)            { this.quantite = quantite; }
    public int    getSeuilAlerte()                     { return seuilAlerte; }
    public void   setSeuilAlerte(int seuilAlerte)      { this.seuilAlerte = seuilAlerte; }
    public double getPrixUnitaire()                    { return prixUnitaire; }
    public void   setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public Date   getDateExpiration()                  { return dateExpiration; }
    public void   setDateExpiration(Date d)            { this.dateExpiration = d; }
    public Date   getDateReception()                   { return dateReception; }
    public void   setDateReception(Date d)             { this.dateReception = d; }
    public String getLocalisation()                    { return localisation; }
    public void   setLocalisation(String localisation) { this.localisation = localisation; }
    public String getFournisseur()                     { return fournisseur; }
    public void   setFournisseur(String fournisseur)   { this.fournisseur = fournisseur; }

    @Override
    public String toString() {
        return "Stock{id=" + idStock + ", lot='" + numeroLot + "', quantite=" + quantite +
                ", seuilAlerte=" + seuilAlerte + ", prix=" + prixUnitaire + "}";
    }
}