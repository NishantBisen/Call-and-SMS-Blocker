package com.blocker.callandsms.about;

import com.blocker.callandsms.startscreen.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutUsActivity extends Activity implements OnClickListener {

	//UIComponents
	Button bBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.aboutus);
		this.initialzeComponents();
	}

	private void initialzeComponents() {
		bBack=(Button)findViewById(R.id.aboutus_bBack);
		bBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v==bBack){
			finish();
		}
	}

}
