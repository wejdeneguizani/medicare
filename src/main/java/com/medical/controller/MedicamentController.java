package com.medical.controller;

import com.medical.model.Medicament;
import com.medical.services.MedicamentService;
import java.util.List;

public class MedicamentController {

    private MedicamentService service = new MedicamentService();

    public boolean ajouterMedicament(Medicament m) {
        if (m.getNomCommercial() == null || m.getNomCommercial().isEmpty()) {
            System.out.println("⚠️ Le nom commercial est obligatoire !");
            return false;
        }
        return service.ajouter(m);
    }

    public List<Medicament> afficherTous() {
        return service.getTous();
    }

    public Medicament chercherParId(int id) {
        return service.getParId(id);
    }

    public boolean modifierMedicament(Medicament m) {
        return service.modifier(m);
    }

    public boolean supprimerMedicament(int id) {
        return service.supprimer(id);
    }
}