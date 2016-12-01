package com.belashdima.rememberwords.adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.belashdima.rememberwords.R;
import com.belashdima.rememberwords.fragments.ALIListFragment;
import com.belashdima.rememberwords.model.AbstractLearnableItem;
import com.belashdima.rememberwords.model.WordTranslationGroup;

import java.util.List;

/**
 * Created by belashdima on 27.02.16.
 */
public class ALIRecyclerViewAdapter extends RecyclerView.Adapter<ALIRecyclerViewAdapter.ALIViewHolder> {

    public List<AbstractLearnableItem> itemsList;
    private ALIListFragment.ALIListInteractionListener aliListInteractionListener;

    public ALIRecyclerViewAdapter(List<AbstractLearnableItem> itemsList, ALIListFragment.ALIListInteractionListener aliListInteractionListener) {
        this.itemsList=itemsList;
        this.aliListInteractionListener=aliListInteractionListener;
    }

    @Override
    public ALIRecyclerViewAdapter.ALIViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.words_list_recyclerview_item, parent, false);

        final ALIViewHolder viewHolder = new ALIViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aliListInteractionListener.onALIOpenClicked(itemsList.get(viewHolder.getAdapterPosition()));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ALIViewHolder viewHolder, int position) {
        AbstractLearnableItem item = itemsList.get(position);
        //viewHolder.setId(position);// did not understand why not
        //viewHolder.setId(viewHolder.getAdapterPosition());
        viewHolder.mainInscriptionTextView.setText(item.getMainInscription());
        viewHolder.auxiliaryInscriptionTextView.setText(item.getAuxiliaryInscription());

        if (item instanceof WordTranslationGroup) {
            viewHolder.folderImageImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public static class ALIViewHolder extends RecyclerView.ViewHolder {
        //private int id;
        private ImageView folderImageImageView;
        private TextView mainInscriptionTextView;
        private TextView auxiliaryInscriptionTextView;

        public ALIViewHolder(final View itemView) {
            super(itemView);

            folderImageImageView = (ImageView) itemView.findViewById(R.id.folderImage_image_view);
            mainInscriptionTextView = (TextView) itemView.findViewById(R.id.mainInscription_text_view);
            auxiliaryInscriptionTextView = (TextView) itemView.findViewById(R.id.auxiliaryInscription_text_view);
        }

        /*public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }*/
    }

    public static class AbstractListRecyclerViewItemDecoration extends RecyclerView.ItemDecoration
    {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.top = 6;
            outRect.bottom = 6;
            outRect.left = 12;
            outRect.right = 12;
        }
    }
}
