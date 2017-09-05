package org.baole.app.service;

import org.baole.app.antismsspam.MessageEntry;
import org.baole.app.conf.Configuration;
import org.baole.app.db.DbHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SmsPageReceiver extends BroadcastReceiver {
	public static String TAG = "SmsPageReceiver";
	private int mWhoReceive = 1;
	@Override
	public void onReceive(Context context, Intent intent) {

		SmsMessage[] msgs = {};
		try {
			if (!intent.getExtras().isEmpty()) {
				Object[] pduObjs = (Object[]) intent.getExtras().get("pdus");
				msgs = new SmsMessage[pduObjs.length];
				for (int i = 0; i < pduObjs.length; i++) {
					msgs[i] = SmsMessage.createFromPdu((byte[]) pduObjs[i]);
					String number = msgs[i].getOriginatingAddress();
					String msg = msgs[i].getMessageBody();

					Configuration conf = Configuration.getInstance();

					boolean block = false;
					if (conf.mPrivateEnable) {
						if (conf.mPrivateBlacklist.isContains(number)) {
							block = true;
							// write log
						} else if (conf.mPrivateKeywords.isContain(msg)) {
							block = true;
						}
					}
					if (block) {
						abortBroadcast();
						DbHelper.addPrivate(context, msg, number,
								MessageEntry.TYPE_INBOX,
								System.currentTimeMillis(),mWhoReceive);
					} else {
						if (conf.mSpamEnable) {
							block = false;
							// check blacklist number
							boolean inContact = DbHelper.contactExists(context,
									number);
							if (conf.mWhitelist.isContains(number)
									|| (inContact && conf.mEnableNumberInContacts)) {
								block = false;
							} else if (conf.mBlockUnknownNumber && !inContact) {
								block = true;
							} else if (conf.mSpamBlacklist.isContains(number)) {
								block = true;
								// write log
							} else if (conf.mSpamKeywords.isContain(msg)) {
								block = true;
							}

							if (block) {
								abortBroadcast();
								DbHelper.addHistory(context, msg, number,
										MessageEntry.TYPE_INBOX,
										System.currentTimeMillis());
							}
						}
					}

				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
