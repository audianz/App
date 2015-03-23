package com.audianz.beans;

public class RegisterBean extends BaseBean {


	private int  CLIENT_ID = 0;
	private String BUSINESS_NAME=null;
	private String NAME    = null;
	private String EMAIL_ID   = null;
	private String PASSWORD   = null;
	private String MOBILE      =null;
	private String ADDRESS      =null;
	private String CITY      =null;
	private String STATE      =null;
	private String ZIP        =null;


	public int getCLIENT_ID() {
		return CLIENT_ID;
	}
	public void setCLIENT_ID(int cLIENT_ID) {
		CLIENT_ID = cLIENT_ID;
	}
	public String getBUSINESS_NAME() {
		return BUSINESS_NAME;
	}
	public void setBUSINESS_NAME(String bUSINESS_NAME) {
		BUSINESS_NAME = bUSINESS_NAME;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getEMAIL_ID() {
		return EMAIL_ID;
	}
	public void setEMAIL_ID(String eMAIL_ID) {
		EMAIL_ID = eMAIL_ID;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getCITY() {
		return CITY;
	}
	public void setCITY(String cITY) {
		CITY = cITY;
	}
	public String getSTATE() {
		return STATE;
	}
	public void setSTATE(String sTATE) {
		STATE = sTATE;
	}
	public String getZIP() {
		return ZIP;
	}
	public void setZIP(String zIP) {
		ZIP = zIP;
	}


}
