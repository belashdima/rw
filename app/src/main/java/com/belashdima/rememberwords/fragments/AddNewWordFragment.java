package com.belashdima.rememberwords.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.belashdima.rememberwords.model.WordTranslation;

public class AddNewWordFragment extends WordFragment {
    private int groupId;

    public AddNewWordFragment() {
        // Required empty public constructor
    }

    public static AddNewWordFragment newInstance(int groupId) {
        AddNewWordFragment fragment = new AddNewWordFragment();
        Bundle args = new Bundle();
        args.putInt("group_id", groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupId = getArguments().getInt("group_id");
        }
    }

    @Override
    protected void setTextToFields() {
        editTextWord.setText("");
        editTextTranslation.setText("");
    }

    @Override
    protected void saveWordToDatabase() {
        String word = editTextWord.getText().toString();
        String translation = editTextTranslation.getText().toString();
        if (emptinessValidationPassed(word, translation)) {
            databaseCommunicator.saveNewAbstractLearnableItem(new WordTranslation(0, groupId, word, translation, 0, null, 0));

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        }
    }
}
