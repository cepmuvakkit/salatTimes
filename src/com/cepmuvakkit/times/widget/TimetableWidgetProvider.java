package com.cepmuvakkit.times.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.cepmuvakkit.times.R;
import com.cepmuvakkit.times.CONSTANT;
import com.cepmuvakkit.times.SalatTimesMainActivity;
import com.cepmuvakkit.times.Schedule;
import com.cepmuvakkit.times.VARIABLE;
import com.cepmuvakkit.times.conversion.hicricalendar.HicriCalendar;
import com.cepmuvakkit.times.posAlgo.AstroLib;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.widget.RemoteViews;
import android.widget.TextView;

public class TimetableWidgetProvider extends AppWidgetProvider {
	private static final int[] text = new int[] { R.id.fajr_text,
			R.id.sunrise_text, R.id.dhuhr_text, R.id.asr_text,
			R.id.maghrib_text, R.id.ishaa_text, R.id.next_fajr_text };
	private static final int[] locale_text = new int[] { R.string.fajr,
			R.string.sunrise, R.string.dhuhr, R.string.asr, R.string.maghrib,
			R.string.ishaa, R.string.next_fajr };
	private static final int[] times = new int[] { R.id.fajr, R.id.sunrise,
			R.id.dhuhr, R.id.asr, R.id.maghrib, R.id.ishaa, R.id.next_fajr };
	private static final int[] am_pms = new int[] { R.id.fajr_am_pm,
			R.id.sunrise_am_pm, R.id.dhuhr_am_pm, R.id.asr_am_pm,
			R.id.maghrib_am_pm, R.id.ishaa_am_pm, R.id.next_fajr_am_pm };
	
	/*private static final int[] markers = new int[] { R.id.fajr_marker,
			R.id.sunrise_marker, R.id.dhuhr_marker, R.id.asr_marker,
			R.id.maghrib_marker, R.id.ishaa_marker, R.id.next_fajr_marker };
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		setLatestTimetable(context, appWidgetManager, appWidgetIds);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	public static void setLatestTimetable(Context context) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		int[] appWidgetIds = appWidgetManager
				.getAppWidgetIds(new ComponentName(context,
						TimetableWidgetProvider.class));
		setLatestTimetable(context, appWidgetManager, appWidgetIds);
	}

	private static void setLatestTimetable(Context context,
			AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		VARIABLE.context = context.getApplicationContext();
		VARIABLE.settings =PreferenceManager.getDefaultSharedPreferences(VARIABLE.context);
		
	//	VARIABLE.settings = VARIABLE.context.getSharedPreferences(
//		/		"settingsFile", Context.MODE_PRIVATE);
		//new LocaleManagerOwn();
		//Date today = new Date();
		Calendar now= Calendar.getInstance();
		Date today=now.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMMM");

		
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
		if (VARIABLE.settings.getInt("timeFormatIndex",
				CONSTANT.DEFAULT_TIME_FORMAT) != CONSTANT.DEFAULT_TIME_FORMAT) {
			timeFormat = new SimpleDateFormat("k:mm");
		}
		final SimpleDateFormat amPmFormat = new SimpleDateFormat("a");
		int nextTimeIndex = Schedule.today().nextTimeIndex();
		if (nextTimeIndex % 2 == 0) nextTimeIndex++;
		int index=(nextTimeIndex-1)/2;		

		final GregorianCalendar[] schedule = Schedule.today().getTimes();
		schedule[CONSTANT.MAGHRIB].get(Calendar.HOUR_OF_DAY);
		double mSunsetHour = schedule[CONSTANT.MAGHRIB]
				.get(Calendar.HOUR_OF_DAY)
				+ schedule[CONSTANT.MAGHRIB].get(Calendar.MINUTE) / 60.0;
		HicriCalendar hc = new HicriCalendar(AstroLib.calculateJulianDay(now),
				now.getTimeZone().getOffset(now.getTimeInMillis()) / 3600000,
				mSunsetHour, 0);
		
		
		for (int i = 0; i < appWidgetIds.length; i++) {
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_my_prayer);

			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					new Intent(context, SalatTimesMainActivity.class), 0);
			views.setOnClickPendingIntent(R.id.widget_my_prayer, pendingIntent);
			views.setTextViewText(R.id.gregorinCalendar,formatter.format(today));
			views.setTextViewText(R.id.hijriCalendar,hc.getHicriTakvim(context));
			for (int j = 0; j < times.length; j++) {
				
				views.setTextViewText(text[j], context.getText(locale_text[j]));
				views.setTextViewText(times[j],	timeFormat.format(schedule[2*j+1].getTime()));
				views.setTextColor(text[j], (j==index)?Color.RED:Color.WHITE);
				views.setTextColor(times[j], (j==index)?Color.RED:Color.WHITE);
				if (VARIABLE.settings.getInt("timeFormatIndex",
						CONSTANT.DEFAULT_TIME_FORMAT) == CONSTANT.DEFAULT_TIME_FORMAT) {
					views.setTextViewText(am_pms[j],
							amPmFormat.format(schedule[2*j+1].getTime()));
					views.setTextColor(am_pms[j], (j==index)?Color.RED:Color.WHITE);

				} else {

					views.setTextViewText(am_pms[j], "");
				}
	
			/*	views.setTextViewText(markers[j],(j==index)?context.getString(R.string.next_time_marker_reverse)
								: "");*/
			}
			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
		}
		
		
	}
	
	
}