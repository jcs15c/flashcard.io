package edu.fsu.cs.mobile.testdatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.fsu.cs.mobile.testdatabase.Database.Card;

// Activity to add a user-inputted card to the database
public class EditSingleActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.android.cardlistsql.REPLY";

    private EditText mEditFront, mEditBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        Intent data = getIntent();
        String[] info = data.getStringArrayExtra(EditCardsActivity.EXTRA_SINGLE);

        mEditFront = findViewById(R.id.edit_front);
        mEditBack = findViewById(R.id.edit_back);

        mEditFront.setText(info[0]);
        mEditBack.setText(info[1]);

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
                finish();
            }
        });
    }
}
