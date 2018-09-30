package edu.fsu.cs.mobile.testdatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewCardActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.android.cardlistsql.REPLY";

    private EditText mEditSet, mEditFront, mEditBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
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
