package com.blocker.callandsms.contentprovider;

import android.net.Uri;

public class BlacklistConstants {

	//database related constants!!!
	public static final String BLACKLIST_DB_NAME="BlacklistDatabase";
	public static final int BLACKLIST_VERSION=1;
	
	//table 1
	public static final String TABLE_NAME="BlacklistContactsList";
	public static final String COLUMN_1_ID="ContactId";
	public static final String COLUMN_2_NAME="ContactName";
	public static final String COLUMN_3_NUMBER="ConatctNumber";
	public static final String COLUMN_4_FLAG_CALL_BUSY="CallBusyFlag";
	public static final String COLUMN_5_FLAG_CALL_BLACKLIST="CallBlacklilstFlag";
	public static final String COLUMN_6_FLAG_CALL_SIGNAL_LOW="CallSignalLowFlag";
	public static final String COLUMN_7_FLAG_CALL_BATTERY_LOW="CallBatteryLowFlag";
	public static final String COLUMN_8_FLAG_SMS_BLACKLIST="SmsBlacklilstFlag";
	public static final String COLUMN_9_FLAG_SMS_NOTIFY_ONLY="SmsNotifyOnlyFlag";
	public static final String COLUMN_10_FLAG_SMS_SAVE_ONLY="SmsSaveOnlyFlag";
	
	//table2
	public static final String TABLE_COMMUNITY_BLACKLIST_NAME="CommunityBlacklistContactList";
	public static final String COLUMN_1_ID_COMMUNITY="CommunityContactID";
	public static final String COLUMN_2_NAME_COMMUNITY="CommunityContactName";
	public static final String COLUMN_3_NUMBER_COMMUNITY="CommunityContactNumber";
	
	//uri constants!!!
	public static final String AUTHORITY="com.blocker.callandsms.contentprovider";
	public static final String MIME_CONTACTS="blacklistcontactlist";
	public static final String MIME_COMMUNITY_CONTACTS="communityblacklistcontacts";
	public static final Uri BLACKLIST_URI=Uri.parse("content://"+AUTHORITY+"/"+MIME_CONTACTS);
	public static final Uri BLACKLIST_COMMUNITY_URI=Uri.parse("content://"+AUTHORITY+"/"+MIME_COMMUNITY_CONTACTS);
}
