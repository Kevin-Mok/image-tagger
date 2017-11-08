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
        String testDir = "/h/u7/c7/05/shyichin/csc207/groupProject/group_0485/phase1/src/test";
        Path subDir = Paths.get(testDir + "/sub");
        Files.createDirectory(subDir);

        File imgInTestDir = new File(testDir + "/test1.jpg");
        imgInTestDir.createNewFile();

        File imgInSubDir = new File(subDir.toString() + "/test2.jpg");
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