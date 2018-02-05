package com.cepmuvakkit.times.search;

import java.text.DecimalFormat;
import java.util.Calendar;

import com.cepmuvakkit.times.R;
import com.cepmuvakkit.times.SalatTimesMainActivity;
import com.cepmuvakkit.times.Schedule;
import com.cepmuvakkit.times.VARIABLE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class LatLongConfirm extends Activity {
	private double mLatitude, mLongitude, mTimeZone;
	private String CountryStr, CityStr, LatitudeStr, LongitudeStr, TimeZoneStr;
	private boolean isSystemTimeZone=true;
	private DecimalFormat twoDigitFormat = new DecimalFormat("#0.00°");
	private DecimalFormat timezonefmt = new DecimalFormat("GMT+#.0;GMT-#.0");
	AlertDialog alert;
	//TextView country, city, latitude, longitude, timezone;

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(1);
		final View latlongLayout = ((LayoutInflater) this
				.getSystemService("layout_inflater")).inflate(
				R.layout.lat_long_confirm, null);
		Calendar now = Calendar.getInstance();
		mTimeZone = now.getTimeZone().getOffset(now.getTimeInMillis()) / 3600000;

		Uri uri = getIntent().getData();
		Cursor cursor = managedQuery(uri, null, null, null, null);

		if (cursor == null) {
			finish();
		} else {
			cursor.moveToFirst();

			TextView country = (TextView) latlongLayout.findViewById(R.id.country);
			TextView city = (TextView) latlongLayout.findViewById(R.id.city);
			TextView latitude = (TextView) latlongLayout.findViewById(R.id.latitude);
			TextView longitude = (TextView) latlongLayout.findViewById(R.id.longitude);
			TextView timezone = (TextView) latlongLayout.findViewById(R.id.timezone);

			int wIndex = cursor
					.getColumnIndexOrThrow(LocationDatabase.KEY_SEARCH);
			int dIndex = cursor
					.getColumnIndexOrThrow(LocationDatabase.KEY_ORDER);
			// int dCountry =
			// cursor.getColumnIndexOrThrow(LocationDatabase.KEY_COUNTRY);
			// int dCity =
			// cursor.getColumnIndexOrThrow(LocationDatabase.KEY_CITY);

			CountryStr = cursor.getString(cursor
					.getColumnIndexOrThrow("Country"));
			CityStr = cursor.getString(cursor.getColumnIndexOrThrow("City"));
			LatitudeStr = cursor.getString(cursor
					.getColumnIndexOrThrow("Latitude"));
			mLatitude = Double.parseDouble(LatitudeStr);
			LongitudeStr = cursor.getString(cursor
					.getColumnIndexOrThrow("Longitude"));
			mLongitude = Double.parseDouble(LongitudeStr);
			if (!isSystemTimeZone) 
			{TimeZoneStr = cursor.getString(cursor
					.getColumnIndexOrThrow("TimeZone"));
			mTimeZone = Double.parseDouble(TimeZoneStr);}

			country.setText(CountryStr);
			city.setText(CityStr);
			latitude.setText(twoDigitFormat.format(mLatitude)
					+ (mLatitude > 0 ? this.getString(R.string.N) :this.getString(R.string.S)));
			longitude.setText(twoDigitFormat.format(mLongitude)
					+ (mLongitude > 0 ? this.getString(R.string.E) : this.getString(R.string.W)));
			timezone.setText(timezonefmt.format(mTimeZone));

			Builder var9 = new Builder(this);
			var9.setView(latlongLayout);
			var9.setCancelable(true)
					.setTitle(R.string.confirmtitle)
					.setPositiveButton(R.string.confirm, new OnClickListener() {
						public void onClick(DialogInterface var1, int var2) {
							LatLongConfirm.this.setLoc(LatitudeStr,
									LongitudeStr, mTimeZone+"", CityStr,
									CountryStr);
						}
					})
					.setNegativeButton(R.string.cancel, new OnClickListener() {
						public void onClick(DialogInterface var1, int var2) {
							var1.cancel();
							LatLongConfirm.this.finish();
						}
					}).setOnCancelListener(new OnCancelListener() {
						public void onCancel(DialogInterface var1) {
							LatLongConfirm.this.finish();
						}
					});
			this.alert = var9.create();
			this.alert.show();

		}

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



	public void setLoc(String latitudeStr, String longitudeStr,
			String timezoneStr, String customCityStr, String countryStr) {

		if (latitudeStr != null && longitudeStr != null
				&& !latitudeStr.equals("") && !longitudeStr.equals("")) {

			SharedPreferences.Editor editor = VARIABLE.settings.edit();

			editor.putString("customCity", customCityStr);
			editor.putString("latitude", latitudeStr);
			editor.putString("longitude", longitudeStr);
			editor.putString("timezone", timezoneStr);
			editor.putString("country", countryStr);

			editor.commit();
			Schedule.setSettingsDirty();
			Intent myIntent = new Intent(this, SalatTimesMainActivity.class);
			// myIntent.putExtra("key", value); //Optional parameters
			this.startActivity(myIntent);
			Toast var13 = Toast.makeText(this.getApplicationContext(),
					this.getString(R.string.locationset), 0);
			var13.setGravity(17, 0, 0);
			var13.show();
			this.finish();
		} else {
			Intent myIntent = new Intent(this, SalatTimesMainActivity.class);
			this.startActivity(myIntent);
			Toast var6 = Toast.makeText(this.getApplicationContext(),
					this.getString(R.string.locationnotset), 0);
			var6.setGravity(17, 0, 0);
			var6.show();
			this.finish();
		}
	}
}
