package com.belashdima.rememberwords.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.belashdima.rememberwords.DatabaseOpenHelper;
import com.belashdima.rememberwords.model.AbstractLearnableItem;
import com.belashdima.rememberwords.model.WordTranslation;
import com.belashdima.rememberwords.receivers.WordRepeatWakefulReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
            ArrayList<AbstractLearnableItem> notifyList = getNotifyList();

            // Release the wake lock provided by the WakefulBroadcastReceiver.
            WordRepeatWakefulReceiver.completeWakefulIntent(intent);
        }
    }

    private ArrayList<AbstractLearnableItem> getNotifyList() {
        Log.i("Vi", "getList");
        ArrayList<AbstractLearnableItem> retList= new ArrayList<AbstractLearnableItem>();

        String thisMoment=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(this);
        SQLiteDatabase database = databaseOpenHelper.getReadableDatabase();

        //Cursor cursor = database.rawQuery("SELECT id, word, translation, notify_next_num, notify_next_time, custom_order_id FROM "+DatabaseOpenHelper.wordsTableName+" WHERE notify_next_time=", null);
        Cursor cursor = database.query(DatabaseOpenHelper.WORDS_TABLE_NAME, DatabaseOpenHelper.allColumns, DatabaseOpenHelper.allColumns[4]+"<?", new String[]{thisMoment}/*new String[]{DatabaseOpenHelper.allColumns[4]}*/, null, null, null);

        Log.i("Vi", cursor.getCount()+"");
        if (cursor.moveToFirst())
        {
            int id = cursor.getInt(0);
            String word = cursor.getString(1);
            String translation = cursor.getString(2);
            int notifyNextNum = cursor.getInt(3);
            String notifyNextTime = cursor.getString(4);
            int customOrder = cursor.getInt(5);

            do
            {
                Log.i("HHJ", id+" "+word+" "+translation+" "+notifyNextNum+" "+notifyNextTime+" "+customOrder);
                retList.add(new WordTranslation(id, word, translation, notifyNextNum, notifyNextTime, customOrder));
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        databaseOpenHelper.close();

        return retList;
    }
}