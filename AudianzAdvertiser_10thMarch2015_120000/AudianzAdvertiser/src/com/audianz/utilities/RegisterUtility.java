package com.audianz.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.audianz.beans.RegistrationResponseBean;
import com.audianz.constants.NetworkResponseCode;
import com.audianz.core.Engine;
import com.audianz.database.Database;
import com.audianz.database.Registration;
import com.audianz.emcl.ELogger;


public class RegisterUtility {


	private static RegisterUtility regUtil=null;
	private Database dbObj =null;
	SQLiteDatabase sqldbObj=null;
	public SQLiteStatement insertRegisterTableStmt;
	public ELogger elogger=null;
	private Engine engObj=null;
	private RegistrationResponseBean beanObj = null;
	private String query="Select * from "+Registration.TABLE_NAME+" where "+Registration.CLIENT_ID+"=";


	private static final String insertRegisterTableSQL = "insert into "
			+Registration.TABLE_NAME
			+ "("
			+Registration.CLIENT_ID
			+","
			+Registration.BUSINESS_NAME
			+","
			+ Registration.NAME
			+","
			+ Registration.EMAIL_ID
			+","
			+ Registration.PASSWORD
			+","
			+ Registration.MOBILE
			+","
			+ Registration.ADDRESS
			+","
			+ Registration.CITY
			+","
			+ Registration.STATE
			+","
			+ Registration.ZIP
			+","
			+ Registration.WEBSITE
			+ ") values (?,?,?,?,?,?,?,?,?,?,?)";



	public boolean init(Context context, ELogger logger) {
		if (context == null)
			return false;
		if (logger == null)
			elogger = new ELogger();
		else
			elogger = logger;
		elogger.setTag("RegisterUtils");
		engObj = Engine.engObj;
		dbObj = Engine.engObj.dbObj;
		insertRegisterTableStmt = dbObj.sqLiteDb
				.compileStatement(insertRegisterTableSQL);
		return true;
	}

	public RegisterUtility() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method is used to return instance of ProfileUtils
	 * @return
	 */
	public static RegisterUtility getInstance() {
		if (regUtil == null)
			regUtil = new RegisterUtility();
		return regUtil;
	}

	/**
	 * This method is used to insert profile data in Register Table
	 * @param profileBeans
	 */

