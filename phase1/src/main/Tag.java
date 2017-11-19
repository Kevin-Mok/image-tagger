package main;

import java.io.Serializable;

/**
 * Represents a tag on an image
 */
public class Tag implements Serializable {
    /**
     * The image this tag is on
     */
    private Image image;
    /**
     * The name of this tag
     */
    private String name;

    /**
     * Constructs a new Tag
     *
     * @param image the image the tag is on
     * @param name  the name of the tag
     */
    public Tag(Image image, String name) {
        this.image = image;
        this.name = name;
    }

    String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    /**
     * A tag is equal to this one if all of its fields are equal to this one's
     *
     * @param o the object to compare to
     * @return true if o is equal to this Tag
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;

        Tag tag = (Tag) o;

        return image.equals(tag.image) && name.equals(tag.name);
    }

    /**
     * Calculates the hash code for this object using its fields
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        int result = image != null ? image.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
