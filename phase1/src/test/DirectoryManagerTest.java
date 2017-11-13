package test;

import main.DirectoryManager;
import main.PathExtractor;
import main.Picture;
import main.wrapper.DirectoryWrapper;
import main.wrapper.ItemWrapper;
import main.wrapper.PictureWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectoryManagerTest {
    DirectoryManager manager;
    String testDirectory;
    Path testDirPath;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        String projectDirectory = System.getProperty("user.dir");
        testDirectory = projectDirectory + File.separator + "src" + File
                .separator + "test";
        testDirPath = Paths.get(testDirectory);

        manager = new DirectoryManager(null);
        manager.setRootFolder(new DirectoryWrapper(testDirPath.toFile()));
    }

    @org.junit.jupiter.api.Test
    void getAllImagesUnderRoot() throws IOException {
        File testImageOne = new File(testDirectory + File.separator +
                "test1.jpg");
        testImageOne.createNewFile();
        PictureWrapper imageOne = new PictureWrapper(
                new Picture(testImageOne, PathExtractor.getImageFileName(
                        (testImageOne).toString())));

        File testImageTwo = new File(testDirectory + File.separator +
                "test2.jpg");
        testImageTwo.createNewFile();
        PictureWrapper imageTwo = new PictureWrapper(
                new Picture(testImageTwo, PathExtractor.getImageFileName(
                        (testImageTwo).toString())));

        ItemWrapper rootDirectory = manager.getAllImagesUnderRoot();
        assertTrue(rootDirectory instanceof DirectoryWrapper);
        DirectoryWrapper testwrapper = new DirectoryWrapper(testDirPath
                .toFile());
        testwrapper.getChildObjects().add(imageOne);
        testwrapper.getChildObjects().add(imageTwo);

        assertEquals(testwrapper, (DirectoryWrapper) rootDirectory);
        //assertion fails, suspect it's because of the way Lists are checked
        // for equality
    }

}