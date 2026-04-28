package tn.esprit.medicare.entities;

import java.time.LocalDateTime;

public class MesureSante {

    private int id;
    private int userId;
    private int habitudeId;
    private int pas;
    private double eauLitres;
    private Integer tensionSystolique;
    private Integer tensionDiastolique;
    private Double calories;
    private Double poidsKg;
    private Double sommeilHeures;
    private LocalDateTime dateMesure;
    private LocalDateTime createdAt;

    public MesureSante() {
    }

    public MesureSante(int id, int userId, int habitudeId, int pas, double eauLitres, Integer tensionSystolique, Integer tensionDiastolique,
                       Double calories, Double poidsKg, Double sommeilHeures, LocalDateTime dateMesure,
                       LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.habitudeId = habitudeId;
        this.pas = pas;
        this.eauLitres = eauLitres;
        this.tensionSystolique = tensionSystolique;
        this.tensionDiastolique = tensionDiastolique;
        this.calories = calories;
        this.poidsKg = poidsKg;
        this.sommeilHeures = sommeilHeures;
        this.dateMesure = dateMesure;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getHabitudeId() {
        return habitudeId;
    }

    public void setHabitudeId(int habitudeId) {
        this.habitudeId = habitudeId;
    }

    public int getPas() {
        return pas;
    }

    public void setPas(int pas) {
        this.pas = pas;
    }

    public double getEauLitres() {
        return eauLitres;
    }

    public void setEauLitres(double eauLitres) {
        this.eauLitres = eauLitres;
    }

    public Integer getTensionSystolique() {
        return tensionSystolique;
    }

    public void setTensionSystolique(Integer tensionSystolique) {
        this.tensionSystolique = tensionSystolique;
    }

    public Integer getTensionDiastolique() {
        return tensionDiastolique;
    }

    public void setTensionDiastolique(Integer tensionDiastolique) {
        this.tensionDiastolique = tensionDiastolique;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getPoidsKg() {
        return poidsKg;
    }

    public void setPoidsKg(Double poidsKg) {
        this.poidsKg = poidsKg;
    }

    public Double getSommeilHeures() {
        return sommeilHeures;
    }

    public void setSommeilHeures(Double sommeilHeures) {
        this.sommeilHeures = sommeilHeures;
    }

    public LocalDateTime getDateMesure() {
        return dateMesure;
    }

    public void setDateMesure(LocalDateTime dateMesure) {
        this.dateMesure = dateMesure;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
