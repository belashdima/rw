package com.belashdima.rememberwords.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.belashdima.rememberwords.NotifyTimeGetter;
import com.belashdima.rememberwords.model.AbstractLearnableItem;
import com.belashdima.rememberwords.model.WordTranslation;
import com.belashdima.rememberwords.model.WordTranslationGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by belashdima on 12.02.16.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper implements DatabaseCommunicator
{
    public static final String DATABASE_NAME = "remember_words_database";
    public static final String WORDS_TABLE_NAME = "words_table";
    public static final String GROUPS_TABLE_NAME = "groups_table";

    public static Map<WordsTableColumns, Integer> wColumnNamesMap = new HashMap<WordsTableColumns, Integer>();
    public static Map<GroupsTableColumns, Integer> gColumnNamesMap = new HashMap<GroupsTableColumns, Integer>();

    static {
        for (int i=0; i<WordsTableColumns.values().length; i++) {
            wColumnNamesMap.put(WordsTableColumns.values()[i], i);
        }

        for (int i=0; i<GroupsTableColumns.values().length; i++) {
            gColumnNamesMap.put(GroupsTableColumns.values()[i], i);
        }
    }

    public DatabaseOpenHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    public DatabaseOpenHelper(Context context, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, DATABASE_NAME, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+WORDS_TABLE_NAME+" ("
                + WordsTableColumns.ID + " integer primary key not null," // id (of word or group)
                + WordsTableColumns.PARENT_GROUP_ID + " integer,"      // list_id (of group. The root list has list_id = 0)
                + WordsTableColumns.WORD + " text,"               // word (word or group name)
                + WordsTableColumns.TRANSLATION + " text,"               // translation (translation or (optional) language name)
                + WordsTableColumns.NOTIFY_NEXT_NUMBER + " integer,"            // notify_next_num
                + WordsTableColumns.NOTIFY_NEXT_TIME + " text,"               // notify_next_time
                + WordsTableColumns.CUSTON_ORDER_ID + " integer"/*+ " unique" */             // custom_order_id
                + ");");
        sqLiteDatabase.execSQL("create table "+GROUPS_TABLE_NAME+" ("
                + GroupsTableColumns.ID + " integer primary key," // id (of word or group)
                + GroupsTableColumns.PARENT_GROUP_ID + " integer,"      // list_id (of group. The root list has list_id = 0)
                + GroupsTableColumns.NAME + " text,"               // word (word or group name)
                + GroupsTableColumns.LANGUAGE + " text"               // translation (translation or (optional) language name)
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("RRRR", "RERERE");
    }

    @Override
    public void saveNewAbstractLearnableItem(AbstractLearnableItem abstractLearnableItem) {
        // TODO: need to check if exists entry with equal word and translation, but different ids
        if(abstractLearnableItem instanceof WordTranslation) {
            WordTranslation wordTranslation = (WordTranslation) abstractLearnableItem;
            if (wordTranslation.getId()==0) {
                this.insertNewWord(wordTranslation.getListId(), wordTranslation.getWord(), wordTranslation.getTranslation());
            } else {
                //this.changeExistingWord(wordTranslation.getListId(), wordTranslation.getWord(), wordTranslation.getTranslation());
            }
        } else if (abstractLearnableItem instanceof WordTranslationGroup) {
            WordTranslationGroup wordTranslationGroup = (WordTranslationGroup) abstractLearnableItem;
            if (wordTranslationGroup.getId()==0) {
                this.insertNewWordsGroup(wordTranslationGroup.getListId(), wordTranslationGroup.getName(), wordTranslationGroup.getLanguage());
            }
        }
    }

    @Override
    public void removeAbstractLearnableItem(AbstractLearnableItem abstractLearnableItem) {
        if(abstractLearnableItem instanceof WordTranslation) {
            this.removeWord(abstractLearnableItem.getId());
        } else if (abstractLearnableItem instanceof WordTranslationGroup) {
            this.removeWordsGroup(abstractLearnableItem.getId());
        }
    }

    @Override
    public void insertAbstractLearnableItemBack(AbstractLearnableItem removedItem) {
        if(removedItem instanceof WordTranslation) {
            WordTranslation wordTranslation = (WordTranslation) removedItem;
            this.insertWord(wordTranslation);

        } else if (removedItem instanceof WordTranslationGroup) {
            WordTranslationGroup wordTranslationGroup = (WordTranslationGroup) removedItem;
            this.insertWordsGroup(wordTranslationGroup);
        }
    }

    @Override
    public void modifyWordTranslation(int id, String word, String translation) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE "+WORDS_TABLE_NAME+" SET "+
                WordsTableColumns.WORD+"='"+word+"', "+
                WordsTableColumns.TRANSLATION+"='"+translation+"' WHERE "+
                WordsTableColumns.ID+"="+id+";");
    }

    @Override
    public List<AbstractLearnableItem> getAbstractLearnableItemsByParentGroupId(int parentGroupId) {
        List<AbstractLearnableItem> itemsList = new LinkedList<AbstractLearnableItem>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor groupsCursor = database.rawQuery("SELECT * FROM "+GROUPS_TABLE_NAME+" WHERE "+GroupsTableColumns.PARENT_GROUP_ID+"="+parentGroupId+" ORDER BY "+GroupsTableColumns.NAME+" DESC;", null);
        if (groupsCursor.moveToFirst()) {
            do {
                int id = groupsCursor.getInt(gColumnNamesMap.get(GroupsTableColumns.ID));
                String name = groupsCursor.getString(gColumnNamesMap.get(GroupsTableColumns.NAME));
                String language = groupsCursor.getString(gColumnNamesMap.get(GroupsTableColumns.LANGUAGE));

                AbstractLearnableItem abstractLearnableItem = new WordTranslationGroup(id, parentGroupId, name, language, null);
                itemsList.add(abstractLearnableItem);
            } while (groupsCursor.moveToNext());
        }
        groupsCursor.close();

        Cursor wordsCursor = database.rawQuery("SELECT * FROM "+WORDS_TABLE_NAME+" WHERE "+WordsTableColumns.PARENT_GROUP_ID+"="+parentGroupId+" ORDER BY id DESC;", null);
        if (wordsCursor.moveToFirst()) {
            do {
                int id = wordsCursor.getInt(wColumnNamesMap.get(WordsTableColumns.ID));
                String word = wordsCursor.getString(wColumnNamesMap.get(WordsTableColumns.WORD));
                String translation = wordsCursor.getString(wColumnNamesMap.get(WordsTableColumns.TRANSLATION));
                int notifyNextNum = wordsCursor.getInt(wColumnNamesMap.get(WordsTableColumns.NOTIFY_NEXT_NUMBER));
                String notifyNextTime = wordsCursor.getString(wColumnNamesMap.get(WordsTableColumns.NOTIFY_NEXT_TIME));
                int customOrder = wordsCursor.getInt(wColumnNamesMap.get(WordsTableColumns.CUSTON_ORDER_ID));

                AbstractLearnableItem abstractLearnableItem = new WordTranslation(id, parentGroupId, word, translation, notifyNextNum, notifyNextTime, customOrder);
                itemsList.add(abstractLearnableItem);
            } while (wordsCursor.moveToNext());
        }
        wordsCursor.close();

        return itemsList;
    }

    @Override
    public AbstractLearnableItem getAbstractLearnableItemById(int id) {
        return null;
    }

    @Override
    public List<AbstractLearnableItem> getAbstractLearnableItemsForNotification(int id) {
        return null;
    }

    private void insertNewWord(int listId, String word, String translation) {
        SQLiteDatabase database = this.getWritableDatabase();
        //long rowID = database.insert(DatabaseOpenHelper.WORDS_TABLE_NAME, null, cv); 
        Date notifyFirstTime = NotifyTimeGetter.nextNotificationTimeGetter(1);
        String notifyFirstTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(notifyFirstTime);

        Log.i("NOTIFY FIRST TIME", notifyFirstTimeString);
        //database.beginTransaction();
        database.execSQL("INSERT INTO "+DatabaseOpenHelper.WORDS_TABLE_NAME+" ("+
                WordsTableColumns.PARENT_GROUP_ID +","+
                WordsTableColumns.WORD +","+
                WordsTableColumns.TRANSLATION +","+
                WordsTableColumns.NOTIFY_NEXT_NUMBER +","+
                WordsTableColumns.NOTIFY_NEXT_TIME +
                ") VALUES ('"+
                listId+
                "','"+word+
                "','"+translation+"','"+
                1+
                "','"+notifyFirstTimeString+
                "');");
        //database.setTransactionSuccessful();
        //database.endTransaction();
        //this.close();
    }

    private void insertNewWordsGroup(int listId, String name, String language) {
        SQLiteDatabase database = this.getWritableDatabase();

        //database.beginTransaction();
        database.execSQL("INSERT INTO "+DatabaseOpenHelper.GROUPS_TABLE_NAME+" ("+
                GroupsTableColumns.PARENT_GROUP_ID +","+
                GroupsTableColumns.NAME +","+
                GroupsTableColumns.LANGUAGE +
                ") VALUES ('"+
                listId+
                "','"+name+
                "','"+language+
                "');");
        //database.setTransactionSuccessful();
        //database.endTransaction();
        //this.close();
    }

    private void removeWord(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM "+WORDS_TABLE_NAME+" WHERE "+WordsTableColumns.ID+"="+id+";");
    }

    private void removeWordsGroup(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM "+GROUPS_TABLE_NAME+" WHERE "+GroupsTableColumns.ID+"="+id+";");
    }

    private void insertWord(WordTranslation wordTranslation) {
        SQLiteDatabase database = this.getWritableDatabase();
        //long rowID = database.insert(DatabaseOpenHelper.WORDS_TABLE_NAME, null, cv); 
        database.execSQL("INSERT INTO "+DatabaseOpenHelper.WORDS_TABLE_NAME+" ("+
                WordsTableColumns.ID +","+
                WordsTableColumns.PARENT_GROUP_ID +","+
                WordsTableColumns.WORD +","+
                WordsTableColumns.TRANSLATION +","+
                WordsTableColumns.NOTIFY_NEXT_NUMBER +","+
                WordsTableColumns.NOTIFY_NEXT_TIME +
                ") VALUES ('"+
                wordTranslation.getId()+
                "','"+wordTranslation.getListId()+
                "','"+wordTranslation.getWord()+
                "','"+wordTranslation.getTranslation()+"','"+
                wordTranslation.getNotifyNextNum()+
                "','"+wordTranslation.getNotifyNextTime()+
                "');");
    }

    private void insertWordsGroup(WordTranslationGroup wordTranslationGroup) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("INSERT INTO "+DatabaseOpenHelper.GROUPS_TABLE_NAME+" ("+
                GroupsTableColumns.ID +","+
                GroupsTableColumns.PARENT_GROUP_ID +","+
                GroupsTableColumns.NAME +","+
                GroupsTableColumns.LANGUAGE +
                ") VALUES ('"+
                wordTranslationGroup.getId()+
                "','"+wordTranslationGroup.getListId()+
                "','"+wordTranslationGroup.getName()+
                "','"+wordTranslationGroup.getLanguage()+
                "');");
    }
}
