package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import models.Progression;

import java.io.IOException;

public class GestionProgressionController {

    public static Progression progression = new Progression();

    @FXML private Label lbIdObjectif;
    @FXML private Label lbDateMesure;
    @FXML private Label lbValeurActuelle;
    @FXML private Label lbValeurCible;
    @FXML private Label lbHumeur;
    @FXML private Label lbNotes;

    @FXML public void initialize() {
        lbIdObjectif.setText(String.valueOf(progression.getId_objectif()));
        lbDateMesure.setText(progression.getDate_mesure() != null ? progression.getDate_mesure().toString() : "");
        lbValeurActuelle.setText(String.valueOf(progression.getValeur_actuelle()));
        lbValeurCible.setText(String.valueOf(progression.getValeur_cible()));
        lbHumeur.setText(progression.getHumeur() != null ? progression.getHumeur() : "");
        lbNotes.setText(progression.getNotes() != null ? progression.getNotes() : "");
    }

    @FXML public void retourListe(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GestionProgression.fxml"));
            lbIdObjectif.getScene().setRoot(root);
        } catch (IOException ex) { ex.printStackTrace(); }
    }
}