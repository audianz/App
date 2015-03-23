package com.audianz.audianzadvertiser;

import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.audianz.beans.CampaignListBean;
import com.audianz.beans.ChildItem;
import com.audianz.beans.FetchCampaignResponse;
import com.audianz.beans.GroupItem;
import com.audianz.beans.RegistrationResponseBean;
import com.audianz.beans.UpdateCampaignRequest;
import com.audianz.constants.APIConstant;
import com.audianz.constants.EventType;
import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;
import com.audianz.core.Engine;
import com.audianz.emcl.ELogger;
import com.audianz.utilities.Common;
import com.audianz.utilities.ExpandableListAdapter;
import com.audianz.utilities.UIStateMachine;
import com.google.android.gms.internal.gb;

public class ReportActivity extends BaseActivity {

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	private ArrayList<CampaignListBean> campList=null;
	private ArrayList<GroupItem> groupItems = null;


	private TextView no_report =null;
	private String act=null;
	private TextView see_report =null;
	private Button promote_btn=null;
	private LinearLayout ll_report_explist = null;
	private LinearLayout ll_no_report_txt =null;
	private LinearLayout ll_no_report_btn =null;
	private TextView header_report;
	LinearLayout ll_refresh_btn=null;
	Button refresh_btn=null;

	private ELogger mELogger    =null;
	private UIStateMachine uistate  =null;
	private Engine engObj =null;
	private final String TAG  ="REPORTACTIVITY";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(engObj==null)
			engObj = Engine.engObj;

		uistate = UIStateMachine.getInstance();
		uistate.curActivity=this;

		if(mELogger == null)
			mELogger = new ELogger();
		mELogger.setTag(TAG);

		setContentView(R.layout.activity_report);
		initializeWidget();
		setClickListener();
		fetchReportData();
		
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				
				
