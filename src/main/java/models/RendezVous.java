package models;

public class RendezVous {
    private int id_rdv, id_patient, id_medecin, duree_min;
    private String date_heure, statut, motif, notes;

    public RendezVous() {}

    public RendezVous(int id_patient, int id_medecin, String date_heure,
                      int duree_min, String statut, String motif, String notes) {
        this.id_patient = id_patient;
        this.id_medecin = id_medecin;
        this.date_heure = date_heure;
        this.duree_min = duree_min;
        this.statut = statut;
        this.motif = motif;
        this.notes = notes;
    }

    public int getId_rdv() { return id_rdv; }
    public void setId_rdv(int id_rdv) { this.id_rdv = id_rdv; }
    public int getId_patient() { return id_patient; }
    public void setId_patient(int id_patient) { this.id_patient = id_patient; }
    public int getId_medecin() { return id_medecin; }
    public void setId_medecin(int id_medecin) { this.id_medecin = id_medecin; }
    public int getDuree_min() { return duree_min; }
    public void setDuree_min(int duree_min) { this.duree_min = duree_min; }
    public String getDate_heure() { return date_heure; }
    public void setDate_heure(String date_heure) { this.date_heure = date_heure; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "RendezVous{id=" + id_rdv + ", patient=" + id_patient +
                ", medecin=" + id_medecin + ", date='" + date_heure +
                "', statut='" + statut + "'}\n";
    }
}
