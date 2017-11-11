package test;

import main.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryManagerTest {
    DirectoryManager manager;
    String testDirectory;
    Path testDirPath;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        String projectDirectory = System.getProperty("user.dir");
        testDirectory = projectDirectory + File.separator + "src" + File.separator + "test";
        testDirPath = Paths.get(testDirectory);

        manager = new DirectoryManager(null);
        manager.setRootFolder(new DirectoryWrapper(testDirPath.toFile()));
    }

    @org.junit.jupiter.api.Test
    void getAllImagesUnderRoot() throws IOException {
        File testImageOne = new File(testDirectory + File.separator + "test1.jpg");
        testImageOne.createNewFile();
        PictureWrapper imageOne = new PictureWrapper(
                new Picture(testImageOne, PathExtractor.getImageName((testImageOne).toString())));

        File testImageTwo = new File(testDirectory + File.separator + "test2.jpg");
        testImageTwo.createNewFile();
        PictureWrapper imageTwo = new PictureWrapper(
                new Picture(testImageTwo, PathExtractor.getImageName((testImageOne).toString())));

        ItemWrapper rootDirectory = manager.getAllImagesUnderRoot();
        assertTrue(rootDirectory instanceof DirectoryWrapper);
        DirectoryWrapper testwrapper = new DirectoryWrapper(testDirPath.toFile());
        testwrapper.getChildObjects().add(imageOne);
        testwrapper.getChildObjects().add(imageTwo);

        assertEquals(testwrapper, (DirectoryWrapper)rootDirectory);
    }

}