package com.cepmuvakkit.times.receiver;

import com.cepmuvakkit.times.Notifier;
import com.cepmuvakkit.times.SalatTimesMainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ClickNotificationReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Notifier.stop();
		
		Intent i = new Intent(context, SalatTimesMainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
}