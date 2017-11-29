package main;

import main.wrapper.DirectoryWrapper;
import main.wrapper.ImageWrapper;
import main.wrapper.ItemWrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Singleton class responsible for searching through a given directory and its
 * subdirectories, looking for image files
 * Also takes care of opening that given directory using the OS's file viewer
 */
public class DirectoryManager {
    private static final String LAST_DIR_FILE = "last_dir.txt";
    /**
     * The singleton instance of DirectoryManager
     */
    private static DirectoryManager instance;
    /**
     * The top level directory for the current instance of the Image Viewer
     * program
     */
    private DirectoryWrapper rootFolder;
    /**
     * Path to text configuration file that contains the image formats to list
     */
    private ArrayList<String> imageFormats;

    private List<Image> allImagesUnderRoot;

    /**
     * Private constructor for singleton use
     */
    private DirectoryManager() {
    }

    public static DirectoryManager getInstance() {
        if (instance == null) {
            instance = new DirectoryManager();
            instance.imageFormats = new ArrayList<>();
            instance.allImagesUnderRoot = new ArrayList<>();
            // temporarily adding in desired formats
            instance.imageFormats.add("jpg");
            instance.imageFormats.add("png");
            instance.imageFormats.add("jpeg");
        }
        return instance;
    }

    /**
     * Returns the root folder of this DirectoryManager.
     *
     * @return The DirectoryWrapper of this object containing the root folder
     * path.
     */
    public DirectoryWrapper getRootFolder() {
        return rootFolder;
    }

    /**
     * Sets the root folder of this DirectoryManager.
     *
     * @param rootFolder The DirectoryWrapper to set this object's root
     *                   folder to.
     */
    public void setRootFolder(File rootFolder) {
        this.rootFolder = getImages(rootFolder.toPath());
    }

    /**
     * Returns ItemWrapper representing the root directory, encapsulating all
     * its
     * subdirectories and images
     *
     * @return ItemWrapper representing the root directory
     */
    public ItemWrapper getRootDirectory() {
        return getImages(rootFolder.getPath());
    }

    /**
     * Returns the list of all images under the root folder
     *
     * @return the list of all images under the root folder
     */
    public List<Image> getAllImagesUnderRoot() {
        return allImagesUnderRoot;
    }

    /**
     * Returns a ItemWrapper representing a directory with the images inside and
     * subdirectories
     *
     * @param directory the directory to search in
     * @return ItemWrapper representing the subdirectories and pictures in a
     * directory
     */
    private DirectoryWrapper getImages(Path directory) {
        DirectoryWrapper directoryWrapper = new DirectoryWrapper(directory
                .toFile());
        Pattern imgFilePattern = Pattern.compile(generateImageMatchingPattern
                ());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream
                (directory)) {
            for (Path file : stream) {
                if (Files.isDirectory(file)) {
                    ItemWrapper subImages = getImages(file);
                    if (((DirectoryWrapper) subImages).getChildObjects()
                            .size() != 0) {
                        directoryWrapper.addToDirectory(subImages);
                    }
                }
                Matcher matcher = imgFilePattern.matcher(file.toString());
                if (matcher.matches()) {
                    if (ImageTagManager.getInstance().containsImagePath(file
                            .toString())) {
                        Image img = ImageTagManager.getInstance().getImage
                                (file.toString());
                        directoryWrapper
                                .addToDirectory(new ImageWrapper(img));
                        this.allImagesUnderRoot.add(img);
                    } else {
                        Image img = new Image(file.toFile(), PathExtractor
                                .getImageName(file.toString()));
                        this.allImagesUnderRoot.add(img);
                        img.getTagManager().addAllExistingTags(img
                                .getImageName()
                                .split("@"));
                        directoryWrapper.addToDirectory(new ImageWrapper(img));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directoryWrapper;
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

    /**
     * Checks if a file/directory is under the root directory
     *
     * @param path path of the file/directory
     * @return true if the file/directory is under the root directory
     */
    public boolean isUnderRootDirectory(File path) {
        return this.getRootFolder().getPath().toString().contains(path
                .getPath());
    }

    /**
     * Reads the stored root directory in LAST_DIR_FILE.
     *
     * @return Whether it was able to read a valid directory or not.
     */
    public boolean readLastDir() {
        try {
            Scanner lastDirScanner = new Scanner(new FileReader(LAST_DIR_FILE));
            if (lastDirScanner.hasNext()) {
                File lastDirFile = new File(lastDirScanner.next());
                if (lastDirFile.isDirectory()) {
                    this.setRootFolder(lastDirFile);
                    return true;
                }
                System.out.println("Last stored directory was not valid.");
            }
            lastDirScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find last directory.");
        }
        return false;
    }

    /**
     * Saves the last used root directory to LAST_DIR_FILE.
     */
    public void saveLastDir() {
        try {
            Files.write(Paths.get(LAST_DIR_FILE), rootFolder.toString()
                    .getBytes());
        } catch (IOException e) {
            System.out.println("Couldn't save last directory.");
        }
    }
}
