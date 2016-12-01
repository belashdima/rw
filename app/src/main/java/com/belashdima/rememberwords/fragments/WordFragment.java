package com.belashdima.rememberwords.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.belashdima.rememberwords.R;
import com.belashdima.rememberwords.database.DatabaseCommunicator;
import com.belashdima.rememberwords.database.DatabaseOpenHelper;
import com.belashdima.rememberwords.model.WordTranslation;

public abstract class WordFragment extends Fragment {
    protected DatabaseCommunicator databaseCommunicator;
    //protected WordTranslation wordTranslation;
    protected EditText editTextWord;
    protected EditText editTextTranslation;
    private WordInteractionListener wordInteractionListener;

    public WordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseCommunicator = new DatabaseOpenHelper(getContext());

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_word, container, false);
        editTextWord = (EditText) view.findViewById(R.id.word_edit_text);
        editTextTranslation = (EditText) view.findViewById(R.id.translation_edit_text);

        setTextToFields();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WordInteractionListener) {
            wordInteractionListener = (WordInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString() + " must implement WordInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        wordInteractionListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_word_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.word_action_done:
                saveWordToDatabase();

                return true;
            default:
                break;
        }

        return false;
    }

    protected boolean emptinessValidationPassed(String word, String translation) {
        if(word.equals("") || translation.equals("")) {
            View view = getView().findViewById(R.id.fragment_word_coordinator_layout);
            if(word.equals("") && !translation.equals("")) {
                Snackbar.make(view, getString(R.string.word_not_inputted), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                return false;
            } else if(!word.equals("") && translation.equals("")) {
                Snackbar.make(view, getString(R.string.translation_not_inputted), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                return false;
            } else {
                Snackbar.make(view, getString(R.string.word_and_translation_not_inputted), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                return false;
            }

        } else {
            return true;
        }
    }

    protected abstract void setTextToFields();

    protected abstract void saveWordToDatabase();

    public interface WordInteractionListener {
        void onDoneMenuItemClicked();
    }
}
