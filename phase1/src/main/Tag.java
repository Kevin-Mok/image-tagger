package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Tag {
    private static HashMap<String, ArrayList<Image>> allTags;
    private Image image;
    private String name;

    public Tag (Image image, String name){
        this.Image = Image;
        this.name = name;
        update(this);
    }

    private static void update(Tag tag){
        ArrayList<Image> list;
        if (allTags.containsKey(tag.getName())){
            list = allTags.get(tag.getName());
            list.add(tag.getImage());
        }
        else{
            list = new ArrayList<Image>();
            list.add(tag.getImage());
            allTags.put(tag.getName(), list);
        }
    }

    public String getName(){
        return name;
    }

    public Image getImage(){
        return image;
    }

    public boolean equals(Object obj){
        if (obj instanceof Tag){
            return ((Tag) obj).getName()  == this.getName();
        }
        return false;
    }


}
