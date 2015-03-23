package com.audianz.core;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.audianz.beans.AddCampaignResponse;
import com.audianz.beans.AddOrderRequest;
import com.audianz.beans.CampaignListBean;
import com.audianz.beans.CampaignStatBean;
import com.audianz.beans.EditCampInfoRequest;
import com.audianz.beans.EditCampInfoResponse;
import com.audianz.beans.FetchCampaignListRequest;
import com.audianz.beans.FetchCampaignResponse;
import com.audianz.beans.FetchStatResponse;
import com.audianz.beans.LoginRequestBean;
import com.audianz.beans.PasswordRequestBean;
import com.audianz.beans.PlanResponseBean;
import com.audianz.beans.PromotePlanRequestBean;
import com.audianz.beans.PromoteRequestBean;
import com.audianz.beans.PromotionPlanResponseBean;
import com.audianz.beans.RegistrationRequestBean;
import com.audianz.beans.RegistrationResponseBean;
import com.audianz.beans.ResponseBean;
import com.audianz.beans.UpdateCampaignRequest;
import com.audianz.beans.UpdateCampaignResponse;
import com.audianz.beans.UpdateRegisterRequestBean;
import com.audianz.constants.EventType;
import com.audianz.constants.InitResponseCode;
import com.audianz.database.Database;
import com.audianz.emcl.ELogger;
import com.audianz.network.ShortRequestNC;
import com.audianz.network.mEventObject;
import com.audianz.utilities.AudianzDatabaseUtility;
import com.audianz.utilities.RegisterUtility;
import com.audianz.utilities.UIStateMachine;
import com.google.android.gms.internal.el;
import com.google.android.gms.internal.ev;
/**
 * The Engine class is the core of this application.The initialization of the application
 * takes place in Engin's init() method.
 * * @author  
 * 
 */

public class Engine {

	Context mContext=null;
	public static Engine engObj = null;
	public UIStateMachine statMachineObj = null;
	public AudianzConfigReader cnfigReaderObj = null;
	public Database dbObj = null;
	private boolean initFlag = false;
	public  Handler uiHandler = null;

	public boolean processEventFlag = true;
	public ShortRequestNC shortReqNCObj = null;
	public RegisterUtility regUtilObj=null;
	public AudianzDatabaseUtility audiDbUtil =null;

	//This flag variable used to disable debug lines.
	public static final boolean IS_DEVELOPMENT_RELEASE = true; 

	public ELogger eLogger = null;
	private String TAG = "Engine";

	// models object
	public BaseModel baseModelObj = null;


	/**
	 * Constructor of Engine is private to make class singleton
	 */
	private Engine() {

	}

	/**
	 * This function is to get instance of Engine . This is creating new
	 * instance only when Engine instance is null
	 * 
	 * @return Engine Instance
	 */
	public static Engine getInstance() {

		if (engObj == null) {
			engObj = new Engine();
		}
		return engObj;
	}

	/**
	 * This method is responsible for initializing Engine class.
	 * @param context
	 * @return
	 */
	public int init(Context context)
	{
		if(!initFlag)
		{
			Log.d("Engine","Engine init called.. ");
			if(context == null)
			{
				Log.e("Engine", "init() : context is null.");
				return InitResponseCode.APP_CONTEXT_NULL;
			}
			mContext=context;
			eLogger = new ELogger();
			ELogger.init("MerchantAppLog.txt");
			eLogger.setTag(TAG);
			ELogger.setLogLevel(ELogger.ERROR);

			if (shortReqNCObj == null)
				shortReqNCObj = ShortRequestNC.getInstance();
			if (!shortReqNCObj.init(context, null)) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.error("init() : ShortRequestNC initialization fail");
				return InitResponseCode.NETWORK_INIT_FAIL;
			}

			statMachineObj = UIStateMachine.getInstance();
			if (!statMachineObj.init(context)) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.error("init() : UIStateMachine could not be initilaized. ");
				return InitResponseCode.INIT_FAILURE;
			}


