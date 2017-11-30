package main;

import main.wrapper.DirectoryWrapper;
import main.wrapper.ImageWrapper;
import main.wrapper.ItemWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectoryManagerTest {
    private DirectoryManager manager;
    private String testDirectory;
    private Path testDirPath;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        testDirectory = DirectoryManager.PROJECT_DIR + File.separator + "src" + File
                .separator + "test";
        testDirPath = Paths.get(testDirectory);

        manager = DirectoryManager.getInstance();
        manager.setRootFolder(testDirPath.toFile());
    }

    @org.junit.jupiter.api.Test
    void getAllImagesUnderRoot() throws IOException {
        File testImageOne = new File(testDirectory + File.separator +
                "test1.jpg");
        testImageOne.createNewFile();
        ImageWrapper imageOne = new ImageWrapper(
                new Image(testImageOne, PathExtractor.getImageFileName(
                        (testImageOne).toString())));

        File testImageTwo = new File(testDirectory + File.separator +
                "test2.jpg");
        testImageTwo.createNewFile();
        ImageWrapper imageTwo = new ImageWrapper(
                new Image(testImageTwo, PathExtractor.getImageFileName(
                        (testImageTwo).toString())));

        ItemWrapper rootDirectory = manager.getRootDirectory();
        assertTrue(rootDirectory instanceof DirectoryWrapper);
        DirectoryWrapper testwrapper = new DirectoryWrapper(testDirPath
                .toFile());
        testwrapper.getChildObjects().add(imageOne);
        testwrapper.getChildObjects().add(imageTwo);

        assertEquals(testwrapper, rootDirectory);
        //assertion fails, suspect it's because of the way Lists are checked
        // for equality
    }

}