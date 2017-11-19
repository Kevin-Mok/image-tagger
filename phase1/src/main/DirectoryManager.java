package main;

import fx.Popup;
import main.wrapper.DirectoryWrapper;
import main.wrapper.ImageWrapper;
import main.wrapper.ItemWrapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectoryManager {
    /**
     * The top level directory for the current instance of the Image Viewer
     * program
     */
    private DirectoryWrapper rootFolder;
    /**
     * Path to text configuration file that contains the image formats to list
     */
    private ArrayList<String> imageFormats;


    public DirectoryManager(File rootFolder) {
        this.rootFolder = new DirectoryWrapper(rootFolder);
        this.imageFormats = new ArrayList<>();
        // temporarily adding in desired formats
        imageFormats.add("jpg");
        imageFormats.add("png");
        imageFormats.add("jpeg");
    }

    // main() for testing purposes only
    public static void main(String[] args) {
        // File rootFolder = new File("/h/u7/c7/05/shyichin");
        // File rootFolder = new File("/home/kevin/Documents");
        File rootFolder = new File("/h/u3/c7/05/mokkar/Downloads");
        main.DirectoryManager m = new main.DirectoryManager(rootFolder);
        // manager.openRootFolder();
    }

    public DirectoryWrapper getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(DirectoryWrapper rootFolder) {
        this.rootFolder = rootFolder;
    }

    public ArrayList<String> getImageFormats() {
        return imageFormats;
    }

    public void setImageFormats(ArrayList<String> imageFormats) {
        this.imageFormats = imageFormats;
    }

    /**
     * Returns ItemWrapper representing all images under the root folder,
     * including those in subdirectories
     *
     * @return List of image paths under the root directory, including those
     * in subdirectories
     */
    public ItemWrapper getAllImagesUnderRoot() {
        return getImages(rootFolder.getPath(), true);
    }

    /**
     * Returns a ItemWrapper representing a directory with the images inside and
     * subdirectories
     *
     * @param directory the directory to search in
     * @param recursive whether or not to search recursively in the subfolders
     * @return ItemWrapper representing the subdirectories and pictures in a
     * directory
     */
    private ItemWrapper getImages(Path directory, boolean recursive) {
        DirectoryWrapper images = new DirectoryWrapper(directory.toFile());
        Pattern imgFilePattern = Pattern.compile(generateImageMatchingPattern
                ());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream
                (directory)) {
            for (Path file : stream) {
                if (recursive) {
                    if (Files.isDirectory(file)) {
                        ItemWrapper subImages = getImages(file, true);
                        if (((DirectoryWrapper) subImages).getChildObjects()
                                .size() != 0) {
                            images.addToDirectory(subImages);
                        }
                    }
                }
                Matcher matcher = imgFilePattern.matcher(file.toString());
                if (matcher.matches()) {
                    if (ImageTagManager.getInstance().containsImagePath(file
                            .toString())) {
                        images.addToDirectory(new ImageWrapper
                                (ImageTagManager.getInstance().getImage(file
                                        .toString())));
                    } else {
                        images.addToDirectory(new ImageWrapper(new Image(file.toFile(),
                                PathExtractor.getImageName(file.toString()))));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return images;
    }

    /**
     * Open the root folder using the OS's file viewer
     */
    public void openRootFolder() {
        if (rootFolder.getPath() != null) {
            if (Desktop.isDesktopSupported()) {
                new Thread(() -> {
                    try {
                        Desktop.getDesktop().open(this.rootFolder.getPath()
                                .toFile());
                    } catch (IOException e) {
                        String popupText = "Unable to open directory.";
                        Popup.errorPopup("Error", popupText);
                    }
                }).start();
            } else {
                String popupText = "The Java awt Desktop API is not supported" +
                        " on this machine.";
                Popup.errorPopup("Error", popupText);
            }
        }
    }

    /**
     * Generates a string matching pattern, using the formats in imageFormats
     *
     * @return a regular expression
     */
    private String generateImageMatchingPattern() {
        StringBuilder sb = new StringBuilder();
        sb.append(".+(");
        for (String format : imageFormats) {
            sb.append(format).append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")$");
        return sb.toString();
    }
}
