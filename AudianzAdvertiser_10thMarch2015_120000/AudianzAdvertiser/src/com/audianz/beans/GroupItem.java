package com.audianz.beans;

import java.util.ArrayList;

public class GroupItem {

	private String camp_name = null;
	private String promo_msg = null;
	private int campaignid = 0;
	private String lat = null;
	private String lon = null;
	private ArrayList<ChildItem> items = null;
	
	
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
	public String getCamp_name() {
		return camp_name;
	}
	public void setCamp_name(String camp_name) {
		this.camp_name = camp_name;
	}
	public ArrayList<ChildItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<ChildItem> items) {
		this.items = items;
	}
	public int getCampaignid() {
		return campaignid;
	}
	public void setCampaignid(int campaignid) {
		this.campaignid = campaignid;
	}
	public String getPromo_msg() {
		return promo_msg;
	}
	public void setPromo_msg(String promo_msg) {
		this.promo_msg = promo_msg;
	}
	
}
