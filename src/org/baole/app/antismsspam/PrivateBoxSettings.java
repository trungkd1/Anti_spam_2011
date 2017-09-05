package org.baole.app.antismsspam;

import org.baole.ad.AdmobHelper;
import org.baole.app.conf.Configuration;
import org.baole.app.util.DialogUtil;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class PrivateBoxSettings extends PreferenceActivity implements
		OnPreferenceClickListener {

	public static final String KEY_ENABLE = "enable";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		if (AdmobHelper.HAS_ADS) {
			findPreference(Configuration.KEY_ENABLE_DIAL_NUMBER)
					.setOnPreferenceClickListener(this);
			

		}
		findPreference(Configuration.CUSTOM_LAYOUT)
		.setOnPreferenceClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Configuration.getInstance().loadPreference();
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (Configuration.KEY_ENABLE_DIAL_NUMBER.equals(preference.getKey())) {
			DialogUtil.createGoProDialog(this, Configuration.PRO_PACKAGE)
					.show();
			((CheckBoxPreference) preference).setChecked(false);
		} else if (Configuration.CUSTOM_LAYOUT.equals(preference.getKey())) {
			startActivity(new Intent(PrivateBoxSettings.this, SettingLayout.class));
		}
		return false;
	}
}
