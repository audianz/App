package com.audianz.beans;

public class ChildItem {

	private int camp_id=0;
	private long start_date =0;
	private int total_imp=0;
	private int shown_imp =0;
	private int call =0;
	private int web =0;
	private int map =0;
	private int camp_status =-1;
	private String camp_msg = null;
	private String action = null;
	
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
	
	public int getMap() {
		return map;
	}
	public void setMap(int map) {
		this.map = map;
	}
	
	public int getCamp_status() {
		return camp_status;
	}
	public void setCamp_status(int camp_status) {
		this.camp_status = camp_status;
	}
	public String getCamp_msg() {
		return camp_msg;
	}
	public void setCamp_msg(String camp_msg) {
		this.camp_msg = camp_msg;
	}
	public int getCamp_id() {
		return camp_id;
	}
	public void setCamp_id(int camp_id) {
		this.camp_id = camp_id;
	}
	
}
