package com.medical.model;

import java.sql.Date;

public class Remboursement {
    private int idRemboursement;
    private int idAssurance;
    private String typeDepense;
    private double montantDepense;
    private double montantEstime;
    private double montantValide;
    private Date dateDemande;
    private Date dateValidation;
    private String statut;
    private String commentaire;

    public Remboursement() {}

    public Remboursement(int idAssurance, String typeDepense, double montantDepense,
                         double montantEstime, double montantValide, Date dateDemande,
                         Date dateValidation, String statut, String commentaire) {
        this.idAssurance = idAssurance;
        this.typeDepense = typeDepense;
        this.montantDepense = montantDepense;
        this.montantEstime = montantEstime;
        this.montantValide = montantValide;
        this.dateDemande = dateDemande;
        this.dateValidation = dateValidation;
        this.statut = statut;
        this.commentaire = commentaire;
    }

    public int getIdRemboursement() { return idRemboursement; }
    public void setIdRemboursement(int idRemboursement) { this.idRemboursement = idRemboursement; }
    public int getIdAssurance() { return idAssurance; }
    public void setIdAssurance(int idAssurance) { this.idAssurance = idAssurance; }
    public String getTypeDepense() { return typeDepense; }
    public void setTypeDepense(String typeDepense) { this.typeDepense = typeDepense; }
    public double getMontantDepense() { return montantDepense; }
    public void setMontantDepense(double montantDepense) { this.montantDepense = montantDepense; }
    public double getMontantEstime() { return montantEstime; }
    public void setMontantEstime(double montantEstime) { this.montantEstime = montantEstime; }
    public double getMontantValide() { return montantValide; }
    public void setMontantValide(double montantValide) { this.montantValide = montantValide; }
    public Date getDateDemande() { return dateDemande; }
    public void setDateDemande(Date dateDemande) { this.dateDemande = dateDemande; }
    public Date getDateValidation() { return dateValidation; }
    public void setDateValidation(Date dateValidation) { this.dateValidation = dateValidation; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