	public void insertRegisterValue(RegistrationResponseBean regBean)
	{
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			elogger.debug("insertProfileValue() : entered ");
		if(regBean!=null)
		{
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				elogger.debug("insertRegisterValue() : regBean is not null ");

			try{

				
				String deleteQuery = "DELETE FROM "+Registration.TABLE_NAME+" WHERE "+Registration.CLIENT_ID+" ="+regBean.getClientid();
				engObj.dbObj.sqLiteDb.execSQL(deleteQuery);
				
				engObj.dbObj.sqLiteDb.beginTransaction();


				int clientId           		= regBean.getClientid();
				String business_name        = regBean.getBusiness_name();      
				String name    				= regBean.getName();
				String emailId 				= regBean.getEmailid() ;
				String password             = regBean.getPassword();
				String mobile     			= regBean.getMobile();
				String address     			= regBean.getAddress();
				String city   				= regBean.getCity();
				String state 			    = regBean.getState() ;
				String zip 	         	   =  regBean.getZip();
                String website             = regBean.getWebsite();
                
				insertRegisterTableStmt.bindLong(1,clientId);
				if(business_name!=null)
					insertRegisterTableStmt.bindString(2,business_name);
				else
					insertRegisterTableStmt.bindNull(2);
				if(name!=null)
					insertRegisterTableStmt.bindString(3, name);
				else
					insertRegisterTableStmt.bindNull(3);

				insertRegisterTableStmt.bindString(4, emailId);

				if(password!=null)
					insertRegisterTableStmt.bindString(5, password);
				else
					insertRegisterTableStmt.bindNull(5);

				insertRegisterTableStmt.bindString(6, mobile);
				if(address!=null)
					insertRegisterTableStmt.bindString(7,address);
				else
					insertRegisterTableStmt.bindNull(7);

				if(city!=null)
					insertRegisterTableStmt.bindString(8,city);
				else
					insertRegisterTableStmt.bindNull(8);

				if(state!=null)
					insertRegisterTableStmt.bindString(9,state);
				else
					insertRegisterTableStmt.bindNull(9);

				if(zip!=null)
					insertRegisterTableStmt.bindString(10,zip);
				else
					insertRegisterTableStmt.bindNull(10);
				if(website!=null)
					insertRegisterTableStmt.bindString(11, website);
				else
					insertRegisterTableStmt.bindNull(11);

				insertRegisterTableStmt.executeInsert();
			}
			catch(Exception e)
			{
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					elogger.error("RegisterUtility:: insertRegisterValue() : error while inserting  value ");
				e.printStackTrace();
			}
			finally
			{
				try
				{
					engObj.dbObj.sqLiteDb.setTransactionSuccessful();
					engObj.dbObj.sqLiteDb.endTransaction();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			if (Engine.IS_DEVELOPMENT_RELEASE)
				elogger.error("insertRegisterValues() : null bean ");
		}

	}


	public RegistrationResponseBean getUserProfile(int clientid) {
		if(NetworkResponseCode.IS_LOGIN)
			beanObj =null;

		if (beanObj != null ) {
			return beanObj;
		}

		Cursor c = getUserData(clientid);
		c.moveToFirst();
		RegistrationResponseBean userData = convertRegisterCursorToBean(c);

		if (c != null) {
			c.close();
			c = null;
		}
		NetworkResponseCode.IS_LOGIN=false;
		return userData;

	}

	public Cursor getUserData(int clientid) {


		Cursor cur = null;
		try {
			sqldbObj = engObj.dbObj.sqLiteDb;
			cur = sqldbObj.rawQuery((query +clientid), null);
		} catch (Exception e) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				elogger.error("getUserData() : caught exception " + e);
		}
		return cur;
	}

	/**
	 * This method is used to convert cursor data to bean
	 * @param cur
	 * @return
	 */
	private RegistrationResponseBean convertRegisterCursorToBean(Cursor cur) {
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			elogger.info("Inside convertTaskCursorToBean method");
		if (cur == null || cur.isClosed() || cur.getCount() == 0) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				elogger.info("convertRegisterCursorToBean() : Noting to convert");
			return null;
		}

		beanObj =new RegistrationResponseBean(cur);

		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			elogger.info("convertTaskCursorToBean() : converting cursor to Register bean");

		return beanObj;
	}

	
	/**
	 * This method is used to update user information
	 * @param pBeanList
	 * @return
	 */

	private ContentValues userValues = new ContentValues();
	String[] clientidArg = new String[1];
	String clientWhere = Registration.CLIENT_ID+" = ?";
	public int updateRegisterTable(RegistrationResponseBean regBean) 
	{
		int rowCount = 0;
		try
		{
			dbObj.sqLiteDb.beginTransaction();

			if(regBean!=null)
			{
				if(regBean.getBusiness_name()!=null)
					userValues.put(Registration.BUSINESS_NAME, regBean.getBusiness_name());

				if(regBean.getName()!=null)
					userValues.put(Registration.NAME, regBean.getName());

				if(regBean.getAddress()!=null)
					userValues.put(Registration.ADDRESS, regBean.getAddress());

				if(regBean.getCity()!=null)
					userValues.put(Registration.CITY, regBean.getCity());

				if(regBean.getState()!=null)
					userValues.put(Registration.STATE, regBean.getState());
				if(regBean.getZip()!=null)
					userValues.put(Registration.ZIP,regBean.getZip());

				if(regBean.getPassword()!=null)
					userValues.put(Registration.PASSWORD, regBean.getPassword());

				if(regBean.getMobile()!=null)
					userValues.put(Registration.MOBILE, regBean.getMobile());
				
				if(regBean.getWebsite()!=null)
					userValues.put(Registration.WEBSITE,regBean.getWebsite());
				
				clientidArg[0] = String.valueOf(regBean.getClientid());
				
				if(dbObj.sqLiteDb.update(Registration.TABLE_NAME, userValues, clientWhere, clientidArg)>0)
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						elogger.debug("updateRegisterTable() update successfull");
				}
				else
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						elogger.error("updateRegisterTable()  update failed");
				}

			}

		}
		finally
		{
			dbObj.sqLiteDb.setTransactionSuccessful();
			dbObj.sqLiteDb.endTransaction();
		}
		return rowCount;
	}


}
