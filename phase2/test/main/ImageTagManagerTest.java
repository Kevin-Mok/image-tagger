package main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImageTagManagerTest {
    private ImageTagManager imageTagManager;
    private Image[] images;
    /**
     * This array needs to have double the amount of images initialized.
     * Could probably be setup by using ASCII values but hardcoded them in here
     * for simplicity.
     */
    private String[] tagNames = new String[]{"a", "b", "c", "d", "e",
            "f"};
    private String testDir = "test_icons";
    private String testExtension = ".png";

    private Image singleImageToAdd;
    private String singleImagePath;

    private String tagNameToRemove;

    private String placeHolderTagNameToAdd = "ph";
    private List<String> tagsWithCountList;

    /**
     * Initializes each image in the images array to have two tags from the
     * tagNames array.
     */
    private void setUpImages() {
        int numImages = 3;

        imageTagManager = ImageTagManager.getInstance();
        images = new Image[numImages];
        for (int i = 0; i < images.length; i++) {
            String imageName = String.format("image%d", i);
            File imageFile = new File(testDir + "/" + imageName +
                    testExtension);
            Image image = new Image(imageFile, imageName);
            if (i == 0) {
                image.addTag(tagNames[i]);
                image.addTag(tagNames[i + 1]);
            } else {
                image.addTag(tagNames[i * 2]);
                image.addTag(tagNames[i * 2 + 1]);
            }
            images[i] = image;
        }
        System.out.println("Done setting images.");
    }

    private void moveImageBackToTestDir(Image image, String imageName) {
        image.move(testDir, imageName, false);
    }

    private void tearDownImages() {
        for (int i = 0; i < images.length; i++) {
            String imageName = String.format("image%d", i);
            moveImageBackToTestDir(images[i], imageName);
            // images[i].move(testDir, imageName, false);
        }
        System.out.println("Done tearing images.");
    }

    @BeforeEach
    void setUp() {
        setUpImages();
    }

    @AfterEach
    void tearDown() {
        tearDownImages();
    }

    /* Add a single image to the imageTagManager. */
    private void addSingleImage() {
        singleImageToAdd = images[1];
        singleImagePath = singleImageToAdd.getPathString();
        imageTagManager.addImage(singleImageToAdd);
    }

    /* Also tests containsImagePath and getImage. */
    @Test
    void addImage() {
        addSingleImage();
        /* pathToImagesMap should contain path of image and return the image
         when given the path. */
        assertTrue(imageTagManager.containsImagePath(singleImagePath));
        assertEquals(singleImageToAdd, imageTagManager.getImage
                (singleImagePath));
    }

    private void removeTagFromImage(int tagNumberToRemove) {
        tagNameToRemove = tagNames[tagNumberToRemove];
        Image imageToRemoveTagFrom = images[(int) Math.floor
                (tagNumberToRemove / 2)];
        imageToRemoveTagFrom.deleteTag(tagNameToRemove);
    }

    @Test
    void removeImage() {
        addSingleImage();
        imageTagManager.removeImage(singleImagePath);
        assertFalse(imageTagManager.containsImagePath(singleImagePath));
        assertNull(imageTagManager.getImage(singleImagePath));
    }

    private void addTagToPlaceholder() {
        imageTagManager.addTagToPlaceholder(placeHolderTagNameToAdd);
    }

    @Test
    void addTagToPlaceholderTest() {
        addTagToPlaceholder();
        Image placeHolderImage = imageTagManager.getImage(ImageTagManager
                .PLACEHOLDER_IMAGE_NAME);
        assertTrue(placeHolderImage.hasTags(Collections.singletonList
                (placeHolderTagNameToAdd)));
    }

    private void addAllImages() {
        for (Image image : images) {
            imageTagManager.addImage(image);
        }
    }

    private void setTagsWithCountList() {
        tagsWithCountList = Arrays.asList(imageTagManager
                .getAvailableTagsWithCount());
    }

    /**
     * Used for testing when simply adding all tags to all images without any
     * interference.
     */
    private void checkIfAllCountsAreOne() {
        for (String tagName : tagNames) {
            String addedTagCountEntry = String.format("(1) - %s",
                    tagName);
            assertTrue(tagsWithCountList.contains(addedTagCountEntry));
        }
    }

    /* Also tests refreshTagToImageList and getAvailableTagsWithCount. */
    @Test
    void addImagesFromPathMap() {
        addAllImages();
        setTagsWithCountList();
        checkIfAllCountsAreOne();
    }

    /* Also tests refreshTagToImageList and getAvailableTagsWithCount. */
    @Test
    void addUnusedTagsToMap() {
        int tagNumberToRemove = 3;
        removeTagFromImage(tagNumberToRemove);
        setTagsWithCountList();

        /* Since each tag is only added once, removing it should set the
        count to 0. */
        String removedTagCountEntry = String.format("(0) - %s",
                tagNameToRemove);
        assertTrue(tagsWithCountList.contains(removedTagCountEntry));
    }

    /* Also tests refreshTagToImageList and getAvailableTagsWithCount. */
    @Test
    void addPlaceholderTagsToMap() {
        addTagToPlaceholder();
        setTagsWithCountList();

        String placeHolderTagCountEntry = String.format("(0) - %s",
                placeHolderTagNameToAdd);
        /* Placeholder tags should always start off at 0. */
        assertTrue(tagsWithCountList.contains(placeHolderTagCountEntry));
    }

    private void hideTag(int tagNumberToHide) {
        imageTagManager.hideThisTag(tagNames[tagNumberToHide]);
    }

    /**
     * Also tests refreshTagToImageList, getAvailableTagsWithCount and
     * removeFromHidden.
     */
    @Test
    void removeHiddenTagsFromMap() {
        int tagNumberToHide = 1;
        hideTag(tagNumberToHide);
        setTagsWithCountList();

        String hiddenTagName = tagNames[tagNumberToHide];
        boolean countListContainsHidden = false;
        for (String entry : tagsWithCountList) {
            if (entry.contains(hiddenTagName)) {
                countListContainsHidden = true;
            }
        }
        /* The hidden tag should not be in any of the count list entries. */
        assertFalse(countListContainsHidden);

        /* Tests removeFromHidden. */
        int imageNumberToAddTo = 2;
        images[imageNumberToAddTo].addTag(hiddenTagName);
        setTagsWithCountList();

        /* Once the hidden tag is added again, it'll still have the old count
         stored and add to it, hence 2. */
        String hiddenTagCountEntry = String.format("(2) - %s",
                hiddenTagName);
        assertTrue(tagsWithCountList.contains(hiddenTagCountEntry));
    }

    /* Cleans up ser file after testing. */
    private void deleteSerFile() {
        boolean serFileDeleted = new File(ImageTagManager.SER_FILE_NAME)
                .delete();
        String message = String.format("Ser file %sdeleted.", (serFileDeleted)
                ? "" : "could not be ");
        System.out.println(message);
    }

    @Test
    void readWriteToFile() {
        addAllImages();
        imageTagManager.saveToFile();
        imageTagManager.readFromFile();
        setTagsWithCountList();
        checkIfAllCountsAreOne();

        deleteSerFile();
    }

    @Test
    void deleteNonExistentImages() {
        addAllImages();
        imageTagManager.saveToFile();

        int imageNumberToMove = 1;
        Image imageToMove = images[imageNumberToMove];
        String imageToMoveName = imageToMove.getImageName();
        imageToMove.move(DirectoryManager.PROJECT_DIR, imageToMoveName,
                false);
        imageTagManager.readFromFile();
        setTagsWithCountList();
        /* Since each image contains 2 unique tags, moving it and then having
         it be removed should decrease the available tags by 2.  */
        assertEquals(tagNames.length - 2, tagsWithCountList.size());

        /* Cleans up after testing. */
        moveImageBackToTestDir(imageToMove, imageToMoveName);
        deleteSerFile();
    }

    @Test
    void deleteUselessImageObjects() {
        addAllImages();

        /* Add unused image to imageTagManager to test if it'll delete it
        upon saving. */
        String unusedImageName = "image" + (images.length + 1);
        String unusedImagePath = String.format("%s/%s%s", testDir,
                unusedImageName, testExtension);
        File unusedImageFile = new File(unusedImagePath);
        Image unusedImage = new Image(unusedImageFile, unusedImageName);
        imageTagManager.addImage(unusedImage);

        imageTagManager.saveToFile();
        /* Since the unused image had no name history, it should've been
        removed from the HashMap after serialization. */
        assertFalse(imageTagManager.containsImagePath(unusedImagePath));

        deleteSerFile();
    }
}