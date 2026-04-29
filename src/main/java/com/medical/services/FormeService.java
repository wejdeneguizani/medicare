package com.medical.services;

import com.medical.dao.FormeDAO;
import com.medical.interfaces.IService;
import com.medical.model.Forme;
import java.util.List;

public class FormeService implements IService<Forme> {

    private FormeDAO dao = new FormeDAO();

    @Override
    public boolean ajouter(Forme f) {
        // RÈGLE 1 : Libellé obligatoire
        if (f.getLibelle() == null || f.getLibelle().trim().isEmpty()) {
            System.out.println("⚠️ Le libellé est obligatoire !");
            return false;
        }
        // RÈGLE 2 : Libellé minimum 3 caractères
        if (f.getLibelle().trim().length() < 3) {
            System.out.println("⚠️ Le libellé doit avoir au moins 3 caractères !");
            return false;
        }
        // RÈGLE 3 : Voie d'administration obligatoire
        if (f.getVoieAdministration() == null || f.getVoieAdministration().trim().isEmpty()) {
            System.out.println("⚠️ La voie d'administration est obligatoire !");
            return false;
        }
        // RÈGLE 4 : Libellé unique
        for (Forme existing : dao.getTous()) {
            if (existing.getLibelle().equalsIgnoreCase(f.getLibelle())) {
                System.out.println("⚠️ Cette forme pharmaceutique existe déjà !");
                return false;
            }
        }
        return dao.ajouter(f);
    }

    @Override
    public List<Forme> getTous() {
        return dao.getTous();
    }

    @Override
    public Forme getParId(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return null;
        }
        return dao.getParId(id);
    }

    @Override
    public boolean modifier(Forme f) {
        if (f.getIdForme() <= 0) {
            System.out.println("⚠️ ID invalide !");
            return false;
        }
        if (f.getLibelle() == null || f.getLibelle().trim().isEmpty()) {
            System.out.println("⚠️ Le libellé est obligatoire !");
            return false;
        }
        if (f.getVoieAdministration() == null || f.getVoieAdministration().trim().isEmpty()) {
            System.out.println("⚠️ La voie d'administration est obligatoire !");
            return false;
        }
        if (dao.getParId(f.getIdForme()) == null) {
            System.out.println("⚠️ Cette forme n'existe pas !");
            return false;
        }
        return dao.modifier(f);
    }

    @Override
    public boolean supprimer(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return false;
        }
        if (dao.getParId(id) == null) {
            System.out.println("⚠️ Cette forme n'existe pas !");
            return false;
        }
        return dao.supprimer(id);
    }
}
