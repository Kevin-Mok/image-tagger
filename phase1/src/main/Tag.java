package main;

import java.util.Objects;

public class Tag {
    private Picture picture;
    private String name;

    public Tag(Picture picture, String name) {
        this.picture = picture;
        this.name = name;
        TagManager.getInstance().add(this);
    }

    String getName() {
        return name;
    }

    public Picture getPicture() {
        return picture;
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

        if (!picture.equals(tag.picture)) return false;
        return name.equals(tag.name);
    }

}
