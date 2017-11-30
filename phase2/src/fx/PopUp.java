package fx;

import com.sun.org.apache.bcel.internal.generic.POP;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class responsible for the display of error information via pop-ups.
 */
public class PopUp {

    /**
     * The message to display for adding tags only to the available tags pool, but not to any images
     */
    private static final String ADD_AVAIL_ONLY_MSG = "add to available tags, but not to any images";
    /**
     * The message to display for adding tags to all images under the root directory
     */
    public static final String ADD_TO_ALL_IMG_MSG = "add to all images under root";
    /**
     * The message to display for deleting a tag only from the available tags pool, but not from any images
     */
    private static final String DEL_AVAIL_ONLY_MSG = "delete from available tags, but not from any images";
    /**
     * The message to display for deleting tags from all images under the root directory
     */
    public static final String DEL_FROM_ALL_IMG_MSG = "delete from all images under root";

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
        String invalidCharRegex = ".*[/\\\\-].*";
        Pattern invalidCharPattern = Pattern.compile(invalidCharRegex);
        String tagName = "";
        boolean invalidInput;
        do {
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                tagName = result.get();
                Matcher invalidCharMatcher = invalidCharPattern.matcher
                        (tagName);
                if (invalidCharMatcher.matches()) {
                    dialog.setContentText(String.format("The tag name must " +
                            "not include " +
                            "the characters: %s", "/, \\, -"));
                    invalidInput = true;
                    tagName = null;
                } else {
                    invalidInput = false;
                }
            } else {
                invalidInput = false;
            }
        } while (invalidInput);
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
     * Shows a pop-up asking the user to choose between adding a tag just to
     * the global pool, or adding the tag to the global tool and adding it to every image in
     * the current root directory
     *
     * @return the action to be done
     */
    static String addToAvailableTags() {
        String defaultChoice = PopUp.ADD_AVAIL_ONLY_MSG;
        List<String> choices
                = Arrays.asList(defaultChoice, PopUp.ADD_TO_ALL_IMG_MSG);
        return createChoiceDialog(defaultChoice, choices);
    }

    /**
     * Shows a pop-up asking the user to choose between deleting a tag just
     * from the global pool, or deleting that tag from the global pool and from all images in the
     * current root directory
     *
     * @return the action to be done
     */
    static String deleteFromAvailableTags() {
        String defaultChoice = PopUp.DEL_AVAIL_ONLY_MSG;
        List<String> choices
                = Arrays.asList(defaultChoice, PopUp.DEL_FROM_ALL_IMG_MSG);
        return createChoiceDialog(defaultChoice, choices);
    }

    /**
     * Helper method for creating a choice dialog
     *
     * @param defaultChoice the default choice for the dialog
     * @param choices       the list of choices for the dialog
     * @return the user's choice
     */
    private static String createChoiceDialog(String defaultChoice,
                                             List<String> choices) {
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(defaultChoice,
                choices);
        choiceDialog.setHeaderText("Select Action");
        choiceDialog.getDialogPane().setPrefHeight(150);
        choiceDialog.getDialogPane().setPrefWidth(480);
        choiceDialog.setResizable(true);
        Optional<String> result = choiceDialog.showAndWait();
        return result.orElseGet(choiceDialog::getDefaultChoice);
    }
}
