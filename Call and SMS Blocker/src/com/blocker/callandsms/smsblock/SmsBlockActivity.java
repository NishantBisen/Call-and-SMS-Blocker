package com.blocker.callandsms.smsblock;

import java.util.ArrayList;

import com.blocker.callandsms.about.CallBlockMoreInfoActivity;
import com.blocker.callandsms.about.SmsBlockMoreInfoActivity;
import com.blocker.callandsms.callcommunityblacklistfeaturescreen.CallCommynityBlacklistFeatureActivity;
import com.blocker.callandsms.contactrep.Contact;
import com.blocker.callandsms.mainapplication.BlacklistApplication;
import com.blocker.callandsms.startscreen.R;
import com.blocker.smscommunityBlacklistFeatureActivity.SmsCommunityBlacklistFeatureActivity;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class SmsBlockActivity extends Activity implements OnClickListener{
	
	//datamembers!!
	BlacklistApplication app=null;
	ArrayList<Contact> contactList=null;
	Contact contact=null;
	int index;
	
	//ui components!!!
	TextView tName;
	TextView tNumber;
	
	CheckBox cBlacklist;
	RadioButton cNotify;
	RadioButton cDoNotNotify;
	
	Button bMoreInfo;
	Button bCommunity;
	Button bSave;
	Button bCancel;
	Button bClear;

/**************************************************************/
	//callbackmethods!!!
/**lifecycle methods**/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.smsblock);
		
		this.innitializeDatamembers();
		this.initializeUIComponents();
		this.setContentsUIComponents();
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
			this.saveSMSBlockOptions();
		}
		else if(bCancel==v){
			finish();
		}
		else if(bClear==v){
			clearRadioButtons();
		}
	}


/**********************************************************************/
	//userdefined methods!!!
	private void initializeUIComponents() {
		
		tName=(TextView)findViewById(R.id.smsblock_tName);
		tNumber=(TextView)findViewById(R.id.smsblock_tNumber);
		
		cDoNotNotify=(RadioButton)findViewById(R.id.smsblock_cDonotNotify);
		cBlacklist=(CheckBox)findViewById(R.id.smsblock_cBlacklist);
		cNotify=(RadioButton)findViewById(R.id.smsblock_cNotify);
		
		bMoreInfo=(Button)findViewById(R.id.smsblock_bMoreinfo);
		bMoreInfo.setOnClickListener(this);
		bCommunity=(Button)findViewById(R.id.smsblock_bCommunityBlackList);
		bCommunity.setOnClickListener(this);
		bSave=(Button)findViewById(R.id.smsblock_bSave);
		bSave.setOnClickListener(this);
		bCancel=(Button)findViewById(R.id.smsblock_bCancel);
		bCancel.setOnClickListener(this);
		
		bClear=(Button)findViewById(R.id.smsblock_bClear);
		bClear.setOnClickListener(this);
	}
	private void innitializeDatamembers()
	{
		Intent intent=getIntent();
		this.index=intent.getIntExtra("index",-1);
		
		app=(BlacklistApplication)getApplication();
		contactList=app.getContactListArray();
		contact=contactList.get(index);
	}
	private void setContentsUIComponents()
	{
		tName.setText(contact.getContactName());
		tNumber.setText(contact.getContactNumber());
		cDoNotNotify.setChecked(contact.getSmsSaveOnlyFlag());
		cBlacklist.setChecked(contact.getSmsBlacklistFlag());
		cNotify.setChecked(contact.getSmsNotifyOnlyFlag());
	}
	
/**handler/listener executor**/
	private void saveSMSBlockOptions()
	{
		contact.setSmsSaveOnlyFlag(cDoNotNotify.isChecked());
		contact.setSmsBlacklistFlag(cBlacklist.isChecked());
		contact.setSmsNotifyOnlyFlag(cNotify.isChecked());
		app.updateContactInfo(contact,index);
		finish();
	}
	
	private void goToMoreInfo(){
		Intent intent=new Intent(this,SmsBlockMoreInfoActivity.class);
		this.startActivity(intent);
	}
	
	private void navigateToCommunityBlacklist()
	{
		Intent intent=new Intent(this,SmsCommunityBlacklistFeatureActivity.class);
		intent.putExtra("index",index);
		this.startActivity(intent);
	}
	
	private void clearRadioButtons() {
		cNotify.setChecked(false);
		cDoNotNotify.setChecked(false);
	}
}
