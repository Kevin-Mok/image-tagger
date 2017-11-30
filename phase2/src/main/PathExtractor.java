package main;

import java.io.File;

/**
 * Utility class with only static methods that operate on image file paths
 */
public class PathExtractor {

    /**
     * Gets the image name of an image file given a path
     *
     * @param imagePath absolute path of an image file
     * @return the image name without the extension
     */
    static String getImageName(String imagePath) {

        return imagePath.substring(imagePath.lastIndexOf(File.separatorChar)
                + 1, imagePath
                .lastIndexOf('.'));
    }

    /**
     * Gets the name of an image file given a path
     *
     * @param imagePath absolute path of an image file
     * @return the image file name with its file extension
     */
    public static String getImageFileName(String imagePath) {
        return imagePath.substring(imagePath.lastIndexOf(File.separatorChar)
                + 1);
    }

    /**
     * Gets the file extension of an image file given a path
     *
     * @param imagePath absolute path of an image file
     * @return the image file extension
     */
    static String getExtension(String imagePath) {
        int indexOfLastPeriod = imagePath.lastIndexOf('.');
        return imagePath.substring(indexOfLastPeriod);
    }

    /**
     * Gets the directory that an image file is in, given a path
     *
     * @param imagePath absolute path an image file
     * @return the full path of the directory where the image file is located
     */
    public static String getDirectory(String imagePath) {
        int indexOfLastSlash = imagePath.lastIndexOf(File.separatorChar);
        return imagePath.substring(0, indexOfLastSlash);
    }

    /**
     * Gets the path of an image file without any tags, given a path
     *
     * @param imagePath absolute path of an image file
     * @return the full path to the image file, excluding any tags
     */
    public static String getPathWithoutTags(String imagePath) {
        int indexOfFirstTag = imagePath.indexOf('@');
        if (indexOfFirstTag < 0) {
            return imagePath;
        }
        int indexOfLastPeriod = imagePath.lastIndexOf('.');
        return imagePath.substring(0, indexOfFirstTag - 1) + imagePath
                .substring(indexOfLastPeriod);
    }

    /**
     * Gets the original name of the image, without the tags
     *
     * @param path path of the image
     * @return the name of the image without any tags
     */
    static String getOriginalName(String path) {
        return (getImageName(getPathWithoutTags(path)));
    }

}
