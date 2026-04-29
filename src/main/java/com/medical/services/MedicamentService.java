package com.medical.services;

import com.medical.dao.MedicamentDAO;
import com.medical.interfaces.IMedicamentService;
import com.medical.model.Medicament;
import java.util.List;

public class MedicamentService implements IMedicamentService {

    private MedicamentDAO dao = new MedicamentDAO();

    @Override
    public boolean ajouter(Medicament m) {
        // ✅ RÈGLE 1 : Nom commercial obligatoire
        if (m.getNomCommercial() == null || m.getNomCommercial().trim().isEmpty()) {
            System.out.println("⚠️ Le nom commercial est obligatoire !");
            return false;
        }

        // ✅ RÈGLE 2 : Nom DCI obligatoire
        if (m.getNomDci() == null || m.getNomDci().trim().isEmpty()) {
            System.out.println("⚠️ Le nom DCI est obligatoire !");
            return false;
        }

        // ✅ RÈGLE 3 : Code barre ne doit pas déjà exister
        for (Medicament existing : dao.getTous()) {
            if (existing.getCodeBarre() != null &&
                    existing.getCodeBarre().equals(m.getCodeBarre())) {
                System.out.println("⚠️ Ce code barre existe déjà !");
                return false;
            }
        }

        // ✅ RÈGLE 4 : Dosage obligatoire
        if (m.getDosage() == null || m.getDosage().trim().isEmpty()) {
            System.out.println("⚠️ Le dosage est obligatoire !");
            return false;
        }

        return dao.ajouter(m);
    }

    @Override
    public List<Medicament> getTous() {
        return dao.getTous();
    }

    @Override
    public Medicament getParId(int id) {
        // ✅ RÈGLE : ID doit être positif
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être un nombre positif !");
            return null;
        }
        return dao.getParId(id);
    }

    @Override
    public boolean modifier(Medicament m) {
        // ✅ RÈGLE 1 : ID obligatoire
        if (m.getIdMedicament() <= 0) {
            System.out.println("⚠️ ID invalide pour la modification !");
            return false;
        }

        // ✅ RÈGLE 2 : Nom commercial obligatoire
        if (m.getNomCommercial() == null || m.getNomCommercial().trim().isEmpty()) {
            System.out.println("⚠️ Le nom commercial est obligatoire !");
            return false;
        }

        // ✅ RÈGLE 3 : Le médicament doit exister
        if (dao.getParId(m.getIdMedicament()) == null) {
            System.out.println("⚠️ Ce médicament n'existe pas !");
            return false;
        }

        return dao.modifier(m);
    }

    @Override
    public boolean supprimer(int id) {
        // ✅ RÈGLE 1 : ID doit être positif
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être un nombre positif !");
            return false;
        }

        // ✅ RÈGLE 2 : Le médicament doit exister
        Medicament m = dao.getParId(id);
        if (m == null) {
            System.out.println("⚠️ Ce médicament n'existe pas !");
            return false;
        }

        // ✅ RÈGLE 3 : Ne pas supprimer un médicament actif
        if (m.isEstActif()) {
            System.out.println("⚠️ Impossible de supprimer un médicament actif !");
            return false;
        }

        return dao.supprimer(id);
    }
}