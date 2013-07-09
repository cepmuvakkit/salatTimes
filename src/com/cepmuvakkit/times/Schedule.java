package com.cepmuvakkit.times;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.EarthPosition;
import com.cepmuvakkit.times.posAlgo.HigherLatitude;
import com.cepmuvakkit.times.posAlgo.Methods;
import com.cepmuvakkit.times.posAlgo.PTimes;
import com.cepmuvakkit.times.settings.Settings;

import android.content.Context;

public class Schedule implements Methods, HigherLatitude {

	private GregorianCalendar[] schedule = new GregorianCalendar[7];
	private double jd, jdn, ΔT;
	private static Schedule today;

	public Schedule(GregorianCalendar day) {
		
		Settings.load(VARIABLE.settings);
		byte[] estMethod =Settings.getInstance().getEstMethods();
		byte calculationMethod = (byte) Settings.getInstance().getCalculationMethodsIndex();
		jd = getJulianDay();
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
		schedule = ptimes.getSalatinGregorian(Settings.getInstance().isHanafiMathab()?ASR_HANEFI:ASR_SHAFI);
		ptimesNext.getJustFajrSalatinGregorian();
		// Next fajr
		// is
		// tomorrow
		schedule[CONSTANT.NEXT_FAJR] =ptimesNext.getJustFajrSalatinGregorian();

	}

	public GregorianCalendar[] getTimes() {
		return schedule;
	}

	/*
	 * public boolean isExtreme(int i) { return extremes[i]; }
	 */

	public short nextTimeIndex() {
		Calendar now = new GregorianCalendar();
		if (now.before(schedule[CONSTANT.FAJR]))
			return CONSTANT.FAJR;
		for (short i = CONSTANT.FAJR; i < CONSTANT.NEXT_FAJR; i++) {
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

	public static double getGMTOffset() {
		Calendar now = new GregorianCalendar();
		int gmtOffset = now.getTimeZone().getOffset(now.getTimeInMillis());
		return gmtOffset / 3600000;
	}

	public static boolean isDaylightSavings() {
		Calendar now = new GregorianCalendar();
		return now.getTimeZone().inDaylightTime(now.getTime());
	}

	public static double getJulianDay() {
		Calendar now = new GregorianCalendar();
		return AstroLib.calculateJulianDay(now);
		/*
		 * return AstroLib.calculateJulianDay(now.get(Calendar.YEAR), (now
		 * .get(Calendar.MONTH) + 1), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0,
		 * 0);
		 */
	}

}