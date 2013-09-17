package com.cepmuvakkit.times;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.cepmuvakkit.times.posAlgo.AstroLib;
import com.cepmuvakkit.times.posAlgo.EarthPosition;
import com.cepmuvakkit.times.posAlgo.HigherLatitude;
import com.cepmuvakkit.times.posAlgo.Methods;
import com.cepmuvakkit.times.posAlgo.PTimes;
import com.cepmuvakkit.times.settings.Settings;

public class Schedule implements Methods, HigherLatitude {
	private GregorianCalendar[] schedule = new GregorianCalendar[14];
	private double jd, jdn;
	private static Schedule today;

	public Schedule(GregorianCalendar day) {
		GregorianCalendar[] scheduleTimes,scheduleTimesTemp = new GregorianCalendar[7];
		Settings.load(VARIABLE.settings);
		byte[] estMethod =Settings.getInstance().getEstMethods();
		byte calculationMethod = (byte) Settings.getInstance().getCalculationMethodsIndex();
		jd = AstroLib.calculateJulianDay(day);
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
		scheduleTimes = ptimes.getSalatinGregorian(Settings.getInstance().isHanafiMathab()?ASR_HANEFI:ASR_SHAFI);
		// Next fajr is tomorrow
		scheduleTimes[CONSTANT.SONRAKI_IMSAK] =ptimesNext.getJustFajrSalatinGregorian();
		scheduleTimesTemp=scheduleTimes;
	
	
		scheduleTimesTemp[CONSTANT.IMSAK].add(GregorianCalendar.MINUTE,VARIABLE.settings.getInt("ewSetFajr", -10));
		scheduleTimesTemp[CONSTANT.GUNES].add(GregorianCalendar.MINUTE,VARIABLE.settings.getInt("ewSetSunrise", -45));
		scheduleTimesTemp[CONSTANT.OGLE].add(GregorianCalendar.MINUTE,VARIABLE.settings.getInt("ewSetDhur", -10));
		scheduleTimesTemp[CONSTANT.IKINDI].add(GregorianCalendar.MINUTE,VARIABLE.settings.getInt("ewSetAsr", -10));
		scheduleTimesTemp[CONSTANT.AKSAM].add(GregorianCalendar.MINUTE,VARIABLE.settings.getInt("ewSetMagrib", -10));
		scheduleTimesTemp[CONSTANT.YATSI].add(GregorianCalendar.MINUTE,VARIABLE.settings.getInt("ewSetIsha", -10));
		scheduleTimesTemp[CONSTANT.SONRAKI_IMSAK].add(GregorianCalendar.MINUTE,VARIABLE.settings.getInt("ewSetFajr", -10));

		schedule[CONSTANT.FAJR_EW]=scheduleTimesTemp[CONSTANT.IMSAK];
		schedule[CONSTANT.FAJR]=scheduleTimes[CONSTANT.IMSAK];
		schedule[CONSTANT.SUNRISE_EW]=scheduleTimesTemp[CONSTANT.GUNES];
		schedule[CONSTANT.SUNRISE]=scheduleTimes[CONSTANT.GUNES];
		schedule[CONSTANT.DHUHR_EW]=scheduleTimesTemp[CONSTANT.OGLE];		
		schedule[CONSTANT.DHUHR]=scheduleTimes[CONSTANT.OGLE];
		schedule[CONSTANT.ASR_EW]=scheduleTimesTemp[CONSTANT.IKINDI];
		schedule[CONSTANT.ASR]=scheduleTimes[CONSTANT.IKINDI];		
		schedule[CONSTANT.MAGHRIB_EW]=scheduleTimesTemp[CONSTANT.AKSAM];
		schedule[CONSTANT.MAGHRIB]=scheduleTimes[CONSTANT.AKSAM];
		schedule[CONSTANT.ISHAA_EW]=scheduleTimesTemp[CONSTANT.YATSI];
		schedule[CONSTANT.ISHAA]=scheduleTimes[CONSTANT.YATSI];
		schedule[CONSTANT.NEXT_FAJR_EW]=scheduleTimesTemp[CONSTANT.SONRAKI_IMSAK];
		schedule[CONSTANT.NEXT_FAJR]=scheduleTimes[CONSTANT.SONRAKI_IMSAK];
	}

	public GregorianCalendar[] getTimes() {
		return schedule;
	}

	/*
	 * public boolean isExtreme(int i) { return extremes[i]; }
	 */

	public short nextTimeIndex() {
		Calendar now = new GregorianCalendar();
		if (now.before(schedule[CONSTANT.FAJR_EW]))
			return CONSTANT.FAJR_EW;
		for (short i = CONSTANT.FAJR_EW; i < CONSTANT.NEXT_FAJR; i++) {
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