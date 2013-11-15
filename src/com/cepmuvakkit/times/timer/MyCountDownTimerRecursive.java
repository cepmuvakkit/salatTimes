package com.cepmuvakkit.times.timer;

import java.util.GregorianCalendar;

import com.cepmuvakkit.times.Schedule;
import com.cepmuvakkit.times.posAlgo.AstroLib;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

public class MyCountDownTimerRecursive extends Activity {
	public TextView mTimer;
	private static  CountDownTimer countDownTimer;
	private final static long interval = 1000;

	public static void  onUpdate(TextView mTimer) {

		
		double[] salatTimes  = Schedule.today().getSalatTimes();
		double hourNow = AstroLib.getLocalHourFromGregor(new GregorianCalendar());
		int i = 0;
		while (hourNow > salatTimes[i]) {
			i++;
		}

		long timeLeftinMillis = (long) ((salatTimes[i] - hourNow) * 60 * 60 * 1000);
		if (countDownTimer != null)
			countDownTimer.cancel();
		countDownTimer = new MyCountDownTimer(timeLeftinMillis, interval,
				mTimer);

		countDownTimer.start();
	}

}
