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
public class LogUtility {

	/** The logger. */
	private  Logger logger;
	private  Logger imgLogger;
	private static LogUtility logUtility;

	/**
	 * Instantiates a new my logging.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private LogUtility() throws IOException{
		System.setProperty("java.util.logging.SimpleFormatter.format",
				"%1$tF %1$tT [%4$-2s: %5$s] %n");
		//instance the logger
		logger = Logger.getLogger("Current Log: ");
		imgLogger = Logger.getLogger("Current Image Name Change Log: ");
		//instance the filehandler
		Handler fileHandler = new FileHandler("myLog.txt",true);
		Handler imgFileHandler = new FileHandler("nameChangeLog.txt",true);
		//instance formatter, set formatting, and handler
		SimpleFormatter plainText = new SimpleFormatter();
		fileHandler.setFormatter(plainText);
		imgFileHandler.setFormatter(plainText);
		logger.addHandler(fileHandler);
		imgLogger.addHandler(imgFileHandler);
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


	/**
	 * Gets the logger.
	 *
	 * @return the logger
	 */
	private  Logger getLogger(boolean imageOrTag) {
		if (imageOrTag) {
			if (logger == null) {
				try {
					new LogUtility();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return logger;
		}
		else{
			if (imgLogger == null) {
				try {
					new LogUtility();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return imgLogger;
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
