package org.baole.app.antismsspam;

import java.util.ArrayList;

import org.baole.ad.AdmobHelper;
import org.baole.app.antismsspam.adapter.SpamAdapter;
import org.baole.app.conf.Configuration;
import org.baole.app.db.DbHelper;
import org.baole.app.model.LogEntry;
import org.baole.app.util.DialogUtil;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.anttek.quickactions.BetterPopupWindow;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class BaseMain extends ListActivity implements OnClickListener,
		OnItemClickListener {
	private static final int DIALOG_CHANGLOGS = 1;
	private static final String PREFS_LAST_RUN = "lastrun";

	private ArrayList<LogEntry> mListData;
	private Configuration mConf;
	protected SettingPopupWindow mSettingPopupWindow;
	private View mOnOffView;

	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);
		setContentView(R.layout.main_activity);
		mConf = Configuration.getInstance();

		setupActionBar();

		AdmobHelper mAdHelper = new AdmobHelper(this);
		mAdHelper.setup((LinearLayout) findViewById(R.id.ad_container),
				BaseMain.AD_ID, true);

		ListView listView = getListView();
		registerForContextMenu(listView);
		listView.setOnItemClickListener(this);
		listView.setEmptyView(findViewById(R.id.empty));

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		final String v0 = preferences.getString(PREFS_LAST_RUN, "");
		final String v1 = this.getString(R.string.app_version);
		if (!v0.equals(v1)) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(PREFS_LAST_RUN, v1);
			editor.commit();
			this.showDialog(DIALOG_CHANGLOGS);
		}		
	}
	
	public final void timerAlert(){ 

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
               
            }
        }, 1000);

    }

	private void setupActionBar() {
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		// actionBar.setHomeAction(new IntentAction(this, createIntent(this),
		// R.drawable.ic_title_home_demo));
		actionBar.setTitle(R.string.app_name);

		final Action onoffAction = new Action() {

			@Override
			public void performAction(View view) {
				mConf.mSpamEnable = !mConf.mSpamEnable;
				mConf.updatePreference();
				showConfiguration();
				view.setSelected(mConf.mSpamEnable);
				Toast.makeText(getApplicationContext(),
						mConf.mSpamEnable ? R.string.app_on : R.string.app_off,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public int getDrawable() {
				return R.drawable.ic_onoff;
			}
		};
		mOnOffView = actionBar.addAction(onoffAction);
		mOnOffView.setSelected(mConf.mSpamEnable);

		final Action settingsAction = new Action() {

			@Override
			public int getDrawable() {
				return R.drawable.ic_setting;
			}

			@Override
			public void performAction(View view) {
				mSettingPopupWindow = new SettingPopupWindow(view);
				mSettingPopupWindow.showLikePopDownMenu();
			}

		};
		actionBar.addAction(settingsAction);

		if (!(mConf.mEnableDialNumber && mConf.mHidePrivateBoxButton)) {
			final Action privateAction = new Action() {

				@Override
				public int getDrawable() {
					return R.drawable.ic_private;
				}

				@Override
				public void performAction(View view) {
					onPrivate();
				}

			};
			actionBar.addAction(privateAction);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Action");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.log_item_menu, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.menu_reply_this:
			reply(info.position);
			break;
		case R.id.menu_call_this:
			call(info.position);
			break;
		case R.id.menu_delete_this:
			deleteLog(info.position, false);
			break;
		case R.id.menu_delete_all:
			deleteLog(info.position, true);
			break;
		case R.id.menu_show_all:
			showLogsForNumber(info.position);
			break;
		case R.id.menu_restore_this:
			restoreLog(info.position, false);
			break;
		case R.id.menu_restore_all:
			restoreLog(info.position, true);
			break;
		default:
		}
		return super.onContextItemSelected(item);
	}

	private void reply(int position) {
		if (mListData != null && position >= 0 && position < mListData.size()) {
			LogEntry data = mListData.get(position);
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"
						+ data.mAddress));
				intent.putExtra("sms_body", "");
				intent.putExtra("address", data.mAddress);
				intent.setType("vnd.android-dir/mms-sms");
				startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(this, "Failed to invoke sms service",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void call(int position) {
		if (mListData != null && position >= 0 && position < mListData.size()) {
			LogEntry data = mListData.get(position);
			if (data != null) {
				try {
					Intent intent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:" + data.mAddress));
					startActivity(intent);
				} catch (Exception e) {
					Toast.makeText(this,
							"Failed to invoke call to " + data.mAddress,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private void onChangePass() {
		String pass = PasswordActivity.getPassword(this);

		Intent intent = new Intent(this, PasswordActivity.class);

		if (TextUtils.isEmpty(pass)) {
			intent.putExtra(PasswordActivity.ACTION,
					PasswordActivity.ACTION_NEW_PASSWORD);
		} else {
			intent.putExtra(PasswordActivity.ACTION,
					PasswordActivity.ACTION_CHANGE_PASSWORD);
		}
		startActivity(intent);
	}

	public static final String AD_ID = "a14da5b4d8c3676";

	private void showLogs(String number) {
		if (TextUtils.isEmpty(number)) {
			mListData = DbHelper.querySpams(this, null, null,true);
		} else {
			mListData = DbHelper.querySpams(this, DbHelper.NUMBER + "=?",
					new String[] { number },true);
		}
		SpamAdapter adapter = new SpamAdapter(this, mListData);
		setListAdapter(adapter);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putStringArrayList(CONTACT, contacts);
		mConf.updatePreference();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mConf.updatePreference();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mConf.loadPreference();
		showConfiguration();
		showLogs(null);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_CHANGLOGS:
			return DialogUtil.createChangLogDialog(this);
		default:
			return null;
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.action_blacklist:
			setting();
			mSettingPopupWindow.dismiss();
			break;
		case R.id.action_whitelist:
			setupWhitelist();
			mSettingPopupWindow.dismiss();
			break;
		case R.id.action_passwd:
			onChangePass();
			mSettingPopupWindow.dismiss();
			break;
		//
		// case R.id.btnEnable:
		// configuration.mSpamEnable = !configuration.mSpamEnable;
		// configuration.updatePreference();
		// showConfiguration();
		// break;
		// case R.id.btnSeting:
		// setting();
		// break;
		// case R.id.btnPrivate:
		// onPrivate();
		// break;
		}
	}

	private void setupWhitelist() {
		if (AdmobHelper.HAS_ADS) {
			DialogUtil.createGoProDialog(this, Configuration.PRO_PACKAGE).show();
		} else {
			Intent intent = new Intent(this, WhitelistSettingActivity.class);
			startActivity(intent);
		}
	}

	private void onPrivate() {

		Intent intent = new Intent(this, PrivateActivity.class);
		startActivity(intent);
	}

	private void showLogsForNumber(int position) {
		if (mListData != null && position >= 0 && position < mListData.size()) {
			LogEntry data = mListData.get(position);
			if (data != null) {
				showLogs(data.mAddress);
			}
		}
	}

	private void restoreLog(int position, boolean all) {
		if (mListData != null && position >= 0 && position < mListData.size()) {
			LogEntry data = mListData.get(position);
			if (data != null) {
				if (all) {
					for (LogEntry e : mListData) {
						if (!TextUtils.isEmpty(e.mAddress)
								&& e.mAddress.equals(data.mAddress)) {
							restoreLog(e);
						}
					}
				} else {
					restoreLog(data);
				}
				showLogs(null);
			}
		}
	}

	private void restoreLog(LogEntry data) {
		DbHelper.restoreMessage(getApplicationContext(), data);
		DbHelper.deleteHistory(getApplicationContext(), data.mId);
	}

	private void deleteLog(int position, boolean all) {
		if (mListData != null && position >= 0 && position < mListData.size()) {
			LogEntry data = mListData.get(position);
			if (data != null) {
				if (all) {
					DbHelper.deleteHistoryByNumber(this, data.mAddress);
				} else {
					DbHelper.deleteHistory(this, data.mId);
				}
				showLogs(null);
			}
		}
	}

	private void setting() {
		Intent intent = new Intent(this, BlacklistActivity.class);
		startActivity(intent);
	}

	private void showConfiguration() {
		if (mOnOffView != null) {
			mOnOffView.setSelected(mConf.mSpamEnable);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		if (!AdmobHelper.HAS_ADS) {
			menu.findItem(R.id.donate).setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.comment:
			onComment();
			break;
		case R.id.donate:
			onDonate();
			break;
		case R.id.menu_restore_all:
			onRestoreAll();
			break;
		case R.id.menu_delete_all:
			onDeleteAll();
			break;
		case R.id.help:
			onHelp();
			break;
		case R.id.menu_change_password:
			onChangePass();
			break;
		}

		return true;
	}

	private void onRestoreAll() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.restore_yes_all)
				.setCancelable(false)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								restoreAll();
							}
						})
				.setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.create().show();
	}

	protected void restoreAll() {
		int count = mListData.size();
		for (int i = 0; i < count; i++) {
			LogEntry data = mListData.get(i);
			DbHelper.restoreMessage(this, data);
			DbHelper.deleteHistory(this, data.mId);
		}
		showLogs(null);
	}

	private void onDeleteAll() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.confirme_yes_all)
				.setCancelable(false)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								deleteAll();
							}
						})
				.setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.create().show();
	}

	protected void deleteAll() {
		int count = mListData.size();
		for (int i = 0; i < count; i++) {
			LogEntry data = mListData.get(i);
			DbHelper.deleteHistory(this, data.mId);
		}
		showLogs(null);
	}

	private void onDonate() {
		Intent donate = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=org.baole.app.asspro"));
		startActivity(donate);
	}

	private void onHelp() {
		this.showDialog(DIALOG_CHANGLOGS);
	}

	private void onComment() {
		Intent donate = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=" + getPackageName()));
		Log.e("test", donate.getData().toString());
		startActivity(donate);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int position,
			long arg3) {
		final int start = adapterView.getFirstVisiblePosition();
		final int index = position - start;
		final View childView = adapterView.getChildAt(index);
		if (childView != null) {
			adapterView.showContextMenuForChild(childView);
		}
	}

	/**
	 * Extends {@link BetterPopupWindow}
	 * <p>
	 * Overrides onCreate to create the view and register the button listeners
	 * 
	 * @author qbert
	 * 
	 */
	private class SettingPopupWindow extends BetterPopupWindow {
		public SettingPopupWindow(View anchor) {
			super(anchor);
		}

		@Override
		protected void onCreate() {
			// inflate layout
			LayoutInflater inflater = (LayoutInflater) this.anchor.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			ViewGroup root = (ViewGroup) inflater.inflate(
					R.layout.popup_settings, null);
			root.findViewById(R.id.action_blacklist).setOnClickListener(
					BaseMain.this);
			root.findViewById(R.id.action_whitelist).setOnClickListener(
					BaseMain.this);
			root.findViewById(R.id.action_passwd).setOnClickListener(
					BaseMain.this);
			// set the inflated view as what we want to display
			this.setContentView(root);
		}
	}

}
