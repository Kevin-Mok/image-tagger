package main;

import java.awt.*;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectoryManager {
    /**
     * The top level directory for the current instance of the Picture Viewer
     * program
     */
    private File rootFolder;
    /**
     * Path to text configuration file that contains the image formats to list
     */
    private ArrayList<String> imageFormats;

    public DirectoryManager(File rootFolder) {
        this.rootFolder = rootFolder;
        this.imageFormats = new ArrayList<>();
        // temporarily adding in desired formats
        imageFormats.add("jpg");
        imageFormats.add("png");
    }

    public File getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(File rootFolder) {
        this.rootFolder = rootFolder;
    }

    public ArrayList<String> getImageFormats() {
        return imageFormats;
    }

    public void setImageFormats(ArrayList<String> imageFormats) {
        this.imageFormats = imageFormats;
    }

    /**
     * Returns a list of all icons directly under the root folder, not
     * including those in sub-folders
     *
     * @return List of image paths directly under the root folder
     */
    public List<String> getImagesUnderRoot() {
        return getImages(rootFolder.toPath(), false);
    }

    /**
     * Returns a list of all the icons under the root directory(including
     * sub-folders)
     *
     * @return List of image paths under the root directory, including those
     * in subdirectories
     */
    public List<String> getAllImagesUnderRoot() {
        return getImages(rootFolder.toPath(), true);
    }

    /**
     * Takes in a directory path and returns a list of the path strings of
     * all the icons in that directory
     *
     * @param directory the directory to search in
     * @param recursive whether or not to search recursively in the subfolders
     * @return list of the paths of image files
     */
    public List getImages(Path directory, boolean recursive) {
        List images = new ArrayList<>();
        Pattern imgFilePattern = Pattern.compile(generateImageMatchingPattern
                ());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream
                (directory)) {
            for (Path file : stream) {
                if (recursive) {
                    if (Files.isDirectory(file)) {
                        // System.out.println(file);
                        images.add(getImages(file, true));
                    }
                }
                Matcher matcher = imgFilePattern.matcher(file.toString());
                // System.out.println(file);
                if (matcher.matches()) {
                    images.add(file.toString());
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
        Desktop desktop;
        if (Desktop.isDesktopSupported()) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(this.rootFolder);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }).start();
        } else {
            System.out.println("The Java awt Desktop API is not supported on " +
                    "this machine");
        }
    }

    private String generateImageMatchingPattern() {
        StringBuilder sb = new StringBuilder();
        sb.append(".+(");
        for (String format : imageFormats) {
            sb.append(format + "|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")$");
        return sb.toString();
    }

    // main() for testing purposes only
    public static void main(String[] args) {
        // File rootFolder = new File("/h/u7/c7/05/shyichin");
        File rootFolder = new File("/home/kevin/Pictures");
        main.DirectoryManager m = new main.DirectoryManager(rootFolder);
        // manager.openRootFolder();
        System.out.println(m.generateImageMatchingPattern());
        System.out.println(m.getImages(rootFolder.toPath(), true));
    }
}
