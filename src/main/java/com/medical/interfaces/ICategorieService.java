package com.medical.interfaces;

import com.medical.model.Categorie;
import java.util.List;

public interface ICategorieService {
    boolean ajouter(Categorie c);
    List<Categorie> getTous();
    Categorie getParId(int id);
    boolean modifier(Categorie c);
    boolean supprimer(int id);
}