package com.audianz.beans;

public class EditCampInfoRequest extends RequestBean{

	private int client_id = -1;
	private int campaign_id =-1;
	private String promote_msg;
	private String click_to_action;
	
	public int getClient_id() {
		return client_id;
	}
	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
	public int getCampaign_id() {
		return campaign_id;
	}
	public void setCampaign_id(int campaign_id) {
		this.campaign_id = campaign_id;
	}
	public String getPromote_msg() {
		return promote_msg;
	}
	public void setPromote_msg(String promote_msg) {
		this.promote_msg = promote_msg;
	}
	public String getClick_to_action() {
		return click_to_action;
	}
	public void setClick_to_action(String click_to_action) {
		this.click_to_action = click_to_action;
	}
	
}
