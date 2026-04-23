package com.medical.controller;

import com.medical.model.Stock;
import com.medical.services.StockService;
import java.util.List;

public class StockController {

    private StockService service = new StockService();

    public boolean ajouterStock(Stock s) {
        if (s.getQuantite() < 0) {
            System.out.println("⚠️ La quantité ne peut pas être négative !");
            return false;
        }
        return service.ajouter(s);
    }

    public List<Stock> afficherTous() { return service.getTous(); }
    public List<Stock> afficherStockFaible() { return service.getStockFaible(); }
    public boolean modifier(Stock s) { return service.modifier(s); }
    public boolean supprimer(int id) { return service.supprimer(id); }
}
