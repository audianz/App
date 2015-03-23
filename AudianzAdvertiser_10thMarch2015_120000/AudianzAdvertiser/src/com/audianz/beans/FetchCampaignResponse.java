package com.audianz.beans;

import java.util.ArrayList;

public class FetchCampaignResponse extends ResponseBean {

	private ArrayList<CampaignListBean> camp_list = null;

	public ArrayList<CampaignListBean> getCamp_list() {
		return camp_list;
	}

	public void setCamp_list(ArrayList<CampaignListBean> camp_list) {
		this.camp_list = camp_list;
	}
}
