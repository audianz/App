package com.audianz.audianzadvertiser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;

public class StartActivity extends BaseActivity {
	
	private Button startBtn			= null;
	private Button signIn_btn		= null;
    private Button learnMore       = null;
	
    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		getWidedID();
		setListener();
	}


	private void getWidedID(){
		startBtn     = (Button)findViewById(R.id.startBtn);
		signIn_btn   = (Button)findViewById(R.id.signIn_btn);
		learnMore    = (Button)findViewById(R.id.learnBtn);
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
			      "fonts/helvetica_neue_regular.ttf");
		startBtn.setTypeface(custom_font);
		signIn_btn.setTypeface(custom_font);
		learnMore.setTypeface(custom_font);
	}


	private void setListener(){
		startBtn.setOnClickListener(mCListener);
		signIn_btn.setOnClickListener(mCListener);
		learnMore.setOnClickListener(mCListener);
	}

	View.OnClickListener mCListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			
			case R.id.startBtn:
				statMachineObj.nextState(StartActivity.this,UIEventType.REGISTRATION, true, UIType.START_ACTIVITY,null);			
				break;

			case R.id.signIn_btn:
				statMachineObj.nextState(StartActivity.this,UIEventType.LOGIN, true, UIType.START_ACTIVITY,null);	
				break;
			case R.id.learnBtn:
				statMachineObj.nextState(StartActivity.this, UIEventType.LEARN_MORE, false, UIType.START_ACTIVITY,null);
				break;
			default:
				break;
			}

		}
	};
	
	


	@Override
	public void onBackPressed() {
		finish();
		System.exit(0);
	}
	

}
