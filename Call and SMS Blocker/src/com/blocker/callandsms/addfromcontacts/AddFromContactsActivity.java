package com.blocker.callandsms.addfromcontacts;

import java.util.ArrayList;

import com.blocker.callandsms.contactrep.Contact;
import com.blocker.callandsms.mainapplication.BlacklistApplication;
import com.blocker.callandsms.startscreen.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AddFromContactsActivity extends ListActivity implements OnClickListener,OnItemClickListener{
	//datamembers!!!
	ArrayList<Contact> phoneContactsList=null; 
	ListView list=null;
	//ui components!!!
	Button bCancel=null;
	AddFromContactsListAdapter adapter=null;
/****************************************************************/
	//callbacks
	
/**life cyclemethods**/
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.addfromcontact);
		this.innitializeDatamembers();
		this.getContactsFromPhoneContacts();
		this.innitializeUIComponents();
	}
	public void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	
/**listener/handlers**/
	@Override
	public void onClick(View v) {
		if(bCancel==v){
			finish();
		}
	}
		
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		this.addContactToDatabase(arg2);
	}
/********************************************************************/
	//userdefined methods

/**innitializers**/
	private void innitializeUIComponents()
	{
		bCancel=(Button)findViewById(R.id.addfromcontact_bCancel);
		bCancel.setOnClickListener(this);
		
		adapter=new AddFromContactsListAdapter(this,phoneContactsList);
		this.setListAdapter(adapter);
		
		list=getListView();
		list.setOnItemClickListener(this);
	}
	private void innitializeDatamembers()
	{
		phoneContactsList=new ArrayList<Contact>();
	}
	
/**handlers/listner executors**/
	private void addContactToDatabase(final int position)
	{
		AlertDialog.Builder alert=new AlertDialog.Builder(this);
		alert.setTitle("Do you want to add this contact to the blocked list??");
		alert.setMessage("Name :"+phoneContactsList.get(position).getContactName()+"\nPhone Number :"+phoneContactsList.get(position).getContactNumber());
		alert.setPositiveButton("Yes",new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BlacklistApplication app=(BlacklistApplication)getApplication();
				app.insertContactIntoDatabase(phoneContactsList.get(position));
				finish();
			}
		});
		alert.setNeutralButton("Cancel",null);
		alert.show();
	}
	
/**gen contacts from phone contacts!!!**/
	private void getContactsFromPhoneContacts()
	{
		ContentResolver resolver=getContentResolver();
		Cursor cursor=resolver.query(ContactsContract.Contacts.CONTENT_URI,new String[]{ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts.HAS_PHONE_NUMBER},null,null,null);
		
		Log.v("iterating cursor","iterating cursor");
		if(!cursor.isAfterLast())
		{
			cursor.moveToFirst();
			do
			{
				Contact contact=new Contact();
				String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				int noOfPhones=cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				contact.setContactName(name);
				Log.v(name,""+noOfPhones);
				
				if(noOfPhones>0)
				{
					Cursor cur=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},String.format("DISPLAY_NAME='%s'",name),null,null);
					cur.moveToFirst();
					String number=cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					Log.v("number = ",number);
					contact.setContactNumber(number);
				}
				phoneContactsList.add(contact);
				cursor.moveToNext();
			}
			while(!cursor.isAfterLast());
		}
		cursor.close();
	}

}
