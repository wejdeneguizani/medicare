package tn.esprit.medicare.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {

    public enum Sexe {
        M, F, Autre
    }

    public enum Statut {
        Actif, Inactif, En_attente, Bloque
    }

    private int id;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private int roleId;
    private Sexe sexe;
    private LocalDate dateNaissance;
    private String telephone;
    private String adresse;
    private String photoProfil;
    private Statut statut;
    private boolean deuxFacteurs;
    private int tentativesEchec;
    private LocalDateTime derniereConnexion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(int id, String matricule, String nom, String prenom, String email, String motDePasse, int roleId, Sexe sexe,
                LocalDate dateNaissance, String telephone, String adresse, String photoProfil, Statut statut,
                boolean deuxFacteurs, int tentativesEchec, LocalDateTime derniereConnexion, LocalDateTime createdAt,
                LocalDateTime updatedAt) {
        this.id = id;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.roleId = roleId;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.adresse = adresse;
        this.photoProfil = photoProfil;
        this.statut = statut;
        this.deuxFacteurs = deuxFacteurs;
        this.tentativesEchec = tentativesEchec;
        this.derniereConnexion = derniereConnexion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public boolean isDeuxFacteurs() {
        return deuxFacteurs;
    }

    public void setDeuxFacteurs(boolean deuxFacteurs) {
        this.deuxFacteurs = deuxFacteurs;
    }

    public int getTentativesEchec() {
        return tentativesEchec;
    }

    public void setTentativesEchec(int tentativesEchec) {
        this.tentativesEchec = tentativesEchec;
    }

    public LocalDateTime getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
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
