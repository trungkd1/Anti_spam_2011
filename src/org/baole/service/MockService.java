package org.baole.service;

import android.util.Log;

public class MockService extends Service {
	public MockService() {
	}

	String report = null;

	// @Override
	public String getReport() {
		return report;
	}

	// @Override
	public int init() {
		Log.e("MockService", "init()");
		return STATUS_LOGGIN_OK;
	}

	// @Override
	public int send(String number, String sms) {
		Log.e("MockService", String.format("send(%s, %s)", number, sms));
		if (Math.random() * 10 > 11)
			return STATUS_SEND_FAILED;
		return STATUS_SEND_OK;
	}

}
