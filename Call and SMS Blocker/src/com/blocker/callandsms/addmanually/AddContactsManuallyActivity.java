package com.blocker.callandsms.addmanually;

import com.blocker.callandsms.contactrep.Contact;
import com.blocker.callandsms.mainapplication.BlacklistApplication;
import com.blocker.callandsms.startscreen.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;


public class AddContactsManuallyActivity extends Activity implements OnClickListener {
	//UI components
	Button bAdd;
	Button bCancel;
	EditText eName;
	EditText eNumber;
	
/*******************************************************************************/
//callbacks!!!
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.addcontactsmanually);
		initializeUIComponents();
	}
	
	@Override
	public void onClick(View v) {
		
		if(bAdd==v){
			addToDataBase();
		}
		else if(bCancel==v){
			finish();
		}	
	}

  
/*********************************************************************************/
	//initialize UI Components
	private void initializeUIComponents() {
		bAdd=(Button)findViewById(R.id.addcontactsmanually_bAdd);
		bAdd.setOnClickListener(this);
		bCancel=(Button)findViewById(R.id.addcontactsmanually_bCancel);
		bCancel.setOnClickListener(this);
		eName=(EditText)findViewById(R.id.addcontactsmanually_eName);
		eNumber=(EditText)findViewById(R.id.addcontactsmanually_eNumber);
	}
	
	 private void addToDataBase() {
		Contact contact=new Contact();
		contact.setContactName(eName.getText().toString());
		contact.setContactNumber(eNumber.getText().toString());			
		BlacklistApplication app=(BlacklistApplication)getApplication();
		app.insertContactIntoDatabase(contact);
		finish();
	}

	
	

}
