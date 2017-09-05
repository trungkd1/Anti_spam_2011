package org.baole.service;

import java.util.ArrayList;

import android.telephony.SmsManager;
import android.text.TextUtils;

public class SMSService extends Service {
	int status;
	String message;
	String username;
	String password;

	// @Override
	public int send(String number, String sms) {
		try {
			if (splitLongSMS && sms.length() > 160) {
				ArrayList<String> smss = smsManager.divideMessage(sms);
				smsManager.sendMultipartTextMessage(number, null, smss, null,
						null);
			} else {
				smsManager.sendTextMessage(number, null, sms, sentPI,
						deliveredPI);
			}
		} catch (Throwable e) {
			if (sms.length() > 160) {
				message = "Message is too long";
			} else if (e == null || TextUtils.isEmpty(e.getMessage())) {
				message = "Unknown";
			} else {
				message = "" + e.getMessage();
			}
			return STATUS_SEND_FAILED;
		}
		return STATUS_SEND_OK;
	}

	// @Override
	public String getReport() {
		return message;
	}

	SmsManager smsManager;

	// @Override
	public int init() {
		smsManager = SmsManager.getDefault();
		return STATUS_LOGGIN_OK;
	}
}
