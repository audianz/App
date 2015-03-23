package com.audianz.audianzadvertiser;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.audianz.beans.RegistrationRequestBean;
import com.audianz.beans.RegistrationResponseBean;
import com.audianz.constants.EventType;
import com.audianz.constants.ServerResponseCode;
import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;
import com.audianz.core.Engine;
import com.audianz.emcl.ELogger;
import com.audianz.utilities.Common;
import com.audianz.utilities.UIStateMachine;




public class RegistrationActivity extends BaseActivity {
	private EditText emailEditText = null;
	private EditText orgNameText=null;
	private EditText passEditText = null;
	private EditText mobileEditText = null;
	private Button submitBtn = null;
	private Button signIn = null;
	private TextView basicinfo =null;
	private TextView termsOfUse = null;
    private TextView header_join_now;
	
	private View.OnClickListener onClickHandler = null;
	//...................... Class variables
	private UIStateMachine 			uistate					= null;
	private ELogger 				mELogger 				= null;

	//......................Class constants
	private final String 			TAG 					= "REGISTRATIONACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		uistate=UIStateMachine.getInstance();
		uistate.curActivity= this;

		if(mELogger == null)
			mELogger = new ELogger();
		mELogger.setTag(TAG);

		mELogger.debug("********* Login Activity OnCreate started .... ");

