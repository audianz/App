package com.audianz.constants;

public class APIConstant {

	public final static String CALL_ACTION  = "Call Me";
	public final static String WEB_ACTION   = "Visit My Website";
	public final static String MAP_ACTION   = "Visit My Store";
	
	
	public final static int CLICK_TO_CALL  = 1;
	public final static int CLICK_TO_WEB   = 2;
	public final static int CLICK_TO_MAP   = 3;
	
	public final static String ACTION_CLICK_TO_CALL = "call";
	public final static String ACTION_CLICK_TO_WEB  = "web";
	public final static String ACTION_CLICK_TO_MAP  = "map";
	
	public final static int STATUS_RUNNING    = 0;
	public final static int STATUS_PAUSED     = 1;
	public final static int STATUS_AWAITING   = 2;
	public final static int STATUS_COMPLETE   = 3;
	public final static int STATUS_UNPAID     = 4;
	public final static int STATUS_COMPLIANCE = 5;
	
	
	
	public final static int  CAMPAIGN_TAG_KEY =1;
	public final static int  CAMPAIGN_TAG_GROUP =2;
	
	
	public static final String CAMPAIGN_MSG_KEY = "campaign_msg_key";
	public static final String CAMPAIGN_LAT     = "campaign_lat";
	public static final String CAMPAIGN_LON     = "campaign_lon";
	public static final String CURRENT_TAB ="current_tab";
	public static final int REPORT_TAB =1;
	
	
	// This lat/lon is used to display map when user location is null
	public static double DELHI_LATITUDE = 28.6100;
	public static double DELHI_LONGITUDE = 77.2300;
	
	//Server Status Constants
	
	public static String STATUS_SUCCESS = "SUCCESS";
	public static String STATUS_ERROR   = "ERROR";
	public static int INVALID_EMAIL     = 108;
	public static int EMAIL_SEND_FAILED = 109;
	// Paytm PG constants
	
	public static final String MERCHANT_ID              = "audian85126526309368";  //"Audian53591216301431";
    public static final String CHANNEL_ID               = "WAP";
    public static final String INDUSTRY_TYPE_ID         = "Retail112";  //"Retail";
    public static final String WEBSITE                  = "audian";  //"AudianzN";
    public static final String JAVASTHEME               = "javas";
    public static final String MERCHANTTHEME            = "merchant";
	public static final String checkSumGenerationURL    = "http://my.audianz.com/paytm/generateChecksum.php";
    public static final String checkSumVerificationURL  = "http://my.audianz.com/paytm/verifyChecksum.php";
    
    
    // Bundle Key Strings
    public static final String CAMPAIGN_ID_KEY = "CAMPAIGN_ID_KEY";
    public static final String TOTAL_IMP_KEY = "TOTAL_IMP_KEY";
    public static final String START_DATE_KEY = "START_DATE_KEY";
    public static final String CALL_KEY = "CALL_KEY";
    public static final String CAMPAIGN_STATUS_KEY  = "CAMPAIGN_STATUS_KEY";
	public static final String WEB_KEY = "WEB_KEY";
	public static final String MAP_KEY = "MAP_KEY";
	public static final String SHOW_IMP_KEY = "SHOW_IMP_KEY";
	public static final String ACTION_KEY = "ACTION_KEY";
	public static final String WEB_URL_KEY = "WEB_URL_KEY";
}
