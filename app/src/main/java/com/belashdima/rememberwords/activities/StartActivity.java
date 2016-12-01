package com.belashdima.rememberwords.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.belashdima.rememberwords.R;
import com.belashdima.rememberwords.adapters.ALIRecyclerViewAdapter;
import com.belashdima.rememberwords.database.DatabaseCommunicator;
import com.belashdima.rememberwords.database.DatabaseOpenHelper;
import com.belashdima.rememberwords.fragments.ALIListFragment;
import com.belashdima.rememberwords.fragments.AddNewWordFragment;
import com.belashdima.rememberwords.fragments.AddNewWordsGroupDialogFragment;
import com.belashdima.rememberwords.fragments.LearnNowFragment;
import com.belashdima.rememberwords.fragments.ModifyExistingWordFragment;
import com.belashdima.rememberwords.model.AbstractLearnableItem;
import com.belashdima.rememberwords.model.WordTranslation;
import com.belashdima.rememberwords.model.WordTranslationGroup;
import com.belashdima.rememberwords.receivers.WordRepeatWakefulReceiver;

import java.util.List;

public class StartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ALIListFragment.ALIListInteractionListener {

    private ActionBarDrawerToggle toggle;

    DatabaseCommunicator databaseCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //deleteDatabase(DatabaseOpenHelper.DATABASE_NAME);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, AddNewWordActivity.class);
                //intent.putExtra("wordModificationType", DatabaseCommunicator.WordModificationType.ADD_NEW_WORD);
                startActivity(intent);
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        //toggle.syncState();

        showDefaultFragment();

        Intent alarmIntent = new Intent(this, WordRepeatWakefulReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                60000, alarmPendingIntent);


        /*for(int i = 0; i<35; i++){
            DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(this);
            databaseOpenHelper.insertNewWord(0, "wor", "tra");
            databaseOpenHelper.close();}*/

        databaseCommunicator = new DatabaseOpenHelper(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_menu, menu);

        // Associate searchable configuration with the SearchView
        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.start_menu_action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_words_list)
        {
            //fragment = new WordsListFragment();
            fragment = ALIListFragment.newInstance(0);
        } else if (id == R.id.nav_learn_now) {
            fragment = new LearnNowFragment();
        } else if (id == R.id.nav_how_to_use) {
            //fragment = new how;
        } else if (id == R.id.nav_settings) {
            Intent intent=new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {

        } else {
            fragment = new ALIListFragment();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.start_main_layout, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        NavigationView nv= (NavigationView) findViewById(R.id.navigation_view);
        nv.setCheckedItem(id);
        //listview.setItemChecked(position, true);
        //setTitle(mPlanetTitles[position]);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(nv);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onSortMenuItemClicked(MenuItem item)
    {
        /*View mv= findViewById(R.id.start_menu_action_sort);
        PopupMenu popup = new PopupMenu(StartActivity.this, mv);
        popup.getMenuInflater().inflate(R.menu.start_action_sort_popup_menu, popup.getMenu());


        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(StartActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();//showing popup menu*/
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void showDefaultFragment() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(0);

        Fragment fragment = new ALIListFragment();
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.start_main_layout, fragment)
                .commit();
    }

    @Override
    public void onAddNewWordClicked(int groupId) {
        // show AddNewWordFragment (with slide)
        Fragment fragment = AddNewWordFragment.newInstance(groupId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(AddNewWordFragment.class.getName());
        fragmentTransaction.replace(R.id.start_main_layout, fragment).commit();
    }

    @Override
    public void onAddNewWordGroupClicked(final int groupId, final RecyclerView recyclerView) {
        final AddNewWordsGroupDialogFragment dialogFragment = new AddNewWordsGroupDialogFragment();
        dialogFragment.addOnDialogButtonClickListener(new AddNewWordsGroupDialogFragment.OnDialogButtonClickListener() {
            @Override
            public void onDialogPositiveClick() {
                String groupName = dialogFragment.getGroupName();
                String language = dialogFragment.getLanguage();
                WordTranslationGroup wordTranslationGroup = new WordTranslationGroup(0, groupId, groupName, language, null);

                databaseCommunicator.saveNewAbstractLearnableItem(wordTranslationGroup);

                //refresh list
                List<AbstractLearnableItem> itemsList = databaseCommunicator.getAbstractLearnableItemsByParentGroupId(groupId);
                recyclerView.setAdapter(new ALIRecyclerViewAdapter(itemsList, StartActivity.this));
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "AddNewWordsGroupDialogFragment");
    }

    @Override
    public void onALIOpenClicked(AbstractLearnableItem abstractLearnableItem) {
        if(abstractLearnableItem instanceof WordTranslation) {
            WordTranslation wordTranslation = (WordTranslation) abstractLearnableItem;

            Fragment fragment = ModifyExistingWordFragment.newInstance(wordTranslation);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(ModifyExistingWordFragment.class.getName());
            fragmentTransaction.replace(R.id.start_main_layout, fragment).commit();
        } else if(abstractLearnableItem instanceof WordTranslationGroup) {
            // show ALIs in this group
            Fragment fragment = ALIListFragment.newInstance(abstractLearnableItem.getId());

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(ALIListFragment.class.getName())
                    .replace(R.id.start_main_layout, fragment)
                    .commit();
        }
    }

    @Override
    public void onALILongClicked(AbstractLearnableItem abstractLearnableItem) {
        //
    }
}
