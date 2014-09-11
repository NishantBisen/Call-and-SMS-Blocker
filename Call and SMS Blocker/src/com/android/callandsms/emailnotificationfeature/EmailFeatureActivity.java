package com.android.callandsms.emailnotificationfeature;

import com.blocker.callandsms.mainapplication.BlacklistApplication;
import com.blocker.callandsms.startscreen.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import static com.blocker.callandsms.mainapplication.SharedPrefrencesHelper.*;

public class EmailFeatureActivity extends Activity implements OnClickListener
{
	//data members
	BlacklistApplication app=null;
	SharedPreferences prefrences=null;
	//String emailId;
	//boolean emailFeature;
	
	//UIComponents
	EditText eEmailId;
	Button bCancel;
	Button bToggleFeature;
	Button bSave;
	TextView tToggle;

/*********************************************************************************/
//callbacks!!
/**lifecycle methods**/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.emailfeaturescreen);
		this.innitializeDatamembers();
		this.initializeUiComponents();
		this.setUIComponents();
	}
	
/**handlers/listeners**/
	@Override
	public void onClick(View v) {
	
		if(v==bCancel){
			finish();
		}
		else if(v==bToggleFeature){
			this.toggleEmailFeature();
		}
		else if(v==bSave){
			this.saveCurrentValuesToSystemPrefrences();
		}
	}
/******************************************************************************************/
	//userdefined methods!!!
	
/**innnitializers**/
	private void initializeUiComponents() {
		eEmailId=(EditText)findViewById(R.id.emailfeaturescreen_eEmailId);
		
		bCancel=(Button)findViewById(R.id.emailfeaturescreen_bCancel);
		bCancel.setOnClickListener(this);
		
		bToggleFeature=(Button)findViewById(R.id.emailfeaturescreen_bToggle);
		bToggleFeature.setOnClickListener(this);
		
		bSave=(Button)findViewById(R.id.emailfeaturescreen_bSave);
		bSave.setOnClickListener(this);
		
		tToggle=(TextView)findViewById(R.id.emailfeaturescreen_tToggle);	
	}
	private void setUIComponents()
	{
		eEmailId.setText(EMAIL_ADDRESS);
		if(EMAIL_NOTIFICATION)
		{
			tToggle.setText("ON");
		}
		else
		{
			tToggle.setText("OFF");
		}
	}
	private void innitializeDatamembers()
	{
		app=(BlacklistApplication)getApplication();
		prefrences=app.getSharedPreferences(SHARED_PREFRENCES_FILE,0);
		EMAIL_ADDRESS=prefrences.getString(ATTRIBUTE_EMAIL_ADDRESS,"default value");
		EMAIL_NOTIFICATION=prefrences.getBoolean(ATTRIBUTE_EMAIL_NOTIFICATION,false);
	}
/**handler / listener executors!!!!**/
	private void toggleEmailFeature()
	{
		if(EMAIL_NOTIFICATION)
		{
			EMAIL_NOTIFICATION=false;
			this.setUIComponents();
		}
		else
		{
			EMAIL_NOTIFICATION=true;
			this.setUIComponents();
		}
	}
	
	private void saveCurrentValuesToSystemPrefrences()
	{
		EMAIL_ADDRESS=eEmailId.getText().toString();
		SharedPreferences.Editor editor=prefrences.edit();
		editor.putBoolean(ATTRIBUTE_EMAIL_NOTIFICATION,EMAIL_NOTIFICATION);
		editor.putString(ATTRIBUTE_EMAIL_ADDRESS,EMAIL_ADDRESS);
		editor.commit();
	}
}
