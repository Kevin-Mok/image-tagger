package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Singleton utility class used for logging user interactions with the program.
 */
class LogUtility {
    private static LogUtility logUtility;
    /**
     * Logs tag related events
     */
    private Logger tagLogger;
    /**
     * Logs renaming events
     */
    private Logger renameLogger;

    /**
     * Instantiates the singleton LogUtility
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private LogUtility() throws IOException {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT [%4$-2s: %5$s] %n");
        tagLogger = this.getLogger(false);
        renameLogger = this.getLogger(true);
    }

    /**
     * Get the singleton instance of LogUtility
     * @return the instance
     */
    static LogUtility getInstance() {
        if (logUtility == null) {
            try {
                logUtility = new LogUtility();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logUtility;
    }

    /**
     * Returns either the renameLogger or the tagLogger
     * @param getRenameLogger true if retrieving the renameLogger
     * @return renameLogger if getRenameLogger is set to true, tagLogger if otherwise
     * @throws IOException if any I/O errors occurred during file reading/writing
     */
    private Logger getLogger(boolean getRenameLogger) throws IOException {
        if (getRenameLogger && this.renameLogger != null) {
            return this.renameLogger;
        }
        if (this.tagLogger != null) {
            return this.tagLogger;
        }

        String loggerName;
        String pattern;

        if (getRenameLogger) {
            loggerName = "Current Image Name Change Log: ";
            pattern = "nameChangeLog.txt";
        } else {
            loggerName = "Current log: ";
            pattern = "myLog.txt";
        }
        Logger logger = Logger.getLogger(loggerName);
        FileHandler handler;
        try {
            handler = new FileHandler(pattern, true);
        } catch (IOException e) {
            System.out.println(pattern + "doesn't exist, creating file now");
            String filePath = System.getProperty("user.dir") + File.separator
                    + pattern;
            try {
                Files.createFile(Paths.get(filePath));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            handler = new FileHandler(pattern, true);
        }

        SimpleFormatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        logger.addHandler(handler);
        return logger;
    }
    /**
     * Logs the level and the message.
     *
     * @param level  the level of the message
     * @param msg    the message to log.
     * @param logTag true if logging a tag related event, false if anything else
     */
    private void log(Level level, String msg, boolean logTag) {
        try {
            getLogger(logTag).log(level, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void logAddOrDeleteTag(String tagName, String imageName, boolean addTag) {
        String message;
        if (addTag) {
            message = String.format("Created new Tag %s for Image %s", tagName, imageName);
        } else {
            message = String.format("Deleted Tag %s from Image %s", tagName, imageName);
        }
        log(Level.INFO, message, true);
    }

    void logRevertName(String oldName, String newName) {
        String message = String.format("Reverted name from %s to %s", oldName, newName);
        log(Level.INFO, message, true);
    }

    void logImageRename(String oldName, String newName) {
        String message = String.format("Changed name from %s to %s", oldName, newName);
        log(Level.INFO, message, false);
    }
}
