package com.medical.services;

import com.medical.dao.MedicamentDAO;
import com.medical.interfaces.IMedicamentService;
import com.medical.model.Medicament;
import java.util.List;

public class MedicamentService implements IMedicamentService {

    private MedicamentDAO dao = new MedicamentDAO();

    @Override
    public boolean ajouter(Medicament m) {
        return dao.ajouter(m);
    }

    @Override
    public List<Medicament> getTous() {
        return dao.getTous();
    }

    @Override
    public Medicament getParId(int id) {
        return dao.getParId(id);
    }

    @Override
    public boolean modifier(Medicament m) {
        return dao.modifier(m);
    }

    @Override
    public boolean supprimer(int id) {
        return dao.supprimer(id);
    }
}