package fx;

import fx.imgur.ImageResponse;
import fx.imgur.ImgurAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.*;
import main.wrapper.DirectoryWrapper;
import main.wrapper.ImageWrapper;
import main.wrapper.ItemWrapper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private Label imageNameLabel;
    @FXML
    private ListView<String> availableTagsView;
    @FXML
    private ListView nameHistoryView;
    @FXML
    private ListView<String> currentTagsView;
    @FXML
    private Button revertNameBtn;
    @FXML
    private Button deleteTagBtn;
    @FXML
    private Button deleteAllBtn;
    @FXML
    private Button uploadBtn;
    @FXML
    private Label uploadLabel;

    private DirectoryManager rootDirectoryManager = new DirectoryManager(null);
    private Stage stage;
    private Image lastSelectedImage;
    private List<Image> curSelectedImages;
    private Service service = new ImgurService();

    private ObservableList<String> availableTagsList = FXCollections
            .observableArrayList();
    private ObservableList<String> currentTagsList = FXCollections
            .observableArrayList();
    private ObservableList<String> nameHistoryList = FXCollections
            .observableArrayList();
    private ObservableList<String> selectedAvailableTags;

    /**
     * Constructor.
     */
    public Controller() {
    }

    /**
     * Adapted from Johnny850807's GitHub repository
     * https://github.com/Johnny850807/Imgur-Picture-Uploading-Example-Using
     * -Retrofit-On-Native-Java
     * on Nov 24th, 2017
     */
    private static ImgurAPI createImgurAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ImgurAPI.SERVER)
                .build();
        return retrofit.create(ImgurAPI.class);
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

