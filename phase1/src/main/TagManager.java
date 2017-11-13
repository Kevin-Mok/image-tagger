package main;

import java.util.ArrayList;
import java.util.HashMap;

class TagManager {
    private static TagManager instance = null;
    private HashMap<String, ArrayList<Image>> allTags;

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

    void add(Tag tag) {
        ArrayList<Image> list;
        if (allTags.containsKey(tag.getName())) {
            list = allTags.get(tag.getName());
            list.add(tag.getImage());
        } else {
            list = new ArrayList<>();
            list.add(tag.getImage());
            allTags.put(tag.getName(), list);
        }
    }

    void delete(String tagName, Image image) {
        allTags.get(tagName).remove(image);
    }
}
