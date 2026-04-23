import interfaces.MainView;
import javafx.application.Application;
import javafx.stage.Stage;
import utils.DatabaseConnection;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        new MainView(primaryStage).show();
    }

    @Override
    public void stop() {
        DatabaseConnection.close();
    }
}
