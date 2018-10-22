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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.fsu.cs.mobile.testdatabase.Database.Card;

// Pretty much identical in form to the MainActivity, but instead of displaying the name of each
//      set, it instead displays the cards themselves. As such, it uses a similar, but
//      separately defined adapter.
public class CardSetActivity extends AppCompatActivity implements CardListAdapter.ItemClickListener {

    public static final String EXTRA_REPLY = "com.android.cardlistsql.REPLY";
    public static final String EXTRA_DELETE = "com.android.cardlistsql.DELETE";
    public static final String EXTRA_REVIEW = "com.android.cardlistsql.REVIEW";
    public static final int NEW_CARD_ACTIVITY_REQUEST_CODE = 1;
    public static final int DELETE_CARDS_ACTIVITY_REQUEST_CODE = 1;


    // This could be a potential memory issue; both this and the main activity each have a
    //      separate ViewModel. I don't know if this is bad, but if we have memory problems
    //      down the line, check this first.
    private CardViewModel mCardViewModel;
    CardListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_set);

        // Receive the data from the MainActivity in order to only display cards from one set.
        Intent data = getIntent();
        String info = data.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if( info != null )
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
        int numberOfColumns = 2;   //Two columns instead of 3
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
            // Since NewCard activity now only asks for the front and back, use the
            //      setName from MainActivity's intent
            String[] info = data.getStringArrayExtra(NewCardActivity.EXTRA_REPLY);
            Card card = new Card(setName, info[0], info[1]);
            mCardViewModel.insertCard(card);
        }
        else if( requestCode == NEW_CARD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_CANCELED ){
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    // This is probably very bad practice, kinda defeating the point of the adapter in the first place.
    //      Should really be changed so that the adapter handles this function.
    //      Probably still pretty inefficient, even without this concern.
    //  In practice, all it does is "flip" the flashcard.
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent data = getIntent();

        // Don't know exactly how this works, but each one of these 'if' blocks corresponds
        //      to a single action in the menu.
        if (id == R.id.delete_cards) {
            //TODO: fix return error on deleteCards. Also seems kinda buggy if there are too many cards
            Intent deleteIntent = new Intent(CardSetActivity.this, DeleteCardsActivity.class);
            deleteIntent.putExtra( EXTRA_DELETE, data.getStringExtra(MainActivity.EXTRA_MESSAGE) );
            startActivity( deleteIntent );
            overridePendingTransition(0,0); // To skip animation
            return true;
        }

        if (id == R.id.review_game) {
            Intent reviewIntent = new Intent(CardSetActivity.this, ReviewGameActivity.class);
            reviewIntent.putExtra( EXTRA_REVIEW, data.getStringExtra(MainActivity.EXTRA_MESSAGE) );
            startActivity( reviewIntent );
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
