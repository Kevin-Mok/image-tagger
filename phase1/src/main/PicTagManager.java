package main;

import java.sql.Timestamp;
import java.util.*;

public class PicTagManager {
    private TreeMap<Timestamp, String> nameStore;
    // List of all tags this picture has ever had.
    private ArrayList<Tag> tagList;
    // Current tags in picture.
    private Set<Tag> currentTags;
    private Picture pic;

    public PicTagManager(String name, Picture pic) {
        nameStore = new TreeMap<>();
        nameStore.put(new Timestamp(System.currentTimeMillis()), name);
        tagList = new ArrayList<>();
        currentTags = new LinkedHashSet<>();
        this.pic = pic;
    }

    /**
     * Adds parameter tag to this tag manager.
     *
     * @param tag Tag to add.
     * @return What the new name of the file should be.
     */
    public String addTag(Tag tag) {
        if (!currentTags.contains(tag)) {
            currentTags.add(tag);
            tagList.add(tag);
            String currentName = nameStore.lastEntry().getValue();
            nameStore.put(new Timestamp(System.currentTimeMillis()),
                    currentName + " @" + tag.getName());
            return nameStore.lastEntry().getValue();
        }
        return nameStore.lastEntry().getValue();
    }

    public String deleteTag(String tagName) {
        Tag tag = new Tag(pic, tagName);
        if (currentTags.contains(tag)) {
            currentTags.remove(tag);
            TagManager.getInstance().delete(tagName, pic);
            nameStore.put(new Timestamp(System.currentTimeMillis()),
                    getCurrentName());
            return nameStore.lastEntry().getValue();
        }
        return nameStore.lastEntry().getValue();
    }

    private String getCurrentName() {
        StringBuilder result = new StringBuilder();
        for (Tag currentTag : currentTags) {
            result.append(currentTag.getName());
        }
        return result.toString();
    }

    public String revertName(String name) {
        if (nameStore.values().contains(name)) {
            nameStore.put(new Timestamp(System.currentTimeMillis()), name);
            rewrite();
        }
        return nameStore.lastEntry().getValue();
    }

    private void rewrite() {
        String name = nameStore.lastEntry().getValue();
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(name
                .split("@")));
        for (int i = 0; i < tags.size(); i++) {
            tags.set(i, tags.get(i).trim());
        }
        tags.remove(0);

        currentTags = new LinkedHashSet<>(returnTagsNeeded(tags));
    }

    // Return tags needed from tagList when reverting name.
    private ArrayList<Tag> returnTagsNeeded(ArrayList<String> names) {
        ArrayList<Tag> tags = new ArrayList<>();
        for (String name : names) {
            for (Tag tag : tagList) {
                if (name.equals(tag.getName())) {
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PicTagManager that = (PicTagManager) o;

        if (nameStore != null ? !nameStore.equals(that.nameStore) : that
                .nameStore != null)
            return false;
        if (tagList != null ? !tagList.equals(that.tagList) : that.tagList !=
                null)
            return false;
        if (currentTags != null ? !currentTags.equals(that.currentTags) :
                that.currentTags != null)
            return false;
        return pic != null ? pic.equals(that.pic) : that.pic == null;
    }

    @Override
    public int hashCode() {
        int result = nameStore != null ? nameStore.hashCode() : 0;
        result = 31 * result + (tagList != null ? tagList.hashCode() : 0);
        result = 31 * result + (currentTags != null ? currentTags.hashCode()
                : 0);
        result = 31 * result + (pic != null ? pic.hashCode() : 0);
        return result;
    }
}



