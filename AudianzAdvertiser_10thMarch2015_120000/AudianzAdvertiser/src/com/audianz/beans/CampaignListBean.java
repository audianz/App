package com.audianz.beans;

import com.audianz.database.CampaignTable;

import android.database.Cursor;

public class CampaignListBean {

	private int camp_id=0;
	private int clientid =0;
	private String camp_name = null;
	private String promo_msg = null;
	private long start_date =0;
	private long end_date = 0;
	private int total_imp=0;
	private int clicks =0;
	private int shown_imp =0;
	private int call =0;
	private int web =0;
	private int map =0;
	private int camp_status =-1;
	private String order_id=null;
	private int order_amount =0;
	private String lat = null;
	private String lon = null;
	private String action = null;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}

	public int getCamp_id() {
		return camp_id;
	}
	public void setCamp_id(int camp_id) {
		this.camp_id = camp_id;
	}
	public String getCamp_name() {
		return camp_name;
	}
	public void setCamp_name(String camp_name) {
		this.camp_name = camp_name;
	}
	public long getStart_date() {
		return start_date;
	}
	public void setStart_date(long start_date) {
		this.start_date = start_date;
	}
	public int getTotal_imp() {
		return total_imp;
	}
	public void setTotal_imp(int total_imp) {
		this.total_imp = total_imp;
	}
	public int getShown_imp() {
		return shown_imp;
	}
	public void setShown_imp(int shown_imp) {
		this.shown_imp = shown_imp;
	}
	public int getCall() {
		return call;
	}
	public void setCall(int call) {
		this.call = call;
	}
	public int getWeb() {
		return web;
	}
	public void setWeb(int web) {
		this.web = web;
	}
	public int getCamp_status() {
		return camp_status;
	}
	public void setCamp_status(int camp_status) {
		this.camp_status = camp_status;
	}
	public int getMap() {
		return map;
	}
	public void setMap(int map) {
		this.map = map;
	}
	public long getEnd_date() {
		return end_date;
	}
	public void setEnd_date(long end_date) {
		this.end_date = end_date;
	}
	public String getPromo_msg() {
		return promo_msg;
	}
	public void setPromo_msg(String promo_msg) {
		this.promo_msg = promo_msg;
	}
	public int getClientid() {
		return clientid;
	}
	public void setClientid(int clientid) {
		this.clientid = clientid;
	}
	public int getClicks() {
		return clicks;
	}
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}


	public CampaignListBean()
	{

	}

	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public int getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(int order_amount) {
		this.order_amount = order_amount;
	}
	
	public CampaignListBean(Cursor c)
	{

		camp_id        = c.getInt(c.getColumnIndex(CampaignTable.CAMPAIGN_ID));
		clientid       = c.getInt(c.getColumnIndex(CampaignTable.CLIENT_ID));
		camp_name      = c.getString(c.getColumnIndex(CampaignTable.CAMPAIGN_NAME));
		promo_msg      = c.getString(c.getColumnIndex(CampaignTable.PROMO_MSG));
		start_date     = c.getLong(c.getColumnIndex(CampaignTable.START_DATE));
		end_date       = c.getLong(c.getColumnIndex(CampaignTable.END_DATE));
		total_imp      = c.getInt(c.getColumnIndex(CampaignTable.TOTAL_IMP));
		clicks         = c.getInt(c.getColumnIndex(CampaignTable.CLICKS));
		shown_imp      = c.getInt(c.getColumnIndex(CampaignTable.SHOWN_IMP));
		call           = c.getInt(c.getColumnIndex(CampaignTable.CALL));
		web            = c.getInt(c.getColumnIndex(CampaignTable.WEB));
		map            = c.getInt(c.getColumnIndex(CampaignTable.MAP));
		camp_status    = c.getInt(c.getColumnIndex(CampaignTable.CAMPAIGN_STATUS));
		order_id       = c.getString(c.getColumnIndex(CampaignTable.ORDER_ID));
		order_amount   = c.getInt(c.getColumnIndex(CampaignTable.ORDER_AMOUNT));
		lat            = c.getString(c.getColumnIndex(CampaignTable.LATITUDE));
		lon            = c.getString(c.getColumnIndex(CampaignTable.LONGITUDE));
		action            = c.getString(c.getColumnIndex(CampaignTable.ACTION));
		
	}
}
