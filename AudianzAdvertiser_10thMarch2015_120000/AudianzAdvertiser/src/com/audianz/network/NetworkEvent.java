package com.audianz.network;

/**
 * The NetworkEvent class is used to add NetworkEvent after receiving successful response from server. 
 * @author 
 *
 */
public class NetworkEvent 
{	
	int evType =  -1;
	Object obj = null;
	boolean addedInMsgQueue = false;

	public NetworkEvent()
	{
		evType =  -1;
		obj = null;
		addedInMsgQueue = false;
	}
}
