package com.audianz.emcl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import android.os.Environment;
import android.util.Log;

/**
 * This class provides the api for saving the log on sdcard and also display the
 * log in logcat
 * 
 * @author vinay_kumar_baghel
 * 
 */
public class ELogger {

	private String tag;
	private static boolean isInit = false;
	private static String fileName = null;
	private static File logFile = null;
	private static final long FILE_SIZE = 100 * 1024 * 1024; // 1 MB = 1024*1024
																// byte

	public static final int VERBOSE = 0;
	public static final int DEBUG = 1;
	public static final int INFO = 2;
	public static final int WARNING = 3;
	public static final int ERROR = 4;

	private static int label = DEBUG;

	/**
	 * This is static method, for initializing for variable of ELogger. This
	 * method takes file name as argument. If file name is passed then this
	 * class saves the log message in given file name on device SDCARD. If file
	 * name not provided then this class print the log only in logcat.
	 * 
	 * @param logFileName
	 * @return return's true if ELogger successfully initialized otherwise false
	 */
	synchronized public static boolean init(String logFileName) {

		if (isInit) {
			if (logFile != null) {
				fileName = null;
				logFile = null;
			}
			isInit = false;
		}

		if (logFileName != null) {
			fileName = logFileName;
			String status = Environment.getExternalStorageState();
			if (!status.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
				return false;
			}
			logFile = new File(Environment.getExternalStorageDirectory(), fileName);
			if (logFile != null) {
				if (!logFile.exists()) {
					try {
						logFile.createNewFile();
					} catch (IOException e) {
						logFile = null;
						e.printStackTrace();
						return false;
					}
					isInit = true;
					return true;

				}
				if (logFile.length() >= FILE_SIZE) {
					if (backUpLog()) {

					}

				}

			} else {
				return false;
			}
		} else {
			return false;
		}

		isInit = true;
		return true;
	}

	static public void close() {
		fileName = null;
		logFile = null;
		isInit = false;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void info(String msg) {
		Log.i(tag, msg);
		msg = "[" + "INFO" + "] " + msg;
		if (logFile != null && label <= INFO) {
			saveLogToFile(msg);
		}

	}

	public void debug(String msg) {
		Log.d(tag, msg);
		msg = "[" + "DEBUG" + "] " + msg;
		if (logFile != null && label <= DEBUG) {
			saveLogToFile(msg);
		}
	}
	
	public void fatal(String msg)
	{
		Log.e(tag, msg);
	}

	public void error(String msg) {
		Log.e(tag, msg);
		msg = "[" + "ERROR" + "] " + msg;
		if (logFile != null && label <= ERROR) {
			saveLogToFile(msg);
		}
	}

	public void warn(String msg) {
		Log.w(tag, msg);
		msg = "[" + "WARNING" + "] " + msg;
		if (logFile != null && label <= WARNING) {
			saveLogToFile(msg);
		}
	}

	public void verbose(String msg) {
		Log.v(tag, msg);
		msg = "[" + "VERBOSE" + "] " + msg;
		if (logFile != null && label <= VERBOSE) {
			saveLogToFile(msg);
		}
	}

	/**
	 * This method takes the backup of log file if the log file size is greater
	 * than 100MB
	 * 
	 * @return true if backup taken otherwise false
	 */
	synchronized private static boolean backUpLog() {
		String backupFileName = fileName + "_backup";
		File backUpFile = new File(Environment.getExternalStorageDirectory(), backupFileName);
		
		if (backUpFile.exists()) {

			backUpFile.delete();

		}

		logFile.renameTo(backUpFile);
		logFile = new File(Environment.getExternalStorageDirectory(), fileName);
		try {
			logFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * This method saves the log message into specified file name. It takes
	 * message as parameter
	 * 
	 * @param message
	 *            to be saved
	 */
	synchronized private void saveLogToFile(String msg) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestr = fmt.format(System.currentTimeMillis());
		long threadId = Thread.currentThread().getId();
		String logStr = "[" + timestr + "]" + " " + "[" + threadId + "]" + "  "
				+ tag + "  " + msg;

		if (logFile.length() >= FILE_SIZE) {

			if (backUpLog()) {

			} else {
				// TODO
			}

		}

		BufferedWriter buf = null;
		try {

			buf = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(logFile, true)),
					8192);
			if (buf != null) {
				buf.append(logStr);
				buf.newLine();
				buf.flush();
				buf.close();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// System.out.println("saveLogToFile(): Problem in writing to File on SD Card");
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to set the log levels.
	 * 
	 * @param logLevel
	 */
	public static void setLogLevel(int logLevel) {
		label = logLevel;
	}

}
// end of class ELogger