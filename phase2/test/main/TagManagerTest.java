package main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TagManagerTest {
    private String originalImageName;
    private Image image;
    private TagManager tagManager;
    private String addedTagName;
    private List<String> multipleTagNames;

    private void setImage() {
        image = new Image(new File(originalImageName + ".jpg"),
                originalImageName);
    }

    @BeforeEach
    void setUp() {
        originalImageName = "test";
        setImage();
        tagManager = image.getTagManager();
        addedTagName = "addedTag";
        multipleTagNames = Arrays.asList("foo bar", "a", "b");
    }

    @AfterEach
    void tearDown() {
        image = null;
    }

    @Test
    void addTag() {
        String newImageFileName = String.format("test @%s", addedTagName);
        assertEquals(newImageFileName, tagManager.addTag(addedTagName));
        assertTrue(tagManager.hasTag(addedTagName));
    }

    @Test
    void addAllExistingTags() {
        StringBuilder imageName = new StringBuilder("test");
        for (String tagName : multipleTagNames) {
            imageName.append(String.format(" @%s", tagName));
        }

        originalImageName = imageName.toString();
        setImage();
        tagManager.addAllExistingTags(image.getImageName().split("@"));
        assertTrue(tagManager.hasTags(multipleTagNames));
    }

    @Test
    void deleteTag() {
        tagManager.addTag(addedTagName);
        assertEquals(originalImageName, tagManager.deleteTag(addedTagName));
    }

    private void addMultipleTags() {
        for (String tagName : multipleTagNames) {
            image.getTagManager().addTag(tagName);
        }
    }

    @Test
    void getNameHistory() {
        addMultipleTags();
        ArrayList<String> nameHistory = image.getTagManager().getNameHistory();
        // Collections.reverse(nameHistory);
        /* Build imageName by adding on tags after checking if the current
        name matches the name history log. */
        StringBuilder imageName = new StringBuilder(originalImageName);
        int tagNameCounter = 0;
        for (String previousName : nameHistory) {
            int indexOfPreviousName = previousName.indexOf('→') + 3;
            assertEquals(imageName.toString(), previousName.substring
                    (indexOfPreviousName));
            if (tagNameCounter < multipleTagNames.size()) {
                imageName.append(String.format(" @%s", multipleTagNames.get
                        (tagNameCounter++)));
            }
        }
    }

    @Test
    void getTagNames() {
        addMultipleTags();
        Collections.sort(multipleTagNames);
        assertEquals(multipleTagNames, tagManager.getTagNames());
    }

    @Test
    void getUnusedTags() {
        addMultipleTags();
        String deletedTagName = multipleTagNames.get(0);
        tagManager.deleteTag(deletedTagName);
        assertEquals(new ArrayList<>(Collections.singletonList(deletedTagName)),
                tagManager.getUnusedTags());
    }

    @Test
    void revertName() {
        addMultipleTags();
        String revertedTagName = multipleTagNames.get(0);
        String revertedName = String.format("%s @%s", originalImageName,
                revertedTagName);
        assertEquals(revertedName, tagManager.revertName(revertedName));
        assertEquals(new ArrayList<>(Collections.singletonList
                (revertedTagName)), tagManager.getTagNames());

        /* Check if deleted tags from reverting are now returned from
        getUnusedTags(). */
        ArrayList<String> deletedTags = new ArrayList<>(multipleTagNames);
        deletedTags.remove(revertedTagName);
        assertEquals(deletedTags, tagManager.getUnusedTags());
    }

}