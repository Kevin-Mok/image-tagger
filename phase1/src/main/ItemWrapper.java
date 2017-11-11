package main;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for displaying a TreeItem, representing either a directory or a Picture object
 */
public abstract class ItemWrapper {
    /**
     * True if this object represents a directory, if false then it represents an image
     */
    private boolean isDirectory;

    private boolean isEmptyDirectory = false;

    public ItemWrapper(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public abstract Path getPath();

}
