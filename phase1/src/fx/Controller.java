package fx;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.*;

import java.io.File;

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
                rootDirectoryManager.setRootFolder(new DirectoryWrapper(rootDirectory));
                refreshGUIElements();
            }
        });
        openCurDirButton.setOnAction(event -> {
            if (rootDirectoryManager.getRootFolder() != null) {
                rootDirectoryManager.openRootFolder();
            }
        });

        //For displaying the picture with a mouse click
        mouseEvent = (javafx.scene.input.MouseEvent event) -> {
            ItemWrapper clickedObject = imagesTreeView.getSelectionModel()
                    .getSelectedItems().get(0).getValue();
            System.out.println(clickedObject.getClass());
            if (clickedObject instanceof PictureWrapper) {
                String filePath = ((PictureWrapper) clickedObject).getPath().toString();
                imageViewPort.setImage(new Image("file:" + filePath));
            }
        };
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
        ItemWrapper rootImagesList = rootDirectoryManager.getAllImagesUnderRoot();
        populateParentNode(rootFolderNode, rootImagesList);
        rootFolderNode.setExpanded(true);
        imagesTreeView.setRoot(rootFolderNode);
        imagesTreeView.refresh();
        imagesTreeView.addEventHandler(javafx.scene.input.MouseEvent
                .MOUSE_CLICKED, mouseEvent);
    }

    // Populates parent node with all images under it.
    private void populateParentNode(TreeItem<ItemWrapper> parentNode, ItemWrapper parentNodeList) {
        if (parentNodeList.isDirectory()) {
            for (ItemWrapper wrappedItem : ((DirectoryWrapper)parentNodeList).getChildObjects()) {
                if (wrappedItem.isDirectory()) {
                    String parentPath = ((DirectoryWrapper) wrappedItem).getPath().toString();
                    TreeItem<ItemWrapper> childNode = new TreeItem<>
                            (new DirectoryWrapper(new File(PathExtractor.getImageName(parentPath))));
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
//                        .getImageName(parentPath));
//                populateParentNode(childNode, (List) o);
//                parentNode.getChildren().add(childNode);
//            } else if (o instanceof Picture) {
//                parentNode.getChildren().add(new TreeItem<>(o));
//            }
//        }
    }
}