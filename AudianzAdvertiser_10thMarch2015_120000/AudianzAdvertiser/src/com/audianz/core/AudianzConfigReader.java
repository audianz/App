package com.audianz.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.audianz.emcl.ELogger;

/**
 * The AudianzConfigReader class is used read and write data to configuration
 * file. When Merchant application is launched we check the configuration file
 * to check login status of user.
 * 
 * 
 * 
 */
public class AudianzConfigReader {
	private static AudianzConfigReader configReader = null;
	private Context appContext=null;
	private ELogger elogger =null;
	public final String LOG_TAG = "AudianzLog";
	

	public String userLoginId = "";

	
	public static final String ENINOV = "Eninov";
	
	 public final long SPLASH_DURATION = 1000;
	
	 
	 // identifier
	 private SharedPreferences.Editor editor;
	 public Integer deviceHeight;
	 public Integer deviceWidth;
	 public String screenType;
	 public String deviceType;
	 private SharedPreferences settings = null;
	 public static String CONFIG_FILE = "audianz.db";
	 private Location MY_LOCATION = null;
//	 public final long MAX_DOWNLOAD_DIR_SIZE = 100 * 1048576; // 100MB
//	 public final long MIN_DOWNLOAD_DIR_FREESPACE = 10 * 1048576; // 10MB
	 public static boolean isSyncPermitted = false;

	 
	 private final String AUDIANZ_CLIENT_ID="audianz_client_id";
	 private final String AUDIANZ_BUSINESS_NAME="audianz_business_name";
	 private final String LATITUDE   = "latitude";
	 private final String LONGITUDE  = "longitude";
	
	 
	 private AudianzConfigReader() {

	 }

	 /**
	  * This method is used to get the configuration reader object.
	  * 
	  * @return
	  */
	 public static AudianzConfigReader getInstance() {
		 if (configReader == null)
			 configReader = new AudianzConfigReader();
		 return configReader;
	 }

	 public boolean init(Context context, ELogger logger) {
		 
		 appContext=context;
		 if(logger==null)
		 {
			 elogger = new ELogger();
		 }
		 elogger=logger;
		 if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			 logger.info("ConfigurationReader : init() , creating config file");
		 settings = context.getSharedPreferences(CONFIG_FILE, 0);
		 editor = settings.edit();
		 deviceType = getDensity(context);
		 screenType = getScreenSize();
		 //setAppSecureKey("b2ff398f8db492c19ef89b548b04889c");
	//	 setClientCMVersion(clientVersion);
		 return true;
	 }

	/* public void setClientCMVersion(String clientVer) {
		 editor.putString(CLIENT_CM_VERSION, clientVer);
		 editor.commit();
	 }

	

	
	 public String getClientCMVersion() {
		 return settings.getString(CLIENT_CM_VERSION, null);
	 }
*/
	
