package edu.fsu.cs.mobile.testdatabase;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.fsu.cs.mobile.testdatabase.Database.Card;

// Pretty much identical in form to the MainActivity, but instead of displaying the name of each
//      set, it instead displays the cards themselves. As such, it uses a similar, but
//      separately defined adapter.
public class CardSetActivity extends AppCompatActivity implements CardListAdapter.ItemClickListener {

    public static final String EXTRA_REPLY = "com.android.cardlistsql.REPLY";
    public static final String EXTRA_DELETE = "com.android.cardlistsql.DELETE";
    public static final String EXTRA_EDIT= "com.android.cardlistsql.EDIT";
    public static final int NEW_CARD_ACTIVITY_REQUEST_CODE = 1;
    public static final int DELETE_CARDS_ACTIVITY_REQUEST_CODE = 2;
    public static final int EDIT_SINGLE_ACTIVITY_REQUEST_CODE = 3;
    public static final String EXTRA_SINGLE = "com.android.cardlistsql.SINGLE";


    // This could be a potential memory issue; both this and the main activity each have a
    //      separate ViewModel. I don't know if this is bad, but if we have memory problems
    //      down the line, check this first.
    private CardViewModel mCardViewModel;
    protected static ArrayList<Boolean> flip_states;
    CardListAdapter adapter;
    private int edit_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        boolean darkModeOn = settings.getBoolean("dark_mode", false);
        if(darkModeOn)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_set);
        // Receive the data from the MainActivity in order to only display cards from one set.
        Intent data = getIntent();
        String info = data.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle( info );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Floating action button goes to the New Card Activity
                Intent intent = new Intent( CardSetActivity.this, NewCardActivity.class);
                startActivityForResult(intent, NEW_CARD_ACTIVITY_REQUEST_CODE);
            }
        });


        // Most of this is identical to how it's used in the mainActivity, but with a different adapter.
        RecyclerView recyclerView = findViewById(R.id.setrecyclerview);
        int numberOfColumns = 1;   //Two columns instead of 3
        int spacing = Math.round(10 * getResources().getDisplayMetrics().density);
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(numberOfColumns, spacing, includeEdge));



        adapter = new CardListAdapter(this);

        mCardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        mCardViewModel.getSetCards(info).observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable final List<Card> cards) {
                adapter.setCards(cards);
            }
        });

        // Initialize array of "flipness", noting all cards face up at start.
        // I wish there was a way to not do this, but i cant find it.
        flip_states = new ArrayList<>();
        for( int i = 0; i < mCardViewModel.countCardSet(info) - 1; i++ ) {
            flip_states.add(Boolean.FALSE);
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        adapter.setClickListener(this);
    }

    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent inputData = getIntent();
        String setName = inputData.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if( requestCode == NEW_CARD_ACTIVITY_REQUEST_CODE ) {
            if( resultCode == RESULT_OK ) {
                String[] info = data.getStringArrayExtra(NewCardActivity.EXTRA_REPLY);
                Card card = new Card(setName, info[0], info[1]);
                if( info[0].trim().length() != 0 && info[1].trim().length() != 0 ) {
                    mCardViewModel.insertCard(card);
                    flip_states.add(Boolean.FALSE);
                }
                else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Cards cannot be empty",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        else if( requestCode == EDIT_SINGLE_ACTIVITY_REQUEST_CODE ){
            if( resultCode == RESULT_OK ) {
                String[] newInfo = data.getStringArrayExtra(EditSingleActivity.EXTRA_REPLY);
                if (newInfo[0].trim().length() != 0 && newInfo[1].trim().length() != 0) {
                    Card theCard = adapter.getCardAt(edit_pos);
                    theCard.setFront(newInfo[0]);
                    theCard.setBack(newInfo[1]);
                    mCardViewModel.updateCard(theCard);
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Cards cannot be empty",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        else if( requestCode == DELETE_CARDS_ACTIVITY_REQUEST_CODE ){
            if( resultCode == RESULT_OK ) {
                int num_deleted = data.getIntExtra(DeleteCardsActivity.EXTRA_REPLY, 0);
                Toast.makeText(
                        getApplicationContext(),
                        "Deleted " + String.valueOf(num_deleted) + " cards",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(View view, int position ) {
        adapter.flipCard(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLongClick(View view, int position ) {
        edit_pos = position;
        Card theCard = adapter.getCardAt(position);

        Intent intent = new Intent( CardSetActivity.this, EditSingleActivity.class);
        String[] info = {theCard.getFront(),
                         theCard.getBack()};

        intent.putExtra(EXTRA_SINGLE, info);
        startActivityForResult(intent, EDIT_SINGLE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent data = getIntent();

        // Don't know exactly how this works, but each one of these 'if' blocks corresponds
        //      to a single action in the menu.
        if (id == R.id.delete_cards) {
            Intent deleteIntent = new Intent(CardSetActivity.this, DeleteCardsActivity.class);
            deleteIntent.putExtra( EXTRA_DELETE, data.getStringExtra(MainActivity.EXTRA_MESSAGE) );
            startActivityForResult( deleteIntent, DELETE_CARDS_ACTIVITY_REQUEST_CODE );
            overridePendingTransition(0,0); // To skip animation
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card_set, menu);
        return true;
    }

}
