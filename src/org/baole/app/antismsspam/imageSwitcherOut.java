package org.baole.app.antismsspam;

import org.baole.app.antismsspam.imageSwitcher.ImageAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class imageSwitcherOut extends Activity implements OnItemSelectedListener, ViewFactory{
	private Intent intent ;
	private int mPosition;
	private ImageSwitcher mSwitcher;
	private Gallery g;
	private int Start;
	private Bundle mBunble;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageswitcher);
        
      
		intent = new Intent();
		mSwitcher = (ImageSwitcher) findViewById(R.id.imgswitcher);
		Start =0;
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

		g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(new ImageAdapter(this));
		g.setOnItemSelectedListener(this);
		g.setSelection(FileItemImage.id_image);
		
		
       
    }
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		if((FileItemImage.id_image > 0 && position == 0 && Start == 0) ){
			mSwitcher.setImageResource(mImageIds[FileItemImage.id_image]);
			Start = 1;
			g.setSelection(FileItemImage.id_image);
		}			
		else{
			 mSwitcher.setImageResource((mImageIds[position]));
			 g.setSelection(position);
		}
		    				
		mPosition =position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onBackPressed() {
		intent.putExtra("position", mPosition);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}
	
	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
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
	public static Integer[] mImageIds = {R.drawable.s1,R.drawable.s2}; 	
	
	public Integer[] mThumbIds = {   R.drawable.icon,R.drawable.icon};

	

}
