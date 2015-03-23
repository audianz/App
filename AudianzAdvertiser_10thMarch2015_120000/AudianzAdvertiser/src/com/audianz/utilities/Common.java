package com.audianz.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

import com.audianz.core.Engine;

/**
 * The Common class provides common static function which can be used any module
 * in the application.
 * 
 * @author 
 * 
 */
public class Common {

	public static boolean networkFlag = true;
	public static String pin = "";
	public static TelephonyManager telephonyManager = null ;
	public static int sortOrderType = 0;


	/**
	 * This method is used to check the availability of the network.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean isNet = false;
		if (context == null) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				Engine.engObj.eLogger
				.error("isNetworkAvailable() : null context");
			return isNet;
		}
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager != null) {
			if (connManager.getActiveNetworkInfo() != null
					&& connManager.getActiveNetworkInfo().isConnected()) {
				isNet = true;
			} else {
				if(connManager.getActiveNetworkInfo() == null)
				{
					if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
						Engine.engObj.eLogger.error("Common isNetworkAvailable() connManager.getActiveNetworkInfo is null...");
				}
				else if(!connManager.getActiveNetworkInfo().isConnected())
				{
					if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
						Engine.engObj.eLogger.error("Common isNetworkAvailable() connManager.getActiveNetworkInfo is not connected...");
				}
			}
		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				Engine.engObj.eLogger
				.error("isNetworkAvailable() : null connManager");
		}
		return isNet;
	}

	// validating email id
	public static  boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	// validating password
	public static boolean isValidPassword(String pass) {
		if (pass != null && pass.length() >0) {
			return true;
		}
		return false;
	}
	public static boolean isValidMobile(String mob) {
		if (mob != null && ((mob.length() >= 10) && (mob.length() <= 14))) {
			return true;
		}
		return false;
	}

	public static boolean isValidName(String name) {
		//return name.matches( "[A-Z][a-zA-Z]*" );
		if(name!=null && name.length()>0)
			return true;
		else
			return false;
	}

	/**
	 * This method is used to get DeviceID which is unique
	 * @param context
	 * @return deviceID
	 */
	public static String getDeviceID(Context context) 
	{
		if(telephonyManager==null)
		{
			telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		}			
		//Getting Device ID
		String deviceID = telephonyManager.getDeviceId(); 
		return deviceID ;
	}

	/**
	 * This method is used to get software version
	 * @param context
	 * @return
	 */
	public static String getSWVersion(Context context)
	{
		if(telephonyManager==null)
		{
			telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		}		
		// Getting software version( not sdk version 
		String software_version = telephonyManager.getDeviceSoftwareVersion();
		return software_version;
	}

	/**
	 * This method is used to get NetworkOperator
	 * @param context
	 * @return
	 */
	public static String getNWOperator(Context context)
	{
		if(telephonyManager==null)
		{
			telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		}
		// Getting the connected network operator ID
		String network_operator_id = telephonyManager.getNetworkOperator();
		return network_operator_id;
	}

	/**
	 * This method is used to get opertor name
	 * @param context
	 * @return
	 */
	public static String getOperatorName(Context context)
	{
		if(telephonyManager==null)
		{
			telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		}		
		// Getting the connected network operator name
		String network_name = telephonyManager.getNetworkOperatorName();
		return network_name;
	}
}
