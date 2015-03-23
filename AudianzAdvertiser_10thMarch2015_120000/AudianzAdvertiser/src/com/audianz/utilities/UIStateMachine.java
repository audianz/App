package com.audianz.utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.audianz.audianzadvertiser.BaseActivity;
import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;
import com.audianz.emcl.ELogger;


public class UIStateMachine 
{
	private ELogger mELogger = null;
	private String TAG = "UIStateMAchine";
	public BaseActivity curActivity = null;
	private int[][] smTable = null;
	private static UIStateMachine stateObj = null;
	private int curActivityType = -1;
	
	private UIStateMachine()
	{
		
	}
	
	/**
	 * This method is responsible for creating UIStateMachine object.
	 * @return
	 */
	public static UIStateMachine getInstance()
	{
		if(stateObj == null) stateObj = new UIStateMachine();
		return stateObj;
	}
	
	/**
	 * This method is responsible for initializing UIStateMachine class.
	 * @param context
	 * @return
	 */
	public boolean init(Context context)
	{
		mELogger = new ELogger();
		mELogger.setTag(TAG);
		maintainSMTable();
		return true;
	}

	/**
	 * This method is responsible for maintaining the smTable using UIType and UIEventTye.
	 */
	private void maintainSMTable() 
	{
		smTable = new int[UIType.MAXUITYPE][UIEventType.MAXUIEVENTTYPE];
		for(int i = 0; i < UIType.MAXUITYPE; i++)
		{
			for(int j = 0; j < UIEventType.MAXUIEVENTTYPE; j++)
			{
				smTable[i][j] = i;   //have a doubt on this line
			}
		}
		smTable[UIType.SPLASH_ACTIVITY][UIEventType.START] = UIType.START_ACTIVITY;
		smTable[UIType.SPLASH_ACTIVITY][UIEventType.LOGGEDIN] = UIType.TABHOST_ACTIVITY;
		smTable[UIType.START_ACTIVITY][UIEventType.REGISTRATION] = UIType.REGISTRATION_ACTIVITY;
		smTable[UIType.START_ACTIVITY][UIEventType.LOGIN] = UIType.LOGIN_ACTIVITY;
		smTable[UIType.START_ACTIVITY][UIEventType.LEARN_MORE]= UIType.FAQACTIVITY;
		
		
		smTable[UIType.REGISTRATION_ACTIVITY][UIEventType.START] = UIType.START_ACTIVITY;
		smTable[UIType.REGISTRATION_ACTIVITY][UIEventType.TABHOST] = UIType.TABHOST_ACTIVITY;
		smTable[UIType.REGISTRATION_ACTIVITY][UIEventType.LOGIN]   = UIType.LOGIN_ACTIVITY;
		
		smTable[UIType.LOGIN_ACTIVITY][UIEventType.START] = UIType.START_ACTIVITY;
		smTable[UIType.LOGIN_ACTIVITY][UIEventType.REGISTRATION] = UIType.REGISTRATION_ACTIVITY;
		smTable[UIType.LOGIN_ACTIVITY][UIEventType.TABHOST] = UIType.TABHOST_ACTIVITY;
		
		smTable[UIType.LOGIN_ACTIVITY][UIEventType.LOGINCLICK] = UIType.MAP_ACTIVITY;
		smTable[UIType.LOGIN_ACTIVITY][UIEventType.CREATEREGISTRATIONCLICK] = UIType.REGISTRATION_ACTIVITY;
		smTable[UIType.LOGIN_ACTIVITY][UIEventType.FORGET_PASSWORD] = UIType.FORGET_PASSWORS_ACTIVITY;
		smTable[UIType.PROMOTENOW_ACTIVITY][UIEventType.TABHOST] = UIType.TABHOST_ACTIVITY;
		smTable[UIType.PROMOTENOW_ACTIVITY][UIEventType.SUCCESSPROMOTE]=UIType.TABHOST_ACTIVITY;
		smTable[UIType.PROMOTENOW_ACTIVITY][UIEventType.HELP_CLICK]=UIType.FAQACTIVITY;
		smTable[UIType.PROMOTENOW_ACTIVITY][UIEventType.ORDER_SUCCESS]=UIType.THANKYOUACTIVITY;
		
		smTable[UIType.REPORTACTIVITY][UIEventType.PROMOTE]=UIType.PROMOTENOW_ACTIVITY;
		smTable[UIType.REPORTACTIVITY][UIEventType.RERUNCAMPAIGN]=UIType.PROMOTENOW_ACTIVITY;
		smTable[UIType.REPORTACTIVITY][UIEventType.EDIT_CAMPAIGN_CLICK]=UIType.PROMOTENOW_ACTIVITY;
		
		
		smTable[UIType.MAP_ACTIVITY][UIEventType.PROMOTE] = UIType.PROMOTENOW_ACTIVITY;
		smTable[UIType.MAP_ACTIVITY][UIEventType.PROFILECLICK] = UIType.PROFILEACTIVITY;
		smTable[UIType.MAP_ACTIVITY][UIEventType.REPORTCLICK] = UIType.REPORTACTIVITY;
		smTable[UIType.MAP_ACTIVITY][UIEventType.HELP_CLICK] = UIType.FAQACTIVITY;
		
		
		smTable[UIType.SETTING_ACTIVITY][UIEventType.REPORTCLICK] = UIType.TABHOST_ACTIVITY;
		smTable[UIType.SETTING_ACTIVITY][UIEventType.LOGOUT]=UIType.SPLASH_ACTIVITY;
		smTable[UIType.SETTING_ACTIVITY][UIEventType.EDIT]=UIType.EDITACTIVITY;
		smTable[UIType.SETTING_ACTIVITY][UIEventType.ABOUTUS]=UIType.ABOUTACTIVITY;
		smTable[UIType.SETTING_ACTIVITY][UIEventType.FAQ]=UIType.FAQACTIVITY;
		smTable[UIType.SETTING_ACTIVITY][UIEventType.CONTACTUS]=UIType.CONTACTUSACTIVITY;
		
		smTable[UIType.EDITACTIVITY][UIEventType.CANCEL]=UIType.TABHOST_ACTIVITY;
		smTable[UIType.EDITACTIVITY][UIEventType.UPDATESUCCESS]=UIType.TABHOST_ACTIVITY;
		smTable[UIType.REGISTRATION_ACTIVITY][UIEventType.REGISTERSUCCESS]=UIType.TABHOST_ACTIVITY;
		
		smTable[UIType.FORGET_PASSWORS_ACTIVITY][UIEventType.BACKBUTTONCLICK]=UIType.LOGIN_ACTIVITY;
		smTable[UIType.FORGET_PASSWORS_ACTIVITY][UIEventType.FORGET_PASS_SUCCESS]=UIType.LOGIN_ACTIVITY;
		smTable[UIType.THANKYOUACTIVITY][UIEventType.REPORTCLICK]=UIType.REPORTACTIVITY;
	
			//NEED TO IMPLEMENT FOR BACK BUTTON
	}
	
