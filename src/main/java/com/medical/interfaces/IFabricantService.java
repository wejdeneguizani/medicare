package com.medical.interfaces;

import com.medical.model.Fabricant;
import java.util.List;

public interface IFabricantService {
    boolean ajouter(Fabricant f);
    List<Fabricant> getTous();
    Fabricant getParId(int id);
    boolean modifier(Fabricant f);
    boolean supprimer(int id);
}