			if (!initializeModel(context, eLogger)) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.error("init() : model initialization fail");
				return InitResponseCode.MODEL_INIT_FAIL;
			}

			dbObj = Database.getInstance();
			if(!dbObj.init(context,eLogger))
			{
				eLogger.error("init() : DatabaseController initilaization is failed.");
				return InitResponseCode.DB_INITFAIL;
			}

			if(!initializeUtility(context, null)){
				if(Engine.IS_DEVELOPMENT_RELEASE)
					eLogger.error("init() : utility initialization failed ");
				return InitResponseCode.UTILITY_INIT_FAIL;
			}


			statMachineObj = UIStateMachine.getInstance();
			createHandler();
			initFlag=true;
		}

		return InitResponseCode.INIT_SUCCESS;
	}//End of init()


	public boolean handleNetworkRequest(int evType, Object obj) {
		if (evType < 0	|| evType > EventType.MAXEVENT) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("handleNetworkRequest() : incorrect parameter");
			return false;
		}
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			eLogger.debug("handleNetworkRequest() : event type is :" + evType);

		try {

			switch (evType) {
			case EventType.REGISTRATION:
			{
				ServerResponse serResp = (ServerResponse)obj;
				RegistrationResponseBean resBean =(RegistrationResponseBean)serResp.respBean;
				if(resBean!=null)
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.debug("handleNetworkRequest() REGISTRATION event res bean is not null");
					engObj.cnfigReaderObj.setAUDIANZ_CLIENT_ID(resBean.getClientid());
					engObj.cnfigReaderObj.setAUDIANZ_BUSINESS_NAME(resBean.getBusiness_name());
					engObj.regUtilObj.insertRegisterValue(resBean);

					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);

					// Add fetch plan event here
					addFetchPromotionPlan();
				}
				else
				{

					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.error("handleNetworkRequest() REGISTRATION event res bean is null");
					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);
				}
			}
			break;
			case EventType.SIGNIN:
			{
				// Handle Sign In response
				ServerResponse serResp = (ServerResponse)obj;
				RegistrationResponseBean resBean =(RegistrationResponseBean)serResp.respBean;
				if(resBean!=null)
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.debug("handleNetworkRequest() SIGNIN event res bean is not null");
					engObj.cnfigReaderObj.setAUDIANZ_CLIENT_ID(resBean.getClientid());
					engObj.cnfigReaderObj.setAUDIANZ_BUSINESS_NAME(resBean.getBusiness_name());
					engObj.regUtilObj.insertRegisterValue(resBean);

					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);
				}
				else
				{

					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.error("handleNetworkRequest() SIGNIN event res bean is null");
					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);
				}
			}
			break;
			case EventType.PROMOTENOW:
			{
				ServerResponse serResp = (ServerResponse)obj;
				AddCampaignResponse resBean =(AddCampaignResponse)serResp.respBean;
				if(resBean!=null)
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.debug("handleNetworkRequest()  event res bean is not null for event type "+evType);

					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);
				}
				else
				{

					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.error("handleNetworkRequest() SIGNIN event res bean is null");
					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);
				}
			}
			break;
			case EventType.ADD_ORDER_DETAIL:
			case EventType.FETCH_CAMPAIGN_LIST:
			{
				ServerResponse serResp = (ServerResponse)obj;
				FetchCampaignResponse resBean =(FetchCampaignResponse)serResp.respBean;
				if(resBean!=null)
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.debug("handleNetworkRequest()  event res bean is not null for event type "+evType);

					ArrayList<CampaignListBean> cmpBean = resBean.getCamp_list();
					engObj.audiDbUtil.insertCampaignValue(cmpBean);	
					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);
				}
				else
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.error("handleNetworkRequest()  res bean is null");
					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);
				}
			}
			break;
			case EventType.FETCHPROMOTIONPLAN:
			{
				// Handle Fetch Promotion Plan
				ServerResponse serResp = (ServerResponse)obj;
				PromotionPlanResponseBean resBean =(PromotionPlanResponseBean)serResp.respBean;
				if(resBean!=null)
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.debug("handleNetworkRequest() FETCHPROMOTIONPLAN event res bean is not null");

					ArrayList<PlanResponseBean> pBean = resBean.getPlans();
					if(pBean!=null && pBean.size()>0)
					{
						engObj.audiDbUtil.insertPlanValue(pBean);
					}
					else
					{
						if(Engine.IS_DEVELOPMENT_RELEASE)
							eLogger.error("handleNetworkRequest() pBean  is null");
					}
					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);

				}
				else
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.error("handleNetworkRequest() FETCHPROMOTIONPLAN event res bean is null");

					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);
				}
			}
			break;
			case EventType.UPDATE_REGISTER:
			{
				// Handle update registration Response
				ServerResponse serResp = (ServerResponse)obj;
				RegistrationResponseBean resBean =(RegistrationResponseBean)serResp.respBean;
				if(resBean!=null)
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.debug("handleNetworkRequest() UPDATE_REGISTER event res bean is not null");
					engObj.cnfigReaderObj.setAUDIANZ_CLIENT_ID(resBean.getClientid());
					engObj.cnfigReaderObj.setAUDIANZ_BUSINESS_NAME(resBean.getBusiness_name());
					engObj.regUtilObj.updateRegisterTable(resBean);

					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);
				}
				else
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.error("handleNetworkRequest() REGISTRATION event res bean is null");
					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);
				}
			}
			break;
			case EventType.UPDATE_CAMPAIGN_STATUS:
			{
				ServerResponse serResp = (ServerResponse)obj;
				UpdateCampaignResponse campResp = (UpdateCampaignResponse)serResp.respBean;
				if(campResp!=null)
				{
					engObj.audiDbUtil.updateCampaignTable(campResp);
					if(statMachineObj.curActivity!=null)
						statMachineObj.curActivity.handleEvent(evType, campResp);
				}
				else
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.error("handleNetworkRequest() res for event type"+EventType.UPDATE_CAMPAIGN_STATUS);
					if(statMachineObj.curActivity!=null)
						statMachineObj.curActivity.handleEvent(evType, campResp);
				}
			}
			break;
			case EventType.FETCH_CAMPAIGN_STAT:
			{
				ServerResponse serResp = (ServerResponse)obj;
				FetchStatResponse resBean =(FetchStatResponse)serResp.respBean;
				if(resBean!=null)
				{
					ArrayList<CampaignStatBean> statBean = resBean.getStat_list();

					if(statBean!=null && statBean.size()>0)
					{
						engObj.audiDbUtil.insertStatValue(statBean);
					}
					else
					{
						if(Engine.IS_DEVELOPMENT_RELEASE)
							eLogger.error("statBean  is null");
					}
					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);					
				}
				else
				{
					if(Engine.IS_DEVELOPMENT_RELEASE)
						eLogger.error("handleNetworkRequest() Response bean is null for event type"+EventType.FETCH_CAMPAIGN_STAT);
					if (statMachineObj.curActivity != null)
						statMachineObj.curActivity.handleEvent(evType,resBean);
				}
			}
			break;
			case EventType.FORGET_PASSWORD:
			{
				ServerResponse serResp = (ServerResponse)obj;
				ResponseBean resBean =(ResponseBean)serResp.respBean;

				if(statMachineObj.curActivity!=null)
					statMachineObj.curActivity.handleEvent(evType, resBean);		
			}
			break;
			case EventType.EDIT_CAMPAIGN:
			{
				ServerResponse serResp = (ServerResponse)obj;
				EditCampInfoResponse resBean =(EditCampInfoResponse)serResp.respBean;
				if(Engine.IS_DEVELOPMENT_RELEASE)
					eLogger.debug("handleNetworkRequest() EDIT_CAMPAIGN event res bean is not null");

				if (statMachineObj.curActivity != null)
					statMachineObj.curActivity.handleEvent(evType,resBean);

			}
			break;
			default:

				break;
			}

		} catch (Exception e) {
			eLogger.error("Engine handleNetworkRequest() : exception : " + e);
		}

		return true;
	}


	public int addOrderDetail(AddOrderRequest orderBean)
	{
		if(baseModelObj == null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.error("addOrderDetail() baseModelObj is null");
		}
		mEventObject evObj = new mEventObject();
		evObj.reqBean = orderBean;
		int engResp  =  baseModelObj.requestToNetwork(EventType.ADD_ORDER_DETAIL, evObj);

		if(engResp != -1)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.debug("addOrderDetail() engin response is "+engResp);
		}
		else
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.error("addOrderDetail() engineResponse is null ");
		}
		return engResp;
	}

	public int promoteCampaign(PromoteRequestBean pBean)
	{
		if(baseModelObj==null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.error("promoteCampaign()  baseModelObj is null  ");
		}
		mEventObject evObj = new mEventObject();
		evObj.reqBean= pBean;
		int engResp  = baseModelObj.requestToNetwork(EventType.PROMOTENOW, evObj);

		if(engResp!=-1)
		{
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.info("promoteCampaign() : Engine Response : "
						+ engResp);
		}
		else
		{
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("promoteCampaign : engine resp null");
		}
		return engResp;
	}

	public int updateCampaignStatus(UpdateCampaignRequest cmpReqBean)
	{
		if(baseModelObj == null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.error("updateCampaignStatus baseModel is null");
		}
		mEventObject evObj = new mEventObject();
		evObj.reqBean = cmpReqBean;
		int engResp = baseModelObj.requestToNetwork(EventType.UPDATE_CAMPAIGN_STATUS, evObj);
		if(engResp!=-1)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.info("updateCampaignStatus() Engine response is "+engResp);
		}
		else
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.error("updateCampaignStatus() engine resp is null");
		}
		return engResp;
	}
	/**
	 * This method is used to call 
	 * @param signInBean
	 * @return
	 */
	public int registerUser(RegistrationRequestBean registerBean) {

		if(baseModelObj == null)
		{
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.info("registerUser() :  baseModelObj null...");
		}
		mEventObject evObj = new mEventObject();
		evObj.reqBean = registerBean;
		int engResp = baseModelObj.requestToNetwork(EventType.REGISTRATION,evObj);
		if (engResp != -1) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.info("registerUser() : Engine Response : "
						+ engResp);
		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("signInUser() : engine resp null");
		}
		return engResp;
	}

	/**
	 * This method is used to update user information
	 * @param regBean
	 * @return
	 */
	public int updateRegisterUser(UpdateRegisterRequestBean regBean)
	{
		if(baseModelObj==null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.error("UpdateRegisterUser baseModelObj is null");

		}
		mEventObject evObj  = new mEventObject();
		evObj.reqBean = regBean;
		int engResp = baseModelObj.requestToNetwork(EventType.UPDATE_REGISTER, evObj);
		if(engResp!=-1)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.info("updateRegisterUser()  eng resp is "+engResp);
		}
		else
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.error("updateRegisterUser()  eng resp is null");
		}
		return engResp;

	}

	public int addFetchCampaigndata()
	{
		if(baseModelObj == null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.error("addFetchCampaignData baseModelObj is null");
		}
		FetchCampaignListRequest fetchCampBean = new FetchCampaignListRequest();
		fetchCampBean.setClientid(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());
		mEventObject evObj = new mEventObject();
		evObj.reqBean  = fetchCampBean;
		int engResp = baseModelObj.requestToNetwork(EventType.FETCH_CAMPAIGN_LIST, evObj);
		if (engResp != -1) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.info("addFetchCampaigndata() : Engine Response : "
						+ engResp);
		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("addFetchCampaigndata() : engine resp null");
		}
		return engResp;


	}
	/**
	 * This method is used to Fetch Promotion Plan form server
	 * @return
	 */

	public int addFetchPromotionPlan()
	{
		if(baseModelObj == null)
		{
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.info("addFetchPromotionPlan() :  baseModelObj null...");
		}
		PromotePlanRequestBean pReqBean = new PromotePlanRequestBean();
		pReqBean.setClientid(engObj.cnfigReaderObj.getAUDIANZ_CLIENT_ID());

		mEventObject evObj = new mEventObject();
		evObj.reqBean = pReqBean;
		int engResp   = baseModelObj.requestToNetwork(EventType.FETCHPROMOTIONPLAN, evObj);

		if (engResp != -1) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.info("fetchPromotionPlan() : Engine Response : "
						+ engResp);
		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("fetchPromotionPlan() : engine resp null");
		}
		return engResp;

	}
	/**
	 * This method is used to login
	 * @param signInBean
	 * @return
	 */
	public int signInUser(LoginRequestBean signInBean) {
		if(baseModelObj == null)
		{
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("registerUser() : Engine Response : baseModelObj null...");

		}
		mEventObject evObj = new mEventObject();
		evObj.reqBean = signInBean;
		int engResp = baseModelObj.requestToNetwork(EventType.SIGNIN,evObj);
		if (engResp != -1) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.info("registerUser() : Engine Response : "
						+ engResp);
		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("signInUser() : engine resp null");
		}
		return engResp;
	}

	/**
	 * This method is used to add request to model for forget password
	 * @param bean  PasswordRequestBean
	 * @return
	 */
	public int forgetPassword(PasswordRequestBean bean)
	{
		if(baseModelObj == null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.error("forgetPassword()  model is null");
		}

		mEventObject evObj = new mEventObject();
		evObj.reqBean = bean;
		int engResp = baseModelObj.requestToNetwork(EventType.FORGET_PASSWORD, evObj);
		if(engResp == -1 )
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.error("forgetPassword() engine resp is null");
		}
		return engResp;
	}

	public boolean sendMsgToUI(int eventType, ServerResponse serResp)
	{
		if(eventType < EventType.MAXEVENT && eventType > EventType.MINEVENT && serResp != null)
		{	
			Message message = new Message();
			message.what = eventType;
			message.obj = serResp;
			Engine.engObj.uiHandler.sendMessage(message);
		}
		return true;
	}

	/**
	 *  This method is responsible for creating handler for handling events coming from UI. 
	 */
	private void createHandler()
	{
		uiHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) 
			{
				handleNetworkRequest(msg.what, msg.obj);
			};
		};
	}



	//................. ESSENTIAL INNITIALIZATION

	/**
	 * This method is used to initialize all the model classes.
	 * 
	 * @param context
	 * @param logger
	 * @return
	 */
	private boolean initializeModel(Context context, ELogger logger) {
		boolean modelInit = false;
		eLogger.debug(" Enter Engine:initializeModel........");

		if(baseModelObj == null)
			baseModelObj = BaseModel.getInstance();

		baseModelObj.init(context, eLogger);

		if (cnfigReaderObj == null)
			cnfigReaderObj = AudianzConfigReader.getInstance();
		if (!cnfigReaderObj.init(context, logger)) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("init() : ConfigurationReader model initialization fail");
			modelInit = false;
		}
		modelInit = true;
		return modelInit;
	}

	/**
	 * This method is used to initialize all the utility classes.
	 * 
	 * @param context
	 * @param logger
	 * @return
	 */
	private boolean initializeUtility(Context context, ELogger logger) {
		boolean utilityInit = true;
		if (regUtilObj == null)
			regUtilObj = RegisterUtility.getInstance();
		if (!regUtilObj.init(context, null)) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("init() : RegisterUtil initialization fail");
			utilityInit = false;
			return utilityInit;
		}

		if(audiDbUtil==null)
			audiDbUtil = AudianzDatabaseUtility.getInstance();
		if(!audiDbUtil.init(context, null))
		{
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("init() : AudianzDatabaseUtility initialization fail");
			utilityInit = false;
			return utilityInit;
		}
		return utilityInit;

	}

	/**
	 * This function is to check whether Engine is initialized
	 * 
	 * @return
	 */
	public boolean isInitialized() {
		return initFlag;
	}

	public int editCampaignReq(EditCampInfoRequest eBean) 
	{
		if(baseModelObj==null)
		{
			if(Engine.IS_DEVELOPMENT_RELEASE)
				eLogger.error("editCampaignReq()  baseModelObj is null  ");
		}
		mEventObject evObj = new mEventObject();
		evObj.reqBean= eBean;
		int engResp  = baseModelObj.requestToNetwork(EventType.EDIT_CAMPAIGN, evObj);

		if(engResp!=-1)
		{
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.info("editCampaignReq() : Engine Response : "
						+ engResp);
		}
		else
		{
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("editCampaignReq : engine resp null");
		}
		return engResp;
	}




}