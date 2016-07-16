package com.belashdima.rememberwords.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, WordRepeatWakefulReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_ONE_SHOT);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                60000, alarmPendingIntent);
    }
}
