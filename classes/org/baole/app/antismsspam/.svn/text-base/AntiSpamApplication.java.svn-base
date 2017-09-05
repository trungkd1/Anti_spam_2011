package org.baole.app.antismsspam;

import org.baole.app.conf.Configuration;

import android.app.Application;

public class AntiSpamApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Configuration conf = Configuration.getInstance();
		conf.init(getApplicationContext());
		conf.loadPreference();
	}

}
