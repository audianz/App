package com.audianz.utilities;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.audianz.beans.CampaignListBean;
import com.audianz.beans.CampaignStatBean;
import com.audianz.beans.PlanResponseBean;
import com.audianz.beans.UpdateCampaignResponse;
import com.audianz.core.Engine;
import com.audianz.database.CampaignStatTable;
import com.audianz.database.CampaignTable;
import com.audianz.database.Database;
import com.audianz.database.PromotePlan;
import com.audianz.emcl.ELogger;
import com.google.android.gms.internal.be;


public class AudianzDatabaseUtility {

	private static AudianzDatabaseUtility auDbUtil = null;
	private Database dbObj = null;
	SQLiteDatabase sqlDbObj = null;
	private SQLiteStatement insertPromotePlanTableStmt;
	private SQLiteStatement insertCampaignTableStmt;
	private SQLiteStatement insertStatTableStmt;
	public ELogger elogger=null;
	private Engine engObj=null;


	private ArrayList<PlanResponseBean> planArray=null;
	private ArrayList<CampaignListBean> campBeanArr = null;
	private ArrayList<CampaignStatBean> statBeanArr = null;
	private String fetchPlanquery = "Select * FROM "+PromotePlan.TABLE_NAME;
	private String fetchCampaignQuery ="Select * FROM "+CampaignTable.TABLE_NAME+"  WHERE "+CampaignTable.CLIENT_ID+"=";
	private String fetchStatQuery ="Select * FROM "+CampaignStatTable.TABLE_NAME+"  WHERE "+CampaignTable.CLIENT_ID+"=";

	private static final String insertPromotePlanTableSQL = "insert into "
			+PromotePlan.TABLE_NAME
			+ "("
			+PromotePlan.ID
			+","
			+PromotePlan.VIEWERS
			+","
			+ PromotePlan.INR_PRICE
			+","
			+ PromotePlan.MESSAGE
			+","
			+PromotePlan.FREE_PLAN
			+ ") values (?,?,?,?,?)";

	private static final String insertStatTableSQL = "INSERT INTO "
			+CampaignStatTable.TABLE_NAME
			+ "("
			+CampaignStatTable.DATE
			+","
			+CampaignStatTable.CLIENT_ID
			+","
			+ CampaignStatTable.IMPRESSION
			+","
			+ CampaignStatTable.CLICK
			+","
			+ CampaignStatTable.CALL
			+","
			+ CampaignStatTable.WEB
			+","
			+ CampaignStatTable.MAP
			+ ") values (?,?,?,?,?,?,?)";


	private static final String insertCampaignTableSQL = "INSERT INTO "
			+CampaignTable.TABLE_NAME
			+"("
			+CampaignTable.CAMPAIGN_ID
			+","
			+CampaignTable.CLIENT_ID
			+","
			+CampaignTable.CAMPAIGN_NAME
			+","
			+CampaignTable.PROMO_MSG
			+","
			+CampaignTable.START_DATE
			+","
			+CampaignTable.END_DATE
			+","
			+CampaignTable.TOTAL_IMP
			+","
			+CampaignTable.SHOWN_IMP
			+","
			+CampaignTable.CLICKS
			+","
			+CampaignTable.CALL
			+","
			+CampaignTable.WEB
			+","
			+CampaignTable.MAP
			+","
			+CampaignTable.CAMPAIGN_STATUS
			+","
			+CampaignTable.ORDER_ID
			+","
			+CampaignTable.ORDER_AMOUNT
			+","
			+CampaignTable.LATITUDE
			+","
			+CampaignTable.LONGITUDE
			+","
			+CampaignTable.ACTION
			+") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


	public boolean init(Context context, ELogger logger)
	{
		if(context ==null)
			return false;
		if(logger==null)
			elogger = new ELogger();
		else
			elogger=logger;
		elogger.setTag("AudianzDatabaseUtils");
		engObj = Engine.engObj;
		dbObj= engObj.dbObj;

		insertPromotePlanTableStmt=dbObj.sqLiteDb.compileStatement(insertPromotePlanTableSQL);
		insertCampaignTableStmt  = dbObj.sqLiteDb.compileStatement(insertCampaignTableSQL);
		insertStatTableStmt      = dbObj.sqLiteDb.compileStatement(insertStatTableSQL);
		return true;
	}

