package com.audianz.audianzadvertiser;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.audianz.beans.RegistrationResponseBean;
import com.audianz.beans.UpdateRegisterRequestBean;
import com.audianz.constants.EventType;
import com.audianz.constants.NetworkResponseCode;
import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;
import com.audianz.core.Engine;
import com.audianz.emcl.ELogger;
import com.audianz.utilities.Common;
import com.audianz.utilities.UIStateMachine;

public class EditActivity extends BaseActivity {

	private UIStateMachine uistate=null;
	private ELogger mElogger =null;
	private Engine engObj=null;
	private final String TAG ="EditActivity";

	private Button cancel_btn = null;
	private TextView setting_tv =null;
	private EditText businame =null;
	private EditText name = null;
	private EditText busi_adrress = null;
	private EditText city = null;
	private EditText state = null;
	private EditText zip  = null;
	private TextView email = null;
	private EditText password = null;
	private EditText web_url = null;
	private EditText mobile = null;
	private Button update_btn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		uistate = UIStateMachine.getInstance();
		uistate.curActivity= this;

		if(engObj==null)
			engObj = Engine.engObj;
		if(mElogger==null)
			mElogger = new ELogger();
		mElogger.setTag(TAG);

		if(Engine.IS_DEVELOPMENT_RELEASE)
			mElogger.debug("onCreate()");

