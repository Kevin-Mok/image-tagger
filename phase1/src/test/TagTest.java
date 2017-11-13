package test;

import main.Image;
import main.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TagTest {
    Tag tag;
    Image pic;
    File picFile;

    @BeforeEach
    void setUp() {
        picFile = new File("test1.jpg");
        pic = new Image(picFile, "test1");
        tag = new Tag(pic, "blah");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void equals() {
        File samePicFile = new File("test1.jpg");
        Image samePic = new Image(samePicFile, "test1");
        Tag compareTag = new Tag(samePic, "blah");
        assertEquals(tag, compareTag);
    }

}