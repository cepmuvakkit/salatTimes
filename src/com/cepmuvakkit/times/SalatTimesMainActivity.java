package com.cepmuvakkit.times;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.cepmuvakkit.times.receiver.StartNotificationReceiver;
import com.cepmuvakkit.times.settings.SalatTimesPreferenceActivity;
import com.cepmuvakkit.times.conversion.hicricalendar.HicriCalendar;
import com.cepmuvakkit.times.R;
import com.cepmuvakkit.times.location.DatabaseHelper;
import com.cepmuvakkit.times.posAlgo.PTimes;
import com.cepmuvakkit.times.settings.Settings;
import com.cepmuvakkit.times.timer.MyCountDownTimer;
import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.EarthPosition;
import com.cepmuvakkit.times.posAlgo.Methods;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SalatTimesMainActivity extends Activity implements
SearchView.OnQueryTextListener, SearchView.OnCloseListener, Methods  {
	
	private TextView mTimer,mFajr,mSunRise,mZuhr,mAsr,mMagrib,mIsha;
	private TextView mYear, mDayOfMonth, mDayName, mMonthName, mHijriYear,
			mHijriDayOfMonth, mHijriDayName, mHijriMonthName, mCityText,mLocationText;
	//private Button  mPreviousButton, mNextButton;
	//private long startTime = 60 * 10 * 1000;
	private final long interval = 1000;
	private CountDownTimer countDownTimer;
	//private boolean  timerHasStarted = false;
	private GregorianCalendar[] schedule;
	private SharedPreferences preferences;
	private int temperature, pressure, altitude;
	private double jd,timezoneinDay,jdn,mLatitude, mLongitude, mTimeZone;
	private String mLocationName;
	private DecimalFormat twoDigitFormat, oneDigit, twoDigit;
	private Calendar now;
	//private GregorianCalendar[] times;
	private double[] salatTimes = new double[7];;
	private ListView mListView;
	private SearchView searchView;
	private DatabaseHelper myDbHelper;
	private  SQLiteDatabase myDb = null;
	private HicriCalendar hc;
	private Context context;
	private GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		twoDigitFormat = new DecimalFormat("#0.00°");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.salat_times_main);
	
		
	//	preferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	//	Settings.load(preferences);
		context=getBaseContext();
		VARIABLE.context = this;
		if(VARIABLE.settings == null) VARIABLE.settings =PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		
		returnCurrentJulianDay();
		calculateSalatTimes();
		setLatLongLocation();
		//setSalatTimesText(salatTimes);
		setGregorianCalender(jd);
		setHijriCalender(salatTimes[4]);
		setTimerCountDown(now);
		//InitializeSearchMethod(); 

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.salat_times_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		switch (item.getItemId()) {
		case R.id.menu_search:
			// Intent intent = new Intent(this, SearchActivity.class);
			return true;
		case R.id.menu_refresh:
			returnCurrentJulianDay();
			calculateSalatTimes();
			//setSalatTimesText(salatTimes);
			setGregorianCalender(jd);
			setHijriCalender(salatTimes[4]);
			setTimerCountDown(now);
			return true;
		case  R.id.use_GPS:
			//Settings.getInstance().setDataFromGPS(false);
			//Settings.getInstance().setManualInput(false);
			getLocation();
			calculateSalatTimes();
			//setSalatTimesText(salatTimes);
			setTimerCountDown(now);
			setLatLongLocation();
			return true;	
		case R.id.menu_settings:
			intent = new Intent(this,SalatTimesPreferenceActivity.class);
			startActivityForResult(intent, 2);
			Settings.load(VARIABLE.settings);
			return true;	
		}
		return false;
	}
	private void setLatLongLocation() {
		mCityText = (TextView) findViewById(R.id.city);
		mLocationText = (TextView) findViewById(R.id.location);
		mLocationName=Settings.getInstance().getCustomCity();
		mCityText.setText(mLocationName);
		mLocationText.setText(twoDigitFormat.format(mLatitude)+" "+twoDigitFormat.format(mLongitude)+"±"+mTimeZone);
	}
	private void setTimerCountDown(Calendar now) {
		int i = 0;
		double hourNow=AstroLib.getLocalHourFromGregor(now);
		while (hourNow>salatTimes[i]) {
			i++;
		}
		long timeLeftinMillis = (long) ((salatTimes[i] - hourNow)* 60 * 60 * 1000);
		mTimer = (TextView) this.findViewById(R.id.timer);
		if (countDownTimer!=null)	countDownTimer.cancel();
		countDownTimer = new MyCountDownTimer(timeLeftinMillis, interval,
				mTimer);
		
		countDownTimer.start();
		//timerHasStarted = true;

		mTimer.setText(mTimer.getText());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
		gps.stopUsingGPS();

			try {
				myDbHelper.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				myDb.close();
			}
		}
	
	@Override
	protected void onStart() {
		super.onStart();
		//Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
	//	calculateSalatTimes();

	}

	@Override
	protected void onResume() {
		VARIABLE.mainActivityIsRunning = true;
		setLatLongLocation();
		updateTodaysTimetableAndNotification();
		super.onResume();
		

	}

	@Override
	protected void onPause() {
		
		VARIABLE.mainActivityIsRunning = false;

		super.onPause();
		//Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onRestart() {
		super.onPause();
		getLocation();
		Toast.makeText(this, " onRestart()", Toast.LENGTH_SHORT).show();
	}

	
	
	private  void returnCurrentJulianDay() {
		now= Calendar.getInstance();
		jd = AstroLib.calculateJulianDay(now);
		mTimeZone = now.getTimeZone().getOffset(now.getTimeInMillis()) / 3600000;
		timezoneinDay = mTimeZone / 24.0;
		/*Settings.getInstance().setJulianDay(jd);
		if (Settings.getInstance().isManualInput() == false) {
			Settings.getInstance().setTimezone(mTimeZone);
		}
		Settings.save(preferences);*/
	}
	public boolean onQueryTextChange(String newText) {
		showResults(newText + "*");
		return false;
	}

	public boolean onQueryTextSubmit(String query) {
		showResults(query + "*");
		return false;
	}

	public boolean onClose() {
		showResults("");
		return false;
	}

	private void showResults(String query) {

		Cursor cursor = myDbHelper.searchCustomer((query != null ? query
				.toString() : "@@@@"),myDb);

		if (cursor == null) {
			//
		} else {
			// Specify the columns we want to display in the result
			String[] from = new String[] { DatabaseHelper.KEY_CITY,
					DatabaseHelper.KEY_COUNTRY };

			// Specify the Corresponding layout elements where we want the
			// columns to go
			int[] to = new int[] { R.id.sCity, R.id.sCountry };

			// Create a simple cursor adapter for the definitions and apply them
			// to the ListView
			@SuppressWarnings("deprecation")
			SimpleCursorAdapter locations = new SimpleCursorAdapter(this,
					R.layout.locationresult, cursor, from, to);
			mListView.setAdapter(locations);

			// Define the on-click listener for the list items
			mListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Get the cursor, positioned to the corresponding row in
					// the result set
					searchView.onActionViewCollapsed();

					Cursor cursor = (Cursor) mListView
							.getItemAtPosition(position);

					// Get the state's capital from this row in the database.
				/*	String Country = cursor.getString(cursor
							.getColumnIndexOrThrow("Country"));*/
					String City = cursor.getString(cursor
							.getColumnIndexOrThrow("City"));

					String Latitude = cursor.getString(cursor
							.getColumnIndexOrThrow("Latitude"));
					mLatitude=Double.parseDouble(Latitude);
					String Longitude = cursor.getString(cursor
							.getColumnIndexOrThrow("Longitude"));
					mLongitude=Double.parseDouble(Longitude);
					String TimeZone = cursor.getString(cursor
							.getColumnIndexOrThrow("TimeZone"));
					mTimeZone=Double.parseDouble(TimeZone)/15.0;
					
					
					Settings.getInstance().setCustomCity(
							mLocationName);
					Settings.getInstance().setLatitude(mLatitude);
					Settings.getInstance().setLongitude(mLongitude);

					Settings.getInstance().setTimezone(mTimeZone);
					
					setLatLongLocation();
					
					Settings.save(VARIABLE.settings);
					
					

					searchView.setQuery("", true);

				}

				
			});
		}
	}
	private void InitializeSearchMethod() {
		//searchView = (SearchView) findViewById(R.id.search);
			searchView.setIconifiedByDefault(true);
			searchView.setOnQueryTextListener(this);
			searchView.setOnCloseListener(this);
			mListView = (ListView) findViewById(R.id.list);
			myDbHelper = new DatabaseHelper(this);
			
			/* * Database must be initialized before it can be used. This will ensure
			 * that the database exists and is the current version.*/
			 
			myDbHelper.initializeDataBase();

			try {
				// A reference to the database can be obtained after initialization.
				myDb = myDbHelper.getWritableDatabase();
				
				// * Place code to use database here.
				 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	}
	private void setHijriCalender(double mSunsetHour) {
		double ΔT=0;
		hc = new HicriCalendar(jd, mTimeZone, mSunsetHour, ΔT);
		
		mHijriYear = (TextView) findViewById(R.id.hijriYear);
		mHijriDayOfMonth = (TextView) findViewById(R.id.hijriDayOfMonth);
		mHijriDayName = (TextView) findViewById(R.id.hijriDayName);
		mHijriMonthName = (TextView) findViewById(R.id.hijriMonthName);
		mHijriYear.setText(hc.getHijriYear() + "");
		mHijriDayOfMonth.setText(hc.getHijriDay() + "");
		mHijriDayName.setText(hc.getDay(context) + "");
		mHijriMonthName.setText(hc.getHijriMonthName(context) + "");
	}
	
	/** Called when the user clicks the calculatePreviousDay */
	public void calculatePreviousDay(@SuppressWarnings("unused") View view) {
		//mPreviousButton = (Button) findViewById(R.id.PrevButton);
		jd--;
		calculateSalatTimes();
		//setSalatTimesText(salatTimes);
		setGregorianCalender(jd);
		setHijriCalender(24);
		
		
	}
	/** Called when the user clicks the calculateNextDay */
	public void calculateNextDay(@SuppressWarnings("unused") View view) {
		//mNextButton = (Button) findViewById(R.id.nextButton);
		jd++;
		calculateSalatTimes();
		//setSalatTimesText(salatTimes);
		setGregorianCalender(jd);
		setHijriCalender(24);
	
	}

	private void setGregorianCalender(double julian) {
		Calendar c = AstroLib.convertJulian2Gregorian(julian);
		Locale locale = Locale.getDefault();
		int year = c.get(Calendar.YEAR);
		String monthName = c.getDisplayName(Calendar.MONTH, Calendar.SHORT,
				locale);
		String dayName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
				locale);
		int day = c.get(Calendar.DAY_OF_MONTH);
		mYear = (TextView) findViewById(R.id.year);
		mDayOfMonth = (TextView) findViewById(R.id.dayOfMonth);
		mDayName = (TextView) findViewById(R.id.dayName);
		mMonthName = (TextView) findViewById(R.id.monthName);
		mYear.setText(year + "");
		mDayOfMonth.setText(day + "");
		mDayName.setText(dayName);
		mMonthName.setText(monthName + "");
	
	}

	private void calculateSalatTimes() {
		Settings.load(VARIABLE.settings);
		byte[] estMethod =Settings.getInstance().getEstMethods();
		byte calculationMethod = (byte) Settings.getInstance().getCalculationMethodsIndex();
		jdn = Math.round(jd) - 0.5;
		EarthPosition loc = new EarthPosition(
				Settings.getInstance().getLatitude(),
				Settings.getInstance().getLongitude(),
				Settings.getInstance().getTimezone(),
				Settings.getInstance().getAltitude(),
				Settings.getInstance().getTemperature(),
				Settings.getInstance().getPressure());
	
		PTimes ptimes = new PTimes(jdn, loc, calculationMethod, estMethod);
		PTimes ptimesNext = new PTimes(jdn+1, loc, calculationMethod, estMethod);

		double[]  salatRaw=ptimes.getSalat();
	

		salatTimes[0] = salatRaw[FAJR];
		salatTimes[1] = salatRaw[SUNRISE];
		salatTimes[2] = salatRaw[SUNTRANSIT];
		salatTimes[3] = salatRaw[Settings.getInstance().isHanafiMathab()?ASR_HANEFI:ASR_SHAFI];
		salatTimes[4] = salatRaw[SUNSET];
		salatTimes[5] = salatRaw[ISHA];
		salatTimes[6] = ptimesNext.getSalat()[FAJR]+24;// Next fajr
		setSalatTimesText();

		// = ptimes.getSalatinGregorian(Settings.getInstance().isHanafiMathab()?ASR_HANEFI:ASR_SHAFI);

		
	}

	private void setSalatTimesText() {
		
		mFajr=(TextView)findViewById(R.id.FajrTxtView);
		mSunRise=(TextView)findViewById(R.id.SunRiseTxtView);
		mZuhr=(TextView)findViewById(R.id.ZuhrTxtView);
		mAsr=(TextView)findViewById(R.id.AsrTxtView);
		mMagrib=(TextView)findViewById(R.id.MagribTxtView);
		mIsha=(TextView)findViewById(R.id.IshaTxtView);
		
		mFajr.setText(AstroLib.getStringHHMM(salatTimes[0]));
		mSunRise.setText(AstroLib.getStringHHMM(salatTimes[1]));
		mZuhr.setText(AstroLib.getStringHHMM(salatTimes[2]));
		mAsr.setText(AstroLib.getStringHHMM(salatTimes[3]));
		mMagrib.setText(AstroLib.getStringHHMM(salatTimes[4]));
		mIsha.setText(AstroLib.getStringHHMM(salatTimes[5]));
	}
	
	private void getLocation() {
		gps = new GPSTracker(this);
		if (gps.canGetLocation()) {

					mLongitude = gps.getLongitude();
					mLatitude = gps.getLatitude();
					mLocationName = getLocationName(mLatitude, mLongitude);
					altitude = (int) gps.getAltitude();

					Settings.getInstance().setLatitude(mLatitude);
					Settings.getInstance()
							.setLongitude(mLongitude);
					Settings.getInstance().setAltitude(altitude);
					Settings.getInstance().setCustomCity(
							mLocationName);
					Settings.getInstance().setTimezone(mTimeZone);
					Settings.save(VARIABLE.settings);

					Toast.makeText(
							this,
							"Your Position: "+mLocationName+" "
								+ twoDigitFormat.format(mLatitude)
									+ twoDigitFormat.format(mLongitude) + " ",
							Toast.LENGTH_SHORT).show();

				} else {

					Toast.makeText(this, getText(R.string.no_location),
							Toast.LENGTH_SHORT).show();

					gps.showSettingsAlert();
				}
			
	}
		

	
	public String getLocationName(double latitude, double longitude){
		String locationName = "Unknown";
		if (Geocoder.isPresent()==true){
		Geocoder gc = new Geocoder(this, Locale.ENGLISH);
		
	 
		try {
			List<Address> addresses = gc.getFromLocation(latitude, longitude,
					1);
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				locationName = address.getLocality();
			}
		} catch (IOException e) {
			Toast.makeText(this, "Can not get Geo coder", Toast.LENGTH_LONG)
					.show();

		}}
		
		return locationName;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (Schedule.settingsAreDirty()) {
			updateTodaysTimetableAndNotification();
			//VARIABLE.updateWidgets(this);
		}

	}
	private void updateTodaysTimetableAndNotification() {
		returnCurrentJulianDay();
		calculateSalatTimes();

		StartNotificationReceiver.setNext(this);

		//setSalatTimesText(salatTimes);
		setGregorianCalender(jd);
		setHijriCalender(salatTimes[4]);
		setTimerCountDown(now);
	}
}
