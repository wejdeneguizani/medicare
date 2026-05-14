import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ouvrirFenetre("/GestionObjectif.fxml",    "Medicare+ — Gestion des Objectifs");
        ouvrirFenetre("/GestionPlanCoaching.fxml", "Medicare+ — Plans de Coaching");
        ouvrirFenetre("/GestionProgression.fxml",  "Medicare+ — Suivi des Progressions");
    }

    private void ouvrirFenetre(String fxml, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(titre);
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur : " + fxml + " → " + e.getMessage());
            e.printStackTrace();
        }
    }
}