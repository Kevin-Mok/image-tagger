package fx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.DirectoryManager;

import java.io.File;
import java.util.List;

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
    private TreeView<String> imagesTreeView;

    private File rootFolder = new File("/home/kevin/Pictures");
    //null placeholder for now, replace the second argument with the actual
    // image format config file later
    private DirectoryManager rootDirectoryManager = new DirectoryManager
            (rootFolder);
    /**
     * The stage this controller is associated with
     */
    private Stage stage;

    public Controller() {
    }

    /**
     * This method is automatically called after the FXML file is loaded
     */
    @FXML
    public void initialize() {
        refreshGUIElements();

        chooseDirButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose a directory to open");
            File rootDirectory = directoryChooser.showDialog(stage);
            if (rootDirectory != null) {
                rootDirectoryManager.setRootFolder(rootDirectory);
                refreshGUIElements();
            }
        });
        openCurDirButton.setOnAction(event -> {
            if (rootDirectoryManager.getRootFolder() != null) {
                rootDirectoryManager.openRootFolder();
            }
        });
    }

    private void refreshGUIElements() {
        currentFolderLabel.setText(rootDirectoryManager.getRootFolder()
                .toString());
        populateImageList();
    }

    private void populateImageList() {
        TreeItem<String> rootFolderNode = new TreeItem<>(rootFolder.toString());
        List rootImagesList = rootDirectoryManager.getImages
                (rootDirectoryManager.getRootFolder().toPath(), true);
        populateParentNode(rootFolderNode, rootImagesList);
        rootFolderNode.setExpanded(true);
        imagesTreeView.setRoot(rootFolderNode);
        imagesTreeView.refresh();
    }

    private void populateParentNode(TreeItem<String> parentNode, List
            imagesList) {
        for (Object o : imagesList) {
            if (o instanceof String) {
                String imageName = getImageName((String) o);
                parentNode.getChildren().add(new TreeItem<>(imageName));
            } else if (o instanceof List) {
                String firstImagePath = (String) ((List) o).get(0);
                TreeItem<String> childNode = new TreeItem<>(getSubdirectoryName
                        (firstImagePath));
                populateParentNode(childNode, (List) o);
                parentNode.getChildren().add(childNode);
            }
        }
    }

    private String getSubdirectoryName(String imagePath) {
        int indexOfLastSlash = imagePath.lastIndexOf('/');
        int indexOfSecondLastSlash = imagePath.lastIndexOf('/',
                indexOfLastSlash - 1);
        return imagePath.substring(indexOfSecondLastSlash + 1,
                indexOfLastSlash);
    }

    private String getImageName(String imagePath) {
        return imagePath.substring(imagePath.lastIndexOf('/') + 1);
    }

/*    private void populateParentNode(TreeItem<String> parentNode,
                                   DirectoryManager
            dir) {
        List dirImagesList = dir.getImages(dir.getRootFolder().toPath(),true);
        for(Object o : dirImagesList) {
            if(o instanceof String) {
                parentNode.getChildren().add(new TreeItem<>((String) o));
            } else if (o instanceof List){
                TreeItem<String> childNode = new TreeItem<>();
                String subdirPathString = (String) ((List) o).get(0);
                DirectoryManager subdir = new DirectoryManager(new File
                (subdirPathString));
                populateParentNode(childNode, subdir);
                parentNode.getChildren().add(childNode);
            }
        }
    }*/

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
