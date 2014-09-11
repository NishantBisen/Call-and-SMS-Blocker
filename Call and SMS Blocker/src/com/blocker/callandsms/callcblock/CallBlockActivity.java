package com.blocker.callandsms.callcblock;

import java.util.ArrayList;

import com.blocker.callandsms.about.CallBlockMoreInfoActivity;
import com.blocker.callandsms.callcommunityblacklistfeaturescreen.CallCommynityBlacklistFeatureActivity;
import com.blocker.callandsms.contactrep.Contact;
import com.blocker.callandsms.mainapplication.BlacklistApplication;
import com.blocker.callandsms.startscreen.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class CallBlockActivity extends Activity implements OnClickListener {
	
	//datamembers!!!
	BlacklistApplication app=null;
	int index=0;
	ArrayList<Contact> contactList=null;
	Contact contact=null;
	
	//ui components!!!
	TextView tName;
	TextView tNumber;
	
	CheckBox cBusy;
	CheckBox cBlacklist;
	CheckBox cSignallow;
	CheckBox cBatterylow;
	
	Button bMoreInfo;
	Button bCommunity;
	Button bSave;
	Button bCancel;

/**************************************************************************/
//callbacks!!!
	
/**lifecycle methods**/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.callblock);
		
		this.innitializeDatamembers();
		this.initializeUIComponents();
		this.setContentsOfUIComponents();
	}
	
/**handlers/listeners**/
	@Override
	public void onClick(View v) {
		if(bMoreInfo==v){
			this.goToMoreInfo();
		}
		else if(bCommunity==v){
			this.navigateToCommunityBlacklist();
		}
		
		else if(bSave==v){
			this.saveBlockOptionsForNumber();
		}
		else if(bCancel==v){
			finish();
		}	
	}
/******************************************************************************/
	//userdefined methdos!!!
	
/**innitializers**/
	private void initializeUIComponents() {
		
		tName=(TextView)findViewById(R.id.callblock_tName);
		tNumber=(TextView)findViewById(R.id.callblock_tNumber);
		
		cBusy=(CheckBox)findViewById(R.id.callblock_cBusy);
		cBlacklist=(CheckBox)findViewById(R.id.callblock_cBlacklist);
		cSignallow=(CheckBox)findViewById(R.id.callblock_cSignallow);
		cBatterylow=(CheckBox)findViewById(R.id.callblock_cBatterylow);
		
		bMoreInfo=(Button)findViewById(R.id.callblock_bMoreinfo);
		bMoreInfo.setOnClickListener(this);
		bCommunity=(Button)findViewById(R.id.callblock_bCommunityBlackList);
		bCommunity.setOnClickListener(this);
		bSave=(Button)findViewById(R.id.callblock_bSave);
		bSave.setOnClickListener(this);
		bCancel=(Button)findViewById(R.id.callblock_bCancel);
		bCancel.setOnClickListener(this);
	}
	private void innitializeDatamembers()
	{
		Intent intent=getIntent();
		this.index=intent.getIntExtra("index",-1);
		
		app=(BlacklistApplication)this.getApplication();
		contactList=app.getContactListArray();
		
		contact=contactList.get(index);
	}
	private void setContentsOfUIComponents()
	{
		tName.setText(contact.getContactName());
		tNumber.setText(contact.getContactNumber());
		cBusy.setChecked(contact.getcallBusyFlag());
		cBlacklist.setChecked(contact.getCallBlackListFlag());
		cSignallow.setChecked(contact.getCallSignalLowFlag());
		cBatterylow.setChecked(contact.getCallBatteryLowFlag());
	}
/**handler/listener executors!!!**/
	private void saveBlockOptionsForNumber()
	{
		//Log.v("update info -","batt flag -"+cBatterylow.isChecked());
		contact.setcallBusyFlag(cBusy.isChecked());
		contact.setCallBlacklistFlag(cBlacklist.isChecked());
		contact.setCallSignalLowFlag(cSignallow.isChecked());
		contact.setCallBatteryLowFlag(cBatterylow.isChecked());
		app.updateContactInfo(contact,index);
		finish();
	}
	
	private void goToMoreInfo(){
		Intent intent=new Intent(this,CallBlockMoreInfoActivity.class);
		this.startActivity(intent);
	}
	
	private void navigateToCommunityBlacklist()
	{
		Intent intent=new Intent(this,CallCommynityBlacklistFeatureActivity.class);
		intent.putExtra("index",index);
		this.startActivity(intent);
	}
}
