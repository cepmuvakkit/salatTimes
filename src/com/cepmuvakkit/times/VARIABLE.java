package com.cepmuvakkit.times;

import com.cepmuvakkit.times.widget.NextNotificationWidgetProvider;
import com.cepmuvakkit.times.widget.TimetableWidgetProvider;

import android.content.Context;
import android.content.SharedPreferences;

public class VARIABLE {

	public static Context context;
	public static SharedPreferences settings;
	public static boolean mainActivityIsRunning = false;

	public static float qiblaDirection = 0;

	private VARIABLE() {
		// Private constructor to enforce un-instantiability.
	}

	public static boolean alertSunrise() {
		if (settings == null)
			return false;
		return Integer.parseInt(settings.getString("notificationMethod"
				+ CONSTANT.SUNRISE, CONSTANT.NOTIFICATION_NONE + "")) != CONSTANT.NOTIFICATION_NONE;

	}

	public static void updateWidgets(Context context) {
		TimetableWidgetProvider.setLatestTimetable(context);
		NextNotificationWidgetProvider.setNextTime(context);
	}
}