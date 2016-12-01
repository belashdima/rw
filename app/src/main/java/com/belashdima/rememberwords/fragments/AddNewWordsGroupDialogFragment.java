package com.belashdima.rememberwords.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.belashdima.rememberwords.database.DatabaseOpenHelper;
import com.belashdima.rememberwords.R;
import com.belashdima.rememberwords.model.WordTranslationGroup;

public class AddNewWordsGroupDialogFragment extends DialogFragment {
    private OnDialogButtonClickListener onDialogButtonClickListener;
    EditText groupNameEditText;
    EditText languageEditText;

    public interface OnDialogButtonClickListener {
        public void onDialogPositiveClick();
        //public void onDialogNegativeClick(DialogFragment dialog);
    }

    public void addOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
        this.onDialogButtonClickListener = onDialogButtonClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_add_new_words_group_dialog, null);

        groupNameEditText = ((EditText) view.findViewById(R.id.add_wg_group_name_edit_text));
        languageEditText = ((EditText) view.findViewById(R.id.add_wg_language_edit_text));

        builder.setView(view);
        builder//.setMessage(R.string.dialog_fire_missiles)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onDialogButtonClickListener.onDialogPositiveClick();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        return builder.create();
    }

    public String getGroupName() {
        return groupNameEditText.getText().toString();
    }

    public String getLanguage() {
        return languageEditText.getText().toString();
    }
}
