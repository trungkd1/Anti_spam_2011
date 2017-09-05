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

public class BlacklistActivity extends ListActivity implements OnClickListener {

	private static final int CONTACT_PICKER_REQUEST_CODE = 1;
	private ToggleButton mBlockUnknownNumber;
	private EditText mEditKeywords;
	private AutoCompleteTextView mEditNumber;
	private ImageView mButtonAdd;
	private ImageView mButtonImport;

	ContactHelperSdk5 mContactHelper;
	ContactListAdapter mContactAdapter;
	Configuration mConf;

	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);
		setContentView(R.layout.blacklist_activity);
		AdmobHelper mAdHelper = new AdmobHelper(this);
		mAdHelper.setup((LinearLayout) findViewById(R.id.ad_container),
				BaseMain.AD_ID, true);

		mContactHelper = new ContactHelperSdk5(this);
		mConf = Configuration.getInstance();

		mBlockUnknownNumber = (ToggleButton) findViewById(R.id.btnUnknownNumber);
		mButtonAdd = (ImageView) findViewById(R.id.btnAdd);
		mButtonImport = (ImageView) findViewById(R.id.btnImport);

		mEditKeywords = (EditText) findViewById(R.id.txtKeywords);
		mEditNumber = (AutoCompleteTextView) findViewById(R.id.txtNumber);

		mBlockUnknownNumber.setOnClickListener(this);
		mButtonAdd.setOnClickListener(this);
		mButtonImport.setOnClickListener(this);

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
			ContactEntry ceContact = new ContactEntry(cursor.getString(5),
					cursor.getString(3));
			ArrayList<ContactEntry> contacts = mConf.mSpamBlacklist.contacts;
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
		// outState.putStringArrayList(CONTACT, contacts);
		mConf.mSpamKeywords.loadPrefString(mEditKeywords.getText().toString());
		mConf.updatePreference();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mConf.mSpamKeywords.loadPrefString(mEditKeywords.getText().toString());
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
		case R.id.btnUnknownNumber:
			mConf.mBlockUnknownNumber = !mConf.mBlockUnknownNumber;
			mConf.updatePreference();
			showConfiguration();
			break;
		case R.id.btnAdd:
			String text = mEditNumber.getText().toString();
			if (!TextUtils.isEmpty(text)) {
				ContactEntry ceContact = new ContactEntry(text, text);
				ArrayList<ContactEntry> contacts = mConf.mSpamBlacklist.contacts;
				if (!contacts.contains(ceContact)) {
					contacts.add(ceContact);
				}
				mEditNumber.setText("");
				showBlackList();
			}
			break;
		case R.id.btnImport:
			Intent i = new Intent(this, ContactPickerActivity.class);
			i.putExtra(ContactPickerActivity.SHOW_CUSTOM_TAB, true);
			startActivityForResult(i, CONTACT_PICKER_REQUEST_CODE);
			break;
		case R.id.btnRemove:
			ContactEntry ce = (ContactEntry) v.getTag();
			mConf.mSpamBlacklist.contacts.remove(ce);
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
			int type = data.getIntExtra(ContactPickerActivity.TYPE,
					Configuration.MATCH_REGULAR);

			if (TextUtils.isEmpty(name))
				name = number;

			if (!TextUtils.isEmpty(number)) {
				ContactEntry ceContact = new ContactEntry(name, number);
				ArrayList<ContactEntry> contacts = mConf.mSpamBlacklist.contacts;
				if (!contacts.contains(ceContact)) {
					ceContact.type = type;
					contacts.add(ceContact);

					mConf.updatePreference();
					showBlackList();
				}
			}
		}
	}

	private void showConfiguration() {
		mBlockUnknownNumber.setChecked(mConf.mBlockUnknownNumber);
		mEditKeywords.setText(mConf.mSpamKeywords.toPrefString());

		showBlackList();
	}

	private void showBlackList() {
		AntiSpamNumber adapter = new AntiSpamNumber(this,
				mConf.mSpamBlacklist.contacts);
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
				holder.mType = (TextView) convertView
						.findViewById(R.id.txtType);
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
			holder.mType.setText(formatType(ce.type));
			holder.mRemove.setTag(ce);
			holder.mRemove.setOnClickListener(BlacklistActivity.this);

			return convertView;
		}

		private int formatType(int type) {
			switch (type) {
			case Configuration.MATCH_REGULAR:
			case Configuration.MATCH_EXACT:
				return R.string.exact_match;
			case Configuration.MATCH_START_WITH:
				return R.string.starts_with;
			case Configuration.MATCH_END_WITH:
				return R.string.ends_with;
			case Configuration.MATCH_CONTAIN:
				return R.string.contains;
			}
			return R.string.exact_match;
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

	static class CEHolder {
		TextView mName;
		TextView mNumber;
		TextView mType;

		ImageView mRemove;
	}

}
