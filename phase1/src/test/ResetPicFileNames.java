package test;

import main.PathExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResetPicFileNames {
    public static void main(String[] args) {
        String dirString = "/home/kevin/Pictures";
        File dir = new File(dirString);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream
                (dir.toPath())) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    String curPathString = path.toString();
                    File curFile = new File(curPathString);
                    File renamedFile = new File(PathExtractor
                            .getPathWithoutTags(curPathString));
                    if (!curFile.renameTo(renamedFile)) {
                        System.out.println("File renaming failed.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
