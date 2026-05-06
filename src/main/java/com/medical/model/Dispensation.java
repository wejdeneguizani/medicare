package com.medical.model;

import java.util.Date;

public class Dispensation {

    private int     idDispensation;
    private int     idStock;
    private Integer idAssurance;
    private int     idPatient;
    private int     quantiteDispensee;
    private Date    dateDispensation;
    private double  prixTotal;
    private double  tauxRemboursement;
    private double  montantRembourse;
    private double  resteAPayer;
    private Integer dispensePar;
    private String  posologie;
    private String  instructions;
    private String  remarques;

    public Dispensation() {}

    public Dispensation(int idStock, Integer idAssurance, int idPatient,
                        int quantiteDispensee, double prixTotal,
                        double tauxRemboursement, double montantRembourse,
                        double resteAPayer, Integer dispensePar,
                        String posologie, String instructions, String remarques) {
        this.idStock           = idStock;
        this.idAssurance       = idAssurance;
        this.idPatient         = idPatient;
        this.quantiteDispensee = quantiteDispensee;
        this.prixTotal         = prixTotal;
        this.tauxRemboursement = tauxRemboursement;
        this.montantRembourse  = montantRembourse;
        this.resteAPayer       = resteAPayer;
        this.dispensePar       = dispensePar;
        this.posologie         = posologie;
        this.instructions      = instructions;
        this.remarques         = remarques;
    }

    public int     getIdDispensation()                         { return idDispensation; }
    public void    setIdDispensation(int idDispensation)       { this.idDispensation = idDispensation; }
    public int     getIdStock()                                { return idStock; }
    public void    setIdStock(int idStock)                     { this.idStock = idStock; }
    public Integer getIdAssurance()                            { return idAssurance; }
    public void    setIdAssurance(Integer idAssurance)         { this.idAssurance = idAssurance; }
    public int     getIdPatient()                              { return idPatient; }
    public void    setIdPatient(int idPatient)                 { this.idPatient = idPatient; }
    public int     getQuantiteDispensee()                      { return quantiteDispensee; }
    public void    setQuantiteDispensee(int q)                 { this.quantiteDispensee = q; }
    public Date    getDateDispensation()                       { return dateDispensation; }
    public void    setDateDispensation(Date d)                 { this.dateDispensation = d; }
    public double  getPrixTotal()                              { return prixTotal; }
    public void    setPrixTotal(double prixTotal)              { this.prixTotal = prixTotal; }
    public double  getTauxRemboursement()                      { return tauxRemboursement; }
    public void    setTauxRemboursement(double t)              { this.tauxRemboursement = t; }
    public double  getMontantRembourse()                       { return montantRembourse; }
    public void    setMontantRembourse(double m)               { this.montantRembourse = m; }
    public double  getResteAPayer()                            { return resteAPayer; }
    public void    setResteAPayer(double r)                    { this.resteAPayer = r; }
    public Integer getDispensePar()                            { return dispensePar; }
    public void    setDispensePar(Integer dispensePar)         { this.dispensePar = dispensePar; }
    public String  getPosologie()                              { return posologie; }
    public void    setPosologie(String posologie)              { this.posologie = posologie; }
    public String  getInstructions()                           { return instructions; }
    public void    setInstructions(String instructions)        { this.instructions = instructions; }
    public String  getRemarques()                              { return remarques; }
    public void    setRemarques(String remarques)              { this.remarques = remarques; }

    @Override
    public String toString() {
        return "Dispensation{id=" + idDispensation + ", idPatient=" + idPatient +
                ", idStock=" + idStock + ", quantite=" + quantiteDispensee +
                ", prixTotal=" + prixTotal + "}";
    }
}