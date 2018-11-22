package edu.fsu.cs.mobile.testdatabase;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

// Pretty much the same as the CardListAdapter, but handles a List of Strings rather than
//      a list of Cards.
public class SetNameAdapter extends RecyclerView.Adapter<SetNameAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private List<String> mStrings;
    private ItemClickListener mClickListener;


    SetNameAdapter( Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.set_grid_item, parent, false);
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position ) {
        if( mStrings != null ){
            String current = mStrings.get( position );
            holder.myTextView.setText(current);

            if( position == MainActivity.selectedPosition )
                //Color.parseColor("#99d9e9")
                holder.myTextView.setBackgroundResource(R.color.selected_set);
            else
                holder.myTextView.setBackgroundResource(R.color.card_color);

        } else {
            holder.myTextView.setText("No Sets!");
        }
    }

    @Override
    public int getItemCount() {
        if (mStrings != null)
            return mStrings.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView myTextView;

        ViewHolder( View itemView ) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.set_grid_item);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick( View view ) {
            if (mClickListener != null ) {
                mClickListener.onItemClick( view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick( View view ) {
            if (mClickListener != null ) {
                mClickListener.onLongClick( view, getAdapterPosition());
            }
            return true;
        }
    }

    void setClickListener( ItemClickListener itemClickListener ) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick( View view, int position );
        void onLongClick( View view, int position );
    }

    void setNames(List<String> strings) {
        mStrings = strings;
        notifyDataSetChanged();
    }

    public String getNameFromPosition(int pos) { return mStrings.get(pos); }

    public boolean cardNamesContains( String setName ) { return mStrings.contains(setName); }
}
