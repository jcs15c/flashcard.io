package edu.fsu.cs.mobile.testdatabase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import edu.fsu.cs.mobile.testdatabase.Database.Card;

// The adapter that makes the RecyclerView "easier" to handle. Much of this is beyond what I
//      understand, particularly the part with inflaters.
public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private List<Card> mCards;

    private ItemClickListener mClickListener;

    CardListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mCards != null) {
            Card current = mCards.get(position);
            if( !CardSetActivity.flip_states.get(position) ) {
                holder.myTextView.setText(current.getFront());
                holder.myTextView.setBackgroundResource(R.drawable.flash_card_image_pale);
            }
            else {
                holder.myTextView.setText(current.getBack());
                holder.myTextView.setBackgroundResource(R.drawable.flash_card_dark_pale);            }

        } else {
            // I dont know how this would be called
            holder.myTextView.setText("No Cards");
        }
    }

    @Override
    public int getItemCount() {
        if (mCards != null)
            return mCards.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.card_grid_item);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mClickListener != null)
                mClickListener.onLongClick(view, getAdapterPosition());
            return true;
        }

    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onLongClick(View view, int position);
    }

    void setCards(List<Card> cards) {
        mCards = cards;
        notifyDataSetChanged();
    }

    public void flipCard( int pos ){
        CardSetActivity.flip_states.set(pos, !CardSetActivity.flip_states.get(pos));
    }

    public Card getCardAt(int pos) {
        return mCards.get(pos);
    }



}
