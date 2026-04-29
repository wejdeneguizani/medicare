package com.medical.services;

import com.medical.dao.StockDAO;
import com.medical.interfaces.IService;
import com.medical.model.Stock;
import java.util.Date;
import java.util.List;

public class StockService implements IService<Stock> {

    private StockDAO dao = new StockDAO();

    @Override
    public boolean ajouter(Stock s) {
        // RÈGLE 1 : ID médicament obligatoire
        if (s.getIdMedicament() <= 0) {
            System.out.println("⚠️ L'ID du médicament est invalide !");
            return false;
        }
        // RÈGLE 2 : Numéro de lot obligatoire
        if (s.getNumeroLot() == null || s.getNumeroLot().trim().isEmpty()) {
            System.out.println("⚠️ Le numéro de lot est obligatoire !");
            return false;
        }
        // RÈGLE 3 : Quantité positive
        if (s.getQuantite() < 0) {
            System.out.println("⚠️ La quantité ne peut pas être négative !");
            return false;
        }
        // RÈGLE 4 : Prix positif
        if (s.getPrixUnitaire() <= 0) {
            System.out.println("⚠️ Le prix unitaire doit être positif !");
            return false;
        }
        // RÈGLE 5 : Date expiration obligatoire
        if (s.getDateExpiration() == null) {
            System.out.println("⚠️ La date d'expiration est obligatoire !");
            return false;
        }
        // RÈGLE 6 : Date expiration dans le futur
        if (s.getDateExpiration().before(new Date())) {
            System.out.println("⚠️ La date d'expiration est déjà passée !");
            return false;
        }
        // RÈGLE 7 : Seuil alerte positif
        if (s.getSeuilAlerte() < 0) {
            System.out.println("⚠️ Le seuil d'alerte ne peut pas être négatif !");
            return false;
        }
        return dao.ajouter(s);
    }

    @Override
    public List<Stock> getTous() {
        return dao.getTous();
    }

    @Override
    public Stock getParId(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return null;
        }
        return dao.getParId(id);
    }

    public List<Stock> getStockFaible() {
        List<Stock> liste = dao.getStockFaible();
        if (liste.isEmpty()) {
            System.out.println("✅ Aucun stock en alerte !");
        } else {
            System.out.println("⚠️ " + liste.size() + " stock(s) en alerte !");
        }
        return liste;
    }

    @Override
    public boolean modifier(Stock s) {
        if (s.getIdStock() <= 0) {
            System.out.println("⚠️ ID invalide !");
            return false;
        }
        if (s.getQuantite() < 0) {
            System.out.println("⚠️ La quantité ne peut pas être négative !");
            return false;
        }
        if (s.getPrixUnitaire() <= 0) {
            System.out.println("⚠️ Le prix unitaire doit être positif !");
            return false;
        }
        if (s.getSeuilAlerte() < 0) {
            System.out.println("⚠️ Le seuil d'alerte ne peut pas être négatif !");
            return false;
        }
        return dao.modifier(s);
    }

    @Override
    public boolean supprimer(int id) {
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être positif !");
            return false;
        }
        if (dao.getParId(id) == null) {
            System.out.println("⚠️ Ce stock n'existe pas !");
            return false;
        }
        return dao.supprimer(id);
    }
}
