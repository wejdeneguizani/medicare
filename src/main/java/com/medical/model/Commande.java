package com.medical.model;

import java.util.Date;

/**
 * Représente une commande.
 * typeCommande = "CLIENT"         → vente au client (diminue le stock)
 * typeCommande = "REAPPROVISIONNEMENT" → commande fournisseur (augmente le stock)
 */
public class Commande {

    private int    idCommande;
    private String typeCommande;      // "CLIENT" ou "REAPPROVISIONNEMENT"
    private String nomClient;         // nom client ou nom fournisseur
    private String emailClient;       // pour envoyer le reçu
    private String telephoneClient;
    private int    idStock;
    private int    quantite;
    private double prixUnitaire;
    private double montantTotal;
    private Date   dateCommande;
    private String modePaiement;
    private String statut;            // Confirmée, En attente, Annulée

    // Champs transients (jointure, non en BDD)
    private String nomMedicament;
    private int    stockDisponible;
    private String numeroLot;

    public Commande() {}

    // Getters / Setters
    public int    getIdCommande()                          { return idCommande; }
    public void   setIdCommande(int v)                     { idCommande = v; }
    public String getTypeCommande()                        { return typeCommande; }
    public void   setTypeCommande(String v)                { typeCommande = v; }
    public String getNomClient()                           { return nomClient; }
    public void   setNomClient(String v)                   { nomClient = v; }
    public String getEmailClient()                         { return emailClient; }
    public void   setEmailClient(String v)                 { emailClient = v; }
    public String getTelephoneClient()                     { return telephoneClient; }
    public void   setTelephoneClient(String v)             { telephoneClient = v; }
    public int    getIdStock()                             { return idStock; }
    public void   setIdStock(int v)                        { idStock = v; }
    public int    getQuantite()                            { return quantite; }
    public void   setQuantite(int v)                       { quantite = v; }
    public double getPrixUnitaire()                        { return prixUnitaire; }
    public void   setPrixUnitaire(double v)                { prixUnitaire = v; }
    public double getMontantTotal()                        { return montantTotal; }
    public void   setMontantTotal(double v)                { montantTotal = v; }
    public Date   getDateCommande()                        { return dateCommande; }
    public void   setDateCommande(Date v)                  { dateCommande = v; }
    public String getModePaiement()                        { return modePaiement; }
    public void   setModePaiement(String v)                { modePaiement = v; }
    public String getStatut()                              { return statut; }
    public void   setStatut(String v)                      { statut = v; }
    public String getNomMedicament()                       { return nomMedicament; }
    public void   setNomMedicament(String v)               { nomMedicament = v; }
    public int    getStockDisponible()                     { return stockDisponible; }
    public void   setStockDisponible(int v)                { stockDisponible = v; }
    public String getNumeroLot()                           { return numeroLot; }
    public void   setNumeroLot(String v)                   { numeroLot = v; }
}