package com.medical.services;

import com.medical.dao.FabricantDAO;
import com.medical.interfaces.IService;
import com.medical.model.Fabricant;
import java.util.List;

public class FabricantService implements IService<Fabricant> {

    private FabricantDAO dao = new FabricantDAO();

    @Override
    public boolean ajouter(Fabricant f) {
        // RÈGLE 1 : Nom obligatoire
        if (f.getNom() == null || f.getNom().trim().isEmpty()) {
            System.out.println("⚠️ Le nom du fabricant est obligatoire !");
            return false;
        }
        // RÈGLE 2 : Nom minimum 2 caractères
        if (f.getNom().trim().length() < 2) {
            System.out.println("⚠️ Le nom doit avoir au moins 2 caractères !");
            return false;
        }
        // RÈGLE 3 : Pays obligatoire
        if (f.getPays() == null || f.getPays().trim().isEmpty()) {
            System.out.println("⚠️ Le pays est obligatoire !");
            return false;
        }
        // RÈGLE 4 : Nom unique
        for (Fabricant existing : dao.getTous()) {
            if (existing.getNom().equalsIgnoreCase(f.getNom())) {
                System.out.println("⚠️ Ce fabricant existe déjà !");
                return false;
            }
        }
        // RÈGLE 5 : Email valide si fourni
        if (f.getContact() != null && f.getContact().contains("@") && !f.getContact().contains(".")) {
            System.out.println("⚠️ L'email du contact est invalide !");
            return false;
        }
        return dao.ajouter(f);
    }

    @Override
    public List<Fabricant> getTous() {
        return dao.getTous();
    }

    @Override
    public Fabricant getParId(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return null;
        }
        return dao.getParId(id);
    }

    @Override
    public boolean modifier(Fabricant f) {
        if (f.getIdFabricant() <= 0) {
            System.out.println("⚠️ ID invalide !");
            return false;
        }
        if (f.getNom() == null || f.getNom().trim().isEmpty()) {
            System.out.println("⚠️ Le nom est obligatoire !");
            return false;
        }
        if (f.getPays() == null || f.getPays().trim().isEmpty()) {
            System.out.println("⚠️ Le pays est obligatoire !");
            return false;
        }
        if (dao.getParId(f.getIdFabricant()) == null) {
            System.out.println("⚠️ Ce fabricant n'existe pas !");
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
            System.out.println("⚠️ Ce fabricant n'existe pas !");
            return false;
        }
        return dao.supprimer(id);
    }
}
