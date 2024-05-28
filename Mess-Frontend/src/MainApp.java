import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginSignUpApp loginSignUpApp = new LoginSignUpApp();
        loginSignUpApp.start(primaryStage);
    }
}
