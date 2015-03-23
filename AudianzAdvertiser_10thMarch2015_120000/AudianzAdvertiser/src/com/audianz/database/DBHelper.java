package com.audianz.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The DBHelper class is used to create the schema of the Merchant App database.
 * All database tables are created here.
 * @author Shyam
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "MERCHANT_DB";
	public static String FNAME;
	public static String LNAME;
	public static String TABLE_NAME;
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null,1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {


		db.execSQL("CREATE TABLE " 
				+ Registration.TABLE_NAME     + "("
				+ Registration.CLIENT_ID      + " INTEGER,"
				+ Registration.BUSINESS_NAME  + " TEXT,"
				+ Registration.NAME           + " TEXT,"
				+ Registration.EMAIL_ID       +" TEXT,"
				+ Registration.PASSWORD       +" TEXT,"
				+ Registration.MOBILE         + " TEXT,"
				+ Registration.ADDRESS        + " TEXT,"
				+ Registration.CITY           + " TEXT,"
				+ Registration.STATE          + " TEXT, "
				+ Registration.ZIP            + " TEXT, "
				+ Registration.WEBSITE        + " TEXT "
				+ ");");
		
		db.execSQL("CREATE TABLE " 
				+ PromotePlan.TABLE_NAME     + "("
				+ PromotePlan.ID             + " INTEGER,"
				+ PromotePlan.VIEWERS 		 + " INTEGER,"
				+ PromotePlan.INR_PRICE      + " INTEGER,"
				+ PromotePlan.MESSAGE        + " TEXT, "
				+PromotePlan.FREE_PLAN       + " INTEGER "
				+ ");");
		
		db.execSQL("CREATE TABLE "
				+CampaignTable.TABLE_NAME       + "("
				+CampaignTable.CAMPAIGN_ID      + " INTEGER,"
				+CampaignTable.CLIENT_ID        + " INTEGER,"
				+CampaignTable.CAMPAIGN_NAME    + " TEXT,"
				+CampaignTable.PROMO_MSG        + " TEXT,"
				+CampaignTable.START_DATE       + " INTEGER,"
				+CampaignTable.END_DATE         + " INTEGER,"
				+CampaignTable.TOTAL_IMP        + " INTEGER,"
				+CampaignTable.SHOWN_IMP        + " INTEGER,"
				+CampaignTable.CLICKS           + " INTEGER,"
				+CampaignTable.CALL             + " INTEGER,"
				+CampaignTable.WEB              + " INTEGER,"
				+CampaignTable.MAP              + " INTEGER,"
				+CampaignTable.CAMPAIGN_STATUS  + " INTEGER, "
				+CampaignTable.ORDER_ID         + " TEXT, "
				+CampaignTable.ORDER_AMOUNT     + " INTEGER, "
				+CampaignTable.LATITUDE         + " TEXT, "
				+CampaignTable.LONGITUDE        + " TEXT,"
				+CampaignTable.ACTION 	        + " TEXT"
				+ ");");
		
		db.execSQL("CREATE TABLE "
				+CampaignStatTable.TABLE_NAME    + "("
				+CampaignStatTable.DATE          +" INTEGER,"
				+CampaignStatTable.CLIENT_ID     + " INTEGER,"
				+CampaignStatTable.IMPRESSION    + " INTEGER,"
				+CampaignStatTable.CLICK         + " INTEGER,"
				+CampaignStatTable.CALL          + " INTEGER,"
				+CampaignStatTable.WEB           + " INTEGER,"
				+CampaignStatTable.MAP           + " INTEGER"
				+ ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

		db.execSQL("DROP TABLE IF EXISTS "+Registration.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+PromotePlan.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXIST "+CampaignTable.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXIST "+CampaignStatTable.TABLE_NAME);
		onCreate(db);

	}




}

