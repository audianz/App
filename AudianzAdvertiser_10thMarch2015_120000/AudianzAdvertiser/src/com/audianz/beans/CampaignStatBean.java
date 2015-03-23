package com.audianz.beans;

import com.audianz.database.CampaignStatTable;

import android.database.Cursor;

public class CampaignStatBean {
	
	private long date =0;
	private int clientid =0;
	private int imp =0;
	private int click =0;
	private int call =0;
	private int web =0;
	private int map =0;
	
	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
	}

	public int getImp() {
		return imp;
	}

	public void setImp(int imp) {
		this.imp = imp;
	}

	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
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

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}
	
		
	public CampaignStatBean()
	{
		
	}
	
	public CampaignStatBean(Cursor cur)
	{			
		date             = cur.getLong(cur.getColumnIndex(CampaignStatTable.DATE));
		clientid        = cur.getInt(cur.getColumnIndex(CampaignStatTable.CLIENT_ID));
		imp              = cur.getInt(cur.getColumnIndex(CampaignStatTable.IMPRESSION));
		click              = cur.getInt(cur.getColumnIndex(CampaignStatTable.CLICK));
		call              = cur.getInt(cur.getColumnIndex(CampaignStatTable.CALL));
		web              = cur.getInt(cur.getColumnIndex(CampaignStatTable.WEB));
		map              = cur.getInt(cur.getColumnIndex(CampaignStatTable.MAP));
	}
}
