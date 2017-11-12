package main;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Picture class that stores its path and a PicTagManager object to work with
 * its tags.
 */
public class Picture {
    private File imageFile;
    private PicTagManager tagManager;
    private String imageName;

    private static Map<String, Picture> pathToPictureObjects = new HashMap<>();

    /**
     * Constructor.
     *
     * @param imageFile File object of the image.
     * @param fileName  The name of the file (without its extension).
     */
    public Picture(File imageFile, String fileName) {
        this.imageFile = imageFile;
        this.imageName = fileName;
        tagManager = new PicTagManager(fileName, this);
    }

    public Path getPath() {
        return imageFile.toPath();
    }

    public static Picture pictureLookup(String path) {
        return pathToPictureObjects.get(path);
    }

    public static void addPictureObject(String path, Picture picture) {
        pathToPictureObjects.put(path, picture);
    }

    @Override
    public String toString() {
        return imageName;
    }

    // Renames the file to the given String.
    void rename(String tags) {
        String curPath = imageFile.getPath();
        String curDir = PathExtractor.getDirectory(curPath);
        String extension = PathExtractor.getExtension(curPath);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Picture picture = (Picture) o;

        if (imageFile != null ? !imageFile.equals(picture.imageFile) : picture.imageFile != null) return false;
        return imageName != null ? imageName.equals(picture.imageName) : picture.imageName == null;
    }

    @Override
    public int hashCode() {
        int result = imageFile != null ? imageFile.hashCode() : 0;
        result = 31 * result + (tagManager != null ? tagManager.hashCode() : 0);
        result = 31 * result + (imageName != null ? imageName.hashCode() : 0);
        return result;
    }
}
