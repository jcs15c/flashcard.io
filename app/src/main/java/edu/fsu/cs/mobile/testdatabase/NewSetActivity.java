package edu.fsu.cs.mobile.testdatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// This actually needs some changing. The way it's set up right now, the only way to input a
//      set of cards is by adding a card to it. Ideally, it should be allowable for a set to exist
//      while not having any card in it. One way to fix this is by maybe letting each set have
//      a card object with "null" as the front and back, and take care to not display it?
public class NewSetActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.android.cardlistsql.REPLY";

    private EditText mEditSet, mEditFront, mEditBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_set);
        mEditSet = findViewById(R.id.edit_set);
        mEditFront = findViewById(R.id.edit_front);
        mEditBack = findViewById(R.id.edit_back);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditSet.getText())   ||
                        TextUtils.isEmpty(mEditFront.getText()) ||
                        TextUtils.isEmpty(mEditBack.getText())) {
                    setResult( RESULT_CANCELED, replyIntent );
                } else {
                    String[] info = {mEditSet.getText().toString(),
                            mEditFront.getText().toString(),
                            mEditBack.getText().toString()};

                    replyIntent.putExtra(EXTRA_REPLY, info);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
