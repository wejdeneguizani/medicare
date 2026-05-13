package com.medical.model;

import java.util.Date;

public class PriseMedicament {

    private int     idPrise;
    private int     idDispensation;
    private int     idPatient;
    private Date    datetimePrevue;
    private Date    datetimeReelle;
    private String  statut;   // 'prise','sautee','retardee','annulee'
    private String  remarque;
    private Integer idMesureSante;

    public PriseMedicament() {}

    public PriseMedicament(int idDispensation, int idPatient,
                           Date datetimePrevue, Date datetimeReelle,
                           String statut, String remarque, Integer idMesureSante) {
        this.idDispensation = idDispensation;
        this.idPatient      = idPatient;
        this.datetimePrevue = datetimePrevue;
        this.datetimeReelle = datetimeReelle;
        this.statut         = statut;
        this.remarque       = remarque;
        this.idMesureSante  = idMesureSante;
    }

    public int     getIdPrise()                           { return idPrise; }
    public void    setIdPrise(int idPrise)                { this.idPrise = idPrise; }
    public int     getIdDispensation()                    { return idDispensation; }
    public void    setIdDispensation(int idDispensation)  { this.idDispensation = idDispensation; }
    public int     getIdPatient()                         { return idPatient; }
    public void    setIdPatient(int idPatient)            { this.idPatient = idPatient; }
    public Date    getDatetimePrevue()                    { return datetimePrevue; }
    public void    setDatetimePrevue(Date d)              { this.datetimePrevue = d; }
    public Date    getDatetimeReelle()                    { return datetimeReelle; }
    public void    setDatetimeReelle(Date d)              { this.datetimeReelle = d; }
    public String  getStatut()                            { return statut; }
    public void    setStatut(String statut)               { this.statut = statut; }
    public String  getRemarque()                          { return remarque; }
    public void    setRemarque(String remarque)           { this.remarque = remarque; }
    public Integer getIdMesureSante()                     { return idMesureSante; }
    public void    setIdMesureSante(Integer idMesureSante){ this.idMesureSante = idMesureSante; }

    @Override
    public String toString() {
        return "PriseMedicament{id=" + idPrise + ", idPatient=" + idPatient +
                ", statut='" + statut + "', prevue=" + datetimePrevue + "}";
    }
}