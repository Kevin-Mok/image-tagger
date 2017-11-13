package main.wrapper;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class representing a directory
 */
public class DirectoryWrapper extends ItemWrapper {

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
     * Is this directory empty?
     */
    private boolean isEmpty;

    public DirectoryWrapper(File path) {
        this.path = path;
        this.childObjects = new ArrayList<>();
    }

    public List<ItemWrapper> getChildObjects() {
        return childObjects;
    }

    public void addToDirectory(ItemWrapper item) {
        this.childObjects.add(item);
    }

    public boolean isEmptyDirectory() {
        return this.isEmpty;
    }

    public void setEmptyDirectory(boolean emptyDirectory) {
        this.isEmpty = emptyDirectory;
    }

    @Override
    public Path getPath() {
        return this.path.toPath();
    }

    @Override
    public String toString() {
        return this.path.getPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectoryWrapper that = (DirectoryWrapper) o;

        if (isEmpty != that.isEmpty) return false;

        if (childObjects != null) {
            if (that.childObjects == null) {
                return false;
            } else {
                for (ItemWrapper wrapper : this.childObjects) {
                    if (wrapper instanceof DirectoryWrapper) {
                        if (!that.getChildObjects().contains(
                                (DirectoryWrapper) wrapper)) {
                            return false;
                        }
                    } else {
                        if (!that.getChildObjects().contains((ImageWrapper)
                                wrapper)) {
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

    @Override
    public int hashCode() {
        int result = childObjects != null ? childObjects.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (isEmpty ? 1 : 0);
        return result;
    }
}
