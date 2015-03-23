package com.audianz.constants;

/**
 * The ServerResponseCode class has error code for server response.
 * @author 
 *
 */
public class ServerResponseCode {
	
	public static final int SUCCESS = 100;
	public static final int IN_PROGRESS = 101;
	public static final int ERROR = -10000;
	public static final int REQ_TIME_OUT = ERROR-1;
	public static final int ADD_EVENT_FAIL = ERROR-2;
	public static final int INCORRECT_PARAM = ERROR-3;
	public static final int INVALID_PHONENUM = ERROR-4;
	public static final int NULL_DATA = ERROR-5;
	public static final int NET_NOT_AVAILABLE = ERROR-6;
	public static final int NET_EXCEPION = ERROR-7;
	public static final int INVALID_URL = ERROR-8;
	public static final int FILE_NOT_FOUND = ERROR-9;
	public static final int VSMS_USER_LIMIT_EXCEDED = ERROR-10;
	public static final int VSMS_QUOTA_LIMIT_EXCEDED = ERROR-11;
	
	
	// ERROR CODES OF SERVER
	
	public static final int ALREADY_REGD=103;
	public static final int INVALID_API=104;
	public static final int DB_ERR=105;
	public static  final int DUPLICATE_CMP=106;
	public static final  int INVALID_USER=107;
	
	
}
