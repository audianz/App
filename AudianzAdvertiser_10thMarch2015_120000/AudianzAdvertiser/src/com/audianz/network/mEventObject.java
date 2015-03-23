package com.audianz.network;

import java.util.ArrayList;

import com.audianz.beans.BaseBean;
import com.audianz.beans.RequestBean;

/**
 * The mEventObject class is used to create event object, which is used to sent data between threads.
 * @author 
 *
 */
public class mEventObject {
	
	public RequestBean reqBean = null;
	public String filePath = null;
	public String fileName = null;
	public int httpMethod = -1;
	public ArrayList<String> filterVar = null;
	public boolean isPhone = false;

	// Bean objects 
	public BaseBean baseBean = null;
	public ArrayList baseBeanList = null;
	public String action = "";
    public String countryCode = "";
    public String app = "";
    public boolean showNotification = false;
	public boolean preemptedFlag = false;
	
	public mEventObject()
	{
		reqBean = null;
		baseBeanList = null;
		filePath = null;
		httpMethod = -1;
	}	
}
