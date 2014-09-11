package com.blocker.callandsms.mainapplication;

import java.util.ArrayList;

import com.blocker.callandsms.broadcastreceiver.CallandSmsBroadcastReceiver;
import com.blocker.callandsms.contactrep.Contact;
import com.blocker.callandsms.contentprovider.BlacklistOpenHelper;


import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import static com.blocker.callandsms.contentprovider.BlacklistConstants.*;
import static com.blocker.callandsms.contentprovider.BlacklistConstants.*;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.*;

public class BlacklistApplication extends Application {
	
	ArrayList<Contact> contactList=null;
	ContentResolver resolver=null;

/***********************************************************/
/**callback mehods**/
/**lifecycle methods**/	
	public void onCreate()
	{
		this.innitializeDataMembers();
		this.retrieveDataFromSystemPrefrences();
		this.registerIntentFilterForBatteryState();
		Log.v("array size = ",""+contactList.size());
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		this.saveDataIntoSystemPrefrences();
	}

/***********************************************************/
/**user defined methdos**/
	
	//innitialize datamembers!!
	private void innitializeDataMembers()
	{
		contactList=new ArrayList<Contact>();
		resolver=getContentResolver();
		refreshListFromDatabase();
	}

/**getter and setter**/
	public ArrayList<Contact> getContactListArray()
	{
		return this.contactList;
	}

/**intent filters**/
	private void registerIntentFilterForBatteryState()
	{
		IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		this.registerReceiver(new CallandSmsBroadcastReceiver(),filter);
	}
	
/**content provider related methods**/
	
	//refresh the ArrayList
	private void refreshListFromDatabase()
	{
		contactList.clear();
		String [] projection={COLUMN_1_ID,COLUMN_2_NAME,COLUMN_3_NUMBER,COLUMN_4_FLAG_CALL_BUSY,
				COLUMN_5_FLAG_CALL_BLACKLIST,COLUMN_6_FLAG_CALL_SIGNAL_LOW,COLUMN_7_FLAG_CALL_BATTERY_LOW,
				COLUMN_8_FLAG_SMS_BLACKLIST,COLUMN_9_FLAG_SMS_NOTIFY_ONLY,COLUMN_10_FLAG_SMS_SAVE_ONLY};
		
		Cursor cursor=resolver.query(BLACKLIST_URI, projection,null,null,null);
		
		if(!cursor.isAfterLast())
		{
			cursor.moveToFirst();
			do
			{
				long id=cursor.getLong(cursor.getColumnIndex(COLUMN_1_ID));
				String name=cursor.getString(cursor.getColumnIndex(COLUMN_2_NAME));
				String number=cursor.getString(cursor.getColumnIndex(COLUMN_3_NUMBER));
				boolean callBusyFlag=(cursor.getInt(cursor.getColumnIndex(COLUMN_4_FLAG_CALL_BUSY)))==1?true:false;
				boolean callBlacklistFlag=(cursor.getInt(cursor.getColumnIndex(COLUMN_5_FLAG_CALL_BLACKLIST)))==1?true:false;
				boolean callSignalLowFlag=(cursor.getInt(cursor.getColumnIndex(COLUMN_6_FLAG_CALL_SIGNAL_LOW))==1?true:false);
				boolean callBattLowFlag=(cursor.getInt(cursor.getColumnIndex(COLUMN_7_FLAG_CALL_BATTERY_LOW))==1?true:false);
				boolean smsBlacklistFlag=(cursor.getInt(cursor.getColumnIndex(COLUMN_8_FLAG_SMS_BLACKLIST))==1?true:false);
				boolean smsNotifyOnlyFlag=(cursor.getInt(cursor.getColumnIndex(COLUMN_9_FLAG_SMS_NOTIFY_ONLY))==1?true:false);
				boolean smsSaveOnlyFlag=(cursor.getInt(cursor.getColumnIndex(COLUMN_10_FLAG_SMS_SAVE_ONLY))==1?true:false);
				Contact contact=new Contact(id,name,number,callBusyFlag,callBlacklistFlag,callSignalLowFlag, callBattLowFlag, smsBlacklistFlag, smsNotifyOnlyFlag, smsSaveOnlyFlag);
				contactList.add(contact);
				cursor.moveToNext();
			}
			while(!cursor.isAfterLast());
			cursor.close();
		}
		else
		{
			cursor.close();
			Log.v("database","database cursor is empty in main application refreah list!!");
		}
		
		//iterate the community table and add to array!!!!!!
		String[]projectionComm=new String[]{COLUMN_3_NUMBER_COMMUNITY};
		Cursor cursor2=resolver.query(BLACKLIST_COMMUNITY_URI,projectionComm,null,null,null);
		if(!cursor2.isAfterLast())
		{
			cursor2.moveToFirst();
			do
			{
				String number=cursor2.getString(cursor2.getColumnIndex(COLUMN_3_NUMBER_COMMUNITY));
				cursor2.moveToNext();
				Contact contact=new Contact(10000,"CommunityBlacklisted",number,true,true,true,true,true,false,false);
				contactList.add(contact);
			}while(!cursor2.isAfterLast());
			cursor2.close();
		}
		else
		{
			cursor2.close();
			Log.v("communit database","ommunity database is empty!!!");
		}
	}
	
