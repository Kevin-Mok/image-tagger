package main.wrapper;

import java.nio.file.Path;

/**
 * Wrapper class for displaying a TreeItem, representing either a directory
 * or a Image object.
 * Created for type safety reasons (want to avoid using List<Object>)
 */
public interface ItemWrapper {
    /**
     * Should return the Path of whatever this wrapper wraps
     *
     * @return the path of the item within this wrapper
     */
    Path getPath();

}
