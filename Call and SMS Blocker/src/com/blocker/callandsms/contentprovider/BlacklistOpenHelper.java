package com.blocker.callandsms.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import static com.blocker.callandsms.contentprovider.BlacklistConstants.*;

public class BlacklistOpenHelper extends SQLiteOpenHelper {
/******************************************************************/
//callbacks
	
/**constructor**/
	public BlacklistOpenHelper(Context context) {
		super(context,BLACKLIST_DB_NAME,null,BLACKLIST_VERSION);	
	}

/**lifecycle methods**/
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "+TABLE_NAME+"("+COLUMN_1_ID+" integer primary key autoincrement not null,"+COLUMN_2_NAME+" text,"+COLUMN_3_NUMBER+" text unique,"
					+COLUMN_4_FLAG_CALL_BUSY+" integer,"+COLUMN_5_FLAG_CALL_BLACKLIST+" integer,"+COLUMN_6_FLAG_CALL_SIGNAL_LOW+" integer,"
					+COLUMN_7_FLAG_CALL_BATTERY_LOW+" integer,"+COLUMN_8_FLAG_SMS_BLACKLIST+" integer,"+COLUMN_9_FLAG_SMS_NOTIFY_ONLY+" integer,"
					+COLUMN_10_FLAG_SMS_SAVE_ONLY+" integer)");
		db.execSQL("create table "+TABLE_COMMUNITY_BLACKLIST_NAME+"("+COLUMN_1_ID_COMMUNITY+" integer primary key autoincrement not null,"+COLUMN_2_NAME_COMMUNITY+" text,"+COLUMN_3_NUMBER_COMMUNITY+" text unique)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
