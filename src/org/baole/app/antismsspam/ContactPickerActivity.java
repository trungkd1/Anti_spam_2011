package org.baole.app.antismsspam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.baole.ad.AdmobHelper;
import org.baole.app.conf.Configuration;
import org.baole.app.db.ContactHelperSdk5;
import org.baole.app.util.DialogUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AlphabetIndexer;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SectionIndexer;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class ContactPickerActivity extends Activity implements
		OnItemClickListener {
	public static final String CONTACT = "Contacts";
	public static final String CALL_LOG = "CallLogs";
	public static final String SMS_LOG = "SMS";
	public static final String NAME = "name";
	public static final String NUMBER = "number";
	public static final String TYPE = "_type";
	public static final String OTHER = "Custom";
	public static final String SHOW_CUSTOM_TAB = "_sct";

	private ListView mContactListView;
	private ListView mCalllogListView;
	private ListView mSMSListView;

	private TabHost myTabHost;
	private Cursor mContactCursor;
	private ArrayList<CallLogEntry> mCallLogData;
	private ArrayList<SMSEntry> mSMSData;

	private static final String[] PHONE_CONTACT = new String[] { Phone._ID,
			Phone.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE };

	private static final String[] CALL_LOGS = new String[] { Calls._ID,
			Calls.CACHED_NAME, Calls.NUMBER, Calls.TYPE, Calls.DATE };

	private static final String[] SMS = new String[] { Calls._ID, "person",
			"address", "body" };

	private int mDefaultAction;
	private AdmobHelper mAdHelper;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(this);

		mContactListView = new ListView(this);
		mCalllogListView = new ListView(this);
		mSMSListView = new ListView(this);

		setContentView(R.layout.contact_picker_activity);

		mAdHelper = new AdmobHelper(this);
		mAdHelper.setup((LinearLayout) findViewById(R.id.ad_container),
				BaseMain.AD_ID, true);

		myTabHost = (TabHost) findViewById(android.R.id.tabhost);
		myTabHost.setup();
		myTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		TabSpec ts = null;

		ts = myTabHost.newTabSpec(SMS_LOG);
		ts.setIndicator(createTabView(this, SMS_LOG));
		ts.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				loadSMSLogs();
				return mSMSListView;
			}
		});
		myTabHost.addTab(ts);

		ts = myTabHost.newTabSpec(CALL_LOG);
		ts.setIndicator(createTabView(this, CALL_LOG));
		ts.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				loadCallLogs();
				return mCalllogListView;
			}
		});
		myTabHost.addTab(ts);

		ts = myTabHost.newTabSpec(CONTACT);
		ts.setIndicator(createTabView(this, CONTACT));
		ts.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				loadContactList();
				return mContactListView;
			}
		});
		myTabHost.addTab(ts);

		if (getIntent().getBooleanExtra(SHOW_CUSTOM_TAB, true)) {
			addOtherTab(ts);
		}
	}

	protected void addOtherTab(TabSpec ts) {
		ts = myTabHost.newTabSpec(OTHER);
		ts.setIndicator(createTabView(this, OTHER));
		ts.setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				loadOther();
				return mOtherView;
			}
		});
		myTabHost.addTab(ts);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	private AutoCompleteTextView mEditNumber;
	private Spinner mMatch;
	private Button mButtonAdd;
	ContactListAdapter mContactAdapter;
	ContactHelperSdk5 mContactHelper;
	private View mOtherView;

	protected void loadOther() {
		mContactHelper = new ContactHelperSdk5(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mOtherView = inflater.inflate(R.layout.customnumber, null);
		mButtonAdd = (Button) mOtherView.findViewById(R.id.btnAdd);
		mEditNumber = (AutoCompleteTextView) mOtherView
				.findViewById(R.id.txtNumber);
		mMatch = (Spinner) mOtherView.findViewById(R.id.spn_match_option);

		mButtonAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AdmobHelper.HAS_ADS) {
					DialogUtil.createGoProDialog(ContactPickerActivity.this,
							Configuration.PRO_PACKAGE).show();
				} else {
					String name = null;
					String number = mEditNumber.getText().toString();
					if (!TextUtils.isEmpty(number)) {
						int match = mMatch.getSelectedItemPosition() + 1;
						updateAndFinish(name, number, match);
					}
				}
			}
		});

		mContactAdapter = new ContactListAdapter(this,
				mContactHelper.getContactCursor());
		mEditNumber.setAdapter(mContactAdapter);
		mEditNumber.setOnItemClickListener(new OnItemClickListener() {

			// @Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectAContact(arg2);
			}
		});

	}

	private void selectAContact(int pos) {
		Cursor cursor = (Cursor) mContactAdapter.getItem(pos);
		if (cursor != null) {
			String name = cursor.getString(5);
			String number = cursor.getString(3);
			updateAndFinish(name, number, Configuration.MATCH_REGULAR);
		}
	}

	public static class ContactListAdapter extends CursorAdapter implements
			Filterable {
		Activity ctx;

		public ContactListAdapter(Activity context, Cursor c) {
			super(context, c);
			ctx = context;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final TextView view = (TextView) inflater.inflate(
					android.R.layout.simple_dropdown_item_1line, parent, false);
			view.setText(cursor.getString(5) + "[" + cursor.getString(3) + "]");
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			((TextView) view).setText(cursor.getString(5) + "("
					+ cursor.getString(3) + ")");
		}

		@Override
		public String convertToString(Cursor cursor) {
			return cursor.getString(5);
		}

		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
			if (getFilterQueryProvider() != null) {
				return getFilterQueryProvider().runQuery(constraint);
			}
			return new ContactHelperSdk5(ctx).queryFilter(constraint);
		}
	}

	static class SMSEntry {
		String mName;
		String mNumber;
		String mBody;

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SMSEntry other = (SMSEntry) obj;
			if (mNumber == null) {
				if (other.mNumber != null)
					return false;
			} else if (!mNumber.equals(other.mNumber))
				return false;
			return true;
		}
	}

	protected void loadSMSLogs() {
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms"),
				SMS, null, null, null);

		mSMSData = new ArrayList<SMSEntry>();

		if (cursor.moveToFirst()) {
			do {
				SMSEntry e = new SMSEntry();
				e.mName = cursor.getString(1);
				e.mNumber = cursor.getString(2);
				e.mBody = cursor.getString(3);
				if (!mSMSData.contains(e))
					mSMSData.add(e);
			} while (cursor.moveToNext());
		}

		ListAdapter adapter = new SMSLogsAdapter(this, mSMSData);
		mSMSListView.setAdapter(adapter);
		mSMSListView.setOnItemClickListener(this);
	}

	static class CallLogEntry {
		String mName;
		String mNumber;
		int mType;
		int mDate;

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CallLogEntry other = (CallLogEntry) obj;
			if (mNumber == null) {
				if (other.mNumber != null)
					return false;
			} else if (!mNumber.equals(other.mNumber))
				return false;
			return true;
		}

	}

	protected void loadCallLogs() {
		Cursor cursor = getContentResolver().query(Calls.CONTENT_URI,
				CALL_LOGS, null, null, Calls.DATE + " DESC");

		mCallLogData = new ArrayList<CallLogEntry>();

		if (cursor.moveToFirst()) {
			do {
				CallLogEntry e = new CallLogEntry();
				e.mName = cursor.getString(1);
				e.mNumber = cursor.getString(2);
				e.mType = cursor.getInt(3);
				e.mDate = cursor.getInt(4);
				if (!mCallLogData.contains(e))
					mCallLogData.add(e);
			} while (cursor.moveToNext());
		}

		ListAdapter adapter = new CallLogsAdapter(this, mCallLogData);
		mCalllogListView.setAdapter(adapter);
		mCalllogListView.setOnItemClickListener(this);
	}

	private void loadContactList() {
		mContactCursor = getContentResolver().query(Phone.CONTENT_URI,
				PHONE_CONTACT, Phone.NUMBER + " IS NOT NULL", null,
				Phone.DISPLAY_NAME + " asc");
		ListAdapter adapter = new ContactAdapter(this, mContactCursor);
		mContactListView.setAdapter(adapter);

		mContactListView.setOnItemClickListener(this);
		mContactListView.setScrollContainer(true);
		mContactListView
				.setScrollBarStyle(ScrollView.SCROLLBARS_INSIDE_OVERLAY);
		mContactListView.setFastScrollEnabled(true);
		mContactListView.setTextFilterEnabled(true);
	}

	public static String getStringByType(int type) {
		String numberType;
		switch (type) {
		case Phone.TYPE_MOBILE:
			numberType = "Mobile";
			break;
		case Phone.TYPE_HOME:
			numberType = "Home";
			break;
		case Phone.TYPE_WORK:
			numberType = "Work";
			break;
		default:
			numberType = "Other";
			break;
		}
		return numberType;
	}

	private static class SMSLogsAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<SMSEntry> data;

		public SMSLogsAdapter(Context context, ArrayList<SMSEntry> data) {
			mInflater = LayoutInflater.from(context);
			this.data = data;
		}

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						android.R.layout.simple_list_item_2, null);
				holder = new ViewHolder();
				holder.text1 = (TextView) convertView
						.findViewById(android.R.id.text1);
				holder.text2 = (TextView) convertView
						.findViewById(android.R.id.text2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			SMSEntry e = data.get(position);
			holder.text1.setText(formatLine1(e));
			holder.text2.setText(e.mBody);
			return convertView;
		}

		private CharSequence formatLine1(SMSEntry e) {
			if (TextUtils.isEmpty(e.mName))
				return e.mNumber;
			return String.format("%s [%s]", e.mName, e.mNumber);
		}

		static class ViewHolder {
			TextView text1;
			TextView text2;
		}
	}

	private static class CallLogsAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<CallLogEntry> data;

		public CallLogsAdapter(Context context, ArrayList<CallLogEntry> data) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			this.data = data;
		}

		public int getCount() {
			return data.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						android.R.layout.simple_list_item_2, null);
				holder = new ViewHolder();
				holder.text1 = (TextView) convertView
						.findViewById(android.R.id.text1);
				holder.text2 = (TextView) convertView
						.findViewById(android.R.id.text2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CallLogEntry e = data.get(position);
			holder.text1.setText(formatLine1(e));
			holder.text2.setText(formatLine2(e));
			return convertView;
		}

		private CharSequence formatLine2(CallLogEntry e) {
			String type = null;

			switch (e.mType) {
			case Calls.INCOMING_TYPE:
				type = "Incoming";
				break;
			case Calls.OUTGOING_TYPE:
				type = "Outgoing";
				break;
			default:
				type = "Missed call";
				break;
			}

			return String.format("%s (%s)", SimpleDateFormat.getInstance()
					.format(new Date(e.mDate)), type);
		}

		private CharSequence formatLine1(CallLogEntry e) {
			if (TextUtils.isEmpty(e.mName))
				return e.mNumber;
			return String.format("%s [%s]", e.mName, e.mNumber);
		}

		static class ViewHolder {
			TextView text1;
			TextView text2;
		}
	}

	static class ContactAdapter extends CursorAdapter implements SectionIndexer {
		private AlphabetIndexer mIndexer;
		private LayoutInflater mInflater;

		public ContactAdapter(Context context, Cursor c) {
			super(context, c);
			mIndexer = new AlphabetIndexer(c, 1, " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView text1 = (TextView) view.findViewById(android.R.id.text1);
			TextView text2 = (TextView) view.findViewById(android.R.id.text2);
			text1.setText(cursor.getString(1));
			text2.setText(cursor.getString(2) + " ["
					+ getStringByType(cursor.getInt(3)) + "]");
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View v = mInflater.inflate(android.R.layout.simple_list_item_2,
					null);
			bindView(v, context, cursor);
			return v;
		}

		public int getPositionForSection(int section) {
			return mIndexer.getPositionForSection(section);
		}

		public int getSectionForPosition(int position) {
			return mIndexer.getPositionForSection(position);
		}

		public Object[] getSections() {
			return mIndexer.getSections();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position,
			long arg3) {
		if (position < 0) {
			return;
		}

		if (mContactListView.equals(view)) {
			Cursor c = (Cursor) mContactListView.getItemAtPosition(position);
			updateAndFinish(c.getString(1), c.getString(2),
					Configuration.MATCH_REGULAR);
		} else if (mCalllogListView.equals(view)) {
			CallLogEntry e = mCallLogData.get(position);
			updateAndFinish(e.mName, e.mNumber, Configuration.MATCH_REGULAR);
		} else if (mSMSListView.equals(view)) {
			SMSEntry e = mSMSData.get(position);
			updateAndFinish(e.mName, e.mNumber, Configuration.MATCH_REGULAR);
		}
	}

	protected void updateAndFinish(String name, String number, int type) {
		Intent data = new Intent();

		data.putExtra(NAME, name);
		data.putExtra(NUMBER, number);
		data.putExtra(TYPE, type);

		setResult(RESULT_OK, data);
		finish();
	}
}
