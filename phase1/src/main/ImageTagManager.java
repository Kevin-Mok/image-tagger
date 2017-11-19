package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Singleton manager class to keep track of all the existing Images and Tags
 * contained within them.
 */
public class ImageTagManager {
    // Singleton instance of this class.
    private static ImageTagManager instance = null;
    // HashMap of String of tag names to list of Images containing that tag.
    private HashMap<String, ArrayList<Image>> tagToImageList;
    // HashMap of String of paths to Image with that path.
    private HashMap<String, Image> pathToImages;

    /**
     * Private constructor so that only one instance may be created through a
     * static method
     */
    private ImageTagManager() {
    }

    /**
     * Returns the singleton instance of this class.
     */
    public static ImageTagManager getInstance() {
        if (instance == null) {
            instance = new ImageTagManager();
            instance.tagToImageList = new HashMap<>();
            instance.pathToImages = new HashMap<>();
        }
        return instance;
    }

    /**
     * Returns String array of all existing tag names in alphabetical order.
     */
    public String[] getListOfTags() {
        Set<String> setOfTagString = tagToImageList.keySet();
        String[] listOfTags = setOfTagString.toArray(new
                String[setOfTagString.size()]);
        Arrays.sort(listOfTags);
        return listOfTags;
    }

    /**
     * Remove Image with path of parameter from HashMap of pathToImages.
     *
     * @param pathString Path of Image to remove.
     */
    public void removeImage(String pathString) {
        pathToImages.remove(pathString);
    }

    /**
     * Returns image with path parameter in pathToImages.
     */
    Image getImage(String path) {
        return pathToImages.get(path);
    }

    /**
     * Returns whether pathToImages contains a key of path parameter.
     */
    boolean containsImagePath(String path) {
        return pathToImages.containsKey(path);
    }

    /**
     * Add Image parameter to pathToImages.
     *
     * @param image Image to add.
     */
    public void addImage(Image image) {
        pathToImages.put(image.getPath().toString(), image);
    }

    /**
     * Recreates tagToImageList HashMap based on Images in pathToImages.
     */
    void refreshNameToTags() {
        HashMap<String, ArrayList<Image>> nameToTags = new HashMap<>();
        // Iterates through all existing Images and adds all their tag names
        // and associated images to new tagToImageList map.
        for (Image image : pathToImages.values()) {
            ArrayList<String> imageTagNames = image.getTagManager()
                    .getTagNames();
            for (String tagName : imageTagNames) {
                // If tag name is not a key, create a new mapping from it to
                // a new ArrayList. Then, add image to new ArrayList or the
                // already existing one.
                if (!nameToTags.containsKey(tagName)) {
                    nameToTags.put(tagName, new ArrayList<>());
                }
                nameToTags.get(tagName).add(image);
            }
            // mapBuilder(image, tagToImageList);
        }
        this.tagToImageList = nameToTags;
    }

    /**
     * Serializes the HashMaps (tagToImageList, pathToImages) of this class.
     */
    public void saveToFile() {
        try {
            deleteUselessImageObjects();
            OutputStream imagesFileOutput = new FileOutputStream("images.ser");
            OutputStream imagesBuffer = new BufferedOutputStream
                    (imagesFileOutput);

            ObjectOutput imagesObjectOutput = new ObjectOutputStream
                    (imagesBuffer);

            OutputStream tagsFileOutput = new FileOutputStream("tags.ser");
            OutputStream tagsBuffer = new BufferedOutputStream(tagsFileOutput);
            ObjectOutput tagsObjectOutput = new ObjectOutputStream(tagsBuffer);

            tagsObjectOutput.writeObject(tagToImageList);
            imagesObjectOutput.writeObject(pathToImages);

            imagesObjectOutput.close();
            tagsObjectOutput.close();
        } catch (IOException e) {
            System.out.println("Could not serialize ImageTagManager.");
        }
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


    /**
     * Reads the serialization of the HashMaps (tagToImageList, pathToImages) of
     * this class.
     */
    public void readFromFile() {
        try {
            InputStream imagesFileInput = new FileInputStream("images.ser");
            InputStream imagesBuffer = new BufferedInputStream(imagesFileInput);
            ObjectInput imagesObjectInput = new ObjectInputStream(imagesBuffer);

            InputStream tagsFileInput = new FileInputStream("tags.ser");
            InputStream tagsBuffer = new BufferedInputStream(tagsFileInput);
            ObjectInput tagsObjectOutput = new ObjectInputStream(tagsBuffer);

            Object pathToImagesObject = imagesObjectInput.readObject();
            Object nameToTagsObject = tagsObjectOutput.readObject();

            pathToImages = (HashMap<String, Image>) pathToImagesObject;
            tagToImageList = (HashMap<String, ArrayList<Image>>)
                    nameToTagsObject;

            imagesObjectInput.close();
            tagsObjectOutput.close();
        } catch (IOException e) {
            System.out.println("Ser files were not found and will be " +
                    "created upon exiting the program.");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found.");
        }

        System.out.println(pathToImages);
        System.out.println(tagToImageList);
    }

}


