package com.medical.services;

import com.medical.dao.FormeDAO;
import com.medical.interfaces.IFormeService;
import com.medical.model.Forme;
import java.util.List;

public class FormeService implements IFormeService {

    private FormeDAO dao = new FormeDAO();

    @Override
    public boolean ajouter(Forme f) {
        // ✅ RÈGLE 1 : Libellé obligatoire
        if (f.getLibelle() == null || f.getLibelle().trim().isEmpty()) {
            System.out.println("⚠️ Le libellé de la forme est obligatoire !");
            return false;
        }

        // ✅ RÈGLE 2 : Libellé minimum 3 caractères
        if (f.getLibelle().trim().length() < 3) {
            System.out.println("⚠️ Le libellé doit avoir au moins 3 caractères !");
            return false;
        }

        // ✅ RÈGLE 3 : Voie d'administration obligatoire
        if (f.getVoieAdministration() == null || f.getVoieAdministration().trim().isEmpty()) {
            System.out.println("⚠️ La voie d'administration est obligatoire !");
            return false;
        }

        // ✅ RÈGLE 4 : Libellé ne doit pas déjà exister
        for (Forme existing : dao.getTous()) {
            if (existing.getLibelle() != null &&
                    existing.getLibelle().equalsIgnoreCase(f.getLibelle())) {
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
        // ✅ RÈGLE : ID doit être positif
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être un nombre positif !");
            return null;
        }
        return dao.getParId(id);
    }

    @Override
    public boolean modifier(Forme f) {
        // ✅ RÈGLE 1 : ID obligatoire
        if (f.getIdForme() <= 0) {
            System.out.println("⚠️ ID invalide pour la modification !");
            return false;
        }

        // ✅ RÈGLE 2 : Libellé obligatoire
        if (f.getLibelle() == null || f.getLibelle().trim().isEmpty()) {
            System.out.println("⚠️ Le libellé est obligatoire !");
            return false;
        }

        // ✅ RÈGLE 3 : Voie d'administration obligatoire
        if (f.getVoieAdministration() == null || f.getVoieAdministration().trim().isEmpty()) {
            System.out.println("⚠️ La voie d'administration est obligatoire !");
            return false;
        }

        // ✅ RÈGLE 4 : La forme doit exister
        if (dao.getParId(f.getIdForme()) == null) {
            System.out.println("⚠️ Cette forme n'existe pas !");
            return false;
        }

        return dao.modifier(f);
    }

    @Override
    public boolean supprimer(int id) {
        // ✅ RÈGLE 1 : ID doit être positif
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être un nombre positif !");
            return false;
        }

        // ✅ RÈGLE 2 : La forme doit exister
        if (dao.getParId(id) == null) {
            System.out.println("⚠️ Cette forme n'existe pas !");
            return false;
        }

        return dao.supprimer(id);
    }
}