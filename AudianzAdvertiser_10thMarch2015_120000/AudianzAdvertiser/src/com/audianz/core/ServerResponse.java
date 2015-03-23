package com.audianz.core;
import com.audianz.beans.BaseBean;
import com.audianz.beans.RequestBean;
import com.audianz.beans.ResponseBean;

/**
 * The ServerResponse class is used to handle the server response. 
 * @author Vikram
 *
 */
public class ServerResponse {

	public int respcode = -1;
	public ResponseBean respBean = null;
	public RequestBean reqBean = null;
	public BaseBean bBean = null;
	
	public ServerResponse()
	{
		respcode = -1;
		respBean = null;
		reqBean = null;
		bBean = null;
	}
}
