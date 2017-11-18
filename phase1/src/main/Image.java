package main;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.logging.Level;

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

    public String getImageName() {
        return imageName;
    }

    public Path getPath() {
        return imageFile.toPath();
    }

    public void setPath(String path) {
        imageFile = new File(path);
    }

    @Override
    public String toString() {
        return PathExtractor.getImageFileName(imageFile.toString());
    }

    /**
     * Renames this image
     *
     * @param newImageName the new image name
     */
    void rename(String newImageName) {
        String curPath = imageFile.getPath();
        String curDir = PathExtractor.getDirectory(curPath);
        ImageTagManager.getInstance().removeImage(curPath);
        String extension = PathExtractor.getExtension(curPath);
        String newPathString = curDir + newImageName + extension;
        if (!imageFile.renameTo(new File(newPathString))) {
            System.out.println("File renaming failed.");
        } else {
            LogUtility.getInstance().log(Level.INFO, "Changed name from: " +  this.getImageName() + "  -->  " + newImageName,
                    false );
            imageFile = new File(newPathString);
            imageName = newImageName;
            ImageTagManager.getInstance().addImage(this);
            ImageTagManager.getInstance().refreshNameToTags();
            /*try {
                ImageTagManager.getInstance().saveToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }

    }


    public TagManager getTagManager() {
        return tagManager;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;

        Image image = (Image) o;

        return imageFile.equals(image.imageFile);
    }
}
