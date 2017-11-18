package main;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Utility class used for logging user interactions with the program
 */
class LogUtility {

	/** The logger. */
	private Logger logger;
	private Logger imgLogger;
	private static LogUtility logUtility;

	/**
	 * Instantiates a new LogUtility.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private LogUtility() throws IOException{
		System.setProperty("java.util.logging.SimpleFormatter.format",
				"%1$tF %1$tT [%4$-2s: %5$s] %n");
		//instance the tagLogger
		tagLogger = Logger.getLogger("Current Log: ");

		//instance the filehandler
		Handler fileHandler = new FileHandler("myLog.txt",true);
		Handler imgFileHandler = new FileHandler("nameChangeLog.txt",true);
		//instance formatter, set formatting, and handler
		SimpleFormatter plainText = new SimpleFormatter();
		fileHandler.setFormatter(plainText);
		imgFileHandler.setFormatter(plainText);
		tagLogger.addHandler(fileHandler);
		renameLogger.addHandler(imgFileHandler);
	}

	public static LogUtility getInstance() {
		if (logUtility == null){
			try {
				return new LogUtility();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return logUtility;
	}

	public static Logger getLogger(boolean getRenameLogger) {
	    String loggerName;
	    String pattern;

	    if ()
    }

	public static Logger getRenameLogger() {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT [%4$-2s: %5$s] %n");
	    if (LogUtility.renameLogger == null) {
            renameLogger = Logger.getLogger("Current Image Name Change Log: ");
            try {
                Handler imgFileHandler = new FileHandler("nameChangeLog.txt",true);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("File not found");
            }
        }
	    return LogUtility.renameLogger;
    }

    public static Logger getTagLogger() {
	    return LogUtility.tagLogger;
    }

	/**
	 * Gets the tagLogger.=======
	 *
	 * @return the tagLogger
	 */
	private  Logger getLogger(boolean imageOrTag) {
		if (imageOrTag) {
			if (tagLogger == null) {
				try {
					new LogUtility();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return tagLogger;
		}
		else{
			if (renameLogger == null) {
				try {
					new LogUtility();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return renameLogger;
		}
	}

	/**
	 * Logs the level and the message.
	 *
	 * @param level the level
	 * @param msg the message to log.
	 */
	public  void log(Level level, String msg, boolean imgOrName) {
		getLogger(imgOrName).log(level, msg);
	}
}
