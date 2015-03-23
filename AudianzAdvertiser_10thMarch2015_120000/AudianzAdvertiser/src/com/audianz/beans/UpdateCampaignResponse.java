package com.audianz.beans;

public class UpdateCampaignResponse extends ResponseBean {

	private int campaignid =0;
	private int camp_status = -1;
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
