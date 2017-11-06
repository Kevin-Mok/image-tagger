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
     * The top level directory for the current instance of the Image Viewer program
     */
    private File rootFolder;
    /**
     * Path to text configuration file that contains the image formats to list
     */
    private File imageFormatConfig;

    public DirectoryManager(File rootFolder, File imageFormatConfig) {
        this.rootFolder = rootFolder;
        this.imageFormatConfig = imageFormatConfig;
    }

    public File getRootFolder() {
        return rootFolder;
    }

    public File getImageFormatConfig() {
        return imageFormatConfig;
    }

    public void setImageFormatConfig(File imageFormatConfig) {
        this.imageFormatConfig = imageFormatConfig;
    }

    public void setRootFolder(File rootFolder) {
        this.rootFolder = rootFolder;
    }

    /**
     * Returns a list of all images directly under the root folder
     * @return List of image paths directly under the root folder
     */
    public List<String> getImagesUnderRoot() {
        return getImages(rootFolder.toPath(), false);
    }

    /**
     * Returns a list of all the images under the root directory(including sub-folders)
     * @return List of image paths including those in subdirectories
     */
    public List<String> getAllImagesUnderRoot() {
        return getImages(rootFolder.toPath(), true);
    }

    /**
     * Takes in a directory path and returns a list of the path strings of all the images in that directory
     * @param directory the directory to search in
     * @param recursive whether or not the search recursively in the subfolders
     * @return list of the paths of image files
     */
    private List<String> getImages(Path directory, boolean recursive) {
        List<String> images = new ArrayList<>();
        Pattern imgFilePattern = Pattern.compile(generateImageMatchingPattern());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(rootFolder.toPath())) {
            for (Path file : stream) {
                if (recursive) {
                    if (Files.isDirectory(file)) {
                        getImages(file, true);
                    }
                }
                Matcher matcher = imgFilePattern.matcher(file.toString());
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
            desktop = Desktop.getDesktop();
            try {
                desktop.open(rootFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The Java awt Desktop API is not supported on this machine");
        }
    }

    private String generateImageMatchingPattern() {
        StringBuilder sb = new StringBuilder();
        sb.append("*.{");
        try (BufferedReader br = new BufferedReader(new FileReader(imageFormatConfig))) {
            /**
             * Appending the first line outside the while loop so the pattern wouldn't end with a comma
             */
            String line;
            line = br.readLine();
            if (line != null) {
                sb.append(line);
            }
            while ((line = br.readLine()) != null) {
                sb.append(",");
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sb.append("}");
        return sb.toString();
    }
    // main() for testing purposes only
//    public static void main(String[] args) {
//        File rootFolder = new File("/h/u7/c7/05/shyichin");
//        main.DirectoryManager manager = new main.DirectoryManager(rootFolder, null);
//        manager.openRootFolder();
//    }
}
