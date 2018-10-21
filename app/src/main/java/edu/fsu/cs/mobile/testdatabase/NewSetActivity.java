package edu.fsu.cs.mobile.testdatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewSetActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.android.cardlistsql.REPLY";

    private EditText mEditSet, mEditFront, mEditBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_set);
        mEditSet = findViewById(R.id.edit_set);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (mEditSet.getText().toString().trim().length() == 0) {
                    setResult( RESULT_CANCELED, replyIntent );
                } else {
                    String info = mEditSet.getText().toString();

                    replyIntent.putExtra(EXTRA_REPLY, info);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
