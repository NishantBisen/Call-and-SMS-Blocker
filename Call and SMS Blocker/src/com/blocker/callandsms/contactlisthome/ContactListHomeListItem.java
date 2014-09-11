package com.blocker.callandsms.contactlisthome;

import com.blocker.callandsms.contactrep.Contact;
import com.blocker.callandsms.startscreen.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


public class ContactListHomeListItem extends android.widget.RelativeLayout {

	TextView tName=null;
	TextView tNumber=null;

/***************************************************************************/
//callbacks
	/**constructor**/
	public ContactListHomeListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**lifecycle methods**/
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.innitializeUIComponents();
	}
/**********************************************************************************/
//userdefined methods!!!
	/**innitialize ui components**/
	private void innitializeUIComponents()
	{	
		tName=(TextView)findViewById(R.id.contactlisthomelistitem_name);
		tNumber=(TextView)findViewById(R.id.conatctlisthomelistitem_phone);
	}
	
	/**set contents**/
	public void setContents(Contact contact)
	{
		tName.setText(contact.getContactName());
		tNumber.setText(contact.getContactNumber());
	}
	
/**************************************************************************************/
}
