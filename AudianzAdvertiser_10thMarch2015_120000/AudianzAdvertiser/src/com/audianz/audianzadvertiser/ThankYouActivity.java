package com.audianz.audianzadvertiser;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.audianz.beans.CampaignListBean;
import com.audianz.constants.APIConstant;
import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;
import com.audianz.core.Engine;
import com.audianz.emcl.ELogger;
import com.audianz.utilities.DateUtil;

public class ThankYouActivity extends BaseActivity{

	private Button see_report;
	private TextView thank_for_promo_tv;
	private TextView lbl_order_id;
	private TextView order_id_tv;
	private TextView lbl_camp_detail;
	private TextView lbl_camp_name;
	private TextView camp_name_tv;
	private TextView lbl_total_imp;
	private TextView total_imp_tv;
	private TextView lbl_start_date;
	private TextView start_date_tv;
	private TextView thank_business_name_tv;
	private TextView lbl_amount_paid;
	private TextView amount_paid_tv;
	private TextView header_thankyou;

	private Engine engObj;
	private ELogger mElogger;
	private final String TAG = "ThankYouActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thankyou);
		if(mElogger==null)
			mElogger = new ELogger();
		mElogger.setTag(TAG);
		if(engObj==null)
			engObj = Engine.getInstance();
		getWidgedID();
		setListener();
		setCampaignDetail();
	}

	private void setCampaignDetail()
	{
		ArrayList<CampaignListBean>  campaignBean = engObj.audiDbUtil.getCampaignList(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());
		if(campaignBean!=null && campaignBean.size()>0)
		{
			CampaignListBean bean = campaignBean.get(0);
			if(bean!=null)
			{
				if(bean.getCamp_name()!=null)
				{
					camp_name_tv.setText(bean.getCamp_name());
				}
				total_imp_tv.setText(String.valueOf(bean.getTotal_imp()));
				if(bean.getStart_date()!=0)
				{
					DateUtil dt = new DateUtil();
					String startdate = dt.myDateFormatter(bean.getStart_date()*1000, "MMM d yyyy");
					start_date_tv.setText(startdate);
				}
				if(bean.getOrder_id()!=null)
					order_id_tv.setText(bean.getOrder_id());
				amount_paid_tv.setText(String.valueOf(bean.getOrder_amount()));
			}
			else
			{
				if(Engine.IS_DEVELOPMENT_RELEASE)
					mElogger.error("setCampaignDetail() bean is null");
			}
		}
		else
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mElogger.error("setCampaignDetail() CampaignList bean is null");
		}

	}
	private void getWidgedID()
	{
		see_report           = (Button)findViewById(R.id.see_report_btn);
		thank_for_promo_tv   = (TextView)findViewById(R.id.thank_for_promo_tv);
		lbl_order_id         = (TextView)findViewById(R.id.lbl_order_id);
		order_id_tv          = (TextView)findViewById(R.id.order_id_tv);
		lbl_camp_detail      = (TextView)findViewById(R.id.lbl_camp_detail);
		lbl_camp_name        = (TextView)findViewById(R.id.lbl_camp_name);
		camp_name_tv         = (TextView)findViewById(R.id.camp_name_tv);
		lbl_total_imp        = (TextView)findViewById(R.id.lbl_total_imp);
		total_imp_tv         = (TextView)findViewById(R.id.total_imp_tv);
		lbl_start_date       = (TextView)findViewById(R.id.lbl_start_date);
		start_date_tv        = (TextView)findViewById(R.id.start_date_tv);
		thank_business_name_tv= (TextView)findViewById(R.id.thank_business_name_tv);
		lbl_amount_paid       = (TextView)findViewById(R.id.lbl_amount_paid);
		amount_paid_tv       = (TextView)findViewById(R.id.amount_paid_tv);
		header_thankyou       = (TextView)findViewById(R.id.header_thankyou);
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		see_report.setTypeface(custom_font);
		thank_for_promo_tv.setTypeface(custom_font, 1);
		lbl_order_id.setTypeface(custom_font);
		order_id_tv.setTypeface(custom_font);
		lbl_camp_detail.setTypeface(custom_font, 1);
		lbl_camp_name.setTypeface(custom_font);
		camp_name_tv.setTypeface(custom_font);
		lbl_total_imp.setTypeface(custom_font);
		total_imp_tv.setTypeface(custom_font);
		lbl_start_date.setTypeface(custom_font);
		start_date_tv.setTypeface(custom_font);
		thank_business_name_tv.setTypeface(custom_font);
		lbl_amount_paid .setTypeface(custom_font);
		amount_paid_tv.setTypeface(custom_font);
		header_thankyou.setTypeface(custom_font);
	}

	private void setListener()
	{
		see_report.setOnClickListener(mCListener);
	}

	View.OnClickListener mCListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.see_report_btn:
			{
				Intent intent = new Intent(getApplicationContext(), TabHostActivity.class);
				intent.putExtra(APIConstant.CURRENT_TAB,APIConstant.REPORT_TAB);
				startActivity(intent);
				finish();
			}
			break;

			default:
				break;
			}
		}
	}; 
	
	@Override
	public void onBackPressed() {
		statMachineObj.nextState(ThankYouActivity.this, UIEventType.REPORTCLICK, true, UIType.REPORTACTIVITY,null);
	}
}

