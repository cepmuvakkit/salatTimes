package com.cepmuvakkit.times;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.cepmuvakkit.times.conversion.hicricalendar.HicriCalendar;
import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.EarthPosition;
import com.cepmuvakkit.times.posAlgo.PTimes;
import com.cepmuvakkit.times.posAlgo.SolarPosition;
import com.cepmuvakkit.times.settings.Settings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class KarahatTimesActivity extends Activity {
	SharedPreferences pref;

	private double latitude;
	private double timezone;
	private double longitude;
	private double jd,mTimeZone;
	//private double jdn;
	private int temperature;
	private int pressure;
	private int altitude;

	private double israkIsfirarAngle;
	double[]  kerahat = new double[8];
	private double fajrAngle;
	private TextView mIshrak;
	private TextView mDuha;
	private TextView mIstiva;
	private TextView mAsrsani;
	private TextView mIsfirar;
	
	private DecimalFormat twoDigitFormat = new DecimalFormat("#0.00°");
	private DecimalFormat timezonefmt = new DecimalFormat("GMT+#.0;GMT-#.0");
	private SimpleDateFormat currentTimeFormat;
	private final byte FAJR_ = 0, ISRAK = 1, SUNTRANSIT_ = 2, ASRHANEFI = 3, ISFIRAR = 4, SUNSET_ = 5, KERAHAT_COUNT = 6, DUHA = 7, ISTIVA = 8;
	private TextView mHijriDayName;
	private TextView mHijriDayOfMonth;
	private TextView mHijriYear;
	private TextView mHijriMonthName;
	private HicriCalendar hc;
	private TextView mYear;
	private TextView mDayOfMonth;
	private TextView mDayName;
	private TextView mMonthName;
	private TextView mCityText;
	private TextView mLocationText;
	private TextView mTimeZoneText;
	private String mLocationName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.karahat_times);
		pref=VARIABLE.settings;
		Bundle extras = getIntent().getExtras();
		double jdr=extras.getDouble(SalatTimesMainActivity.JULIAN_DAY);
		jd = Math.round(jdr) - 0.5;
		returnCurrentJulianDay();
		setTimeFormat();
		calculateKarahatTimes(jd);
		setLatLongLocation();

		setGregorianCalender(jd);
		setHijriCalender(24);
	
	}
	private void returnCurrentJulianDay() {
		GregorianCalendar now = new GregorianCalendar();
		//jd = AstroLib.calculateJulianDay(now);
		mTimeZone= now.getTimeZone().getOffset(now.getTimeInMillis()) / 3600000;
		
	}

	
	
	private void setLatLongLocation() {
		mCityText = (TextView) findViewById(R.id.city);
		mLocationText = (TextView) findViewById(R.id.location);
		mTimeZoneText = (TextView) findViewById(R.id.timezone);

		mLocationName =pref.getString("customCity", "DefCustomCity");
	
		mCityText.setText(mLocationName);

		mLocationText.setText(twoDigitFormat.format(latitude)
				+ (latitude > 0 ? this.getString(R.string.N) : this
						.getString(R.string.S))
				+ " "
				+ twoDigitFormat.format(longitude)
				+ (longitude > 0 ? this.getString(R.string.E) : this
						.getString(R.string.W)));
		try {
			mTimeZoneText.setText(timezonefmt.format(mTimeZone));
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		}

	}
	private void calculateKarahatTimes(double jd) {
		
		latitude = Double.parseDouble(pref.getString("latitude", "39.938"));
		longitude =  Double.parseDouble(pref.getString("longitude", "32.848"));
		timezone = Double.parseDouble(pref.getString("timezone", "3.0"));
		temperature = Integer.parseInt(pref.getString("temperature","10"));
		pressure = Integer.parseInt(pref.getString("pressure", "1010"));
		altitude = Integer.parseInt(pref.getString("altitude", "0"));
		fajrAngle =  Double.parseDouble(VARIABLE.settings.getString("dawnAngle", "-18"));
		israkIsfirarAngle= Double.parseDouble(pref.getString("israkIsfirarAngle", "5.0"));

		SolarPosition solar = new SolarPosition();

		kerahat = solar.calculateKerahetTimes(jd, latitude, longitude,
				timezone, temperature, pressure, altitude, fajrAngle, israkIsfirarAngle);
		setKarahatTimesText(kerahat);


	}
	private void setKarahatTimesText(double[] kerahat) {
		
		mIshrak = (TextView) findViewById(R.id.ishrakTxtView);
		mDuha = (TextView) findViewById(R.id.duhaTxtView);
		mIstiva = (TextView) findViewById(R.id.istivaTxtView);
		mAsrsani = (TextView) findViewById(R.id.asrsaniTxtView);
		mIsfirar = (TextView) findViewById(R.id.isfirarTxtView);
		
		GregorianCalendar[] schedule=getKarahatInGregorian();
		mIshrak.setText(currentTimeFormat.format(schedule[0].getTime()));
		mDuha.setText(currentTimeFormat.format(schedule[1].getTime()));
		mIstiva.setText(currentTimeFormat.format(schedule[2].getTime()));
		mAsrsani.setText(currentTimeFormat.format(schedule[3].getTime()));
		mIsfirar.setText(currentTimeFormat.format(schedule[4].getTime()));

		
	}
    public GregorianCalendar[] getKarahatInGregorian() {
     	 
    	GregorianCalendar[] schedule = new GregorianCalendar[5];
    	int offsetAsr=pref.getInt("offsetAsr", 0);
    	int offsetIsrak=pref.getInt("offsetIsrak", 10);

    //	schedule[0]    = AstroLib.convertJulian2Gregorian(jd+kerahat[FAJR_]/24.0);
 		schedule[0]    = AstroLib.convertJulian2Gregorian(jd+kerahat[ISRAK]/24.0+offsetIsrak/(24*60.0));
 		schedule[1] = AstroLib.convertJulian2Gregorian(jd+kerahat[DUHA]/24.0);
 		schedule[2]   = AstroLib.convertJulian2Gregorian(jd+kerahat[ISTIVA]/24.0);
 		schedule[3]     = AstroLib.convertJulian2Gregorian(jd+kerahat[ASRHANEFI]/24.0+offsetAsr/(24*60.0));
 		schedule[4] = AstroLib.convertJulian2Gregorian(jd+kerahat[ISFIRAR]/24.0);
 		return schedule;

    }
	
    

	/** Called when the user clicks the calculatePreviousDay */
	public void calculatePreviousDay(View view) {
		jd--;
		calculateKarahatTimes(jd);
		setGregorianCalender(jd);
		setHijriCalender(24);

	}

	/** Called when the user clicks the calculateNextDay */
	public void calculateNextDay(View view) {
		jd++;
		calculateKarahatTimes(jd);
		setGregorianCalender(jd);
		setHijriCalender(24);

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
		mHijriDayName.setText(hc.getDay(getBaseContext()) + "");
		mHijriMonthName.setText(hc.getHijriMonthName(getBaseContext()) + "");
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
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
	private void setTimeFormat() {
		int formatIndex = Integer.parseInt(pref.getString(
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
