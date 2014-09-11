package com.blocker.callandsms.addfromcontacts;

import java.util.ArrayList;

import com.blocker.callandsms.contactrep.Contact;
import com.blocker.callandsms.startscreen.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AddFromContactsListAdapter extends BaseAdapter {

	Context context;
	ArrayList<Contact> array;
	
	public AddFromContactsListAdapter(Context currentContext,ArrayList<Contact> array)
	{
		this.context=currentContext;
		this.array=array;
	}
	
	@Override
	public int getCount() {
		
		return array.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AddFromContactListListItem item=null;
		if(convertView==null)
		{
			item=(AddFromContactListListItem)View.inflate(context,R.layout.addfromcontactslistitem,null);
		}
		else
		{
			item=(AddFromContactListListItem)convertView;
		}
		Contact contact=array.get(position);
		item.setContent(contact);
		return item;
	}

}
