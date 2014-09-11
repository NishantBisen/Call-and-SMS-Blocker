package com.blocker.callandsms.broadcastreceiver;

import java.lang.reflect.Method;
import java.util.ArrayList;


import com.android.internal.telephony.ITelephony;
import com.blocker.callandsms.contactrep.Contact;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.BatteryManager;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.*;
import static com.blocker.callandsms.contentprovider.BlacklistConstants.*;

public class CallandSmsBroadcastReceiver extends BroadcastReceiver {

	//levels at which call should be blocked!!!
	private static final int CRITICAL_BATTERY_LEVEL=20;
	private static final int CRITICAL_SIGNAL_LEVEL=5;
	
	//datamembers!!!
	ArrayList<Contact> contactList=null;
	
	//datamembers used for blocking the call!!!
	TelephonyManager manager=null;
	ITelephony telephonyService=null;
	
	//system prefrences datamembers!!
	SharedPreferences prefrences=null;
	boolean busyFlag;
	int signalState;
	int batteryState;
	boolean callCommunityBlacklistFeatureState;
	boolean smsCommunityBlacklistFeature;
	
/*****************************************************************************************************************/
//callbacks!!!!
	
/**lifecycle methods!!!!**/
	@Override
	public void onReceive(final Context context,Intent intent) {
		this.innitializeDatamembers(context);
		this.retieveBatteryStateAndSaveToSystemprefrences(intent,context);
		this.innitializeDatamembersForBlocking(manager);
		manager.listen(new PhoneStateListener(){
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				CallandSmsBroadcastReceiver.this.retrieveSystemPrefrences(context);
				CallandSmsBroadcastReceiver.this.retrieveContactListFromTheDatabase(context);
				for(int i=0;i<contactList.size();i++)
				{
					CallandSmsBroadcastReceiver.this.blockCallIfBatteryBelowCriticalLevel(contactList.get(i), incomingNumber);
					CallandSmsBroadcastReceiver.this.blockCallIfSignalBelowCriticalLevel(contactList.get(i),incomingNumber);
					CallandSmsBroadcastReceiver.this.blockCallIfNumberBlacklisted(contactList.get(i),incomingNumber);
					CallandSmsBroadcastReceiver.this.blockCallIfPhoneInBusyMode(contactList.get(i),incomingNumber);
				}
				CallandSmsBroadcastReceiver.this.saveSystemPrefrences(context);
			}
			
			
			@Override
			public void onSignalStrengthsChanged(SignalStrength signalStrength) {
				
				CallandSmsBroadcastReceiver.this.retrieveSignalStrengthAndSaveToSystemPrefrences(signalStrength,context);
			}	
		},PhoneStateListener.LISTEN_CALL_STATE|PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		
		
	}
	
/***********************************************************************************************/
/*******************************User defined methods*******************************************/
/***********************************************************************************************/

	
	
/**************************************************************************************************/
/**callblock features**/
	
