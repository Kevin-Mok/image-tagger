package main.wrapper;

import main.PathExtractor;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class representing a directory
 */
public class DirectoryWrapper implements ItemWrapper {

    /**
     * List of ItemWrappers representing the contents of this directory, only
     * includes subdirectories or images
     */
    private List<ItemWrapper> childObjects;

    /**
     * The path of this directory
     */
    private File path;

    /**
     * Constructor for this DirectoryWrapper
     *
     * @param path path of the directory
     */
    public DirectoryWrapper(File path) {
        this.path = path;
        this.childObjects = new ArrayList<>();
    }

    /**
     * Returns a list of ItemWrapper objects representing the directories and
     * images under this directory
     *
     * @return list of directories and images under this directory
     */
    public List<ItemWrapper> getChildObjects() {
        return childObjects;
    }

    /**
     * Add a subdirectory/image to this directory
     *
     * @param item the directory/image to be added
     */
    public void addToDirectory(ItemWrapper item) {
        this.childObjects.add(item);
    }

    /**
     * Returns the path of this directory as a Path object
     *
     * @return the path of this directory as a Path object
     */
    @Override
    public Path getPath() {
        if (path == null) {
            return null;
        }
        return this.path.toPath();
    }

    /**
     * Returns the path string of this directory
     *
     * @return the path string of this directory
     */
    @Override
    public String toString() {
        return PathExtractor.getImageFileName(path.getPath());
    }

    /**
     * A DirectoryWrapper is equal to this one if all its fields are equal to
     * this one's
     *
     * @param o the object to be compared to
     * @return true if o is equal to this, false if otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectoryWrapper that = (DirectoryWrapper) o;

        if (childObjects != null) {
            if (that.childObjects == null) {
                return false;
            } else {
                for (ItemWrapper wrapper : this.childObjects) {
                    if (wrapper instanceof DirectoryWrapper) {
                        if (!that.getChildObjects().contains(wrapper)) {
                            return false;
                        }
                    } else {
                        if (!that.getChildObjects().contains(wrapper)) {
                            return false;
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return path != null ? path.equals(that.path) : that.path == null;
    }

    /**
     * Calculates the hash code for this object using its fields
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        int result = childObjects != null ? childObjects.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }
}
