package main;

import java.io.*;
import java.util.*;

/**
 * Singleton manager class to keep track of all the existing Images and Tags
 * contained within them.
 */
public class ImageTagManager {
    /**
     * Serialized file name
     */
    public static final String SER_FILE_NAME = "path_to_images.ser";
    static final String PLACEHOLDER_IMAGE_NAME = "Placeholder Image";

    // Singleton instance of this class.
    private static ImageTagManager instance = null;
    // HashMap of String of tag names to list of Images containing that tag.
    private HashMap<String, ArrayList<Image>> tagToImageList;
    // HashMap of String of paths to Image with that path.
    private HashMap<String, Image> pathToImages;
    private ArrayList<String> hideTags;

    /**
     * Private constructor so that only one instance may be created through a
     * static method
     */
    private ImageTagManager() {
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return The singleton ImageTagManager instance.
     */
    public static ImageTagManager getInstance() {
        if (instance == null) {
            instance = new ImageTagManager();
            instance.tagToImageList = new HashMap<>();
            instance.pathToImages = new HashMap<>();
            instance.hideTags = new ArrayList<>();
            instance.pathToImages.put(PLACEHOLDER_IMAGE_NAME, new Image
                    (PLACEHOLDER_IMAGE_NAME));
        }
        return instance;
    }

    /**
     * Return all existing tags with how many times they are currently being
     * used.
     *
     * @return String array of all existing tags with how many times they are
     * currently being used.
     */
    public String[] getAvailableTagsWithCount() {
        refreshNameToTags();
        String[] availableTagsWithCount = tagToImageList.keySet().toArray(new
                String[0]);
        for (int i = 0; i < availableTagsWithCount.length; i++) {
            String curTagName = availableTagsWithCount[i];
            int imageCount = (tagToImageList.containsKey(curTagName)) ?
                    tagToImageList.get(curTagName).size() : -1;
            availableTagsWithCount[i] = String.format("(%d) - %s", imageCount,
                    curTagName);
        }
        Arrays.sort(availableTagsWithCount, Collections.reverseOrder());
        return availableTagsWithCount;
    }

    public void hideThisTag(String tagName) {
        hideTags.add(tagName);
    }

    /**
     * Remove Image with path of parameter from HashMap of pathToImages.
     *
     * @param pathString Path of Image to remove.
     */
    void removeImage(String pathString) {
        pathToImages.remove(pathString);
    }

    /**
     * Returns image with path parameter in pathToImages.
     * @param path the path to use to look up an image
     * @return Image with path parameter in pathToImages.
     */
    Image getImage(String path) {
        return pathToImages.get(path);
    }

    /**
     * onAction="#hideTags"
     * Returns whether pathToImages contains a key of path parameter.
     * @param path the path to check
     * @return Boolean of whether pathToImages contains a key of path parameter.
     */
    boolean containsImagePath(String path) {
        return pathToImages.containsKey(path);
    }

    /**
     * Add Image parameter to pathToImages.
     *
     * @param image Image to add.
     */
    void addImage(Image image) {
        pathToImages.put(image.getPathString(), image);
    }

    /**
     * Recreates tagToImageList HashMap based on Images in pathToImages.
     */
    void refreshNameToTags() {
        HashMap<String, ArrayList<Image>> nameToTags = new HashMap<>();
        // Iterates through all existing Images and adds all their tag names
        // and associated images to new tagToImageList map.
        for (Image image : pathToImages.values()) {
            if (!image.getImageName().equals(PLACEHOLDER_IMAGE_NAME)) {
                ArrayList<String> imageTagNames = image.getTagManager()
                        .getTagNames();
                for (String tagName : imageTagNames) {
                    // If tag name is not a key, create a new mapping from it to
                    // a new ArrayList. Then, add image to new ArrayList or the
                    // already existing one.
                    if (!hideTags.contains(tagName)) {
                        if (!nameToTags.containsKey(tagName)) {
                            nameToTags.put(tagName, new ArrayList<>());
                        }
                        nameToTags.get(tagName).add(image);
                    }
                }
            }
        }
        for (Image image : pathToImages.values()) {
            for (String unUsed : image.getTagManager().getUnusedTags()) {
                if (!nameToTags.containsKey(unUsed))
                    nameToTags.put(unUsed, new ArrayList<>());
            }
        }
        // mapBuilder(image, tagToImageList);

        ArrayList<String> leftOver = pathToImages.get(PLACEHOLDER_IMAGE_NAME)
                .getTagManager().getTagNames();
        for (String tagNames : leftOver) {
            if (!nameToTags.containsKey(tagNames))
                nameToTags.put(tagNames, new ArrayList<>());
        }
        this.tagToImageList = nameToTags;
    }

    public void addTagToToken(String tagName) {
        Image img = pathToImages.get(PLACEHOLDER_IMAGE_NAME);
        img.getTagManager().addTag(tagName);
    }

    /*
    ** Deletes entries from pathToImages that only have a name history of
    ** size 1 (i.e. no tags were ever added to that Image). Decreases size of
    ** serialized objects.
    */
    private void deleteUselessImageObjects() {
        HashMap<String, Image> rebuild = new HashMap<>();
        for (String keys : pathToImages.keySet()) {
            if (pathToImages.get(keys).getTagManager().getNameHistory().size()
                    != 1) {
                rebuild.put(keys, pathToImages.get(keys));
            }
        }
        pathToImages = rebuild;
    }

    private void deleteNonExistentImages() {
        ArrayList<String> toDelete = new ArrayList<>();
        for (String path : pathToImages.keySet()) {
            if (!path.equals(PLACEHOLDER_IMAGE_NAME) && !new File(path).exists()) {
                    toDelete.add(path);
                }
            }
        for (String deleteItem : toDelete) {
            pathToImages.remove(deleteItem);
        }
        refreshNameToTags();
    }

    /**
     * Reads the serialization of the HashMaps (tagToImageList, pathToImages) of
     * this class.
     */
    public void readFromFile() {
        try {
            InputStream imagesFileInput = new FileInputStream(SER_FILE_NAME);
            InputStream imagesBuffer = new BufferedInputStream(imagesFileInput);
            ObjectInput imagesObjectInput = new ObjectInputStream(imagesBuffer);

            Object pathToImagesObject = imagesObjectInput.readObject();

            /* Can't find a way to fix this yellow error. In the Serializable
            code given in class, the same error occurs. Also on this answer on
            Stack Overflow (https://stackoverflow.com/a/1609963/8811872), it
            says that there isn't really a good way to handle this in Java.
             */
            pathToImages = (HashMap<String, Image>) pathToImagesObject;
            imagesObjectInput.close();
            if (!pathToImages.containsKey(PLACEHOLDER_IMAGE_NAME)) {
                pathToImages.put(PLACEHOLDER_IMAGE_NAME, new Image
                        (PLACEHOLDER_IMAGE_NAME));
            }
            deleteNonExistentImages();
        } catch (IOException e) {
            System.out.println(SER_FILE_NAME + " was not found and will be " +
                    "created upon exiting the program.");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found.");
        }


    }

    /**
     * Serializes the pathToImages HashMap of this class.
     */
    public void saveToFile() {
        try {
            deleteUselessImageObjects();
            OutputStream imagesFileOutput = new FileOutputStream(SER_FILE_NAME);
            OutputStream imagesBuffer = new BufferedOutputStream
                    (imagesFileOutput);
            ObjectOutput imagesObjectOutput = new ObjectOutputStream
                    (imagesBuffer);

            imagesObjectOutput.writeObject(pathToImages);
            imagesObjectOutput.close();
        } catch (IOException e) {
            System.out.printf("Could not serialize %s.%n", SER_FILE_NAME);
        }
    }

}


