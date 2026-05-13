package com.medical.model;

import java.util.Date;

public class AlerteMedicament {

    private int     idAlerte;
    private int     idPatient;
    private Integer idStock;
    private String  typeAlerte;   // 'prise','stock_bas','expiration','renouvellement','interaction'
    private String  messageAlerte;
    private Date    datetimeAlerte;
    private boolean estEnvoyee;
    private boolean estAcquittee;
    private String  canal;        // 'app','email','sms'
    private Date    dateAcquittement;
    private String  recurrence;   // 'unique','quotidienne','hebdomadaire'

    public AlerteMedicament() {}

    public AlerteMedicament(int idPatient, Integer idStock, String typeAlerte,
                            String messageAlerte, Date datetimeAlerte,
                            String canal, String recurrence) {
        this.idPatient      = idPatient;
        this.idStock        = idStock;
        this.typeAlerte     = typeAlerte;
        this.messageAlerte  = messageAlerte;
        this.datetimeAlerte = datetimeAlerte;
        this.canal          = canal;
        this.recurrence     = recurrence;
    }

    public int     getIdAlerte()                           { return idAlerte; }
    public void    setIdAlerte(int idAlerte)               { this.idAlerte = idAlerte; }
    public int     getIdPatient()                          { return idPatient; }
    public void    setIdPatient(int idPatient)             { this.idPatient = idPatient; }
    public Integer getIdStock()                            { return idStock; }
    public void    setIdStock(Integer idStock)             { this.idStock = idStock; }
    public String  getTypeAlerte()                         { return typeAlerte; }
    public void    setTypeAlerte(String typeAlerte)        { this.typeAlerte = typeAlerte; }
    public String  getMessageAlerte()                      { return messageAlerte; }
    public void    setMessageAlerte(String messageAlerte)  { this.messageAlerte = messageAlerte; }
    public Date    getDatetimeAlerte()                     { return datetimeAlerte; }
    public void    setDatetimeAlerte(Date d)               { this.datetimeAlerte = d; }
    public boolean isEstEnvoyee()                          { return estEnvoyee; }
    public void    setEstEnvoyee(boolean estEnvoyee)       { this.estEnvoyee = estEnvoyee; }
    public boolean isEstAcquittee()                        { return estAcquittee; }
    public void    setEstAcquittee(boolean estAcquittee)   { this.estAcquittee = estAcquittee; }
    public String  getCanal()                              { return canal; }
    public void    setCanal(String canal)                  { this.canal = canal; }
    public Date    getDateAcquittement()                   { return dateAcquittement; }
    public void    setDateAcquittement(Date d)             { this.dateAcquittement = d; }
    public String  getRecurrence()                         { return recurrence; }
    public void    setRecurrence(String recurrence)        { this.recurrence = recurrence; }

    @Override
    public String toString() {
        return "AlerteMedicament{id=" + idAlerte + ", idPatient=" + idPatient +
                ", type='" + typeAlerte + "', canal='" + canal + "', envoyee=" + estEnvoyee + "}";
    }
}