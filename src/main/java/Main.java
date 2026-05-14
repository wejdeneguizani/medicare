import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // ── Window 1: Objectif ──────────────────────────────────────────
        FXMLLoader loaderObj = new FXMLLoader(getClass().getResource("/GestionObjectif.fxml"));
        Stage stageObjectif = new Stage();
        stageObjectif.setTitle("Gestion des Objectifs");
        stageObjectif.setScene(new Scene(loaderObj.load()));
        stageObjectif.setX(50);
        stageObjectif.setY(50);
        stageObjectif.show();

        // ── Window 2: Plan Coaching ─────────────────────────────────────
        FXMLLoader loaderPlan = new FXMLLoader(getClass().getResource("/GestionPlanCoaching.fxml"));
        Stage stagePlan = new Stage();
        stagePlan.setTitle("Gestion des Plans de Coaching");
        stagePlan.setScene(new Scene(loaderPlan.load()));
        stagePlan.setX(100);
        stagePlan.setY(100);
        stagePlan.show();

        // ── Window 3: Progression ───────────────────────────────────────
        FXMLLoader loaderProg = new FXMLLoader(getClass().getResource("/GestionProgression.fxml"));
        Stage stageProg = new Stage();
        stageProg.setTitle("Gestion des Progressions");
        stageProg.setScene(new Scene(loaderProg.load()));
        stageProg.setX(150);
        stageProg.setY(150);
        stageProg.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}