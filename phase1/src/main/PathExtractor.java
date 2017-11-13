package main;

public class PathExtractor {
    public static String getImageName(String imagePath) {
        return imagePath.substring(imagePath.lastIndexOf('/') + 1, imagePath
                .lastIndexOf('.'));
    }

    public static String getImageFileName(String imagePath) {
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

    // Extracts the full directory from the path name.
    public static String getPathWithoutTags(String imagePath) {
        int indexOfFirstTag = imagePath.indexOf('@');
        if (indexOfFirstTag < 0) {
            return imagePath;
        }
        int indexOfLastPeriod = imagePath.lastIndexOf('.');
        String newPathName = imagePath.substring(0, indexOfFirstTag - 1) +
                imagePath.substring(indexOfLastPeriod);
        return newPathName;
    }

}
