package com.belashdima.rememberwords;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class WordsContentProvider extends ContentProvider {
    public WordsContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Log.i("Qreate", "CR");
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String query = uri.getLastPathSegment().toLowerCase();
        Log.i("QUERY", query);

        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(getContext());
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        //Cursor cursor = database.query(DatabaseOpenHelper.wordsTableName, projection, selection, selectionArgs, sortOrder);
        Cursor cursor = database.query(DatabaseOpenHelper.WORDS_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        //Log.i("COUNT", cursor.getCount()+"");
        cursor.close();
        database.close();
        databaseOpenHelper.close();

        if (selectionArgs != null && selectionArgs.length > 0 && selectionArgs[0].length() > 0) {
            // the entered text can be found in selectionArgs[0]
            // return a cursor with appropriate data
            /*DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(getContext());
            SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
            //Cursor cursor = database.query(DatabaseOpenHelper.wordsTableName, projection, selection, selectionArgs, sortOrder);
            Cursor cursor = database.query("", projection, selection, selectionArgs, null, null, sortOrder);

            cursor.close();
            database.close();
            databaseOpenHelper.close();*/

            return cursor;
        }
        else {
            // user hasn’t entered anything
            // thus return a default cursor
        }

        return cursor;
        // TODO: Implement this to handle query requests from clients.
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
