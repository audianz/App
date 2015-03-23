package com.audianz.audianzadvertiser;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * This class is used to display information about application
 * @author 
 *
 */
public class AboutActivity extends  BaseActivity {

	
    
    TextView  about_us_tv =null;
    TextView header_about;
    private LinearLayout ll_aboutus_back_icon;
    private View.OnClickListener mClickListener = null;
    	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		initializeWidgetId();
		implementOnClickHandler();
		setListener();
		
	
		
	}
	
	private void implementOnClickHandler()
	{
		mClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.ll_aboutus_back_icon:
					finish();
					break;

				default:
					break;
				}
			}
		};
	}
	private void initializeWidgetId()
	{
		ll_aboutus_back_icon  = (LinearLayout)findViewById(R.id.ll_aboutus_back_icon);
		about_us_tv  = (TextView)findViewById(R.id.about_us_textview);
		header_about   = (TextView)findViewById(R.id.header_about);
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		about_us_tv.setTypeface(custom_font);
		header_about.setTypeface(custom_font);
	}
	
	private void setListener()
	{
		ll_aboutus_back_icon.setOnClickListener(mClickListener);
	}
}
