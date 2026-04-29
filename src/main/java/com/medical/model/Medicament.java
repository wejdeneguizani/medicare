package com.medical.model;

public class Medicament {

    private int     idMedicament;
    private String  nomCommercial;
    private String  nomDci;
    private String  codeBarre;
    private int     idCategorie;
    private int     idForme;
    private int     idFabricant;
    private String  dosage;
    private String  uniteDosage;
    private String  description;
    private String  contreIndications;
    private String  effetsSecondaires;
    private boolean estActif;

    public Medicament() {}

    public Medicament(String nomCommercial, String nomDci, String codeBarre,
                      int idCategorie, int idForme, int idFabricant,
                      String dosage, String uniteDosage, String description,
                      String contreIndications, String effetsSecondaires, boolean estActif) {
        this.nomCommercial     = nomCommercial;
        this.nomDci            = nomDci;
        this.codeBarre         = codeBarre;
        this.idCategorie       = idCategorie;
        this.idForme           = idForme;
        this.idFabricant       = idFabricant;
        this.dosage            = dosage;
        this.uniteDosage       = uniteDosage;
        this.description       = description;
        this.contreIndications = contreIndications;
        this.effetsSecondaires = effetsSecondaires;
        this.estActif          = estActif;
    }

    public int     getIdMedicament()                        { return idMedicament; }
    public void    setIdMedicament(int id)                  { this.idMedicament = id; }
    public String  getNomCommercial()                       { return nomCommercial; }
    public void    setNomCommercial(String nomCommercial)   { this.nomCommercial = nomCommercial; }
    public String  getNomDci()                              { return nomDci; }
    public void    setNomDci(String nomDci)                 { this.nomDci = nomDci; }
    public String  getCodeBarre()                           { return codeBarre; }
    public void    setCodeBarre(String codeBarre)           { this.codeBarre = codeBarre; }
    public int     getIdCategorie()                         { return idCategorie; }
    public void    setIdCategorie(int idCategorie)          { this.idCategorie = idCategorie; }
    public int     getIdForme()                             { return idForme; }
    public void    setIdForme(int idForme)                  { this.idForme = idForme; }
    public int     getIdFabricant()                         { return idFabricant; }
    public void    setIdFabricant(int idFabricant)          { this.idFabricant = idFabricant; }
    public String  getDosage()                              { return dosage; }
    public void    setDosage(String dosage)                 { this.dosage = dosage; }
    public String  getUniteDosage()                         { return uniteDosage; }
    public void    setUniteDosage(String uniteDosage)       { this.uniteDosage = uniteDosage; }
    public String  getDescription()                         { return description; }
    public void    setDescription(String description)       { this.description = description; }
    public String  getContreIndications()                   { return contreIndications; }
    public void    setContreIndications(String ci)          { this.contreIndications = ci; }
    public String  getEffetsSecondaires()                   { return effetsSecondaires; }
    public void    setEffetsSecondaires(String es)          { this.effetsSecondaires = es; }
    public boolean isEstActif()                             { return estActif; }
    public void    setEstActif(boolean estActif)            { this.estActif = estActif; }

    @Override
    public String toString() {
        return "Medicament{id=" + idMedicament + ", nom='" + nomCommercial +
               "', dci='" + nomDci + "', dosage='" + dosage + " " + uniteDosage +
               "', actif=" + estActif + "}";
    }
}
