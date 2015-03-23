package com.audianz.network;

import com.audianz.constants.EventType;


/**
 * The ServerApi class is used to provide server api command for given event type.
 * @author Vikram
 *
 */
public class ServerApi {
	
	public static String getApi(int eventType)
	{
		String api = null;
		switch(eventType)
		{
		
		case EventType.SIGNIN:
			api = "login_request";
			break;
		case EventType.REGISTRATION:
			api = "register_request";
			break;
		case EventType.PROMOTENOW:
			api="add_campaign_request";
			break;
		case EventType.FETCHPROMOTIONPLAN:
		    api="fetch_promote_plan";
			break;
		case EventType.UPDATE_REGISTER:
			api="update_register_request";
			break;
		case EventType.FETCH_CAMPAIGN_STAT:
			api="fetch_stat_request";
			break;
		case EventType.UPDATE_CAMPAIGN_STATUS:
			api = "update_campaign_status";
			break;
		case EventType.FETCH_CAMPAIGN_LIST:
			api = "fetch_campaign_request";
			break;
		case EventType.ADD_ORDER_DETAIL:
			api="add_order_detail_request";
			break;
		case EventType.FORGET_PASSWORD:
			api = "forget_pass_request";
			break;
		case EventType.EDIT_CAMPAIGN:
			api = "update_campaign_request";
			break;
		default:
			break;
		}
		return api;
	}

}
