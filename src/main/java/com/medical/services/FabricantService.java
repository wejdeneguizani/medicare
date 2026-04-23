package com.medical.services;

import com.medical.dao.FabricantDAO;
import com.medical.interfaces.IFabricantService;
import com.medical.model.Fabricant;
import java.util.List;

public class FabricantService implements IFabricantService {

    private FabricantDAO dao = new FabricantDAO();

    @Override
    public boolean ajouter(Fabricant f) {
        return dao.ajouter(f);
    }

    @Override
    public List<Fabricant> getTous() {
        return dao.getTous();
    }

    @Override
    public Fabricant getParId(int id) {
        return dao.getParId(id);
    }

    @Override
    public boolean modifier(Fabricant f) {
        return dao.modifier(f);
    }

    @Override
    public boolean supprimer(int id) {
        return dao.supprimer(id);
    }
}

