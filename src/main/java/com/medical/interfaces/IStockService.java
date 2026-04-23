package com.medical.interfaces;

import com.medical.model.Stock;
import java.util.List;

public interface IStockService {
    boolean ajouter(Stock s);
    List<Stock> getTous();
    List<Stock> getStockFaible();
    boolean modifier(Stock s);
    boolean supprimer(int id);
}