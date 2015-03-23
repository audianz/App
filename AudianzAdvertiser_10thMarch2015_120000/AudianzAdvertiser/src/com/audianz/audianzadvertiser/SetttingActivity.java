package com.audianz.audianzadvertiser;

import com.audianz.beans.RegistrationResponseBean;
import com.audianz.constants.NetworkResponseCode;
import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;
import com.audianz.core.Engine;
import com.audianz.emcl.ELogger;
import com.audianz.utilities.UIStateMachine;

import android.app.Notification.Style;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SetttingActivity extends BaseActivity {

	private UIStateMachine 			uistate					= null;
	private ELogger 				mELogger 				= null;
	private Engine engObj =null;
	//......................Class constants
	private final String 			TAG 					= "SETTINGCTIVITY";

	// Widgets

	private Button edit_btn =null;
	private TextView header_tv =null;
	private Button signout_btn =null;
	private TextView busi_name_tv=null;
	private TextView name_tv =null;
	private TextView address_tv =null;
	private TextView city_tv =null;
	private TextView state_tv =null;
	private TextView zip_tv =null;
	private TextView email_tv=null;
	private TextView web_url_tv = null;
	private TextView mob_tv =null;
	private TextView about_tv =null;
	private TextView faq_tv =null;
	private TextView contactus_tv=null;
	private TextView about_arrow_tv =null;
	private TextView faq_arrow_tv =null;
	private TextView contactus_arrow_tv=null;

	private Typeface custom_font ;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		//....... Application initialization
		uistate=UIStateMachine.getInstance();
		uistate.curActivity= this;

		if(engObj==null)
			engObj = Engine.engObj;

		if(mELogger == null)
			mELogger = new ELogger();
		mELogger.setTag(TAG);

		mELogger.debug("OnCreate started ");

		uiType = UIType.SETTING_ACTIVITY;

		mELogger.debug("onCreate");
		findWidgetsById();
		setListener();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setSettingsData();
	}
	private void findWidgetsById()
	{
		edit_btn =(Button)findViewById(R.id.edit_btn);
		header_tv =(TextView)findViewById(R.id.header_tv);
		signout_btn =(Button)findViewById(R.id.signOut_btn);
		busi_name_tv=(TextView)findViewById(R.id.busi_name_tv);
		name_tv =(TextView)findViewById(R.id.name_tv);
		address_tv =(TextView)findViewById(R.id.address_tv);
		city_tv =(TextView)findViewById(R.id.city_tv);
		state_tv =(TextView)findViewById(R.id.state_tv);
		zip_tv =(TextView)findViewById(R.id.zip_tv);
		email_tv=(TextView)findViewById(R.id.email_tv);
		mob_tv =(TextView)findViewById(R.id.mob_tv);
		web_url_tv=(TextView)findViewById(R.id.web_url_tv);
		about_tv =(TextView)findViewById(R.id.about_tv);
		faq_tv =(TextView)findViewById(R.id.faq_tv);
		contactus_tv=(TextView)findViewById(R.id.contactus_tv);
		about_arrow_tv = (TextView)findViewById(R.id.about_arrow_tv);
		faq_arrow_tv = (TextView)findViewById(R.id.faq_arrow_tv);
		contactus_arrow_tv = (TextView)findViewById(R.id.contactus_arrow_tv);


		custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");

		edit_btn.setTypeface(custom_font);
		header_tv.setTypeface(custom_font);
		signout_btn.setTypeface(custom_font);
		busi_name_tv.setTypeface(custom_font);
		name_tv.setTypeface(custom_font);
		address_tv.setTypeface(custom_font);
		city_tv.setTypeface(custom_font);
		state_tv.setTypeface(custom_font);
		zip_tv.setTypeface(custom_font);
		email_tv.setTypeface(custom_font);
		mob_tv.setTypeface(custom_font);
		web_url_tv.setTypeface(custom_font);
		about_tv.setTypeface(custom_font);
		faq_tv.setTypeface(custom_font);
		contactus_tv.setTypeface(custom_font);

	}

	/**
	 * This method is used to set User profile data on settins activity
	 */
	private void setSettingsData()
	{
		int clientid = Engine.engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID();
		RegistrationResponseBean userData = Engine.engObj.regUtilObj.getUserProfile(clientid);
		if(userData!=null)
		{
			if(userData.getBusiness_name()!=null && userData.getBusiness_name().length()>0)
			{
				busi_name_tv.setText(userData.getBusiness_name());
				busi_name_tv.setTypeface(custom_font, 1);
			}
			if(userData.getName()!=null && userData.getName().length()>0)
			{
				name_tv.setText(userData.getName());
				name_tv.setTypeface(custom_font, 1);
			}
			else
			{
				name_tv.setText("");
			}
			if(userData.getAddress()!=null && userData.getAddress().length()>0)
			{
				address_tv.setText(userData.getAddress());
				address_tv.setTypeface(custom_font, 1);
			}
			else
			{
				address_tv.setText("");
			}
			if(userData.getCity()!=null && userData.getCity().length()>0)
			{
				city_tv.setText(userData.getCity());
				city_tv.setTypeface(custom_font, 1);
			}
			else
			{
				city_tv.setText("");
			}
			if(userData.getState()!=null && userData.getState().length()>0)
			{
				state_tv.setText(userData.getState());
				state_tv.setTypeface(custom_font, 1);
			}
			else
			{
				state_tv.setText("");
			}
			if(userData.getZip()!=null && userData.getZip().length()>0)
			{
				zip_tv.setText(userData.getZip());
				zip_tv.setTypeface(custom_font, 1);
			}
			else
			{
				zip_tv.setText("");
			}
			if(userData.getEmailid()!=null && userData.getEmailid().length()>0)
			{
				email_tv.setText(userData.getEmailid());
				email_tv.setTypeface(custom_font, 1);
			}
			if(userData.getMobile()!=null && userData.getMobile().length()>0)
			{
				mob_tv.setText(userData.getMobile());
				mob_tv.setTypeface(custom_font, 1);
			}
			if(userData.getWebsite()!=null && userData.getWebsite().length()>0)
			{
				web_url_tv.setText(userData.getWebsite());
				web_url_tv.setTypeface(custom_font, 1);
			}
			else
			{
				web_url_tv.setText("");
			}


		}
		else
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mELogger.error("userData is null");
		}
	}

	private void setListener()
	{
		edit_btn.setOnClickListener(mClickListener);
		signout_btn.setOnClickListener(mClickListener);

		about_tv.setOnClickListener(mClickListener);
		about_arrow_tv.setOnClickListener(mClickListener);

		faq_tv.setOnClickListener(mClickListener);
		faq_arrow_tv.setOnClickListener(mClickListener);

		contactus_tv.setOnClickListener(mClickListener);
		contactus_arrow_tv.setOnClickListener(mClickListener);
	}

	View.OnClickListener mClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.edit_btn:
				uistate.nextState(SetttingActivity.this, UIEventType.EDIT, false, UIType.SETTING_ACTIVITY,null);
				break;

			case R.id.signOut_btn:
				engObj.cnfigReaderObj.setAUDIANZ_BUSINESS_NAME(null);
				engObj.cnfigReaderObj.setAUDIANZ_CLIENT_ID(0);
				NetworkResponseCode.IS_LOGIN=true;
				uistate.nextState(SetttingActivity.this, UIEventType.LOGOUT, true, UIType.SPLASH_ACTIVITY,null);
				break;
			case R.id.about_arrow_tv:
			case R.id.about_tv:
				uistate.nextState(SetttingActivity.this, UIEventType.ABOUTUS, false, UIType.SETTING_ACTIVITY,null);
				break;
			case R.id.faq_arrow_tv:
			case R.id.faq_tv:
				uistate.nextState(SetttingActivity.this, UIEventType.FAQ, false, UIType.SETTING_ACTIVITY,null);
				break;
			case R.id.contactus_arrow_tv:
			case R.id.contactus_tv:
				uistate.nextState(SetttingActivity.this, UIEventType.CONTACTUS, false, UIType.SETTING_ACTIVITY,null);
				break;
			default:
				break;
			}

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}


}
