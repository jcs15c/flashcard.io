package edu.fsu.cs.mobile.testdatabase;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import edu.fsu.cs.mobile.testdatabase.Database.Card;

public class ReviewGameActivity extends AppCompatActivity {
    private CardViewModel mCardViewModel;
    List<Card> review_cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_game);

        // For the rest of the activity, only use the cards at the time of the activity's creation.
        mCardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        Intent data = getIntent();
        String setName = data.getStringExtra(MainActivity.EXTRA_REVIEW);
        review_cards = mCardViewModel.getStaticSet( setName );

    }
}
