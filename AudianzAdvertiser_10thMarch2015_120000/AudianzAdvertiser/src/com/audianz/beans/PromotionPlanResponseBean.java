package com.audianz.beans;

import java.util.ArrayList;

public class PromotionPlanResponseBean extends ResponseBean {

	private ArrayList<PlanResponseBean> plans =null;

	public ArrayList<PlanResponseBean> getPlans() {
		return plans;
	}

	public void setPlans(ArrayList<PlanResponseBean> plans) {
		this.plans = plans;
	}

	

}
