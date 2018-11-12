package edu.fsu.cs.mobile.testdatabase;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.fsu.cs.mobile.testdatabase.Database.Card;

public class EditCardsActivity extends AppCompatActivity implements CardListAdapter.ItemClickListener {

    CardViewModel mCardViewModel;
    CardListAdapter adapter;

    public static final int EDIT_SINGLE_ACTIVITY_REQUEST_CODE = 1;
    public static final String EXTRA_SINGLE = "com.android.cardlistsql.SINGLE";
    int pos;
    String setName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent data = getIntent();
        setName = data.getStringExtra(CardSetActivity.EXTRA_EDIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cards);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if( setName != null )
            getSupportActionBar().setTitle( "Edit which cards in " + setName + "?" );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);  //May or May not do anything
                finish();
            }
        });

        // Again, pretty similar to all the other card displays. Uses same cardList adapter
        RecyclerView recyclerView = findViewById(R.id.edit_setrecyclerview);
        int numberOfColumns = 1;   //One columns instead of 3
        int spacing = Math.round(10 * getResources().getDisplayMetrics().density);
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(numberOfColumns, spacing, includeEdge));

        adapter = new CardListAdapter(this);

        mCardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        mCardViewModel.getSetCards(setName).observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable final List<Card> cards) {
                adapter.setCards(cards);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position ) {
        Card theCard = adapter.getCardAt(position);
        pos = position;

        Intent intent = new Intent( EditCardsActivity.this, EditSingleActivity.class);
        String[] info = {theCard.getFront(),
                         theCard.getBack()};

        intent.putExtra(EXTRA_SINGLE, info);
        startActivityForResult(intent, EDIT_SINGLE_ACTIVITY_REQUEST_CODE);

    }

    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Card theCard = adapter.getCardAt(pos);

        if( requestCode == EDIT_SINGLE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK ) {
            // Since NewCard activity now only asks for the front and back, use the
            //      setName from MainActivity's intent
            String[] newInfo = data.getStringArrayExtra(EditSingleActivity.EXTRA_REPLY);
            theCard.setFront(newInfo[0]);
            theCard.setBack(newInfo[1]);
            mCardViewModel.updateCard(theCard);
        }
        else if( requestCode == EDIT_SINGLE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_CANCELED ){
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    // To skip slide animation when backing out of activity
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
