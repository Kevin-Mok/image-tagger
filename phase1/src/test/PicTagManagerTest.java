package test;

import main.PicTagManager;
import main.Picture;
import main.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PicTagManagerTest {
    PicTagManager manager;
    Picture pic;
    Tag addedTag;

    @BeforeEach
    void setUp() {
        pic = new Picture(new File("test1.jpg"), "test1");
        manager = new PicTagManager(pic.getImageName(), pic);
        addedTag = new Tag(pic, "yooo");
    }

    @Test
    void addTagToNewPicture() {
        assertEquals("test1 @yooo", manager.addTag(addedTag));
        // Code below for testing Picture class that actually renames files.
        // File newFile = new File("test1 @yooo.jpg");
        // Picture newPic = new Picture(newFile, "test1 @yooo");
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