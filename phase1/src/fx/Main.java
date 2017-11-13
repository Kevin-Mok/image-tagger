package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout" +
                ".fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStage(primaryStage);
        // todo: not sure why taskbar icon won't show up
        // Image taskbarIcon = new Image("file:resources/icons/taskbar-icon
        // .png");
        primaryStage.setTitle("Image Renamer");
        // primaryStage.getIcons().add(taskbarIcon);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