	//insert contact info into list!!!
	public void insertContactIntoDatabase(Contact contact)
	{
		String number=contact.getContactNumber();
		contact.setContactNumber(this.eliminateDashFromContact(number));
		try
		{
			ContentValues values=new ContentValues();
			values.put(COLUMN_2_NAME,contact.getContactName());
			values.put(COLUMN_3_NUMBER,contact.getContactNumber());
			values.put(COLUMN_4_FLAG_CALL_BUSY,contact.getcallBusyFlag()?1:0);
			values.put(COLUMN_5_FLAG_CALL_BLACKLIST,contact.getCallBlackListFlag()?1:0);
			values.put(COLUMN_6_FLAG_CALL_SIGNAL_LOW,contact.getCallSignalLowFlag()?1:0);
			values.put(COLUMN_7_FLAG_CALL_BATTERY_LOW,contact.getCallBatteryLowFlag()?1:0);
			values.put(COLUMN_8_FLAG_SMS_BLACKLIST,contact.getSmsBlacklistFlag()?1:0);
			values.put(COLUMN_9_FLAG_SMS_NOTIFY_ONLY,contact.getSmsNotifyOnlyFlag()?1:0);
			values.put(COLUMN_10_FLAG_SMS_SAVE_ONLY,contact.getSmsSaveOnlyFlag()?1:0);
			resolver.insert(BLACKLIST_URI, values);
			this.refreshListFromDatabase();
			Log.v("contact inserted","contact inserted!!!");
		}
		catch(Exception ex)
		{
			Log.v("Duplicate number","trying to add duplicate number into personal database!!!");
		}
	}
	
	//insert contact into community blacklist
	public void insertContactIntoCommunityBlacklist(Contact contact)
	{
		try
		{
			ContentValues values=new ContentValues();
			values.put(COLUMN_2_NAME_COMMUNITY,"CommunityBlacklisted");
			values.put(COLUMN_3_NUMBER_COMMUNITY,contact.getContactNumber());
			resolver.insert(BLACKLIST_COMMUNITY_URI,values);
			this.refreshListFromDatabase();
		}
		catch(SQLiteConstraintException ex)
		{
			Log.v("Duplicate number","Trying to add duplicate number to community blacklist!!!");
		}
	}
	
	//update contact info for list!!!
	public void updateContactInfo(Contact contact,int position)
	{
		ContentValues values=new ContentValues();
		values.put(COLUMN_2_NAME,contact.getContactName());
		values.put(COLUMN_3_NUMBER,contact.getContactNumber());
		values.put(COLUMN_4_FLAG_CALL_BUSY,contact.getcallBusyFlag()?1:0);
		values.put(COLUMN_5_FLAG_CALL_BLACKLIST,contact.getCallBlackListFlag()?1:0);
		values.put(COLUMN_6_FLAG_CALL_SIGNAL_LOW,contact.getCallSignalLowFlag()?1:0);
		values.put(COLUMN_7_FLAG_CALL_BATTERY_LOW,contact.getCallBatteryLowFlag()?1:0);
		values.put(COLUMN_8_FLAG_SMS_BLACKLIST,contact.getSmsBlacklistFlag()?1:0);
		values.put(COLUMN_9_FLAG_SMS_NOTIFY_ONLY,contact.getSmsNotifyOnlyFlag()?1:0);
		values.put(COLUMN_10_FLAG_SMS_SAVE_ONLY,contact.getSmsSaveOnlyFlag()?1:0);
		resolver.update(BLACKLIST_URI,values,String.format("%s=%d",COLUMN_1_ID,contact.getContactId()),null);
		this.refreshListFromDatabase();
	}
	
