package com.belashdima.rememberwords.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.belashdima.rememberwords.R;
import com.belashdima.rememberwords.fragments.RepeatWordFragment;
import com.belashdima.rememberwords.fragments.WordsListFragment;
import com.belashdima.rememberwords.model.WordTranslation;

import java.util.ArrayList;

public class RepeatWordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_words);

        ArrayList<WordTranslation> wordTranslationArrayList = getIntent().getParcelableArrayListExtra("NOTIFIED_WORDS");

        Fragment fragment = new RepeatWordFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("NOTIFIED_WORDS", wordTranslationArrayList);

        fragment.setArguments(bundle);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.repeat_words_main_layout, fragment)
                .commit();
    }
}
