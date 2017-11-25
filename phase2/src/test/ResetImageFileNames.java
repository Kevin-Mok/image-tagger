package test;

import main.PathExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

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
                    // String curDirAndFileName = PathExtractor
                    //         .getDirectoryAndFileName(curPathString);
                    File renamedFile = new File(PathExtractor
                            .getPathWithoutTags(curPathString));
                    // String newDirAndFileName = PathExtractor
                    //         .getDirectoryAndFileName(renamedFile.toString());

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

    /**
     * Only for test purposes.
     */
    public static void main(String[] args) {
        /* Reset all image names. */
        // String dirString = "/home/kevin/Pictures";
         String dirString = "/h/u5/c6/05/khans167/Desktop/Images";
        // String dirString = "/h/u3/c7/05/mokkar/Downloads";
//        String dirString = "/h/u3/c7/05/mokkar/207/group_0485/test-images";
        renameImagesInDir(dirString);

        /* Delete ser files. */
//        String serPathString =
//                "/h/u3/c7/05/mokkar/207/group_0485/phase2/";
        String serPathString =
                "/h/u5/c6/05/khans167/group_0485";
        String[] serFileNames = {"images.ser", "tags.ser"};

        boolean sersDeleted = true;
        for (String fileName : serFileNames) {
            File serFile = new File(serPathString + fileName);
            if (!serFile.delete()) {
                sersDeleted = false;
            }
        }

        System.out.println(sersDeleted ? "Ser's deleted." : "Could not delete" +
                " ser's.");
    }

}
