package com.belashdima.rememberwords.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.belashdima.rememberwords.database.DatabaseOpenHelper;
import com.belashdima.rememberwords.R;
import com.belashdima.rememberwords.fragments.RepeatWordFragment;
import com.belashdima.rememberwords.model.WordTranslation;

import java.util.ArrayList;

public class RepeatWordsActivity extends FragmentActivity {

    private ViewPager viewPager;
    private PagerAdapter viewPagerAdapter;

    ArrayList<WordTranslation> wordTranslationArrayList;

    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_words);

        wordTranslationArrayList = getIntent().getParcelableArrayListExtra("NOTIFIED_WORDS");

        // for development
        /*if(wordTranslationArrayList == null) {
            DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(this);
            wordTranslationArrayList = databaseOpenHelper.getNotifiedWordsArrayList();
            databaseOpenHelper.close();
        }
        if (wordTranslationArrayList.isEmpty()) {
            DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(this);
            databaseOpenHelper.insertStubWords();
            databaseOpenHelper.close();
            databaseOpenHelper = new DatabaseOpenHelper(this);
            wordTranslationArrayList = databaseOpenHelper.getNotifiedWordsArrayList();
            databaseOpenHelper.close();
        }*/
        //

        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = (ViewPager) findViewById(R.id.repeat_words_view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //return new RepeatWordFragment();
            /*Bundle bundle = new Bundle();
            bundle.putParcelable("NOTIFIED_WORD", wordTranslationArrayList.get(position));
            Fragment fragment = new RepeatWordFragment();
            fragment.setArguments(bundle);
            return fragment;*/
            return RepeatWordFragment.newInstance(wordTranslationArrayList.get(position));
        }

        @Override
        public int getCount() {
            return wordTranslationArrayList.size();
        }
    }
}
