/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cepmuvakkit.times.search;

import java.text.DecimalFormat;
import java.util.Calendar;

import com.cepmuvakkit.times.R;
import com.cepmuvakkit.times.SalatTimesMainActivity;
import com.cepmuvakkit.times.Schedule;
import com.cepmuvakkit.times.VARIABLE;
import com.cepmuvakkit.times.settings.Settings;

import android.app.Activity;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Displays a word and its definition.
 */
public class LocationActivity extends Activity {
	private double mLatitude, mLongitude,mTimeZone;
	private String Country,City;
	private Button confirmButton;
	private CheckBox onlineSalatChkbox,timezoneChkbox,dstChkBox;	
	private boolean isSystemTimeZone;
	private DecimalFormat twoDigitFormat=new DecimalFormat("#0.00°");
	private DecimalFormat timezonefmt=new DecimalFormat("GMT+#.0;GMT-#.0");

	//"+#,##0.00;-#"
	@Override  
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_city);
        confirmButton=(Button)findViewById(R.id.confirmLocation);
        onlineSalatChkbox=(CheckBox)findViewById(R.id.onlineChkbox);
        timezoneChkbox=(CheckBox)findViewById(R.id.timezoneChkbox);
        dstChkBox=(CheckBox)findViewById(R.id.dstChkBox);
        isSystemTimeZone=timezoneChkbox.isChecked();
     	Calendar now = Calendar.getInstance();
		mTimeZone = now.getTimeZone().getOffset(now.getTimeInMillis()) / 3600000;
		timezoneChkbox.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	                if(timezoneChkbox.isChecked()){
	                	isSystemTimeZone=true;
	               }else{
	            	   isSystemTimeZone=false;
	            	   dstChkBox.setClickable(true);
	                }
	            }
	        });
		
		onlineSalatChkbox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(onlineSalatChkbox.isChecked()){
                    confirmButton.setText("Download");
                    timezoneChkbox.setClickable(false);
                    dstChkBox.setClickable(false);
               }else{
                   confirmButton.setText("Confirm");
                   timezoneChkbox.setClickable(true);
                }
            }
        });
        
      
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Uri uri = getIntent().getData();
        Cursor cursor = managedQuery(uri, null, null, null, null);

        if (cursor == null) {
            finish();
        } else {
            cursor.moveToFirst();

            TextView country = (TextView) findViewById(R.id.country);
            TextView city = (TextView) findViewById(R.id.city);
            TextView latitude = (TextView) findViewById(R.id.latitude);
            TextView longitude = (TextView) findViewById(R.id.longitude);
            TextView timezone = (TextView) findViewById(R.id.timezone);


            int wIndex = cursor.getColumnIndexOrThrow(LocationDatabase.KEY_SEARCH);
            int dIndex = cursor.getColumnIndexOrThrow(LocationDatabase.KEY_ORDER);
           // int dCountry = cursor.getColumnIndexOrThrow(LocationDatabase.KEY_COUNTRY);
            //int dCity = cursor.getColumnIndexOrThrow(LocationDatabase.KEY_CITY);

			Country = cursor.getString(cursor.getColumnIndexOrThrow("Country"));
			City = cursor.getString(cursor.getColumnIndexOrThrow("City"));
			String Latitude = cursor.getString(cursor.getColumnIndexOrThrow("Latitude"));
			mLatitude = Double.parseDouble(Latitude);
			String Longitude = cursor.getString(cursor.getColumnIndexOrThrow("Longitude"));
			mLongitude = Double.parseDouble(Longitude);
			final String TimeZone = cursor.getString(cursor.getColumnIndexOrThrow("TimeZone"));
			if (!isSystemTimeZone)
			{
			
				mTimeZone= Double.parseDouble(TimeZone);
			}
			
		
            
			country.setText(Country);
			city.setText(City);
			latitude.setText( twoDigitFormat.format(mLatitude)+(mLatitude>0?"N":"S"));
			longitude.setText( twoDigitFormat.format(mLongitude)+(mLongitude>0?"E":"W"));
			timezone.setText(timezonefmt.format(mTimeZone));
			
            
          
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.salat_times_main, menu);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                onSearchRequested();
                return true;
            case android.R.id.home:
                Intent intent = new Intent(this, SalatTimesMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
    
	public void confirmLocation(View view) {
		
		Schedule.setSettingsDirty(); 
		Settings.getInstance().setCountryName(Country);
		Settings.getInstance().setCustomCity(City);
		Settings.getInstance().setLatitude(mLatitude);
		Settings.getInstance().setLongitude(mLongitude);
		Settings.getInstance().setTimezone(mTimeZone);
		Settings.save(VARIABLE.settings);
		
		Intent myIntent = new Intent(this, SalatTimesMainActivity.class);
		//myIntent.putExtra("key", value); //Optional parameters
		this.startActivity(myIntent);
	
	}
	
	
	public void cancelButton(View view) {
		Intent myIntent = new Intent(this, SalatTimesMainActivity.class);
		this.startActivity(myIntent);
	
	}
	
	public void onlineCheckBox(View view) {
		Toast.makeText(this, "onlineCheckBox", Toast.LENGTH_SHORT).show();

	
	}
}
