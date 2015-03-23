package com.audianz.audianzadvertiser;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;
import com.audianz.core.Engine;

/**
 * This is the splash activity, it is displayed for 1 sec and the initialization of 
 * the application is performed from this activity. 
 * @author shyam
 *
 */

public class SplashActivity extends BaseActivity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 1000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Engine engObj =Engine.getInstance();
				if(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID()!=0)
				{
					statMachineObj.nextState(SplashActivity.this,UIEventType.LOGGEDIN, true, UIType.SPLASH_ACTIVITY,null);
				}
				else
				{
					statMachineObj.nextState(SplashActivity.this,UIEventType.START, true, UIType.SPLASH_ACTIVITY,null);
				}
			}
		}, SPLASH_TIME_OUT);
	}
}