	/**
	 * This method is responsible for what will be the next activity for the current application.
	 * @param current
	 * @param evType
	 * @param closeActivity
	 * @param curUiType
	 * @return
	 */
	public boolean nextState(BaseActivity current, int evType, boolean closeActivity, int curUiType, Bundle bundleData)
	{
		mELogger.debug("nextState 1");
		if(curUiType <= UIType.MINUITYPE || curUiType >= UIType.MAXUITYPE
				|| evType <= UIEventType.MINUIEVENTTYPE || evType >= UIEventType.MAXUIEVENTTYPE)
		{
			mELogger.error("nextState() : parameters are not correct.");
			return false;
		}
		mELogger.debug("nextState 2");
		if(current == null)
		{
			mELogger.error("nextState() : current activity is null.");
			return false;
		}
		mELogger.debug("nextState 3");
		int nextState = smTable[curUiType][evType];
		String nextActivity = UIType.getActivity(nextState);
		if(TextUtils.isEmpty(nextActivity))
		{
			mELogger.error("nextState() : next activity is null or empty.");
			return false;
		}
		mELogger.debug("ne " + nextActivity);
		Intent i = null;
		try 
		{
			mELogger.debug("nextState 5");
			i = new Intent(current,Class.forName(nextActivity));
			if(bundleData != null)
				i.putExtras(bundleData);
			curActivityType = nextState;
			current.startActivity(i);
		}
		catch (ClassNotFoundException e) 
		{
			mELogger.error("UIStateMachine nextState() : exception while starting new activity : "+e);
			return false;
		}
		if(closeActivity)
		{
			mELogger.debug("nextState 6");
			mELogger.debug("UIStateMachine nextState() : finishing pervious activity: "+curUiType);
			current.finish();
		}
		return true;
	}
}
