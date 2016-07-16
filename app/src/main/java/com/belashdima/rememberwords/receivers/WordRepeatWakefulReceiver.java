package com.belashdima.rememberwords.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.belashdima.rememberwords.services.WordRepeatIntentService;

public class WordRepeatWakefulReceiver extends WakefulBroadcastReceiver {
    public WordRepeatWakefulReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntentServiceIntent = new Intent(context, WordRepeatIntentService.class);
        startWakefulService(context, notificationIntentServiceIntent);
    }
}
