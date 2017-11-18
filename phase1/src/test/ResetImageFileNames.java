package test;

import main.PathExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Resets all pictures in dirString to their original names. Will probably be
 * remade if we get rid of the original file name and have it serialized in the
 * nameStore map in ImageTagManager.
 */
public class ResetImageFileNames {
    // Recursively rename images in passed in directory.
    private static void renameImagesInDir(String dirString) {
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
                } else {
                    renameImagesInDir(path.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // String dirString = "/home/kevin/Pictures";
        String dirString = "/h/u3/c7/05/mokkar/Downloads";
        renameImagesInDir(dirString);

        // String imagesSerString = "../../images.ser";
        // String tagsSerString = "../../tags.ser";
        // Files.delete(Paths.get(imagesSerString));
        // Files.delete(Paths.get(tagsSerString));
    }

}
