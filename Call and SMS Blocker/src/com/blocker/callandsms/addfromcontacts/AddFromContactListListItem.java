package com.blocker.callandsms.addfromcontacts;

import com.blocker.callandsms.contactrep.Contact;
import com.blocker.callandsms.startscreen.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddFromContactListListItem extends RelativeLayout {

	
	TextView tName;
	TextView tNumber;
	
/*****************************************************************************/
	public AddFromContactListListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.innitializeUIComponents();
		
	}
/*******************************************************************************/
	//userdefined methods!!!

/**innitialize ui components**/
	private void innitializeUIComponents()
	{
		tName=(TextView)findViewById(R.id.addfromcontactlistitem_tName);
		tNumber=(TextView)findViewById(R.id.addfromcontactlistitem_tNumber);
	}
	

	public void setContent(Contact contact)
	{
		tName.setText(contact.getContactName());
		tNumber.setText(contact.getContactNumber());
	}
	
}
