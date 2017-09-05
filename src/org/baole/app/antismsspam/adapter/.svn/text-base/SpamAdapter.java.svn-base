package org.baole.app.antismsspam.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.baole.app.antismsspam.R;
import org.baole.app.model.LogEntry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpamAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<LogEntry> data;
	// Context context;
	public static DateFormat format = SimpleDateFormat.getInstance();

	public SpamAdapter(Context context, ArrayList<LogEntry> data) {
		// Cache the LayoutInflate to avoid asking for a new one each time.
		// this.context = context;
		mInflater = LayoutInflater.from(context);
		this.data = data;
	}

	/**
	 * The number of items in the list is determined by the number of speeches
	 * in our array.
	 * 
	 * @see android.widget.ListAdapter#getCount()
	 */
	public int getCount() {
		return data.size();
	}

	/**
	 * Since the data comes from an array, just returning the index is sufficent
	 * to get at the data. If we were using a more complex data structure, we
	 * would return whatever object represents one row in the list.
	 * 
	 * @see android.widget.ListAdapter#getItem(int)
	 */
	public Object getItem(int position) {
		return position;
	}

	/**
	 * Use the array index as a unique id.
	 * 
	 * @see android.widget.ListAdapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Make a view to hold each row.
	 * 
	 * @see android.widget.ListAdapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.history_record, null);
			holder = new ViewHolder();
			holder.mAddress = (TextView) convertView.findViewById(R.id.address);
			holder.mBody = (TextView) convertView.findViewById(R.id.body);
			holder.mCreated = (TextView) convertView.findViewById(R.id.created);
			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}

		// Bind the data efficiently with the holder.
		LogEntry viewData = data.get(position);
		// TODO format
		holder.mAddress.setText(viewData.getFriendlyName()+" ("+viewData.mCount+")");
		holder.mBody.setText(viewData.mBody);
		holder.mCreated.setText(format.format(viewData.mCreated));
		return convertView;
	}

	static class ViewHolder {
		TextView mAddress;
		TextView mCreated;
		TextView mBody;
	}
}