		findWidgetsById();
		implementOnClickHandler();
		setListener() ;
	}

	/**
	 * This method is used to handle all the click events on View.
	 */
	private void implementOnClickHandler() {

		onClickHandler = new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				switch (view.getId()) {
				case R.id.btnSubmit:

					submitClick();
					break;
				case R.id.signIN_btn :
					statMachineObj.nextState(RegistrationActivity.this, UIEventType.LOGIN, true, UIType.REGISTRATION_ACTIVITY,null);
					break;
				case R.id.terms_of_use:
				{
					WebView web = new WebView(getApplicationContext());
					web.getSettings().setJavaScriptEnabled(true);
					web.setWebViewClient(new WebViewClient()
					{
						public void onReceivedError(WebView view,int errorCode,String description,String failingURL)
						{
							displayToast(description, Gravity.CENTER, false, Toast.LENGTH_SHORT);
						}
					});

					web.loadUrl("http://www.audianzexpress.com/");
					setContentView(web);

				}
				break;
				default:
					break;
				}
			}
		};
	}

	/**
	 * This method is for finding widgets by their generated id.
	 */
	private void findWidgetsById()
	{	
		basicinfo =(TextView)findViewById(R.id.reg_basic_info);
		back_btn = (Button)findViewById(R.id.back_btn);
		orgNameText   =(EditText)findViewById(R.id.orgNameText);
		emailEditText = (EditText) findViewById(R.id.regisEmailId);
		passEditText = (EditText) findViewById(R.id.regisPassword);
		mobileEditText = (EditText) findViewById(R.id.regisMobilenum);
		submitBtn = ((Button)findViewById(R.id.btnSubmit));
		signIn    = (Button)findViewById(R.id.signIN_btn);
		termsOfUse = (TextView)findViewById(R.id.terms_of_use);
		header_join_now = (TextView)findViewById(R.id.header_join_now);

		// termsOfUse.setMovementMethod(LinkMovementMethod.getInstance());

		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		basicinfo.setTypeface(custom_font);
		submitBtn.setTypeface(custom_font);
		signIn.setTypeface(custom_font);
		orgNameText.setTypeface(custom_font);
		emailEditText.setTypeface(custom_font);
		passEditText.setTypeface(custom_font);
		mobileEditText.setTypeface(custom_font);
		header_join_now.setTypeface(custom_font);

		Typeface  proxima = Typeface.createFromAsset(getAssets(),
				"fonts/MarkSimonsonProximaNovaRegular.otf");


		termsOfUse.setTypeface(proxima);

	} // -- End of findWidgetsById()

	private void setListener() {
		submitBtn.setOnClickListener(onClickHandler);
		signIn.setOnClickListener(onClickHandler);
		termsOfUse.setOnClickListener(onClickHandler);
	}

	@Override
	public void onBackPressed() {
		statMachineObj.nextState(RegistrationActivity.this, UIEventType.START, true, UIType.REGISTRATION_ACTIVITY,null);
	}

	private RegistrationRequestBean getRegisBean(String name, String email, String password, String mobileNum) {

		RegistrationRequestBean rBean = new RegistrationRequestBean();
		rBean.setName(name);
		rBean.setEmailid(email);
		rBean.setPassword(password);
		rBean.setMobile(mobileNum);
		rBean.setDevice_id(Common.getDeviceID(getApplicationContext()));
		rBean.setOpr_name(Common.getOperatorName(getApplicationContext()));
		rBean.setSw_version(Common.getSWVersion(getApplicationContext()));
		return rBean;
	}


	private void submitClick()
	{
		mELogger.debug("Entered RegistrationActivity:submitClick()");
		if(!Common.isNetworkAvailable(getApplicationContext()))
		{
			displayToast(getString(R.string.net_not_available), Gravity.TOP, false,  Toast.LENGTH_SHORT);
			return;
		}

		String orgname = orgNameText.getText().toString();
		String email = emailEditText.getText().toString();
		String pass = passEditText.getText().toString();
		pass = pass.trim();
		String mobile = mobileEditText.getText().toString();

		if(orgname.length()==0 || email.length()==0 || pass.length() ==0 || mobile.length()==0)
		{
			displayToast(getString(R.string.invalid_reg_detail), Gravity.CENTER, false,  Toast.LENGTH_SHORT);
			return;
		}
		if(!Common.isValidName(orgname) )
		{
			displayToast(getString(R.string.invalid_name), Gravity.CENTER, false,  Toast.LENGTH_SHORT);
			return;
		}
		if(!Common.isValidEmail(email))
		{
			displayToast(getString(R.string.invalid_email_id), Gravity.CENTER, false,  Toast.LENGTH_SHORT);
			return;
		}

		if(!Common.isValidMobile(mobile) )
		{
			displayToast(getString(R.string.invalid_mobile), Gravity.CENTER, false,  Toast.LENGTH_SHORT);
			return;
		}


		RegistrationRequestBean registrationBean = getRegisBean(orgname,email,pass, mobile);

		baseEngObj = Engine.getInstance();
		if((baseEngObj != null) && (registrationBean != null))
		{
			int serverRespCode = baseEngObj.registerUser(registrationBean);
			checkResponse(serverRespCode);
		}
		else
		{
			mELogger.error("RegistrationActivity:submitClick failed to send register request.");
			//displayToast("", gravity, flag, duration)
		}
	}

	@Override
	public void handleEvent(int evType,Object msgObj) {
		// TODO Auto-generated method stub
		if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
		{
			mELogger.debug("handleEvent() :server response is back for  eventType "+evType);
		}

		if(msgObj == null)
		{
			mELogger.error("HandleEvent response not found.");
			return;
		}

		switch (evType) {
		case EventType.REGISTRATION:
		{
			dismissProgressDialogue();
			RegistrationResponseBean registrationResp = (RegistrationResponseBean)msgObj;
			if(registrationResp.getStatus().equals("SUCCESS"))
			{
				statMachineObj.nextState(RegistrationActivity.this, UIEventType.REGISTERSUCCESS, true, UIType.REGISTRATION_ACTIVITY,null);
			}
			else if(registrationResp.getStatus().equals("ERROR"))
			{
				mELogger.error("Invalid Registration");

				switch(registrationResp.getErrorCode())
				{
				case ServerResponseCode.ALREADY_REGD:
				{

					displayToast(getString(R.string.already_registered), Gravity.TOP, false,  Toast.LENGTH_SHORT);
					emailEditText.setText("");
					passEditText.setText("");
					mobileEditText.setText("");
				}
				break;
				default:
				{
					displayToast(getString(R.string.reg_failed), Gravity.TOP, false,  Toast.LENGTH_SHORT);
					emailEditText.setText("");
					passEditText.setText("");
					mobileEditText.setText("");
				}
				break;

				}
			}
		}
		break;
		default:
			break;
		}
	}

}
