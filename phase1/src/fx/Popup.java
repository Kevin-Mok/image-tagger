package fx;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Class responsible for the display of error information via pop-ups
 */
public class Popup {
    /**
     * Displays error information in a pop-up window
     * @param title the title of the window
     * @param text the text to display
     */
    public static void errorPopup(String title, String text) {
        Platform.runLater(() -> {
            Alert noDirSelectedAlert = new Alert(Alert.AlertType.ERROR);
            noDirSelectedAlert.setTitle(title);
            noDirSelectedAlert.setHeaderText(null);
            noDirSelectedAlert.setContentText(text);
            noDirSelectedAlert.showAndWait();
        });
    }

    /**
     * Displays an error pop-up when no directory is selected
     */
    public static void noDirSelectedPopup() {
        String popupTitle = "No Directory Selected";
        String popupText = "Please select a valid directory and try again.";
        errorPopup(popupTitle, popupText);
    }
}
