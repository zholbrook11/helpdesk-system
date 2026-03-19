import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.MainView;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        MainView root = new MainView();

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/ui/styles.css").toExternalForm());
        stage.setTitle("Help Desk System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}