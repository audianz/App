package com.audianz.beans;

import java.util.ArrayList;

public class FetchStatResponse extends ResponseBean {

	private ArrayList<CampaignStatBean> stat_list =null;

	public ArrayList<CampaignStatBean> getStat_list() {
		return stat_list;
	}

	public void setStat_list(ArrayList<CampaignStatBean> stat_list) {
		this.stat_list = stat_list;
	}
}
