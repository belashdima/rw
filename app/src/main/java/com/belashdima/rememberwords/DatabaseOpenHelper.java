package com.belashdima.rememberwords;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.belashdima.rememberwords.model.AbstractLearnableItem;
import com.belashdima.rememberwords.model.WordTranslation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by belashdima on 12.02.16.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "remember_words_database";
    public static final String WORDS_TABLE_NAME = "words_table";
    public static final String[] allColumnNames = {"id", "list_id", "word", "translation", "notify_next_num", "notify_next_time", "custom_order_id"};
    // -1 in id means that activity is opened to create a new word, not to edit already existent
    public static final int ADDING_NEW_WORD_ID = -1;
    public static final int DEFAULT_LIST_ID = -1;

    public DatabaseOpenHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    public DatabaseOpenHelper(Context context, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, DATABASE_NAME, factory, version);
    }

    public void saveWord(int id, String word, String translation) {
        SQLiteDatabase database = this.getWritableDatabase();

        if(id==ADDING_NEW_WORD_ID) {
            this.insertNewWord(word, translation);
        } else {
            this.changeExistingWord(id, word, translation);

        }

        database.close();
    }

    public void insertNewWord(String word, String translation) {
        SQLiteDatabase database = this.getWritableDatabase();
        //long rowID = database.insert(DatabaseOpenHelper.WORDS_TABLE_NAME, null, cv);
        Date notifyFirstTime = NotifyTimeGetter.nextNotificationTimeGetter(1);
        String notifyFirstTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(notifyFirstTime);

        Log.i("NOTIFY FIRST TIME", notifyFirstTimeString);
        database.execSQL("INSERT INTO "+DatabaseOpenHelper.WORDS_TABLE_NAME+" ("+
                DatabaseOpenHelper.allColumnNames[1]+","+ // list_id
                DatabaseOpenHelper.allColumnNames[2]+","+ // word
                DatabaseOpenHelper.allColumnNames[3]+","+ // translation
                DatabaseOpenHelper.allColumnNames[4]+","+ // notify_next_num
                DatabaseOpenHelper.allColumnNames[5]+     // notify_next_time
                ") VALUES ('"+
                DEFAULT_LIST_ID+    // list_id
                "','"+word+         // word
                "','"+translation+"','"+  // translation
                1+                  // notify_next_num
                "','"+notifyFirstTimeString+   // notify_next_time
                "');");
        database.close();
    }

    public void changeExistingWord(int id, String word, String translation) {
        /*ContentValues cv = new ContentValues();
        cv.put("word", word);
        cv.put("translation", translation);
        cv.put("id", id);*/
        SQLiteDatabase database = this.getWritableDatabase();
        //long rowID = database.update("words_table", cv, "id="+id, null);
        database.execSQL("UPDATE "+DatabaseOpenHelper.WORDS_TABLE_NAME+" SET word='"+word+"', translation='"+translation+"' WHERE id='"+id+"';");
        database.close();
    }

    public List<AbstractLearnableItem> getItemsList() {
        List<AbstractLearnableItem> itemsList = new LinkedList<AbstractLearnableItem>();

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(DatabaseOpenHelper.WORDS_TABLE_NAME, null, null, null, null, null, "id DESC");

        if (cursor.moveToFirst())
        {
            do
            {
                int id = cursor.getInt(0);
                int listId = cursor.getInt(1);
                String word = cursor.getString(2);
                String translation = cursor.getString(3);
                int notifyNextNum = cursor.getInt(4);
                String notifyNextTime = cursor.getString(5);
                int customOrder = cursor.getInt(6);
                WordTranslation wt = new WordTranslation(id, listId, word, translation, notifyNextNum, notifyNextTime, customOrder);
                itemsList.add(wt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return itemsList;
    }

    public void insertInDatabaseDeletedWT(AbstractLearnableItem removedItem)
    {
        // supports only words
        WordTranslation removedWord = (WordTranslation) removedItem;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("INSERT INTO "+DatabaseOpenHelper.WORDS_TABLE_NAME+
                " ("+DatabaseOpenHelper.allColumnNames[0]+ //id
                ","+DatabaseOpenHelper.allColumnNames[1]+  //list_id
                ","+DatabaseOpenHelper.allColumnNames[2]+  //word
                ","+DatabaseOpenHelper.allColumnNames[3]+  //translation
                ","+DatabaseOpenHelper.allColumnNames[4]+  //notify_next_num
                ","+DatabaseOpenHelper.allColumnNames[5]+  //notify_next_time
                ","+DatabaseOpenHelper.allColumnNames[6]+") " + //custom order_id
                "VALUES ('"+
                removedItem.getId()+
                "','"+removedWord.getListId()+
                "','"+removedWord.getWord()+
                "','"+removedWord.getTranslation()+
                "','"+removedWord.getNotifyNextNum()+
                "','"+removedWord.getNotifyNextTime()+
                "','"+removedWord.getCustomOrder()+
                "');");
        sqLiteDatabase.close();
        this.close();
    }

    public ArrayList<WordTranslation> getNotifiedWordsArrayList() {
        ArrayList<WordTranslation> wordsList = new ArrayList<WordTranslation>();

        String thisMomentString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        String selection = DatabaseOpenHelper.allColumnNames[5]+ " < ?";
        String[] selectionArgs = new String[] { thisMomentString };

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DatabaseOpenHelper.WORDS_TABLE_NAME, null, selection, selectionArgs, null, null, "id DESC");
        if (cursor.moveToFirst())
        {
            do
            {
                int id = cursor.getInt(0);
                int listId = cursor.getInt(1);
                String word = cursor.getString(2);
                String translation = cursor.getString(3);
                int notifyNextNum = cursor.getInt(4);
                String notifyNextTime = cursor.getString(5);
                int customOrder = cursor.getInt(6);
                WordTranslation wt = new WordTranslation(id, listId, word, translation, notifyNextNum, notifyNextTime, customOrder);
                wordsList.add(wt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return wordsList;
    }

    public void deleteFromDatabaseDeletedWT(AbstractLearnableItem removedItem) {
        // supports only words
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+DatabaseOpenHelper.WORDS_TABLE_NAME+" WHERE id='"+removedItem.getId()+"';");
        sqLiteDatabase.close();
        this.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+WORDS_TABLE_NAME+" ("
                + allColumnNames[0]+ " integer primary key," // id
                + allColumnNames[1]+ " integer,"      // list_id
                + allColumnNames[2] + " text,"               // word
                + allColumnNames[3] + " text,"               // translation
                + allColumnNames[4] + " integer,"            // notify_next_num
                + allColumnNames[5] + " text,"               // notify_next_time
                + allColumnNames[6] /*+ " unique" */             // custom_order_id
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("RRRR", "RERERE");
    }
}
