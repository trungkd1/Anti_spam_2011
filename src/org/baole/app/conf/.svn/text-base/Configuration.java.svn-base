package org.baole.app.conf;

import java.util.ArrayList;
import java.util.HashMap;

import org.baole.app.model.ContactEntry;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

public class Configuration {

	private static Configuration instance = null;
	public static final int MATCH_BASE = 0;
	public static final int MATCH_REGULAR = MATCH_BASE;
	public static final int MATCH_EXACT = MATCH_BASE + 1;
	public static final int MATCH_START_WITH = MATCH_BASE + 2;
	public static final int MATCH_END_WITH = MATCH_BASE + 3;
	public static final int MATCH_CONTAIN = MATCH_BASE + 4;

	private Configuration() {

	}

	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	public void init(Context c) {
		context = c;
		loadPreference();
	}

	static class MyMessage {
		String sender;
		String body;
		int count = 0;
	}

	public boolean mSpamEnable;
	public boolean mBlockUnknownNumber;
	public boolean mSpamHelp;
	public Keywords mSpamKeywords;
	public BlackList mSpamBlacklist;

	public boolean mPrivateEnable;
	public boolean mPrivateHelp;
	public Keywords mPrivateKeywords;
	public BlackList mPrivateBlacklist;
	public BlackList mWhitelist;
	public boolean mHidePrivateBoxButton;
	public boolean mEnableDialNumber;
	public String mDialNumber;
	public boolean mDisablePasswordWhenHide;

	public boolean mIgnoreCase = true;
	private Context context;

	private static final String SPAM_ENABLE = "SPAM_ENABLE2";
	private static final String SPAM_BLOCK_UNKNOWN_NUMBER = "SPAM_BLOCK_UNKNOWN_NUMBER";
	private static final String SPAM_WL_ENABLE_IN_CONTACTS = "SPAM_WLeic";

	private static final String SPAM_HELP = "SPAM_HELP";
	private static final String SPAM_KEYWORDS = "SPAM_KEYWORDS";
	private static final String SPAM_BLACKLIST = "SPAM_BLACKLIST";

	private static final String PRIVATE_ENABLE = "PRIVATE_ENABLE2";
	private static final String PRIVATE_HELP = "PRIVATE_HELP";
	private static final String PRIVATE_KEYWORDS = "PRIVATE_KEYWORDS";
	private static final String PRIVATE_BLACKLIST = "PRIVATE_BLACKLIST";
	private static final String WHITELIST = "_wl";
	private static final String KEY_HIDE_PRIVATE_BOX_BUTTON = "hide_private_box_button";
	private static final String KEY_DIAL_NUMBER = "dial_number";
	private static final String KEY_DISABLE_PASSWORD = "disable_password";
	public static final String KEY_ENABLE_DIAL_NUMBER = "enable_dial";
	public static final String PRO_PACKAGE = "org.baole.app.asspro";
	public static final String CUSTOM_LAYOUT = "custom_layout";

	public boolean mDuplicateSMSEnable = false;
	// public boolean mAdvanced = false;
	public HashMap<String, MyMessage> mNumberToLastMessage = new HashMap<String, MyMessage>();
	public boolean mEnableNumberInContacts = false;

	// private static final String ADVANCED = "ADVANCED";
	// private static final String HELP = "HELP";

	public boolean checkAndUpdateSpamSMS(String key, String msg) {
		MyMessage value = null;
		if (mNumberToLastMessage.containsKey(key)) {
			value = mNumberToLastMessage.get(key);
			if (value.body.equalsIgnoreCase(msg)) {
				value.count++;
				return true;
			}
		}

		if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(msg)) {
			value = new MyMessage();
			value.sender = key;
			value.body = msg;
			value.count = 1;
			mNumberToLastMessage.put(value.sender, value);
		}

