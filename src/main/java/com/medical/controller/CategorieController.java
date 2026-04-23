package com.medical.controller;

import com.medical.model.Categorie;
import com.medical.services.CategorieService;
import java.util.List;

public class CategorieController {

    private CategorieService service = new CategorieService();

    public boolean ajouterCategorie(Categorie c) {
        if (c.getLibelle() == null || c.getLibelle().isEmpty()) {
            System.out.println("⚠️ Le libellé est obligatoire !");
            return false;
        }
        return service.ajouter(c);
    }

    public List<Categorie> afficherTous() { return service.getTous(); }
    public Categorie chercherParId(int id) { return service.getParId(id); }
    public boolean modifier(Categorie c) { return service.modifier(c); }
    public boolean supprimer(int id) { return service.supprimer(id); }
}
