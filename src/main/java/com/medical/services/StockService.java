package com.medical.services;

import com.medical.dao.StockDAO;
import com.medical.interfaces.IStockService;
import com.medical.model.Stock;
import java.util.List;

public class StockService implements IStockService {

    private StockDAO dao = new StockDAO();

    @Override
    public boolean ajouter(Stock s) {
        return dao.ajouter(s);
    }

    @Override
    public List<Stock> getTous() {
        return dao.getTous();
    }

    @Override
    public List<Stock> getStockFaible() {
        return dao.getStockFaible();
    }

    @Override
    public boolean modifier(Stock s) {
        return dao.modifier(s);
    }

    @Override
    public boolean supprimer(int id) {
        return dao.supprimer(id);
    }
}
