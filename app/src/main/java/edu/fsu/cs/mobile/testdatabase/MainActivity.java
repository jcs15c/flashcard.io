package edu.fsu.cs.mobile.testdatabase;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
    public static final String EXTRA_REVIEW = "com.android.cardlistsql.REVIEW";

    protected static int selectedPosition = -1;

    private String newSetName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout subMenu = findViewById(R.id.subMenu);
        getLayoutInflater().inflate(R.layout.text_hint, subMenu, true);
        getLayoutInflater().inflate(R.layout.submenu_options, subMenu, true);
        setupSubmenuButtons(); //Only initialize buttons after they're added to layout
        findViewById(R.id.submenu_table).setVisibility(View.INVISIBLE);

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
        RecyclerView recyclerView = findViewById(R.id.set_name_recyclerview);

        // The Decoration is something I found online to make the grid look more evenly spaced.
        int numberOfColumns = 3;
        int spacing = Math.round(10 * getResources().getDisplayMetrics().density);
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(numberOfColumns,
                                                                     spacing, includeEdge));

        // This adapter connects the recyclerView to the room database to display the set names
        adapter = new SetNameAdapter(this);

        // This statement is (update: REALLY) important, but I don't really know why.
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

    private void setupSubmenuButtons() {
        Button submenu_explore = (Button) findViewById(R.id.submenu_explore);
        Button submenu_review = (Button) findViewById(R.id.submenu_review);
        Button submenu_rename = (Button) findViewById(R.id.submenu_rename);
        Button submenu_delete = (Button) findViewById(R.id.submenu_delete);
        Button submenu_clone = (Button) findViewById(R.id.submenu_clone);

        submenu_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exploreIntent = new Intent( MainActivity.this, CardSetActivity.class);
                exploreIntent.putExtra(EXTRA_MESSAGE, adapter.getNameFromPosition(selectedPosition));
                startActivityForResult(exploreIntent, CARD_SET_ACTIVITY_REQUEST_CODE);            }
        });

        submenu_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewIntent = new Intent(MainActivity.this, ReviewGameActivity.class);
                reviewIntent.putExtra( EXTRA_REVIEW, adapter.getNameFromPosition(selectedPosition));
                startActivity( reviewIntent );
            }
        });

        submenu_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: prevent renaming to a duplicate, or to a whitespace only name
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Rename " + adapter.getNameFromPosition(selectedPosition) + "?");
                View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_rename_set,
                        (ViewGroup) findViewById(android.R.id.content), false);

                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                input.setHint(adapter.getNameFromPosition(selectedPosition));

                builder.setView(viewInflated);

                builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        newSetName = input.getText().toString();
                        mCardViewModel.renameSet(adapter.getNameFromPosition(selectedPosition), newSetName);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });

        submenu_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure you want to delete " + adapter.getNameFromPosition(selectedPosition) + "?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mCardViewModel.deleteSet( adapter.getNameFromPosition(selectedPosition) );
                        findViewById(R.id.submenu_table).setVisibility(View.INVISIBLE);
                        findViewById(R.id.text_hint).setVisibility(View.VISIBLE);
                        selectedPosition = -1;
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        submenu_clone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: prevent adding card if selectedSet + " - Copy" already exists
                List<Card> new_cards = mCardViewModel.getStaticSet( adapter.getNameFromPosition(selectedPosition) );
                String thisSetName = adapter.getNameFromPosition(selectedPosition);
                mCardViewModel.insertSet(thisSetName + " - Copy");
                for( Card C : new_cards ) {
                    mCardViewModel.insertCard(new Card(thisSetName, C.getFront(), C.getBack()));
                }
            }
        });
    }


    @Override
    public void onItemClick(View view, int position ) {
        // When an item is clicked, its position in the List<String> is known, which
        //      is used to pass the right data into the next activity
        findViewById(R.id.text_hint).setVisibility(View.INVISIBLE);
        findViewById(R.id.submenu_table).setVisibility(View.VISIBLE);

        selectedPosition = position;
        adapter.notifyDataSetChanged();
    }

    // This is definitely written poorly, I wasn't sure how to do the error checking
    //      one would normally do when returning from an activity.
    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == NEW_SET_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK ) {
            //TODO: prevent adding new set if setName is already the name of a set
            String setName = data.getStringExtra(NewCardActivity.EXTRA_REPLY);
            mCardViewModel.insertSet(setName);
        }
        else if( requestCode == NEW_SET_ACTIVITY_REQUEST_CODE && resultCode == RESULT_CANCELED)
        {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
        else {}
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
        if (id == R.id.delete_all_cards) {
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

        if (id == R.id.add_demo_cards) {
            addTestData();
            return true;
        }

        if (id == R.id.settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            settingsIntent.putExtra(EXTRA_MESSAGE, adapter.getNameFromPosition(selectedPosition));
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void addTestData() {
        mCardViewModel.insertCard(new Card("Animal Sounds", "", ""));
        mCardViewModel.insertCard(new Card("Animal Sounds", "Cat", "Meow"));
        mCardViewModel.insertCard(new Card("Animal Sounds", "Dog", "Woof"));
        mCardViewModel.insertCard(new Card("Animal Sounds", "Cow", "Moo"));
        mCardViewModel.insertCard(new Card("Animal Sounds", "Sheep", "Baa"));

        mCardViewModel.insertCard(new Card("Flowers", "", ""));
        mCardViewModel.insertCard(new Card("Flowers", "Daisy", "White"));
        mCardViewModel.insertCard(new Card("Flowers", "Rose", "Red"));
        mCardViewModel.insertCard(new Card("Flowers", "Violet", "Purple"));

        mCardViewModel.insertCard(new Card("Milk Kinds", "", ""));
        mCardViewModel.insertCard(new Card("Milk Kinds", "Whole", "Gross"));
        mCardViewModel.insertCard(new Card("Milk Kinds", "Chocolate", "Yum Yum"));
        mCardViewModel.insertCard(new Card("Milk Kinds", "Skim", "Please no"));
        mCardViewModel.insertCard(new Card("Milk Kinds", "Almond", "Not a cow"));
        mCardViewModel.insertCard(new Card("Milk Kinds", "Half", "Not real Milk"));
    }
}
