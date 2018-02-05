package com.cepmuvakkit.times;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.SharedPreferences;

import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.EarthPosition;
import com.cepmuvakkit.times.posAlgo.HigherLatitude;
import com.cepmuvakkit.times.posAlgo.Methods;
import com.cepmuvakkit.times.posAlgo.PTimes;
import com.cepmuvakkit.times.settings.Settings;

public class Schedule implements Methods, HigherLatitude {
	private GregorianCalendar[] schedule;
	private double jd, jdn;
	private double[] salat;
	private static Schedule today;

	public Schedule(GregorianCalendar day) {
		SharedPreferences pref=VARIABLE.settings;
		byte[] estMethod = {
				Byte.parseByte(pref.getString("estMethodofFajr", NO_ESTIMATION
						+ "")),
				Byte.parseByte(pref.getString("estMethodofIsha", NO_ESTIMATION
						+ "")) };
		byte calculationMethod = Byte.parseByte(pref.getString("calculationMethodsIndex",
						CONSTANT.DEFAULT_CALCULATION_METHOD + ""));
		jd = AstroLib.calculateJulianDay(day);
		jdn = Math.round(jd) - 0.5;
		EarthPosition loc = new EarthPosition(
				Double.parseDouble(pref.getString("latitude", "39.938")),
				Double.parseDouble(pref.getString("longitude", "32.848")),
				Double.parseDouble(pref.getString("timezone", "3.0")),
				Integer.parseInt(pref.getString("altitude", "0")),
				Integer.parseInt(pref.getString("temperature","10")),
				Integer.parseInt(pref.getString("pressure", "1010")));
	
		PTimes ptimes = new PTimes(jdn, loc, calculationMethod, estMethod);
		PTimes ptimesNext = new PTimes(jdn+1, loc, calculationMethod, estMethod);
		salat=ptimes.getSalat();
		// Next fajr is tomorrow
		schedule = new GregorianCalendar[14];
		schedule[CONSTANT.FAJR_EW] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[FAJR] / 24.0
				- VARIABLE.settings.getInt("ewSetFajr", 10) / 1440.0);

		schedule[CONSTANT.FAJR] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[FAJR] / 24.0);

		schedule[CONSTANT.SUNRISE_EW] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[SUNRISE] / 24.0
				- VARIABLE.settings.getInt("ewSetSunrise", 45) / 1440.0);

		schedule[CONSTANT.SUNRISE] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[SUNRISE] / 24.0);

		schedule[CONSTANT.DHUHR_EW] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[SUNTRANSIT] / 24.0
				- VARIABLE.settings.getInt("ewSetDhuhr", 10) / 1440.0);

		schedule[CONSTANT.DHUHR] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[SUNTRANSIT] / 24.0);

		schedule[CONSTANT.ASR_EW] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[pref.getString("isHanafiMathab", "0").equals("1")? ASR_HANEFI
						: ASR_SHAFI] / 24.0
				- VARIABLE.settings.getInt("ewSetAsr", 10) / 1440.0);

		schedule[CONSTANT.ASR] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[pref.getString("isHanafiMathab", "0").equals("1") ? ASR_HANEFI
						: ASR_SHAFI] / 24.0);

		schedule[CONSTANT.MAGHRIB_EW] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[SUNSET] / 24.0
				- VARIABLE.settings.getInt("ewSetMagrib", 2) / 1440.0);

		schedule[CONSTANT.MAGHRIB] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[SUNSET] / 24.0);

		schedule[CONSTANT.ISHAA_EW] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[ISHA] / 24.0 - VARIABLE.settings.getInt("ewSetIsha", 1)
				/ 1440.0);

		schedule[CONSTANT.ISHAA] = AstroLib.convertJulian2Gregorian(jdn
				+ salat[ISHA] / 24.0);

		schedule[CONSTANT.NEXT_FAJR_EW] = AstroLib.convertJulian2Gregorian(jdn
				+ 1 + ptimesNext.getSalat()[0] / 24.0
				- VARIABLE.settings.getInt("ewSetFajr", 10) / 1440.0);

		schedule[CONSTANT.NEXT_FAJR] = AstroLib.convertJulian2Gregorian(jdn + 1
				+ ptimesNext.getSalat()[0] / 24.0);

	}

	
	public GregorianCalendar[] getTimes() {
		return schedule;
	}

	
	public double[] getSalatTimes() {
		double[] salatTimes = new double[7];
		salatTimes[CONSTANT.IMSAK] = AstroLib.getLocalHourFromGregor(schedule[CONSTANT.FAJR]);
		salatTimes[CONSTANT.GUNES] = AstroLib.getLocalHourFromGregor(schedule[CONSTANT.SUNRISE]);
		salatTimes[CONSTANT.OGLE] = AstroLib.getLocalHourFromGregor(schedule[CONSTANT.DHUHR]);
		salatTimes[CONSTANT.IKINDI] = AstroLib.getLocalHourFromGregor(schedule[CONSTANT.ASR]);
		salatTimes[CONSTANT.AKSAM] = AstroLib.getLocalHourFromGregor(schedule[CONSTANT.MAGHRIB]);
		salatTimes[CONSTANT.YATSI] = AstroLib.getLocalHourFromGregor(schedule[CONSTANT.ISHAA]);
		salatTimes[CONSTANT.SONRAKI_IMSAK] = AstroLib.getLocalHourFromGregor(schedule[CONSTANT.NEXT_FAJR])+24;// Next fajr
		return salatTimes;
	}
	/*
	 * public boolean isExtreme(int i) { return extremes[i]; }
	 */
	
	public short nextTimeIndex() {
		Calendar now = new GregorianCalendar();
		if (now.before(schedule[CONSTANT.FAJR_EW]))
			return CONSTANT.FAJR_EW;
		for (short i = CONSTANT.FAJR_EW; i < CONSTANT.NEXT_FAJR_EW; i++) {
			if (now.after(schedule[i]) && now.before(schedule[i + 1])) {
				return ++i;   
			}
		}
		return CONSTANT.NEXT_FAJR;
	}



	public static Schedule today() {
		GregorianCalendar now = new GregorianCalendar();
		if (today == null) {
			today = new Schedule(now);
		} else {
			GregorianCalendar fajr = today.getTimes()[CONSTANT.FAJR];
			if (fajr.get(Calendar.YEAR) != now.get(Calendar.YEAR)
					|| fajr.get(Calendar.MONTH) != now.get(Calendar.MONTH)
					|| fajr.get(Calendar.DAY_OF_MONTH) != now
							.get(Calendar.DAY_OF_MONTH)) {
				today = new Schedule(now);
			}
		}
		return today;
	}

	public static void setSettingsDirty() {
		today = null; // Nullifying causes a new today to be created with new
		// settings when today() is called
	}

	public static boolean settingsAreDirty() {
		return today == null;
	}

	

}