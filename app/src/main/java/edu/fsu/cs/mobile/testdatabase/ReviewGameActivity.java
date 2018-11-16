package edu.fsu.cs.mobile.testdatabase;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

import edu.fsu.cs.mobile.testdatabase.Database.Card;

public class ReviewGameActivity extends AppCompatActivity {
    private CardViewModel mCardViewModel;
    List<Card> review_cards;
    ArrayList<Integer> indexes;
    ArrayList<Integer> randomIndexes;
    ArrayList<Integer> options;
    int rand_int1 = 0;
    int rand_int2 = 0;
    int rand_int3 = 0;
    int correct = 0;
    int max_size = 0;
    int i = 0;
    int correct_count = 0;
    int number_count = 0;
    double percentage = 0;
    boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_game);
        final TextView back = findViewById(R.id.review_back);
        final Button btn1 = findViewById(R.id.review_front1);
        final Button btn2 = findViewById(R.id.review_front2);
        final Button btn3 = findViewById(R.id.review_front3);
        final Button btn4 = findViewById(R.id.review_front4);
        final TextView percent = findViewById(R.id.review_percentage);
        // For the rest of the activity, only use the cards at the time of the activity's creation.
        mCardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        Intent data = getIntent();
        String setName = data.getStringExtra(MainActivity.EXTRA_REVIEW);
        review_cards = mCardViewModel.getStaticSet( setName );
        indexes = new ArrayList<Integer>();
        randomIndexes = new ArrayList<Integer>();
        options = new ArrayList<Integer>();

        //this loop creates an order of what the backs will be called in
        max_size = review_cards.size();
        for(i = 0; i < max_size; i++){
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        correct = indexes.get(0);
        indexes.remove(0);
        for(i = 0; i < max_size; i++){
            if(correct != i) {
                randomIndexes.add(i);
            }
        }
        Collections.shuffle(randomIndexes);
        rand_int1 = randomIndexes.get(0);
        rand_int2 = randomIndexes.get(1);
        rand_int3 = randomIndexes.get(2);
        options.add(rand_int1);
        options.add(rand_int2);
        options.add(rand_int3);
        options.add(correct);
        Collections.shuffle(options);
        back.setText(review_cards.get(correct).getBack());
        btn1.setText(review_cards.get(options.get(0)).getFront());
        btn2.setText(review_cards.get(options.get(1)).getFront());
        btn3.setText(review_cards.get(options.get(2)).getFront());
        btn4.setText(review_cards.get(options.get(3)).getFront());
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If front1 is equal to front to
                if(options.get(0)== correct){
                    //add to correct counter
                    //add to counter
                    correct_count++;
                    number_count++;
                    if(indexes.isEmpty()){
                        stop = true;
                    }
                }
                else{
                    //add to counter
                    //add correct index back into array
                    if(correct!=999) {
                        number_count++;
                        indexes.add(correct);
                    }
                }
                //move to next index in array
                //reset randomIndexes and options
                if(!stop) {
                    correct = indexes.get(0);
                    randomIndexes.clear();
                    options.clear();
                    indexes.remove(0);
                    for (i = 0; i < max_size; i++) {
                        if (correct != i) {
                            randomIndexes.add(i);
                        }
                    }
                    Collections.shuffle(randomIndexes);
                    rand_int1 = randomIndexes.get(0);
                    rand_int2 = randomIndexes.get(1);
                    rand_int3 = randomIndexes.get(2);
                    options.add(rand_int1);
                    options.add(rand_int2);
                    options.add(rand_int3);
                    options.add(correct);
                    Collections.shuffle(options);
                    back.setText(review_cards.get(correct).getBack());
                    btn1.setText(review_cards.get(options.get(0)).getFront());
                    btn2.setText(review_cards.get(options.get(1)).getFront());
                    btn3.setText(review_cards.get(options.get(2)).getFront());
                    btn4.setText(review_cards.get(options.get(3)).getFront());
                    percentage = ((double)Math.round(((double)correct_count / number_count)*10000)/100);
                    String temp = correct_count + "/" + number_count + " = " + percentage + "%";
                    percent.setText(temp);
                }
                else{
                    percentage = ((double)Math.round(((double)correct_count / number_count)*10000)/100);
                    String temp = correct_count + "/" + number_count + " = " + percentage + "%";
                    percent.setText(temp);
                    back.setText("Review Complete");
                    btn1.setText("");
                    btn2.setText("");
                    btn3.setText("");
                    btn4.setText("");
                    correct = 999;
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If front2 is equal to front to
                if(options.get(1)== correct){
                    //add to correct counter
                    //add to counter
                    correct_count++;
                    number_count++;
                    if(indexes.isEmpty()){
                        stop = true;
                    }
                }
                else{
                    //add to counter
                    //add correct index back into array
                    if(correct!=999) {
                        number_count++;
                        indexes.add(correct);
                    }
                }
                //move to next index in array
                //reset randomIndexes and options
                if(!stop) {
                    correct = indexes.get(0);
                    randomIndexes.clear();
                    options.clear();
                    indexes.remove(0);
                    for (i = 0; i < max_size; i++) {
                        if (correct != i) {
                            randomIndexes.add(i);
                        }
                    }
                    Collections.shuffle(randomIndexes);
                    rand_int1 = randomIndexes.get(0);
                    rand_int2 = randomIndexes.get(1);
                    rand_int3 = randomIndexes.get(2);
                    options.add(rand_int1);
                    options.add(rand_int2);
                    options.add(rand_int3);
                    options.add(correct);
                    Collections.shuffle(options);
                    back.setText(review_cards.get(correct).getBack());
                    btn1.setText(review_cards.get(options.get(0)).getFront());
                    btn2.setText(review_cards.get(options.get(1)).getFront());
                    btn3.setText(review_cards.get(options.get(2)).getFront());
                    btn4.setText(review_cards.get(options.get(3)).getFront());
                    percentage = ((double)Math.round(((double)correct_count / number_count)*10000)/100);
                    String temp = correct_count + "/" + number_count + " = " + percentage + "%";
                    percent.setText(temp);
                }
                else{
                    percentage = ((double)Math.round(((double)correct_count / number_count)*10000)/100);
                    String temp = correct_count + "/" + number_count + " = " + percentage + "%";
                    percent.setText(temp);
                    back.setText("Review Complete");
                    btn1.setText("");
                    btn2.setText("");
                    btn3.setText("");
                    btn4.setText("");
                    correct = 999;
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If front3 is equal to front to
                if(options.get(2)== correct){
                    //add to correct counter
                    //add to counter
                    correct_count++;
                    number_count++;
                    if(indexes.isEmpty()){
                        stop = true;
                    }
                }
                else{
                    //add to counter
                    //add correct index back into array
                    if(correct!=999) {
                        number_count++;
                        indexes.add(correct);
                    }
                }
                //move to next index in array
                //reset randomIndexes and options
                if(!stop) {
                    correct = indexes.get(0);
                    randomIndexes.clear();
                    options.clear();
                    indexes.remove(0);
                    for (i = 0; i < max_size; i++) {
                        if (correct != i) {
                            randomIndexes.add(i);
                        }
                    }
                    Collections.shuffle(randomIndexes);
                    rand_int1 = randomIndexes.get(0);
                    rand_int2 = randomIndexes.get(1);
                    rand_int3 = randomIndexes.get(2);
                    options.add(rand_int1);
                    options.add(rand_int2);
                    options.add(rand_int3);
                    options.add(correct);
                    Collections.shuffle(options);
                    back.setText(review_cards.get(correct).getBack());
                    btn1.setText(review_cards.get(options.get(0)).getFront());
                    btn2.setText(review_cards.get(options.get(1)).getFront());
                    btn3.setText(review_cards.get(options.get(2)).getFront());
                    btn4.setText(review_cards.get(options.get(3)).getFront());
                    percentage = ((double)Math.round(((double)correct_count / number_count)*10000)/100);
                    String temp = correct_count + "/" + number_count + " = " + percentage + "%";
                    percent.setText(temp);
                }
                else{
                    percentage = ((double)Math.round(((double)correct_count / number_count)*10000)/100);
                    String temp = correct_count + "/" + number_count + " = " + percentage + "%";
                    percent.setText(temp);
                    back.setText("Review Complete");
                    btn1.setText("");
                    btn2.setText("");
                    btn3.setText("");
                    btn4.setText("");
                    correct = 999;
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If front4 is equal to front to
                if(options.get(3)== correct){
                    //add to correct counter
                    //add to counter
                    correct_count++;
                    number_count++;
                    if(indexes.isEmpty()){
                        stop = true;
                    }
                }
                else{
                    //add to counter
                    //add correct index back into array
                    if(correct!= 999) {
                        indexes.add(correct);
                        number_count++;
                    }

                }
                //move to next index in array
                //reset randomIndexes and options
                if(!stop) {
                    correct = indexes.get(0);
                    randomIndexes.clear();
                    options.clear();
                    indexes.remove(0);
                    for (i = 0; i < max_size; i++) {
                        if (correct != i) {
                            randomIndexes.add(i);
                        }
                    }
                    Collections.shuffle(randomIndexes);
                    rand_int1 = randomIndexes.get(0);
                    rand_int2 = randomIndexes.get(1);
                    rand_int3 = randomIndexes.get(2);
                    options.add(rand_int1);
                    options.add(rand_int2);
                    options.add(rand_int3);
                    options.add(correct);
                    Collections.shuffle(options);
                    back.setText(review_cards.get(correct).getBack());
                    btn1.setText(review_cards.get(options.get(0)).getFront());
                    btn2.setText(review_cards.get(options.get(1)).getFront());
                    btn3.setText(review_cards.get(options.get(2)).getFront());
                    btn4.setText(review_cards.get(options.get(3)).getFront());
                    percentage = ((double)Math.round(((double)correct_count / number_count)*10000)/100);
                    String temp = correct_count + "/" + number_count + " = " + percentage + "%";
                    percent.setText(temp);
                }
                else{
                    percentage = ((double)Math.round(((double)correct_count / number_count)*10000)/100);
                    String temp = correct_count + "/" + number_count + " = " + percentage + "%";
                    percent.setText(temp);
                    back.setText("Review Complete");
                    btn1.setText("");
                    btn2.setText("");
                    btn3.setText("");
                    btn4.setText("");
                    correct = 999;
                }
            }
        });

    }
}
