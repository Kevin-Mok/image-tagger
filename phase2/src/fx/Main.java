package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.ImageTagManager;

import java.io.IOException;

/**
 * Main class from which our program runs.
 */
public class Main extends Application {
    private ImageTagManager imgTagManager = ImageTagManager.getInstance();

    /**
     * Launches the application
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start by reading serialized image and tag information from the local
     * hard disk,
     * then, load the FXML file that contains the application layout
     *
     * @param primaryStage the window where the application will be displayed
     * @throws IOException if any errors occur during file reading
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        imgTagManager.readFromFile();
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

    /**
     * Serializes the ImageTagManager and stores that information on the
     * local hard disk before
     * the application closes
     *
     * @throws IOException if any errors occur during writing to file
     */
    @Override
    public void stop() throws IOException {
        System.out.println("Serializing files.");
        imgTagManager.saveToFile();

    }


}