		initializeWidget();
		setSettingsData();
		setListener();

	}


	private void initializeWidget()
	{
		cancel_btn   = (Button)findViewById(R.id.edit_cancel_btn);
		setting_tv   = (TextView)findViewById(R.id.edit_header_tv);
		businame     = (EditText)findViewById(R.id.edit_busi_name_et);
		name 		 = (EditText)findViewById(R.id.edit_name_et);
		busi_adrress = (EditText)findViewById(R.id.edit_address_et);
		city 		 = (EditText)findViewById(R.id.edit_city_et);
		state 		 = (EditText)findViewById(R.id.edit_state_et);
		zip 		 = (EditText)findViewById(R.id.edit_zip_et);
		email        = (TextView)findViewById(R.id.edit_email_tv);
		password 	 = (EditText)findViewById(R.id.edit_password_et);
		update_btn   = (Button)findViewById(R.id.edit_update_btn);  
        web_url      = (EditText)findViewById(R.id.edit_web_url_et);
        mobile       = (EditText)findViewById(R.id.edit_mobile_et); 
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");

		cancel_btn.setTypeface(custom_font);	    

		setting_tv.setTypeface(custom_font);
		businame.setTypeface(custom_font);
		name.setTypeface(custom_font);
		busi_adrress.setTypeface(custom_font);
		city.setTypeface(custom_font);
		state.setTypeface(custom_font);
		zip.setTypeface(custom_font);
		email.setTypeface(custom_font);
		password.setTypeface(custom_font);
		update_btn.setTypeface(custom_font);
		web_url.setTypeface(custom_font);
		mobile.setTypeface(custom_font);
	}


	/**
	 * This method is used to set User profile data on settings activity
	 */
	private void setSettingsData()
	{
		int clientid = Engine.engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID();
		RegistrationResponseBean userData = Engine.engObj.regUtilObj.getUserProfile(clientid);
		if(userData!=null)
		{
			if(userData.getBusiness_name()!=null)
			{
				businame.setText(userData.getBusiness_name());
			}
			if(userData.getName()!=null)
			{
				name.setText(userData.getName());
			}
			if(userData.getAddress()!=null)
			{
				busi_adrress.setText(userData.getAddress());
			}
			if(userData.getCity()!=null)
			{
				city.setText(userData.getCity());
			}
			if(userData.getState()!=null)
			{
				state.setText(userData.getState());
			}
			if(userData.getZip()!=null)
			{
				zip.setText(userData.getZip());
			}
			if(userData.getEmailid()!=null)
			{
				email.setText(userData.getEmailid());
			}
			if(userData.getPassword()!=null)
				password.setText(userData.getPassword());
		
			if(userData.getMobile()!=null)
				mobile.setText(userData.getMobile());
			
			if(userData.getWebsite()!=null)
				web_url.setText(userData.getWebsite());
			
			
		}
		else
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mElogger.error("userData is null");
		}
	}


	private void setListener()
	{
		cancel_btn.setOnClickListener(mClickListener);
		update_btn.setOnClickListener(mClickListener);
	}

	View.OnClickListener mClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.edit_cancel_btn:
				uistate.nextState(EditActivity.this,UIEventType.CANCEL, true, UIType.EDITACTIVITY,null);
				break;
			case R.id.edit_update_btn:
				handleUpdate();
				break;
			default:
				break;
			}

		}
	};

	private void handleUpdate()
	{

		if(!Common.isNetworkAvailable(getApplicationContext()))
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mElogger.error("Network not available");
			displayToast(getString(R.string.net_not_available), Gravity.TOP, false,  Toast.LENGTH_SHORT);
			return;
		}

		String busi_name    = businame.getText().toString();
		String username     = name.getText().toString();
		String address      = busi_adrress.getText().toString();
		String user_city    = city.getText().toString();
		String user_state   = state.getText().toString();
		String user_zip     = zip.getText().toString();
		String user_email   = email.getText().toString();
		String user_pass    = password.getText().toString();
		String mob          = mobile.getText().toString();
		String website      = web_url.getText().toString();

		if(!Common.isValidName(busi_name))
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mElogger.error("Business Name  can not be null");
			displayToast(getString(R.string.invalid_business_name), Gravity.CENTER, false, Toast.LENGTH_SHORT);
			return;
		}
		if(!Common.isValidMobile(mob))
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mElogger.error("Mobile can not be null");
			displayToast(getString(R.string.invalid_mobile), Gravity.CENTER, false, Toast.LENGTH_SHORT);
			return;
		}
		if(!Common.isValidPassword(user_pass))
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mElogger.error("Password can not be null");
			displayToast(getString(R.string.invalid_pass), Gravity.CENTER, false, Toast.LENGTH_SHORT);
			return;
		}
		
		UpdateRegisterRequestBean reqBean = new UpdateRegisterRequestBean();

		reqBean.setClientid(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());
		reqBean.setBusiness_name(busi_name);
		reqBean.setName(username);
		reqBean.setAddress(address);
		reqBean.setCity(user_city);
		reqBean.setState(user_state);
		reqBean.setZip(user_zip);
		reqBean.setEmail(user_email);
		reqBean.setPassword(user_pass);
		reqBean.setMobileno(mob);
		reqBean.setWeb_url(website);

		if(engObj!=null && reqBean!=null)
		{
			int serverRespCode = baseEngObj.updateRegisterUser(reqBean);
			if(serverRespCode == -1)
			{
				if(Engine.IS_DEVELOPMENT_RELEASE)
					mElogger.error("onClick() : null engine response for update register user.");
			}
			checkResponse(serverRespCode);
		}
		else
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mElogger.error("handleUpdate() engObj or reqBean is null");
		}



	}

	@Override
	public void handleEvent(int evType, Object msgObj) {
		// TODO Auto-generated method stub
		super.handleEvent(evType, msgObj);

		if(Engine.IS_DEVELOPMENT_RELEASE)
			mElogger.debug("handleEvent server response back for event type "+evType);

		if(msgObj==null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mElogger.error("handleEvent() msgObj is null");
			return;
		}
		switch (evType) {
		case EventType.UPDATE_REGISTER:
		{
			dismissProgressDialogue();
			RegistrationResponseBean registrationResp = (RegistrationResponseBean)msgObj;
			if(registrationResp.getStatus().equals("SUCCESS"))
			{
				displayToast("Update Successfull", Gravity.CENTER, false, Toast.LENGTH_SHORT);
				NetworkResponseCode.IS_LOGIN=true;
				uistate.nextState(EditActivity.this, EventType.UPDATE_SUCCESS, true, UIType.EDITACTIVITY,null);
			}
			else
			{
				if(Engine.IS_DEVELOPMENT_RELEASE)
					mElogger.error("Update Failed");
				displayToast("Update Failed", Gravity.CENTER, false, Toast.LENGTH_SHORT);
				return;
			}

		}
		break;

		default:
			break;
		}
	}
}
