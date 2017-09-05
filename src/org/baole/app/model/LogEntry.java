package org.baole.app.model;

import java.util.Date;

import android.text.TextUtils;

public class LogEntry {

	public String mId;
	public String mAddress;
	public String mSender;
	public String mBody;
	public int mType;
	public Date mCreated;
	public boolean mSelected = false;
	public String mCount;
	public int mWho;
	public String getFriendlyName() {
		if (TextUtils.isEmpty(mSender)) {
			return mAddress;
		} else {
			return mSender;
		}
	}
}
