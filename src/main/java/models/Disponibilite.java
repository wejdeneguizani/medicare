package models;

public class Disponibilite {
    private int id_dispo, id_medecin;
    private String jour_semaine, heure_debut, heure_fin;
    private boolean actif;

    public Disponibilite() {}

    public Disponibilite(int id_medecin, String jour_semaine,
                         String heure_debut, String heure_fin, boolean actif) {
        this.id_medecin = id_medecin;
        this.jour_semaine = jour_semaine;
        this.heure_debut = heure_debut;
        this.heure_fin = heure_fin;
        this.actif = actif;
    }

    public int getId_dispo() { return id_dispo; }
    public void setId_dispo(int id_dispo) { this.id_dispo = id_dispo; }
    public int getId_medecin() { return id_medecin; }
    public void setId_medecin(int id_medecin) { this.id_medecin = id_medecin; }
    public String getJour_semaine() { return jour_semaine; }
    public void setJour_semaine(String jour_semaine) { this.jour_semaine = jour_semaine; }
    public String getHeure_debut() { return heure_debut; }
    public void setHeure_debut(String heure_debut) { this.heure_debut = heure_debut; }
    public String getHeure_fin() { return heure_fin; }
    public void setHeure_fin(String heure_fin) { this.heure_fin = heure_fin; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    @Override
    public String toString() {
        return "Disponibilite{" + jour_semaine + " " + heure_debut + "-" + heure_fin + "}\n";
    }
}
