package com.blocker.callandsms.about;

import com.blocker.callandsms.startscreen.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SmsBlockMoreInfoActivity extends Activity implements OnClickListener {
	//UIComponents
	
	Button bBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.smsmoreinfo);
		this.initializeUIComponents();
	}
	
private void initializeUIComponents() {
		
		bBack=(Button)findViewById(R.id.smsmoreinfo_bBack);
		bBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(bBack==v){
			finish();
		}
		
	}

}
