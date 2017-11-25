package fx;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Class responsible for the display of error information via pop-ups. Unused
 * now in Phase 1 since could not get to compile through command line.
 */
public class Popup {
    /**
     * Displays error information in a pop-up window
     *
     * @param title the title of the window
     * @param text  the text to display
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
    static void noDirSelectedPopup() {
        String popupTitle = "No Directory Selected";
        String popupText = "Please select a valid directory and try again.";
        errorPopup(popupTitle, popupText);
    }

    /**
     * Displays a popup that allows the user to enter a new tag to be added
     *
     * @return the tag name that the user entered
     */
    static String addTagPopup() {
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter a new tag");
        dialog.setTitle("Add Tag");
        dialog.setContentText(String.format("The tag name must not include " +
                        "the characters: %s", "/, \\, -"));
        String tagName = "";
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                tagName = result.get();
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }
        return tagName;
    }
}