	public static AudianzDatabaseUtility getInstance()
	{
		if(auDbUtil==null)
			auDbUtil = new AudianzDatabaseUtility();
		return auDbUtil;

	}
	/**
	 * Public constructor
	 */
	private AudianzDatabaseUtility()
	{

	}

	/**
	 * This method is used to insert  campaign data of advertiser
	 * @param cmpList
	 */
	public void insertCampaignValue(ArrayList<CampaignListBean> cmpList)
	{

		if (Engine.IS_DEVELOPMENT_RELEASE)
			elogger.debug("insertCampaignValue() : entered ");

		if (cmpList != null) {

			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				elogger.debug("insertCampaignValue() : campList is not null ");

			try {

				// First empty table

				String deleteQuery = "DELETE FROM "+CampaignTable.TABLE_NAME;
				engObj.dbObj.sqLiteDb.execSQL(deleteQuery);

				engObj.dbObj.sqLiteDb.beginTransaction();


				for (CampaignListBean bean : cmpList) {

					int cmp_id		  		= bean.getCamp_id();
					int client_id			= bean.getClientid();
					String cmp_name         = bean.getCamp_name();
					String promo_msg        = bean.getPromo_msg();
					long start_date         = bean.getStart_date();
					long end_date           = bean.getEnd_date();
					int total_imp			= bean.getTotal_imp();
					int shown_imp			= bean.getShown_imp();
					int clicks			    = bean.getClicks();
					int call			    = bean.getCall();
					int web                 = bean.getWeb();
					int map					= bean.getMap();
					int cmp_status			= bean.getCamp_status();
					String order_id         = bean.getOrder_id();
					int order_amount        = bean.getOrder_amount();
                    String lat              = bean.getLat();
                    String lon              = bean.getLon();
                    String action			= bean.getAction();

					insertCampaignTableStmt.bindLong(1,cmp_id);
					insertCampaignTableStmt.bindLong(2,client_id);
					if(cmp_name!=null)
						insertCampaignTableStmt.bindString(3,cmp_name);
					else
						insertCampaignTableStmt.bindNull(3);

					if(promo_msg!=null)
						insertCampaignTableStmt.bindString(4,promo_msg);
					else
						insertCampaignTableStmt.bindNull(4);
					insertCampaignTableStmt.bindLong(5,start_date);
					insertCampaignTableStmt.bindLong(6,end_date);
					insertCampaignTableStmt.bindLong(7,total_imp);
					insertCampaignTableStmt.bindLong(8,shown_imp);
					insertCampaignTableStmt.bindLong(9,clicks);
					insertCampaignTableStmt.bindLong(10,call);
					insertCampaignTableStmt.bindLong(11,web);
					insertCampaignTableStmt.bindLong(12,map);
					insertCampaignTableStmt.bindLong(13,cmp_status);
					if(order_id!=null)
                        insertCampaignTableStmt.bindString(14, order_id);
					else
						insertCampaignTableStmt.bindNull(14);
					insertCampaignTableStmt.bindLong(15,order_amount);
					if(lat!=null)
						insertCampaignTableStmt.bindString(16,lat);
					else
						insertCampaignTableStmt.bindNull(16);
					if(lon!=null)
						insertCampaignTableStmt.bindString(17,lon);
					else
						insertCampaignTableStmt.bindNull(17);
					if(action!=null)
						insertCampaignTableStmt.bindString(18,action);
					else
						insertCampaignTableStmt.bindNull(18);
					
					
					insertCampaignTableStmt.executeInsert();
				}

			} catch (Exception e) {

				if (Engine.IS_DEVELOPMENT_RELEASE)
					elogger.error("insertCampaignValue() : error while inserting  value ");
				e.printStackTrace();
			} finally {
				try {
					engObj.dbObj.sqLiteDb.setTransactionSuccessful();
					engObj.dbObj.sqLiteDb.endTransaction();
				} catch (Exception e) {
					if(Engine.IS_DEVELOPMENT_RELEASE)
						elogger.error("insertCampaignValue() exception "+e.getMessage());
				}
			}

		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				elogger.error("insertCampaignValues() : null bean ");
		}

	}

	/**
	 * This method is used to fetch campaign list 
	 * @return
	 */
	public ArrayList<CampaignListBean> getCampaignList(int clientid)
	{
		if(campBeanArr!=null && campBeanArr.size()>0)
		{
			campBeanArr.clear();
		}
		String query=null;
		if(clientid!=0)
		      query= fetchCampaignQuery+clientid;
         
		String orderBy = " ORDER BY "+CampaignTable.CAMPAIGN_ID+"  DESC";

		Cursor cur = null;
		try{
			cur = engObj.dbObj.sqLiteDb.rawQuery((query+orderBy), null);
		}
		catch(Exception e)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				elogger.error("getCampaignList caught exception "+e);
		}

