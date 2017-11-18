package main;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;

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
     * Return name of image without its extension.
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Return path of image.
     */
    public Path getPath() {
        return imageFile.toPath();
    }

    /**
     * Returns file name of image with its extension.
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
        ImageTagManager imageTagManager = ImageTagManager.getInstance();
        String curPath = imageFile.getPath();
        String curDir = PathExtractor.getDirectory(curPath);
        String extension = PathExtractor.getExtension(curPath);
        String newPathString = curDir + newImageName + extension;
        if (!imageFile.renameTo(new File(newPathString))) {
            System.out.println("File renaming failed.");
        } else {
            // Renaming was a success and following is all business that
            // needs to be taken care of upon renaming this image.
            imageFile = new File(newPathString);
            imageName = newImageName;
            imageTagManager.removeImage(curPath);
            imageTagManager.addImage(this);
            imageTagManager.refreshNameToTags();
            LogUtility.getInstance().logImageRename(this.getImageName(), newImageName);
        }

    }

    /**
     * Returns this Image's TagManager.
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
        rename(tagManager.addTag(tagName));

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
