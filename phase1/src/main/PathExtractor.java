package main;

public class PathExtractor {
    public static String getImageName(String imagePath) {
        return imagePath.substring(imagePath.lastIndexOf('/') + 1);
    }

    // Extracts the last directory from the path name.
/*    public static String getSubdirectoryName(String imagePath) {
        int indexOfLastSlash = imagePath.lastIndexOf('/');
        return imagePath.substring(indexOfLastSlash + 1,
                imagePath.length());
    }*/

    // Extracts the extension from the path name.
    public static String getExtension(String imagePath) {
        int indexOfLastPeriod = imagePath.lastIndexOf('.');
        return imagePath.substring(indexOfLastPeriod);
    }

    // Extracts the full directory from the path name.
    public static String getDirectory(String imagePath) {
        int indexOfLastSlash = imagePath.lastIndexOf('/');
        return imagePath.substring(0, indexOfLastSlash + 1);
    }
}
