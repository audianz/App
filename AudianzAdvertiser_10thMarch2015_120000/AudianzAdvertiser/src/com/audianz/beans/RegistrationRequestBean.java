package com.audianz.beans;

public class RegistrationRequestBean extends RequestBean {

	
	private String name =null;
	private String emailid=null;
	private String password=null;
	private String mobile=null;
	private String device_id=null;
	private String opr_name=null;
	private String sw_version = null;

	
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getOpr_name() {
		return opr_name;
	}
	public void setOpr_name(String opr_name) {
		this.opr_name = opr_name;
	}
	public String getSw_version() {
		return sw_version;
	}
	public void setSw_version(String sw_version) {
		this.sw_version = sw_version;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
			
}
