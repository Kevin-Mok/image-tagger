package test;

import main.DirectoryManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryManagerTest {
    DirectoryManager manager;
    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException {
        String projectDirectory = System.getProperty("user.dir");
        String testDirectory = projectDirectory + File.separator + "src" + File.separator + "test";
        Path testDirPath = Paths.get(testDirectory);

        File imgInTestDir = new File(testDirectory + File.separator + "test1.jpg");
        imgInTestDir.createNewFile();

        File imgInSubDir = new File(testDirectory + File.separator + "test2.jpg");
        imgInSubDir.createNewFile();
        // manager = new DirectoryManager();
    }

    @org.junit.jupiter.api.Test
    void getImagesUnderRoot() {

    }

    @org.junit.jupiter.api.Test
    void getAllImagesUnderRoot() {
    }

    @org.junit.jupiter.api.BeforeEach
    void tearDown() {

    }

}