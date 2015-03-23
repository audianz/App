package com.audianz.beans;

public class LoginResponseBean extends ResponseBean{

	private  int clientid =0;
	private String business_name=null;

	/**
	 * @return the clientid
	 */
	public int getClientid() {
		return clientid;
	}

	/**
	 * @param clientid the clientid to set
	 */
	public void setClientid(int clientid) {
		this.clientid = clientid;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

}
