package fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.DirectoryManager;
import main.Image;
import main.ImageTagManager;
import main.PathExtractor;
import main.wrapper.DirectoryWrapper;
import main.wrapper.ImageWrapper;
import main.wrapper.ItemWrapper;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller for JavaFX GUI.
 */
public class Controller {

    // Use the UI element id specified in the FXML file as the variable name
    // to get a hook on those elements
    @FXML
    private Button chooseDirBtn;
    @FXML
    private Button moveFileBtn;
    @FXML
    private Button openCurDirBtn;
    @FXML
    private Label currentFolderLabel;
    @FXML
    private TreeView<ItemWrapper> imagesTreeView;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField addNewTagField;
    @FXML
    private Label imageNameLabel;
    @FXML
    private ListView<String> availableTagsView;
    @FXML
    private ListView nameHistoryView;
    @FXML
    private ListView<String> currentTagsView;
    @FXML
    private Button revertNameBtn;

    /*
     * The following three fields were used repeatedly in the button
     * EventHandlers, made sense to factor them out
     */
    private Image curSelectedImage;

    private DirectoryManager rootDirectoryManager = new DirectoryManager(null);
    /**
     * The stage this controller is associated with
     */
    private Stage stage;

    /**
     * Constructor.
     */
    public Controller() {
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * This method is automatically called after the FXML file is loaded
     * Used to bind UI elements to event listeners
     */
    @FXML
    public void initialize() {
        availableTagsView.getSelectionModel().setSelectionMode(SelectionMode
                .MULTIPLE);
        currentTagsView.getSelectionModel().setSelectionMode(SelectionMode
                .MULTIPLE);

        chooseDirBtn.setOnAction(event -> {
            File rootDirectory = chooseDirectory("Choose a directory to open");
            if (rootDirectory != null) {
                rootDirectoryManager.setRootFolder(new DirectoryWrapper
                        (rootDirectory));
                refreshGUIElements();
            }
        });

        openCurDirBtn.setOnAction(event -> {
            if (rootDirectoryManager.getRootFolder() != null) {
                rootDirectoryManager.openRootFolder();
            }
        });

        /* For displaying the image with a mouse click. */
        imagesTreeView.setOnMouseClicked(event -> {
            ObservableList<TreeItem<ItemWrapper>> selectedTreeItems =
                    imagesTreeView.getSelectionModel().getSelectedItems();
            if (selectedTreeItems.size() != 0) {
                ItemWrapper clickedObject = selectedTreeItems.get(0).getValue();
                if (clickedObject instanceof ImageWrapper) {
                    curSelectedImage = ((ImageWrapper) clickedObject)
                            .getImage();
                    updateSelectedImageGUI();
                }
            }
        });

        moveFileBtn.setOnAction(event -> {
            if (curSelectedImage != null) {
                try {
                    File newDirectoryFile = chooseDirectory("Move file to " +
                            "directory");
                    boolean sameDir = newDirectoryFile.toString().equals
                            (curSelectedImage.getCurDir());
                    if (!sameDir) {
                        // todo: select new image in TreeView afterwards?
                        curSelectedImage.move(newDirectoryFile.toString(),
                                curSelectedImage.getImageName(), false);
                        curSelectedImage = null;
                        refreshGUIElements();
                    }
                } catch (NullPointerException e) {
                    Popup.noDirSelectedPopup();
                    System.out.println("No valid directory was selected.");
                }
            }
        });

        revertNameBtn.setOnAction(event -> {
            if (curSelectedImage != null && nameHistoryView.getSelectionModel
                    ().getSelectedItems().get(0) != null) {
                String chosenName = (String) nameHistoryView
                        .getSelectionModel().getSelectedItems().get(0);
                chosenName = chosenName.substring(chosenName.indexOf("→") +
                        1).trim();
                curSelectedImage.revertName(chosenName);
                updateSelectedImageGUI();
            }
        });
    }

    /* Updates the GUI if any changes were made to the selected image,
    ** e.g. reverting the name, adding/deleting a tag, etc
     */
    private void updateSelectedImageGUI() {
        if (curSelectedImage != null) {
            // Update ImageView.
            String filePath = curSelectedImage.getPathString();
            imageView.setImage(new javafx.scene.image.Image
                    ("file:" + filePath));
            // Update TreeView.
            imagesTreeView.refresh();
            // Update label.
            imageNameLabel.setText(curSelectedImage.getPathString());
            updateNameHistory();
            updateCurrentTags();
            updateAvailableTags();
        } else {
            imageView.setImage(null);
        }
    }

    /**
     * Method that allows the user to add new tags by interacting with GUI
     * elements
     * Exposed to the FXML file through the @FXML annotation
     */
    @FXML
    public void addNewTag() {
        String newTagName = addNewTagField.getText();
        if (curSelectedImage != null && newTagName.length() > 0) {
            String invalidCharRegex = ".*[/\\\\].*";
            Pattern invalidCharPattern = Pattern.compile(invalidCharRegex);
            Matcher invalidCharMatcher = invalidCharPattern.matcher(newTagName);
            if (!invalidCharMatcher.matches()) {
                curSelectedImage.addTag(newTagName);
                addNewTagField.clear();
                updateSelectedImageGUI();
            } else {
                String invalidChars = "/, \\";
                String popupTitle = "Invalid Tag Name";
                String popupText = String.format("The tag name must not " +
                        "include the characters: %s", invalidChars);
                Popup.errorPopup(popupTitle, popupText);
            }
        }
    }

    /**
     * Allows the user to add a tag from the ListView of available tags by
     * interacting with a GUI element
     */
    @FXML
    public void addAvailableTag() {
        ObservableList<String> selectedAvailableTags = availableTagsView
                .getSelectionModel().getSelectedItems();
        if (curSelectedImage != null && selectedAvailableTags.size() != 0) {
            for (String tagName : selectedAvailableTags) {
                curSelectedImage.addTag(tagName);
            }
            updateSelectedImageGUI();
        }
    }

    /**
     * Allows user to delete a tag by interacting with a GUI element
     */
    @FXML
    public void deleteTag() {
        ObservableList<String> selectedCurrentTags = currentTagsView
                .getSelectionModel().getSelectedItems();
        if (curSelectedImage != null && selectedCurrentTags.size() != 0) {
            for (String tagName : selectedCurrentTags) {
                curSelectedImage.deleteTag(tagName);
            }
            updateSelectedImageGUI();
        }
    }

    /**
     * Display the OS's file selector so the user can select a directory
     *
     * @param title the title of the file selector window
     * @return the directory that was chosen by the user
     */
    @FXML
    private File chooseDirectory(String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        return directoryChooser.showDialog(stage);
    }

    /**
     * Refreshes all GUI elements when something is changed by the user
     */
    private void refreshGUIElements() {
        currentFolderLabel.setText(rootDirectoryManager.getRootFolder()
                .toString());
        populateImageList();
        updateAvailableTags();
        updateSelectedImageGUI();
    }

    /* Populates the TreeView with list of all images under current dir. */
    private void populateImageList() {
        TreeItem<ItemWrapper> rootFolderNode = new TreeItem<>(
                rootDirectoryManager.getRootFolder());
        ItemWrapper rootImagesList = rootDirectoryManager
                .getAllImagesUnderRoot();
        populateParentNode(rootFolderNode, rootImagesList);
        rootFolderNode.setExpanded(true);
        imagesTreeView.setRoot(rootFolderNode);
        imagesTreeView.refresh();
    }

    /**
     * Clears the current name history ListView and rebuilds it using the
     * selected
     * image's TagManager
     */
    private void updateNameHistory() {
        if (curSelectedImage != null) {
            ObservableList<String> nameHistoryList = FXCollections
                    .observableArrayList();
            nameHistoryList.setAll(curSelectedImage.getTagManager()
                    .getNameHistory());
            /* Don't know how to fix this yellow error. The suggested fix
            fort this is the same as the fix for casting from an unknown
            Serialized object (see ImageTagManager's yellow errors for more
            clarification).
             */
            nameHistoryView.setItems(nameHistoryList);
        } else {
            nameHistoryView.getItems().clear();
        }
    }

    /**
     * Populates the parentNode using ItemWrapper objects from the
     * parentNodeList
     *
     * @param parentNode     The UI element to be populated
     * @param parentNodeList ItemWrapper containing the data needed to
     *                       populate the parent
     */
    private void populateParentNode(TreeItem<ItemWrapper> parentNode,
                                    ItemWrapper parentNodeList) {
        if (parentNodeList instanceof DirectoryWrapper) {
            for (ItemWrapper wrappedItem : ((DirectoryWrapper)
                    parentNodeList).getChildObjects()) {
                if (wrappedItem instanceof DirectoryWrapper) {
                    String parentPath = wrappedItem.getPath().toString();
                    TreeItem<ItemWrapper> childNode = new TreeItem<>
                            (new DirectoryWrapper(new File(PathExtractor
                                    .getImageFileName(parentPath))));
                    populateParentNode(childNode, wrappedItem);
                    parentNode.getChildren().add(childNode);
                } else {
                    parentNode.getChildren().add(new TreeItem<>(wrappedItem));
                }
            }
        }
    }

    /**
     * Updates the ListView that displays all available tags in the chosen
     * root directory
     */
    private void updateAvailableTags() {
        ObservableList<String> availableTagsList = FXCollections
                .observableArrayList();
        availableTagsList.setAll(ImageTagManager.getInstance().getListOfTags());
        availableTagsView.setItems(availableTagsList);
    }

    /**
     * Updates the ListView that displays all current tags on the selected image
     */
    private void updateCurrentTags() {
        ObservableList<String> currentTagsList = FXCollections
                .observableArrayList();
        currentTagsList.setAll(curSelectedImage.getTagManager().getTagNames());
        currentTagsView.setItems(currentTagsList);
    }

}
