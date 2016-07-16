package com.belashdima.rememberwords.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.belashdima.rememberwords.DatabaseOpenHelper;
import com.belashdima.rememberwords.R;
import com.belashdima.rememberwords.adapters.RecyclerViewAdapter;
import com.belashdima.rememberwords.model.AbstractLearnableItem;
import com.belashdima.rememberwords.model.WordTranslation;

import java.util.LinkedList;
import java.util.List;

public class WordsListFragment extends Fragment
{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private OnFragmentInteractionListener onFragmentOnteractionListener;

    public WordsListFragment() {
        // Required empty public constructor
    }

    public static WordsListFragment newInstance(String param1, String param2) {
        WordsListFragment fragment = new WordsListFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.words_list_recycler_view);

        // use a linear layout manager
        recyclerViewLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        recyclerView.addItemDecoration(new RecyclerViewAdapter.AbstractListRecyclerViewItemDecoration());

        // specify an adapter
        recyclerViewAdapter = new RecyclerViewAdapter(getItemsList(), this.getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = viewHolder.getAdapterPosition();
                Log.i("DRAG", "OP");
                // move item in `fromPos` to `toPos` in adapter.
                /*LinkedList<WT> list = (LinkedList<WT>) ((RecyclerViewAdapter) recyclerView.getAdapter()).wtList;
                list.add(toPos, list.remove(fromPos));
                recyclerView.getAdapter().notifyItemMoved(fromPos, toPos);*/
                return true;// true if moved, false otherwise
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                onWordSwiped(viewHolder);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void onWordSwiped(RecyclerView.ViewHolder viewHolder)
    {
        final int pos= viewHolder.getAdapterPosition();
        final LinkedList<AbstractLearnableItem> list = (LinkedList<AbstractLearnableItem>) ((RecyclerViewAdapter) recyclerView.getAdapter()).itemsList;
        final AbstractLearnableItem removedItem = list.remove(viewHolder.getAdapterPosition());
        deleteFromDatabaseDeletedWT(removedItem);
        recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());

        Snackbar wtDeleredSnackbar = Snackbar.make(getActivity().findViewById(R.id.words_list_recycler_view), getString(R.string.word_deleted), Snackbar.LENGTH_LONG);
        wtDeleredSnackbar.setAction(R.string.cancel_word_deletion, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(pos, removedItem);
                recyclerView.getAdapter().notifyItemInserted(pos);
                insertInDatabaseDeletedWT(removedItem);
            }
        });
        wtDeleredSnackbar.show();
    }

    private void insertInDatabaseDeletedWT(AbstractLearnableItem removedItem)
    {
        // supports only words
        WordTranslation removedWord = (WordTranslation) removedItem;
        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = databaseOpenHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("INSERT INTO "+DatabaseOpenHelper.WORDS_TABLE_NAME+" (id,word,translation) VALUES ('"+removedItem.getId()+"','"+removedWord.getWord()+"','"+removedWord.getTranslation()+"');");
        sqLiteDatabase.close();
        databaseOpenHelper.close();
    }

    private void deleteFromDatabaseDeletedWT(AbstractLearnableItem removedItem) {
        // supports only words
        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = databaseOpenHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+DatabaseOpenHelper.WORDS_TABLE_NAME+" WHERE id='"+removedItem.getId()+"';");
        sqLiteDatabase.close();
        databaseOpenHelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((RecyclerViewAdapter )recyclerView.getAdapter()).itemsList = getItemsList();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private List<AbstractLearnableItem> getItemsList() {
        List<AbstractLearnableItem> itemsList = new LinkedList<AbstractLearnableItem>();

        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(this.getActivity());

        SQLiteDatabase database = databaseOpenHelper.getReadableDatabase();

        Cursor cursor = database.query(DatabaseOpenHelper.WORDS_TABLE_NAME, null, null, null, null, null, "id DESC");

        if (cursor.moveToFirst())
        {
            do
            {
                int id = cursor.getInt(0);
                String word = cursor.getString(1);
                String translation = cursor.getString(2);
                int notifyNextNum = cursor.getInt(3);
                String notifyNextTime = cursor.getString(4);
                int customOrder = cursor.getInt(5);
                WordTranslation wt = new WordTranslation(id, word, translation, notifyNextNum, notifyNextTime, customOrder);
                itemsList.add(wt);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        databaseOpenHelper.close();

        return itemsList;
    }

    public void onButtonPressed(Uri uri) {
        if (onFragmentOnteractionListener != null) {
            onFragmentOnteractionListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentOnteractionListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentOnteractionListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}
