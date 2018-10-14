package edu.fsu.cs.mobile.testdatabase;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.fsu.cs.mobile.testdatabase.Database.Card;

public class CardSetActivity extends AppCompatActivity implements CardListAdapter.ItemClickListener {

    public static final String EXTRA_REPLY = "com.android.cardlistsql.REPLY";
    public static final int NEW_CARD_ACTIVITY_REQUEST_CODE = 1;
    private CardViewModel mCardViewModel;
    CardListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent data = getIntent();
        setContentView(R.layout.activity_card_set);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( CardSetActivity.this, NewCardActivity.class);
                startActivityForResult(intent, NEW_CARD_ACTIVITY_REQUEST_CODE);
            }
        });


        String info = data.getStringExtra(MainActivity.EXTRA_MESSAGE);

        RecyclerView recyclerView = findViewById(R.id.setrecyclerview);
        int numberOfColumns = 2;
        int spacing = Math.round(10 * getResources().getDisplayMetrics().density);
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(numberOfColumns, spacing, includeEdge));

        adapter = new CardListAdapter(this);

        mCardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        mCardViewModel.getSet(info).observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable final List<Card> cards) {
                adapter.setCards(cards);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        adapter.setClickListener(this);

    }

    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent inputData = getIntent();
        String setName = inputData.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if( requestCode == NEW_CARD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK ) {
            String[] info = data.getStringArrayExtra(NewCardActivity.EXTRA_REPLY);
            Card card = new Card(setName, info[0], info[1]);
            mCardViewModel.insertCard(card);
        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(View view, int position ) {
        RecyclerView recyclerView = findViewById(R.id.setrecyclerview);
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
        TextView textView = holder.itemView.findViewById(R.id.card_grid_item);

        String currentBack = adapter.getCards().get(position).getBack();
        String currentFront = adapter.getCards().get(position).getFront();

        if ( currentFront.equals(textView.getText()) )
            textView.setText(currentBack);
        else
            textView.setText(currentFront);
    }
}
