package main;

import java.io.Serializable;

public class Tag implements Serializable {
    private Image image;
    private String name;

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

/*    public boolean equals(Object obj) {
        return obj instanceof Tag && Objects.equals(((Tag) obj).getName(),
                this.getName());
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;

        Tag tag = (Tag) o;

        return image.equals(tag.image) && name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        int result = image != null ? image.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
