
package com.cepmuvakkit.times.timer;

import android.os.CountDownTimer;
import android.widget.TextView;

public class MyCountDownTimer extends CountDownTimer {
	public static TextView mTimer;
	public MyCountDownTimer(long startTime, long interval,TextView mTimer) {
		super(startTime, interval);
		MyCountDownTimer.mTimer = mTimer;
	}

	public void onFinish() {
		mTimer.setText("Vakit Tamam!");//Sabah Namazında Namaz vakti demek olmuyor
		//MyCountDownTimerRecursive.onUpdate(mTimer);
	}
	public static String intTwoDigit(int i) {
		return ((i < 10) ? "0" : "") + i;
	}

	public void onTick(long millisUntilFinished) {

		int Hour = (int) (millisUntilFinished / 3600000L);
		int Minute = (int) (millisUntilFinished / 60000L % 60L);
		int Seconds = (int) (millisUntilFinished / 1000L % 60L);
		mTimer.setText(intTwoDigit(Hour) + ":" + intTwoDigit(Minute) + ":"
				+ intTwoDigit(Seconds));
		// mTimer.setText("" + millisUntilFinished / 1000);
	}
}

