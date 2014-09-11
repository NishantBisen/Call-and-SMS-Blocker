package com.android.callandsms.broadcastreceiverforsmsblock;

import static com.blocker.callandsms.contentprovider.BlacklistConstants.*;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.*;

import java.util.ArrayList;

import com.blocker.callandsms.contactrep.Contact;
import com.blocker.callandsms.mainapplication.BlacklistApplication;
import com.blocker.callandsms.startscreen.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.telephony.SmsMessage;
import android.util.Log;

public class SmsBlockerBroadcastReceiver extends BroadcastReceiver {

	public static final int NOTIFY_FOR_SMS_RECEIVED=1;
	public static final int NOTIFY_FOR_SMS_RECEIVED_BUT_DELETED=2;
	
	//datamembers
	ArrayList<Contact> contactList=null;
	ContentResolver resolver=null;
	SharedPreferences prefrences=null;
	boolean smsCommunityBlacklistFeature=false;
	//boolean emailFeature=false;
	//String emailAddress;
	String incommingAddress=null;
	String incommingMessage=null;
	NotificationManager nManager=null;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("sms received!!!","sms received in sms broadcast receiver!!!!");
		this.innitializeDatamembers(context);
		this.retrieveSystemPrefrences(context);
		incommingAddress=this.getIncomingSMSNumberAndSetMessage(intent);
		this.retrieveContactListFromTheDatabase(context);
		boolean notificationFlag=false;
		
		//BLOCK SMS methods!!!!!!
		notificationFlag=checkIfNumberPresentInList();
		//if present then search and execute!!
		if(notificationFlag)
		{
			for(int i=0;i<contactList.size();i++)
			{
				blockSMSIfBlacklisted(contactList.get(i),context);
				blockSMSButSendNotification(contactList.get(i),context);
				//no code needed for save but donot notify!!
			}
		}
		//if not present in list then just notify for the message receieved!!
		else
		{
			//this.sendEmailToSpecifiedAccount(context);
			Log.v("in notify if not present in list!!!","in notify if not present in list!!");
			this.sendNotification(NOTIFY_FOR_SMS_RECEIVED,context);
		}
		/*******************************testing********************************/
		Log.v("executing delete!!!","executing delete query!!!");	
	}
