package models;

public class Medecin {
    private int id_medecin;
    private String nom, prenom, email, telephone, specialite, num_ordre;

    public Medecin() {}

    public Medecin(String nom, String prenom, String email, String telephone,
                   String specialite, String num_ordre) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.specialite = specialite;
        this.num_ordre = num_ordre;
    }

    public int getId_medecin() { return id_medecin; }
    public void setId_medecin(int id_medecin) { this.id_medecin = id_medecin; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public String getNum_ordre() { return num_ordre; }
    public void setNum_ordre(String num_ordre) { this.num_ordre = num_ordre; }

    @Override
    public String toString() {
        return "Medecin{id=" + id_medecin + ", nom='" + nom + "', specialite='" + specialite + "'}\n";
    }
}
