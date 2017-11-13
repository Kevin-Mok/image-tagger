package main.wrapper;

import java.nio.file.Path;

/**
 * Wrapper class for displaying a TreeItem, representing either a directory
 * or a Picture object
 */
public abstract class ItemWrapper {

    public ItemWrapper() {
    }

    public abstract Path getPath();

}