/******************************************************************************************/
	//userdefined methods!!!
	private void innitializeDatamembers(Context context)
	{
		resolver=context.getContentResolver();
		contactList=new ArrayList();
		nManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
/**check if message is to be notified, ie number is not present in the list!!**/
	private boolean checkIfNumberPresentInList()
	{
		boolean flag=false;
		//check if number is present in the list!!!
		for(int i=0;i<contactList.size();i++)
		{
			if(incommingAddress.endsWith(contactList.get(i).getContactNumber()) && ((contactList.get(i).getSmsBlacklistFlag())||(contactList.get(i).getSmsNotifyOnlyFlag())||contactList.get(i).getSmsSaveOnlyFlag()))
			{
				flag=true;
			}
		}
		return flag;
	}
/**get sms incoming number**/
	public String getIncomingSMSNumberAndSetMessage(Intent intent)
	{
		SmsMessage message=null;
		String addr=null;
		Bundle extras=intent.getExtras();
		Object[]pdus=(Object[]) extras.get("pdus");
		
		for(Object pdu:pdus)
		{
			message=SmsMessage.createFromPdu((byte[]) pdu);
			addr=message.getOriginatingAddress();
			incommingMessage=message.getMessageBody();
			Log.v("Address of message - ",""+message.getOriginatingAddress());
		}
		return addr;
	}
/**send notification function**/
 	private void sendNotification(int id,Context context)
 	{
 		switch(id)
 		{
 		case NOTIFY_FOR_SMS_RECEIVED:
 			Notification notification=new Notification(R.drawable.blacklist,"SMS received from number : "+incommingAddress,System.currentTimeMillis());
 			Intent intent=new Intent(Intent.ACTION_CALL_BUTTON);
 			PendingIntent pIntent=PendingIntent.getActivity(context,0, intent,0);
 			notification.setLatestEventInfo(context,"SMS RECEIVED : "+incommingAddress,"SMS RECEIVED!!!",pIntent);
 			nManager.notify(NOTIFY_FOR_SMS_RECEIVED,notification);
 			break;
 			
 		case NOTIFY_FOR_SMS_RECEIVED_BUT_DELETED:
 			Notification notification1=new Notification(R.drawable.blacklist,"SMS received from number : "+incommingAddress,System.currentTimeMillis());
 			Intent intent1=new Intent(Intent.ACTION_VIEW);
 			PendingIntent pIntent1=PendingIntent.getActivity(context,0, intent1,0);
 			notification1.setLatestEventInfo(context,"SMS RECEIVED FROM : "+incommingAddress,"SMS WAS RECEIVED BUT DELETED!!!",pIntent1);
 			nManager.notify(NOTIFY_FOR_SMS_RECEIVED,notification1);
 			break;
 		}
 	}
/***********************************delete perticular sms************************/
	
	//delete sms if blacklisted and donot notify!!!!!!
	private void blockSMSIfBlacklisted(Contact contact,Context context)
	{
		Log.v("in block if blacklisted","in block if blacklisted!!!");
		if(incommingAddress.equals(contact.getContactNumber()) && contact.getSmsBlacklistFlag())
		{
			if(contact.getContactName().equals("CommunityBlacklisted") && smsCommunityBlacklistFeature==false)
			{
				//do nothing for community blacklisted numbers if feature is not on!!
				//this.sendEmailToSpecifiedAccount(context);
				Log.v("in comm blacklist feature","in com blacklist feature....do not delete");
				sendNotification(NOTIFY_FOR_SMS_RECEIVED, context);
			}
			else
			{
				//delete blacklisted numbers and donot send notification!!
				Log.v("deleting blacklisted numbers","deleting blacklisted numbers!!!");
				deleteSMSFromInboxWithAddress(incommingAddress);
			}
		}
	}
	//delete sms but notify!!!
	private void blockSMSButSendNotification(Contact contact,Context context)
	{
		if(incommingAddress.endsWith(contact.getContactNumber()) && contact.getSmsNotifyOnlyFlag())
		{
			Log.v("deleting sms from inbox!!","deleting sms from inbox due to send not const!!");
			this.deleteSMSFromInboxWithAddress(incommingAddress);
			sendNotification(NOTIFY_FOR_SMS_RECEIVED_BUT_DELETED,context);
		}
	}
 	
	//delete the sms from inbox!!!!
	private void deleteSMSFromInboxWithAddress(final String address)
	{
		final Thread th=new Thread(new Runnable(){
			public void run()
			{
				try
				{
					Thread.sleep(2000);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				resolver.delete(Uri.parse("content://sms"),"address=?",new String[]{address});
			}
		});
		th.start();
	}
/*****************************************************************************************/
	/**send email to account!!!**/
	/*private void sendEmailToSpecifiedAccount(Context context)
	{
		Intent intent=new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL,new String[]{EMAIL_ADDRESS});
		intent.putExtra(Intent.EXTRA_SUBJECT,"SMS RECEIVED ON YOUR PHONE!!!");
		intent.putExtra(Intent.EXTRA_TEXT,incommingMessage);
		context.startActivity(intent);
	}*/
	 
	 
/*************************************************************************************/
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
	private void retrieveSystemPrefrences(Context context)
	{
		prefrences=context.getSharedPreferences(SHARED_PREFRENCES_FILE,0);
		//busyFlag=prefrences.getBoolean(ATTRIBUTE_BUSY_MODE_FLAG,false);
		//signalState=prefrences.getInt(ATTRIBUTE_SIGNAL_STATE,-1);
		//batteryState=prefrences.getInt(ATTRIBUTE_BATTERY_STATE, -1);
		//callCommunityBlacklistFeatureState=prefrences.getBoolean(ATTRIBUTE_CALL_COMMUNITY_BLACKLIST,false);
		smsCommunityBlacklistFeature=prefrences.getBoolean(ATTRIBUTE_SMS_COMMUNITY_BLACKLIST,false);
		//emailFeature=prefrences.getBoolean(ATTRIBUTE_EMAIL_NOTIFICATION,false);
		//emailAddress=prefrences.getString(ATTRIBUTE_EMAIL_ADDRESS,"default");
		/*Log.v("prefrences : -","prefrences : -");
		Log.v("busy flag = ",""+busyFlag);
		Log.v("signalState = ",""+signalState);
		Log.v("batteryState = ",""+batteryState);
		Log.v("callCommunity blacklist",""+callCommunityBlacklistFeatureState);
		Log.v("sms community blacklist",""+smsCommunityBlacklistFeature);*/
	
	}
}