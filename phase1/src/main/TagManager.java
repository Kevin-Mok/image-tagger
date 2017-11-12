package main;

import java.util.ArrayList;
import java.util.HashMap;

class TagManager {
    private static TagManager instance = null;
    private HashMap<String, ArrayList<Picture>> allTags;

    private TagManager() {
        // Exists only to defeat instantiation.
    }

    static TagManager getInstance() {
        if (instance == null) {
            instance = new TagManager();
            instance.allTags = new HashMap<>();
        }
        return instance;
    }

    void update(Tag tag) {
        ArrayList<Picture> list;
        if (allTags.containsKey(tag.getName())) {
            list = allTags.get(tag.getName());
            list.add(tag.getPicture());
        } else {
            list = new ArrayList<>();
            list.add(tag.getPicture());
            allTags.put(tag.getName(), list);
        }
    }
}
