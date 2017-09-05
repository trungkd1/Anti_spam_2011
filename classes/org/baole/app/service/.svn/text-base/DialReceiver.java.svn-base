package org.baole.app.service;

import org.baole.app.antismsspam.PrivateActivity;
import org.baole.app.conf.Configuration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DialReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Configuration conf = Configuration.getInstance();
		String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

		// L.l("dialing " + number);

		if (conf.mEnableDialNumber && number != null
				&& number.equals(conf.mDialNumber)) {
			setResultData(null);

			Intent confirmIntent = new Intent(context, PrivateActivity.class);
			confirmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			confirmIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, number);
			context.startActivity(confirmIntent);
		}

	}

}