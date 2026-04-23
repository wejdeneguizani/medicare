package com.medical.interfaces;

import com.medical.model.Forme;
import java.util.List;

public interface IFormeService {
    boolean ajouter(Forme f);
    List<Forme> getTous();
    Forme getParId(int id);
    boolean modifier(Forme f);
    boolean supprimer(int id);
}