package FX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;

public class Controller {

    // Use the UI element id specified in the FXML file as the variable name to get a hook on those elements
    @FXML
    Button chooseDirButton;

    @FXML
    Button moveFileButton;

    @FXML
    Button openCurDirButton;

    public Controller() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
