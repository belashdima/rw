package com.belashdima.rememberwords.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.belashdima.rememberwords.DatabaseOpenHelper;
import com.belashdima.rememberwords.R;
import com.belashdima.rememberwords.activities.RepeatWordsActivity;
import com.belashdima.rememberwords.model.WordTranslation;
import com.belashdima.rememberwords.receivers.WordRepeatWakefulReceiver;

import java.util.ArrayList;

public class WordRepeatIntentService extends IntentService {
    public WordRepeatIntentService() {
        super("WordRepeatIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            // Do the work that requires your app to keep the CPU running.

            Log.i("Vi", "VI");
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            //v.vibrate(100);
            DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(this);
            ArrayList<WordTranslation> notifiedWordsArrayList = databaseOpenHelper.getNotifiedWordsArrayList();
            databaseOpenHelper.close();
            Log.i("WORDS", notifiedWordsArrayList.toString());
            createNotification(notifiedWordsArrayList);

            // Release the wake lock provided by the WakefulBroadcastReceiver.
            WordRepeatWakefulReceiver.completeWakefulIntent(intent);
        }
    }

    private void createNotification(ArrayList<WordTranslation> notifiedWordsArrayList) {

        Intent intent = new Intent(this, RepeatWordsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        
        intent.putParcelableArrayListExtra("NOTIFIED_WORDS", notifiedWordsArrayList);
        PendingIntent resultPendingIntent;
        resultPendingIntent = PendingIntent.getActivities(
                this,
                1,
                new Intent[] {intent},
                PendingIntent.FLAG_UPDATE_CURRENT //PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_ONE_SHOT
        );


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                R.mipmap.logo));
        mBuilder.setSmallIcon(R.mipmap.logo);
        mBuilder.setContentTitle(getString(R.string.time_to_repeat_words_title));
        mBuilder.setContentText(getString(R.string.time_to_repeat_words_title));
        mBuilder.setLights(Color.BLUE, 1000, 500);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.time_to_repeat_words_title)));
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        //mBuilder.setVibrate(new long[] { 500, 1000 });

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }
}