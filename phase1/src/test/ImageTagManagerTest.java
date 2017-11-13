package test;

import main.Image;
import main.ImageTagManager;
import main.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageTagManagerTest {
    ImageTagManager manager;
    Image pic;
    Tag addedTag;

    @BeforeEach
    void setUp() {
        pic = new Image(new File("test1.jpg"), "test1");
        manager = new ImageTagManager(pic.getImageName(), pic);
        addedTag = new Tag(pic, "yooo");
    }

    @Test
    void addTagToNewPicture() {
        assertEquals("test1 @yooo", manager.addTag(addedTag));
        // Code below for testing Image class that actually renames files.
        // File newFile = new File("test1 @yooo.jpg");
        // Image newPic = new Image(newFile, "test1 @yooo");
        // newFile.renameTo(new File("test1.jpg"));
    }

    @Test
    void deleteTag() {
        manager.addTag(addedTag);
        assertEquals("test1", manager.deleteTag("yooo"));
    }

    @Test
    void revertName() {
    }

    @AfterEach
    void tearDown() {
    }
}