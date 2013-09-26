package com.cepmuvakkit.times.services;

import java.lang.Runnable;

import com.cepmuvakkit.times.receiver.StartNotificationReceiver;
import com.cepmuvakkit.times.Notifier;
import com.cepmuvakkit.times.R;
import com.cepmuvakkit.times.SalatTimesMainActivity;
import com.cepmuvakkit.times.VARIABLE;
import com.cepmuvakkit.times.WakeLock;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class StartNotificationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {

		/**
		 * We do the actual work in a separate thread since a Service has a
		 * limited life and we want to guarantee completion
		 */
		final class StartNotificationTask implements Runnable {

			private Context context;
			private Intent intent;

			public StartNotificationTask(Context c, Intent i) {
				context = c;
				intent = i;
			}

			public void run() {
				if (VARIABLE.settings == null)
					VARIABLE.settings = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());

				if (!VARIABLE.mainActivityIsRunning) {
					StartNotificationReceiver.setNext(context);
				} else {
					Intent i = new Intent(context, SalatTimesMainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i); // Update the gui marker and set the
										// notification for the next prayer
				}

				VARIABLE.updateWidgets(context);

				short timeIndex = intent.getShortExtra("timeIndex", (short)-1);
				long actualTime = intent.getLongExtra("actualTime", (long) 0);
				if (timeIndex == -1) { // Got here from boot
					if (VARIABLE.settings
							.getBoolean("bismillahOnBootUp", false)) {
						MediaPlayer mediaPlayer = MediaPlayer.create(context,
								R.raw.bismillah);
						mediaPlayer.setScreenOnWhilePlaying(true);
						mediaPlayer
								.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
									public void onCompletion(MediaPlayer mp) {
										WakeLock.release();
									}
								});
						mediaPlayer.start();
					} else {
						WakeLock.release();
					}
				} else {
					Notifier.start(context, timeIndex, actualTime);
					// Notify  the user  for the current time, need to	 do this last	since it releases the WakeLock
				}
			}
		}

		new Thread(new StartNotificationTask(this, intent)).start();
	}
}