package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Tag {
    private static HashMap<String, ArrayList<Picture>> allTags;
    private Picture picture;
    private String name;

    public Tag (Picture picture, String name){
        this.picture = picture;
        this.name = name;
        update(this);
    }

    private static void update(Tag tag){
        ArrayList<Picture> list;
        if (allTags.containsKey(tag.getName())){
            list = allTags.get(tag.getName());
            list.add(tag.getPicture());
        }
        else{
            list = new ArrayList<Picture>();
            list.add(tag.getPicture());
            allTags.put(tag.getName(), list);
        }
    }

    public String getName(){
        return name;
    }

    public Picture getPicture(){
        return picture;
    }

    public boolean equals(Object obj){
        if (obj instanceof Tag){
            return ((Tag) obj).getName()  == this.getName();
        }
        return false;
    }


}
