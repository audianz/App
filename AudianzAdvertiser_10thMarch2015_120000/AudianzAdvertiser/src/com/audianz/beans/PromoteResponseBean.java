package com.audianz.beans;

public class PromoteResponseBean extends ResponseBean{
	
	private String camp_name = "";
	private int campid = 0;
	
	public String getCamp_name() {
		return camp_name;
	}
	public void setCamp_name(String camp_name) {
		this.camp_name = camp_name;
	}
	public int getCampid() {
		return campid;
	}
	public void setCampid(int campid) {
		this.campid = campid;
	}
}
