package org.baole.app.antismsspam;

import java.util.ArrayList;

import org.baole.ad.AdmobHelper;
import org.baole.app.conf.Configuration;
import org.baole.app.db.ContactHelperSdk5;
import org.baole.app.model.ContactEntry;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class WhitelistSettingActivity extends ListActivity implements
		OnClickListener {
	private static final int CONTACT_PICKER_REQUEST_CODE = 0;
	private AutoCompleteTextView mEditNumber;
	private ImageView mButtonAdd;
	private ImageView mButtonImport;

	ContactHelperSdk5 mContactHelper;
	ContactListAdapter mContactAdapter;
	Configuration mConf;
	private ToggleButton mEnableNumbersInContact;

	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);
		setContentView(R.layout.whitelist_activity);

		mContactHelper = new ContactHelperSdk5(this);
		mConf = Configuration.getInstance();

		mEnableNumbersInContact = (ToggleButton) findViewById(R.id.btnNumbersInContacts);
		mEnableNumbersInContact.setOnClickListener(this);

		mButtonAdd = (ImageView) findViewById(R.id.btnAdd);
		mButtonImport = (ImageView) findViewById(R.id.btnImport);

		mEditNumber = (AutoCompleteTextView) findViewById(R.id.txtNumber);

		mButtonAdd.setOnClickListener(this);
		mButtonImport.setOnClickListener(this);

		mContactAdapter = new ContactListAdapter(this, mContactHelper
				.getContactCursor());
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
			ContactEntry ceContact = new ContactEntry(cursor.getString(5),
					cursor.getString(3));
			ArrayList<ContactEntry> contacts = mConf.mWhitelist.contacts;
			if (!contacts.contains(ceContact)) {
				contacts.add(ceContact);
			}
			mEditNumber.setText("");
			showBlackList();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mConf.updatePreference();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mConf.updatePreference();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mConf.loadPreference();
		showConfiguration();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNumbersInContacts:
			mConf.mEnableNumberInContacts = !mConf.mEnableNumberInContacts;
			mConf.updatePreference();
			showConfiguration();
			break;
		case R.id.btnAdd:
			String text = mEditNumber.getText().toString();
			if (!TextUtils.isEmpty(text)) {
				ContactEntry ceContact = new ContactEntry(text, text);
				ArrayList<ContactEntry> contacts = mConf.mWhitelist.contacts;
				if (!contacts.contains(ceContact)) {
					contacts.add(ceContact);
				}
				mEditNumber.setText("");
				showBlackList();
			}
			break;
		case R.id.btnImport:
			Intent i = new Intent(this, ContactPickerActivity.class);
			i.putExtra(ContactPickerActivity.SHOW_CUSTOM_TAB, false);
			startActivityForResult(i, CONTACT_PICKER_REQUEST_CODE);
			break;

		case R.id.btnRemove:
			ContactEntry ce = (ContactEntry) v.getTag();
			mConf.mWhitelist.contacts.remove(ce);
			showBlackList();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CONTACT_PICKER_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			String name = data.getStringExtra(ContactPickerActivity.NAME);
			String number = data.getStringExtra(ContactPickerActivity.NUMBER);

			if (TextUtils.isEmpty(name))
				name = number;

			if (!TextUtils.isEmpty(number)) {
				ContactEntry ceContact = new ContactEntry(name, number);
				ArrayList<ContactEntry> contacts = mConf.mWhitelist.contacts;
				if (!contacts.contains(ceContact)) {
					contacts.add(ceContact);
					mConf.updatePreference();
					showBlackList();
				}
			}
		}
	}

	private void showConfiguration() {
		showBlackList();
	}

	private void showBlackList() {
		AntiSpamNumber adapter = new AntiSpamNumber(this,
				mConf.mWhitelist.contacts);
		mEnableNumbersInContact.setChecked(mConf.mEnableNumberInContacts);
		setListAdapter(adapter);
	}

	public class AntiSpamNumber extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<ContactEntry> mData;
		Context context;

		public AntiSpamNumber(Context context, ArrayList<ContactEntry> data) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			this.mData = data;
			this.context = context;
		}

		public int getCount() {
			return mData.size();
		}

		public Object getItem(int position) {
			return position;
		}

		/**
		 * Use the array index as a unique id.
		 * 
		 * @see android.widget.ListAdapter#getItemId(int)
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * Make a view to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			CEHolder holder;

			if (convertView == null) {
				// convertView = mInflater.inflate(
				// android.R.layout.simple_list_item_2, null);
				convertView = mInflater.inflate(R.layout.ce_item, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new CEHolder();
				holder.mName = (TextView) convertView
						.findViewById(R.id.txtName);
				holder.mNumber = (TextView) convertView
						.findViewById(R.id.txtNumber);
				holder.mRemove = (ImageView) convertView
						.findViewById(R.id.btnRemove);
				convertView.setTag(holder);

			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (CEHolder) convertView.getTag();
			}
			// Bind the data efficiently with the holder.
			ContactEntry ce = mData.get(position);
			holder.mName.setText(ce.name);
			holder.mNumber.setText(ce.number);
			holder.mRemove.setTag(ce);
			holder.mRemove.setOnClickListener(WhitelistSettingActivity.this);

			return convertView;
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

		// private ContentResolver mContent;
	}

	static class CEHolder {
		TextView mName;
		TextView mNumber;
		ImageView mRemove;
	}

}
