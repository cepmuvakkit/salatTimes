package com.cepmuvakkit.times;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.IOException;

public class LatLong extends Activity {

	AlertDialog alert;
	String lang = "AR";

	public void onCreate(Bundle var1) {
		super.onCreate(var1);
		this.requestWindowFeature(1);
		final View latlongLayout = ((LayoutInflater) this
				.getSystemService("layout_inflater")).inflate(R.layout.lat_long, null);
		this.lang = "EN";
		TextView latitudeText = (TextView) latlongLayout.findViewById(R.id.editText1);
		TextView longitudeText = (TextView) latlongLayout.findViewById(R.id.editText2);
		String var6;
		String var7;
		String var8;

		latitudeText.setText("Latitude:");
		longitudeText.setText("Longitude:");
		var6 = "Set";
		var7 = "Cancel";
		var8 = "Location";

		Builder var9 = new Builder(this);
		var9.setView(latlongLayout);
		var9.setCancelable(true).setTitle(var8)
				.setPositiveButton(var6, new OnClickListener() {
					public void onClick(DialogInterface var1, int var2) {
						EditText var3x = (EditText) latlongLayout
								.findViewById(R.id.editText1);
						EditText var4 = (EditText) latlongLayout
								.findViewById(R.id.editText2);
						String var5 = var3x.getText().toString();
						String var6 = var4.getText().toString();
						LatLong.this.setLoc(var5, var6);
					}
				}).setNegativeButton(var7, new OnClickListener() {
					public void onClick(DialogInterface var1, int var2) {
						var1.cancel();
						LatLong.this.finish();
					}
				}).setOnCancelListener(new OnCancelListener() {
					public void onCancel(DialogInterface var1) {
						LatLong.this.finish();
					}
				});
		this.alert = var9.create();
		this.alert.show();
	}

	public void onPause() {
		super.onPause();
		if (this.alert != null) {
			this.alert.dismiss();
		}

	}

	public void onResume() {
		super.onResume();
		if (this.alert != null) {
			this.alert.show();
		}

	}

	public void setLoc(String var1, String var2) {
		String var3 = var1 + "\n" + var2;
		String var4;
		String var5;

		var4 = "Location has been set";
		var5 = "Location wasn\'t enterd properly";

		if (var1 != null && var2 != null && !var1.equals("")
				&& !var2.equals("")) {
			Editor var7 = PreferenceManager.getDefaultSharedPreferences(this)
					.edit();
			var7.putString("lastLat", var1);
			var7.putString("lastLon", var2);
			var7.putString("lastLocName", "");
			var7.commit();

			try {
				FileOutputStream var14 = this.openFileOutput("lastFix.pray", 0);
				var14.write(var3.getBytes());
				var14.close();
			} catch (IOException var15) {
				var15.printStackTrace();
			}

			Toast var13 = Toast.makeText(this.getApplicationContext(), var4, 0);
			var13.setGravity(17, 0, 0);
			var13.show();
			this.finish();
		} else {
			Toast var6 = Toast.makeText(this.getApplicationContext(), var5, 0);
			var6.setGravity(17, 0, 0);
			var6.show();
			this.finish();
		}
	}
}
