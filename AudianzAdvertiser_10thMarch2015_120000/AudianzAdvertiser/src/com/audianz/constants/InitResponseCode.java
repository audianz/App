package com.audianz.constants;

public class InitResponseCode {
	
	public static final int INIT_SUCCESS = 1;
	public static final int INIT_FAILURE = -100;
	public static final int MSGLOOP_INITFAIL = INIT_FAILURE-1;
	public static final int NETWORK_INIT_FAIL = INIT_FAILURE-2;
	public static final int MODEL_INIT_FAIL = INIT_FAILURE-3;
	public static final int UTILITY_INIT_FAIL = INIT_FAILURE-4;
	public static final int APP_CONTEXT_NULL = INIT_FAILURE-5;
	public static final int CONFIG_INIT_FAIL = INIT_FAILURE-6;
	public static final int DB_INITFAIL = INIT_FAILURE-7;
	
}
