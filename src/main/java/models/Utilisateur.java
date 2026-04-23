package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Utilisateur {

    private int id;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private int roleId;
    private String roleNom;
    private String sexe;
    private LocalDate dateNaissance;
    private String telephone;
    private String adresse;
    private String statut;
    private boolean deuxFacteurs;
    private int tentativesEchec;
    private LocalDateTime derniereConnexion;
    private LocalDateTime createdAt;

    public Utilisateur() {}

    public Utilisateur(int id, String matricule, String nom, String prenom,
                       String email, String motDePasse, int roleId,
                       String sexe, LocalDate dateNaissance, String telephone,
                       String adresse, String statut) {
        this.id            = id;
        this.matricule     = matricule;
        this.nom           = nom;
        this.prenom        = prenom;
        this.email         = email;
        this.motDePasse    = motDePasse;
        this.roleId        = roleId;
        this.sexe          = sexe;
        this.dateNaissance = dateNaissance;
        this.telephone     = telephone;
        this.adresse       = adresse;
        this.statut        = statut;
    }

    public int getId()                           { return id; }
    public void setId(int id)                    { this.id = id; }

    public String getMatricule()                 { return matricule; }
    public void setMatricule(String matricule)   { this.matricule = matricule; }

    public String getNom()                       { return nom; }
    public void setNom(String nom)               { this.nom = nom; }

    public String getPrenom()                    { return prenom; }
    public void setPrenom(String prenom)         { this.prenom = prenom; }

    public String getNomComplet()                { return prenom + " " + nom; }

    public String getEmail()                     { return email; }
    public void setEmail(String email)           { this.email = email; }

    public String getMotDePasse()                { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public int getRoleId()                       { return roleId; }
    public void setRoleId(int roleId)            { this.roleId = roleId; }

    public String getRoleNom()                   { return roleNom; }
    public void setRoleNom(String roleNom)       { this.roleNom = roleNom; }

    public String getSexe()                      { return sexe; }
    public void setSexe(String sexe)             { this.sexe = sexe; }

    public LocalDate getDateNaissance()                      { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance)    { this.dateNaissance = dateNaissance; }

    public String getTelephone()                 { return telephone; }
    public void setTelephone(String telephone)   { this.telephone = telephone; }

    public String getAdresse()                   { return adresse; }
    public void setAdresse(String adresse)       { this.adresse = adresse; }

    public String getStatut()                    { return statut; }
    public void setStatut(String statut)         { this.statut = statut; }

    public boolean isDeuxFacteurs()              { return deuxFacteurs; }
    public void setDeuxFacteurs(boolean v)       { this.deuxFacteurs = v; }

    public int getTentativesEchec()              { return tentativesEchec; }
    public void setTentativesEchec(int v)        { this.tentativesEchec = v; }

    public LocalDateTime getDerniereConnexion()              { return derniereConnexion; }
    public void setDerniereConnexion(LocalDateTime v)        { this.derniereConnexion = v; }

    public LocalDateTime getCreatedAt()          { return createdAt; }
    public void setCreatedAt(LocalDateTime v)    { this.createdAt = v; }

    @Override
    public String toString() {
        return "Utilisateur{id=" + id + ", matricule='" + matricule + "', nom='" + getNomComplet() + "'}";
    }
}