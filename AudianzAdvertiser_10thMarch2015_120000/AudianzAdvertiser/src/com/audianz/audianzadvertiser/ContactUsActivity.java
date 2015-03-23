package com.audianz.audianzadvertiser;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactUsActivity extends BaseActivity{

	TextView  contact_us_tv =null;
	TextView header_contact;
	private LinearLayout ll_contact_back_icon;
	private View.OnClickListener mClickListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_us);

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
				case R.id.ll_contact_back_icon:
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
		ll_contact_back_icon  = (LinearLayout)findViewById(R.id.ll_contact_back_icon);
		contact_us_tv  = (TextView)findViewById(R.id.contact_us_tv);
		header_contact = (TextView)findViewById(R.id.header_contact);
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		contact_us_tv.setTypeface(custom_font);
		header_contact.setTypeface(custom_font);
	}

	private void setListener()
	{
		ll_contact_back_icon.setOnClickListener(mClickListener);
	}
}
