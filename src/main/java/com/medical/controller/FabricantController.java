package com.medical.controller;

import com.medical.model.Fabricant;
import com.medical.services.FabricantService;
import java.util.List;

public class FabricantController {

    private FabricantService service = new FabricantService();

    public boolean ajouterFabricant(Fabricant f) {
        if (f.getNom() == null || f.getNom().isEmpty()) {
            System.out.println("⚠️ Le nom du fabricant est obligatoire !");
            return false;
        }
        return service.ajouter(f);
    }

    public List<Fabricant> afficherTous() { return service.getTous(); }
    public Fabricant chercherParId(int id) { return service.getParId(id); }
    public boolean modifier(Fabricant f) { return service.modifier(f); }
    public boolean supprimer(int id) { return service.supprimer(id); }
}
