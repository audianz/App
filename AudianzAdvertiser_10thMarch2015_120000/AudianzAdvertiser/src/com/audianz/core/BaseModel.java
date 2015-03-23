package com.audianz.core;

import android.content.Context;

import com.audianz.beans.RequestBean;
import com.audianz.constants.EventType;
import com.audianz.constants.HttpConstant;
import com.audianz.constants.ServerResponseCode;
import com.audianz.emcl.ELogger;
import com.audianz.network.mEventObject;

/**
 * The BaseModel class is the base class for all the Model classes. All the model classes need to extend 
 * BaseModel class. It provides the common functionalities to all the Model classes.
 * @author 
 *
 */
public class BaseModel {

	protected ELogger elogger =  null;
	protected boolean initFlag = false;
	protected Engine engObj = null;
	protected String TAG = "";
	protected Context appContext = null;
	private static BaseModel baseModObj = null;

	private BaseModel()
	{

	}

	public static BaseModel getInstance()
	{
		if(baseModObj == null)
		{
			baseModObj = new BaseModel();
		}
		return baseModObj;
	}

	/**
	 * This method is used to initialize the BaseModel.
	 * @param context
	 * @param logger
	 * @return
	 */
	public boolean init(Context context , ELogger logger )
	{
		if(initFlag)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				elogger.error("init : already initialized");
			return initFlag;
		}
		if(logger == null)
		{
			elogger = new ELogger();
		}
		else
			elogger = logger;
		elogger.setTag(TAG);
		engObj = Engine.engObj;

		if(engObj == null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				elogger.error("init : engine object is null");
		}
		if(context == null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				elogger.error("init : app context is null");
			return initFlag;
		}
		appContext = context;

		if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
			elogger.debug("initialized successfully");
		initFlag = true;
		return initFlag;
	}

	/**
	 * This method is used to make request to the network.
	 * @param evType
	 * @param evObject
	 * @return
	 */
	public int requestToNetwork(int evType , mEventObject evObject)
	{
		int respCode = -1 ;
		if(evType < 0 || evType >= EventType.MAXEVENT || evObject == null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				elogger.error("incorrect input param");
			return ServerResponseCode.INCORRECT_PARAM;
		}

		evObject.reqBean.addData(evType);

		switch(evType)
		{
		case EventType.SIGNIN:
		case EventType.REGISTRATION:
		case EventType.PROMOTENOW:
		case EventType.FETCHPROMOTIONPLAN:
		case EventType.UPDATE_REGISTER:
		case EventType.FETCH_CAMPAIGN_STAT:
		case EventType.UPDATE_CAMPAIGN_STATUS:
		case EventType.FETCH_CAMPAIGN_LIST:
		case EventType.ADD_ORDER_DETAIL:
		case EventType.FORGET_PASSWORD:
		case EventType.EDIT_CAMPAIGN:
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				elogger.info("requestToNetwork :Short Request event type: "+evType);

			evObject.httpMethod = HttpConstant.HTTP_POST;
			if(engObj.shortReqNCObj != null)
			{
				respCode = engObj.shortReqNCObj.addEvent(evType, evObject);
				if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
					elogger.debug("requestToNetwork :Short Request event type: Response Code  :"+respCode);
			}
			else
			{
				if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
					elogger.error("requestToNetwork :shortRequestNCObj is null..");
			}
			break;

		default:
			break;
		}
		return respCode;
	}

	/**
	 * The method is used to handle the UI events. This method should me Overrided by all the Model classes
	 * @param evType
	 * @param evObject
	 * @return
	 */
	public boolean handleUIEvent(int evType , mEventObject evObject)
	{
		return false;
	}

	/**
	 * This function is to handle network response
	 * @param evType
	 * @param servResp
	 * @return true if response is success otherwise false;
	 */
	public boolean networkCallback(int evType , ServerResponse servResp)
	{
		if(evType < 0 || evType > EventType.MAXEVENT || servResp == null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				elogger.error("callback() : incorret input param");
		}
		return true;
	}

}
