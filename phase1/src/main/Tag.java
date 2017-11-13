package main;

public class Tag {
    private Image image;
    private String name;

    public Tag(Image image, String name) {
        this.image = image;
        this.name = name;
        TagManager.getInstance().add(this);
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

        if (!image.equals(tag.image)) return false;
        return name.equals(tag.name);
    }

}
