package main;

import java.sql.Timestamp;
import java.util.*;

public class TagManager {
    TreeMap<Timestamp, String> nameStore;
    ArrayList<Tag> tagList;
    // Current tags in picture.
    Set<Tag> currentTags;
    private Picture img;

    public TagManager(String name, Picture img){
        nameStore = new TreeMap<>();
        nameStore.put(new Timestamp(System.currentTimeMillis()), name);
        tagList = new ArrayList<>();
        currentTags = new LinkedHashSet<>();
        this.img = img;
    }

    public String addTag(Tag tag) {
        if (!currentTags.contains(tag)) {
            currentTags.add(tag);
            tagList.add(tag);
            String currentName = nameStore.lastEntry().getValue();
            nameStore.put(new Timestamp(System.currentTimeMillis()), currentName + " " + tag.getName());
            return nameStore.lastEntry().getValue();
        }
        return nameStore.lastEntry().getValue();
    }

    public String deleteTag(String tagName){
        Tag tag = new Tag(img, tagName);
        if(currentTags.contains(tag)){
            currentTags.remove(tag);
            nameStore.put(new Timestamp(System.currentTimeMillis()), getCurrentName());
            return nameStore.lastEntry().getValue();
        }
        return nameStore.lastEntry().getValue();
    }

    private String getCurrentName(){
        String result= "";
        Iterator<Tag> itr =  currentTags.iterator();
        while (itr.hasNext()){
            result += itr.next().getName();
        }
        return result;
    }

    public String revertName(String name){
        if(nameStore.values().contains(name)){
            nameStore.put(new Timestamp(System.currentTimeMillis()), name);
            rewrite();
        }
        return nameStore.lastEntry().getValue();
    }

    public void rewrite(){
        String name = nameStore.lastEntry().getValue();
        ArrayList<String> tags = new ArrayList<String>(Arrays.asList(name.split("@")));
        for (int i =0; i<tags.size(); i++){
            tags.get(i).trim();
        }
        tags.remove(0);

        currentTags = new LinkedHashSet<Tag>(returnTagsNeeded(tags));
    }

    public ArrayList<Tag> returnTagsNeeded(ArrayList<String> names) {
        ArrayList<Tag> tags = new ArrayList<>();
        for (int i =0; i<names.size(); i++){
            for (int w = 0; w<tagList.size(); w++){
                if (names.get(i).equals(tagList.get(w).getName())){
                    tags.add(tagList.get(w));
                }
            }
        }
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagManager that = (TagManager) o;

        if (nameStore != null ? !nameStore.equals(that.nameStore) : that.nameStore != null) return false;
        if (tagList != null ? !tagList.equals(that.tagList) : that.tagList != null) return false;
        if (currentTags != null ? !currentTags.equals(that.currentTags) : that.currentTags != null) return false;
        return img != null ? img.equals(that.img) : that.img == null;
    }

    @Override
    public int hashCode() {
        int result = nameStore != null ? nameStore.hashCode() : 0;
        result = 31 * result + (tagList != null ? tagList.hashCode() : 0);
        result = 31 * result + (currentTags != null ? currentTags.hashCode() : 0);
        result = 31 * result + (img != null ? img.hashCode() : 0);
        return result;
    }
}



