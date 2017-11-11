package main;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class representing a directory
 */
public class DirectoryWrapper extends ItemWrapper {

    /**
     * null of this object is not a directory
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
        super(true);
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
}
