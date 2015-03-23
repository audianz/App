package com.audianz.beans;

public class UpdateCampaignRequest extends RequestBean{

	private int clientid = -1;
	private int campaignid =-1;
	private int camp_status =-1;
	
	public int getClientid() {
		return clientid;
	}
	public void setClientid(int clientid) {
		this.clientid = clientid;
	}
	public int getCampaignid() {
		return campaignid;
	}
	public void setCampaignid(int campaignid) {
		this.campaignid = campaignid;
	}
	public int getCamp_status() {
		return camp_status;
	}
	public void setCamp_status(int camp_status) {
		this.camp_status = camp_status;
	}

}
