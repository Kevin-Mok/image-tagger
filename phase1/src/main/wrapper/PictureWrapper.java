package main.wrapper;

import main.Picture;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PictureWrapper that = (PictureWrapper) o;

        return picture != null ? picture.equals(that.picture) : that.picture
                == null;
    }

    @Override
    public int hashCode() {
        return picture != null ? picture.hashCode() : 0;
    }
}
