package fx;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.DirectoryManager;
import main.PathExtractor;
import main.Picture;
import main.wrapper.DirectoryWrapper;
import main.wrapper.ItemWrapper;
import main.wrapper.PictureWrapper;

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
     * The following three fields were used repeatedly in the button EventHandlers, made sense to factor them out
     */
    private Picture curSelectedPic;
    private ObservableList<TreeItem<ItemWrapper>> selectedTreeItems;
    private TreeItem<ItemWrapper> lastPicTreeItemSelected;

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
     */
    @FXML
    public void initialize() {

        chooseDirButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose a directory to open");
            File rootDirectory = directoryChooser.showDialog(stage);
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
            // todo: not allow empty tags to be added
            curSelectedPic.addTag(addNewTagField.getText());
            lastPicTreeItemSelected = new TreeItem<>(new PictureWrapper
                    (curSelectedPic));
            imagesTreeView.refresh();
        });

        // For displaying the picture with a mouse click
        mouseEvent = (javafx.scene.input.MouseEvent event) -> {
            selectedTreeItems = imagesTreeView.getSelectionModel()
                    .getSelectedItems();
            if (selectedTreeItems.size() != 0) {
                ItemWrapper clickedObject = selectedTreeItems.get(0).getValue();
                if (clickedObject instanceof PictureWrapper) {
                    String filePath = clickedObject.getPath().toString();
                    imageViewPort.setImage(new Image("file:" + filePath));
                    lastPicTreeItemSelected = selectedTreeItems.get(0);
                    curSelectedPic = ((PictureWrapper) clickedObject)
                            .getPicture();
                    imageNameLabel.setText(curSelectedPic.getImageName());
                }
            }
        };

        // todo: display no image when picture is moved?
        moveFileButton.setOnAction(event -> {
            if (curSelectedPic != null) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Move Selected Item");
                String newDirectory = chooser.showDialog(stage).toString();
                String imageFileName = PathExtractor.getImageFileName
                        (curSelectedPic.getPath().toString());
                String newPathOfImage = newDirectory + "/" + imageFileName;
                try {
                    Files.move(curSelectedPic.getPath(), Paths.get
                            (newPathOfImage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                refreshGUIElements();
                // todo: need to update the PictureWrapper's new location
                // in PictureManager, maybe delete the old object from the
                // HashMap first using the old path, and then reinsert
            }
        });
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

    // Populates parent node with all images under it.
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
//            } else if (o instanceof Picture) {
//                parentNode.getChildren().add(new TreeItem<>(o));
//            }
//        }
    }

}
