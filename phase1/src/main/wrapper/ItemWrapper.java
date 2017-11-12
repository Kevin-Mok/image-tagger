package main.wrapper;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for displaying a TreeItem, representing either a directory or a Picture object
 */
public abstract class ItemWrapper {

    public ItemWrapper() {}

    public abstract Path getPath();

}
