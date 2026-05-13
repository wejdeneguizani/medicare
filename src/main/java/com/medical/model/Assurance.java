package com.medical.model;

import java.sql.Date;

public class Assurance {
    private int idAssurance;
    private String numeroContrat;
    private String typeAssurance;
    private String nomAssureur;
    private Date dateDebut;
    private Date dateFin;
    private double plafondAnnuel;
    private double tauxBaseRemboursement;
    private String statut;
    private int idUser;

    public Assurance() {}

    public Assurance(String numeroContrat, String typeAssurance, String nomAssureur,
                     Date dateDebut, Date dateFin, double plafondAnnuel,
                     double tauxBaseRemboursement, String statut, int idUser) {
        this.numeroContrat = numeroContrat;
        this.typeAssurance = typeAssurance;
        this.nomAssureur = nomAssureur;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.plafondAnnuel = plafondAnnuel;
        this.tauxBaseRemboursement = tauxBaseRemboursement;
        this.statut = statut;
        this.idUser = idUser;
    }

    public int getIdAssurance() { return idAssurance; }
    public void setIdAssurance(int idAssurance) { this.idAssurance = idAssurance; }
    public String getNumeroContrat() { return numeroContrat; }
    public void setNumeroContrat(String numeroContrat) { this.numeroContrat = numeroContrat; }
    public String getTypeAssurance() { return typeAssurance; }
    public void setTypeAssurance(String typeAssurance) { this.typeAssurance = typeAssurance; }
    public String getNomAssureur() { return nomAssureur; }
    public void setNomAssureur(String nomAssureur) { this.nomAssureur = nomAssureur; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public double getPlafondAnnuel() { return plafondAnnuel; }
    public void setPlafondAnnuel(double plafondAnnuel) { this.plafondAnnuel = plafondAnnuel; }
    public double getTauxBaseRemboursement() { return tauxBaseRemboursement; }
    public void setTauxBaseRemboursement(double tauxBaseRemboursement) { this.tauxBaseRemboursement = tauxBaseRemboursement; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    @Override
    public String toString() {
        return "Assurance{id=" + idAssurance + ", contrat='" + numeroContrat + "', assureur='" + nomAssureur + "'}";
    }
}
