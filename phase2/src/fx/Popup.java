package fx;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.util.Arrays;
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

    /**
     * Shows a pop-up asking the user to choose between adding a tag just to the global pool
     * or adding the tag to the global tool and adding it to every image in the current root directory
     * @return the action to be done
     */
    static String addToAvailableTags() {
        String defaultChoice = "add to available tags, but not to any images";
        List<String> choices
                = Arrays.asList(defaultChoice, "add to all images under root");
        return createChoiceDialog(defaultChoice, choices);
    }

    /**
     * Shows a pop-up asking the user to choose between deleting a tag just from the global pool,
     * or deleting that tag from the global pool and from all images in the current root directory
     * @return the action to be done
     */
    static String deleteFromAvailableTags() {
        String defaultChoice = "delete from available tags, but not from any images";
        List<String> choices
                = Arrays.asList(defaultChoice, "delete from all images under root");
        return createChoiceDialog(defaultChoice, choices);
    }

    /**
     * Helper method for creating a choice dialog
     * @param defaultChoice the default choice for the dialog
     * @param choices the list of choices for the dialog
     * @return the user's choice
     */
    private static String createChoiceDialog(String defaultChoice, List<String> choices) {
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(defaultChoice, choices);
        choiceDialog.getDialogPane().setPrefHeight(300);
        choiceDialog.getDialogPane().setPrefWidth(480);
        choiceDialog.setResizable(true);
        Optional<String> result = choiceDialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }
        return choiceDialog.getDefaultChoice();
    }
}
