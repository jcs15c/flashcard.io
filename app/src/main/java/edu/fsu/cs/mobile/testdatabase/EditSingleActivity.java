package edu.fsu.cs.mobile.testdatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// Activity to add a user-inputted card to the database
public class EditSingleActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.android.cardlistsql.REPLY";

    private EditText mEditFront, mEditBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        mEditFront = findViewById(R.id.edit_front);
        mEditBack = findViewById(R.id.edit_back);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                // disallows card with only whitespace
                if ( mEditFront.getText().toString().trim().length() == 0 ||
                        mEditBack.getText().toString().trim().length() == 0) {
                    setResult( RESULT_CANCELED, replyIntent );
                } else {
                    String[] info = {mEditFront.getText().toString(),
                            mEditBack.getText().toString()};

                    replyIntent.putExtra(EXTRA_REPLY, info);
                    setResult(RESULT_OK, replyIntent);
                }
                // Don't know what finish() does. Sorry.
                finish();
            }
        });
    }
}
