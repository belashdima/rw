package com.belashdima.rememberwords.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.belashdima.rememberwords.database.DatabaseOpenHelper;
import com.belashdima.rememberwords.R;
import com.belashdima.rememberwords.activities.RepeatWordsActivity;
import com.belashdima.rememberwords.model.WordTranslation;

public class RepeatWordFragment extends Fragment {
    //private OnFragmentInteractionListener mListener;
    private WordTranslation wordTranslation;

    public RepeatWordFragment() {
        // Required empty public constructor
    }

    public static RepeatWordFragment newInstance(WordTranslation wordTranslation) {
        RepeatWordFragment fragment = new RepeatWordFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("NOTIFIED_WORD", wordTranslation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wordTranslation = getArguments().getParcelable("NOTIFIED_WORD");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_repeat_word, container, false);

        TextView translationTextView = (TextView) view.findViewById(R.id.translation_text_view);
        translationTextView.setText(wordTranslation.getTranslation());

        Button checkButton = (Button) view.findViewById(R.id.check_button);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckButtonClicked(wordTranslation, v);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void onCheckButtonClicked(WordTranslation wordTranslation, View v) {
        EditText wordEditText = (EditText) this.getActivity().findViewById(R.id.word_edit_text);
        String typedWord = wordEditText.getText().toString();

        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(getActivity());

        if(wordTranslation.getWord().equals(typedWord)) {
            v.setBackgroundColor(Color.GREEN);
            ((Button) v).setText("Right!");

            //databaseOpenHelper.setNextNotificationTime(wordTranslation);
        } else {
            v.setBackgroundColor(Color.RED);

            //databaseOpenHelper.setNextNotificationTimeToFirst(wordTranslation);
        }
        databaseOpenHelper.close();

        ViewPager viewPager = ((RepeatWordsActivity) getActivity()).getViewPager();
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}