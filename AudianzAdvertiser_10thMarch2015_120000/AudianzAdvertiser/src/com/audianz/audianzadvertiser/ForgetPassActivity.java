package com.audianz.audianzadvertiser;

import com.audianz.beans.PasswordRequestBean;
import com.audianz.beans.ResponseBean;
import com.audianz.constants.APIConstant;
import com.audianz.constants.EventType;
import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;
import com.audianz.core.Engine;
import com.audianz.emcl.ELogger;
import com.audianz.utilities.Common;
import com.audianz.utilities.UIStateMachine;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPassActivity extends BaseActivity{

	private TextView reset_desc_tv;
	private TextView pass_reset_tv;
	private TextView pass_email;
	private Button pass_continue_btn;
	private LinearLayout ll_back_icon;
	private UIStateMachine 			uistate					= null;
	private ELogger 				mElogger 				= null;
	private Engine engObj;
	private static final String TAG ="ForgetPasswordActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		uistate  = UIStateMachine.getInstance();
		uistate.curActivity=this;
		if(mElogger == null)
			mElogger = new ELogger();
		mElogger.setTag(TAG);
		if(engObj==null)
			engObj= Engine.getInstance();
		initializeWidget();
		setListener();
	}

	/**
	 * This method is used to initialze all widgets of this activity
	 */
	private void initializeWidget()
	{

		pass_reset_tv      = (TextView)findViewById(R.id.header_reset_pwd);
		reset_desc_tv      = (TextView)findViewById(R.id.pass_reset__desc_tv);
		pass_email         = (TextView)findViewById(R.id.pass_email);
		pass_continue_btn  = (Button)findViewById(R.id.btn_continue);
		ll_back_icon       = (LinearLayout)findViewById(R.id.ll_forgetpass_back_icon);
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		pass_reset_tv.setTypeface(custom_font);
		reset_desc_tv.setTypeface(custom_font);
		pass_email.setTypeface(custom_font);
		pass_continue_btn.setTypeface(custom_font);
	}

	/**
	 * This method is used to clicklistener for widgets which need to be clicked on this Activity
	 */
	private void setListener()
	{

		pass_continue_btn.setOnClickListener(mCListener);	
		ll_back_icon.setOnClickListener(mCListener);
	}

	View.OnClickListener mCListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_continue:
				onForgetPassClick();
				break;
			case R.id.ll_forgetpass_back_icon:
				statMachineObj.nextState(ForgetPassActivity.this, UIEventType.BACKBUTTONCLICK, true, UIType.LOGIN_ACTIVITY,null);
				break;
			default:
				break;
			}
		}
	};

	private void onForgetPassClick()
	{
		if(!Common.isNetworkAvailable(getApplicationContext()))
		{
			displayToast(getString(R.string.net_not_available), Gravity.TOP, false,  Toast.LENGTH_SHORT);
			return;
		}

		String email = pass_email.getText().toString();
		if (!Common.isValidEmail(email)) {
			displayToast(getResources().getString(R.string.invalid_email_id), Gravity.TOP | Gravity.CENTER_HORIZONTAL, false,  Toast.LENGTH_SHORT);
			return;
		}

		PasswordRequestBean passBean  = new PasswordRequestBean();
		passBean.setEmailid(email);

		if((engObj != null) && (passBean != null))
		{
			int respCode = engObj.forgetPassword(passBean);

			if(respCode == -1)
			{
				if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
					mElogger.error("onClick() : null engine response for signInUser.");
			}

			checkResponse(respCode);

		}
		else
		{
			mElogger.error("LoginActivity:loginClick failed to send login request.");
		}
	}

	@Override
	public void handleEvent(int evType, Object msgObj) {
	
		if(msgObj == null)
		{
			displayToast("Password reset failed! Try again", Gravity.CENTER, false, Toast.LENGTH_SHORT);
			return;
		}
		
		switch (evType) {
		case EventType.FORGET_PASSWORD:
		{
		    dismissProgressDialogue();
		    
		    ResponseBean respBean = (ResponseBean)msgObj;
		    if(respBean.getStatus().equals(APIConstant.STATUS_SUCCESS))
		    {
		    	displayToast("You new password has been suceessfully sent to your email!", Gravity.CENTER, false, Toast.LENGTH_LONG);
		    	pass_email.setText(null);
		    	statMachineObj.nextState(ForgetPassActivity.this, UIEventType.FORGET_PASS_SUCCESS, true, UIType.LOGIN_ACTIVITY,null);
		    }
		    else if(respBean.getErrorCode()==APIConstant.EMAIL_SEND_FAILED)
		    {
		    	displayToast("Failed to send email! Please try again", Gravity.CENTER, false, Toast.LENGTH_SHORT);
		    }
		    else
		    {
		    	displayToast("This email is not registered!", Gravity.CENTER, false, Toast.LENGTH_SHORT);
		    }
		}
		break;

		default:
			break;
		}
	}

}
