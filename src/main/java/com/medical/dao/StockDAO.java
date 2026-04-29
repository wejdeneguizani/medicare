package com.medical.services;

import com.medical.dao.StockDAO;
import com.medical.interfaces.IStockService;
import com.medical.model.Stock;
import java.util.Date;
import java.util.List;

public class StockService implements IStockService {

    private StockDAO dao = new StockDAO();

    @Override
    public boolean ajouter(Stock s) {
        // ✅ RÈGLE 1 : ID médicament obligatoire
        if (s.getIdMedicament() <= 0) {
            System.out.println("⚠️ L'ID du médicament est invalide !");
            return false;
        }

        // ✅ RÈGLE 2 : Numéro de lot obligatoire
        if (s.getNumeroLot() == null || s.getNumeroLot().trim().isEmpty()) {
            System.out.println("⚠️ Le numéro de lot est obligatoire !");
            return false;
        }

        // ✅ RÈGLE 3 : Quantité doit être positive
        if (s.getQuantite() < 0) {
            System.out.println("⚠️ La quantité ne peut pas être négative !");
            return false;
        }

        // ✅ RÈGLE 4 : Prix unitaire doit être positif
        if (s.getPrixUnitaire() <= 0) {
            System.out.println("⚠️ Le prix unitaire doit être positif !");
            return false;
        }

        // ✅ RÈGLE 5 : Date expiration obligatoire
        if (s.getDateExpiration() == null) {
            System.out.println("⚠️ La date d'expiration est obligatoire !");
            return false;
        }

        // ✅ RÈGLE 6 : Date expiration doit être dans le futur
        if (s.getDateExpiration().before(new Date())) {
            System.out.println("⚠️ La date d'expiration est déjà passée !");
            return false;
        }

        // ✅ RÈGLE 7 : Seuil alerte doit être positif
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
    public List<Stock> getStockFaible() {
        List<Stock> stockFaible = dao.getStockFaible();
        if (stockFaible.isEmpty()) {
            System.out.println("✅ Aucun stock en alerte !");
        } else {
            System.out.println("⚠️ " + stockFaible.size() + " stock(s) en alerte !");
        }
        return stockFaible;
    }

    @Override
    public boolean modifier(Stock s) {
        // ✅ RÈGLE 1 : ID obligatoire
        if (s.getIdStock() <= 0) {
            System.out.println("⚠️ ID invalide pour la modification !");
            return false;
        }

        // ✅ RÈGLE 2 : Quantité ne peut pas être négative
        if (s.getQuantite() < 0) {
            System.out.println("⚠️ La quantité ne peut pas être négative !");
            return false;
        }

        // ✅ RÈGLE 3 : Prix unitaire doit être positif
        if (s.getPrixUnitaire() <= 0) {
            System.out.println("⚠️ Le prix unitaire doit être positif !");
            return false;
        }

        // ✅ RÈGLE 4 : Seuil alerte doit être positif
        if (s.getSeuilAlerte() < 0) {
            System.out.println("⚠️ Le seuil d'alerte ne peut pas être négatif !");
            return false;
        }

        return dao.modifier(s);
    }

    @Override
    public boolean supprimer(int id) {
        // ✅ RÈGLE 1 : ID doit être positif
        if (id <= 0) {
            System.out.println("⚠️ L'ID doit être un nombre positif !");
            return false;
        }

        return dao.supprimer(id);
    }
}