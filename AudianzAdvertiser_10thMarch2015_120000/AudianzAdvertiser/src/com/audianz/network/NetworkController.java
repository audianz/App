package com.audianz.network;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import android.content.Context;
import android.text.TextUtils;

import com.audianz.beans.AddCampaignResponse;
import com.audianz.beans.EditCampInfoResponse;
import com.audianz.beans.FetchCampaignResponse;
import com.audianz.beans.FetchStatResponse;
import com.audianz.beans.LoginResponseBean;
import com.audianz.beans.PlanResponseBean;
import com.audianz.beans.PromoteResponseBean;
import com.audianz.beans.PromotionPlanResponseBean;
import com.audianz.beans.RegistrationResponseBean;
import com.audianz.beans.RequestBean;
import com.audianz.beans.ResponseBean;
import com.audianz.beans.UpdateCampaignResponse;
import com.audianz.constants.EngineEventType;
import com.audianz.constants.EventType;
import com.audianz.constants.HttpConstant;
import com.audianz.constants.NetworkResponse;
import com.audianz.constants.NetworkResponseCode;
import com.audianz.constants.ServerResponseCode;
import com.audianz.core.Engine;
import com.audianz.core.ServerResponse;
import com.audianz.emcl.ELogger;
import com.audianz.emcl.MsgLoop;
import com.audianz.utilities.Common;


/**
 * The NetworkController class extends MsgLoop. The classes ShortRequestNC,
 * LongRequestNC and PreemptedRequestNC extends this class. It provides the
 * functionalities for making network request and also after receiving
 * successful or failure response adds a net event to engine thread.
 * 
 * @author 
 * 
 */
public class NetworkController extends MsgLoop {

	private static String charset = "utf-8";
	protected boolean initFlag = false;
	protected ELogger eLogger = null;
	protected String TAG = "NetworkController";
	protected Engine engObj = null;
	protected ArrayList<NetworkEvent> netEventQueue = null;
	protected String httpUrl = null;
	protected NetworkEvent netEvObj = null;
	protected Context appContext = null;
	protected HttpPost httpPost = null;
	HttpURLConnection httpConn = null;
	private static final String FLD_DATA = "data";

	/**
	 * This function is to initialize network thread
	 * 
	 * @param context
	 * @param eLogger
	 * @return
	 */
	public boolean init(Context context, ELogger logger) {
		if (initFlag) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("init() : network already initialized");
			return initFlag;
		}
		if (logger == null) {
			eLogger = new ELogger();
		} else {
			eLogger = logger;
		}
		eLogger.setTag(TAG);

