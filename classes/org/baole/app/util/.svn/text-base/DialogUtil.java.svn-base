package org.baole.app.util;

import org.baole.app.antismsspam.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class DialogUtil {

	public static final Dialog createChangLogDialog(Context context) {
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.updates_title);
		final String[] changes = context.getResources().getStringArray(
				R.array.updates);
		
		final StringBuilder buf = new StringBuilder(changes[0]);
		for (int i = 1; i < changes.length; i++) {
			buf.append("\n\n");
			buf.append(changes[i]);
		}
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setMessage(buf.toString());
		builder.setCancelable(true);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						//write current version
						dialog.cancel();
					}
				});
		return builder.create();
	}
	
	public static final Dialog createGoProDialog(final Context context,
			final String pkg) {
		AlertDialog.Builder builder;

		builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(R.string.pro_version)
				.setMessage(R.string.pro_alert)
				.setPositiveButton(R.string.purchase,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent pro = new Intent(Intent.ACTION_VIEW, Uri
										.parse(String.format(
												"market://details?id=%s", pkg)));
								context.startActivity(pro);
							}

						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								dialog.cancel();
							}
						});

		return builder.create();
	}

	
}
