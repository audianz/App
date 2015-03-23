package com.audianz.beans;

import com.audianz.database.PromotePlan;

import android.database.Cursor;

public class PlanResponseBean {

	private int id =0;
	private int viewer=0;
	private int inr_price=0;
	private String message=null;
	private int free_plan=0;


	public PlanResponseBean()
	{


	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getViewer() {
		return viewer;
	}


	public void setViewer(int viewer) {
		this.viewer = viewer;
	}


	public int getInr_price() {
		return inr_price;
	}


	public void setInr_price(int inr_price) {
		this.inr_price = inr_price;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	public int getFree_plan() {
		return free_plan;
	}

	public void setFree_plan(int free_plan) {
		this.free_plan = free_plan;
	}	

	

	public PlanResponseBean(Cursor cur)
	{
		id 			= cur.getInt(cur.getColumnIndex(PromotePlan.ID));
		viewer		= cur.getInt(cur.getColumnIndex(PromotePlan.VIEWERS));
		inr_price	= cur.getInt(cur.getColumnIndex(PromotePlan.INR_PRICE));
		message     = cur.getString(cur.getColumnIndex(PromotePlan.MESSAGE));
		free_plan   = cur.getInt(cur.getColumnIndex(PromotePlan.FREE_PLAN));
		
	}
}
