package com.audianz.audianzadvertiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.audianz.utilities.FAQExpandableListAdapter;

public class FAQActivity  extends BaseActivity{

	FAQExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	TextView header_faq;
	HashMap<String, List<String>> listDataChild;
	
	private LinearLayout ll_faq_back_icon;
	private View.OnClickListener mClickListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);
		
		initializeWidgetId();
		implementOnClickHandler();
		setListener();
		prepareListData();
		listAdapter = new FAQExpandableListAdapter(this, listDataHeader, listDataChild);
		expListView.setAdapter(listAdapter);
	}

	private void implementOnClickHandler()
	{
		mClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.ll_faq_back_icon:
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
		ll_faq_back_icon  = (LinearLayout)findViewById(R.id.ll_faq_back_icon);
		expListView       = (ExpandableListView)findViewById(R.id.faqlvExp);
		header_faq        = (TextView)findViewById(R.id.header_faq);
		
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		header_faq.setTypeface(custom_font);
	}
	private void setListener()
	{
		//back_btn.setOnClickListener(mClickListener);
		ll_faq_back_icon.setOnClickListener(mClickListener);
	}
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add(getString(R.string.faq_ques1));
		listDataHeader.add(getString(R.string.faq_ques2));
		listDataHeader.add(getString(R.string.faq_ques3));
		listDataHeader.add(getString(R.string.faq_ques4));
		listDataHeader.add(getString(R.string.faq_ques5));
		listDataHeader.add(getString(R.string.faq_ques6));
		listDataHeader.add(getString(R.string.faq_ques7));
		listDataHeader.add(getString(R.string.faq_ques8));


		// Adding child data
		List<String> faq1 = new ArrayList<String>();
		faq1.add(getString(R.string.faq_ans1));

		List<String> faq2 = new ArrayList<String>();
		faq2.add(getString(R.string.faq_ans2));

		List<String> faq3 = new ArrayList<String>();
		faq3.add(getString(R.string.faq_ans3));
		List<String> faq4 = new ArrayList<String>();
		faq4.add(getString(R.string.faq_ans4));
		List<String> faq5 = new ArrayList<String>();
		faq5.add(getString(R.string.faq_ans5));
		List<String> faq6 = new ArrayList<String>();
		faq6.add(getString(R.string.faq_ans6));
		List<String> faq7 = new ArrayList<String>();
		faq7.add(getString(R.string.faq_ans7));
		List<String> faq8 = new ArrayList<String>();
		faq8.add(getString(R.string.faq_ans8));


		listDataChild.put(listDataHeader.get(0), faq1); // Header, Child data
		listDataChild.put(listDataHeader.get(1), faq2);
		listDataChild.put(listDataHeader.get(2), faq3);
		listDataChild.put(listDataHeader.get(3), faq4);
		listDataChild.put(listDataHeader.get(4), faq5);
		listDataChild.put(listDataHeader.get(5), faq6);
		listDataChild.put(listDataHeader.get(6), faq7);
		listDataChild.put(listDataHeader.get(7), faq8);
	}
}
