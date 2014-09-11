package com.blocker.callandsms.contactlisthome;

import java.util.ArrayList;

import com.blocker.callandsms.contactrep.Contact;
import com.blocker.callandsms.startscreen.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ContactListHomeAdapter extends BaseAdapter {

	Context context=null;
	ArrayList<Contact> array=null;
	
	public ContactListHomeAdapter(Context currentContext,ArrayList<Contact> contactlist)
	{
		this.context=currentContext;
		this.array=contactlist;
	}
	
	@Override
	public int getCount() {
		return array.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactListHomeListItem item=null;
		
		if(convertView==null)
		{
			item=(ContactListHomeListItem)View.inflate(context,R.layout.contactlisthomelistitem,null);
		}
		else
		{
			item=(ContactListHomeListItem)convertView;
		}
		Log.v("",""+array.get(position).toString());
		item.setContents(array.get(position));
		return item;
	}

}
