package com.audianz.utilities;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.audianz.audianzadvertiser.R;
import com.audianz.beans.ChildItem;
import com.audianz.beans.GroupItem;
import com.audianz.constants.APIConstant;
import com.audianz.core.Engine;

public class ExpandableListAdapter extends BaseExpandableListAdapter {


	private Context context;
	private ArrayList<GroupItem> campListData= null;
	private OnClickListener mClickListener = null;


	public ExpandableListAdapter(Context context, ArrayList<GroupItem> campListData,OnClickListener mClickListener) {
		this.context = context;
		this.campListData=campListData;
		this.mClickListener = mClickListener;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ChildItem> chList = campListData.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	class vChildHolder{

		TextView startDate;
		TextView CampId;
		TextView lblCampId;
		TextView LblstartDate;
		TextView Imptv;
		TextView LblImptv;
		TextView shownImp;
		TextView LblshownImp;
		TextView callWeb;
		TextView LblcallWeb;
		TextView status;
		TextView Lblstatus;
		LinearLayout ll_camp_action;
		Button report_reRun;
		Button report_run;
		Button report_pause;
		Button edit_campaign;
		Button un_paid_btn;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		vChildHolder vHolder = null;
		ChildItem child =(ChildItem)getChild(groupPosition, childPosition);
		
		Engine.engObj.eLogger.info("getChildView() : Group Position : "+groupPosition +",  Child Position is :"+childPosition +", Status : "+child.getCamp_status());
		
//		if(convertView == null)
//		{
			vHolder = new vChildHolder();
			LayoutInflater infalInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.camp_list_item, null);

			vHolder.CampId     = (TextView)convertView.findViewById(R.id.lblCampIdItem);
			vHolder.startDate     = (TextView)convertView.findViewById(R.id.lblStartDateItem);
			vHolder.Imptv         = (TextView)convertView.findViewById(R.id.lblImpItem);
			vHolder.shownImp      = (TextView)convertView.findViewById(R.id.lblshownImpItem);
			vHolder.callWeb       = (TextView)convertView.findViewById(R.id.lblcallWebItem);
			vHolder.status        = (TextView)convertView.findViewById(R.id.lblStatusItem);
			vHolder.ll_camp_action= (LinearLayout)convertView.findViewById(R.id.ll_camp_action);
			vHolder.report_reRun  = (Button)convertView.findViewById(R.id.report_reRun_btn);
			vHolder.report_run    = (Button)convertView.findViewById(R.id.report_run_btn);
			vHolder.report_pause  = (Button)convertView.findViewById(R.id.report_pause_btn);
			vHolder.edit_campaign = (Button)convertView.findViewById(R.id.edit_campaign_btn);
			vHolder.un_paid_btn = (Button)convertView.findViewById(R.id.un_paid_btn);

            vHolder.lblCampId     = (TextView)convertView.findViewById(R.id.lblCampId);
 			vHolder.LblcallWeb    = (TextView)convertView.findViewById(R.id.lblcallWeb);
			vHolder.LblImptv      = (TextView)convertView.findViewById(R.id.lblImp);
			vHolder.LblshownImp   = (TextView)convertView.findViewById(R.id.lblshownImp);
			vHolder.LblstartDate  = (TextView)convertView.findViewById(R.id.lblStartDate);
			vHolder.Lblstatus     = (TextView)convertView.findViewById(R.id.lblStatus);

			Typeface custom_font = Typeface.createFromAsset(this.context.getAssets(),
					"fonts/helvetica_neue_regular.ttf");

			vHolder.CampId.setTypeface(custom_font);
			vHolder.startDate.setTypeface(custom_font);
			vHolder.Imptv.setTypeface(custom_font);
			vHolder.shownImp.setTypeface(custom_font);
			vHolder.callWeb.setTypeface(custom_font);
			vHolder.status.setTypeface(custom_font);
			vHolder.report_pause.setTypeface(custom_font);
			vHolder.report_reRun.setTypeface(custom_font);
			vHolder.report_run.setTypeface(custom_font);
			vHolder.edit_campaign.setTypeface(custom_font);
			vHolder.lblCampId.setTypeface(custom_font);
			vHolder.LblcallWeb.setTypeface(custom_font);
			vHolder.LblImptv.setTypeface(custom_font);
			vHolder.LblshownImp.setTypeface(custom_font);
			vHolder.LblstartDate.setTypeface(custom_font);
			vHolder.Lblstatus.setTypeface(custom_font);

			vHolder.report_reRun.setOnClickListener(mClickListener);
			vHolder.report_run.setOnClickListener(mClickListener);
			vHolder.report_pause.setOnClickListener(mClickListener);
			vHolder.edit_campaign.setOnClickListener(mClickListener);
			vHolder.un_paid_btn.setOnClickListener(mClickListener);
			
			convertView.setTag(vHolder);
//		}
//		else
//		{
//			vHolder = (vChildHolder)convertView.getTag();
//		}

		DateUtil  dtUtil = new DateUtil();
		int remAds = 0;
		if(child.getShown_imp() < child.getTotal_imp())
		{
			 remAds = child.getTotal_imp()-child.getShown_imp();	
		}
		
		vHolder.CampId.setText("AudExp- #"+child.getCamp_id());
		vHolder.startDate.setText(dtUtil.myDateFormatter(child.getStart_date()*1000,"MMM d, yyyy hh:mm a"));
		vHolder.Imptv.setText(String.valueOf(child.getTotal_imp()));
		vHolder.shownImp.setText(String.valueOf(remAds));
		vHolder.callWeb.setText(String.valueOf(child.getCall())+"/"+String.valueOf(child.getWeb()));

		switch(child.getCamp_status())
		{
		case APIConstant.STATUS_RUNNING:
		{
			vHolder.status.setText("Running");
			vHolder.ll_camp_action.setVisibility(View.GONE);

		}
		break;
		case APIConstant.STATUS_UNPAID:
		{
			vHolder.status.setText("Un-Paid");
			vHolder.ll_camp_action.setVisibility(View.VISIBLE);
			vHolder.report_pause.setVisibility(View.GONE);
			vHolder.report_reRun.setVisibility(View.GONE);
			vHolder.report_run.setVisibility(View.GONE);
			vHolder.edit_campaign.setVisibility(View.GONE);
			vHolder.un_paid_btn.setVisibility(View.VISIBLE);
		}
		break;
		case APIConstant.STATUS_PAUSED:
		{
			vHolder.status.setText("Paused");
			vHolder.ll_camp_action.setVisibility(View.GONE);
		}
		break;
		case APIConstant.STATUS_COMPLIANCE:
		{
			vHolder.status.setText("Compliance");
			vHolder.ll_camp_action.setVisibility(View.VISIBLE);
			vHolder.report_pause.setVisibility(View.GONE);
			vHolder.report_reRun.setVisibility(View.GONE);
			vHolder.report_run.setVisibility(View.GONE);
			vHolder.edit_campaign.setVisibility(View.VISIBLE);
			vHolder.un_paid_btn.setVisibility(View.GONE);
		}
		break;
		case APIConstant.STATUS_AWAITING:
		{
			vHolder.status.setText("Scheduled");
			vHolder.ll_camp_action.setVisibility(View.VISIBLE);
			vHolder.report_pause.setVisibility(View.GONE);
			vHolder.report_reRun.setVisibility(View.GONE);
			vHolder.report_run.setVisibility(View.VISIBLE);
			vHolder.edit_campaign.setVisibility(View.GONE);
			vHolder.un_paid_btn.setVisibility(View.GONE);
		}
		break;
		case APIConstant.STATUS_COMPLETE:
		{
			vHolder.status.setText("Completed");
			vHolder.ll_camp_action.setVisibility(View.VISIBLE);
			vHolder.report_pause.setVisibility(View.GONE);
			vHolder.report_reRun.setVisibility(View.VISIBLE);
			vHolder.report_run.setVisibility(View.GONE);
			vHolder.edit_campaign.setVisibility(View.GONE);
			vHolder.un_paid_btn.setVisibility(View.GONE);
		}

		break;
		default:
			vHolder.status.setText("Status Error");
			break;
		}

		vHolder.report_reRun.setTag(R.integer.campaign_position,groupPosition);
		vHolder.report_pause.setTag(R.integer.campaign_position,groupPosition);
		vHolder.report_run.setTag(R.integer.campaign_position,groupPosition);
		vHolder.edit_campaign.setTag(R.integer.campaign_position,groupPosition);
		vHolder.un_paid_btn.setTag(R.integer.campaign_position,groupPosition);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<ChildItem> chList = campListData.get(groupPosition).getItems();
		return chList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return campListData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.campListData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		GroupItem campBean = (GroupItem) getGroup(groupPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.camp_list_group, null);
			ExpandableListView elv = (ExpandableListView)parent;
			elv.expandGroup(0);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);

		Typeface custom_font = Typeface.createFromAsset(this.context.getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		lblListHeader.setTypeface(custom_font,1);
		lblListHeader.setText("Campaign Name: "+campBean.getCamp_name());

		return convertView;
	}


	@Override
	public void registerDataSetObserver(DataSetObserver observer)
	{
		if(observer!=null)
			super.registerDataSetObserver(observer);
	}

	@Override

	public void unregisterDataSetObserver(DataSetObserver observer)
	{
		if(observer!=null)
			super.unregisterDataSetObserver(observer);
		
	}
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	public void updateAdapter(ArrayList<GroupItem> campListData)
	{
		if(campListData!=null)
		{
			if(this.campListData==null)
				this.campListData= new ArrayList<GroupItem>();
			this.campListData.clear();
			this.campListData.addAll(campListData);
		}
		
	}
}
