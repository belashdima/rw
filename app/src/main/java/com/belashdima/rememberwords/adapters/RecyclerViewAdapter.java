package com.belashdima.rememberwords.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.belashdima.rememberwords.R;
import com.belashdima.rememberwords.activities.AddNewWordActivity;
import com.belashdima.rememberwords.model.AbstractLearnableItem;

import java.util.List;

/**
 * Created by belashdima on 27.02.16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public List<AbstractLearnableItem> itemsList;
    static private Context context;

    public RecyclerViewAdapter(List<AbstractLearnableItem> wtList, Context context) {
        this.itemsList=wtList;
        this.context=context;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.words_list_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AbstractLearnableItem item = itemsList.get(position);
        //holder.setId(item.getId());
        holder.mainInscriptionTextView.setText(item.getMainInscription());
        holder.auxiliaryInscriptionTextView.setText(item.getAuxiliaryInscription());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private int id;
        private TextView mainInscriptionTextView;
        private TextView auxiliaryInscriptionTextView;

        public ViewHolder(final View itemView) {
            super(itemView);

            mainInscriptionTextView = (TextView) itemView.findViewById(R.id.mainInscription_text_view);
            auxiliaryInscriptionTextView = (TextView) itemView.findViewById(R.id.auxiliaryInscription_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddNewWordActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("mainInscription", mainInscriptionTextView.getText().toString());
                    intent.putExtra("auxiliaryInscription", auxiliaryInscriptionTextView.getText().toString());
                    context.startActivity(intent);
                }
            });
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class AbstractListRecyclerViewItemDecoration extends  RecyclerView.ItemDecoration
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
