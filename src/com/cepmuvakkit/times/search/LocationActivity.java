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

import com.cepmuvakkit.times.R;
import com.cepmuvakkit.times.SalatTimesMainActivity;
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
import android.widget.SearchView;

/**
 * Displays a word and its definition.
 */
public class LocationActivity extends Activity {
	private double mLatitude, mLongitude,mTimeZone;

	@Override  
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.word);

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

           // TextView word = (TextView) findViewById(R.id.word);
            //TextView definition = (TextView) findViewById(R.id.definition);

            int wIndex = cursor.getColumnIndexOrThrow(LocationDatabase.KEY_SEARCH);
            int dIndex = cursor.getColumnIndexOrThrow(LocationDatabase.KEY_ORDER);
           // int dCountry = cursor.getColumnIndexOrThrow(LocationDatabase.KEY_COUNTRY);
            //int dCity = cursor.getColumnIndexOrThrow(LocationDatabase.KEY_CITY);

			String City = cursor.getString(cursor.getColumnIndexOrThrow("City"));
			String Latitude = cursor.getString(cursor.getColumnIndexOrThrow("Latitude"));
			mLatitude = Double.parseDouble(Latitude);
			String Longitude = cursor.getString(cursor.getColumnIndexOrThrow("Longitude"));
			mLongitude = Double.parseDouble(Longitude);
			String TimeZone = cursor.getString(cursor.getColumnIndexOrThrow("TimeZone"));
			mTimeZone= Double.parseDouble(TimeZone);
			Settings.getInstance().setCustomCity(City);
			Settings.getInstance().setLatitude(mLatitude);
			Settings.getInstance().setLongitude(mLongitude);
			Settings.getInstance().setTimezone(mTimeZone);
			
			Settings.save(VARIABLE.settings);
            
          //  word.setText(cursor.getString(wIndex));
            //definition.setText(cursor.getString(dIndex));
            
          
			//definition.setText(Country+" "+City+" "+TimeZone+" "+Latitude+" "+Longitude);
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
}
