package main.wrapper;

import main.Image;

import java.nio.file.Path;

/**
 * Wrapper class for a Image object
 */
public class ImageWrapper extends ItemWrapper {

    /**
     * The Image object that this wrapper encapsulates. Null if isDirectory
     */
    private Image image;

    public ImageWrapper(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public Path getPath() {
        return this.image.getPath();
    }

    @Override
    public String toString() {
        return this.image.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageWrapper that = (ImageWrapper) o;

        return image != null ? image.equals(that.image) : that.image
                == null;
    }

    @Override
    public int hashCode() {
        return image != null ? image.hashCode() : 0;
    }
}