/*    private <T extends SelectionModel> void setMultipleSelection(T view) {
        view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }*/

    /**
     * This method is automatically called after the FXML file is loaded
     * Used to bind UI elements to event listeners
     */
    @FXML
    public void initialize() {
        /* Set ListView's for multiple selection. */
        // todo: refactor into setMultipleSelection method
        availableTagsView.getSelectionModel().setSelectionMode(SelectionMode
                .MULTIPLE);
        currentTagsView.getSelectionModel().setSelectionMode(SelectionMode
                .MULTIPLE);
        imagesTreeView.getSelectionModel().setSelectionMode(SelectionMode
                .MULTIPLE);
        /* Only visible when an Image is in the process of being uploaded to
        Imgur. */
        uploadLabel.setVisible(false);

        updateAvailableTags();
        availableTagsView.setItems(availableTagsList);
        currentTagsView.setItems(currentTagsList);
        /* Don't know how to fix this yellow error. The suggested fix
        fort this is the same as the fix for casting from an unknown
        Serialized object (see ImageTagManager's yellow errors for more
        clarification).
         */
        nameHistoryView.setItems(nameHistoryList);

        chooseDirBtn.setOnAction(event -> {
            File rootDirectory = chooseDirectory("Choose a directory to open");
            if (rootDirectory != null) {
                rootDirectoryManager.setRootFolder(new DirectoryWrapper
                        (rootDirectory));
                refreshGUIElements();
            }
        });

        deleteTagBtn.setOnAction(event -> deleteTag(currentTagsView
                .getSelectionModel().getSelectedItems()));

        deleteAllBtn.setOnMouseClicked(event -> {
            curSelectedImages = rootDirectoryManager.getAllImagesUnderRoot();
            if (Popup.confirmDeleteAll(selectedAvailableTags)) {
                deleteTag(selectedAvailableTags);
            }
        });

        uploadBtn.setOnAction(event -> {
            /*
             * Adapted from ItachiUchiha's post on StackOverflow
             * https://stackoverflow
             * .com/questions/31607656/how-to-show-and-then-hide-a-label-in
             * -javafx-after-a-task-is-completed
             * retrieved Nov 25, 2017
             */
            /* Only allow the user to upload one picture at a time */
            if (imagesTreeView.getSelectionModel().getSelectedItems().size() == 1) {
                if (Popup.confirmUpload()) {
                    uploadLabel.setVisible(true);
                    if (!service.isRunning()) {
                    /* Service will take care of uploading the image */
                        service.start();
                    }

                    service.setOnSucceeded(event1 -> {
                        uploadLabel.setVisible(false);
                        service.reset();
                    });
                }
            }
        });

        openCurDirBtn.setOnAction(event -> {
            if (rootDirectoryManager.getRootFolder() != null) {
                rootDirectoryManager.openRootFolder();
            }
        });

        /* For displaying the image with a mouse click. */
        imagesTreeView.setOnMouseClicked(event -> {
            updateLastSelectedImage();
            updateSelectedImageGUI();
            updateCurSelectedImages();
        });

        moveFileBtn.setOnAction(event -> {
            if (curSelectedImages != null) {
                try {
                    File newDirectoryFile = chooseDirectory("Move file to " +
                            "directory");
                    boolean sameDir = newDirectoryFile.toString().equals
                            (lastSelectedImage.getCurDir());
                    if (!sameDir) {
                        lastSelectedImage.move(newDirectoryFile.toString(),
                                lastSelectedImage.getImageName(), false);
                        curSelectedImages = null;
                        refreshGUIElements();
                    }
                } catch (NullPointerException e) {
                    Popup.noDirSelectedPopup();
                    System.out.println("No valid directory was selected.");
                }
            }
        });

        revertNameBtn.setOnAction(event -> {
            if (curSelectedImages != null && nameHistoryView.getSelectionModel
                    ().getSelectedItems().get(0) != null) {
                String chosenName = (String) nameHistoryView
                        .getSelectionModel().getSelectedItems().get(0);
                chosenName = chosenName.substring(chosenName.indexOf("→") +
                        1).trim();
                lastSelectedImage.revertName(chosenName);
                updateSelectedImageGUI();
            }
        });
    }

    private void updateLastSelectedImage() {
        TreeItem<ItemWrapper> lastSelectedTreeItem = imagesTreeView
                .getSelectionModel().getSelectedItem();
        if (lastSelectedTreeItem != null) {
            ItemWrapper lastSelectedItemWrapper = lastSelectedTreeItem
                    .getValue();
            if (lastSelectedItemWrapper instanceof ImageWrapper) {
                lastSelectedImage = ((ImageWrapper) lastSelectedItemWrapper)
                        .getImage();
                currentTagsList.setAll(lastSelectedImage.getTagManager()
                        .getTagNames());
            }
        }
    }

    private void updateCurSelectedImages() {
        ObservableList<TreeItem<ItemWrapper>> selectedTreeItems =
                imagesTreeView.getSelectionModel().getSelectedItems();
        if (selectedTreeItems.size() != 0) {
            ArrayList<Image> curSelectedImages = new ArrayList<>();
            if (selectedTreeItems.size() == 1) {
                ItemWrapper firstSelectedItem;
                        /* Following try-catch block to address strange JavaFX
                        behavior described in #21. */
                try {
                    firstSelectedItem = selectedTreeItems.get(0).getValue();
                } catch (NullPointerException e) {
                    firstSelectedItem = selectedTreeItems.get(0).getValue();
                }
                if (firstSelectedItem instanceof ImageWrapper) {
                    Image firstSelectedImage = ((ImageWrapper)
                            firstSelectedItem).getImage();
                    curSelectedImages.add(firstSelectedImage);
                    lastSelectedImage = firstSelectedImage;
                }
            } else {
                for (TreeItem<ItemWrapper> items : selectedTreeItems) {
                    if (items.getValue() instanceof ImageWrapper) {
                        curSelectedImages.add(((ImageWrapper) items
                                .getValue()).getImage());
                    }
                }
            }
            this.curSelectedImages = curSelectedImages;
        }
    }

    /* Updates the GUI if any changes were made to the selected image,
    ** e.g. reverting the name, adding/deleting a tag, etc
     */
    private void updateSelectedImageGUI() {
        if (lastSelectedImage != null) {
            // Update ImageView.
            String filePath = lastSelectedImage.getPathString();
            imageView.setImage(new javafx.scene.image.Image
                    ("file:" + filePath));
            // Update TreeView.
            imagesTreeView.refresh();
            // Update label.
            imageNameLabel.setText(lastSelectedImage.getPathString());
            updateNameHistory();
            updateAvailableTags();
        } else {
            imageView.setImage(null);
        }
    }

    private String extractAvailableTagName(String tagName) {
        return tagName.substring(tagName.indexOf("-") + 1).trim();
    }

    /**
     * Method that allows the user to add new tags by interacting with GUI
     * elements
     * Exposed to the FXML file through the @FXML annotation
     */
    @FXML
    public void addNewTag() {
        String newTagName = Popup.addTagPopup();
        if (curSelectedImages != null && newTagName.length() > 0) {
            String invalidCharRegex = ".*[/\\\\-].*";
            Pattern invalidCharPattern = Pattern.compile(invalidCharRegex);
            Matcher invalidCharMatcher = invalidCharPattern.matcher(newTagName);
            if (!invalidCharMatcher.matches()) {
                for (Image img : curSelectedImages) {
                    img.addTag(newTagName);
                }
                updateSelectedImageGUI();

            } else {
                String invalidChars = "/ \\ -";
                String popupTitle = "Invalid Tag Name";
                String popupText = String.format("The tag name must not " +
                        "include the characters: %s", invalidChars);
                Popup.errorPopup(popupTitle, popupText);
            }
        }
    }

    @FXML
    public void updateSelectedAvailableTags() {
        selectedAvailableTags = availableTagsView.getSelectionModel()
                .getSelectedItems();
    }

    /**
     * Allows the user to add a tag from the ListView of available tags by
     * interacting with a GUI element
     */
    @FXML
    public void addAvailableTag() {
        if (curSelectedImages != null && selectedAvailableTags.size() != 0) {
            for (String tagName : selectedAvailableTags) {
                for (Image img : curSelectedImages) {
                    img.addTag(extractAvailableTagName(tagName));
                }
            }
            updateSelectedImageGUI();
        }
    }

    @FXML
    private void deleteTag(ObservableList<String> tagNamesToDelete) {
        if (curSelectedImages != null && tagNamesToDelete.size() != 0) {
            for (String tagName : tagNamesToDelete) {
                int indexOfDash = tagName.indexOf('-');
                if (indexOfDash != -1) {
                    tagName = tagName.substring(indexOfDash + 2);
                }
                for (Image img : curSelectedImages) {
                    img.deleteTag(tagName);
                }
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

    @FXML
    private void viewRenameLog() {
        File renameLogFile = new File(LogUtility.RENAME_LOGGER_NAME + ".txt");
        try {
            Runtime.getRuntime().exec(String.format("gvim %s", renameLogFile
                    .getAbsoluteFile().toString()));
        } catch (IOException e) {
            String popupText = String.format("Unable to open %s.",
                    renameLogFile.toString());
            Popup.errorPopup("Unable to Open File", popupText);
        }
    }

    /**
     * Refreshes all GUI elements when something is changed by the user
     */
    private void refreshGUIElements() {
        currentFolderLabel.setText(rootDirectoryManager.getRootFolder()
                .toString());
        /* Pass in an empty list when just refreshing (no filtering) */
        populateImageList(new ArrayList<>(), false);
        updateAvailableTags();
        updateSelectedImageGUI();
    }

    /**
     * Filters the images displayed in imagesTreeView based on the tags the
     * user has
     * selected from the availableTagsView
     */
    @FXML
    public void filterImagesByTags() {
        if (selectedAvailableTags.size() != 0) {
            List<String> tagNames = new ArrayList<>();
            tagNames.addAll(selectedAvailableTags);
            for (int i = 0; i < tagNames.size(); i++) {
                tagNames.set(i, extractAvailableTagName(tagNames.get(i)));
            }
            populateImageList(tagNames, true);
        }
    }

    /**
     * Shows all images without any tag filtering
     */
    @FXML
    public void showAllImages() {
        populateImageList(new ArrayList<>(), false);
    }

    /**
     * Populates the TreeView with list of all images under current dir.
     *
     * @param tagNames list of tag names to filter images by
     */
    private void populateImageList(List<String> tagNames, boolean
            expandDirectories) {
        TreeItem<ItemWrapper> rootFolderNode = new TreeItem<>(
                rootDirectoryManager.getRootFolder());
        ItemWrapper rootImagesList = rootDirectoryManager
                .getRootDirectory();
        populateParentNode(rootFolderNode, rootImagesList, tagNames,
                expandDirectories);
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
        if (lastSelectedImage != null) {
            nameHistoryList.setAll(lastSelectedImage.getTagManager()
                    .getNameHistory());
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
     * @param tags           List of tags to filter images by, images
     *                       containing any tag in
     *                       the list will be added to the parentNode
     */
    private void populateParentNode(TreeItem<ItemWrapper> parentNode,
                                    ItemWrapper parentNodeList, List<String>
                                            tags, boolean expandDirectories) {
        if (parentNodeList instanceof DirectoryWrapper) {
            for (ItemWrapper wrappedItem : ((DirectoryWrapper)
                    parentNodeList).getChildObjects()) {
                /* If the wrappedItem is a directory, recurse */
                if (wrappedItem instanceof DirectoryWrapper) {
                    String parentPath = wrappedItem.getPath().toString();
                    TreeItem<ItemWrapper> childNode = new TreeItem<>
                            (new DirectoryWrapper(new File(PathExtractor
                                    .getImageFileName(parentPath))));
                    populateParentNode(childNode, wrappedItem, tags,
                            expandDirectories);
                    if (!childNode.isLeaf()) {
                        parentNode.getChildren().add(childNode);
                        childNode.setExpanded(expandDirectories);
                    }
                /* If the wrapped item is an image */
                } else {
                    if (((ImageWrapper) wrappedItem).getImage().hasTags(tags)) {
                        parentNode.getChildren().add(new TreeItem<>
                                (wrappedItem));
                    }
                }
            }
        }
    }

    /**
     * Updates the ListView that displays all available tags in the chosen
     * root directory
     */
    private void updateAvailableTags() {
        availableTagsList.setAll(ImageTagManager.getInstance()
                .getAvailableTagsWithCount());
    }

    /**
     * Inner class that takes care of uploading an image to Imgur
     * <p>
     * Adapted from ItachiUchiha's post on StackOverflow
     * https://stackoverflow.com/questions/31607656/how-to-show-and-then-hide
     * -a-label-in-javafx-after-a-task-is-completed
     * retrieved Nov 25, 2017
     */
    class ImgurService extends Service<Void> {
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    /*
                     * Adapted from Johnny850807's GitHub repository
                     * https://github
                     * .com/Johnny850807/Imgur-Picture-Uploading-Example
                     * -Using-Retrofit-On-Native-Java
                     * on Nov 24th, 2017
                     */
                    final ImgurAPI imgurApi = createImgurAPI();
                    try {
                        File image = new File(lastSelectedImage.getPath()
                                .toString());
                        RequestBody request = RequestBody.create(MediaType.parse
                                ("image/*"), image);
                        Call<ImageResponse> call = imgurApi.postImage(request);
                        Response<ImageResponse> res = call.execute();

                        System.out.println("Successful? " + res.isSuccessful());
                        String url = res.body().data.link;

                        Runtime runtime = Runtime.getRuntime();
                        try {
                            runtime.exec("firefox " + url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                    return null;
                }
            };
        }
    }
}
