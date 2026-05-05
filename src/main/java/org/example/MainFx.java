package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/GestionRendezVous.fxml"));
        primaryStage.setTitle("Medicare+ — Gestion des Rendez-Vous");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }
}