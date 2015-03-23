package com.audianz.audianzadvertiser;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.audianz.constants.APIConstant;

public class TabHostActivity extends TabActivity {
	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_host);

		Resources res = getResources(); 
		final TabHost tabHost = getTabHost(); 
		TabHost.TabSpec spec; 

		Intent intent; 

		Intent reportIntent = getIntent();
		int curTab=reportIntent.getIntExtra(APIConstant.CURRENT_TAB, 0);
	
		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, MapActivity.class);
		
		if(curTab == APIConstant.REPORT_TAB)
		{
			spec = tabHost.newTabSpec("0").setIndicator("Promote", res.getDrawable(R.drawable.promote_white)).setContent(intent);
			tabHost.addTab(spec);


			intent = new Intent().setClass(this, ReportActivity.class);
			spec = tabHost.newTabSpec("1").setIndicator("Report", res.getDrawable(R.drawable.report_red)).setContent(intent);

			tabHost.addTab(spec);


			intent = new Intent().setClass(this, SetttingActivity.class);
			spec = tabHost.newTabSpec("2").setIndicator("Settings",res.getDrawable(R.drawable.setting_white)).setContent(intent);
			tabHost.addTab(spec);
		}
		else
		{
			spec = tabHost.newTabSpec("0").setIndicator("Promote", res.getDrawable(R.drawable.promote_red)).setContent(intent);
			tabHost.addTab(spec);


			intent = new Intent().setClass(this, ReportActivity.class);
			spec = tabHost.newTabSpec("1").setIndicator("Report", res.getDrawable(R.drawable.report_white)).setContent(intent);

			tabHost.addTab(spec);


			intent = new Intent().setClass(this, SetttingActivity.class);
			spec = tabHost.newTabSpec("2").setIndicator("Settings",res.getDrawable(R.drawable.setting_white)).setContent(intent);
			tabHost.addTab(spec);
		}
		
		

		tabHost.setBackgroundColor(getResources().getColor(R.color.bgColor));

		
		tabHost.setCurrentTab(curTab);
		

		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) 
		{
			TextView tv   = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			if(i==curTab)
				tv.setTextColor(getResources().getColor(R.color.redBgColor));
			else
				tv.setTextColor(getResources().getColor(R.color.tabgrayColor));
			tabHost.getTabWidget().getChildAt(i).setBackground(null);
		}


		final ImageView ev_promote  = (ImageView)tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.icon);
		final ImageView ev_report   = (ImageView)tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.icon);
		final ImageView ev_setting  = (ImageView)tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.icon);

		final TextView tv_promote         = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
		final TextView tv_report          = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
		final TextView tv_setting         = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);

		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		
		tv_promote.setTypeface(custom_font);
		tv_report.setTypeface(custom_font);
		tv_setting.setTypeface(custom_font);
		
		OnTabChangeListener onTabChanged = new OnTabChangeListener(){ 

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub

				switch (Integer.valueOf(tabId)) {
				case 0:
				{

					ev_promote.setImageDrawable(getResources().getDrawable(R.drawable.promote_red));
					ev_report.setImageDrawable(getResources().getDrawable(R.drawable.report_white));
					ev_setting.setImageDrawable(getResources().getDrawable(R.drawable.setting_white));

					tv_promote.setTextColor(getResources().getColor(R.color.redBgColor));
					tv_report.setTextColor(getResources().getColor(R.color.tabgrayColor));
					tv_setting.setTextColor(getResources().getColor(R.color.tabgrayColor));

				}
				break;
				case 1:
				{
					ev_promote.setImageDrawable(getResources().getDrawable(R.drawable.promote_white));
					ev_report.setImageDrawable(getResources().getDrawable(R.drawable.report_red));
					ev_setting.setImageDrawable(getResources().getDrawable(R.drawable.setting_white));

					tv_promote.setTextColor(getResources().getColor(R.color.tabgrayColor));
					tv_report.setTextColor(getResources().getColor(R.color.redBgColor));
					tv_setting.setTextColor(getResources().getColor(R.color.tabgrayColor));
				}
				break;
				case 2:
				{
					ev_promote.setImageDrawable(getResources().getDrawable(R.drawable.promote_white));
					ev_report.setImageDrawable(getResources().getDrawable(R.drawable.report_white));
					ev_setting.setImageDrawable(getResources().getDrawable(R.drawable.setting_red));

					tv_promote.setTextColor(getResources().getColor(R.color.tabgrayColor));
					tv_report.setTextColor(getResources().getColor(R.color.tabgrayColor));
					tv_setting.setTextColor(getResources().getColor(R.color.redBgColor));
				}
				break;
				default:
					break;
				}
			}
		};


		tabHost.setOnTabChangedListener(onTabChanged);


	}

}