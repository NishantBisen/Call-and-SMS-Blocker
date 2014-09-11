package com.blocker.callandsms.contactlisthome;


import com.blocker.callandsms.addcontactoptionsscreen.AddContactOptionsScreenActivity;
import com.blocker.callandsms.editblockoption.EditBlockOptionsActivity;
import com.blocker.callandsms.mainapplication.BlacklistApplication;
import com.blocker.callandsms.startscreen.R;
import com.blocker.callandsms.startscreen.StartScreenActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.*;
 
public class ContactListHomeActivity extends ListActivity implements OnClickListener,OnItemClickListener{
	
	//datamembers!!
	ContactListHomeAdapter adapter=null;
	BlacklistApplication app=null;
	
	//ui components!!
	Button bAddNumber;
	Button bToggleCallCommunity;
	Button bToggleSMSCommunity;
	Button bToggleBusyState;
	Button bCancel;
	ListView list;
	
/************************************************************************/
	//callbacks
/**lifecycle methods**/
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactlisthome);
		this.innitializeDatamembers();
		this.innitializeUIComponents();
	}
	public void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	
/**handlers/listeners**/
	@Override
	public void onClick(View v) {
		if(bAddNumber==v)
		{
			this.moveToAddContactOptionsScreen();
		}
		if(bToggleCallCommunity==v)
		{
			this.callToggleState();
		}
		if(bToggleSMSCommunity==v)
		{
			this.smsToggleState();
		}
		if(bToggleBusyState==v)
		{
			this.busyToggleState();
		}
		if(bCancel==v)
		{
			this.moveBackToHomeScreen();
		}
		app.saveDataIntoSystemPrefrences();
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent=new Intent(this,EditBlockOptionsActivity.class);
		intent.putExtra("position",arg2);
		this.startActivity(intent);
	}
/**************************************************************************/
	//user defined methdos
/**innitializers!!**/
	private void innitializeUIComponents()
	{
		bAddNumber=(Button)findViewById(R.id.contactlisthome_bAddNumber);
		bAddNumber.setOnClickListener(this);
		bToggleCallCommunity=(Button)findViewById(R.id.contactlisthome_bCallCommFeatureToggle);
		bToggleCallCommunity.setOnClickListener(this);
		bToggleSMSCommunity=(Button)findViewById(R.id.contactlisthome_bSMSCommFeatureToggle);
		bToggleSMSCommunity.setOnClickListener(this);
		bToggleBusyState=(Button)findViewById(R.id.contactlisthome_bBusyStateToggle);
		bToggleBusyState.setOnClickListener(this);
		bCancel=(Button)findViewById(R.id.contactlisthome_bCancel);
		bCancel.setOnClickListener(this);
		
		BlacklistApplication app=(BlacklistApplication)getApplication();
		adapter=new ContactListHomeAdapter(this,app.getContactListArray());
		this.setListAdapter(adapter);
		list=getListView();
		list.setOnItemClickListener(this);
	}
	private void innitializeDatamembers()
	{
		app=(BlacklistApplication)getApplication();
	}

/**userdefined handlers**/
	private void moveBackToHomeScreen()
	{
		Intent intent=new Intent(this,StartScreenActivity.class);
		this.startActivity(intent);
	}
	
	private void moveToAddContactOptionsScreen()
	{
		Intent intent=new Intent(this,AddContactOptionsScreenActivity.class);
		this.startActivity(intent);
	}
	private void callToggleState()
	{	
		if(CALL_COMMUNITY_BLACKLIST==true)
		{
			CALL_COMMUNITY_BLACKLIST=false;
			Toast.makeText(this,"CALL COMMUNITY BLACKLIST FEATURE TURNED OFF",Toast.LENGTH_LONG).show();
		}
		else
		{
			CALL_COMMUNITY_BLACKLIST=true;
			Toast.makeText(this,"CALL COMMUNITY BLACKLIST FEATURE TURNED ON",Toast.LENGTH_LONG).show();
		}
	}
	private void smsToggleState() 
	{
		if(SMS_COMMUNITY_BLACKLIST==true)
		{
			SMS_COMMUNITY_BLACKLIST=false;
			Toast.makeText(this,"SMS COMMUNITY BLACKLIST FEATURE TURNED OFF",Toast.LENGTH_LONG).show();
		}
		else
		{
			SMS_COMMUNITY_BLACKLIST=true;
			Toast.makeText(this,"SMS COMMUNITY BLACKLIST FEATURE TURNED ON",Toast.LENGTH_LONG).show();
		}
	}
	private void busyToggleState()
	{
		if(BUSY_MODE_FLAG==true)
		{
			BUSY_MODE_FLAG=false;
			Toast.makeText(this,"BUSY MODE TURNED OFF",Toast.LENGTH_LONG).show();
		}
		else
		{
			BUSY_MODE_FLAG=true;
			Toast.makeText(this,"BUSY MODE TURNED ON",Toast.LENGTH_LONG).show();
		}
	}
}