	private void blockCallIfBatteryBelowCriticalLevel(Contact contact,String number)
	{
		if( (number.endsWith(contact.getContactNumber())) && contact.getCallBatteryLowFlag()==true && batteryState<CRITICAL_BATTERY_LEVEL)
		{
			this.blockIncomingCall();
			Log.v("blocking call--","block call due to battery state constraint");
		}
	
	}
	private void blockCallIfSignalBelowCriticalLevel(Contact contact,String number)
	{
		if( (number.endsWith(contact.getContactNumber())) && contact.getCallSignalLowFlag() && signalState<CRITICAL_SIGNAL_LEVEL)
		{
			this.blockIncomingCall();
		}
	}
	private void blockCallIfNumberBlacklisted(Contact contact,String number)
	{
		if( (number.endsWith(contact.getContactNumber())) && contact.getCallBlackListFlag())
		{
			if(contact.getContactName().equals("CommunityBlacklisted") && callCommunityBlacklistFeatureState==false)
			{}
			else
			{
				this.blockIncomingCall();
			}
			
		}
	}
	private void blockCallIfPhoneInBusyMode(Contact contact,String number)
	{
		if((number.endsWith(contact.getContactNumber())) && contact.getcallBusyFlag() && busyFlag)
		{
			this.blockIncomingCall();
		}
	}
	
//block the call!!!
	private void blockIncomingCall()
	{
		try
		{
			telephonyService.endCall();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void innitializeDatamembersForBlocking(TelephonyManager manager)
	{
		try
		{
			Class c=Class.forName(TelephonyManager.class.getName());
			Method m=c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			telephonyService=(ITelephony)m.invoke(manager);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
/************************************************************************************************/
//innitialize the datamembers!!!
	private void innitializeDatamembers(Context context)
	{
		contactList=new ArrayList<Contact>();
		manager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	}
//system prefrences related methods!!!!
	private void retrieveSystemPrefrences(Context context)
	{
		prefrences=context.getSharedPreferences(SHARED_PREFRENCES_FILE,0);
		busyFlag=prefrences.getBoolean(ATTRIBUTE_BUSY_MODE_FLAG,false);
		signalState=prefrences.getInt(ATTRIBUTE_SIGNAL_STATE,-1);
		batteryState=prefrences.getInt(ATTRIBUTE_BATTERY_STATE, -1);
		callCommunityBlacklistFeatureState=prefrences.getBoolean(ATTRIBUTE_CALL_COMMUNITY_BLACKLIST,false);
		smsCommunityBlacklistFeature=prefrences.getBoolean(ATTRIBUTE_SMS_COMMUNITY_BLACKLIST,false);
		/*Log.v("prefrences : -","prefrences : -");
		Log.v("busy flag = ",""+busyFlag);
		Log.v("signalState = ",""+signalState);
		Log.v("batteryState = ",""+batteryState);
		Log.v("callCommunity blacklist",""+callCommunityBlacklistFeatureState);
		Log.v("sms community blacklist",""+smsCommunityBlacklistFeature);*/
	
	}
	private void saveSystemPrefrences(Context context)
	{
		SharedPreferences prefrences=context.getSharedPreferences(SHARED_PREFRENCES_FILE,0);
		SharedPreferences.Editor editor=prefrences.edit();
		//editor.putBoolean(ATTRIBUTE_BUSY_MODE_FLAG,busyFlag);
		editor.putInt(ATTRIBUTE_SIGNAL_STATE,signalState);
		editor.putInt(ATTRIBUTE_BATTERY_STATE,batteryState);
		//editor.putBoolean(ATTRIBUTE_CALL_COMMUNITY_BLACKLIST,callCommunityBlacklistFeatureState);
		//editor.putBoolean(ATTRIBUTE_SMS_COMMUNITY_BLACKLIST,smsCommunityBlacklistFeature);
		editor.commit();
	}
	
/**************************************************************************************************/
	//retrieve states of battery or signal!!!
	private void retieveBatteryStateAndSaveToSystemprefrences(Intent intent,Context context)
	{
		Log.v("in battery state change!!!","in battery state!!!");
		if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED))
		{
			Log.v("changing battery state!!","changing battery state!!");
			batteryState=intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			this.saveSystemPrefrences(context);
		}
	}
	private void retrieveSignalStrengthAndSaveToSystemPrefrences(SignalStrength signalStrength,Context context)
	{
		signalState=signalStrength.getGsmSignalStrength();
		this.saveSystemPrefrences(context);
	}
	
	//content provider related codes!!!
	private void retrieveContactListFromTheDatabase(Context context)
	{
		contactList.clear();
		
		//firing query to first table!!!
		
		ContentResolver resolver=context.getContentResolver();
		String [] projection={COLUMN_1_ID,COLUMN_2_NAME,COLUMN_3_NUMBER,COLUMN_4_FLAG_CALL_BUSY,
				COLUMN_5_FLAG_CALL_BLACKLIST,COLUMN_6_FLAG_CALL_SIGNAL_LOW,COLUMN_7_FLAG_CALL_BATTERY_LOW,
				COLUMN_8_FLAG_SMS_BLACKLIST,COLUMN_9_FLAG_SMS_NOTIFY_ONLY,COLUMN_10_FLAG_SMS_SAVE_ONLY};
		Cursor cursor=resolver.query(BLACKLIST_URI, projection,null,null,null);
		if(cursor!=null)
		{
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
				}while(!cursor.isAfterLast());
				cursor.close();
			}
			else
			{
				cursor.close();
				Log.v("local database cursor","local database cursor is empty!!!");
			}
		}
		else
		{
			Log.v("localdatabase","localdatabase cursor is null");
		}
		
		//firing query to second database!!!
		String[]projectionComm=new String[]{COLUMN_3_NUMBER_COMMUNITY};
		Cursor cursor2=resolver.query(BLACKLIST_COMMUNITY_URI,projectionComm,null,null,null);
		if(cursor2!=null)
		{
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
		else
		{
			Log.v("community database cursor is null","community database cursor is null");
		}
	}
}
