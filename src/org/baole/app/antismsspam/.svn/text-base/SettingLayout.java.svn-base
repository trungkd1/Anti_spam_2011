package org.baole.app.antismsspam;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher.ViewFactory;

public class SettingLayout extends Activity implements OnItemClickListener,
		OnItemSelectedListener, OnClickListener, OnSeekBarChangeListener {
	private String[] mItemtSetting;
	private ListView mListSetting;
	private ViewFlipper mViewflipper;
	private SeekBar mseekRed;
	private SeekBar mseekGreen;
	private SeekBar mseekBlue;
	private SeekBar mseekAlpha;

	private SeekBar mseekRedIncoming;
	private SeekBar mseekGreenIncoming;
	private SeekBar mseekBlueIncoming;
	private SeekBar mseekAlphaIncoming;

	private SeekBar mseekRedOutgoing;
	private SeekBar mseekGreenOutgoing;
	private SeekBar mseekBlueOutgoing;
	private SeekBar mseekAlphaOutgoing;

	private SeekBar mseekTextSize;

	private int status = 0;
	private FrameLayout mlayoutModel;
	private LinearLayout mlayoutincoming;
	private LinearLayout mlayoutoutgoing;

	private Spinner mIncoming_font;
	private Spinner mIncoming_style;

	private TextView mtextContentIncoming;
	private TextView mtextContentOutgoing;
	private TextView textCreatedIncoming;
	private TextView textCreatedOutgoing;
	

	private static final int SELECT_BACKGROUND_LAYOUT = 1;
	private static final int SELECT_BACKGROUND_INCOMING = 2;

	private static final int SELECT_BACKGROUND_OUTGOING = 3;

	private String[] mnameTypeface = { "DEFAULT", "DEFAULT_BOLD", "MONOSPACE",
			"SANS_SERIF", "SERIF" };

	private Typeface[] mvaluesfTypeface = { Typeface.DEFAULT,
			Typeface.DEFAULT_BOLD, Typeface.MONOSPACE, Typeface.SANS_SERIF,
			Typeface.SERIF };

	private String[] mnameStyle = { "BOLD", "BOLD_ITALIC", "ITALIC", "NORMAL" };

	private int[] mvaluesStyle = { Typeface.BOLD, Typeface.BOLD_ITALIC,
			Typeface.ITALIC, Typeface.NORMAL };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_layout);
		mViewflipper = (ViewFlipper) findViewById(R.id.viewflipper);

		mlayoutModel = (FrameLayout) findViewById(R.id.layoutModel1);
		mlayoutincoming = (LinearLayout)findViewById(R.id.layoutIncoming);
		mlayoutoutgoing = (LinearLayout)findViewById(R.id.layoutOutgoing);
		mListSetting = (ListView) findViewById(R.id.listviewSetting);
		mListSetting.setOnItemClickListener(SettingLayout.this);
		
		mlayoutModel.setBackgroundResource(R.drawable.background1);
		mlayoutincoming.setBackgroundColor(R.drawable.inc1);
		
		
		
		
		
		mseekRed = (SeekBar) findViewById(R.id.seekred);
		mseekGreen = (SeekBar) findViewById(R.id.seekgreen);
		mseekBlue = (SeekBar) findViewById(R.id.seekblue);
		mseekAlpha = (SeekBar) findViewById(R.id.seekalpha);

		mseekRed.setOnSeekBarChangeListener(this);
		mseekGreen.setOnSeekBarChangeListener(this);
		mseekBlue.setOnSeekBarChangeListener(this);
		mseekAlpha.setOnSeekBarChangeListener(this);

		mseekTextSize = (SeekBar) findViewById(R.id.seekSize);
		mseekTextSize.setOnSeekBarChangeListener(this);

		mIncoming_font = (Spinner) findViewById(R.id.incoming_font);
		mIncoming_style = (Spinner) findViewById(R.id.incoming_style);
		mIncoming_font.setOnItemSelectedListener(this);

		mtextContentIncoming = (TextView) findViewById(R.id.textContentIncoming);
		mtextContentOutgoing = (TextView) findViewById(R.id.textContentOutgoing);
		textCreatedIncoming = (TextView) findViewById(R.id.textCreatedIncoming);
		textCreatedOutgoing = (TextView) findViewById(R.id.textCreatedOutgoing);

		mseekRedIncoming = (SeekBar) findViewById(R.id.seekredIncoming);
		mseekGreenIncoming = (SeekBar) findViewById(R.id.seekgreenIncoming);
		mseekBlueIncoming = (SeekBar) findViewById(R.id.seekblueIncoming);
		mseekAlphaIncoming = (SeekBar) findViewById(R.id.seekalphaIncoming);

		mseekRedIncoming.setOnSeekBarChangeListener(this);
		mseekGreenIncoming.setOnSeekBarChangeListener(this);
		mseekBlueIncoming.setOnSeekBarChangeListener(this);
		mseekAlphaIncoming.setOnSeekBarChangeListener(this);

		mseekRedOutgoing = (SeekBar) findViewById(R.id.seekredOutgoing);
		mseekGreenOutgoing = (SeekBar) findViewById(R.id.seekgreenOutgoing);
		mseekBlueOutgoing = (SeekBar) findViewById(R.id.seekblueOutgoing);
		mseekAlphaOutgoing = (SeekBar) findViewById(R.id.seekalphaOutgoing);

		mseekRedOutgoing.setOnSeekBarChangeListener(this);
		mseekGreenOutgoing.setOnSeekBarChangeListener(this);
		mseekBlueOutgoing.setOnSeekBarChangeListener(this);
		mseekAlphaOutgoing.setOnSeekBarChangeListener(this);

		Button buttonImage = (Button) findViewById(R.id.radioImage);
		
		buttonImage.setOnClickListener(this);
		findViewById(R.id.buttonImageIncoming).setOnClickListener(this);
		findViewById(R.id.buttonImageOutgoing).setOnClickListener(this);
		showModel();
		showListSetting();
		setIncomingFont();
	}

	private void setIncomingFont() {
		ArrayAdapter<String> adapterTypeface = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mnameTypeface);
		adapterTypeface
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mIncoming_font.setAdapter(adapterTypeface);

		ArrayAdapter<String> adapterStyle = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mnameStyle);
		adapterStyle
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mIncoming_style.setAdapter(adapterStyle);

	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			this.mContext = c;
		}

		@Override
		public int getCount() {
			return mThumbIds.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			i.setImageResource(mThumbIds[position]);
			i.setAdjustViewBounds(true);
			i.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			i.setBackgroundResource(R.drawable.picture_frame);

			return i;
		}

	}

	public Integer[] mThumbIds = { R.drawable.icon, R.drawable.icon };

	public static Integer[] mImageIds = { R.drawable.background1,
			R.drawable.background2 };

	private void updateBackgroundColor() {
		Log.e("COLOR", "Run");
		int red = mseekRed.getProgress();
		int green = mseekGreen.getProgress();
		int blue = mseekBlue.getProgress();
		int alpha = mseekAlpha.getProgress();

		mlayoutModel.setBackgroundColor(((alpha << 24) & 0xFF000000)
				+ ((red << 16) & 0x00FF0000) + ((green << 8) & 0x0000FF00)
				+ (blue & 0x000000FF));

	}

	private void updateBackgroundColor_Incoming() {
		Log.e("COLOR", "Run");
		int red = mseekRedIncoming.getProgress();
		int green = mseekGreenIncoming.getProgress();
		int blue = mseekBlueIncoming.getProgress();
		int alpha = mseekAlphaIncoming.getProgress();

		mlayoutincoming.setBackgroundColor(((alpha << 24) & 0xFF000000)
				+ ((red << 16) & 0x00FF0000) + ((green << 8) & 0x0000FF00)
				+ (blue & 0x000000FF));

	}

	private void updateBackgroundColor_Outgoing() {
		Log.e("COLOR", "Run");
		int red = mseekRedOutgoing.getProgress();
		int green = mseekGreenOutgoing.getProgress();
		int blue = mseekBlueOutgoing.getProgress();
		int alpha = mseekAlphaOutgoing.getProgress();

		mlayoutoutgoing.setBackgroundColor(((alpha << 24) & 0xFF000000)
				+ ((red << 16) & 0x00FF0000) + ((green << 8) & 0x0000FF00)
				+ (blue & 0x000000FF));

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			status--;
			if (status >= 0) {
				mViewflipper.setDisplayedChild(R.id.listviewSetting);

				if (mViewflipper.getDisplayedChild() == R.id.listviewSetting) {
					mViewflipper.showNext();
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressWarnings("unchecked")
	public void showListSetting() {
		Resources res = getResources();
		mItemtSetting = res.getStringArray(R.array.match_custom_option);
		mListSetting.setAdapter(new ArrayAdapter(SettingLayout.this,
				android.R.layout.simple_expandable_list_item_1, mItemtSetting));
	}

	private void showModel() {
		// ListView listviewModel = (ListView) findViewById(R.id.listviewModel);
		//
		// ArrayList<LogEntry> mListData = new ArrayList<LogEntry>();
		// LogEntry viewData1 = new LogEntry();
		// LogEntry viewData2 = new LogEntry();
		// LogEntry viewData3 = new LogEntry();
		// LogEntry viewData4 = new LogEntry();
		// viewData1.mBody = "Hi! every body";
		// viewData1.mAddress = "0911113";
		// viewData1.mSender = "abc";
		// viewData1.mCreated = new Date(System.currentTimeMillis());
		// viewData1.mType = 1;
		// viewData1.mWho = 2;
		//
		// viewData2.mBody = "I'm fine";
		// viewData2.mAddress = "0965656";
		// viewData2.mSender = "abc";
		// viewData2.mCreated = new Date(System.currentTimeMillis());
		// viewData2.mType = 1;
		// viewData2.mWho = 1;
		//
		// viewData3.mBody = "I'm fine";
		// viewData3.mAddress = "0965656";
		// viewData3.mSender = "abc";
		// viewData3.mCreated = new Date(System.currentTimeMillis());
		// viewData3.mType = 1;
		// viewData3.mWho = 2;
		//
		// viewData4.mBody = "I'm fine";
		// viewData4.mAddress = "0965656";
		// viewData4.mSender = "abc";
		// viewData4.mCreated = new Date(System.currentTimeMillis());
		// viewData4.mType = 1;
		// viewData4.mWho = 1;
		//
		// mListData.add(viewData1);
		// mListData.add(viewData2);
		// // mListData.add(viewData3);
		// // mListData.add(viewData4);
		// listviewModel.setAdapter(new SettingAdapter(this, mListData));

		mtextContentIncoming.setText("Hi! every body, how r you?");
		mtextContentOutgoing.setText("I'm fine");
		textCreatedIncoming.setText((new Date(System.currentTimeMillis()))
				.toString());
		textCreatedOutgoing.setText((new Date(System.currentTimeMillis()))
				.toString());
	}

	private void updateFrontIncoming() {
		Typeface typeface = mvaluesfTypeface[mIncoming_font
				.getSelectedItemPosition()];
		String familyName = mnameTypeface[mIncoming_font
				.getSelectedItemPosition()];
		int style = mvaluesStyle[mIncoming_style.getSelectedItemPosition()];
		String stringStyle = mnameStyle[mIncoming_style
				.getSelectedItemPosition()];

		mtextContentIncoming.setTypeface(typeface, style);

	}

	private void updateTextSize() {
		int size = mseekTextSize.getProgress();
		mtextContentIncoming.setTextSize(size);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		mViewflipper.setDisplayedChild(position);
		if (mViewflipper.getDisplayedChild() == position) {
			mViewflipper.showNext();
		}
		status++;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (view.getId()) {
		case R.id.incoming_font:
			updateFrontIncoming();
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.radioImage:
			startActivityForResult(new Intent(SettingLayout.this,
					imageSwitcher.class), SELECT_BACKGROUND_LAYOUT);
		case R.id.buttonImageIncoming:
			startActivityForResult(new Intent(SettingLayout.this,
					imageSwitcherInc.class), SELECT_BACKGROUND_INCOMING);
		case R.id.buttonImageOutgoing:
			startActivityForResult(new Intent(SettingLayout.this,
					imageSwitcherOut.class), SELECT_BACKGROUND_OUTGOING);
			break;
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.seekSize:
			updateTextSize();
			break;
		case R.id.seekalpha:
		case R.id.seekblue:
		case R.id.seekgreen:
		case R.id.seekred:
			updateBackgroundColor();
			break;
		case R.id.seekalphaIncoming:
		case R.id.seekblueIncoming:
		case R.id.seekgreenIncoming:
		case R.id.seekredIncoming:
			updateBackgroundColor_Incoming();
			break;
		case R.id.seekalphaOutgoing:
		case R.id.seekblueOutgoing:
		case R.id.seekgreenOutgoing:
		case R.id.seekredOutgoing:
			updateBackgroundColor_Outgoing();
			break;

		default:
			break;
		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		int mPos = data.getIntExtra("position", 1);
		
		if (resultCode == RESULT_OK && requestCode == SELECT_BACKGROUND_LAYOUT) {
			
			mlayoutModel.setBackgroundResource(imageSwitcher.mImageIds[mPos]);
			FileItemImage.setId_image(data.getIntExtra("position", 1));
			Log.e("Error", "abc");

		}
		if (resultCode == RESULT_OK
				&& requestCode == SELECT_BACKGROUND_INCOMING) {
			mlayoutincoming.setBackgroundResource(imageSwitcherInc.mImageIds[mPos]);
			FileItemImage.setId_image(data.getIntExtra("position", 1));
			Log.e("Error 2", "bdf");
			
		}
		if (resultCode == RESULT_OK
				&& requestCode == SELECT_BACKGROUND_OUTGOING) {
			mlayoutoutgoing.setBackgroundResource(imageSwitcherOut.mImageIds[mPos]);
			FileItemImage.setId_image(data.getIntExtra("position", 1));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// void saveconfig() {
	// SharedPreferences pref = PreferenceManager
	// .getDefaultSharedPreferences(this);
	// Editor e = pref.edit();
	// e.putInt("mImageID", 1);
	// e.commit();
	// }

}
