package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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
    private HashMap<String, ArrayList<Image>> tagToImageListMap;
    // HashMap of String of paths to Image with that path.
    private HashMap<String, Image> pathToImagesMap;
    //ArrayList of Hidden Tags
    private ArrayList<String> hiddenTagNames;

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
            instance.tagToImageListMap = new HashMap<>();
            instance.pathToImagesMap = new HashMap<>();
            instance.hiddenTagNames = new ArrayList<>();
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
        /* After updating the tagToImageListMap, iterate through all the tag
        names and add their count to before the name. */
        refreshTagToImageList();
        String[] availableTagsWithCount = tagToImageListMap.keySet().toArray(new
                String[0]);
        for (int i = 0; i < availableTagsWithCount.length; i++) {
            String curTagName = availableTagsWithCount[i];
            int imageCount = (tagToImageListMap.containsKey(curTagName)) ?
                    tagToImageListMap.get(curTagName).size() : -1;
            availableTagsWithCount[i] = String.format("(%d) - %s", imageCount,
                    curTagName);
        }
        Arrays.sort(availableTagsWithCount, Collections.reverseOrder());
        return availableTagsWithCount;
    }

    /**
     * Hides the tag in the available tags view.
     *
     * @param tagName Name of tag to be hidden.
     */
    public void hideThisTag(String tagName) {
        hiddenTagNames.add(tagName);
    }

    /**
     * When adding a tag, if the tag is currently hidden, this will make it
     * show again.
     *
     * @param tagName Tag name to be shown again.
     */
    void removeFromHidden(String tagName) {
        if (hiddenTagNames.contains(tagName)) {
            hiddenTagNames.remove(tagName);
        }
    }

    /**
     * Remove Image with path of parameter from HashMap of pathToImagesMap.
     *
     * @param pathString Path of Image to remove.
     */
    void removeImage(String pathString) {
        pathToImagesMap.remove(pathString);
    }

    /**
     * Returns image with path parameter in pathToImagesMap.
     *
     * @param path the path to use to look up an image
     * @return Image with path parameter in pathToImagesMap.
     */
    Image getImage(String path) {
        return pathToImagesMap.get(path);
    }

    /**
     * onAction="#hiddenTagNames"
     * Returns whether pathToImagesMap contains a key of path parameter.
     *
     * @param path the path to check
     * @return Boolean of whether pathToImagesMap contains a key of path parameter.
     */
    boolean containsImagePath(String path) {
        return pathToImagesMap.containsKey(path);
    }

    /**
     * Add Image parameter to pathToImagesMap.
     *
     * @param image Image to add.
     */
    void addImage(Image image) {
        pathToImagesMap.put(image.getPathString(), image);
    }

    /**
     * Recreates tagToImageListMap based on Images in pathToImagesMap, unused
     * tags, placeholder tags and hidden tags.
     */
    void refreshTagToImageList() {
        tagToImageListMap = new HashMap<>();
        addImagesFromPathMap();
        addUnusedTagsToMap();
        addPlaceholderTagsToMap();
        removeHiddenTagsFromMap();
    }

    /* Iterates through all existing Images and adds all their tag names
    and associated images to tagToImageListMap map. */
    private void addImagesFromPathMap() {
        for (Image image : pathToImagesMap.values()) {
            /* If it's the placeholder image, don't do anything with it. */
            if (!image.getImageName().equals(PLACEHOLDER_IMAGE_NAME)) {
                ArrayList<String> imageTagNames = image.getTagManager()
                        .getTagNames();
                for (String tagName : imageTagNames) {
                    /* If tag name is not a key, create a new mapping from it to
                    a new ArrayList. Then, add image to new ArrayList or the
                    already existing one. */
                    if (!tagToImageListMap.containsKey(tagName)) {
                        tagToImageListMap.put(tagName, new ArrayList<>());
                    }
                    tagToImageListMap.get(tagName).add(image);
                }
            }
        }
    }

    /* Add unused tags to tagToImageListMap with an empty ArrayList if it's not
    already in there. */
    private void addUnusedTagsToMap() {
        for (Image image : pathToImagesMap.values()) {
            for (String unusedTagName : image.getTagManager().getUnusedTags()) {
                if (!tagToImageListMap.containsKey(unusedTagName))
                    tagToImageListMap.put(unusedTagName, new ArrayList<>());
            }
        }
    }

    /* Add any tags in the placeholder image to the tagToImageListMap if they aren't
    there already. */
    private void addPlaceholderTagsToMap() {
        if (pathToImagesMap.containsKey(PLACEHOLDER_IMAGE_NAME)) {
            ArrayList<String> placeHolderTagNames = pathToImagesMap.get
                    (PLACEHOLDER_IMAGE_NAME).getTagManager().getTagNames();
            for (String tagName : placeHolderTagNames) {
                if (!tagToImageListMap.containsKey(tagName))
                    tagToImageListMap.put(tagName, new ArrayList<>());
            }
        }
    }

    /* Hidden tags will be removed from tagToImageListMap since the user
    doesn't want them to be displayed. */
    private void removeHiddenTagsFromMap() {
        for (String hiddenTagName : hiddenTagNames) {
            tagToImageListMap.remove(hiddenTagName);
        }
    }

    /**
     * Add tag to the placeholder image to display in available tags.
     *
     * @param tagName Name of tag to be added to placeholder.
     */
    public void addTagToPlaceholder(String tagName) {
        if (!pathToImagesMap.containsKey(PLACEHOLDER_IMAGE_NAME)) {
            pathToImagesMap.put(PLACEHOLDER_IMAGE_NAME, new Image
                    ());
        }
        Image placeHolderImage = pathToImagesMap.get(PLACEHOLDER_IMAGE_NAME);
        placeHolderImage.getTagManager().addTag(tagName);
        removeFromHidden(tagName);
    }

    /*
    ** Deletes entries from pathToImagesMap that only have a name history of
    ** size 1 (i.e. no tags were ever added to that Image). Decreases size of
    ** serialized objects.
    */
    private void deleteUselessImageObjects() {
        HashMap<String, Image> rebuild = new HashMap<>();
        for (String path : pathToImagesMap.keySet()) {
            boolean noNewName = pathToImagesMap.get(path).getTagManager()
                    .getNameHistory().size() != 1;
            String pathOfPlaceHolderImage = DirectoryManager.PROJECT_DIR +
                    File.separator + PLACEHOLDER_IMAGE_NAME;
            if (noNewName || path.equals(pathOfPlaceHolderImage)) {
                rebuild.put(path, pathToImagesMap.get(path));
            }
        }
        pathToImagesMap = rebuild;
    }
    /*
    ** Iterates though the image HashMap and
    ** deletes image objects that correspond to non-existent images on your computer
     */
    private void deleteNonExistentImages() {
        ArrayList<String> toDelete = new ArrayList<>();
        for (String path : pathToImagesMap.keySet()) {
            if (!path.equals(PLACEHOLDER_IMAGE_NAME) && !new File(path)
                    .exists()) {
                toDelete.add(path);
            }
        }
        for (String deleteItem : toDelete) {
            pathToImagesMap.remove(deleteItem);
        }
        refreshTagToImageList();
    }

    /**
     * Reads the serialization of the HashMaps (tagToImageListMap, pathToImagesMap) of
     * this class.
     */
    @SuppressWarnings("unchecked")
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
            pathToImagesMap = (HashMap<String, Image>) pathToImagesObject;
            imagesObjectInput.close();
            deleteNonExistentImages();
        } catch (IOException e) {
            System.out.println(SER_FILE_NAME + " was not found and will be " +
                    "created upon exiting the program.");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found.");
        }
    }

    /**
     * Serializes the pathToImagesMap HashMap of this class.
     */
    public void saveToFile() {
        try {
            deleteUselessImageObjects();
            OutputStream imagesFileOutput = new FileOutputStream(SER_FILE_NAME);
            OutputStream imagesBuffer = new BufferedOutputStream
                    (imagesFileOutput);
            ObjectOutput imagesObjectOutput = new ObjectOutputStream
                    (imagesBuffer);

            imagesObjectOutput.writeObject(pathToImagesMap);
            System.out.printf("Serialized %s.%n", SER_FILE_NAME);
            imagesObjectOutput.close();
        } catch (IOException e) {
            System.out.printf("Could not serialize %s.%n", SER_FILE_NAME);
        }
    }

}


