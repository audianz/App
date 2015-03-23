package com.audianz.beans;

public class AddOrderRequest extends RequestBean {

	
	private String order_id     = null;
	private String sub_order_id = null;
	private String product_sku  = null;
	private float order_amount = 0;
	private String currency = null;
	private String order_date = null;
	private int order_status =-1;
	private int cust_id =0;
	private String cust_name = null;
	private String cust_email = null;
	private String cust_mobile = null;
	private String cust_country =null;
	private String cust_pincode = null;
	private String invoice_id = null;
	private int isFreePlan =0;
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getSub_order_id() {
		return sub_order_id;
	}
	public void setSub_order_id(String sub_order_id) {
		this.sub_order_id = sub_order_id;
	}
	public String getProduct_sku() {
		return product_sku;
	}
	public void setProduct_sku(String product_sku) {
		this.product_sku = product_sku;
	}
	public float getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(float order_amount) {
		this.order_amount = order_amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public int getOrder_status() {
		return order_status;
	}
	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}
	public int getCust_id() {
		return cust_id;
	}
	public void setCust_id(int cust_id) {
		this.cust_id = cust_id;
	}
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public String getCust_email() {
		return cust_email;
	}
	public void setCust_email(String cust_email) {
		this.cust_email = cust_email;
	}
	public String getCust_mobile() {
		return cust_mobile;
	}
	public void setCust_mobile(String cust_mobile) {
		this.cust_mobile = cust_mobile;
	}
	public String getCust_country() {
		return cust_country;
	}
	public void setCust_country(String cust_country) {
		this.cust_country = cust_country;
	}
	public String getCust_pincode() {
		return cust_pincode;
	}
	public void setCust_pincode(String cust_pincode) {
		this.cust_pincode = cust_pincode;
	}
	public String getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}
	public int getIsFreePlan() {
		return isFreePlan;
	}
	public void setIsFreePlan(int isFreePlan) {
		this.isFreePlan = isFreePlan;
	}
	
}