	 public static void createNomediaFile(String filePath) {

		 File myFile = new File(filePath, ".nomedia");
		 try {
			 if (!myFile.exists())
				 myFile.createNewFile();
		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
	 }

	 public boolean isExternalStorageAvailable() {
		 String state = Environment.getExternalStorageState();
		 if (Environment.MEDIA_MOUNTED.equals(state)) {
			 // We can read and write the media
			 return true;
		 }
		 // Media storage not present
		 return false;
	 }

	 public SharedPreferences getSettings() {
		 return settings;
	 }

	 public void clearPreferences() {
		 editor.clear();
		 editor.commit();
	 }


	
	 private String getDensity(Context paramContext) {

		 String str = "hdpi";
		 switch (paramContext.getResources().getDisplayMetrics().densityDpi) {
		 case DisplayMetrics.DENSITY_LOW:
			 str = "ldpi";
			 break;
		 case DisplayMetrics.DENSITY_MEDIUM:
			 str = "mdpi";
			 break;
		 case DisplayMetrics.DENSITY_HIGH:
			 str = "hdpi";
			 break;

		 }
		 return str;
	 }

	 public String getResourceValue(int paramInt) {
		 return appContext.getString(paramInt);
	 }

	 public String getScreenSize() {
		 String str;
		 if ((0xF & appContext.getResources().getConfiguration().screenLayout) == 3)
			 str = "largescreen";
		 if ((0xF & appContext.getResources().getConfiguration().screenLayout) == 2) {
			 str = "normalscreen";
		 }
		 if ((0xF & appContext.getResources().getConfiguration().screenLayout) == 1) {
			 str = "smallscreen";
		 }
		 str = "none";
		 return str;
	 }

	/* private static Typeface typeFaceBold = null;

	 public Typeface getTypeFaceBold(Context context) {
		 if (typeFaceBold == null) {
			 typeFaceBold = Typeface.createFromAsset(context.getAssets(),
					 "fonts/Polaris_V2_Bold.otf");
		 }
		 return typeFaceBold;
	 }

	 private static Typeface typeFaceBook = null;

	 public Typeface getTypeFaceBook(Context context) {
		 if (typeFaceBook == null) {
			 typeFaceBook = Typeface.createFromAsset(context.getAssets(),
					 "fonts/Polaris_V2_Book.otf");
		 }
		 return typeFaceBook;
	 }

	 private static Typeface typeFaceLight = null;

	 public Typeface getTypeFaceLight(Context context) {
		 if (typeFaceLight == null) {
			 typeFaceLight = Typeface.createFromAsset(context.getAssets(),
					 "fonts/Polaris_V2_Light.otf");
		 }
		 return typeFaceLight;
	 }

	 private static Typeface typeFaceMedium = null;

	 public Typeface getTypeFaceMedium(Context context) {
		 if (typeFaceMedium == null) {
			 typeFaceMedium = Typeface.createFromAsset(context.getAssets(),
					 "fonts/Polaris_V2_Medium.otf");
		 }
		 return typeFaceMedium;
	 }
*/

	 public boolean isHDPIDevice() {
		 Log.i(LOG_TAG, "hdpi");
		 Log.i(LOG_TAG, "hdpimsg " + deviceType);
		 return deviceType.equals("hdpi");
	 }

	 public boolean isLDPIDevice() {
		 Log.i(LOG_TAG, "ldpi");
		 Log.i(LOG_TAG, "ldpimsg " + deviceType);
		 return deviceType.equals("ldpi");
	 }

	 public boolean isLargeScreen() {
		 return screenType.equals("largescreen");
	 }

	 public boolean isMDPIDevice() {
		 Log.i(LOG_TAG, "mdpi");
		 Log.i(LOG_TAG, "mdpimsg " + deviceType);
		 return deviceType.equals("mdpi");
	 }

	 public boolean isNormalScreen() {
		 return screenType.equals("normalscreen");
	 }

	 public boolean isSmallScreen() {
		 return screenType.equals("smallscreen");
	 }

	 public void removePreference(String paramString) {
		 editor.remove(paramString);
		 editor.commit();
	 }

	 public void savePreferences() {
		 editor.commit();
	 }


	/* public String getCurrentLocation(Context context) {
		 Location location = getGPS(context);
		 if (location != null) {
			 return getLocation(context, location.getLatitude(),
					 location.getLongitude(), null);
		 } else {
			 if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				 elogger.info("getCurrentLocation() : Location is found null");
		 }
		 return "";
	 }

	 public Location getGPS(Context context) {
		 LocationManager lm = (LocationManager) context
				 .getSystemService(Context.LOCATION_SERVICE);
		 Criteria criteria = new Criteria();
		 String bestProvider = lm.getBestProvider(criteria, false);
		 return lm.getLastKnownLocation(bestProvider);
	 }

	 public String getLocation(Context context, double latitude,
			 double longitude, Locale locale) {
		 List<Address> addresses = getFromLocation(context, latitude, longitude,
				 1, locale);
		 if (addresses != null && addresses.size() > 0) {
			 return addresses.get(0).getLocality();
		 } else {
			 if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				 elogger.info("getLocation() : Address is found null");
		 }
		 return "";
	 }

	 public List<Address> getFromLocation(Context context, double latitude,
			 double longitude, int maxResults, Locale locale) {
		 if (locale == null) {
			 locale = Locale.getDefault();
		 }
		 Geocoder gcd = new Geocoder(context, locale);
		 try {
			 return gcd.getFromLocation(latitude, longitude, maxResults);
		 } catch (IOException e) {
			 if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				 elogger.error("getFromLocation() : Exception is " + e);
		 }
		 return new ArrayList<Address>();
	 }
*/
	

	 public Location getMY_LOCATION() {
		 return MY_LOCATION;
	 }

	 public void setMY_LOCATION(Location my_location) {
		 MY_LOCATION = my_location;
	 }

	 // returns locale in format : en_US
	 public String getCurrentLocale() {
		 return java.util.Locale.getDefault().toString();
	 }

	 public int getAUDIANZ_CLIENT_ID() {
		 return settings.getInt(AUDIANZ_CLIENT_ID, 0);
	 }
	 public void setAUDIANZ_CLIENT_ID(int id)
	 {
		 editor.putInt(AUDIANZ_CLIENT_ID, id);
		 editor.commit();
	 }

	public String getAUDIANZ_BUSINESS_NAME() {
		return settings.getString(AUDIANZ_BUSINESS_NAME, null);
	}
	 
	public void setAUDIANZ_BUSINESS_NAME(String name)
	{
		editor.putString(AUDIANZ_BUSINESS_NAME, name);
		editor.commit();
	}

	public void setLATITUDE(double lat)
	{
		editor.putString(LATITUDE, String.valueOf(lat));
		editor.commit();
	}
	public void setLONGITUDE(double lon)
	{
		editor.putString(LONGITUDE, String.valueOf(lon));
		editor.commit();
	}
	public String getLATITUDE() {
		return settings.getString(LATITUDE, null);
	}

	public String getLONGITUDE() {
		return settings.getString(LONGITUDE, null);
	}
	 
}
