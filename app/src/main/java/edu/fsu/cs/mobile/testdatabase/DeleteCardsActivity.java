package edu.fsu.cs.mobile.testdatabase;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.fsu.cs.mobile.testdatabase.Database.Card;

public class DeleteCardsActivity extends AppCompatActivity implements DeleteCardsAdapter.ItemClickListener {

    CardViewModel mCardViewModel;
    DeleteCardsAdapter adapter;
    public static final int DELETE_CARDS_ACTIVITY_RESULT_CODE = 1;
    public static final String EXTRA_REPLY = "com.android.cardlistsql.REPLY";
    protected static ArrayList<Boolean> delete_states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        boolean darkModeOn = settings.getBoolean("dark_mode", false);

        if(darkModeOn)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_cards);

        Intent data = getIntent();
        String info = data.getStringExtra(CardSetActivity.EXTRA_DELETE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle( "Delete from " + info + "?" );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num_deleted = 0;
                for( int i = delete_states.size() - 1; i >= 0; i--){
                    if( delete_states.get(i) ) {
                        mCardViewModel.deleteCard(adapter.getCardAt(i).getId());
                        CardSetActivity.flip_states.remove(i);
                        num_deleted++;
                    }
                }

                Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_REPLY, num_deleted);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

        // Again, pretty similar to all the other card displays. Uses same cardList adapter
        RecyclerView recyclerView = findViewById(R.id.delete_setrecyclerview);
        int numberOfColumns = 1;   //Two columns instead of 3
        int spacing = Math.round(10 * getResources().getDisplayMetrics().density);
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(numberOfColumns, spacing, includeEdge));

        adapter = new DeleteCardsAdapter(this);

        mCardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        mCardViewModel.getSetCards(info).observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable final List<Card> cards) {
                adapter.setCards(cards);
            }
        });

        delete_states = new ArrayList<>();
        for( int i = 0; i < mCardViewModel.countCardSet(info) - 1; i++ ) {
            delete_states.add(Boolean.FALSE);
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position ) {
        adapter.flipState( position );
        adapter.notifyDataSetChanged();
    }


    // To skip slide animation when backing out of activity
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
