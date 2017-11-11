package test;

import main.PathExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathExtractorTest {
    String testPath;

    @BeforeEach
    void setUp() {
        testPath = "/h/u3/c7/05/mokkar/258/258-labs-self/l6/l6.pdf";
    }

    @Test
    void getImageName() {
        assertEquals("l6.pdf", PathExtractor.getImageName(testPath));
    }

    @Test
    void getExtension() {
        assertEquals(".pdf", PathExtractor.getExtension(testPath));
    }

    @Test
    void getDirectory() {
        assertEquals("/h/u3/c7/05/mokkar/258/258-labs-self/l6/",
                PathExtractor.getDirectory(testPath));
    }

}