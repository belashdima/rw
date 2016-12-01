package com.belashdima.rememberwords.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.belashdima.rememberwords.model.WordTranslation;

public class ModifyExistingWordFragment extends WordFragment {
    private WordTranslation wordTranslation;

    public ModifyExistingWordFragment() {
        // Required empty public constructor
    }

    public static ModifyExistingWordFragment newInstance(WordTranslation wordTranslation) {
        ModifyExistingWordFragment fragment = new ModifyExistingWordFragment();
        Bundle args = new Bundle();
        args.putParcelable("word_translation", wordTranslation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wordTranslation = getArguments().getParcelable("word_translation");
        }
    }

    @Override
    protected void setTextToFields() {
        editTextWord.setText(wordTranslation.getWord());
        editTextTranslation.setText(wordTranslation.getTranslation());
    }

    @Override
    protected void saveWordToDatabase() {
        String word = editTextWord.getText().toString();
        String translation = editTextTranslation.getText().toString();
        if (emptinessValidationPassed(word, translation)) {
            databaseCommunicator.modifyWordTranslation(wordTranslation.getId(), word, translation);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        }
    }
}
