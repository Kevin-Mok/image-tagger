package fx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.ImageTagManager;

public class Main extends Application {
    ImageTagManager t = ImageTagManager.getInstance();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        t.readFromFile();
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

    public void stop() throws IOException {
        System.out.println("Stage is closing");
        t.saveToFile();

    }


}
