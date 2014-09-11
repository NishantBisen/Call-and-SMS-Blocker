package com.blocker.callandsms.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import static com.blocker.callandsms.contentprovider.BlacklistConstants.*;
public class BlacklistContactsContentProvider extends ContentProvider {

	SQLiteDatabase database=null;
	static UriMatcher matcher=null;
	
	static
	{
		matcher=new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(AUTHORITY,MIME_CONTACTS,1);
		matcher.addURI(AUTHORITY,MIME_COMMUNITY_CONTACTS,2);
	}
	
	@Override
	public boolean onCreate() {
		BlacklistOpenHelper helper=new BlacklistOpenHelper(getContext());
		database=helper.getWritableDatabase();
		return true;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int matchCode=matcher.match(uri);
		int status=0;
		switch(matchCode)
		{
			case 1:
				status=database.delete(TABLE_NAME,selection,selectionArgs);
				break;
			case 2:
				status=database.delete(TABLE_COMMUNITY_BLACKLIST_NAME,selection,selectionArgs);
				break;
			default:
				Log.v("uri not matching","URI match error in delete of BlacklistContactsContentProvider");
				
		}
		return status;
	}

	@Override
	public String getType(Uri uri) {
		String type=null;
		int matchCode=matcher.match(uri);
		switch(matchCode)
		{
			case 1:
				type="BlacklistContactsList";
				break;
			case 2:
				type="communityblacklistcontacts";
				break;
			default:
				Log.v("uri not matching","URI match error in type of BlacklistContactsContentProvider");
		}
		return type;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int matchCode=matcher.match(uri);
		switch(matchCode)
		{
			case 1:
				database.insert(TABLE_NAME,null,values);
				break;
			case 2:
				database.insert(TABLE_COMMUNITY_BLACKLIST_NAME,null,values);
				break;
			default:
				Log.v("uri not matching","URI match error in insert of BlacklistContactsContentProvider");
		}
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		int matchCode=matcher.match(uri);
		Cursor cursor=null;
		switch(matchCode)
		{
			case 1:
				Log.v("uri match","executing query!!!");
				cursor=database.query(TABLE_NAME,projection,selection, selectionArgs,null,null,null);
				break;
			case 2:
				cursor=database.query(TABLE_COMMUNITY_BLACKLIST_NAME,projection,selection, selectionArgs,null,null,null);
				break;
			default:
				Log.v("uri not matching","URI match error in query of BlacklistContactsContentProvider");
 		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int matchCode=matcher.match(uri);
		int statusCode=0;
		switch(matchCode)
		{
			case 1:
				statusCode=database.update(TABLE_NAME, values,selection,selectionArgs);
				break;
			case 2:
				statusCode=database.update(TABLE_COMMUNITY_BLACKLIST_NAME, values, selection,selectionArgs);
			default:
				Log.v("uri not matching","URI match error in update of BlacklistContactsContentProvider");
		}
		return statusCode;
	}

}
