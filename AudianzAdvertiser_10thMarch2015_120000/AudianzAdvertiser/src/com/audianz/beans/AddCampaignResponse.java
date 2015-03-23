package com.audianz.beans;

public class AddCampaignResponse extends ResponseBean {

	private String order_id=null;
    private float amount=0;
	
	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	} 
}
