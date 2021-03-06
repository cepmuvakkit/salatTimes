package com.cepmuvakkit.times;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.cepmuvakkit.times.receiver.StartNotificationReceiver;
import com.cepmuvakkit.times.search.LatLongConfirm;
import com.cepmuvakkit.times.search.LocationDatabase;
import com.cepmuvakkit.times.search.LocationProvider;
import com.cepmuvakkit.times.settings.SalatTimesPreferenceActivity;
import com.cepmuvakkit.times.conversion.hicricalendar.HicriCalendar;
import com.cepmuvakkit.times.R;

import com.cepmuvakkit.times.posAlgo.PTimes;
import com.cepmuvakkit.times.settings.Settings;
import com.cepmuvakkit.times.timer.MyCountDownTimer;
import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.EarthPosition;
import com.cepmuvakkit.times.posAlgo.HigherLatitude;
import com.cepmuvakkit.times.posAlgo.Methods;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SalatTimesMainActivity extends Activity implements Methods,HigherLatitude {

	private TextView mTimer, mFajr, mSunRise, mZuhr, mAsr, mMagrib, mIsha;
	private TextView mYear, mDayOfMonth, mDayName, mMonthName, mHijriYear,
			mHijriDayOfMonth, mHijriDayName, mHijriMonthName, mCityText,
			mLocationText, mTimeZoneText;
	// private Button mPreviousButton, mNextButton;
	// private long startTime = 60 * 10 * 1000;
	private final long interval = 1000;
	private static CountDownTimer countDownTimer;
	private GregorianCalendar[] schedule;
	private int altitude;
	private double jd, jdn, mLatitude, mLongitude, mTimeZone;
	private String mLocationName;
	private DecimalFormat twoDigitFormat = new DecimalFormat("#0.00°");
	private DecimalFormat timezonefmt = new DecimalFormat("GMT+#.0;GMT-#.0");
	private SimpleDateFormat currentTimeFormat;
	private boolean isCurrentDay = true;
	private GregorianCalendar now;
	// private double[] salatTimes = new double[7];
	private HicriCalendar hc;
	private Context context;
	private GPSTracker gps;
	private TextView mTextView;
	private ListView mListView;
	public final static String JULIAN_DAY = "com.cepmuvakkit.times.JULIANDAY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.salat_times_main);
		context = getBaseContext();
		VARIABLE.context = this;
		if (VARIABLE.settings == null)
			VARIABLE.settings = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
		// Settings.load(VARIABLE.settings);
		returnCurrentJulianDay();
		configureCalculationDefaults();
		updateTodaysTimetableAndNotification();
		setGregorianCalender(jd);
		// setHijriCalender(salatTimes[4]);

		setTimerCountDown(now);
		// mTimer = (TextView) this.findViewById(R.id.timer);
		// MyCountDownTimerRecursive.onUpdate(mTimer);
		mTextView = (TextView) findViewById(R.id.text);
		mListView = (ListView) findViewById(R.id.list);

		handleIntent(getIntent());

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// Because this activity has set launchMode="singleTop", the system
		// calls this method
		// to deliver the intent if this activity is currently the foreground
		// activity when
		// invoked again (when the user executes a search from this activity, we
		// don't create
		// a new instance of this activity, so the system delivers the search
		// intent here)
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// handles a click on a search suggestion; launches activity to show
			// word
			Intent locationIntent = new Intent(this, LatLongConfirm.class);
			locationIntent.setData(intent.getData());
			startActivity(locationIntent);
		} else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// handles a search query
			String query = intent.getStringExtra(SearchManager.QUERY);
			showResults(query);
		}
	}

	/**
	 * Searches the dictionary and displays results for the given query.
	 * 
	 * @param query
	 *            The search query
	 */
	private void showResults(String query) {

		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(LocationProvider.CONTENT_URI, null, null,
				new String[] { query }, null);

		if (cursor == null) {
			// There are no results
			mTextView.setText(getString(R.string.no_results,
					new Object[] { query }));
		} else {
			// Display the number of results
			int count = cursor.getCount();
			String countString = getResources().getQuantityString(
					R.plurals.search_results, count,
					new Object[] { count, query });
			mTextView.setText(countString);

			// Specify the columns we want to display in the result
			String[] from = new String[] { LocationDatabase.KEY_SEARCH,
					LocationDatabase.KEY_ORDER };

			// Specify the corresponding layout elements where we want the
			// columns to go
			int[] to = new int[] { R.id.word, R.id.definition };

			// Create a simple cursor adapter for the definitions and apply them
			// to the ListView
			@SuppressWarnings("deprecation")
			SimpleCursorAdapter words = new SimpleCursorAdapter(this,
					R.layout.result, cursor, from, to);
			mListView.setAdapter(words);

			// Define the on-click listener for the list items
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Build the Intent used to open LatLongConfirm with a
					// specific word Uri
					Intent locationIntent = new Intent(getApplicationContext(),
							LatLongConfirm.class);
					Uri data = Uri.withAppendedPath(
							LocationProvider.CONTENT_URI, String.valueOf(id));
					locationIntent.setData(data);
					startActivity(locationIntent);
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.salat_times_main, menu);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			SearchView searchView = (SearchView) menu
					.findItem(R.id.menu_search).getActionView();
			searchView.setSearchableInfo(searchManager
					.getSearchableInfo(getComponentName()));
			searchView.setIconifiedByDefault(false);
		}

		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		switch (item.getItemId()) {
		case R.id.menu_search:
			onSearchRequested();
			return true;
		case R.id.menu_refresh:
			returnCurrentJulianDay();
			updateTodaysTimetableAndNotification();
			// calculateSalatTimes();
			// setGregorianCalender(jd);
			// setHijriCalender(salatTimes[4]);
			setTimerCountDown(now);
			return true;
		case R.id.use_GPS:
			// Settings.getInstance().setDataFromGPS(false);
			// Settings.getInstance().setManualInput(false);
			getLocation();
			// calculateSalatTimes();
			updateTodaysTimetableAndNotification();
			// setTimerCountDown(now);
			setLatLongLocation();
			return true;

		case R.id.menu_more_app:
			try {
				startActivity(new Intent(
						"android.intent.action.VIEW",
						Uri.parse("market://search?q=pub:%22Mehmet%20Mahmudoglu%22")));
			} catch (Exception e) {
				Toast.makeText(this, "Google Play Not Found", Toast.LENGTH_SHORT).show();

				e.printStackTrace();
			}
			return true;

		case R.id.menu_more_info:
			 Intent myIntent = new Intent(this.getBaseContext(), KarahatTimesActivity.class);
			 myIntent.putExtra(JULIAN_DAY, jd);
			 startActivity(myIntent);
			return true;
		case R.id.menu_settings:
			intent = new Intent(this, SalatTimesPreferenceActivity.class);
			startActivityForResult(intent, 2);
			//Settings.load(VARIABLE.settings);
			updateTodaysTimetableAndNotification();
			return true;
		}
		return false;
	}

	private void setLatLongLocation() {
		mCityText = (TextView) findViewById(R.id.city);
		mLocationText = (TextView) findViewById(R.id.location);
		mTimeZoneText = (TextView) findViewById(R.id.timezone);
		SharedPreferences pref=VARIABLE.settings;
		mLocationName =pref.getString("customCity", "DefCustomCity");
		mLatitude = Double.parseDouble(pref.getString("latitude", "39.938"));
		mLongitude = Double.parseDouble(pref.getString("longitude", "32.848"));
		mTimeZone =  Double.parseDouble(pref.getString("timezone", "3.0"));
		mCityText.setText(mLocationName);

		mLocationText.setText(twoDigitFormat.format(mLatitude)
				+ (mLatitude > 0 ? this.getString(R.string.N) : this
						.getString(R.string.S))
				+ " "
				+ twoDigitFormat.format(mLongitude)
				+ (mLongitude > 0 ? this.getString(R.string.E) : this
						.getString(R.string.W)));
		try {
			mTimeZoneText.setText(timezonefmt.format(mTimeZone));
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		}

	}

	private void setTimerCountDown(Calendar now) {
		int nextTimeIndex = Schedule.today().nextTimeIndex();
		if (nextTimeIndex % 2 == 0)
			nextTimeIndex++;
		long timeLeftinMillis = schedule[nextTimeIndex].getTimeInMillis()
				- now.getTimeInMillis();
		mTimer = (TextView) this.findViewById(R.id.timer);
		if (countDownTimer != null)
			countDownTimer.cancel();
		countDownTimer = new MyCountDownTimer(timeLeftinMillis, interval,
				mTimer);

		countDownTimer.start();
		// timerHasStarted = true;

		mTimer.setText(mTimer.getText());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			// Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
			gps.stopUsingGPS();
		} catch (Exception e) {
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// VARIABLE.mainActivityIsRunning = true;

		// Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
		// updateTodaysTimetableAndNotification();
	}

	@Override
	protected void onResume() {
		VARIABLE.mainActivityIsRunning = true;
		// Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

		// updateTodaysTimetableAndNotification();
		super.onResume();
	}

	@Override
	protected void onPause() {

		VARIABLE.mainActivityIsRunning = false;

		super.onPause();
		// onPause", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onRestart() {
		// Toast.makeText(this, " onRestart()", Toast.LENGTH_SHORT).show();
		super.onPause();
		// updateTodaysTimetableAndNotification();

	}

	private void returnCurrentJulianDay() {
		now = new GregorianCalendar();
		jd = AstroLib.calculateJulianDay(now);
		mTimeZone = now.getTimeZone().getOffset(now.getTimeInMillis()) / 3600000;
		// timezoneinDay = mTimeZone / 24.0;
		/*
		 * Settings.getInstance().setJulianDay(jd); if
		 * (Settings.getInstance().isManualInput() == false) {
		 * Settings.getInstance().setTimezone(mTimeZone); }
		 * Settings.save(preferences);
		 */
	}

	private void setHijriCalender(double mSunsetHour) {
		double ΔT = 0;
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
	public void calculatePreviousDay(View view) {
		isCurrentDay = false;
		jd--;
		calculateSalatTimesNav(jd);
		setGregorianCalender(jdn);
		setHijriCalender(24);

	}

	/** Called when the user clicks the calculateNextDay */
	public void calculateNextDay(View view) {
		isCurrentDay = false;
		jd++;
		calculateSalatTimesNav(jd);
		setGregorianCalender(jdn);
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

	private void calculateSalatTimesNav(double jd) {
		SharedPreferences pref=VARIABLE.settings;
		byte[] estMethod = {
				Byte.parseByte(pref.getString("estMethodofFajr", NO_ESTIMATION
						+ "")),
				Byte.parseByte(pref.getString("estMethodofIsha", NO_ESTIMATION
						+ "")) };
		byte calculationMethod = Byte.parseByte(pref.getString("calculationMethodsIndex",
						CONSTANT.DEFAULT_CALCULATION_METHOD + ""));
		jdn = Math.round(jd) - 0.5;
		EarthPosition loc = new EarthPosition(
				Double.parseDouble(pref.getString("latitude", "39.938")),
				Double.parseDouble(pref.getString("longitude", "32.848")),
				Double.parseDouble(pref.getString("timezone", "3.0")),
				Integer.parseInt(pref.getString("altitude", "0")),
				Integer.parseInt(pref.getString("temperature","10")),
				Integer.parseInt(pref.getString("pressure", "1010")));

		PTimes ptimes = new PTimes(jdn, loc, calculationMethod, estMethod);
		// PTimes ptimesNext = new PTimes(jdn+1, loc, calculationMethod,
		// estMethod);
		GregorianCalendar[] salatTimes = new GregorianCalendar[6];
		salatTimes = ptimes.getSalatinGregorian( pref.getString("isHanafiMathab", "0").equals("1") ? ASR_HANEFI : ASR_SHAFI);
		setSalatTimesText(salatTimes);

	}

	private void setSalatTimesText(GregorianCalendar[] salatTimes) {
		mFajr.setText(currentTimeFormat.format(salatTimes[CONSTANT.IMSAK]
				.getTime()));
		mSunRise.setText(currentTimeFormat.format(salatTimes[CONSTANT.GUNES]
				.getTime()));
		mZuhr.setText(currentTimeFormat.format(salatTimes[CONSTANT.OGLE]
				.getTime()));
		mAsr.setText(currentTimeFormat.format(salatTimes[CONSTANT.IKINDI]
				.getTime()));
		mMagrib.setText(currentTimeFormat.format(salatTimes[CONSTANT.AKSAM]
				.getTime()));
		mIsha.setText(currentTimeFormat.format(salatTimes[CONSTANT.YATSI]
				.getTime()));

	}

	private void setSalatTimesTextToday() {

		mFajr = (TextView) findViewById(R.id.FajrTxtView);
		mSunRise = (TextView) findViewById(R.id.SunRiseTxtView);
		mZuhr = (TextView) findViewById(R.id.ZuhrTxtView);
		mAsr = (TextView) findViewById(R.id.AsrTxtView);
		mMagrib = (TextView) findViewById(R.id.MagribTxtView);
		mIsha = (TextView) findViewById(R.id.IshaTxtView);
		int index = 6;
		int nextTimeIndex = Schedule.today().nextTimeIndex();
		if (nextTimeIndex % 2 == 0)
			nextTimeIndex++;
		if (isCurrentDay)
			index = (nextTimeIndex - 1) / 2;

		mFajr.setText(currentTimeFormat.format(schedule[CONSTANT.FAJR]
				.getTime()));
		mSunRise.setText(currentTimeFormat.format(schedule[CONSTANT.SUNRISE]
				.getTime()));
		mZuhr.setText(currentTimeFormat.format(schedule[CONSTANT.DHUHR]
				.getTime()));
		mAsr.setText(currentTimeFormat.format(schedule[CONSTANT.ASR].getTime()));
		mMagrib.setText(currentTimeFormat.format(schedule[CONSTANT.MAGHRIB]
				.getTime()));
		mIsha.setText(currentTimeFormat.format(schedule[CONSTANT.ISHAA]
				.getTime()));
		if (index == 0) {
			((TextView) findViewById(R.id.FajrTxtLbl)).setTextColor(Color.RED);
			mFajr.setTextColor(Color.RED);
		} else {
			((TextView) findViewById(R.id.FajrTxtLbl))
					.setTextColor(Color.BLACK);
			mFajr.setTextColor(Color.BLACK);

		}
		if (index == 1) {
			((TextView) findViewById(R.id.SunRiseTxtLbl))
					.setTextColor(Color.RED);
			mSunRise.setTextColor(Color.RED);
		} else {
			((TextView) findViewById(R.id.SunRiseTxtLbl))
					.setTextColor(Color.BLACK);
			mSunRise.setTextColor(Color.BLACK);

		}
		if (index == 2) {
			((TextView) findViewById(R.id.DhuhrTxtLbl)).setTextColor(Color.RED);
			mZuhr.setTextColor(Color.RED);
		} else {
			((TextView) findViewById(R.id.DhuhrTxtLbl))
					.setTextColor(Color.BLACK);
			mZuhr.setTextColor(Color.BLACK);

		}
		if (index == 3) {
			((TextView) findViewById(R.id.AsrTxtLbl)).setTextColor(Color.RED);
			mAsr.setTextColor(Color.RED);
		} else {
			((TextView) findViewById(R.id.AsrTxtLbl)).setTextColor(Color.BLACK);
			mAsr.setTextColor(Color.BLACK);

		}
		if (index == 4) {
			((TextView) findViewById(R.id.MaghribTxtLbl))
					.setTextColor(Color.RED);
			mMagrib.setTextColor(Color.RED);
		} else {
			((TextView) findViewById(R.id.MaghribTxtLbl))
					.setTextColor(Color.BLACK);
			mMagrib.setTextColor(Color.BLACK);

		}
		if (index == 5) {
			((TextView) findViewById(R.id.IshaTxtLbl)).setTextColor(Color.RED);
			mIsha.setTextColor(Color.RED);
		} else {
			((TextView) findViewById(R.id.IshaTxtLbl))
					.setTextColor(Color.BLACK);
			mIsha.setTextColor(Color.BLACK);

		}

	}

	private void getLocation() {
		gps = new GPSTracker(this);
		if (gps.canGetLocation()) {

			mLongitude = gps.getLongitude();
			mLatitude = gps.getLatitude();
			mLocationName = getLocationName(mLatitude, mLongitude);
			altitude = (int) gps.getAltitude();
			mTimeZone = now.getTimeZone().getOffset(now.getTimeInMillis()) / 3600000;

			SharedPreferences.Editor editor = VARIABLE.settings.edit();
			editor.putString("customCity", mLocationName);
			editor.putString("latitude", mLatitude + "");
			editor.putString("longitude", mLongitude + "");
			editor.putString("altitude", altitude + "");
			editor.putString("timezone", mTimeZone + "");
			editor.commit();

			Toast.makeText(
					this,
					"Your Position: " + mLocationName + " "
							+ twoDigitFormat.format(mLatitude)
							+ twoDigitFormat.format(mLongitude) + " "
							+ altitude, Toast.LENGTH_SHORT).show();

		} else {

			Toast.makeText(this, getText(R.string.no_location),
					Toast.LENGTH_SHORT).show();

			gps.showSettingsAlert();
		}

	}

	public String getLocationName(double latitude, double longitude) {
		String locationName = "Unknown";
		if (Geocoder.isPresent() == true) {
			Geocoder gc = new Geocoder(this, Locale.ENGLISH);

			try {
				List<Address> addresses = gc.getFromLocation(latitude,
						longitude, 1);
				if (addresses.size() > 0) {
					Address address = addresses.get(0);
					locationName = address.getLocality();
				}
			} catch (IOException e) {
				Toast.makeText(this, "Can not get Geo coder", Toast.LENGTH_LONG)
						.show();

			}
		}

		return locationName;
	}

	private void configureCalculationDefaults() {
		if (!VARIABLE.settings.contains("latitude")
				|| !VARIABLE.settings.contains("longitude")) {

			getLocation();
			// VARIABLE.updateWidgets(this); Buradaki update yüzünden
			// Calculation methodu catch edemiyordu.

		}
		if (!VARIABLE.settings.contains("calculationMethodsIndex")) {
			try {
				String country = Locale.getDefault().getISO3Country()
						.toUpperCase();
				SharedPreferences.Editor editor = VARIABLE.settings.edit();
				for (int i = 0; i < CONSTANT.CALCULATION_METHOD_COUNTRY_CODES.length; i++) {
					if (Arrays.asList(
							CONSTANT.CALCULATION_METHOD_COUNTRY_CODES[i])
							.contains(country)) {
						editor.putString("calculationMethodsIndex", i + "");
						editor.commit();
						VARIABLE.updateWidgets(this);
						break;
					}
				}
			} catch (Exception ex) {
				// Wasn't set, oh well we'll uses DEFAULT_CALCULATION_METHOD
				// later
			}
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (Schedule.settingsAreDirty()) {
			updateTodaysTimetableAndNotification();
			VARIABLE.updateWidgets(this);
		}

	}

	private void updateTodaysTimetableAndNotification() {
		// returnCurrentJulianDay();
		isCurrentDay = true;
		Schedule.setSettingsDirty();
		StartNotificationReceiver.setNext(this);
		schedule = Schedule.today().getTimes();
		// salatTimes= Schedule.today().getSalatTimes();
		setTimeFormat();
		setLatLongLocation();
		setSalatTimesTextToday();
		setGregorianCalender(jd);
		setHijriCalender(AstroLib
				.getLocalHourFromGregor(schedule[CONSTANT.MAGHRIB]));
		// setTimerCountDown(now);
	}

	private void setTimeFormat() {
		int formatIndex = Integer.parseInt(VARIABLE.settings.getString(
				"timeFormatIndex", CONSTANT.DEFAULT_TIME_FORMAT + ""));
		SimpleDateFormat timeFormatAMPM, timeFormatHHMM, timeFormatHHMMSS;
		timeFormatAMPM = new SimpleDateFormat("h:mm a");
		timeFormatHHMM = new SimpleDateFormat("HH:mm");
		timeFormatHHMMSS = new SimpleDateFormat("HH:mm:ss");
		switch (formatIndex) {
		case 0:
			currentTimeFormat = timeFormatHHMM;
			break;
		case 1:
			currentTimeFormat = timeFormatAMPM;
			break;
		case 2:
			currentTimeFormat = timeFormatHHMMSS;
			break;
		}
	}

}
