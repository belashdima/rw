package com.belashdima.rememberwords.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.belashdima.rememberwords.DatabaseOpenHelper;
import com.belashdima.rememberwords.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewWordActivity extends AppCompatActivity {
    // -1 in id means that activity is opened to create a new word, not to edit already existent
    public static final int NEW_WORD=-1;
    private int id;
    private String word;
    private String translation;

    EditText editTextWord;
    EditText editTextTranslation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_word);

        editTextWord = (EditText) findViewById(R.id.alter_word_word_edit_text);
        editTextTranslation = (EditText) findViewById(R.id.alter_word_translation_edit_text);

        id = getIntent().getExtras().getInt("id");
        word = getIntent().getExtras().getString("mainInscription");
        translation = getIntent().getExtras().getString("auxiliaryInscription");

        editTextWord.setText(word);
        editTextTranslation.setText(translation);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.alter_word_tool bar);
        setSupportActionBar(toolbar);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_word_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_new_word_action_done) {
            return true;
        } else if (id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDoneMenuClicked(MenuItem item) {
        saveWordToDatabase();
        finish();
    }

    private void saveWordToDatabase() {
        String word = editTextWord.getText().toString();
        String translation = editTextTranslation.getText().toString();

        if(word.equals("") || translation.equals("")) {
            if(word.equals("") && !translation.equals("")) {
                Snackbar.make(findViewById(R.id.alter_word_coordinator_layout), getString(R.string.word_not_inputted), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            } else if(!word.equals("") && translation.equals("")) {
                Snackbar.make(findViewById(R.id.alter_word_coordinator_layout), getString(R.string.translation_not_inputted), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            } else {
                Snackbar.make(findViewById(R.id.alter_word_coordinator_layout), getString(R.string.word_and_translation_not_inputted), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }

        } else {
            DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(this);

            ContentValues cv = new ContentValues();
            cv.put("word", word);
            cv.put("translation", translation);

            SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();

            if(id==NEW_WORD) {
                //long rowID = database.insert(DatabaseOpenHelper.WORDS_TABLE_NAME, null, cv);
                String thisMoment=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(addMinutes(new Date(),10));
                Log.i("nrjelknv", thisMoment);
                database.execSQL("INSERT INTO "+DatabaseOpenHelper.WORDS_TABLE_NAME+" ("+
                        DatabaseOpenHelper.allColumns[1]+","+
                        DatabaseOpenHelper.allColumns[2]+","+
                        DatabaseOpenHelper.allColumns[3]+","+
                        DatabaseOpenHelper.allColumns[4]+
                        ") VALUES ('"+word+"','"+translation+"','"+1+"','"+thisMoment+"');");
                //database.execSQL("INSERT INTO "+DatabaseOpenHelper.WORDS_TABLE_NAME+" SET word='"+word+"', translation='"+translation+"' WHERE id='"+id+"';");
            } else {
                cv.put("id", id);
                //long rowID = database.update("words_table", cv, "id="+id, null);
                database.execSQL("UPDATE "+DatabaseOpenHelper.WORDS_TABLE_NAME+" SET word='"+word+"', translation='"+translation+"' WHERE id='"+id+"';");
            }

            database.close();
            databaseOpenHelper.close();
        }
    }

    public static Date addMinutes(Date date, int minutesNumber)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutesNumber); //minus number would decrement the days
        return cal.getTime();
    }
}