		return false;
	}

	public void updatePreference() {
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor settingsEditor = setting.edit();
		// settingsEditor.putBoolean(ENABLE, mEnable);
		// settingsEditor.putBoolean(ADVANCED, mAdvanced);
		// settingsEditor.putBoolean(HELP, mHelp);

		settingsEditor.putBoolean(SPAM_ENABLE, mSpamEnable);
		settingsEditor.putBoolean(SPAM_BLOCK_UNKNOWN_NUMBER,
				mBlockUnknownNumber);
		settingsEditor.putBoolean(SPAM_WL_ENABLE_IN_CONTACTS,
				mEnableNumberInContacts);

		settingsEditor.putBoolean(SPAM_HELP, mSpamHelp);
		settingsEditor.putString(SPAM_KEYWORDS, mSpamKeywords.toPrefString());
		settingsEditor.putString(SPAM_BLACKLIST, mSpamBlacklist.toPrefString());

		settingsEditor.putBoolean(PRIVATE_ENABLE, mPrivateEnable);
		settingsEditor.putBoolean(PRIVATE_HELP, mPrivateHelp);
		settingsEditor.putString(PRIVATE_KEYWORDS,
				mPrivateKeywords.toPrefString());
		settingsEditor.putString(PRIVATE_BLACKLIST,
				mPrivateBlacklist.toPrefString());
		settingsEditor.putString(WHITELIST, mWhitelist.toPrefString());

		settingsEditor.commit();
	}

	public void loadPreference() {
		if (context == null)
			return;
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(context);
		// mEnable = setting.getBoolean(ENABLE, false);
		// mAdvanced = setting.getBoolean(ADVANCED, false);
		// mHelp = setting.getBoolean(HELP, true);
		mSpamEnable = setting.getBoolean(SPAM_ENABLE, false);
		mBlockUnknownNumber = setting.getBoolean(SPAM_BLOCK_UNKNOWN_NUMBER,
				false);
		mEnableNumberInContacts = setting.getBoolean(
				SPAM_WL_ENABLE_IN_CONTACTS, false);

		mSpamHelp = setting.getBoolean(SPAM_HELP, false);
		mSpamKeywords = new Keywords();
		mSpamKeywords.loadPrefString(setting.getString(SPAM_KEYWORDS, null));
		mSpamBlacklist = new BlackList();
		mSpamBlacklist.loadPrefString(setting.getString(SPAM_BLACKLIST, null));

		mPrivateEnable = setting.getBoolean(PRIVATE_ENABLE, false);
		mPrivateHelp = setting.getBoolean(PRIVATE_HELP, false);
		mPrivateKeywords = new Keywords();
		mPrivateKeywords.loadPrefString(setting.getString(PRIVATE_KEYWORDS,
				null));
		mPrivateBlacklist = new BlackList();
		mPrivateBlacklist.loadPrefString(setting.getString(PRIVATE_BLACKLIST,
				null));
		mWhitelist = new BlackList();
		mWhitelist.loadPrefString(setting.getString(WHITELIST, null));

		mHidePrivateBoxButton = setting.getBoolean(KEY_HIDE_PRIVATE_BOX_BUTTON,
				false);
		mDialNumber = setting.getString(KEY_DIAL_NUMBER, "888");
		mDisablePasswordWhenHide = setting.getBoolean(KEY_DISABLE_PASSWORD,
				false);
		mEnableDialNumber = setting.getBoolean(KEY_ENABLE_DIAL_NUMBER, false);

	}

	public class Keywords {
		public ArrayList<String> keywords = new ArrayList<String>();

		public boolean isContain(String msg) {
			if (TextUtils.isEmpty(msg))
				return false;

			if (Configuration.getInstance().mIgnoreCase) {
				msg = msg.toLowerCase();
			}
			for (String word : keywords) {
				if (!TextUtils.isEmpty(word) && msg.contains(word)) {
					return true;
				}
			}
			return false;
		}

		public String toPrefString() {
			StringBuffer buff = new StringBuffer();
			for (String word : keywords) {
				buff.append(word).append(";");
			}

			return buff.toString();
		}

		public void loadPrefString(String s) {
			keywords.clear();
			if (TextUtils.isEmpty(s))
				return;

			if (Configuration.getInstance().mIgnoreCase) {
				s = s.toLowerCase();
			}

			String words[] = s.split(";");
			for (String word : words) {
				keywords.add(word);
			}
		}
	}

	public class BlackList {
		public ArrayList<ContactEntry> contacts = new ArrayList<ContactEntry>();

		public boolean isContains(String number) {
			if (TextUtils.isEmpty(number))
				return false;
			for (ContactEntry contact : contacts) {

				switch (contact.type) {
				case Configuration.MATCH_REGULAR:
				case Configuration.MATCH_EXACT:
					if (number.equalsIgnoreCase(contact.number)
							|| PhoneNumberUtils.compare(number, contact.number)) {
						return true;

					}
					break;
				case Configuration.MATCH_START_WITH:
					return number.startsWith(contact.number);
				case Configuration.MATCH_END_WITH:
					return number.endsWith(contact.number);
				case Configuration.MATCH_CONTAIN:
					return number.contains(contact.number);
				}
			}
			return false;
		}

		public String toPrefString() {
			StringBuffer buff = new StringBuffer();
			for (ContactEntry contact : contacts) {
				buff.append(contact.name).append(";;;");
				buff.append(contact.number).append(";;;");
				buff.append(contact.type).append("###");
			}

			return buff.toString();
		}

		public void loadPrefString(String s) {
			contacts.clear();
			if (TextUtils.isEmpty(s))
				return;

			String words[] = s.split("###");
			for (String word : words) {

				if (!TextUtils.isEmpty(word)) {
					String contact[] = word.split(";;;");
					if (contact.length > 1) {
						ContactEntry ce = new ContactEntry(contact[0],
								contact[1]);
						int type = 0;
						if (contact.length > 2) {
							try {
								type = Integer.parseInt(contact[2]);
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
						ce.type = type;
						contacts.add(ce);
					}
				}
			}
		}
	}

}
