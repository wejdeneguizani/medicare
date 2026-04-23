package com.medical.model;

public class Medicament {

    private int idMedicament;
    private String nomCommercial;
    private String nomDci;
    private String codeBarre;
    private int idCategorie;
    private int idForme;
    private int idFabricant;
    private String dosage;
    private String uniteDosage;
    private String description;
    private String contreIndications;
    private String effetsSecondaires;
    private boolean estActif;

    // ============================================
    // Constructeur vide
    // ============================================
    public Medicament() {}

    // ============================================
    // Constructeur complet
    // ============================================
    public Medicament(int idMedicament, String nomCommercial, String nomDci,
                      String codeBarre, int idCategorie, int idForme,
                      int idFabricant, String dosage, String uniteDosage,
                      String description, String contreIndications,
                      String effetsSecondaires, boolean estActif) {
        this.idMedicament = idMedicament;
        this.nomCommercial = nomCommercial;
        this.nomDci = nomDci;
        this.codeBarre = codeBarre;
        this.idCategorie = idCategorie;
        this.idForme = idForme;
        this.idFabricant = idFabricant;
        this.dosage = dosage;
        this.uniteDosage = uniteDosage;
        this.description = description;
        this.contreIndications = contreIndications;
        this.effetsSecondaires = effetsSecondaires;
        this.estActif = estActif;
    }

    // ============================================
    // Getters & Setters
    // ============================================
    public int getIdMedicament() { return idMedicament; }
    public void setIdMedicament(int idMedicament) { this.idMedicament = idMedicament; }

    public String getNomCommercial() { return nomCommercial; }
    public void setNomCommercial(String nomCommercial) { this.nomCommercial = nomCommercial; }

    public String getNomDci() { return nomDci; }
    public void setNomDci(String nomDci) { this.nomDci = nomDci; }

    public String getCodeBarre() { return codeBarre; }
    public void setCodeBarre(String codeBarre) { this.codeBarre = codeBarre; }

    public int getIdCategorie() { return idCategorie; }
    public void setIdCategorie(int idCategorie) { this.idCategorie = idCategorie; }

    public int getIdForme() { return idForme; }
    public void setIdForme(int idForme) { this.idForme = idForme; }

    public int getIdFabricant() { return idFabricant; }
    public void setIdFabricant(int idFabricant) { this.idFabricant = idFabricant; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getUniteDosage() { return uniteDosage; }
    public void setUniteDosage(String uniteDosage) { this.uniteDosage = uniteDosage; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getContreIndications() { return contreIndications; }
    public void setContreIndications(String contreIndications) { this.contreIndications = contreIndications; }

    public String getEffetsSecondaires() { return effetsSecondaires; }
    public void setEffetsSecondaires(String effetsSecondaires) { this.effetsSecondaires = effetsSecondaires; }

    public boolean isEstActif() { return estActif; }
    public void setEstActif(boolean estActif) { this.estActif = estActif; }

    // ============================================
    // Affichage
    // ============================================
    @Override
    public String toString() {
        return "╔══════════════════════════════════════╗\n" +
                "  ID        : " + idMedicament + "\n" +
                "  Nom       : " + nomCommercial + "\n" +
                "  DCI       : " + nomDci + "\n" +
                "  Dosage    : " + dosage + " " + uniteDosage + "\n" +
                "  Actif     : " + (estActif ? "Oui" : "Non") + "\n" +
                "╚══════════════════════════════════════╝";
    }
}
