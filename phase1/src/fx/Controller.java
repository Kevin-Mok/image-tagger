package fx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.DirectoryManager;

import java.io.File;

public class Controller {

    // Use the UI element id specified in the FXML file as the variable name to get a hook on those elements
    @FXML
    private Button chooseDirButton;

    @FXML
    private Button moveFileButton;

    @FXML
    private Button openCurDirButton;

    //null placeholder for now, replace the second argument with the actual image format config file later
    private DirectoryManager directoryManager = new DirectoryManager(null, null);
    /**
     * The stage this controller is associated with
     */
    private Stage stage;

    public Controller() {}

    /**
     * This method is automatically called after the FXML file is loaded
     */
    @FXML
    public void initialize() {
        chooseDirButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose a directory to open");
            File rootDirectory = directoryChooser.showDialog(stage);
            if (rootDirectory != null) {
                directoryManager.setRootFolder(rootDirectory);
            }
            //TODO: println for debugging, delete later on
            System.out.println(directoryManager.getRootFolder());
        });
        openCurDirButton.setOnAction(event -> {
            if (directoryManager.getRootFolder() != null) {
                directoryManager.openRootFolder();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
