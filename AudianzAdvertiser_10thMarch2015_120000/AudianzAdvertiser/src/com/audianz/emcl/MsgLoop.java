///////////////////////////////////////////////////////////////////////
//                                                                   //
//                                                                   //
//           Eninov Mobility Solutions                               //
//                                                                   //
//           Author: Anjali Kaushal                                  //
//           Created On: 15th Feb 2011                               //
//           Modified By:                                            //
//           Modified On:                                            //
//                                                                   //
//           This code is part of Eninov Mobile Common Layer         //
//           Copyright (c) 2011: Eninov Mobility Solutions           //
//                                                                   //
///////////////////////////////////////////////////////////////////////

package com.audianz.emcl;

//import com.eninov.campaignmgt.appcore.Engine;

import com.audianz.core.Engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * The MsgLoop class is used to create a thread with looper and handler. When
 * Looper.prepare() is called inside run method of any thread, a message queue
 * is initialized for that thread. Other threads, can post messages to this
 * queue. To handle these messages, a handler needs to be bound to the message
 * queue. The Handler(class) will have callbacks which would be invoked whenever
 * appropriate messages are posted in the thread's message queue.
 * 
 * @author 
 * 
 */
public class MsgLoop {
	private int maxEventType = 25;
	private HandlerFunction[] callbackHandler = null;
	private Object[] cbObject = null;
	protected boolean initFlag = false;
	protected ELogger log = null;
	public final static String MSGLOOPLOG = "EMCLMSGLOOP";

	// using this as semaphore mechanism
	private boolean syncFlag = false;
	private final static int syncCount = 5;
	private final static long SLEEP_INTVL = 50;

	class LooperThread extends Thread {
		public Handler mHandler = null;

		public void run() {

			Looper.prepare();

			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
						log.debug("handleMessage in MsgLoop");
					handleEvent((int) msg.arg1, (int) msg.arg2,
							(Object) msg.obj);
				}
			};
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				log.info("In the Message Loop Thread");
			syncFlag = true;
			Looper.loop();
		}
	}

	LooperThread loop;

	public MsgLoop() {
		initFlag = false;
	}

	/**
	 * This function simply starts the message loop and initializes all the
	 * related objects.
	 * 
	 * @param appLogger
	 * @param noOfEventTypes
	 * @return
	 */
	public boolean init(Object appLogger, int noOfEventTypes) {
		if (initFlag) {
			if (log != null)
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					log.error("Message loop is already initialised");
			return false;
		}

		if (appLogger == null) {
			log = new ELogger();
			log.setTag("MSGLOOPLOG");
		} else
			log = (ELogger) appLogger;
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			log.info("init:Initialising Message Loop");

		if (noOfEventTypes != 0) {
			maxEventType = noOfEventTypes;
		}

		// This part of the code is not used.
		callbackHandler = new HandlerFunction[maxEventType];
		cbObject = new Object[maxEventType];

		if ((callbackHandler == null) || (cbObject == null)) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				log.error("init:Memory Allocation Failed");
			return false;
		}

		syncFlag = false;

		// Initializing new Looper thread
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			log.verbose("init:Creating Looper Thread");
		loop = new LooperThread();
		if (loop == null) {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				log.error("init:Memory Allocation for Thread failed");
			return false;
		} else {
			try {
				loop.start();
			} catch (Exception e) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					log.error("init:Caught Exception when starting new thread");
				return false;
			}
			while ((syncCount > 0) && (!syncFlag)) {
				try {
					Thread.sleep(SLEEP_INTVL);
				} catch (Exception e) {
					if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
						log.error("init:Exception in thread sleep");
				}
			}
			if (syncFlag) {
				initFlag = true;
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					log.info("init:Message Loop Successfully Initialised");
				return true;
			} else {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					log.error("init:The other thread failed to start");
				return false;
			}
		}
	}

	/**
	 * This function sets the object of type Handler Function for an event type.
	 * This objects callback function is called when MsgHandler receives event
	 * type. Parameter cbObj is passed to the callback function. This method is
	 * not being used.
	 * 
	 * @param type
	 * @param cbHndl
	 * @param cbObj
	 * @return
	 */
	public boolean setEventHandler(int type, HandlerFunction cbHndl,
			Object cbObj) {
		if ((type >= maxEventType) || (!initFlag) || (cbHndl == null)) {
			if (log != null) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					log.error("setEventHandler: Incorrect inputs");
			}
			return false;
		}

		/*
		 * If the applications sets the handler for an event type twice then
		 * first set handler is lost. Only one handler is set for an event type
		 */
		callbackHandler[type] = cbHndl;
		cbObject[type] = cbObj;
		return true;

	}

	/**
	 * This function allows events to be added to message loop. The events are
	 * handles by the message loop in the sequence in which they are added.
	 * 
	 * @param type
	 *            eventType which is added
	 * @param index
	 *            Where to be added
	 * @param evObj
	 *            data which is to be passed
	 * @return
	 */
	public boolean addEvent(int type, int index, Object evObj) {
		if (type >= maxEventType) {
			if (log != null) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					log.error("addEvent: Incorrect event type "
							+ Integer.toString(type));
			}
			return false;
		}
		if (!initFlag) {
			if (log != null) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					log.error("addEvent: MsgLoop not initialized.");
			}
			return false;
		}

		if (loop.mHandler != null) {
			Message newMsg = loop.mHandler.obtainMessage();
			newMsg.arg1 = type;
			newMsg.arg2 = index;
			newMsg.obj = evObj;

			loop.mHandler.sendMessage(newMsg);
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				log.debug("addEvent:Sent Message type " + Integer.toString(type));
			return true;
		} 
		else 
		{
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				log.debug("addEvent:Handler not Initialised"
						+ Integer.toString(type));
			return false;
		}

	}

	/**
	 * This function handles all the events that have been set to be handled in
	 * the event loop. If any event is not set then it is discarded.
	 * 
	 * @param type
	 * @param index
	 * @param obj
	 * @return
	 */
	protected boolean handleEvent(int type, int index, Object obj) {
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			log.info("handleEvent: In Base Message Loop Event Handler");
		HandlerFunction cur = callbackHandler[type];
		if (cur != null) {
			try {
				cur.callback(type, index, obj, cbObject[type]);
			} catch (Exception e) {
				if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
					log.error("handleEvent: Exception generated for the callback function of event type"
							+ Integer.toString(type));
				return false;
			}
			return true;
		} else {
			if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
				log.warn("handleEvent: No callback handler set for event type:"
						+ Integer.toString(type));
		}
		return false;

	}

	public boolean close() {
		// See how to stop the other thread
		if (Engine.IS_DEVELOPMENT_RELEASE)// This log is not print now.
			log.info("close:Closing the Message Loop Thread");
		loop.destroy();

		return true;
	}

}
