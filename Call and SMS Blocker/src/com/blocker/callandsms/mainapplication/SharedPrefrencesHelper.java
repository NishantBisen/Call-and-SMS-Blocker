package com.blocker.callandsms.mainapplication;

import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.ATTRIBUTE_BATTERY_STATE;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.ATTRIBUTE_BUSY_MODE_FLAG;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.ATTRIBUTE_CALL_COMMUNITY_BLACKLIST;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.ATTRIBUTE_SIGNAL_STATE;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.ATTRIBUTE_SMS_COMMUNITY_BLACKLIST;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.BATTERY_STATE;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.BUSY_MODE_FLAG;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.CALL_COMMUNITY_BLACKLIST;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.SHARED_PREFRENCES_FILE;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.SIGNAL_STATE;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.SMS_COMMUNITY_BLACKLIST;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefrencesHelper {
	
	//variables to be used!!
	public static boolean BUSY_MODE_FLAG=false;
	public static int SIGNAL_STATE=0;
	public static int BATTERY_STATE=0;
	public static boolean CALL_COMMUNITY_BLACKLIST=false;
	public static boolean SMS_COMMUNITY_BLACKLIST=false;
	public static boolean EMAIL_NOTIFICATION=false;
	public static String EMAIL_ADDRESS="";
	
	//shared prefrences file and attributes!!
	public static final String SHARED_PREFRENCES_FILE="SavedStatesFile";
	public static final String ATTRIBUTE_BUSY_MODE_FLAG="BusyMode";
	public static final String ATTRIBUTE_SIGNAL_STATE="SignalState";
	public static final String ATTRIBUTE_BATTERY_STATE="BatteryState";
	public static final String ATTRIBUTE_CALL_COMMUNITY_BLACKLIST="CallCommunityBlacklistFeature";
	public static final String ATTRIBUTE_SMS_COMMUNITY_BLACKLIST="SMSCommunityBlacklistFeature";
	public static final String ATTRIBUTE_EMAIL_NOTIFICATION="EmailNotification";
	public static final String ATTRIBUTE_EMAIL_ADDRESS="EmailAddress";
}
