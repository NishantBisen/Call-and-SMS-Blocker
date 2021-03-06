package com.blocker.callandsms.callcommunityblacklistfeaturescreen;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.blocker.callandsms.contactrep.Contact;
import com.blocker.callandsms.mainapplication.BlacklistApplication;
import com.blocker.callandsms.startscreen.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CallCommynityBlacklistFeatureActivity extends Activity implements OnClickListener{
	
	//datamembers!!!
	BlacklistApplication app=null;
	int currentIndex=0;
	ArrayList<Contact> contactList=null;
	String currentNumber;
	
	//ui components!!!
	TextView tStatus;
	
	Button bRefresh;
	Button bRequest;
	Button bCancel;
	
/****************************************************************************/
//callback methods!!!

/**lifecycle methods**/
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.callcommunityblacklistscreen);
		
		this.innitializeUIComponents();
		this.innitializeDatamembers();
	}
	
/**handlers/listeners**/
	@Override
	public void onClick(View v) {
		if(bCancel==v){
			finish();
		}
		else if(bRefresh==v){
			new Thread(new Runnable(){
				public void run(){
					CallCommynityBlacklistFeatureActivity.this.refreshTheListToIncludeCommunityBlacklistedNumbers();
				}
			}).start();
			Toast.makeText(this,"Refreshing the list from the server!!!",Toast.LENGTH_LONG).show();
		}
		else if(bRequest==v){
			communityBlacklistHandler();
		}
		
	}
/*****************************************************************************/
//userdefined methods!!!
	/**innitializers**/
	private void innitializeUIComponents()
	{
		tStatus=(TextView)findViewById(R.id.callcommunityblacklist_tCurrentStatus);
		bRefresh=(Button)findViewById(R.id.callcommunityblacklist_bRefresh);
		bRefresh.setOnClickListener(this);
		
		bRequest=(Button)findViewById(R.id.callcommunityblacklist_bRequest);
		bRequest.setOnClickListener(this);
		
		bCancel=(Button)findViewById(R.id.callcommunityblacklist_bCancel);
		bCancel.setOnClickListener(this);
	}
	private void innitializeDatamembers()
	{
		app=(BlacklistApplication)getApplication();
		Intent intent=getIntent();
		currentIndex=intent.getIntExtra("index",-1);
		contactList=app.getContactListArray();
		currentNumber=contactList.get(currentIndex).getContactNumber();
	}
	
/**handler/listener executors**/
	private void refreshTheListToIncludeCommunityBlacklistedNumbers()
	{
		try
		{
			URL url=new URL("http://172.0.1.89:8080/CallandSmsBlockServer/retrievenumbers");
			URLConnection conn=url.openConnection();
			InputStream inStream=conn.getInputStream();
			
			SAXParserFactory factory=SAXParserFactory.newInstance();
			SAXParser parser=factory.newSAXParser();
			parser.parse(inStream,new DefaultHandler(){
				
				boolean flag=false;
				
				@Override
				public void startDocument() throws SAXException {
					super.startDocument();
				}

				@Override
				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {
					super.startElement(uri, localName, qName, attributes);
					if(qName.equals("number"))
					{
						flag=true;
					}
				}
				
				@Override
				public void characters(char[] ch, int start, int length)
						throws SAXException {
					super.characters(ch, start, length);
					if(flag)
					{
						Contact contact=new Contact(10000,"CommunityBlacklisted",String.valueOf(ch,start,length),true,true,true,true,true,true,true);
						app.insertContactIntoCommunityBlacklist(contact);
					}
				}

				@Override
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					super.endElement(uri, localName, qName);
					if(qName.equals("number"))
					{
						flag=false;
					}
				}
				
				@Override
				public void endDocument() throws SAXException {
					super.endDocument();
				}
				
			});
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	private void communityBlacklistHandler()
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						Log.v("client side : ","adding numebr : "+currentNumber);
						CallCommynityBlacklistFeatureActivity.this.requestCurrentNumberToBeBlacklisted(currentNumber);
					}
				}).start();
			}
		});
		builder.setNeutralButton("Cancel",null);
		builder.setNegativeButton("No",null);
		builder.setTitle("DO YOU WANT TO COMMUNITY BLACKLIST THIS NUMBER???");
		builder.setMessage("WARNING!!!! YOU CANNOT DELETE THIS NUMBER FROM THE SERVER LATER!!!!");
		builder.show();
	}
	
	private void requestCurrentNumberToBeBlacklisted(String number)
	{
		try
		{
			URL url=new URL("http://172.0.1.89:8080/CallandSmsBlockServer/addnumber?number="+number);
			URLConnection conn=url.openConnection();
			conn.getInputStream();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
