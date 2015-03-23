package com.audianz.beans;

import com.audianz.network.ServerApi;



public class RequestBean extends BaseBean{
	private String api = "";
	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}
	public void addData(int evType)
	{
		setApi(ServerApi.getApi(evType));
	}
}
