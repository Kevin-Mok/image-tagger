package main;

import fx.Popup;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Image class that stores its path and a ImageTagManager object to work with
 * its tags.
 */
public class Image implements Serializable {
    private File imageFile;
    private TagManager tagManager;
    private String imageName;

    /**
     * Constructor.
     *
     * @param imageFile File object of the image.
     * @param imageName The name of the file (without its extension).
     */
    public Image(File imageFile, String imageName) {
        this.imageFile = imageFile;
        this.imageName = imageName;
        tagManager = new TagManager(imageName, this);
        ImageTagManager.getInstance().addImage(this);
    }

    /**
     * Gets the image name without its extension
     *
     * @return the image name without its extension
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Gets the path of the image
     *
     * @return the path of the image
     */
    public Path getPath() {
        return imageFile.toPath();
    }

    /**
     * Return this image's Path.
     *
     * @return String of this image's path.
     */
    public String getPathString() {
        return imageFile.toString();
    }

    /**
     * Return this image's current directory.
     *
     * @return String of this image's current directory.
     */
    public String getCurDir() {
        return PathExtractor.getDirectory(imageFile.getPath());
    }

    /**
     * Returns file name of image with its extension.
     *
     * @return String of this image's file name with its extension.
     */
    @Override
    public String toString() {
        return PathExtractor.getImageFileName(imageFile.toString());
    }

    /**
     * Renames this Image object and its associated file.
     *
     * @param newImageName The new name of the Image.
     */
    private void rename(String newImageName) {
        // String newPathString = curDir + newImageName + extension;
        String curImageName = imageName;
        move(getCurDir(), newImageName, true);
        // todo: log once per batch action
        LogUtility.getInstance().logImageRename(curImageName,
                newImageName);
    }

    /**
     * Moves this Image to a new directory. Also used for renaming when newDir
     * is same as old but newImageName is different.
     *
     * @param newDir       New directory to move this image to.
     * @param newImageName New name to rename this image to.
     * @param renaming     Whether the call to this function is for renaming
     *                     or not.
     */
    public void move(String newDir, String newImageName, boolean renaming) {
        try {
            /* Strings of all parts of this image's path. */
            // String imageFileName = PathExtractor.getImageFileName
            //         (imageFile.toString());
            String extension = PathExtractor.getExtension(imageFile.getPath());
            String newPathString = newDir + "/" + newImageName + extension;

            ImageTagManager imageTagManager = ImageTagManager.getInstance();
            imageTagManager.removeImage(imageFile.toString());
            Files.move(imageFile.toPath(), Paths.get(newPathString));
            if (!renaming) {
                LogUtility.getInstance().logMoveImage(imageFile.getPath(),
                        newPathString);
            }

            /* Update changes to this Image's fields. */
            imageFile = new File(newPathString);
            imageName = newImageName;
            // tagManager.setImage(this);

            /* Update the changes in the singleton ImageTagManager.*/
            imageTagManager.addImage(this);
            imageTagManager.refreshNameToTags();
        } catch (IOException e) {
            String popupTitle = "Error";
            String popupText = "File could not be moved or renamed.";
            Popup.errorPopup(popupTitle, popupText);
//            System.out.println("File could not be moved or renamed.");
        }
    }

    /**
     * Returns this Image's TagManager.
     *
     * @return TagManager of this image.
     */
    public TagManager getTagManager() {
        return tagManager;
    }

    /**
     * Adds a tag to the currently existing ones.
     *
     * @param tagName The name of the tag to be added.
     */
    public void addTag(String tagName) {
        if (!this.hasTag(tagName)) {
            rename(tagManager.addTag(tagName));
        }
    }

    /**
     * Checks whether this image has a given tag
     * @param tagName the tag to check
     * @return true if this image has the tag
     */
    private boolean hasTag(String tagName) {
        return this.tagManager.hasTag(tagName);
    }

    /**
     * Removes a tag from the currently existing ones.
     *
     * @param tagName The name of the tag to be removed.
     */
    public void deleteTag(String tagName) {
        rename(tagManager.deleteTag(tagName));
    }

    /**
     * Checks if this image has all the tags in a list of tag names
     * @param tagNames the tag names to check
     * @return true if the image contains at least a tag in the list, or if the list is empty (no filtering),
     *         false if otherwise
     */
    public boolean hasTags(List<String> tagNames) {
        if (tagNames.size() == 0) {
            return true;
        }
        return this.tagManager.hasTags(tagNames);
    }
    /**
     * Reverts the name of the file to a previous one in time.
     *
     * @param name Name to be reverted to.
     */
    public void revertName(String name) {
        rename(tagManager.revertName(name));
    }

    /**
     * Image objects are only equals if they are both Images and the same
     * instance or their Files are the same.
     *
     * @param o Object to be checked with against this Image.
     * @return Whether this and object o are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;

        Image image = (Image) o;

        return imageFile.equals(image.imageFile);
    }
}