		eLogger.debug("Enter Network init ....... ");
		if (context == null) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("init() : application context is null");
			return initFlag;
		}
		appContext = context;
		engObj = Engine.engObj;
		if (engObj == null) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("init() : Engine Object is null");
			return initFlag;
		}
		if (!super.init(eLogger, EventType.MAXEVENT)) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("init() : msgloop initialization fail");
			return initFlag;
		}
		//httpUrl = "http://54.235.173.134/androidrequest.php";
		//httpUrl = "http://54.235.173.134/index.php/site/siteapi";
		initFlag = true;
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			eLogger.debug("init() :network initialized successfully");
		return initFlag;
	}

	/**
	 * This function is to add network event to event queue for further
	 * processing
	 * 
	 * @param eventType
	 * @param obj
	 * @return response code
	 */
	public int addEvent(int eventType, Object obj) {
		try {

			if (eventType < 0 || eventType >= EventType.MAXEVENT || obj == null) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.error("addEvent() : invalid input parameters");
				return ServerResponseCode.INCORRECT_PARAM;
			}
			if (!Common.isNetworkAvailable(appContext)) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.info("addEvent() : network not available");
				return ServerResponseCode.NET_NOT_AVAILABLE;
			}
			if (netEventQueue == null)
				netEventQueue = new ArrayList<NetworkEvent>();
			NetworkEvent netEvent = new NetworkEvent();
			netEvent.evType = eventType;
			netEvent.obj = obj;
			if (netEvObj == null) {
				if (super.addEvent(eventType, 0, obj)) {
					if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
						eLogger.debug("addEvent : event added in msg event queue");
					netEvent.addedInMsgQueue = true;
					netEvObj = netEvent;
				} else {
					if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
						eLogger.error("addEvent : event could not added in msg loop event queue");
				}
			} else {
				netEvent.addedInMsgQueue = false;
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.debug("addEvent : net event is not null");
			}
			netEventQueue.add(netEvent);
		} catch (Exception e) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("NetworkController : addEvent() : " + e);
			return ServerResponseCode.ADD_EVENT_FAIL;
		}
		return ServerResponseCode.IN_PROGRESS;
	}

	@Override
	public boolean handleEvent(int evType, int index, Object obj) {
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			eLogger.info("******* NetworkController handleEvent() : Entry");
		long allocatedHeap = android.os.Debug.getNativeHeapAllocatedSize();
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			eLogger.info("handleEvent() : allocated heap before network event processing : "
					+ allocatedHeap);
		try {
			if (evType < 0 || evType > EventType.MAXEVENT || obj == null) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.error("handleEvent() : incorrect parameter");

				return false;
			}
			mEventObject evObj = (mEventObject) obj;
			if (!Common.isNetworkAvailable(appContext)) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.error("handleEvent() : network not available");
				if (netEventQueue != null) {
					netEventQueue.clear();
					netEventQueue = null;
					netEvObj = null;
				}
				ServerResponse serResp = new ServerResponse();
				serResp.respcode = ServerResponseCode.NET_NOT_AVAILABLE;
				serResp.reqBean = evObj.reqBean;
				engObj.sendMsgToUI(evType, serResp);
				// eLogger.info("handleEvent() : allocated heap after network event processing net not available : "+allocatedHeap);
				return false;
			}
			if (evObj.reqBean == null) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.error("handleEvent() : req bean object is null for event type : "
							+ evType);
				manageEvent(null, evType, obj);
				ServerResponse serResp = new ServerResponse();
				serResp.respcode = ServerResponseCode.INCORRECT_PARAM;
				engObj.sendMsgToUI(evType, serResp);
				return false;
			}
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.info("NetworkController handleEvent() : bean to json");

			// TODO Adding controller name to list based on API
			String httpUrlStr = addControllerName(evType);
			eLogger.debug("NetworkController: handleEvent httpUrlString " + httpUrlStr);
			String reqJson = beanToJson(evObj.reqBean);
			NetworkResponse netResp = null;


			switch (evObj.httpMethod) 
			{
			case HttpConstant.HTTP_POST:

				long time = System.currentTimeMillis();
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.debug("handleEvent() : httpPost request for event type"
							+ evType + "at time" + time);
				netResp = httpPost(reqJson,httpUrlStr);
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.debug("handleEvent() : httpPost response get for event type : "
							+ evType
							+ " after time : "
							+ (System.currentTimeMillis() - time));
				break;

			}
			ServerResponse serResp = null;
			if (netResp != null) {
				manageEvent(netResp, evType, obj);
				serResp = parseNetResponse(netResp, evType);
			}
			if (serResp == null) {
				serResp = new ServerResponse();
				serResp.respcode = ServerResponseCode.NET_EXCEPION;
			}
			serResp.reqBean = evObj.reqBean;
			serResp.bBean = evObj.baseBean;
			engObj.sendMsgToUI(evType, serResp);
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.info("NetworkController handleEvent() : EXIT");
		} catch (Exception e) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("NetworkController : handleEvent() : " + e);
			ServerResponse serResp = new ServerResponse();
			serResp.respcode = ServerResponseCode.NET_EXCEPION;
			engObj.sendMsgToUI(evType, serResp);
		}
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			eLogger.info("handleEvent() : allocated heap after network event processing  : "
					+ allocatedHeap);
		return true;
	}

	/**
	 * This method is used to add controller name to the base URL
	 * @param evType
	 */
	private String addControllerName(int evType) {
		String httpUrlStr=null;
		String baseUrlStr = "http://54.235.252.159/index.php/";
		switch (evType) {
		case EventType.REGISTRATION:
		case EventType.SIGNIN:
		case EventType.UPDATE_REGISTER:
		case EventType.FORGET_PASSWORD:
			httpUrlStr = baseUrlStr + "site/siteapi";
			break;
		case EventType.PROMOTENOW:
		case EventType.FETCHPROMOTIONPLAN:
		case EventType.FETCH_CAMPAIGN_STAT:
		case EventType.UPDATE_CAMPAIGN_STATUS:
		case EventType.FETCH_CAMPAIGN_LIST:
		case EventType.ADD_ORDER_DETAIL:
		case EventType.EDIT_CAMPAIGN:
			httpUrlStr = baseUrlStr + "advertiser/campaignapi";
		break;
		default:
			break;
		}
		return httpUrlStr;
	}

	/**
	 * This function is to convert reqbean to reqjson
	 * 
	 * @param bean
	 *            RequestBean
	 * @return string json
	 */
	public String beanToJson(RequestBean bean) {
		if (bean == null) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("beanToJson : incorrect param");
			return null;
		}
		String reqJson = null;
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES,
				false);
		try {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.info("Network : API Request Command : " + bean.getApi());
			reqJson = objMapper.writeValueAsString(bean);
		} catch (JsonGenerationException e) {
			reqJson = null;

			eLogger.fatal("beanToJson : JsonGenerationException " + e);

		} catch (JsonMappingException e) {
			reqJson = null;
			eLogger.fatal("beanToJson : JsonMappingException " + e);

		} catch (IOException e) {
			reqJson = null;
			eLogger.fatal("beanToJson : IOException " + e);

		}
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			eLogger.info("beanToJSON() : Request JSON  :" + reqJson);
		return reqJson;
	}


	/**
	 * This method is used to close all the http connections.
	 * 
	 * @param httpConn
	 * @return
	 */
	private boolean closeHttpConnection(HttpURLConnection httpConn) {
		boolean rslt = false;
		if (httpConn != null) {
			httpConn.disconnect();
			httpConn = null;
			rslt = true;
		} else {
			rslt = false;
		}
		return rslt;
	}

	/**
	 * This method is used to close all the file Input streams.
	 * 
	 * @param fileInputStream
	 * @return
	 */
	private boolean closeFileInputStream(FileInputStream fileInputStream) {
		boolean rslt = false;
		if (fileInputStream != null) {
			try {
				fileInputStream.close();
				rslt = true;
			} catch (IOException e) {
				rslt = false;
				e.printStackTrace();
			}
			fileInputStream = null;
		} else {
			rslt = false;
		}
		return rslt;

	}

	/**
	 * This method is used to send http post request to server.
	 * 
	 * @param reqJson
	 * @return
	 */
	private NetworkResponse httpPost(String reqJson,String httpUrlStr) {
		NetworkResponse netResp = null;
		if (TextUtils.isEmpty(reqJson) || TextUtils.isEmpty(httpUrlStr)) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("httpPost() : req json null or or null url");
		} else {
			netResp = new NetworkResponse();
			httpPost = null;
			try {
				httpPost = new HttpPost(httpUrlStr);

			} catch (IllegalArgumentException e) {
				eLogger.fatal("httpPost() : IllegalArgumentException " + e);
				netResp.netRespCode = NetworkResponseCode.INVALID_URL;
				httpPost = null;
			}
			if (httpPost != null) {
				BasicHttpParams basicHttpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(basicHttpParams,
						HttpConstant.HTTP_REQUEST_TIMEOUT);
				HttpConnectionParams.setSoTimeout(basicHttpParams,
						HttpConstant.SOCKET_REQUEST_TIMEOUT);
				HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);


				nameValuePairs
				.add(new BasicNameValuePair(FLD_DATA, reqJson));
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,charset));
					// CompressionUtil
					// .setCompressedEntity(appContext,
					// new UrlEncodedFormEntity(nameValuePairs).toString(),
					// httpPost);
				} catch (Exception e) {

					eLogger.fatal("httpPost : UnsupportedEncodingException" + e);
					httpPost = null;
					netResp.netRespCode = NetworkResponseCode.NET_EXCEPTION;
					return netResp;
				}

				DefaultHttpClient httpClient = new DefaultHttpClient(
						basicHttpParams);
				HttpResponse response = null;
				try {
					if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
						eLogger.debug("httpPost : sending request to network ");
					response = httpClient.execute(httpPost);
					if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
						eLogger.debug("httpPost : response from network  : "
								+ response);
				} catch (ClientProtocolException e) {
					eLogger.fatal("httpPost : ClientProtocolException" + e);

					closeHttpPost(httpPost);
					netResp.netRespCode = NetworkResponseCode.NET_EXCEPTION;
					return netResp;
				} catch (ConnectTimeoutException e) {
					netResp.netRespCode = NetworkResponseCode.NET_REQ_TIMEOUT;
					eLogger.fatal("httpPost : ConnectTimeoutException" + e);
					closeHttpPost(httpPost);
					return netResp;
				} catch (SocketTimeoutException e) {
					netResp.netRespCode = NetworkResponseCode.NET_REQ_TIMEOUT;

					eLogger.fatal("httpPost : SocketTimeoutException : " + e);
					closeHttpPost(httpPost);
					return netResp;
				} catch (Exception e) {
					netResp.netRespCode = NetworkResponseCode.NET_EXCEPTION;
					eLogger.fatal("httpPost : Network Exception" + e);
					closeHttpPost(httpPost);
					return netResp;
				}

				StringBuffer resposebuf = new StringBuffer();
				StatusLine status = response.getStatusLine();
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStreamReader inputStreamReader = null;
					BufferedReader bufferedReader = null;
					try {
						inputStreamReader = new InputStreamReader(
								entity.getContent());
						bufferedReader = new BufferedReader(inputStreamReader,
								HttpConstant.DEFAULT_BUFF_SIZE);
						String str;
						while ((str = bufferedReader.readLine()) != null) {
							resposebuf.append(str);
						}
					} catch (OutOfMemoryError e) {

						eLogger.fatal("httpPost : OutOfMemoryError" + e);

						netResp.netRespCode = NetworkResponseCode.NET_EXCEPTION;
						return netResp;
					} catch (IllegalStateException e) {
						if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print
							// now.
							eLogger.error("httpPost : IOException" + e);
						e.printStackTrace();
						netResp.netRespCode = NetworkResponseCode.NET_EXCEPTION;
						return netResp;
					} catch (IOException e) {
						if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print
							// now.
							eLogger.error("httpPost : IOException" + e);
						e.printStackTrace();
						netResp.netRespCode = NetworkResponseCode.NET_EXCEPTION;
						return netResp;
					} finally {
						closeHttpPost(httpPost);
						closeBufferedReader(bufferedReader);
						closeInputStream(inputStreamReader);
					}
				}
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.info("httpPost : response code : "
							+ status.getStatusCode());
				if (status.getStatusCode() != 200) {
					if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
						eLogger.error("httpPost : invalid status code HTTP:"
								+ status.getStatusCode() + " "
								+ status.getReasonPhrase() + "\n"
								+ resposebuf.toString());
					netResp.netRespCode = NetworkResponseCode.NET_EXCEPTION;
					return netResp;
				} else {
					netResp.netRespCode = NetworkResponseCode.NET_RESP_SUCCESS;
					netResp.respStr = resposebuf.toString();

				}
			} else {

			}
		}
		// Common.checkObject("httpPost: EXIT : ");
		return netResp;
	}

	/**
	 * This method is used to close all the http post request.
	 * 
	 * @param httpPost
	 * @return
	 */
	private boolean closeHttpPost(HttpPost httpPost) {
		boolean rslt = false;
		if (httpPost != null) {
			httpPost.abort();
			httpPost = null;
			rslt = true;
		} else {
			rslt = false;
		}
		return rslt;
	}

	/**
	 * This method is used to close input streams.
	 * 
	 * @param inputStream
	 * @return
	 */
	private boolean closeInputStream(InputStreamReader inputStream) {
		boolean rslt = false;
		if (inputStream != null) {
			try {
				inputStream.close();
				rslt = true;
			} catch (IOException e) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.error("closeInputStream() : exception while closing input stream : "
							+ e);
				e.printStackTrace();
				rslt = false;
			}
			inputStream = null;
		} else {
			rslt = false;
		}
		return rslt;
	}

	/**
	 * This method is used to close all the buffered readers.
	 * 
	 * @param bufReader
	 * @return
	 */
	private boolean closeBufferedReader(BufferedReader bufReader) {
		boolean rslt = false;
		if (bufReader != null) {
			try {
				bufReader.close();
				rslt = true;
			} catch (IOException e) {
				e.printStackTrace();
				rslt = false;
			}
		} else {
			rslt = false;
		}
		return rslt;
	}

	/**
	 * This method is used to manage the network events.
	 * 
	 * @param netResp
	 */
	protected void manageEvent(NetworkResponse netResp, int evType, Object obj) {
		if (obj == null) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("manageEvent() : object is null");
			return;
		}
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			eLogger.info("manageEvent() : Response : for event type : "
					+ evType + "response : " + netResp);
		if (netEventQueue != null && netEventQueue.size() > 0) {
			NetworkEvent netEvent = netEventQueue.get(0);
			if (netEvent != null) {
				if (obj.equals(netEvent.obj)) {
					if (netEvent.addedInMsgQueue) {
						if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print
							// now.
							eLogger.debug("manageEvent() : event is handled successfully , removing it from network queue");
						netEventQueue.remove(netEvent);
						netEvObj = null;
					} else {
						if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print
							// now.
							eLogger.error("manageEvent() : addedInMsgQueue flag is false of processed event");
					}
				} else {
					if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
						eLogger.error("manageEvent() : processed event is not same as first network event queue");
				}
			} else {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.error("manageEvent() : netEvent is null");
			}
		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("manageEvent() : netEventQueue is null");
		}
		// }
		if (Common.networkFlag) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.debug("manageEvent() :network is available");
			if (netEventQueue != null && netEventQueue.size() > 0) {
				NetworkEvent netEv = netEventQueue.get(0);
				if (netEv != null) {
					if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
						eLogger.debug("manageEvent() : adding next event from network queue to msg queue");
					if (netEvObj == null) {
						if (super.addEvent(netEv.evType, 0, netEv.obj)) {
							if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not
								// print now.
								eLogger.debug("manageEvent() : event added successfully in msg event queue");
							netEv.addedInMsgQueue = true;
							netEvObj = netEv;
						} else {
							netEv.addedInMsgQueue = false;
							netEvObj = null;
							if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not
								// print now.
								eLogger.error("manageEvent() : event could not added in msg event queue");
						}
					} else {
						netEv.addedInMsgQueue = false;
						if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print
							// now.
							eLogger.debug("manageEvent() : net event is not null");
					}
				}
			} else {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.info("manageEvent() :network event queue is of size 0");
			}
		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("manageEvent() :network is not available");
		}
	}

	/**
	 * this method is used to parse the network server response.
	 * 
	 * @param netResp
	 * @param evType
	 * @return
	 */
	private ServerResponse parseNetResponse(NetworkResponse netResp, int evType) {
		ServerResponse servResp = null;
		if (netResp == null) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("parseNetResponse() :network response is null");
			return servResp;
		}
		servResp = new ServerResponse();
		if (netResp.netRespCode == NetworkResponseCode.NET_REQ_TIMEOUT) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.debug("parseNetResponse() :network response req time out");
			servResp.respcode = ServerResponseCode.REQ_TIME_OUT;
			servResp.respBean = null;
		} else if (netResp.netRespCode == NetworkResponseCode.NET_EXCEPTION) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.debug("parseNetResponse() :network exception");
			servResp.respcode = ServerResponseCode.NET_EXCEPION;
			servResp.respBean = null;
		} else if (netResp.netRespCode == NetworkResponseCode.INVALID_URL) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.debug("parseNetResponse() : invalid url");
			servResp.respcode = ServerResponseCode.INVALID_URL;
			servResp.respBean = null;
		} else if (netResp.netRespCode == NetworkResponseCode.FILE_NOT_FOUND) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.debug("parseNetResponse() : file not found");
			servResp.respcode = ServerResponseCode.FILE_NOT_FOUND;
			servResp.respBean = null;
		} else if (netResp.netRespCode == NetworkResponseCode.NET_RESP_SUCCESS) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.debug("parseNetResponse() :network response is success");
			ResponseBean respBean = jsonToBean(netResp.respStr, evType);
			if (respBean == null) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.error("parseNetResponse() :response bean is null");
				servResp.respcode = ServerResponseCode.NULL_DATA;
				servResp.respBean = null;
			} else {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					eLogger.debug("parseNetResponse() :network response is success and response bean is not null");
				servResp.respcode = ServerResponseCode.SUCCESS;
				servResp.respBean = respBean;
			}
		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("parseNetResponse() :network response exception :"
						+ netResp.netRespCode);
			servResp.respcode = ServerResponseCode.ERROR;
			servResp.respBean = null;
		}
		return servResp;
	}

	/**
	 * This method is used to convert json string to bean object using Object
	 * mapper.
	 * 
	 * @param json
	 * @param evType
	 * @return
	 */
	public ResponseBean jsonToBean(String json, int evType) {
		ResponseBean respBean = null;
		if (json == null) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("jsonToBean : null json response");
			return respBean;
		}
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			eLogger.info("jsonToBean : response json  for event type : "
					+ evType + " : json : " + json);
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		try {
			switch (evType) {
			case EventType.SIGNIN:
				RegistrationResponseBean signInRespBean = objMapper.readValue(json,RegistrationResponseBean.class);
				respBean = signInRespBean;
				break;
			case EventType.FORGET_PASSWORD:
				respBean  = objMapper.readValue(json,ResponseBean.class);
				break;
			case EventType.REGISTRATION:
				RegistrationResponseBean registrationRespBean = objMapper.readValue(json,RegistrationResponseBean.class);
				respBean = registrationRespBean;
				break;
			case EventType.PROMOTENOW:
				AddCampaignResponse campResp = objMapper.readValue(json, AddCampaignResponse.class);
				respBean = campResp;
				break;
			case EventType.ADD_ORDER_DETAIL:
			case EventType.FETCH_CAMPAIGN_LIST:
				FetchCampaignResponse  pRespBean = objMapper.readValue(json,FetchCampaignResponse.class);
				respBean=pRespBean;
				break;
			case EventType.FETCHPROMOTIONPLAN:
				PromotionPlanResponseBean planBean  = objMapper.readValue(json, PromotionPlanResponseBean.class);
				respBean = planBean;
				break;
			case EventType.UPDATE_REGISTER:
				RegistrationResponseBean rRespbean   = objMapper.readValue(json, RegistrationResponseBean.class);
				respBean = rRespbean;
				break;
			case EventType.FETCH_CAMPAIGN_STAT:
				FetchStatResponse statResp           = objMapper.readValue(json, FetchStatResponse.class);
				respBean = statResp;
				break;
			case EventType.UPDATE_CAMPAIGN_STATUS:
				UpdateCampaignResponse  respStat                   = objMapper.readValue(json, UpdateCampaignResponse.class);
				respBean = respStat;
				break;
			case EventType.EDIT_CAMPAIGN:
				EditCampInfoResponse editCampResp = objMapper.readValue(json, EditCampInfoResponse.class);
				respBean = editCampResp;
				break;
			default:
				break;
			}
		} catch (JsonParseException e) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("jsonToBean : JsonParseException For event Type "
						+ evType + ": " + e);
		} catch (JsonMappingException e) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("jsonToBean : JsonMappingException For event Type "
						+ evType + ": " + e);
		} catch (IOException e) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("jsonToBean : JsonMappingException For event Type "
						+ evType + ": " + e);
		}
		return respBean;
	}

	/**
	 * This method is used to reset the network queue
	 * 
	 * @return
	 */
	public boolean resetNetworkQueue() {
		netEvObj = null;
		if (netEventQueue != null) {
			netEventQueue.clear();
		}
		netEventQueue = null;
		return false;
	}

	public boolean close() {
		resetNetworkQueue();
		super.close();
		return false;
	}
}
