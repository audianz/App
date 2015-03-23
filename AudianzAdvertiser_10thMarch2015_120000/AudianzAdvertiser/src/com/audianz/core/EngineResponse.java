package com.audianz.core;

import java.util.ArrayList;

import com.audianz.beans.BaseBean;


/**
 * The EngineResponse class send the response to the UI. 
 * @author 
 *
 */
public class EngineResponse extends ServerResponse{

	public ArrayList<BaseBean> dataList = null;

	public EngineResponse()
	{
		dataList = null;
	}
}
