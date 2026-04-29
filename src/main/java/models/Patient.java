package models;

public class Patient {
    private int id_patient;
    private String nom, prenom, email, telephone, adresse, groupe_sanguin;
    private String date_naissance;

    public Patient() {}

    public Patient(String nom, String prenom, String email, String telephone,
                   String date_naissance, String groupe_sanguin, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.date_naissance = date_naissance;
        this.groupe_sanguin = groupe_sanguin;
        this.adresse = adresse;
    }

    public int getId_patient() { return id_patient; }
    public void setId_patient(int id_patient) { this.id_patient = id_patient; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getGroupe_sanguin() { return groupe_sanguin; }
    public void setGroupe_sanguin(String groupe_sanguin) { this.groupe_sanguin = groupe_sanguin; }
    public String getDate_naissance() { return date_naissance; }
    public void setDate_naissance(String date_naissance) { this.date_naissance = date_naissance; }

    @Override
    public String toString() {
        return "Patient{id=" + id_patient + ", nom='" + nom + "', prenom='" + prenom + "'}\n";
    }
}
