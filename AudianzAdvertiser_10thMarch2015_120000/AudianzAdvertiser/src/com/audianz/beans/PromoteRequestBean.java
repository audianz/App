package com.audianz.beans;

public class PromoteRequestBean extends RequestBean{

	private String camp_name = "";
	private String promote_msg = "";
	private String lat = null;
	private String lon = null;
	private int accbal= 0;
	private int clientid = 0;
	private long startTime=0;
	private String web_url=null;
	private String click_to_action =null;


	public String getCamp_name() {
		return camp_name;
	}
	public void setCamp_name(String camp_name) {
		this.camp_name = camp_name;
	}

	public int getClientid() {
		return clientid;
	}
	public void setClientid(int clientid) {
		this.clientid = clientid;
	}
	public int getAccbal() {
		return accbal;
	}
	public void setAccbal(int accbal) {
		this.accbal = accbal;
	}
	public String getPromote_msg() {
		return promote_msg;
	}
	public void setPromote_msg(String promote_msg) {
		this.promote_msg = promote_msg;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
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
	public String getClick_to_action() {
		return click_to_action;
	}
	public void setClick_to_action(String click_to_action) {
		this.click_to_action = click_to_action;
	}
	public String getWeb_url() {
		return web_url;
	}
	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}

}
