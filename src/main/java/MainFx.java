import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DatabaseConnection;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Medicare+ - Accueil");
        primaryStage.setScene(new Scene(root, 1100, 680));
        primaryStage.show();
    }

    @Override
    public void stop() {
        DatabaseConnection.close();
    }
}
