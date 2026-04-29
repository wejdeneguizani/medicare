package com.medical.interfaces;

import java.util.List;

public interface IService<T> {
    boolean ajouter(T t);
    List<T> getTous();
    T getParId(int id);
    boolean modifier(T t);
    boolean supprimer(int id);
}
