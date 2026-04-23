package com.medical.services;

import com.medical.dao.CategorieDAO;
import com.medical.interfaces.ICategorieService;
import com.medical.model.Categorie;
import java.util.List;

public class CategorieService implements ICategorieService {

    private CategorieDAO dao = new CategorieDAO();

    @Override
    public boolean ajouter(Categorie c) {
        return dao.ajouter(c);
    }

    @Override
    public List<Categorie> getTous() {
        return dao.getTous();
    }

    @Override
    public Categorie getParId(int id) {
        return dao.getParId(id);
    }

    @Override
    public boolean modifier(Categorie c) {
        return dao.modifier(c);
    }

    @Override
    public boolean supprimer(int id) {
        return dao.supprimer(id);
    }
}