				setAdapterInList();
				return false;
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		uistate.curActivity=this;
		getCampaignDataList();
		if(campList==null || campList.size()==0)
		{
			ll_report_explist.setVisibility(View.GONE);
			ll_no_report_btn.setVisibility(View.VISIBLE);
			ll_no_report_txt.setVisibility(View.VISIBLE);
		}
		else
		{	
			setAdapterInList();
			ll_report_explist.setVisibility(View.VISIBLE);
			ll_no_report_btn.setVisibility(View.GONE);
			ll_no_report_txt.setVisibility(View.GONE);	
		}


	}

	private void fetchReportData()
	{
		int campSererResp   = engObj.addFetchCampaigndata();

		if(campSererResp == -1)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mELogger.error(" fetch campaign response is null");
		}
		checkResponse(campSererResp);
	}
	/*
	 * Preparing the list data
	 */
	private void getCampaignDataList()
	{
		ArrayList<CampaignListBean>  campaignBean = engObj.audiDbUtil.getCampaignList(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());

		if(campList == null)
			campList = new ArrayList<CampaignListBean>();
		campList.clear();
		if(campaignBean!=null)
		{
			campList.addAll(campaignBean);	
		}


		if(campList!=null)
		{
			groupItems = new ArrayList<GroupItem>();

			for(CampaignListBean cmpBean : campList )
			{
				GroupItem group = new GroupItem();
				ChildItem child = new ChildItem();

				group.setCamp_name(cmpBean.getCamp_name());
				group.setCampaignid(cmpBean.getCamp_id());
				group.setPromo_msg(cmpBean.getPromo_msg());
				group.setLat(cmpBean.getLat());
				group.setLon(cmpBean.getLon());

				child.setCamp_id(cmpBean.getCamp_id());
				child.setStart_date(cmpBean.getStart_date());
				child.setTotal_imp(cmpBean.getTotal_imp());
				child.setShown_imp(cmpBean.getShown_imp());
				child.setCall(cmpBean.getCall());
				child.setWeb(cmpBean.getWeb());
				child.setMap(cmpBean.getMap());
				child.setAction(cmpBean.getAction());
				
				mELogger.error("checking for action"+cmpBean.getAction());
				
				child.setCamp_status(cmpBean.getCamp_status());
				child.setCamp_msg(cmpBean.getPromo_msg());
				ArrayList<ChildItem> childList = new ArrayList<ChildItem>();
				childList.add(child);
				group.setItems(childList);

				groupItems.add(group);
			}
		}
		else
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mELogger.error("campList is null");

		}


	}

	private void setAdapterInList() {

		if(listAdapter==null)
		{
			listAdapter = new ExpandableListAdapter(this, groupItems,mCListener);
			expListView.setAdapter(listAdapter);

		}
		else
		{
			listAdapter.updateAdapter(groupItems);
			listAdapter.notifyDataSetInvalidated();
		}

	}
	private void initializeWidget() {
		// TODO Auto-generated method stub

		no_report   = (TextView)findViewById(R.id.no_report_tv);
		see_report  = (TextView)findViewById(R.id.you_will_see_tv);
		promote_btn = (Button)findViewById(R.id.report_promote_btn);

		ll_report_explist = (LinearLayout)findViewById(R.id.ll_report_explist);
		ll_no_report_txt  = (LinearLayout)findViewById(R.id.ll_no_report_txt);
		ll_no_report_btn  = (LinearLayout)findViewById(R.id.ll_no_report_btn);
		expListView       = (ExpandableListView) findViewById(R.id.lvExp);
		header_report     = (TextView)findViewById(R.id.header_report);
		ll_refresh_btn    = (LinearLayout)findViewById(R.id.ll_refresh_icon);
		refresh_btn       = (Button)findViewById(R.id.refresh_btn);
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		promote_btn.setTypeface(custom_font);
		header_report.setTypeface(custom_font);
		Typeface  proxima = Typeface.createFromAsset(getAssets(),
				"fonts/MarkSimonsonProximaNovaRegular.otf");
		no_report.setTypeface(proxima);
		see_report.setTypeface(proxima);
	}

	private void setClickListener()
	{
		promote_btn.setOnClickListener(mCListener);
		refresh_btn.setOnClickListener(mCListener);
		ll_refresh_btn.setOnClickListener(mCListener);
	}


	View.OnClickListener mCListener  = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.report_promote_btn:
				uistate.nextState(ReportActivity.this, UIEventType.PROMOTE, true, UIType.REPORTACTIVITY,null);
				break;
			case R.id.report_reRun_btn:
			{
				int position = (Integer)v.getTag(R.integer.campaign_position);
				GroupItem gBean = groupItems.get(position);
				Intent intent = new Intent(getApplicationContext(), PromoteNowActivity.class);
				intent.putExtra(APIConstant.CAMPAIGN_MSG_KEY, gBean.getPromo_msg());
				intent.putExtra(APIConstant.CAMPAIGN_LAT,gBean.getLat());
				intent.putExtra(APIConstant.CAMPAIGN_LON,gBean.getLon());
				startActivity(intent);
				finish();

			}
			break;
			case R.id.report_pause_btn:
			{
				if(!Common.isNetworkAvailable(getApplicationContext()))
				{
					displayToast(getString(R.string.net_not_available), Gravity.CENTER, false, Toast.LENGTH_SHORT);
					return;
				}
				int position = (Integer)v.getTag(R.integer.campaign_position);
				GroupItem gBean = groupItems.get(position);

				UpdateCampaignRequest updateBean = new UpdateCampaignRequest();
				updateBean.setClientid(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());
				updateBean.setCampaignid(gBean.getCampaignid());
				updateBean.setCamp_status(APIConstant.STATUS_PAUSED);

				engObj.updateCampaignStatus(updateBean);

			}
			break;
			case R.id.report_run_btn:
			{

				if(!Common.isNetworkAvailable(getApplicationContext()))
				{
					displayToast(getString(R.string.net_not_available), Gravity.CENTER, false, Toast.LENGTH_SHORT);
					return;
				}
				int position = (Integer)v.getTag(R.integer.campaign_position);
				GroupItem gBean = groupItems.get(position);

				UpdateCampaignRequest updateBean = new UpdateCampaignRequest();
				updateBean.setClientid(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());
				updateBean.setCampaignid(gBean.getCampaignid());
				updateBean.setCamp_status(APIConstant.STATUS_RUNNING);

				if(engObj!=null && updateBean!=null)
				{
					int respCode  = engObj.updateCampaignStatus(updateBean);
					checkResponse(respCode);
				}
			}	
			break;
			case R.id.edit_campaign_btn:
			{
				if(!Common.isNetworkAvailable(getApplicationContext()))
				{
					displayToast(getString(R.string.net_not_available), Gravity.CENTER, false, Toast.LENGTH_SHORT);
					return;
				}
				
				int position = (Integer)v.getTag(R.integer.campaign_position);
				GroupItem gBean = groupItems.get(position);
				ChildItem cBean = gBean.getItems().get(0);
				if(cBean != null)
				{
					Bundle data = new Bundle();
					data.putInt(APIConstant.CAMPAIGN_ID_KEY, cBean.getCamp_id());
					data.putInt(APIConstant.TOTAL_IMP_KEY, cBean.getTotal_imp());
					
					Date d = new Date(cBean.getStart_date());
					
					data.putLong(APIConstant.START_DATE_KEY, cBean.getStart_date());
					data.putInt(APIConstant.CALL_KEY, cBean.getCall());
					data.putInt(APIConstant.CAMPAIGN_STATUS_KEY, cBean.getCamp_status());
					data.putString(APIConstant.CAMPAIGN_MSG_KEY, cBean.getCamp_msg());
					data.putInt(APIConstant.WEB_KEY, cBean.getWeb());
					data.putInt(APIConstant.MAP_KEY, cBean.getMap());
					data.putInt(APIConstant.SHOW_IMP_KEY, cBean.getShown_imp());
					data.putString(APIConstant.ACTION_KEY, cBean.getAction());
					
					RegistrationResponseBean bean = engObj.regUtilObj.getUserProfile(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());
					if(bean != null)
					{
						data.putString(APIConstant.WEB_URL_KEY, bean.getWebsite());
					}
					
					statMachineObj.nextState(ReportActivity.this, UIEventType.EDIT_CAMPAIGN_CLICK, false, UIType.REPORTACTIVITY,data);
				}
				else
					Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
			}
			break;
			
			case R.id.ll_refresh_icon:
			case R.id.refresh_btn:
				fetchReportData();
				break;
			default:
				break;
			}

		}
	}; 


	@Override
	public void handleEvent(int evType,Object msgObj)
	{
		if(msgObj == null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mELogger.error("handleEvent()  null respose for event type "+evType);
		}

		switch (evType) {
		case EventType.UPDATE_CAMPAIGN_STATUS:
		{
			dismissProgressDialogue();
			displayToast("Status Updated successfully", Gravity.CENTER, false, Toast.LENGTH_SHORT);
			getCampaignDataList();	
			setAdapterInList();
		}
		break;
		case EventType.FETCH_CAMPAIGN_LIST:
		{
			dismissProgressDialogue();
			getCampaignDataList();
			setAdapterInList();
		}
		break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		System.exit(0);
	}
}
