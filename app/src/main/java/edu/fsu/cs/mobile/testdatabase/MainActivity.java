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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import edu.fsu.cs.mobile.testdatabase.Database.Card;

// I've read that making the MainActivity implement the adapter is bad, but here we go
public class MainActivity extends AppCompatActivity implements SetNameAdapter.ItemClickListener {

    // Declare the ViewModel to be used in this activity.
    private CardViewModel mCardViewModel;

    // Adapter for displaying the set names of each card
    SetNameAdapter adapter;

    // Messages for various intents
    public static final int NEW_SET_ACTIVITY_REQUEST_CODE = 1;
    public static final int CARD_SET_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_MESSAGE = "edu.fsu.cs.mobile.testdatabase.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FAB goes to the new set activity, which is then added to the recyclerView
                Intent intent = new Intent( MainActivity.this, NewSetActivity.class);
                startActivityForResult(intent, NEW_SET_ACTIVITY_REQUEST_CODE);
            }
        });

        // Display uses a recyclerView to better interact with the LiveData returned by Room
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        // The Decoration is something I found online to make the grid look more evenly spaced.
        int numberOfColumns = 3;
        int spacing = Math.round(10 * getResources().getDisplayMetrics().density);
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(numberOfColumns,
                                                                     spacing, includeEdge));

        // This adapter connects the recyclerView to the room database to display the set names
        adapter = new SetNameAdapter(this);

        // This statement is important, but I don't really know why.
        mCardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        // Setup an observer on the setNames LiveData object, changing the display if it does.
        mCardViewModel.getSetNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable final List<String> names) {
                adapter.setNames(names);
            }
        });

        // Connect the adapter to the recyclerView, which is declared to be a grid.
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        // Adapter has within it the logic to manage the onClickListener of each grid square
        adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position ) {
        // When an item is clicked, its position in the List<String> is known, which
        //      is used to pass the right data into the next activity
        Intent intent = new Intent( MainActivity.this, CardSetActivity.class);
        intent.putExtra(EXTRA_MESSAGE, adapter.getNameFromPosition(position));
        startActivityForResult(intent, CARD_SET_ACTIVITY_REQUEST_CODE);
    }

    // This is definitely written poorly, I wasn't sure how to do the error checking
    //      one would normally do when returning from an activity.
    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == NEW_SET_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK ) {
            // If we come from the NewSetActivity, we need to add a new card. Needs fixing
            String[] info = data.getStringArrayExtra(NewCardActivity.EXTRA_REPLY);
            Card card = new Card(info[0], info[1], info[2]);
            mCardViewModel.insertCard(card);
        }
        else if( requestCode == CARD_SET_ACTIVITY_REQUEST_CODE ) {}
        else {
            Toast.makeText(
                    getApplicationContext(),
                    "Set must have at least one card",
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Don't know exactly how this works, but each one of these 'if' blocks corresponds
        //      to a single action in the menu.
        if (id == R.id.delete_settings) {
            mCardViewModel.deleteAllCards();
            return true;
        }

        if (id == R.id.count_settings) {
            Toast.makeText(
                    getApplicationContext(),
                    Integer.toString(mCardViewModel.countAllCards()),
                    Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
