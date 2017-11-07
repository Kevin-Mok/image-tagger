package main;

import java.io.File;

/**
 * Picture class that stores its path and a TagManager object to work with
 * its tags.
 */
public class Picture {
    private File imageFile;
    private TagManager tagManager;

    /**
     * Constructor.
     *
     * @param imageFile File object of the image.
     * @param fileName  The name of the file (without its extension).
     */
    Picture(File imageFile, String fileName) {
        this.imageFile = imageFile;
        tagManager = new TagManager(fileName, this);
    }

    // Renames the file to the given String.
    void rename(String tags) {
        String curPath = imageFile.getPath();
        int indexOfLastSlash = curPath.lastIndexOf('/');
        String curDir = curPath.substring(0, indexOfLastSlash + 1);
        int indexOfLastPeriod = curPath.lastIndexOf('.');
        String extension = curPath.substring(indexOfLastPeriod);
        // String name = curPath.substring(indexOfLastSlash + 1,
        // indexOfLastPeriod);
        if (!imageFile.renameTo(new File(curDir + tags + extension))) {
            System.out.println("File renaming failed.");
        }
    }

    /**
     * Adds a tag to the currently existing ones.
     *
     * @param tagName The name of the tag to be added.
     */
    public void addTag(String tagName) {
        rename(tagManager.addTag(new Tag(this, tagName)));
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

}
