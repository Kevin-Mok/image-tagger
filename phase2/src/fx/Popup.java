package fx;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.List;
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

    /**
     * Shows a popup asking the user to confirm the deletion of a list of tags
     *
     * @param tagNames the list of tags to be deleted
     * @return true if the user clicks the OK button, false if otherwise
     */
    static boolean confirmDeleteAll(List<String> tagNames) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setPrefHeight(200.0);
        alert.getDialogPane().setPrefWidth(320.0);
        alert.setResizable(true);
        alert.setTitle("Delete All?");
        StringBuilder sb = new StringBuilder("Are you sure you want to delete" +
                " ");
        String firstTag = tagNames.get(0);
        sb.append(firstTag.substring(firstTag.indexOf('-') + 2));
        for (int index = 1; index < tagNames.size(); index++) {
            String tag = tagNames.get(index);
            tag = tag.substring(tag.indexOf('-') + 2);
            sb.append(", ").append(tag);
        }
        sb.append(" from all images?");
        alert.setContentText(sb.toString());
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Shows a popup asking the user to confirm the upload of the currently
     * selected image
     *
     * @return true if the user clicks the OK button, false if otherwise
     */
    static boolean confirmUpload() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setPrefHeight(200.0);
        alert.getDialogPane().setPrefWidth(320.0);
        alert.setResizable(true);
        alert.setTitle("Upload?");
        alert.setContentText("Upload the selected image to Imgur?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
