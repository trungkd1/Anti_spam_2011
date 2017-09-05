package org.baole.app.db;

import java.util.ArrayList;
import java.util.Date;

import org.baole.app.model.LogEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract.PhoneLookup;
import android.text.TextUtils;
import android.util.Log;

public class DbHelper {

	public static int TRUE = 1;
	public static int FALSE = 2;

	private static DbHelper mDbHelper = null;

	private SQLiteDatabase mDb;
	private Context mContext = null;

	public static DbHelper getInstance(Context context) {
		if (mDbHelper == null) {
			synchronized (DbHelper.class) {
				mDbHelper = new DbHelper(context);
			}
		}
		return mDbHelper;
	}

	public SQLiteDatabase getDb() {
		return mDb;
	}

	public DbHelper(Context context) {
//		context.deleteDatabase(DATABASE_NAME);
		mContext = context;
		DatabaseHelper openHelper = new DatabaseHelper(mContext);
		mDb = openHelper.getWritableDatabase();
	}

	public static void addHistory(Context context, String body, String number,
			int type, long date) {
		ContentValues values = new ContentValues();

		values.put(CREATED, date);
		values.put(BODY, body);
		values.put(NUMBER, number);
		values.put(TYPE, type);

		getInstance(context).mDb.insert(SPAM_TABLE_NAME, null, values);
	}

	public static void deleteHistory(Context context, String id) {
		getInstance(context).mDb.delete(SPAM_TABLE_NAME, "_id=?",
				new String[] { id });
	}

	public static void deleteHistoryByNumber(Context context, String number) {
		getInstance(context).mDb.delete(SPAM_TABLE_NAME, NUMBER + "=?",
				new String[] { number });
	}

	public static void addPrivate(Context context, String body, String number,
			int type, long date,int who) {
		ContentValues values = new ContentValues();

		values.put(CREATED, date);
		values.put(BODY, body);
		values.put(NUMBER, number);
		values.put(TYPE, type);
		values.put(WHO, who);
		getInstance(context).mDb.insert(PRIVATE_TABLE_NAME, null, values);
	}

	public static void deletePrivate(Context context, String id) {
		getInstance(context).mDb.delete(PRIVATE_TABLE_NAME, "_id=?",
				new String[] { id });
	}

	public static void deletePrivateByNumber(Context context, String number) {
		getInstance(context).mDb.delete(PRIVATE_TABLE_NAME, NUMBER + "=?",
				new String[] { number });
	}

	public static void restoreMessage(Context context, LogEntry viewData) {
		ContentValues values = new ContentValues();
		values.put(ADDRESS, viewData.mAddress);
		values.put(DATE, viewData.mCreated.getTime());
		values.put(READ, 1);
		values.put(STATUS, -1);
		values.put(MTYPE, 1);// inbox
		values.put(MBODY, viewData.mBody);
		context.getContentResolver().insert(Uri.parse("content://sms"), values);
	}

