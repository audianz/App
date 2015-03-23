package com.audianz.audianzadvertiser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.audianz.beans.AddCampaignResponse;
import com.audianz.beans.AddOrderRequest;
import com.audianz.beans.ChildItem;
import com.audianz.beans.EditCampInfoRequest;
import com.audianz.beans.EditCampInfoResponse;
import com.audianz.beans.FetchCampaignResponse;
import com.audianz.beans.PlanResponseBean;
import com.audianz.beans.PromoteRequestBean;
import com.audianz.beans.PromotionPlanResponseBean;
import com.audianz.beans.RegistrationResponseBean;
import com.audianz.constants.APIConstant;
import com.audianz.constants.EventType;
import com.audianz.constants.UIEventType;
import com.audianz.constants.UIType;
import com.audianz.core.Engine;
import com.audianz.emcl.ELogger;
import com.audianz.utilities.Common;
import com.audianz.utilities.DateUtil;
import com.audianz.utilities.UIStateMachine;
import com.paytm.pgsdk.PaytmClientCertificate;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

public class PromoteNowActivity extends BaseActivity {

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	private String promote_text;
	private String weburl=null;

	private boolean startDateClick = true;
	private boolean startTimeClick = true;

	private boolean iscurTimeSelected=false;
	private boolean iscurDateSelected=false;

	private ImageView start_date_iv=null;
	private ImageView start_time_iv=null;
	private LinearLayout start_date_ll = null;
	private LinearLayout start_time_ll = null;
	private Spinner plan_spinner=null;
	private String promotoMsg = null;
	private String url = null;
	private EditText promote_msg_et=null;
	private EditText web_fb_url=null;
	private TextView header_promote;
	private TextView web_fb_url_tv;

	private int acc_bal =0;
	private int viewers =0;
	private int free_plan=0;

	private Button confirmBtn=null;
	private Button helpBtn = null;
	private Button back_btn =null;

	private LinearLayout ll_web_fb_url;
	private LinearLayout ll_web_fb_url_val;
	private TextView promote_desc_tv =null;
	private TextView start_date_tv=null;
	private TextView start_time_tv=null;
	private TextView user_action_tv=null;
	private Spinner user_action_spin=null;
	private String user_action_value=null;
	private String click_to_action = APIConstant.ACTION_CLICK_TO_CALL;;

	private final int DATEDIALOG_ID_START_DATE = 0;
	private final int DATEDIALOG_ID_START_TIME=1;
	private ELogger mELogger    =null;
	private UIStateMachine uistate  =null;
	private Engine engObj =null;
	private final String TAG  ="PROMOTENOWACTIVITY";

	private ArrayList<PlanResponseBean> planList=null;
	private PromoteRequestBean pReqBean = null;
	private RegistrationResponseBean userData=null;

	private Dialog dialog=null;
	EditText  card_no          = null;
	EditText  exp_date         = null;
	EditText  cvv              =null;

