package com.medical.model;

import java.util.Date;

public class InteractionMedicament {

    private int    idInteraction;
    private int    idMedicamentA;
    private int    idMedicamentB;
    private String niveauSeverite;    // 'faible','modere','severe','contre-indique'
    private String descriptionEffet;
    private String recommandation;
    private Date   dateAjout;

    public InteractionMedicament() {}

    public InteractionMedicament(int idMedicamentA, int idMedicamentB,
                                 String niveauSeverite, String descriptionEffet,
                                 String recommandation) {
        this.idMedicamentA    = idMedicamentA;
        this.idMedicamentB    = idMedicamentB;
        this.niveauSeverite   = niveauSeverite;
        this.descriptionEffet = descriptionEffet;
        this.recommandation   = recommandation;
    }

    public int    getIdInteraction()                           { return idInteraction; }
    public void   setIdInteraction(int idInteraction)         { this.idInteraction = idInteraction; }
    public int    getIdMedicamentA()                           { return idMedicamentA; }
    public void   setIdMedicamentA(int idMedicamentA)         { this.idMedicamentA = idMedicamentA; }
    public int    getIdMedicamentB()                           { return idMedicamentB; }
    public void   setIdMedicamentB(int idMedicamentB)         { this.idMedicamentB = idMedicamentB; }
    public String getNiveauSeverite()                          { return niveauSeverite; }
    public void   setNiveauSeverite(String niveauSeverite)    { this.niveauSeverite = niveauSeverite; }
    public String getDescriptionEffet()                        { return descriptionEffet; }
    public void   setDescriptionEffet(String descriptionEffet){ this.descriptionEffet = descriptionEffet; }
    public String getRecommandation()                          { return recommandation; }
    public void   setRecommandation(String recommandation)     { this.recommandation = recommandation; }
    public Date   getDateAjout()                               { return dateAjout; }
    public void   setDateAjout(Date dateAjout)                 { this.dateAjout = dateAjout; }

    @Override
    public String toString() {
        return "InteractionMedicament{id=" + idInteraction +
                ", medA=" + idMedicamentA + ", medB=" + idMedicamentB +
                ", severite='" + niveauSeverite + "'}";
    }
}