		ArrayList<CampaignListBean> cmpBeans = convertCampaignCursorToBean(cur);

		if(cur!=null)
		{
			cur.close();
			cur =null;
		}
		return cmpBeans;
	}
	/**
	 * This method is used to insert Statistics data
	 * @param planBean
	 */
	public void insertStatValue( ArrayList<CampaignStatBean> stBean)
	{

		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			elogger.debug("insertStatValue : entered ");

		if (stBean != null) {

			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				elogger.debug("insertStatValue() : bean not null ");

			try {

				String deleteQuery = "DELETE FROM "+CampaignStatTable.TABLE_NAME;
				engObj.dbObj.sqLiteDb.execSQL(deleteQuery);

				engObj.dbObj.sqLiteDb.beginTransaction();


				for (CampaignStatBean cBean : stBean) {

					long date		  			= cBean.getDate();
					int clientid				= cBean.getClientid();
					int imp         			= cBean.getImp();
					int click         			= cBean.getClick();
					int call        			= cBean.getCall();
					int web         			= cBean.getWeb();
					int map         			= cBean.getMap();

					insertStatTableStmt.bindLong(1,date);
					insertStatTableStmt.bindLong(2,clientid);
					insertStatTableStmt.bindLong(3,imp);
					insertStatTableStmt.bindLong(4,click);
					insertStatTableStmt.bindLong(5,call);
					insertStatTableStmt.bindLong(6,web);
					insertStatTableStmt.bindLong(7,map);

					insertStatTableStmt.executeInsert();

				}

			} catch (Exception e) {

				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					elogger.error("insertStatValue : error while inserting  value ");
				e.printStackTrace();
			} finally {
				try {
					engObj.dbObj.sqLiteDb.setTransactionSuccessful();
					engObj.dbObj.sqLiteDb.endTransaction();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				elogger.error("insertStatValue() : null bean ");
		}

	}

	public ArrayList<CampaignStatBean> getStatList()
	{
		if(statBeanArr!=null && statBeanArr.size()>0)
			return statBeanArr;

		String orderBy = " ORDER BY "+CampaignStatTable.DATE+" DESC";

		Cursor cur = null;
		try{
			cur = engObj.dbObj.sqLiteDb.rawQuery((fetchStatQuery+orderBy), null);
		}
		catch(Exception e)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				elogger.error("getStatList() caught exception "+e);
		}

		ArrayList<CampaignStatBean> statBeans = convertStatCursorToBean(cur);

		if(cur!=null)
		{
			cur.close();
			cur =null;
		}
		return statBeans;


	}

	private ContentValues campValues = new ContentValues();
	String[] campaignidArg = new String[1];
	String whereCampaignId = CampaignTable.CAMPAIGN_ID+" = ?";

	public int updateCampaignTable(UpdateCampaignResponse campBean) 
	{
		int rowCount = 0;
		try
		{
			dbObj.sqLiteDb.beginTransaction();
			if(campBean!=null)
			{
				if(campBean.getCamp_status()!=-1)
				{
					campValues.put(CampaignTable.CAMPAIGN_STATUS, campBean.getCamp_status());
					campaignidArg[0] = String.valueOf(campBean.getCampaignid());
					if(dbObj.sqLiteDb.update(CampaignTable.TABLE_NAME, campValues, whereCampaignId,campaignidArg)>0)
					{
						if(Engine.IS_DEVELOPMENT_RELEASE)
							elogger.debug("updateCampaignTable()  updated successfully");
					}
					else
					{
						if(Engine.IS_DEVELOPMENT_RELEASE)
							elogger.error("updateCampaignTable() update Failed");
					}
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

	/**
	 * This method is used to insert plan value in table
	 */

	public void insertPlanValue( ArrayList<PlanResponseBean> planBean)
	{

		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			elogger.debug("insertPlanValue() : entered ");

		if (planBean != null) {

			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				elogger.debug("insertPlanValue() : taskBean is not null ");

			try {

				String deleteQuery = "DELETE FROM "+PromotePlan.TABLE_NAME;
				engObj.dbObj.sqLiteDb.execSQL(deleteQuery);

				engObj.dbObj.sqLiteDb.beginTransaction();


				for (PlanResponseBean pBean : planBean) {

					int id		  			= pBean.getId();
					int viewers				= pBean.getViewer();
					int inr_price			= pBean.getInr_price();
					String msg				= pBean.getMessage();
					int free_plan           = pBean.getFree_plan();

					insertPromotePlanTableStmt.bindLong(1, id);
					insertPromotePlanTableStmt.bindLong(2, viewers);
					insertPromotePlanTableStmt.bindLong(3, inr_price);
					insertPromotePlanTableStmt.bindString(4, msg);
					insertPromotePlanTableStmt.bindLong(5, free_plan);
					insertPromotePlanTableStmt.executeInsert();
				}

			} catch (Exception e) {

				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					elogger.error("insertPlanValues() : error while inserting  value ");
				e.printStackTrace();
			} finally {
				try {
					engObj.dbObj.sqLiteDb.setTransactionSuccessful();
					engObj.dbObj.sqLiteDb.endTransaction();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				elogger.error("insertPlanValues() : null bean ");
		}

	}

	/**
	 * This method is used to fetch PromotionPlan data from DB tabel
	 * @param loginId
	 * @param sortBy
	 * @return
	 */

	public ArrayList<PlanResponseBean> getPlanList() {
		if (planArray != null && planArray.size() > 0) {
			return planArray;
		}

		String orderBy=" ORDER BY "+PromotePlan.ID;

		Cursor cur = null;
		try {

			cur = engObj.dbObj.sqLiteDb.rawQuery((fetchPlanquery+orderBy), null);
		} catch (Exception e) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				elogger.error("getPlanList() : caught exception "+e);
		}

		ArrayList<PlanResponseBean> plans = convertPlanCursorToBean(cur);

		if (cur != null) {
			cur.close();
			cur = null;
		}
		return plans;

	}

	/**
	 * This method is used to convert cusror to Plan bean
	 * @param cur
	 * @return
	 */

	private ArrayList<PlanResponseBean> convertPlanCursorToBean(Cursor cur)
	{
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			elogger.info("convertPlanCursorToBean() : Inside convertPlanCursorToBean method");
		if (cur == null || cur.isClosed() || cur.getCount() == 0) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				elogger.info("convertPlanCursorToBean() : Noting to convert");
			return null;
		}
		planArray = new ArrayList<PlanResponseBean>();
		while (cur.moveToNext()) 
		{
			PlanResponseBean planObject = new PlanResponseBean(cur);
			planArray.add(planObject);
		}

		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			elogger.info("convertPlanCursorToBean() : converting cursor to plan bean size is "
					+ planArray.size());

		return planArray;
	}

	/**
	 * This method is used to convert campaign cursor to bean
	 * @param cur
	 * @return
	 */
	private ArrayList<CampaignListBean> convertCampaignCursorToBean(Cursor cur)
	{
		if(Engine.IS_DEVELOPMENT_RELEASE)
			elogger.debug("Inside convertCampaignCursorToBean () ");
		if(cur == null || cur.isClosed()  || cur.getCount()==0)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				elogger.error("Nothing to convert cur is null");
			return null;
		}

		campBeanArr = new ArrayList<CampaignListBean>(13);
		while(cur.moveToNext())
		{
			CampaignListBean cmp = new CampaignListBean(cur);
			campBeanArr.add(cmp);
		}
		if(Engine.IS_DEVELOPMENT_RELEASE)
			elogger.info("convertCampaignCursorToBean()  bean size is "+campBeanArr.size());
		return campBeanArr;
	}

	private ArrayList<CampaignStatBean> convertStatCursorToBean(Cursor cur)
	{
		if(Engine.IS_DEVELOPMENT_RELEASE)
			elogger.debug("Inside convertStatCursorToBean() ");
		if(cur == null || cur.isClosed()  || cur.getCount()==0)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				elogger.error("Nothing to convert cur is null");
			return null;
		}

		statBeanArr = new ArrayList<CampaignStatBean>(10);
		while(cur.moveToNext())
		{
			CampaignStatBean stat = new CampaignStatBean(cur);
			statBeanArr.add(stat);
		}
		if(Engine.IS_DEVELOPMENT_RELEASE)
			elogger.info("convertStatCursorToBean()  bean size is "+statBeanArr.size());
		return statBeanArr;
	}
}
