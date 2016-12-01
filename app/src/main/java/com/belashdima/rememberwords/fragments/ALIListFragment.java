package com.belashdima.rememberwords.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.belashdima.rememberwords.R;
import com.belashdima.rememberwords.adapters.ALIRecyclerViewAdapter;
import com.belashdima.rememberwords.database.DatabaseCommunicator;
import com.belashdima.rememberwords.database.DatabaseOpenHelper;
import com.belashdima.rememberwords.model.AbstractLearnableItem;

import java.util.LinkedList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class ALIListFragment extends Fragment
{
    private RecyclerView recyclerView;
    private ALIListInteractionListener aliListInteractionListener;
    private DatabaseCommunicator databaseCommunicator;

    private int groupId;

    public ALIListFragment() {
        // Required empty public constructor
    }

    public static ALIListFragment newInstance(int groupId) {
        ALIListFragment fragment = new ALIListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("group_id", groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            groupId = getArguments().getInt("group_id");
        }

        databaseCommunicator = new DatabaseOpenHelper(getActivity());
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        recyclerView.addItemDecoration(new ALIRecyclerViewAdapter.AbstractListRecyclerViewItemDecoration());

        List<AbstractLearnableItem> itemsList = databaseCommunicator.getAbstractLearnableItemsByParentGroupId(groupId);

        // specify an adapter
        recyclerView.setAdapter(new ALIRecyclerViewAdapter(itemsList, (ALIListInteractionListener) (this.getActivity())));

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = viewHolder.getAdapterPosition();
                Log.i("DRAG", "OP");
                // move item in `fromPos` to `toPos` in adapter.
                /*LinkedList<AbstractLearnableItem> list = (LinkedList<AbstractLearnableItem>) ((ALIRecyclerViewAdapter) recyclerView.getAdapter()).itemsList;
                list.add(toPos, list.remove(fromPos));
                recyclerView.getAdapter().notifyItemMoved(fromPos, toPos);*/
                return true;// true if moved, false otherwise
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                onItemSwiped(viewHolder);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        /*FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNewWordActivity.class);
                intent.putExtra("id", DatabaseOpenHelper.ADDING_NEW_WORD_ID);
                intent.putExtra("mainInscription", "");
                intent.putExtra("auxiliaryInscription", "");
                startActivity(intent);
            }
        });*/
        FabSpeedDial fabSpeedDial = (FabSpeedDial) getActivity().findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                String menuTitle = menuItem.getTitle().toString();
                if(menuTitle.equals("Word")) {
                    openAddNewWordFragment();
                } else if (menuTitle.equals("Group of words")) {
                    openAddNewWordGroupDialogFragment();
                }

                return super.onMenuItemSelected(menuItem);
            }
        });
    }

    private void openAddNewWordFragment() {
        if(aliListInteractionListener != null) {
            aliListInteractionListener.onAddNewWordClicked(groupId);
        }
    }

    private void openAddNewWordGroupDialogFragment() {
        if(aliListInteractionListener != null) {
            aliListInteractionListener.onAddNewWordGroupClicked(groupId, recyclerView);
        }
    }

    private void onItemSwiped(RecyclerView.ViewHolder viewHolder)
    {
        final int pos = viewHolder.getAdapterPosition();
        final List<AbstractLearnableItem> list = ((ALIRecyclerViewAdapter) recyclerView.getAdapter()).itemsList;

        final AbstractLearnableItem removedItem = list.remove(viewHolder.getAdapterPosition());

        databaseCommunicator.removeAbstractLearnableItem(removedItem);
        recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());

        Snackbar wtDeletedSnackbar = Snackbar.make(getActivity().findViewById(R.id.words_list_recycler_view), getString(R.string.word_deleted), Snackbar.LENGTH_LONG);
        wtDeletedSnackbar.setAction(R.string.cancel_word_deletion, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(pos, removedItem);
                recyclerView.getAdapter().notifyItemInserted(pos);
                databaseCommunicator.insertAbstractLearnableItemBack(removedItem);
            }
        });
        wtDeletedSnackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        //((RecyclerViewAdapter )recyclerView.getAdapter()).itemsList = databaseOpenHelper.getAbstractLearnableItemsByParentGroupId(groupId);
        //((RecyclerViewAdapter )recyclerView.getAdapter()).itemsList = databaseOpenHelper.allWords();
        ((ALIRecyclerViewAdapter) recyclerView.getAdapter()).itemsList = databaseCommunicator.getAbstractLearnableItemsByParentGroupId(groupId);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ALIListInteractionListener) {
            aliListInteractionListener = (ALIListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        aliListInteractionListener = null;
    }

    public interface ALIListInteractionListener {
        void onAddNewWordClicked(int groupId);

        void onAddNewWordGroupClicked(int groupId, RecyclerView recyclerView);

        void onALIOpenClicked(AbstractLearnableItem abstractLearnableItem);

        void onALILongClicked(AbstractLearnableItem abstractLearnableItem);
    }

}
