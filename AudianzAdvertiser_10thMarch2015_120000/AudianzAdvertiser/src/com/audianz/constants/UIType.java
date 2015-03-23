package com.audianz.constants;

public class UIType 
{
	public static final int MINUITYPE = -1;
	public static final int SPLASH_ACTIVITY = 0;
	public static final int START_ACTIVITY = 1;

	public static final int REGISTRATION_ACTIVITY = 2;
	public static final int LOGIN_ACTIVITY = 3;
	public static final int TABHOST_ACTIVITY = 4;
	public static final int MAP_ACTIVITY = 5;
	public static final int SETTING_ACTIVITY = 6;
	public static final int PROMOTENOW_ACTIVITY = 7;
	public static final int PROFILEACTIVITY = 8;
	public static final int UPDATEPROFILEACTIVITY = 9;
	public static final int REPORTACTIVITY = 10;
	public static final int ABOUTACTIVITY = 11;

	public static final int EDITACTIVITY =12;
	public static final int FAQACTIVITY = 13;
	public static final int CONTACTUSACTIVITY=14;
	public static final int THANKYOUACTIVITY=15;
	public static final int FORGET_PASSWORS_ACTIVITY=16;
	public static final int MAXUITYPE = 17;

	private static final String uiPackage = "com.audianz.audianzadvertiser";

	public static String getActivity(int nextState) 
	{
		String activtyName = "";
		switch(nextState)
		{
		case SPLASH_ACTIVITY:
			activtyName = ".SplashActivity";
			break;

		case START_ACTIVITY:
			activtyName = ".StartActivity";
			break;
		case LOGIN_ACTIVITY:
			activtyName = ".LoginActivity";
			break;
		case REGISTRATION_ACTIVITY:
			activtyName = ".RegistrationActivity";
			break;

		case TABHOST_ACTIVITY:
			activtyName = ".TabHostActivity";
			break;

		case SETTING_ACTIVITY:
			activtyName = ".SetttingActivity";
			break;
		case EDITACTIVITY:
			activtyName=".EditActivity";
			break;
		case MAP_ACTIVITY:
			activtyName = ".MapActivity";
			break;
		case PROMOTENOW_ACTIVITY:
			activtyName = ".PromoteNowActivity";
			break;
		case PROFILEACTIVITY:
			activtyName = ".ProfileActivity";
			break;
		case UPDATEPROFILEACTIVITY:
			activtyName = ".UpdateProfileActivity";
			break;
		case REPORTACTIVITY:
			activtyName = ".ReportActivity";
			break;
		case ABOUTACTIVITY:
			activtyName = ".AboutActivity";
			break;
		case FAQACTIVITY:
			activtyName  = ".FAQActivity";
			break;
		case CONTACTUSACTIVITY:
			activtyName = ".ContactUsActivity";
			break;
		case THANKYOUACTIVITY:
			activtyName = ".ThankYouActivity";
			break;
		case FORGET_PASSWORS_ACTIVITY:
			activtyName = ".ForgetPassActivity";
			break;
		}
		return (uiPackage + activtyName);
	}
}
