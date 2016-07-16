package com.belashdima.rememberwords;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by belashdima on 12.02.16.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper
{
    public static String databaseName="remember_words_database";
    public static String WORDS_TABLE_NAME="words_table";
    public static String[] allColumns = {"id", "word", "translation", "notify_next_num", "notify_next_time", "custom_order_id"};

    public DatabaseOpenHelper(Context context) {

        super(context, databaseName, null, 1);
    }

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, databaseName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+WORDS_TABLE_NAME+" ("
                + allColumns[0]+ " integer primary key,"
                + allColumns[1] + " text,"
                + allColumns[2] + " text,"
                + allColumns[3] + " integer,"
                + allColumns[4] + " text,"
                + allColumns[5] + " unique"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("RRRR", "RERERE");
    }
}
