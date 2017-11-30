package main;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Manages anything related to Tags for an Image.
 */
public class TagManager implements Serializable {
    /**
     * The name history of the image this TagManager is associated with
     */
    private TreeMap<Timestamp, String> nameHistory;

    /**
     * List of Tags the image has ever had
     */
    private List<Tag> tagList;
    /**
     * Current tags on the image
     */
    private Set<Tag> currentTags;
    /**
     * The image this TagManager is associated with
     */
    private Image image;

    /**
     * Constructor.
     *
     * @param originalImageName Original name of image without extension.
     * @param image             Image this TagManager will be associated with.
     */
    TagManager(String originalImageName, Image image) {
        nameHistory = new TreeMap<>();
        nameHistory.put(new Timestamp(System.currentTimeMillis()),
                originalImageName);
        tagList = new ArrayList<>();
        currentTags = new LinkedHashSet<>();
        this.image = image;
    }

    /**
     * Adds new Tag with tagName param to this tag manager.
     *
     * @param tagName Name of tag to add.
     * @return What the new name of the file should be.
     */
    String addTag(String tagName) {
        delay();
        Tag tag = new Tag(image, tagName);
        if (!currentTags.contains(tag)) {
            currentTags.add(tag);
            tagList.add(tag);
            ImageTagManager.getInstance().removeFromHidden(tagName);
            String currentName = nameHistory.lastEntry().getValue();
            nameHistory.put(new Timestamp(System.currentTimeMillis()),
                    currentName + " @" + tag.getName());
            LogUtility.getInstance().logAddTag(tag.getName(), image
                    .getImageName());
        }
        return nameHistory.lastEntry().getValue();
    }

    /**
     * Adds multiple tags to the image this TagManager is associated with
     *
     * @param tags an array of tags to be added
     */
    void addAllExistingTags(String[] tags) {
        ArrayList<String> clone = new ArrayList<>(Arrays.asList(tags));
        clone.remove(0);
        for (String tagNames : clone) {
            Tag toAdd = new Tag(image, tagNames.trim());
            currentTags.add(toAdd);
            tagList.add(toAdd);
        }
        ImageTagManager.getInstance().refreshTagToImageList();
    }

    /**
     * Gets the tags currently on the image this TagManager is associated with
     *
     * @return Set of tags currently on the image
     */
    Set<Tag> getCurrentTags() {
        return currentTags;
    }

    /**
     * Checks if the image this TagManager is associated with has all the
     * tags in a list
     *
     * @param tagNames the tag names to check
     * @return true if the image has at least one of the tags, false if
     * otherwise
     */
    boolean hasTags(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            tags.add(new Tag(image, tagName));
        }
        for (Tag tag : tags) {
            if (!this.currentTags.contains(tag)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the image this TagManager is associated with has the given tag
     *
     * @param tagName the name of the tag to check
     * @return true if the image has the aforementioned tag
     */
    boolean hasTag(String tagName) {
        Tag tag = new Tag(image, tagName);
        return this.currentTags.contains(tag);
    }

    /**
     * Deletes Tag with tagName param to this tag manager.
     *
     * @param tagName Name of tag to delete.
     * @return What the new name of the file should be.
     */
    String deleteTag(String tagName) {
        delay();
        Tag tag = new Tag(image, tagName);
        if (currentTags.contains(tag)) {
            currentTags.remove(tag);
            nameHistory.put(new Timestamp(System.currentTimeMillis()),
                    getCurrentName());
            LogUtility.getInstance().logDeleteTag(tagName, image.getImageName
                    ());
        }
        return nameHistory.lastEntry().getValue();
    }

    private void delay() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            System.out.println("Delay between adding tags failed.");
        }
    }

    /**
     * Returns the current name of the image (tags included)
     *
     * @return the current name of the image
     */
    private String getCurrentName() {
        StringBuilder result = new StringBuilder();
        result.append(PathExtractor.getOriginalName((image.getPathString())));
        for (Tag currentTag : currentTags) {
            result.append(" @").append(currentTag.getName());
        }
        return result.toString();
    }

    /**
     * Returns all the names this Image has ever had.
     *
     * @return ArrayList of all the names this Image has ever had.
     */
    public ArrayList<String> getNameHistory() {
        ArrayList<String> result = new ArrayList<>();
        for (Timestamp timestamp : nameHistory.keySet()) {
            String s = new SimpleDateFormat("MM/dd HH:mm:ss").format
                    (timestamp);
            result.add(s + "  →  " + nameHistory.get(timestamp));
        }
        // result.sort(Collections.reverseOrder());
        return result;
    }

    /**
     * Returns of the current tags of this image.
     *
     * @return Alphabetically sorted ArrayList of current tags.
     */
    public ArrayList<String> getTagNames() {
        ArrayList<String> result = new ArrayList<>();
        for (Tag tag : currentTags) {
            result.add(tag.getName());
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Returns all the tags that were once used with this Image but not any
     * more.
     *
     * @return Alphabetically sorted ArrayList of Strings that contain all
     * the tag names that were once used but not any more.
     */
    ArrayList<String> getUnusedTags() {
        ArrayList<String> result = new ArrayList<>();
        for (Tag tag : tagList) {
            if (!currentTags.contains(tag)) {
                result.add(tag.getName());
            }
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Reverts the Image to a previous name in nameHistory.
     *
     * @param name Name to be reverted to.
     * @return New name of Image.
     */
    String revertName(String name) {
        if (nameHistory.values().contains(name)) {
            LogUtility.getInstance().logRevertName(nameHistory.lastEntry()
                    .getValue(), name);
            nameHistory.put(new Timestamp(System.currentTimeMillis()), name);
            updateCurrentTags();
        }
        return nameHistory.lastEntry().getValue();
    }

    // Updates set of current tags based on latest name.
    private void updateCurrentTags() {
        String name = nameHistory.lastEntry().getValue();
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(name
                .split("@")));
        for (int i = 0; i < tags.size(); i++) {
            tags.set(i, tags.get(i).trim());
        }
        tags.remove(0);

        currentTags = new LinkedHashSet<>(returnTagsNeeded(tags));
    }

    /**
     * Return tags needed from tagList when reverting name.
     */
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

    /**
     * Object is equal to this TagManager if it is an instance of a
     * TagManager and all its fields are equal.
     *
     * @param o Object to be compared to.
     * @return Whether this and the object are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagManager that = (TagManager) o;

        if (nameHistory != null ? !nameHistory.equals(that.nameHistory) : that
                .nameHistory != null)
            return false;
        if (tagList != null ? !tagList.equals(that.tagList) : that.tagList !=
                null)
            return false;
        // Ignoring this yellow error for readability.
        if (currentTags != null ? !currentTags.equals(that.currentTags) :
                that.currentTags != null)
            return false;
        return image != null ? image.equals(that.image) : that.image == null;
    }

    /**
     * Returns hash code of this object based on its fields.
     *
     * @return Hashed value.
     */
    @Override
    public int hashCode() {
        int result = nameHistory != null ? nameHistory.hashCode() : 0;
        result = 31 * result + (tagList != null ? tagList.hashCode() : 0);
        result = 31 * result + (currentTags != null ? currentTags.hashCode()
                : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }
}



