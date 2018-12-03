package edu.fsu.cs.mobile.testdatabase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// Activity to add a user-inputted card to the database
public class NewCardActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.android.cardlistsql.REPLY";
    public static final int OCR_ACTIVITY_REQUEST_CODE = 0;
    private EditText mEditFront, mEditBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        boolean darkModeOn = settings.getBoolean("dark_mode", false);
        if(darkModeOn)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        mEditFront = findViewById(R.id.edit_front);
        mEditBack = findViewById(R.id.edit_back);

        final Button scanButton = findViewById(R.id.camera_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent cameraIntent = new Intent(NewCardActivity.this, OCRActivity.class);
                startActivityForResult(cameraIntent, OCR_ACTIVITY_REQUEST_CODE);
            }
        });

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                String[] info = {mEditFront.getText().toString(),
                                 mEditBack.getText().toString()};

                replyIntent.putExtra(EXTRA_REPLY, info);
                setResult(RESULT_OK, replyIntent);

                finish();
            }
        });
    }


    public void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String[] cardData = data.getStringArrayExtra(OCRActivity.EXTRA_REPLY);

        if( requestCode == OCR_ACTIVITY_REQUEST_CODE ) {
            if( resultCode == RESULT_OK ) {
                mEditFront.setText( cardData[0] );
                mEditBack.setText( cardData[1] );
            }
        }
    }

}
