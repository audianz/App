package com.audianz.beans;

import com.audianz.database.Registration;

import android.database.Cursor;

public class RegistrationResponseBean extends ResponseBean{

	private int clientid = 0;
	private String business_name=null;
	private String name =null;
	private String emailid = null;
	private String password = null;
	private String mobile=null;
	private String address=null;
	private String city=null;
	private String state=null;
	private String zip=null;
	private String website = null;


	
	public int getClientid() {
		return clientid;
	}
	public void setClientid(int clientid) {
		this.clientid = clientid;
	}
	public String getBusiness_name() {
		return business_name;
	}
	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailid() {
		return emailid;
	}
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}


    public RegistrationResponseBean()
    {
    	
    	
    }
    
    
	public RegistrationResponseBean(Cursor cur)
	{
		try
		{
			clientid 			= cur.getInt(cur.getColumnIndex(Registration.CLIENT_ID));
			business_name 		= cur.getString(cur.getColumnIndex(Registration.BUSINESS_NAME));
			name  				= cur.getString(cur.getColumnIndex(Registration.NAME));
			emailid 			= cur.getString(cur.getColumnIndex(Registration.EMAIL_ID));
			password            = cur.getString(cur.getColumnIndex(Registration.PASSWORD));
			mobile				= cur.getString(cur.getColumnIndex(Registration.MOBILE));
			address				= cur.getString(cur.getColumnIndex(Registration.ADDRESS));
			city				= cur.getString(cur.getColumnIndex(Registration.CITY));
			state 				= cur.getString(cur.getColumnIndex(Registration.STATE));
			zip					= cur.getString(cur.getColumnIndex(Registration.ZIP));
			website             = cur.getString(cur.getColumnIndex(Registration.WEBSITE));
		}
		catch (Exception e)
		{
			
			System.out.print("Exception "+e);
		}
		
		
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}



}
