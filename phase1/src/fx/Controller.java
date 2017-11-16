package fx;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.DirectoryManager;
import main.Image;
import main.PathExtractor;
import main.wrapper.DirectoryWrapper;
import main.wrapper.ImageWrapper;
import main.wrapper.ItemWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Controller {

    // Use the UI element id specified in the FXML file as the variable name
    // to get a hook on those elements
    @FXML
    private Button chooseDirButton;
    @FXML
    private Button moveFileButton;
    @FXML
    private Button openCurDirButton;
    @FXML
    private Label currentFolderLabel;
    @FXML
    private TreeView<ItemWrapper> imagesTreeView;
    @FXML
    private ImageView imageViewPort;
    @FXML
    private Button addNewTagButton;
    @FXML
    private TextField addNewTagField;
    @FXML
    private Label imageNameLabel;

    /*
     * The following three fields were used repeatedly in the button
     * EventHandlers, made sense to factor them out
     */
    private Image curSelectedImage;
    private ObservableList<TreeItem<ItemWrapper>> selectedTreeItems;
    private TreeItem<ItemWrapper> lastImageTreeItemSelected;

    private EventHandler<javafx.scene.input.MouseEvent> mouseEvent;
    // private File rootFolder = new File("/home/kevin/Documents");
    private DirectoryManager rootDirectoryManager = new DirectoryManager(null);
    /**
     * The stage this controller is associated with
     */
    private Stage stage;

    public Controller() {
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * This method is automatically called after the FXML file is loaded
     * Used primarily to bind UI elements to event listeners
     */
    @FXML
    public void initialize() {

        chooseDirButton.setOnAction(event -> {
            File rootDirectory = chooseDirectory("Choose a directory to open");
            if (rootDirectory != null) {
                rootDirectoryManager.setRootFolder(new DirectoryWrapper
                        (rootDirectory));
                refreshGUIElements();
            }
        });

        openCurDirButton.setOnAction(event -> {
            if (rootDirectoryManager.getRootFolder() != null) {
                rootDirectoryManager.openRootFolder();
            }
        });

        addNewTagButton.setOnAction(event -> {
            addNewTag();
        });

        // For displaying the image with a mouse click
        mouseEvent = (javafx.scene.input.MouseEvent event) -> {
            selectedTreeItems = imagesTreeView.getSelectionModel()
                    .getSelectedItems();
            if (selectedTreeItems.size() != 0) {
                ItemWrapper clickedObject = selectedTreeItems.get(0).getValue();
                if (clickedObject instanceof ImageWrapper) {
                    updateSelectedImage(((ImageWrapper) clickedObject)
                            .getImage());
                }
            }
        };

        // todo: display no image when picture is moved?
        moveFileButton.setOnAction(event -> {
            if (curSelectedImage != null) {
                String newDirectory = chooseDirectory("Move file to " +
                        "directory").toString();
                String imageFileName = PathExtractor.getImageFileName
                        (curSelectedImage.getPath().toString());
                String newPathOfImage = newDirectory + "/" + imageFileName;
                try {
                    Files.move(curSelectedImage.getPath(), Paths.get
                            (newPathOfImage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // todo: regenerating entire tree is a bit overkill here,
                // should only deal with that specific TreeItem?
                refreshGUIElements();
                // todo: need to update the ImageWrapper's new location
                // in PictureManager, maybe delete the old object from the
                // HashMap first using the old path, and then reinsert
            }
        });
    }

    @FXML
    public void addNewTag() {
        // todo: not allow empty tags to be added
        curSelectedImage.addTag(addNewTagField.getText());
        lastImageTreeItemSelected = new TreeItem<>(new ImageWrapper
                (curSelectedImage));
        imagesTreeView.refresh();
        imageNameLabel.setText(curSelectedImage.getImageName());
    }

    @FXML
    private File chooseDirectory(String title) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        return directoryChooser.showDialog(stage);
    }

    private void updateSelectedImage(Image clickedImage) {
        String filePath = clickedImage.getPath().toString();
        imageViewPort.setImage(new javafx.scene.image.Image
                ("file:" + filePath));
        lastImageTreeItemSelected = selectedTreeItems.get(0);
        curSelectedImage = clickedImage;
        imageNameLabel.setText(clickedImage.getImageName());
    }

    // Updates all the needed elements when a new directory is selected.
    private void refreshGUIElements() {
        currentFolderLabel.setText(rootDirectoryManager.getRootFolder()
                .toString());
        populateImageList();
    }

    // Populates the TreeView with list of all images under current dir.
    private void populateImageList() {
        TreeItem<ItemWrapper> rootFolderNode = new TreeItem<>(
                rootDirectoryManager.getRootFolder());
        ItemWrapper rootImagesList = rootDirectoryManager
                .getAllImagesUnderRoot();
        populateParentNode(rootFolderNode, rootImagesList);
        rootFolderNode.setExpanded(true);
        imagesTreeView.setRoot(rootFolderNode);
        imagesTreeView.refresh();
        imagesTreeView.addEventHandler(javafx.scene.input.MouseEvent
                .MOUSE_CLICKED, mouseEvent);
    }

    /**
     * Populates the parentNode using ItemWrapper objects from the parentNodeList
     * @param parentNode The UI element to be populated
     * @param parentNodeList ItemWrapper containing the data needed to populate the parent
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
//        for (Object o : imagesList) {
//            if (o instanceof List) {
//                String parentPath = (String) ((List) o).get(0);
//                TreeItem<Object> childNode = new TreeItem<>(PathExtractor
//                        .getImageFileName(parentPath));
//                populateParentNode(childNode, (List) o);
//                parentNode.getChildren().add(childNode);
//            } else if (o instanceof Image) {
//                parentNode.getChildren().add(new TreeItem<>(o));
//            }
//        }
    }

}
