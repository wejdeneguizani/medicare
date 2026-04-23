package com.medical.interfaces;

import com.medical.model.Medicament;
import java.util.List;

public interface IMedicamentService {
    boolean ajouter(Medicament m);
    List<Medicament> getTous();
    Medicament getParId(int id);
    boolean modifier(Medicament m);
    boolean supprimer(int id);
}