	//delete contact info from list!!!
	public void deleteContactInfo(Contact contact)
	{
		resolver.delete(BLACKLIST_URI,String.format("%s=%d",COLUMN_1_ID,contact.getContactId()),null);
		this.refreshListFromDatabase();
	}
	

/**system prefrences related methods**/
	private void retrieveDataFromSystemPrefrences()
	{
		Log.v("System prefrences : -","retrieving data from system prefrences");
		SharedPreferences prefrences=getSharedPreferences(SHARED_PREFRENCES_FILE,0);
		SharedPreferences.Editor editor=prefrences.edit();
		if(!prefrences.contains(ATTRIBUTE_BUSY_MODE_FLAG))
		{
			BUSY_MODE_FLAG=false;
			editor.putBoolean(ATTRIBUTE_BUSY_MODE_FLAG,false);
		}
		else
		{
			BUSY_MODE_FLAG=prefrences.getBoolean(ATTRIBUTE_BUSY_MODE_FLAG,false);
		}
		if(!prefrences.contains(ATTRIBUTE_SIGNAL_STATE))
		{
			SIGNAL_STATE=20;
			editor.putInt(ATTRIBUTE_SIGNAL_STATE,20);
		}
		else
		{
			SIGNAL_STATE=prefrences.getInt(ATTRIBUTE_SIGNAL_STATE,20);
		}
		if(!prefrences.contains(ATTRIBUTE_BATTERY_STATE))
		{
			BATTERY_STATE=100;
			editor.putInt(ATTRIBUTE_BATTERY_STATE,100);
		}
		else
		{
			BATTERY_STATE=prefrences.getInt(ATTRIBUTE_BATTERY_STATE, 100);
		}
		if(!prefrences.contains(ATTRIBUTE_CALL_COMMUNITY_BLACKLIST))
		{
			CALL_COMMUNITY_BLACKLIST=false;
			editor.putBoolean(ATTRIBUTE_CALL_COMMUNITY_BLACKLIST,false);
		}
		else
		{
			CALL_COMMUNITY_BLACKLIST=prefrences.getBoolean(ATTRIBUTE_CALL_COMMUNITY_BLACKLIST,false);
		}
		if(!prefrences.contains(ATTRIBUTE_SMS_COMMUNITY_BLACKLIST))
		{
			SMS_COMMUNITY_BLACKLIST=false;
			editor.putBoolean(ATTRIBUTE_SMS_COMMUNITY_BLACKLIST,false);
		}
		else
		{
			SMS_COMMUNITY_BLACKLIST=prefrences.getBoolean(ATTRIBUTE_SMS_COMMUNITY_BLACKLIST, false);
		}
		if(!prefrences.contains(ATTRIBUTE_EMAIL_NOTIFICATION))
		{
			EMAIL_NOTIFICATION=false;
			editor.putBoolean(ATTRIBUTE_EMAIL_NOTIFICATION,false);
		}
		else
		{
			EMAIL_NOTIFICATION=prefrences.getBoolean(ATTRIBUTE_EMAIL_NOTIFICATION,false);
		}
		if(!prefrences.contains(ATTRIBUTE_EMAIL_ADDRESS))
		{
			EMAIL_ADDRESS="";
			editor.putString(ATTRIBUTE_EMAIL_ADDRESS,EMAIL_ADDRESS);
		}
		else
		{
			EMAIL_ADDRESS=prefrences.getString(ATTRIBUTE_EMAIL_ADDRESS,EMAIL_ADDRESS);
		}
		editor.commit();
	
		Log.v("busy flay",""+BUSY_MODE_FLAG);
		Log.v("signal state",""+SIGNAL_STATE);
		Log.v("battery state",""+BATTERY_STATE);
		Log.v("call black comm",""+CALL_COMMUNITY_BLACKLIST);
		Log.v("sms black comm",""+SMS_COMMUNITY_BLACKLIST);
		Log.v("email notification",""+EMAIL_NOTIFICATION);
		Log.v("system prefrences reader","retriev from system prefrences com plete!!");
	
	}
	public void saveDataIntoSystemPrefrences()
	{
		Log.v("saving into system prefrences","saving data to system prefrences!!!");
		SharedPreferences prefrences=getSharedPreferences(SHARED_PREFRENCES_FILE,0);
		SharedPreferences.Editor editor=prefrences.edit();
		editor.putBoolean(ATTRIBUTE_BUSY_MODE_FLAG,BUSY_MODE_FLAG);
		editor.putInt(ATTRIBUTE_SIGNAL_STATE,SIGNAL_STATE);
		editor.putInt(ATTRIBUTE_BATTERY_STATE,BATTERY_STATE);
		editor.putBoolean(ATTRIBUTE_CALL_COMMUNITY_BLACKLIST,CALL_COMMUNITY_BLACKLIST);
		editor.putBoolean(ATTRIBUTE_SMS_COMMUNITY_BLACKLIST,SMS_COMMUNITY_BLACKLIST);
		editor.putBoolean(ATTRIBUTE_EMAIL_NOTIFICATION,EMAIL_NOTIFICATION);
		editor.putString(ATTRIBUTE_EMAIL_ADDRESS,EMAIL_ADDRESS);
		editor.commit();
	}
/**tokanize the contact and eliminate --**/
	private String eliminateDashFromContact(String inNumber)
	{
		String outNumber=null;
		char[]arr=inNumber.toCharArray();
		int innitialLength=arr.length; 
		int count=0;
		
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i]=='-')
			{
				for(int j=i;j<(arr.length-1);j++)
				{
					arr[j]=arr[j+1];
				}
				count++;
			}
		}
		outNumber=String.valueOf(arr);
		Log.v("innitialilength = "+innitialLength,"count ="+count);
		outNumber=outNumber.substring(0,innitialLength-count);
		return outNumber;
	}

}
