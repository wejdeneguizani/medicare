package tn.esprit.medicare.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Habitude {

    public enum TypeHabitude {
        EAU,
        PAS,
        SOMMEIL,
        TENSION,
        ACTIVITE_PHYSIQUE,
        AUTRE
    }

    private int id;
    private int userId;
    private TypeHabitude type;
    private String titre;
    private String description;
    private double objectifValeur;
    private String unite;
    private boolean active;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Habitude() {
    }

    public Habitude(int id, int userId, TypeHabitude type, String titre, String description, double objectifValeur,
                    String unite, boolean active, LocalDate dateDebut, LocalDate dateFin, LocalDateTime createdAt,
                    LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.titre = titre;
        this.description = description;
        this.objectifValeur = objectifValeur;
        this.unite = unite;
        this.active = active;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public TypeHabitude getType() {
        return type;
    }

    public void setType(TypeHabitude type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getObjectifValeur() {
        return objectifValeur;
    }

    public void setObjectifValeur(double objectifValeur) {
        this.objectifValeur = objectifValeur;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
