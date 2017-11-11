package main;

import java.nio.file.Path;

/**
 * Wrapper class for a Picture object
 */
public class PictureWrapper extends ItemWrapper {

    /**
     * The Picture object that this wrapper encapsulates. Null if isDirectory
     */
    private Picture picture;

    public PictureWrapper(Picture picture) {
        super(false);
        this.picture = picture;
    }

    public Picture getPicture() {
        return picture;
    }

    @Override
    public Path getPath() {
        return this.picture.getPath();
    }

    @Override
    public String toString() {
        return this.picture.toString();
    }
}
