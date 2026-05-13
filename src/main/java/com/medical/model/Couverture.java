package com.medical.model;

public class Couverture {
    private int idCouverture;
    private int idAssurance;
    private String typeService;
    private double pourcentageCouverture;
    private double montantMax;
    private String conditionSpeciale;

    public Couverture() {}

    public Couverture(int idAssurance, String typeService, double pourcentageCouverture,
                      double montantMax, String conditionSpeciale) {
        this.idAssurance = idAssurance;
        this.typeService = typeService;
        this.pourcentageCouverture = pourcentageCouverture;
        this.montantMax = montantMax;
        this.conditionSpeciale = conditionSpeciale;
    }

    public int getIdCouverture() { return idCouverture; }
    public void setIdCouverture(int idCouverture) { this.idCouverture = idCouverture; }
    public int getIdAssurance() { return idAssurance; }
    public void setIdAssurance(int idAssurance) { this.idAssurance = idAssurance; }
    public String getTypeService() { return typeService; }
    public void setTypeService(String typeService) { this.typeService = typeService; }
    public double getPourcentageCouverture() { return pourcentageCouverture; }
    public void setPourcentageCouverture(double pourcentageCouverture) { this.pourcentageCouverture = pourcentageCouverture; }
    public double getMontantMax() { return montantMax; }
    public void setMontantMax(double montantMax) { this.montantMax = montantMax; }
    public String getConditionSpeciale() { return conditionSpeciale; }
    public void setConditionSpeciale(String conditionSpeciale) { this.conditionSpeciale = conditionSpeciale; }
}
