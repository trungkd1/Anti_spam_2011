package org.baole.app.antismsspam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.baole.ad.AdmobHelper;
import org.baole.app.db.ContactHelperSdk5;
import org.baole.app.db.DbHelper;
import org.baole.app.model.ContactEntry;
import org.baole.app.model.LogEntry;
import org.baole.app.util.DialogUtil;
import org.baole.service.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ComposeActivity extends Activity implements OnClickListener {
	private static final int DIALOG_CHANGLOGS = 1;
	private static final String MESSAGE = "message";
	private static final String CONTACT = "contact";

	public static final String NUMBER = "NUMBER";
	public static final String NAME = "NAME";

	public int recent_numberItems = 3;

	private String mName;
	private String mNumber;
	private int mWhoSend = 2;

	private Button btnSend;
	// private Button btnClear;
	private ArrayList<ContactEntry> contacts;
	private EditText txtMessage;
	private TextView txtSelectedContact;
	// private AutoCompleteTextView txtContact;
	private ArrayList<LogEntry> mListData;
	private ListView mListView;
	Service service;
	private static ComposeAdapter MAdapter;

	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);

		setContentView(R.layout.compose_activity);

		AdmobHelper mAdHelper = new AdmobHelper(this);
		mAdHelper.setup((LinearLayout) findViewById(R.id.ad_container),
				BaseMain.AD_ID, true);

		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(this);

		// btnClear = (Button) findViewById(R.id.btnClear);
		// btnClear.setOnClickListener(this);

		txtMessage = (EditText) findViewById(R.id.txtMessage);
		txtSelectedContact = (TextView) findViewById(R.id.txtSelectedContact);
		txtSelectedContact.setMovementMethod(new ScrollingMovementMethod());
		// txtContact = (AutoCompleteTextView) findViewById(R.id.txtContact);

		mListView = (ListView) findViewById(R.id.listview);

		if (inState != null) {
			txtMessage.setText(inState.getString(MESSAGE));
			contacts = inState.getParcelableArrayList(CONTACT);
		}

		if (contacts == null) {
			contacts = new ArrayList<ContactEntry>();
		}

		mName = getIntent().getStringExtra(NAME);
		mNumber = getIntent().getStringExtra(NUMBER);
		if (!TextUtils.isEmpty(mNumber)) {
			contacts.add(new ContactEntry(mName, mNumber));
		}

		adapter = new ContactListAdapter(this,
				new ContactHelperSdk5(this).getContactCursor());
		// txtContact.setAdapter(adapter);

		txtMessage.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				setMessageCharCount();
			}
		});

		// txtContact.setOnItemClickListener(new OnItemClickListener() {
		//
		// // @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// selectAContact(arg2);
		// showContacts();
		// }
		// });

		showContacts();
		loadConfig();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(MESSAGE, txtMessage.getText().toString());
		outState.putParcelableArrayList(CONTACT, contacts);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_CHANGLOGS:
			return DialogUtil.createChangLogDialog(this);
		default:
			return null;
		}
	}

	protected void setMessageCharCount() {
		btnSend.setText(String
				.format("Send(%d)", txtMessage.getText().length()));

	}

	private void selectAContact(int pos) {
		Cursor cursor = (Cursor) adapter.getItem(pos);
		if (cursor != null) {
			ceContact = new ContactEntry(cursor.getString(5),
					cursor.getString(3));
			if (!contacts.contains(ceContact)) {
				contacts.add(ceContact);
			}
			// clearContactBox();
		}
	}

	ContactListAdapter adapter;
	ContactEntry ceContact = null;

	public void onClick(View v) {
		if (btnSend.equals(v)) {
			send();
		}
		// else if (btnClear.equals(v)) {
		// new AlertDialog.Builder(this)
		// .setIcon(android.R.drawable.ic_dialog_alert)
		// .setTitle(R.string.confirmation)
		// .setMessage(R.string.contact_remove_message)
		// .setPositiveButton(R.string.all,
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// contacts.clear();
		// clearContactBox();
		// showContacts();
		// }
		// })
		// .setNeutralButton(R.string.last,
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// int size = contacts.size();
		// if (size > 0) {
		// contacts.remove(size - 1);
		// showContacts();
		// }
		// }
		// }).setNegativeButton(R.string.cancel, null).show();
		// }
	}

	// private void clearContactBox() {
	// txtContact.setText("");
	// }

	private void send() {
		String message = txtMessage.getText().toString();
		if (TextUtils.isEmpty(message)) {
			Toast.makeText(getApplicationContext(), "Please enter a sms",
					Toast.LENGTH_LONG).show();
			return;
		}

		// String contact = txtContact.getText().toString();
		// if (!TextUtils.isEmpty(contact)) {
		// ContactEntry ce = new ContactEntry(contact, contact);
		// if (!contacts.contains(ce)) {
		// contacts.add(ce);
		// showContacts();
		// }
		// }

		if (contacts.size() <= 0) {
			Toast.makeText(getApplicationContext(), "Please select a contact",
					Toast.LENGTH_LONG).show();
			return;
		}

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txtMessage.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		new SendTask().execute(message);
	}

	// stats
	int sentOKCount[];
	int sentFailedCount[];
	public int sentCount;

	private void showContacts() {
		int size = contacts.size();
		StringBuffer buff = new StringBuffer(String.format("To (%d): ", size));

		for (ContactEntry e : contacts) {
			if (TextUtils.isEmpty(e.name)) {
				buff.append(e.number).append(", ");
			} else {
				buff.append(e.name).append("(" + e.number + "), ");
			}
		}
		txtSelectedContact.setText(buff.toString());
	}

	private class SendTask extends AsyncTask<String, Integer, Void> {

		private String message;

		@Override
		protected void onPreExecute() {
			int size = contacts.size();
			sentFailedCount = new int[size];
			sentOKCount = new int[size];
			sentCount = 0;

			// TODO
			service = getInstance(ComposeActivity.this);
			service.setDeliveryReport(false);
			service.setSplitLongSMS(true);

			int status = service.init();
			if (status == Service.STATUS_LOGGIN_FAILED) {
				return;
			}

			btnSend.setEnabled(false);
		}

		@Override
		protected Void doInBackground(String... params) {
			message = params[0];

			int size = contacts.size();
			for (int i = 0; i < size; i++) {

				String msg = message;
				int sendstatus = service.send(contacts.get(i).number, msg);
				publishProgress(i, sendstatus, sentCount);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// addReport(info);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			btnSend.setEnabled(true);

			for (int i = 0; i < contacts.size(); i++) {
				DbHelper.addPrivate(
						ComposeActivity.this.getApplicationContext(), message,
						contacts.get(i).number, MessageEntry.TYPE_SENT,
						System.currentTimeMillis(),mWhoSend);
			}
			Toast.makeText(ComposeActivity.this,
					String.format("%d sms sent", contacts.size()),
					Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	ContentValues values = new ContentValues();

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
			view.setText(cursor.getString(5) + "(" + cursor.getString(3) + ")");
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

		// private ContentResolver mContent;
	}

	private static Service sInstance;

	public static Service getInstance(Context ctx) {
		// if(true) return new MockService();
		if (sInstance == null) {
			String className;

			int sdkVersion = Integer.parseInt(Build.VERSION.SDK); // Cupcake
			// style
			if (sdkVersion < Build.VERSION_CODES.DONUT) {
				className = "org.baole.service.SMSService34";
			} else {
				className = "org.baole.service.SMSService";
			}

			/*
			 * Find the required class by name and instantiate it.
			 */
			try {
				Class<? extends Service> clazz = Class.forName(className)
						.asSubclass(Service.class);
				sInstance = clazz.newInstance();
				sInstance.setContext(ctx);
			} catch (Exception e) {
				Log.e("SMSWraper", "" + e.getMessage());
				e.printStackTrace();
			}
		}
		return sInstance;
	}

	public class ComposeAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<LogEntry> data;
		public DateFormat format = SimpleDateFormat.getInstance();

		public ComposeAdapter(Context context, ArrayList<LogEntry> data) {
			mInflater = LayoutInflater.from(context);
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			LogEntry view = data.get(position);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row2, null);
				holder = new ViewHolder();

				holder.mContent = (TextView) convertView
						.findViewById(R.id.textContentSMS);
				holder.mCreated = (TextView) convertView
						.findViewById(R.id.textCreated);
				holder.layout = (LinearLayout)convertView.findViewById(R.id.layout);
			
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(view.mWho == 1){
				holder.layout.setGravity(Gravity.RIGHT);
				holder.mContent.setText(view.mBody);
				holder.mCreated
						.setText(format.format(view.mCreated));
			}else if(view.mWho == 2){
				holder.layout.setGravity(Gravity.LEFT);
				holder.mContent.setText(view.mBody);
				holder.mCreated
						.setText(format.format(view.mCreated));
				
			}
			

			return convertView;
		}

		class ViewHolder {
			TextView mAddress;
			TextView mCreated;
			TextView mContent;
			LinearLayout layout;
		}

	}

	public void loadConfig() {
		mListData = new ArrayList<LogEntry>();
		mListData = DbHelper.queryPrivateSMS(ComposeActivity.this, mNumber,
				null, false);
		MAdapter = new ComposeAdapter(ComposeActivity.this, mListData);
		mListView.setAdapter(MAdapter);
	}

}
