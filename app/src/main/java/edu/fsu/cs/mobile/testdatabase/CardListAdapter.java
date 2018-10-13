package edu.fsu.cs.mobile.testdatabase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.fsu.cs.mobile.testdatabase.Model.Card;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private List<Card> mCards;
    private ItemClickListener mClickListener;

    CardListAdapter( Context context) {
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_grid_item, parent, false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position ) {
        if( mCards != null ){
            Card current = mCards.get( position );
            holder.myTextView.setText(current.getFront());
        } else {
            holder.myTextView.setText("No Cards");
        }
    }

    @Override
    public int getItemCount() {
        if (mCards != null)
            return mCards.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder( View itemView ) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.card_grid_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick( View view ) {
            if (mClickListener != null )
                mClickListener.onItemClick( view, getAdapterPosition());
        }
    }

    void setClickListener( ItemClickListener itemClickListener ) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick( View view, int position );
    }

    void setCards(List<Card> cards) {
        mCards = cards;
        notifyDataSetChanged();
   }
}
