package com.audianz.network;

import com.audianz.core.Engine;
import com.audianz.constants.EventType;
import com.audianz.emcl.ELogger;

import android.content.Context;

/**
 * The ShortRequestNC class is a network class which handles short json request
 * and response.
 * 
 * @author 
 * 
 */
public class ShortRequestNC extends NetworkController {

	private static ShortRequestNC shortReqNCObj = null;

	private ShortRequestNC() {
		TAG = "SHORTREQUESTNC";
		initFlag = false;
		engObj = null;
		eLogger = null;
	}

	/**
	 * This function is to get instance of LongRequestNC . This is to create new
	 * instance only when LongRequestNC instance is null
	 * 
	 * @return shortReqNCObj
	 */
	public static ShortRequestNC getInstance() {
		if (shortReqNCObj == null)
			shortReqNCObj = new ShortRequestNC();
		return shortReqNCObj;
	}

	/**
	 * This function is to initialize ShortRequestNC.It will initiate a new
	 * thread to handle short network operation
	 * 
	 * @param Context
	 *            context
	 * @param ELogger
	 *            logger
	 */
	@Override
	public boolean init(Context context, ELogger logger) {
		if (initFlag) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.debug("init() : already initialized");
			return initFlag;
		}
		initFlag = super.init(context, logger);
		eLogger.debug("***** shortRequest init.... ");
		return initFlag;
	}

	@Override
	public boolean handleEvent(int evType, int index, Object obj) {
		
		eLogger.debug("******* Enter ShortRequestNC:handleEvent ... ");
		if (evType < 0 || evType > EventType.MAXEVENT || obj == null) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				eLogger.error("handleEvent() : incorrect parameter");
			return false;
		}
		try {
			switch (evType) {
			/*case EventType.CONNECTFB:
				break;
			case EventType.CONNECTTWITTER:
				// need not to process this event as their url is loaded in
				// webview
				break;
			*/default:
				super.handleEvent(evType, index, obj);
				break;
			}
		} catch (Exception e) {

			eLogger.fatal("ShortRequestNC : handleEvent() : exception caught : "
					+ e);
			e.printStackTrace();
		}
		return false;
	}
}
