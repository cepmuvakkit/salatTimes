package com.cepmuvakkit.times;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LatLong extends Activity {

	AlertDialog alert;
	EditText latitudeEditTxt, longitudeEditTxt, timezoneEditText,
			cityNameEditText, countryEditText, altitudeEditText,
			temperatureEditText,pressureEditText;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.requestWindowFeature(1);
		final View latlongLayout = ((LayoutInflater) this
				.getSystemService("layout_inflater")).inflate(R.layout.lat_long, null);
		latitudeEditTxt = (EditText) latlongLayout
				.findViewById(R.id.latitudeEditText);
		longitudeEditTxt  = (EditText) latlongLayout
				.findViewById(R.id.longitudeEditText);
		timezoneEditText  = (EditText) latlongLayout
				.findViewById(R.id.timezoneEditText);
		cityNameEditText= (EditText) latlongLayout
				.findViewById(R.id.cityNameEditText);
		countryEditText= (EditText) latlongLayout
				.findViewById(R.id.countryEditText);
		altitudeEditText= (EditText) latlongLayout
				.findViewById(R.id.altitudeEditText);
		temperatureEditText= (EditText) latlongLayout
				.findViewById(R.id.temperatureEditText);
		pressureEditText= (EditText) latlongLayout
				.findViewById(R.id.pressureEditText);
		latitudeEditTxt.setText(VARIABLE.settings.getString("latitude", ""));
		longitudeEditTxt.setText(VARIABLE.settings.getString("longitude", ""));
		timezoneEditText.setText(VARIABLE.settings.getString("timezone", ""));
		cityNameEditText.setText(VARIABLE.settings.getString("customCity", ""));
		countryEditText.setText(VARIABLE.settings.getString("country", ""));
		altitudeEditText.setText(VARIABLE.settings.getString("altitude", "0"));
		temperatureEditText.setText(VARIABLE.settings.getString("temperature", "10"));
		pressureEditText.setText(VARIABLE.settings.getString("pressure", "1010"));


	
		Builder var9 = new Builder(this);
		var9.setView(latlongLayout);
		var9.setCancelable(true).setTitle(R.string.location)
				.setPositiveButton(R.string.set, new OnClickListener() {
					public void onClick(DialogInterface var1, int var2) {
					
						String latitudeStr = latitudeEditTxt.getText().toString();
						String longitudeStr = longitudeEditTxt.getText().toString();
						String timezoneStr =timezoneEditText.getText().toString();
						String customCityStr =cityNameEditText.getText().toString();
						String countryStr =countryEditText.getText().toString();
						String altitudeStr =altitudeEditText.getText().toString();
						String temperatureStr =temperatureEditText.getText().toString();
						String pressureStr =pressureEditText.getText().toString();
						
						LatLong.this.setLoc(latitudeStr, longitudeStr,
								timezoneStr, customCityStr, countryStr,
								altitudeStr, temperatureStr, pressureStr);
					}
				}).setNegativeButton(R.string.cancel, new OnClickListener() {
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

	public void setLoc(String latitudeStr, String longitudeStr,
			String timezoneStr, String customCityStr, String countryStr,
			String altitudeStr, String temperatureStr, String pressureStr) {
		
		if (latitudeStr != null && longitudeStr != null && !latitudeStr.equals("")
				&& !longitudeStr.equals("")) {
	
			SharedPreferences.Editor editor = VARIABLE.settings.edit();

			
			editor.putString("customCity",customCityStr);
			editor.putString("latitude", latitudeStr);
			editor.putString("longitude",longitudeStr);
			editor.putString("timezone",timezoneStr);
			editor.putString("country",countryStr);
			editor.putString("altitude",altitudeStr);
			editor.putString("temperature",temperatureStr);
			editor.putString("pressure",pressureStr);
			
			editor.commit();
		

			Toast var13 = Toast.makeText(this.getApplicationContext(), R.string.locationset, 0);
			var13.setGravity(17, 0, 0);
			var13.show();
			this.finish();
		} else {
			Toast var6 = Toast.makeText(this.getApplicationContext(),R.string.locationnotset, 0);
			var6.setGravity(17, 0, 0);
			var6.show();
			this.finish();
		}
	}
}
