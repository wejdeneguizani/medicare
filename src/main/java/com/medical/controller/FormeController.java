package com.medical.controller;

import com.medical.model.Forme;
import com.medical.services.FormeService;
import java.util.List;

public class FormeController {

    private FormeService service = new FormeService();

    public boolean ajouterForme(Forme f) {
        if (f.getLibelle() == null || f.getLibelle().isEmpty()) {
            System.out.println("⚠️ Le libellé est obligatoire !");
            return false;
        }
        return service.ajouter(f);
    }

    public List<Forme> afficherTous() { return service.getTous(); }
    public Forme chercherParId(int id) { return service.getParId(id); }
    public boolean modifier(Forme f) { return service.modifier(f); }
    public boolean supprimer(int id) { return service.supprimer(id); }
}
