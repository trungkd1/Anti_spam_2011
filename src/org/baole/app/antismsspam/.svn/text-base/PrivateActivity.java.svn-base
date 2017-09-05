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
import android.net.Uri;
import android.os.Bundle;
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
import com.markupartist.android.widget.ActionBar.IntentAction;

public class PrivateActivity extends ListActivity implements
		OnItemClickListener, OnClickListener {
	private static final int DIALOG_CHANGLOGS = 1;
	private static final int PASSWORD_ACTIVITY = 0;

	private ArrayList<LogEntry> mListData;

	private Configuration mConf;

	private View mOnOffView;
	protected BetterPopupWindow mSettingPopupWindow;

	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);
		setContentView(R.layout.private_activity);

		mConf = Configuration.getInstance();

		AdmobHelper mAdHelper = new AdmobHelper(this);
		mAdHelper.setup((LinearLayout) findViewById(R.id.ad_container),
				BaseMain.AD_ID, true);

		ListView listView = getListView();
		registerForContextMenu(listView);
		listView.setOnItemClickListener(this);
		listView.setEmptyView(findViewById(R.id.empty));

		setupActionBar();

		boolean disablePassword = false;
		String dialnumber = getIntent().getStringExtra(
				Intent.EXTRA_PHONE_NUMBER);
		if (!TextUtils.isEmpty(dialnumber) && mConf.mDisablePasswordWhenHide) {
			disablePassword = true;
		}

		if (!disablePassword) {
			String pass = PasswordActivity.getPassword(this);
			Intent intent = new Intent(this, PasswordActivity.class);
			if (TextUtils.isEmpty(pass)) {
				intent.putExtra(PasswordActivity.ACTION,
						PasswordActivity.ACTION_NEW_PASSWORD);
			} else {
				intent.putExtra(PasswordActivity.ACTION,
						PasswordActivity.ACTION_ASK_PASSWORD);
			}
			startActivityForResult(intent, PASSWORD_ACTIVITY);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PASSWORD_ACTIVITY && resultCode == RESULT_CANCELED) {
			finish();
		}
	}

	private void showLogs(String number) {
		if (TextUtils.isEmpty(number)) {
			mListData = DbHelper.queryPrivateSMS(this, null, null,true);
		} else {
			mListData = DbHelper.queryPrivateSMS(this, DbHelper.NUMBER + "=?",
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Action");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.private_log_item_menu, menu);
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
			if (data != null) {
				Intent intent = new Intent(this, ComposeActivity.class);
				intent.putExtra(ComposeActivity.NAME, data.mSender);
				intent.putExtra(ComposeActivity.NUMBER, data.mAddress);
				startActivity(intent);
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
								&& e.mAddress.equals(data)) {
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
		DbHelper.deletePrivate(getApplicationContext(), data.mId);
	}

	private void deleteLog(int position, boolean all) {
		if (mListData != null && position >= 0 && position < mListData.size()) {
			LogEntry data = mListData.get(position);
			if (data != null) {
				if (all) {
					DbHelper.deletePrivateByNumber(this, data.mAddress);
				} else {
					DbHelper.deletePrivate(this, data.mId);
				}
				showLogs(null);
			}
		}
	}

	private void setupActionBar() {
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle(R.string.private_box);
		actionBar.setHomeAction(new IntentAction(this, new Intent(this,
				BaseMain.class), R.drawable.ic_home));

		final Action onoffAction = new Action() {

			@Override
			public void performAction(View view) {
				mConf.mPrivateEnable = !mConf.mPrivateEnable;
				mConf.updatePreference();
				showConfiguration();
			}

			@Override
			public int getDrawable() {
				return R.drawable.ic_onoff;
			}
		};
		mOnOffView = actionBar.addAction(onoffAction);
		showConfiguration();

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

		final Action privateAction = new Action() {

			@Override
			public int getDrawable() {
				return R.drawable.ic_compose;
			}

			@Override
			public void performAction(View view) {
				compose();
			}

		};
		actionBar.addAction(privateAction);
	}

	private void compose() {
		Intent intent = new Intent(this, ComposeActivity.class);
		startActivity(intent);
	}

	private void setupPrivateList() {
		Intent intent = new Intent(this, PrivateSettingActivity.class);
		startActivity(intent);
	}

	private void showConfiguration() {
		if (mOnOffView != null) {
			mOnOffView.setSelected(mConf.mPrivateEnable);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.help:
			onHelp();
			break;
		case R.id.menu_restore_all:
			onRestoreAll();
			break;
		case R.id.menu_delete_all:
			onDeleteAll();
			break;
		case R.id.menu_change_password:
			onChangePass();
			break;

		case R.id.comment:
			onComment();
			break;
		case R.id.donate:
			onDonate();
			break;
		}

		return true;
	}

	private void onDonate() {
		Intent donate = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=org.baole.app.asspro"));
		startActivity(donate);
	}

	private void onComment() {
		Intent donate = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=" + getPackageName()));
		Log.e("test", donate.getData().toString());
		startActivity(donate);
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
			DbHelper.deletePrivate(getApplicationContext(), data.mId);
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
			DbHelper.deletePrivate(getApplicationContext(), data.mId);
		}
		showLogs(null);
	}

	private void onHelp() {
		this.showDialog(DIALOG_CHANGLOGS);
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
					R.layout.popup_private_settings, null);
			root.findViewById(R.id.action_private_list).setOnClickListener(
					PrivateActivity.this);
			root.findViewById(R.id.action_option).setOnClickListener(
					PrivateActivity.this);
			root.findViewById(R.id.action_passwd).setOnClickListener(
					PrivateActivity.this);
			// set the inflated view as what we want to display
			this.setContentView(root);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.action_private_list:
			setupPrivateList();
			mSettingPopupWindow.dismiss();
			break;
		case R.id.action_option:
			settings();
			mSettingPopupWindow.dismiss();
			break;
		case R.id.action_passwd:
			onChangePass();
			mSettingPopupWindow.dismiss();
			break;
		}
	}

	private void settings() {
		Intent intent = new Intent(this, PrivateBoxSettings.class);
		startActivity(intent);
	}
}