	private boolean isUpdateCamp = false;
	private ChildItem childItemObject = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promote);

		if(engObj==null)
			engObj= Engine.getInstance();
		uistate=UIStateMachine.getInstance();

		if(mELogger == null)
			mELogger = new ELogger();
		mELogger.setTag(TAG);

		if(Engine.IS_DEVELOPMENT_RELEASE)
			mELogger.debug("Entered onCreate");

		Intent i = getIntent();
		if(i != null)
		{
			Bundle data = i.getExtras();
			if(data != null)
			{
				isUpdateCamp = true;
				childItemObject = new ChildItem();

				childItemObject.setCamp_id(data.getInt(APIConstant.CAMPAIGN_ID_KEY));
				childItemObject.setCamp_msg(data.getString(APIConstant.CAMPAIGN_MSG_KEY));
				childItemObject.setCall(data.getInt(APIConstant.CALL_KEY));
				childItemObject.setCamp_status(data.getInt(APIConstant.CAMPAIGN_STATUS_KEY));
				childItemObject.setShown_imp(data.getInt(APIConstant.SHOW_IMP_KEY));
				childItemObject.setStart_date(data.getLong(APIConstant.START_DATE_KEY));
				childItemObject.setTotal_imp(data.getInt(APIConstant.TOTAL_IMP_KEY));
				childItemObject.setWeb(data.getInt(APIConstant.WEB_KEY));
				childItemObject.setMap(data.getInt(APIConstant.MAP_KEY));
				childItemObject.setAction(data.getString(APIConstant.ACTION_KEY));
				url = data.getString(APIConstant.WEB_URL_KEY);
			}
		}

		getWidgedID();
		setCurrentDateTime(true,true);
		setPlanList();
		createPlanSpinnerList();
		createUserActionSpinner();
		fetchPlanList();
		setListener();
		if(!TextUtils.isEmpty(url))
		{
			web_fb_url.setText(url);	
		}

		if(Engine.IS_DEVELOPMENT_RELEASE)
			mELogger.debug("Exited onCreate");
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		uistate.curActivity= this;
		Intent intent = getIntent();
		if(intent != null)
		{
			promotoMsg = intent.getStringExtra(APIConstant.CAMPAIGN_MSG_KEY);
			String lat = intent.getStringExtra(APIConstant.CAMPAIGN_LAT);
			String lon = intent.getStringExtra(APIConstant.CAMPAIGN_LON);
			if(lat!=null)
				engObj.cnfigReaderObj.setLATITUDE(Double.parseDouble(lat));
			if(lon!=null)
				engObj.cnfigReaderObj.setLONGITUDE(Double.parseDouble(lon));
			if(!TextUtils.isEmpty(promotoMsg))
				promote_msg_et.setText(promotoMsg);	
		}
		else
		{
			if(!TextUtils.isEmpty(promotoMsg))
				promote_msg_et.setText(promotoMsg);
		} 
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		promotoMsg = promote_msg_et.getText().toString();
	}

	protected void showCustomDialog()
	{
		dialog = new Dialog(PromoteNowActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.custompaymentdialog);


		final Button pay_inr_btn  = (Button)dialog.findViewById(R.id.payment_pay_inr_btn);
		final Button go_back_btn  = (Button)dialog.findViewById(R.id.payment_go_back_btn);

		TextView ad_preview        = (TextView)dialog.findViewById(R.id.payment_ad_preview_tv);
		TextView business_name     = (TextView)dialog.findViewById(R.id.payment_business_name_tv);
		TextView local_consume     = (TextView)dialog.findViewById(R.id.payment_total_consumer_tv);
		TextView camp_start        = (TextView)dialog.findViewById(R.id.payment_camp_start_tv);
		TextView camp_location     = (TextView)dialog.findViewById(R.id.payment_camp_loc);
		TextView ad_banner_prev_tv = (TextView)dialog.findViewById(R.id.ad_banner_preview_tv);
		TextView user_action       = (TextView)dialog.findViewById(R.id.payment_user_action);
		TextView banner_msg_tv     = (TextView)dialog.findViewById(R.id.banner_msg_tv); 
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		pay_inr_btn.setTypeface(custom_font);
		go_back_btn.setTypeface(custom_font);
		ad_preview.setTypeface(custom_font,1);
		business_name.setTypeface(custom_font);
		local_consume.setTypeface(custom_font);
		camp_start.setTypeface(custom_font);
		camp_location.setTypeface(custom_font);
		ad_banner_prev_tv.setTypeface(custom_font);
		user_action.setTypeface(custom_font);

		engObj.cnfigReaderObj.getLATITUDE();
		engObj.cnfigReaderObj.getLONGITUDE();
		if(engObj.cnfigReaderObj.getAUDIANZ_BUSINESS_NAME()!=null)
		{
			business_name.setText(engObj.cnfigReaderObj.getAUDIANZ_BUSINESS_NAME());
		}		

		if(camp_location!=null)
		{
			int length = 8;
			String lat = engObj.cnfigReaderObj.getLATITUDE();
			String lon = engObj.cnfigReaderObj.getLONGITUDE();
			if(lat!=null && lat.length()>length)
				lat  = lat.substring(0,length);
			if(lon!=null && lon.length()>length)
				lon = lon.substring(0,length);
			camp_location.setText(camp_location.getText().toString()+"  "+lat+"/ "+lon);
		}
		if(promote_text!=null && promote_text.length()>0)
		{
			banner_msg_tv.setText(promote_text);
		}
		if(viewers!=0)
		{
			String msg =String.valueOf(viewers)+" Local Consumers";
			local_consume.setText(msg);
		}

		if(user_action_value==APIConstant.CALL_ACTION)
		{
			user_action.setText(user_action.getText().toString()+user_action_value);
		}
		if(user_action_value==APIConstant.WEB_ACTION)
		{	
			user_action.setText(user_action.getText().toString()+user_action_value);
		}
		if(user_action_value==APIConstant.MAP_ACTION)
		{
			user_action.setText(user_action.getText().toString()+user_action_value);
		}
		if(acc_bal!=0 && free_plan==0)
		{
			String msg ="Pay INR "+String.valueOf(acc_bal);
			pay_inr_btn.setText(msg);
		}
		else
		{
			String msg ="Pay INR 0";
			pay_inr_btn.setText(msg);
		}

		long campStartdate = getStartDateTime();

		DateUtil dtutil = new DateUtil();
		String camp_date  = dtutil.myDateFormatter(campStartdate, "E, MMM d yyyy");

		if(camp_date!=null)
		{
			camp_start.setText("Starting "+camp_date);
		}

		pay_inr_btn.setOnClickListener(mCListener);
		go_back_btn.setOnClickListener(mCListener);

		dialog.show();
	}


	@Override
	public void handleEvent(int evType,Object msgObj)
	{

		if(Engine.IS_DEVELOPMENT_RELEASE)
			mELogger.debug("handleEvent() : server response is back for event type = "+evType);

		//		if(msgObj == null)
		//		{
		//			mELogger.error("HandleEvent response not found.");
		//			dismissProgressDialogue();
		//			return;
		//		}

		switch (evType) {
		case EventType.PROMOTENOW:
		{
			dismissProgressDialogue();

			if(msgObj == null)
			{
				mELogger.error("HandleEvent response not found.");
				return;
			}

			AddCampaignResponse resBean = (AddCampaignResponse)msgObj;
			if(resBean.getStatus().equals("SUCCESS"))
			{
				if(free_plan==0)
				{
					if(resBean.getOrder_id()!=null)
					{			
						makePaytmPayment(resBean);	
					}
					else
					{
						mELogger.error("Failed to generate orderID");
						displayToast("Promotion Failed! Please Try again", Gravity.CENTER, false, Toast.LENGTH_SHORT);
						return;
					}	
				}
				else
				{
					if(userData==null)
					{
						userData = engObj.regUtilObj.getUserProfile(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());	
					}

					AddOrderRequest order = new AddOrderRequest();
					order.setCurrency("INR");
					order.setCust_country("India");
					order.setCust_email(userData.getEmailid());
					order.setCust_id(userData.getClientid());
					order.setCust_mobile(userData.getMobile());
					if(userData.getName()==null)
						order.setCust_name(userData.getBusiness_name());
					else
						order.setCust_name(userData.getName());
					order.setCust_pincode(userData.getZip());
					order.setInvoice_id(resBean.getOrder_id());
					order.setOrder_amount(Float.valueOf(resBean.getAmount()));
					order.setOrder_date(DateUtil.getCurrentTimeStamp().toString());
					order.setOrder_id(resBean.getOrder_id());
					order.setOrder_status(1);
					order.setProduct_sku(resBean.getOrder_id());
					order.setSub_order_id(resBean.getOrder_id());
					order.setIsFreePlan(1);

					if(engObj!=null && order!=null)
					{
						int serverRespCode = engObj.addOrderDetail(order);
						if(serverRespCode == -1)
						{
							if(Engine.IS_DEVELOPMENT_RELEASE)
								mELogger.error("onTransactionSuccess(): null engine response for register user.");
						}
						else
						{
							checkResponse(serverRespCode);
						}
					}
					else
					{
						mELogger.error("onTransactionSuccess() failed to send request.");
					}
				}
			}
			else if(resBean.getStatus().equals("ERROR"))
			{
				displayToast("Promotion Failed! Please Try again", Gravity.CENTER, false, Toast.LENGTH_SHORT);
				return;
			}
		}
		break;
		case EventType.EDIT_CAMPAIGN:
		{
			dismissProgressDialogue();
			if(msgObj == null)
			{
				mELogger.error("HandleEvent response not found.");
				displayToast("Updation Failed! Please Try again", Gravity.CENTER, false, Toast.LENGTH_SHORT); 
				finish();
				return;
			}
			EditCampInfoResponse respBean = (EditCampInfoResponse)msgObj;
			if(respBean.getStatus().equals("SUCCESS"))
			{
				displayToast("Successfully updated", Gravity.CENTER, false, Toast.LENGTH_SHORT);
			}
			else
			{
				displayToast("Updation Failed! Please Try again", Gravity.CENTER, false, Toast.LENGTH_SHORT);   
			}

			int campSererResp   = engObj.addFetchCampaigndata();
			finish();
		}
		break;
		case EventType.ADD_ORDER_DETAIL:
		{
			dismissProgressDialogue();
			if(msgObj == null)
			{
				mELogger.error("HandleEvent response not found.");
				return;
			}

			FetchCampaignResponse resbBean = (FetchCampaignResponse)msgObj;
			if(resbBean.getStatus().equals("SUCCESS"))
			{
				statMachineObj.nextState(PromoteNowActivity.this, UIEventType.ORDER_SUCCESS, true, UIType.PROMOTENOW_ACTIVITY,null);
			}
			else
			{
				displayToast("Promotion Failed! Please Try again", Gravity.CENTER, false, Toast.LENGTH_SHORT);
				return;    
			}
		}

		break;
		case EventType.FETCHPROMOTIONPLAN:
		{
			dismissProgressDialogue();
			if(msgObj == null)
			{
				mELogger.error("HandleEvent response not found.");
				return;
			}
			PromotionPlanResponseBean planBean = (PromotionPlanResponseBean)msgObj;
			if(planBean!=null)
			{
				if(planList!=null)
					planList.clear();
				planList = planBean.getPlans();
				createPlanSpinnerList();
			}
		}
		break;
		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		if(isUpdateCamp)
			finish();
		else
			statMachineObj.nextState(PromoteNowActivity.this, UIEventType.TABHOST, true,UIType.PROMOTENOW_ACTIVITY,null);
	}


	private void getWidgedID(){
		back_btn           = (Button)findViewById(R.id.back_btn);
		start_date_iv      = (ImageView)findViewById(R.id.start_date_img);
		start_time_iv      = (ImageView)findViewById(R.id.start_time_img);
		start_date_ll      = (LinearLayout)findViewById(R.id.start_date_ll);
		start_time_ll      = (LinearLayout)findViewById(R.id.start_time_ll);
		plan_spinner       = (Spinner)findViewById(R.id.plan_spinner);
		start_date_tv      = (TextView)findViewById(R.id.start_date_tv);
		start_time_tv      = (TextView)findViewById(R.id.start_time_tv);
		web_fb_url         = (EditText)findViewById(R.id.web_fb_url_etv);
		promote_msg_et        = (EditText)findViewById(R.id.promote_msg_etv);
		promote_desc_tv    = (TextView)findViewById(R.id.promote_desc_msg_txt);
		confirmBtn         = (Button)findViewById(R.id.confirm_btn);
		helpBtn            = (Button)findViewById(R.id.help_btn);
		user_action_tv     = (TextView)findViewById(R.id.user_action_tv);
		user_action_spin   = (Spinner)findViewById(R.id.user_action_spinner);
		ll_web_fb_url      = (LinearLayout)findViewById(R.id.ll_web_fb_url);
		ll_web_fb_url_val  = (LinearLayout)findViewById(R.id.ll_web_fb_val);
		header_promote     = (TextView)findViewById(R.id.header_promote);
		web_fb_url_tv      = (TextView)findViewById(R.id.web_fb_url_tv);

		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_neue_regular.ttf");
		back_btn.setTypeface(custom_font);
		start_date_tv.setTypeface(custom_font);
		start_time_tv.setTypeface(custom_font);
		web_fb_url.setTypeface(custom_font);
		promote_msg_et.setTypeface(custom_font);
		helpBtn.setTypeface(custom_font);
		confirmBtn.setTypeface(custom_font);
		user_action_tv.setTypeface(custom_font);
		header_promote.setTypeface(custom_font);

		if(isUpdateCamp){
			header_promote.setText("Update Info");
			confirmBtn.setText("Update");
			helpBtn.setVisibility(View.INVISIBLE);
		}else{
			header_promote.setText("Promote");
			confirmBtn.setText("Confirm");
			helpBtn.setVisibility(View.VISIBLE);
		}

		Typeface  proxima = Typeface.createFromAsset(getAssets(), "fonts/MarkSimonsonProximaNovaRegular.otf");
		promote_desc_tv.setTypeface(proxima);
	}

	private void setListener(){
		helpBtn.setOnClickListener(mCListener);
		back_btn.setOnClickListener(mCListener);
		confirmBtn.setOnClickListener(mCListener);
		if(childItemObject != null && childItemObject.getCamp_status() == APIConstant.STATUS_COMPLIANCE)
			return;
		start_date_iv.setOnClickListener(mCListener);
		start_time_iv.setOnClickListener(mCListener);
		start_date_ll.setOnClickListener(mCListener);
		start_time_ll.setOnClickListener(mCListener);
	}

	/**
	 * This method is used to create user action spinner list item
	 */
	private void createUserActionSpinner()
	{
		String[]  dataList  = {APIConstant.CALL_ACTION,APIConstant.WEB_ACTION,APIConstant.MAP_ACTION};
		//String[]  dataList  = {APIConstant.WEB_ACTION,APIConstant.CALL_ACTION,APIConstant.MAP_ACTION};

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_action_spin_item, dataList);
		adapter.setDropDownViewResource(R.layout.user_action_spin_item);
		user_action_spin.setAdapter(adapter);

		user_action_spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) 
			{	
				user_action_value = user_action_spin.getSelectedItem().toString();

				if(user_action_value==APIConstant.CALL_ACTION)
				{
					ll_web_fb_url.setVisibility(View.GONE);
					ll_web_fb_url_val.setVisibility(View.GONE);
					click_to_action = APIConstant.ACTION_CLICK_TO_CALL;
				}
				if(user_action_value==APIConstant.WEB_ACTION)
				{	
					ll_web_fb_url.setVisibility(View.VISIBLE);
					ll_web_fb_url_val.setVisibility(View.VISIBLE);
					click_to_action = APIConstant.ACTION_CLICK_TO_WEB;
				}
				if(user_action_value==APIConstant.MAP_ACTION)
				{
					ll_web_fb_url.setVisibility(View.GONE);
					ll_web_fb_url_val.setVisibility(View.GONE);
					click_to_action = APIConstant.ACTION_CLICK_TO_MAP;

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		if(isUpdateCamp && childItemObject != null && childItemObject.getAction() != null)
		{
			if(childItemObject.getAction().equals(APIConstant.ACTION_CLICK_TO_CALL))
				user_action_spin.setSelection(0);
			else if(childItemObject.getAction().equals(APIConstant.ACTION_CLICK_TO_WEB)){
				ll_web_fb_url.setVisibility(View.VISIBLE);
				ll_web_fb_url_val.setVisibility(View.VISIBLE);


				user_action_spin.setSelection(1);
			}
			else if(childItemObject.getAction().equals(APIConstant.ACTION_CLICK_TO_MAP))
				user_action_spin.setSelection(2);
		}

	}
	/**
	 * This method is used to create Spinner Item list
	 */
	private void createPlanSpinnerList() 
	{
		if(planList==null || planList.size() <= 0)
			return;

		String[]  dataList  = new String[planList.size()];
		int i =0;
		for(PlanResponseBean pBean: planList)
		{
			dataList[i] = pBean.getMessage();
			i++;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.merchant_plan_spin_item, dataList);
		adapter.setDropDownViewResource(R.layout.merchant_plan_spin_item);
		plan_spinner.setAdapter(adapter);

		plan_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) 
			{	
				plan_spinner.getSelectedItem().toString();
				PlanResponseBean pb = planList.get(arg2);
				if(pb!=null)
				{
					acc_bal   = pb.getInr_price();
					viewers   = pb.getViewer();
					free_plan = pb.getFree_plan();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		if(isUpdateCamp && childItemObject != null)
		{
			if(childItemObject.getTotal_imp() == 2500)
				plan_spinner.setSelection(0);
			else if(childItemObject.getTotal_imp() == 5000)
				plan_spinner.setSelection(1);
			else if(childItemObject.getTotal_imp() == 12500)
				plan_spinner.setSelection(2);
			else if(childItemObject.getTotal_imp() == 25000)
				plan_spinner.setSelection(3);
			else if(childItemObject.getTotal_imp() == 50000)
				plan_spinner.setSelection(4);

			plan_spinner.setClickable(false);
		}
	}

	View.OnClickListener mCListener = new View.OnClickListener() {

		@SuppressWarnings("deprecation")
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.back_btn:
				statMachineObj.nextState(PromoteNowActivity.this, UIEventType.TABHOST, true,UIType.PROMOTENOW_ACTIVITY,null);
				break;

			case R.id.start_date_img:
			case R.id.start_date_ll:
			{
				iscurDateSelected=false;
				showDialog(DATEDIALOG_ID_START_DATE);
			}
			break;
			case R.id.help_btn:
				uistate.nextState(PromoteNowActivity.this, UIEventType.HELP_CLICK, false, UIType.PROMOTENOW_ACTIVITY,null);
				break;
			case R.id.start_time_img:
			case R.id.start_time_ll:
			{
				iscurTimeSelected=false;
				showDialog(DATEDIALOG_ID_START_TIME);

			}
			break;
			case R.id.confirm_btn:
				if(isUpdateCamp)
				{
					// Diplay progress bar and send update request to server. Then finish the activity.
					handleEditCampaignDetails();
					//finish();
				}
				else
				{	
					handlePromotionClick();
				}
				break;
			case R.id.payment_go_back_btn:
			{
				if(dialog!=null)
					dialog.dismiss();
			}
			break;
			case R.id.payment_pay_inr_btn:
			{
				if(dialog !=null)
					dialog.dismiss();
				confirmPromotion();
			}
			break;
			default:
				break;
			}
		}
	};

	/**
	 * This method is used to create a update campaign request to the server.
	 */
	private void handleEditCampaignDetails() 
	{
		if(!Common.isNetworkAvailable(getApplicationContext()))
		{
			displayToast(getString(R.string.net_not_available), Gravity.TOP, false,  Toast.LENGTH_SHORT);
			return;
		}
		promote_text = promote_msg_et.getText().toString();
		weburl    = web_fb_url.getText().toString();
		if(promote_text==null || promote_text.length()<1)
		{
			displayToast("Plese enter your promotion message", Gravity.CENTER, false, Toast.LENGTH_SHORT);
			return;
		}
		if(click_to_action.equals(APIConstant.ACTION_CLICK_TO_WEB))
		{
			if(weburl==null || weburl.length()<8)
			{
				displayToast("Plese enter your website or facebook page address", Gravity.CENTER, false, Toast.LENGTH_SHORT);
				return;
			}
		}
		else
		{
			weburl = null;
		}

		EditCampInfoRequest eReqBean = new EditCampInfoRequest();
		eReqBean.setCampaign_id(childItemObject.getCamp_id());
		eReqBean.setClient_id(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());
		eReqBean.setClick_to_action(click_to_action);
		eReqBean.setPromote_msg(promote_text);

		if(engObj!=null && eReqBean!=null)
		{
			int serverRespCode = engObj.editCampaignReq(eReqBean);
			if(serverRespCode == -1)
			{
				if(Engine.IS_DEVELOPMENT_RELEASE)
					mELogger.error("handleEditCampaignDetails(): null engine response for register user.");
			}
			else
			{
				checkResponse(serverRespCode);
			}
		}
		else
		{
			mELogger.error("handleEditCampaignDetails() failed to send request.");
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		final int year = c.get(Calendar.YEAR);
		final int monthOfYear = c.get(Calendar.MONTH);
		final int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

		final int hourOfday = c.get(Calendar.HOUR_OF_DAY);
		final int minute    = c.get(Calendar.MINUTE); 
		switch(id){
		case DATEDIALOG_ID_START_DATE  :{

			iscurDateSelected=false;
			DatePickerDialog picker = new DatePickerDialog(
					this,
					mDateSetListenerForFrom, // instead of a listener
					year, monthOfYear, dayOfMonth);
			picker.setCancelable(true);
			picker.setCanceledOnTouchOutside(true);

			picker.setButton(DialogInterface.BUTTON_NEUTRAL, "Current",
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					setCurrentDateTime(true,false);
					iscurDateSelected = true;
					startDateClick= false;
				}
			});
			picker.show();
			return picker;
		}  
		case DATEDIALOG_ID_START_TIME:
		{
			iscurTimeSelected=false;
			TimePickerDialog  timePicker = new TimePickerDialog(this, mTimeSetListener, hourOfday, minute, false);
			timePicker.setCancelable(true);
			timePicker.setCanceledOnTouchOutside(true);

			timePicker.setButton(DialogInterface.BUTTON_NEGATIVE,"Current", 
					new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					setCurrentDateTime(false, true);
					iscurTimeSelected=true;
					startTimeClick=false;
				}
			});
			timePicker.show();
			return timePicker;
		}
		default :
			break;
		}
		return null;
	}

	/**
	 * Time Picker 
	 */

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			if(iscurTimeSelected)
				return;

			Calendar c= Calendar.getInstance();

			long curDate = c.getTimeInMillis();
			String timeFormat ="AM";

			int hour    = c.get(Calendar.HOUR_OF_DAY);
			int curMinute     = c.get(Calendar.MINUTE);

			mHour=hourOfDay;
			mMinute=minute;

			Calendar cal = Calendar.getInstance();
			cal.set(mYear, mMonth, mDay);
			long startDate = cal.getTimeInMillis();

			DateUtil dtUtilObj = new DateUtil();

			String startDateStr = dtUtilObj.myDateFormatter(startDate, "dMMyyyy");
			String curDateStr   = dtUtilObj.myDateFormatter(curDate, "dMMyyyy");

			if(startDateStr.equals(curDateStr))
			{
				if(hourOfDay<hour)
				{
					displayToast("Time can not be less than current time", Gravity.CENTER, false, Toast.LENGTH_SHORT);
				}
				else if(minute<curMinute && hourOfDay<=hour)
				{
					displayToast("Time can not be less than current time", Gravity.CENTER, false, Toast.LENGTH_SHORT);
				}
				else
				{

					if(hourOfDay>=12)
						timeFormat="PM";
					if(hourOfDay>12)
					{
						hourOfDay=hourOfDay%12;
					}
					String curTime = String.format("%02d:%02d", hourOfDay,minute);
					String strTime = curTime+" "+timeFormat;

					if(startTimeClick)
						start_time_tv.setText(strTime);
					startTimeClick=true;
				}

			}
			else
			{
				// This condition will come when selected date > current date. Selected date can not be less than current Date 
				// as it is alredy checked in onDateSet method

				if(hourOfDay>=12)
					timeFormat="PM";
				if(hourOfDay>12)
				{
					hourOfDay=hourOfDay%12;
				}
				String curTime = String.format("%02d:%02d", hourOfDay,minute);
				String strTime = curTime+" "+timeFormat;

				if(startTimeClick)
					start_time_tv.setText(strTime);
				startTimeClick=true;
			}

		}
	};

	/**
	 *  Date picker for form 
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListenerForFrom = new DatePickerDialog.OnDateSetListener() {

		// onDateSet method
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {

			if(iscurDateSelected)
				return;
			Calendar cal = Calendar.getInstance();
			Date dt = new Date();
			long curDate=dt.getTime();
			cal.set(year,monthOfYear,dayOfMonth);
			long startDt = cal.getTimeInMillis();
			mYear=year;
			mMonth=monthOfYear;
			mDay=dayOfMonth;
			if(startDt<curDate)
			{
				displayToast("Start Date can not be less than current date",Gravity.CENTER,false,Toast.LENGTH_SHORT);
			}
			else
			{
				monthOfYear++;
				String start_dt=String.valueOf(dayOfMonth)+"/"+String.valueOf(monthOfYear)+"/"+String.valueOf(year);
				if(startDateClick)
					start_date_tv.setText(start_dt);
				startDateClick = true;
			}

		}// ENd of onDateSet()

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.promote, menu);
		return true;
	}

	private PromoteRequestBean getPromotionBean(String msg,String web_url)
	{
		Calendar cal = Calendar.getInstance();
		DateUtil dUtil = new DateUtil();

		String time = dUtil.myDateFormatter(cal.getTimeInMillis(), "dMMMyy hh:mm:ss");
		String camp_name = engObj.cnfigReaderObj.getAUDIANZ_BUSINESS_NAME()+" "+time;

		PromoteRequestBean reqBean = new PromoteRequestBean();

		reqBean.setClientid(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());
		reqBean.setCamp_name(camp_name);
		reqBean.setAccbal(acc_bal);
		reqBean.setStartTime(getStartDateTime());
		reqBean.setWeb_url(web_url);
		reqBean.setPromote_msg(msg);

		reqBean.setClick_to_action(click_to_action);
		reqBean.setLat(engObj.cnfigReaderObj.getLATITUDE());
		reqBean.setLon(engObj.cnfigReaderObj.getLONGITUDE());

		return reqBean;

	}
	public void handlePromotionClick() {
		if(!Common.isNetworkAvailable(getApplicationContext()))
		{
			displayToast(getString(R.string.net_not_available), Gravity.TOP, false,  Toast.LENGTH_SHORT);
			return;
		}
		promote_text = promote_msg_et.getText().toString();
		weburl    = web_fb_url.getText().toString();
		if(promote_text==null || promote_text.length()<1)
		{
			displayToast("Plese enter your promotion message", Gravity.CENTER, false, Toast.LENGTH_SHORT);
			return;
		}
		if(click_to_action.equals(APIConstant.ACTION_CLICK_TO_WEB))
		{
			if(weburl==null || weburl.length()<8)
			{
				displayToast("Plese enter your website or facebook page address", Gravity.CENTER, false, Toast.LENGTH_SHORT);
				return;
			}
		}
		else
		{
			weburl = null;
		}
		pReqBean = getPromotionBean(promote_text,weburl);
		showCustomDialog();	
	}

	/**
	 * This method is used to make Payment using PayTM PG
	 */
	private void makePaytmPayment(AddCampaignResponse campBean)
	{
		PaytmPGService pgservices = null;

		try{

			if(userData==null)
				userData = engObj.regUtilObj.getUserProfile(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());

			pgservices = PaytmPGService.getStagingService();
			PaytmOrder po  = new PaytmOrder(campBean.getOrder_id(), String.valueOf(userData.getClientid()), 
					String.valueOf(acc_bal),userData.getEmailid(), userData.getMobile());
			PaytmMerchant merchant  = new PaytmMerchant
					(APIConstant.MERCHANT_ID, APIConstant.CHANNEL_ID, APIConstant.INDUSTRY_TYPE_ID,APIConstant.WEBSITE, 
							APIConstant.MERCHANTTHEME, APIConstant.checkSumGenerationURL,
							APIConstant.checkSumVerificationURL);

			PaytmClientCertificate certificate = null;

			pgservices.initialize(po, merchant, certificate);

		}catch(Exception e)
		{
			mELogger.error("makePaytmPayment() exception occured "+e.getMessage());
		}

		pgservices.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {

			@Override
			public void someUIErrorOccurred(String ex) {
				// TODO Auto-generated method stub
				mELogger.error("makePaytmPayment() UI error "+ex);
			}

			@Override
			public void onTransactionSuccess(Bundle response) {
				// TODO Auto-generated method stub

				response.getString("GATEWAYNAME");
				String txndate          = response.getString("TXNDATE");
				response.getString("PAYMENTMODE");
				String status           = response.getString("STATUS");
				response.getString("MID");
				response.getString("TXTTYPE");
				String orderID          = response.getString("ORDERID");
				String currency         = response.getString("CURRENCY");
				response.getString("TXTID");
				String txnamount        = response.getString("TXNAMOUNT");
				response.getString("IS_CHECKSUM_VALID");
				response.getString("BANKTXNID");           
				response.getString("BANKNAME");
				response.getString("RESPMSG");
				response.getString("RESPCODE");

				if(userData==null)
				{
					userData = engObj.regUtilObj.getUserProfile(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());	
				}

				AddOrderRequest order = new AddOrderRequest();
				order.setCurrency(currency);
				order.setCust_country("India");
				order.setCust_email(userData.getEmailid());
				order.setCust_id(userData.getClientid());
				order.setCust_mobile(userData.getMobile());
				if(userData.getName()==null)
					order.setCust_name(userData.getBusiness_name());
				else
					order.setCust_name(userData.getName());
				order.setCust_pincode(userData.getZip());
				order.setInvoice_id(orderID);
				order.setOrder_amount(Float.valueOf(txnamount));
				order.setOrder_date(txndate);
				order.setOrder_id(orderID);
				if(status.equals("TXN_SUCCESS"))
					order.setOrder_status(1);
				else
					order.setOrder_status(0);
				order.setProduct_sku(orderID);
				order.setSub_order_id(orderID);
				order.setIsFreePlan(0);



				if(engObj!=null && order!=null)
				{
					int serverRespCode = engObj.addOrderDetail(order);
					if(serverRespCode == -1)
					{
						if(Engine.IS_DEVELOPMENT_RELEASE)
							mELogger.error("onTransactionSuccess(): null engine response for register user.");
					}
					else
					{
						checkResponse(serverRespCode);
					}
				}
				else
				{
					mELogger.error("onTransactionSuccess() failed to send request.");
				}

			}

			@Override
			public void onTransactionFailure(String errorMsg, Bundle arg1) {
				// TODO Auto-generated method stub
				displayToast("Transaction Failure "+errorMsg, Gravity.CENTER,false, Toast.LENGTH_SHORT);
				mELogger.error("makePaytmPayment()  Transaction Failure "+errorMsg);
			}

			@Override
			public void onErrorLoadingWebPage(int errorCode, String errorMsg, String failURL) {
				// TODO Auto-generated method stub
				mELogger.error("makePaytmPayment()  ErrorLoadingWebPage");

			}

			@Override
			public void networkNotAvailable() {
				// TODO Auto-generated method stub
				displayToast(getResources().getString(R.string.net_not_available), Gravity.CENTER, false, Toast.LENGTH_SHORT);
				return;

			}

			@Override
			public void clientAuthenticationFailed(String errorMsg) {
				// TODO Auto-generated method stub

				mELogger.error("makePaytmPayment() clientAuthenticationFailed "+errorMsg);
			}
		});
	}

	private void confirmPromotion()
	{
		if(engObj!=null && pReqBean!=null)
		{
			int serverRespCode = engObj.promoteCampaign(pReqBean);
			if(serverRespCode == -1)
			{
				if(Engine.IS_DEVELOPMENT_RELEASE)
					mELogger.error("confirmPromotion(): null engine response for register user.");
			}
			else
			{
				checkResponse(serverRespCode);
			}
		}
		else
		{
			mELogger.error("confirmPromotion() failed to send request.");
		}
	}
	private void setCurrentDateTime(boolean date, boolean time)
	{
		Calendar c  = null;
		if(isUpdateCamp && childItemObject != null)
		{
			c = Calendar.getInstance();
			Date dt = new Date(childItemObject.getStart_date()*1000);
			c.setTime(dt);
		}
		else
			c  = Calendar.getInstance();

		if(date)
		{
			mDay        = c.get(Calendar.DAY_OF_MONTH);
			mMonth      = c.get(Calendar.MONTH);
			mYear       = c.get(Calendar.YEAR);	
		}
		if(time)
		{
			mHour       = c.get(Calendar.HOUR_OF_DAY);
			mMinute     = c.get(Calendar.MINUTE);
		}
		String format = "AM";
		if(mHour>=12)
			format="PM";

		int hour=mHour;
		if(mHour>12)
		{
			hour=mHour%12;
		}

		c.set(mYear, mMonth, mDay);
		c.getTimeInMillis();

		if(date)
		{
			int month = mMonth+1; // 0 index based month
			start_date_tv.setText(String.valueOf(mDay)+"/"+String.valueOf(month)+"/"+String.valueOf(mYear));
			mELogger.info("Current Date is : "+String.valueOf(mDay)+"/"+String.valueOf(month)+"/"+String.valueOf(mYear));
		}
		if(time)
		{
			String curTime = String.format("%02d:%02d", hour,mMinute);
			String strStartTime = curTime+"  "+format;
			mELogger.info("Current Time is : "+strStartTime);
			start_time_tv.setText(strStartTime);
		}

	}

	/**
	 * This method is used to get Start date time in millisecond
	 * @return
	 */
	private long getStartDateTime()
	{
		Calendar c = Calendar.getInstance();
		c.set(mYear, mMonth, mDay, mHour, mMinute);
		return c.getTimeInMillis();
	}

	private void setPlanList()
	{
		ArrayList<PlanResponseBean> pBeanList = engObj.audiDbUtil.getPlanList();
		if(pBeanList!=null)
		{
			if(planList == null)
				planList = new ArrayList<PlanResponseBean>();
			planList.clear();
			planList.addAll(pBeanList);
		}
		else
		{
			mELogger.debug("setPlanList() : Plan list is found Null");
		}
	}

	private void fetchPlanList()
	{

		int serverRespCode = engObj.addFetchPromotionPlan();
		if(serverRespCode == -1)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				mELogger.error("fetchPlanList : null engine response");
		}

	}

}
