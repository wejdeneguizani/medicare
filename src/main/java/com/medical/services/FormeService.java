package com.medical.services;

import com.medical.dao.FormeDAO;
import com.medical.interfaces.IFormeService;
import com.medical.model.Forme;
import java.util.List;

public class FormeService implements IFormeService {

    private FormeDAO dao = new FormeDAO();

    @Override
    public boolean ajouter(Forme f) {
        return dao.ajouter(f);
    }

    @Override
    public List<Forme> getTous() {
        return dao.getTous();
    }

    @Override
    public Forme getParId(int id) {
        return dao.getParId(id);
    }

    @Override
    public boolean modifier(Forme f) {
        return dao.modifier(f);
    }

    @Override
    public boolean supprimer(int id) {
        return dao.supprimer(id);
    }
}