	public static ArrayList<LogEntry> querySpams(Context c, String filter,
			String[] args,boolean choice) {
		String sql;
		if(choice == true){
			 sql = "select _id, mbody, mnumber, created, mtype,count(mnumber) as count from "
				+ SPAM_TABLE_NAME +" group by mnumber" ;
		}else{
			 sql = "select _id, mbody, mnumber, created, mtype from "
				+ SPAM_TABLE_NAME;
		}
		
		if (!TextUtils.isEmpty(filter)) {
			sql += " WHERE " + filter;
		}

		sql += " ORDER BY (created) DESC ";

		Cursor cursor = getInstance(c).mDb.rawQuery(sql, args);
		ArrayList<LogEntry> list = new ArrayList<LogEntry>();
		try {
			if (c != null && cursor.moveToFirst()) {

				do {
					LogEntry viewData = new LogEntry();
					viewData.mId = cursor.getString(0);
					viewData.mBody = cursor.getString(1);
					viewData.mAddress = cursor.getString(2);
					viewData.mSender = DbHelper.getSenderName(c,
							viewData.mAddress);
					viewData.mCreated = new Date(cursor.getLong(3));
					viewData.mType = cursor.getInt(4);
					if(choice == true){
						viewData.mCount = cursor.getString(5);
					}
					list.add(viewData);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return list;
	}	
	
	
	
	public static ArrayList<LogEntry> queryPrivateSMS(Context c, String filter,
			String[] args,boolean choice) {
		String sql;
		if(choice == true){
			 sql = "select _id, mbody, mnumber, created, mtype,mwho, count(mnumber) as count from "
				+ PRIVATE_TABLE_NAME  +" group by mnumber" ;
		}else{
			 sql = "select _id, mbody, mnumber, created, mtype,mwho from "
				+ PRIVATE_TABLE_NAME  ;
		}
		
		if (!TextUtils.isEmpty(filter)) {
			sql += " WHERE mnumber = '" + filter+"'";
		}

		sql += " ORDER BY (created) DESC ";

		Cursor cursor = getInstance(c).mDb.rawQuery(sql, args);
		ArrayList<LogEntry> list = new ArrayList<LogEntry>();
		try {
			if (c != null && cursor.moveToFirst()) {

				do {
					LogEntry viewData = new LogEntry();
					viewData.mId = cursor.getString(0);
					viewData.mBody = cursor.getString(1);
					viewData.mAddress = cursor.getString(2);
					viewData.mSender = DbHelper.getSenderName(c,
							viewData.mAddress);
					viewData.mCreated = new Date(cursor.getLong(3));
					viewData.mType = cursor.getInt(4);
					viewData.mWho = cursor.getInt(5);
					if(choice == true){
						viewData.mCount = cursor.getString(6);
					}
					
				
					list.add(viewData);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return list;
	}

	public static final String ADDRESS = "address";
	public static final String PERSON = "person";
	public static final String DATE = "date";
	public static final String READ = "read";
	public static final String STATUS = "status";
	public static final String MTYPE = "type";
	public static final String MBODY = "body";
	public static final String MWHO = "who";
	
	public static String getSenderName(Context context, String mAddress) {
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(mAddress));
		String[] mPhoneNumberProjection = { PhoneLookup._ID,
				PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
		Cursor cur = context.getContentResolver().query(lookupUri,
				mPhoneNumberProjection, null, null, null);
		try {
			if (cur.moveToFirst()) {
				return cur.getString(2);
			}
		} finally {
			if (cur != null)
				cur.close();
		}
		return null;
	}

	/**
	 * check either numbers in phone contacts
	 * @param context
	 * @param number
	 * @return
	 */
	public static boolean contactExists(Context context, String number) {
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));
		String[] mPhoneNumberProjection = { PhoneLookup._ID,
				PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
		Cursor cur = context.getContentResolver().query(lookupUri,
				mPhoneNumberProjection, null, null, null);
		try {
			try {
				if (cur.moveToFirst()) {
					return true;
				}
			} finally {
				if (cur != null)
					cur.close();
			}

		} catch (Throwable e) {
		}
		return false;
	}

	private static final String DATABASE_NAME = "antismsspam.db";
	private static final int DATABASE_VERSION = 3;

	private static final String SPAM_TABLE_NAME = "spam";
	private static final String PRIVATE_TABLE_NAME = "privatesms";

	public static final String CREATED = "created";
	public static final String BODY = "mbody";
	public static final String NUMBER = "mnumber";
	public static final String TYPE = "mtype";
	public static final String COUNT = "mcount";
	public static final String WHO = "mwho";

	/**
	 * This class helps open, create, and upgrade the database file.
	 */
	public static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + SPAM_TABLE_NAME + " ("
					+ BaseColumns._ID + " INTEGER PRIMARY KEY," + BODY
					+ " TEXT," + NUMBER + " TEXT," + TYPE + " INTEGER," + COUNT
					+ " INTEGER," + CREATED + " INTEGER," +  WHO  + " INTEGER " + " );");

			db.execSQL("CREATE TABLE " + PRIVATE_TABLE_NAME + " ("
					+ BaseColumns._ID + " INTEGER PRIMARY KEY," + BODY
					+ " TEXT," + NUMBER + " TEXT," + TYPE + " INTEGER," + COUNT
					+ " INTEGER," + CREATED + " INTEGER," + WHO  + " INTEGER " + " );");
			
			//Log.e("CREATE DATABASE", "Data");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Log.w(getClass().getName(), "Upgrading database from version "
			// + oldVersion + " to " + newVersion
			// + ", which will destroy all old data");
			db.execSQL("CREATE TABLE " + PRIVATE_TABLE_NAME + " ("
					+ BaseColumns._ID + " INTEGER PRIMARY KEY," + BODY
					+ " TEXT," + NUMBER + " TEXT," + TYPE + " INTEGER," + COUNT
					+ " INTEGER," + CREATED + " INTEGER" + ");");
		}
	}

}
