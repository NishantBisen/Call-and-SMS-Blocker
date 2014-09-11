package com.blocker.callandsms.addcontactoptionsscreen;

import com.blocker.callandsms.addfromcontacts.AddFromContactsActivity;
import com.blocker.callandsms.addmanually.AddContactsManuallyActivity;
import com.blocker.callandsms.startscreen.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AddContactOptionsScreenActivity extends Activity implements OnClickListener {

//UIComponents
	Button bFromContacts;
	Button bManually;
	Button bCancel;
	
/***************************************************************************/
//callbacks!!!
	
/**lifecycle methods**/
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.addcontactoptionsscreen);
		initializeUIComponents();
	}

/**hendler/listener**/
	public void onClick(View v) {
		if(bFromContacts==v)
		{
			Intent intent=new Intent(this,AddFromContactsActivity.class);
			this.startActivity(intent);
		}
		else if(bManually==v)
		{
			Intent intent=new Intent(this,AddContactsManuallyActivity.class);
			this.startActivity(intent);
		}
		else if(bCancel==v)
		{
			finish();
			
		}
	}
	
/*********************************************************************/
//userdefined methods!!!
	
	/**innitializer**/
	private void initializeUIComponents() {	
		bFromContacts=(Button)findViewById(R.id.addcontactoptionscreens_bFromContacts);
		bFromContacts.setOnClickListener(this);
		bManually=(Button)findViewById(R.id.addcontactoptionscreens_bManually);
		bManually.setOnClickListener(this);
		bCancel=(Button)findViewById(R.id.addcontactoptionscreens_bCancel);
		bCancel.setOnClickListener(this);
	}
	
	/**handler/listener executors**/
	public void moveToAddFromPhoneContactsScreen()
	{
		
	}
	public void moveToAddManuallyScreen()
	{
		
	}
}
