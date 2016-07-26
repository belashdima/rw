package com.belashdima.rememberwords.fragments;

import android.content.Context;
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

    public static WordsListFragment newInstance() {
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

        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(this.getActivity());
        List<AbstractLearnableItem> itemsList = databaseOpenHelper.getItemsList();
        databaseOpenHelper.close();

        // specify an adapter
        recyclerViewAdapter = new RecyclerViewAdapter(itemsList, this.getActivity());
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
        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(WordsListFragment.this.getActivity());
        databaseOpenHelper.deleteFromDatabaseDeletedWT(removedItem);
        databaseOpenHelper.close();
        recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());

        Snackbar wtDeleredSnackbar = Snackbar.make(getActivity().findViewById(R.id.words_list_recycler_view), getString(R.string.word_deleted), Snackbar.LENGTH_LONG);
        wtDeleredSnackbar.setAction(R.string.cancel_word_deletion, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(pos, removedItem);
                recyclerView.getAdapter().notifyItemInserted(pos);
                DatabaseOpenHelper databaseOpenHelperForUndo = new DatabaseOpenHelper(WordsListFragment.this.getActivity());
                databaseOpenHelperForUndo.insertInDatabaseDeletedWT(removedItem);
                databaseOpenHelperForUndo.close();
            }
        });
        wtDeleredSnackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(this.getActivity());
        ((RecyclerViewAdapter )recyclerView.getAdapter()).itemsList = databaseOpenHelper.getItemsList();
        databaseOpenHelper.close();
        recyclerView.getAdapter().notifyDataSetChanged();
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
