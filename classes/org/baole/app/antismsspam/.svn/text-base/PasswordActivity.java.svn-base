package org.baole.app.antismsspam;

import org.baole.ad.AdmobHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PasswordActivity extends Activity implements OnClickListener {

	private EditText mCurrentPassword;
	private EditText mNewPassword;
	private EditText mConfirmPassword;
	private int mAction = ACTION_ASK_PASSWORD;
	private String mPassword;

	public static String ACTION = "action";
	public static String PASSWORD = "password";
	public final static int ACTION_ASK_PASSWORD = 0;
	public final static int ACTION_NEW_PASSWORD = 1;
	public final static int ACTION_CHANGE_PASSWORD = 2;

	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);

		setContentView(R.layout.password_activity);

		AdmobHelper  mAdHelper = new AdmobHelper(this);
		mAdHelper.setup((LinearLayout) findViewById(R.id.ad_container),
				BaseMain.AD_ID, true);
		
		mAction = getIntent().getIntExtra(ACTION, ACTION_ASK_PASSWORD);

		mCurrentPassword = (EditText) findViewById(R.id.edit_current_password);
		mNewPassword = (EditText) findViewById(R.id.edit_new_password);
		mConfirmPassword = (EditText) findViewById(R.id.edit_confirm_password);

		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(this);

		mPassword = setting.getString(PASSWORD, "");

		switch (mAction) {
		case ACTION_ASK_PASSWORD:
			findViewById(R.id.TextView02).setVisibility(View.GONE);
			findViewById(R.id.TextView03).setVisibility(View.GONE);
			mNewPassword.setVisibility(View.GONE);
			mConfirmPassword.setVisibility(View.GONE);
			break;
		case ACTION_NEW_PASSWORD:
			findViewById(R.id.TextView01).setVisibility(View.GONE);
			mCurrentPassword.setVisibility(View.GONE);
			break;
		}

		findViewById(R.id.btn_cancel).setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent data = new Intent();
		data.putExtra(ACTION, mAction);

		String currentPassword = mCurrentPassword.getText().toString();
		String newPass = mNewPassword.getText().toString();
		String confirmPass = mConfirmPassword.getText().toString();

		switch (v.getId()) {
		case R.id.btn_cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.btn_ok:
			switch (mAction) {
			case ACTION_ASK_PASSWORD:
				if (!TextUtils.isEmpty(currentPassword)
						&& currentPassword.equals(mPassword)) {
					setResult(RESULT_OK, data);
					finish();
				} else {
					Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG)
							.show();
				}
				break;
			case ACTION_NEW_PASSWORD:
				if (!TextUtils.isEmpty(newPass) && newPass.equals(confirmPass)) {
					savePassword(newPass);
					setResult(RESULT_OK, data);
					finish();
				} else {
					Toast.makeText(this, "Miss-matched password",
							Toast.LENGTH_LONG).show();
				}
				break;
			case ACTION_CHANGE_PASSWORD:
				if (TextUtils.isEmpty(currentPassword)
						|| !currentPassword.equals(mPassword)) {
					Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG)
							.show();
				} else if (TextUtils.isEmpty(newPass)
						|| !newPass.equals(confirmPass)) {
					Toast.makeText(this, "Miss-matched password",
							Toast.LENGTH_LONG).show();
				} else {
					savePassword(newPass);
					setResult(RESULT_OK, data);
					finish();
				}
				break;
			}
			break;
		}
	}

	private void savePassword(String newPass) {
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor settingsEditor = setting.edit();
		settingsEditor.putString(PASSWORD, newPass);
		settingsEditor.commit();
	}

	public static String getPassword(Context context) {
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(context);
		return setting.getString(PASSWORD, "");

	}
}
