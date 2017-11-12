package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Tag {
    private Picture picture;
    private String name;

    public Tag(Picture picture, String name) {
        this.picture = picture;
        this.name = name;
        TagManager.getInstance().update(this);
    }

    String getName() {
        return name;
    }

    public Picture getPicture() {
        return picture;
    }

    public boolean equals(Object obj) {
        return obj instanceof Tag && Objects.equals(((Tag) obj).getName(),
                this.getName());
    }

}
