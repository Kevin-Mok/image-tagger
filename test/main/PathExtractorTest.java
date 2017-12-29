package main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathExtractorTest {
    private String testPathString;

    @BeforeEach
    void setUp() {
        testPathString = "/h/u3/c7/05/mokkar/258/258-labs-self/l6/l6.pdf";
    }

    @Test
    void getImageName() {
        Assertions.assertEquals("l6", PathExtractor.getImageName
                (testPathString));
    }

    @Test
    void getImageFileName() {
        assertEquals("l6.pdf", PathExtractor.getImageFileName(testPathString));
    }

    @Test
    void getExtension() {
        assertEquals(".pdf", PathExtractor.getExtension(testPathString));
    }

    @Test
    void getDirectory() {
        assertEquals("/h/u3/c7/05/mokkar/258/258-labs-self/l6",
                PathExtractor.getDirectory(testPathString));
    }

    @Test
    void getPathWithoutTags() {
        testPathString = "/home/kevin/Pictures/RTJ2 Icon @RTJ2.jpg";
        assertEquals("/home/kevin/Pictures/RTJ2 Icon.jpg",
                PathExtractor.getPathWithoutTags(testPathString));
    }

    @Test
    void getPathWithoutTagsButNoTags() {
        testPathString = "/home/kevin/Pictures/RTJ2 Icon.jpg";
        assertEquals("/home/kevin/Pictures/RTJ2 Icon.jpg",
                PathExtractor.getPathWithoutTags(testPathString));
    }

}