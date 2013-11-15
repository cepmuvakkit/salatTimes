package com.cepmuvakkit.times.receiver;

import java.util.Calendar;
import com.cepmuvakkit.times.Schedule;
import com.cepmuvakkit.times.WakeLock;
import com.cepmuvakkit.times.service.StartNotificationService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.util.Log;
public class StartNotificationReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		WakeLock.acquire(context);
		context.startService(new Intent(context, StartNotificationService.class)
				.putExtras(intent));
	}

	public static void setNext(Context context) {
		Schedule today = Schedule.today();
		short nextTimeIndex = today.nextTimeIndex();
		set(context, nextTimeIndex, today.getTimes()[nextTimeIndex]);
	}

	private static void set(Context context, short timeIndex,
			Calendar actualTime) {
		if (Calendar.getInstance().after(actualTime))
			return; // Somehow current time is greater than the prayer time

		Intent intent = new Intent(context, StartNotificationReceiver.class);
		intent.putExtra("timeIndex", timeIndex);
		intent.putExtra("actualTime", actualTime.getTimeInMillis());
		Log.i("StartNotificationReceiver", "timeIndex:" + timeIndex);
		Log.i("StartNotificationReceiver", "actualTime:" + actualTime.getTime());
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, actualTime.getTimeInMillis(),
				PendingIntent.getBroadcast(context, 0, intent,
						PendingIntent.FLAG_CANCEL_CURRENT));
	}

}