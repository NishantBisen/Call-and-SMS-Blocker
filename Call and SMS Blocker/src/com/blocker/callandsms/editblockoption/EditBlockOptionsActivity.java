package com.blocker.callandsms.editblockoption;

import com.blocker.callandsms.callcblock.CallBlockActivity;
import com.blocker.callandsms.mainapplication.BlacklistApplication;
import com.blocker.callandsms.smsblock.SmsBlockActivity;
import com.blocker.callandsms.startscreen.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EditBlockOptionsActivity extends Activity implements OnClickListener{

	//datamembers!!
	int clickedIndex;
	BlacklistApplication app=null;
	
	//ui components!!!
	TextView tName;
	TextView tNumber;
	
	Button bCallBlockOpt;
	Button bSMSBlockOpt;
	Button bDelete;
	Button bCancel;
	
/*******************************************************************************/
	//callbacks!!!
	
/**lifecycle methods**/
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.editblockoptions);
		this.innitializeDatamembers();
		this.innitializeUIComponents();
		this.setTextViews();
	}
	
/**callback methods**/
	@Override
	public void onClick(View v) {
		if(v==bCallBlockOpt)
		{
			this.moveToCallBlockOptsScreen();
		}
		if(v==bSMSBlockOpt)
		{
			this.moveToSMSBlockOptions();
		}
		if(v==bCancel)
		{
			finish();
		}
		if(v==bDelete)
		{
			this.deleteContact();
		}
	}

/********************************************************************************/
	private void innitializeUIComponents()
	{
		tName=(TextView)findViewById(R.id.editblockoptions_tName);
		tNumber=(TextView)findViewById(R.id.editblockoptions_tNumber);
		bCallBlockOpt=(Button)findViewById(R.id.editblockoptions_bcallblockopt);
		bCallBlockOpt.setOnClickListener(this);
		bSMSBlockOpt=(Button)findViewById(R.id.editblockoptions_bsmsblockopt);
		bSMSBlockOpt.setOnClickListener(this);
		bDelete=(Button)findViewById(R.id.editblockoptions_bDelete);
		bDelete.setOnClickListener(this);
		bCancel=(Button)findViewById(R.id.editblockoptions_bCancel);
		bCancel.setOnClickListener(this);
	}
	
	private void innitializeDatamembers()
	{
		Intent intent=getIntent();
		clickedIndex=intent.getIntExtra("position",-1);
	}
	
	private void setTextViews()
	{
		app=(BlacklistApplication)getApplication();
		tName.setText(app.getContactListArray().get(clickedIndex).getContactName());
		tNumber.setText(app.getContactListArray().get(clickedIndex).getContactNumber());
	}
	
/**userdefined handlers**/
	
	private void deleteContact() {
		app.deleteContactInfo(app.getContactListArray().get(clickedIndex));
		finish();
	}
	private void moveToCallBlockOptsScreen()
	{
		Intent intent=new Intent(this,CallBlockActivity.class);
		intent.putExtra("index",clickedIndex);
		this.startActivity(intent);
	}
	private void moveToSMSBlockOptions()
	{
		Intent intent=new Intent(this,SmsBlockActivity.class);
		intent.putExtra("index",clickedIndex);
		this.startActivity(intent);
	}

}
