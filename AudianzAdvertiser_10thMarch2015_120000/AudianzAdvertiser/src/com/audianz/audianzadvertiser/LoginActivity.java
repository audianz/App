package com.audianz.audianzadvertiser;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.audianz.beans.LoginRequestBean;
import com.audianz.beans.RegistrationResponseBean;
import com.audianz.constants.APIConstant;
import com.audianz.constants.EventType;
import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;
import com.audianz.core.Engine;
import com.audianz.emcl.ELogger;
import com.audianz.utilities.Common;
import com.audianz.utilities.UIStateMachine;

public class LoginActivity extends BaseActivity {
	private EditText emailEditText = null;
	private EditText passEditText = null;
	private Button loginBtn = null;
	private Button registrationBtn = null;
	private TextView txt_forget_pass=null;
	private TextView txt_not_register=null;	
	private LinearLayout ll_login_back_icon;
	private TextView header_login;
	ImageView my_image;

	private View.OnClickListener onClickHandler = null;
	//...................... Class variables
	private UIStateMachine 			uistate					= null;
	private ELogger 				mELogger 				= null;

	//......................Class constants
	private final String 			TAG 					= "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//....... Application initialization
		uistate=UIStateMachine.getInstance();
		uistate.curActivity= this;

		if(mELogger == null)
			mELogger = new ELogger();
		mELogger.setTag(TAG);

		findWidgetsById();
		implementOnClickHandler();
		setListener();
	}

	/**
	 * This method is used to handle all the click events on View.
	 */
	private void implementOnClickHandler() {

		onClickHandler = new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				switch (view.getId()) {
				case R.id.btnLogin:
					loginClick();
					break;

				case R.id.btnRegistration:
					statMachineObj.nextState(LoginActivity.this, UIEventType.REGISTRATION, true, UIType.LOGIN_ACTIVITY,null);
					break;

				case R.id.ll_login_back_icon :
					statMachineObj.nextState(LoginActivity.this, UIEventType.START, true, UIType.LOGIN_ACTIVITY,null);
					break;
				case R.id.txt_fgt_pass:
					statMachineObj.nextState(LoginActivity.this, UIEventType.FORGET_PASSWORD, true, UIType.LOGIN_ACTIVITY,null);
					break;
				default:
					break;
				}
			}
		};
	}


	@Override
	public void onBackPressed() {
		statMachineObj.nextState(LoginActivity.this, UIEventType.START, true, UIType.LOGIN_ACTIVITY,null);
	}
	/**
	 * This method is for finding widgets by their generated id.
	 */
	private void findWidgetsById()
	{	
		ll_login_back_icon = (LinearLayout)findViewById(R.id.ll_login_back_icon);
		emailEditText = (EditText) findViewById(R.id.txtEmail);
		passEditText = (EditText) findViewById(R.id.txtPassword);
		loginBtn = ((Button)findViewById(R.id.btnLogin));
		registrationBtn = ((Button)findViewById(R.id.btnRegistration));
		txt_forget_pass =(TextView)findViewById(R.id.txt_fgt_pass);
		txt_not_register=(TextView)findViewById(R.id.txt_nt_regd);
		header_login    = (TextView)findViewById(R.id.header_login);
		
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		emailEditText.setTypeface(custom_font);
		passEditText.setTypeface(custom_font);
		loginBtn.setTypeface(custom_font);
		registrationBtn.setTypeface(custom_font);
		txt_forget_pass.setTypeface(custom_font);
		txt_not_register.setTypeface(custom_font);
		header_login.setTypeface(custom_font);


	} // -- End of findWidgetsById()

	private void setListener(){
		registrationBtn.setOnClickListener(onClickHandler);
		ll_login_back_icon.setOnClickListener(onClickHandler);
		loginBtn.setOnClickListener(onClickHandler);
		txt_forget_pass.setOnClickListener(onClickHandler);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private LoginRequestBean getUserBean(String userId, String password) {

		LoginRequestBean uBean = new LoginRequestBean();
		uBean.setEmailid(userId);
		uBean.setPassword(password);

		return uBean;
	}

	//.................. UI CLick handlers
	private void loginClick() {

		mELogger.debug("Entered LoginActivity:loginClick");
		if(!Common.isNetworkAvailable(getApplicationContext()))
		{
			displayToast(getString(R.string.net_not_available), Gravity.TOP, false,  Toast.LENGTH_SHORT);
			return;
		}

		String email = emailEditText.getText().toString();
		if (!Common.isValidEmail(email)) {
			displayToast(getResources().getString(R.string.invalid_email_id), Gravity.TOP | Gravity.CENTER_HORIZONTAL, false,  Toast.LENGTH_SHORT);
			return;
		}

		final String pass = passEditText.getText().toString();

		if(pass.length()<1 && TextUtils.isEmpty(pass))
		{
			displayToast(getResources().getString(R.string.invalid_pass), Gravity.TOP | Gravity.CENTER_HORIZONTAL, false,  Toast.LENGTH_SHORT);
			return;
		}
		LoginRequestBean signInBean = getUserBean(email,pass);

		baseEngObj = Engine.getInstance();
		if((baseEngObj != null) && (signInBean != null))
		{
			int respCode = baseEngObj.signInUser(signInBean);

			if(respCode == -1)
			{
				if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
					mELogger.error("onClick() : null engine response for signInUser.");
			}

			checkResponse(respCode);

		}
		else
		{
			mELogger.error("LoginActivity:loginClick failed to send login request.");
		}
	}



	@Override
	public void handleEvent(int evType,Object msgObj) {

		// TODO Auto-generated method stub
		if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
		{
			mELogger.debug("handleEvent() : server response is back for eventType  "+evType);
		}

		if(msgObj == null)
		{
			displayToast("Failed to login! Try again", Gravity.CENTER, false, Toast.LENGTH_SHORT);
			return;
		}

		switch (evType) {
		case EventType.SIGNIN:
		{	
			dismissProgressDialogue();	
			RegistrationResponseBean logInResp = (RegistrationResponseBean)msgObj;

			if(logInResp.getStatus().equals(APIConstant.STATUS_SUCCESS))
			{
				baseEngObj = Engine.getInstance();
				baseEngObj.addFetchCampaigndata();
				statMachineObj.nextState(LoginActivity.this, UIEventType.TABHOST, true, UIType.LOGIN_ACTIVITY,null);
			}
			else if(logInResp.getStatus().equals("ERROR"))
			{
				String errorMsg = "Invalid loginId or password";
				displayToast(errorMsg, Gravity.TOP | Gravity.CENTER_HORIZONTAL, false,  Toast.LENGTH_SHORT);
				emailEditText.setText("");
				passEditText.setText("");
				Selection.setSelection(emailEditText.getText(), emailEditText.getText().toString().length());
				mELogger.error("Invalid emaiId or password");
			}
		}
		break;
		default:
			break;
		}
	}

}