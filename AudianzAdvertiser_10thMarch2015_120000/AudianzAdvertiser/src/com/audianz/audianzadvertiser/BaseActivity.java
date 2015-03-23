package com.audianz.audianzadvertiser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.audianz.constants.InitResponseCode;
import com.audianz.constants.ServerResponseCode;
import com.audianz.core.Engine;
import com.audianz.emcl.ELogger;
import com.audianz.utilities.UIStateMachine;
/**
 * This class is base class for all activity classes.
 * @author 
 *
 */
public class BaseActivity extends Activity
{
	protected ProgressDialog pd = null;
	private ELogger mELogger = null; 
	private String TAG = "BaseActivity";
	protected  Engine baseEngObj = null;
	protected UIStateMachine statMachineObj = null;
	protected LongToast customToast;
	protected Button back_btn 		= null;
	protected final int LONG_TOAST = 2;

	protected int uiType = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		if(mELogger == null)
		     mELogger = new ELogger();
		mELogger.setTag(TAG);
		baseEngObj = Engine.engObj;
		if(baseEngObj == null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mELogger.error("onCreate() : engine object is null initializeing.");
			if(!init())
			{
				if(Engine.IS_DEVELOPMENT_RELEASE)
					mELogger.error("onCreate() : init failed.");
				this.finish();
				return;
			}
		}	
		statMachineObj = baseEngObj.statMachineObj;
		statMachineObj.curActivity = this;
	}

	/**
	 * This function is responsible for initializing UIStateMachine and Engine. 
	 * @return true if successfully initialization happened, otherwise false.
	 */
	protected boolean init()
	{
		if(baseEngObj==null)
		     baseEngObj = Engine.getInstance();
		
		long time = System.currentTimeMillis();
		int initResp = baseEngObj.init(getApplicationContext()) ;
		if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
			mELogger.info("init() : time taken in initialization of engine : "+(System.currentTimeMillis()-time));
		if(initResp == InitResponseCode.INIT_SUCCESS)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.info("init() : engine initialized successfully");
		}
		else
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.error("init() : engine initialization fail initialization response code is : "+initResp);
			return false;
		}
		if(statMachineObj == null)
		     statMachineObj = baseEngObj.statMachineObj;
		return true;
	}//End of init()


	

	/**
	 * This function is responsible for handling all the response codes that are returned by the engine thread. 
	 * @param respCode
	 * @return
	 */
	protected boolean checkResponse(int respCode)
	{
		boolean successFlag = false;
		if(respCode == ServerResponseCode.ERROR)
		{  
			dismissProgressDialogue();  
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.error("checkResponse() : ServerResponseCode is an Error.");    
			return successFlag;
		}  
		else if(respCode == ServerResponseCode.IN_PROGRESS)
		{
			showProgressDialogue(getApplicationContext().getString(R.string.loading));
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.debug("checkResponse() : ServerResponseCode is representing Request in progress.");
			return successFlag;
		}
		else if(respCode == ServerResponseCode.SUCCESS)
		{
			dismissProgressDialogue();
			successFlag = true;
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.debug("checkResponse() : ServerResponseCode is Success");   
		}
		else if(respCode == ServerResponseCode.NET_NOT_AVAILABLE)
		{
			dismissProgressDialogue();
			displayToast(getString(R.string.net_not_available), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, false,  Toast.LENGTH_SHORT);
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.error("checkResponse() : Network not availabe.");
		}
		else if (respCode == ServerResponseCode.REQ_TIME_OUT )
		{
			dismissProgressDialogue();
			displayToast(getString(R.string.net_time_out), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, false,  Toast.LENGTH_SHORT);
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.error("checkResponse() : connection timeout");
		}
		else if(respCode == ServerResponseCode.NET_EXCEPION)
		{
			dismissProgressDialogue();
			displayToast(getString(R.string.server_not_rechable), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, false,  Toast.LENGTH_SHORT);
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.error("checkResponse() : net exception");
		}
		else if(respCode == ServerResponseCode.INVALID_URL)
		{
			dismissProgressDialogue();
			displayToast(getString(R.string.server_not_found), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, false,  Toast.LENGTH_SHORT);
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.error("checkResponse() : connection timeout");
		}
		else if(respCode == ServerResponseCode.ADD_EVENT_FAIL)
		{
			dismissProgressDialogue();
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.error("checkResponse() : Add event failure");
		} 
		else if(respCode == ServerResponseCode.NULL_DATA)
		{
			dismissProgressDialogue();
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.error("checkResponse() : return data is null.");
		}
		else
		{
			dismissProgressDialogue();
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.debug("checkResponse() : not availabe.");
		}

		return successFlag;
	}//End of checkResponse()

	@Override
	protected void onResume() 
	{
		super.onResume();
	}
	/**
	 * This function will show progress dialogue with given message 
	 * @param msg String (message to show on progress dialogue if message is null then progress dialogue is shown without message)
	 */
	public void showProgressDialogue(String msg)
	{
		if(pd == null)
		{
			pd = new ProgressDialog(this, R.style.CustomDialogTheme);
			pd.setTitle("Processing");
		}
		if(msg != null) 
		{
			pd.setMessage(msg);
		}
		/*
		 * This will cancel the progress bar on back button otherwise set it false
		 */
		pd.setCancelable(false);
		if(!pd.isShowing())
			pd.show();
	}

	/**
	 * This function will dismiss progress dialogue with given message 
	 * @param msg String 
	 */
	public void dismissProgressDialogue()
	{
		if(pd != null)
		{
			if(pd.isShowing())
				pd.dismiss();
			pd = null ;
		}
	}//End of dismissProgressDialogue()


	public void handleEvent(int evType, Object obj)
	{
		
	}

	/**
	 * This method is responsible for displaying custom toast message for given string value.
	 * @param str
	 * @param flag TODO
	 */
	Toast toast = null;
	@SuppressLint("InflateParams")
	protected void displayToast(String str, int gravity, boolean flag, int duration)
	{
		LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
		View view = mInflater.inflate(R.layout.toast, null);
		TextView text = (TextView)view.findViewById(R.id.tv_toast);
		if(flag)
		{
			ImageView sentIcon = (ImageView) view.findViewById(R.id.toast_iv);
			sentIcon.setVisibility(View.VISIBLE);
		}
		toast = new Toast(getApplicationContext());
		if(baseEngObj.cnfigReaderObj.isHDPIDevice())
		{
			toast.setGravity(gravity, 0, 200);
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.debug("displayToast() :decice is hdpi");
		}
		else if(baseEngObj.cnfigReaderObj.isLDPIDevice())
		{
			toast.setGravity(gravity, 0, 70);
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.debug("displayToast() :decice is ldpi");
		}
		else if(baseEngObj.cnfigReaderObj.isMDPIDevice())
		{
			toast.setGravity(gravity, 0, 120); 
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.debug("displayToast() :decice is mdpi");
		}
		else
		{
			toast.setGravity(gravity, 0, 230); 
			if(Engine.IS_DEVELOPMENT_RELEASE)//This log is not print now.
				mELogger.debug("displayToast() :decice is xhdpi");
		}

		if(duration == LONG_TOAST)
		{

			text.setText(str);
			toast.setView(view);
			customToast = new LongToast(toast);
			customToast.start();
		}
		else
		{
			text.setText(str);
			toast.setView(view);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		}
	}//End of displayToast()


	/**
	 * This method is used to cancel the toast message.
	 */
	protected void cancelToast()
	{
		if(toast != null)
		{
			toast.cancel();
		}
	}

	/**
	 * This class LongToast is used to increase the display time of toast message.
	 * Display time for toast message can be customized in this class.
	 * @author Vikram
	 *
	 */
	class LongToast extends CountDownTimer
	{
		Toast longToast;
		public LongToast(Toast toast)
		{
			super(4000,1000);
			this.longToast = toast;
			//start();
		}
		public void onTick(long millisUntilFinished) {longToast.show();}
		public void onFinish() {longToast.cancel();}

	}

}
