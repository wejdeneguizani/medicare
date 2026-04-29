package com.medical.services;

import com.medical.dao.CategorieDAO;
import com.medical.interfaces.IService;
import com.medical.model.Categorie;
import java.util.List;

public class CategorieService implements IService<Categorie> {

    private CategorieDAO dao = new CategorieDAO();

    @Override
    public boolean ajouter(Categorie c) {
        // RÈGLE 1 : Libellé obligatoire
        if (c.getLibelle() == null || c.getLibelle().trim().isEmpty()) {
            System.out.println("⚠️ Le libellé est obligatoire !");
            return false;
        }
        // RÈGLE 2 : Libellé minimum 3 caractères
        if (c.getLibelle().trim().length() < 3) {
            System.out.println("⚠️ Le libellé doit avoir au moins 3 caractères !");
            return false;
        }
        // RÈGLE 3 : Code ATC unique
        for (Categorie existing : dao.getTous()) {
            if (existing.getCodeAtc() != null &&
                existing.getCodeAtc().equalsIgnoreCase(c.getCodeAtc())) {
                System.out.println("⚠️ Ce code ATC existe déjà !");
                return false;
            }
        }
        return dao.ajouter(c);
    }

    @Override
    public List<Categorie> getTous() {
        return dao.getTous();
    }

    @Override
    public Categorie getParId(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return null;
        }
        return dao.getParId(id);
    }

    @Override
    public boolean modifier(Categorie c) {
        if (c.getIdCategorie() <= 0) {
            System.out.println("⚠️ ID invalide !");
            return false;
        }
        if (c.getLibelle() == null || c.getLibelle().trim().isEmpty()) {
            System.out.println("⚠️ Le libellé est obligatoire !");
            return false;
        }
        if (dao.getParId(c.getIdCategorie()) == null) {
            System.out.println("⚠️ Cette catégorie n'existe pas !");
            return false;
        }
        return dao.modifier(c);
    }

    @Override
    public boolean supprimer(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return false;
        }
        if (dao.getParId(id) == null) {
            System.out.println("⚠️ Cette catégorie n'existe pas !");
            return false;
        }
        return dao.supprimer(id);
    }
}
