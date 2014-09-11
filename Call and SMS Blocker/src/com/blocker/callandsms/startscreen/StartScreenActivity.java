package com.blocker.callandsms.startscreen;

import com.android.callandsms.emailnotificationfeature.EmailFeatureActivity;
import com.blocker.callandsms.about.AboutApplicationActivity;
import com.blocker.callandsms.about.AboutUsActivity;
import com.blocker.callandsms.contactlisthome.ContactListHomeActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartScreenActivity extends Activity implements OnClickListener{
   
	//ui components!!
	Button bConfigureApp;
	//Button bConfigureEmailOptions;
	Button bAboutApplication;
	Button bAboutUs;
	
/***********************************************************/
//callback methods!!
	
/**lifecycle methods**/
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.innitializeUIComponents();
    }

/**handlers/listeners**/
	@Override
	public void onClick(View v) {
		if(v==bConfigureApp)
		{
			this.goToContactsListScreen();
		}
		/*if(v==bConfigureEmailOptions)
		{
			this.setEmailNotificationFeature();
		}*/
		if(v==bAboutApplication)
		{
			this.displayInfoAboutApplication();
		}
		if(v==bAboutUs)
		{
			this.displayInfoAboutUs();
		}
	}


/**********************************************************/
//userdefined methods!!

/**innitializers**/
	private void innitializeUIComponents()
	{
		bConfigureApp=(Button)findViewById(R.id.startscreen_bStartApplication);
		bConfigureApp.setOnClickListener(this);
		
		//bConfigureEmailOptions=(Button)findViewById(R.id.startscreen_bCofiguringeMail);
	//	bConfigureEmailOptions.setOnClickListener(this);
		
		bAboutApplication=(Button)findViewById(R.id.startscreen_bAbout);
		bAboutApplication.setOnClickListener(this);
		
		bAboutUs=(Button)findViewById(R.id.startscreen_bAboutUs);
		bAboutUs.setOnClickListener(this);
		
	}
	
/**handler executors**/
	private void goToContactsListScreen()
	{
		Intent intent=new Intent(this,ContactListHomeActivity.class);
		this.startActivity(intent);
	}
	private void displayInfoAboutUs()
	{
		Intent intent=new Intent(this,AboutUsActivity.class);
		startActivity(intent);
	}
	private void displayInfoAboutApplication()
	{
		Intent intent=new Intent(this,AboutApplicationActivity.class);
		startActivity(intent);
	}
	private void setEmailNotificationFeature()
	{
		Intent intent=new Intent(this,EmailFeatureActivity.class);
		this.startActivity(intent);
	}
}